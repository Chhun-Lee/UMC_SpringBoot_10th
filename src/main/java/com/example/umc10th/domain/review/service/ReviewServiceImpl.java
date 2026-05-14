package com.example.umc10th.domain.review.service;

import com.example.umc10th.domain.member.entity.Member;
import com.example.umc10th.domain.member.exception.MemberException;
import com.example.umc10th.global.code.status.MemberErrorCode;
import com.example.umc10th.domain.member.repository.MemberRepository;
import com.example.umc10th.domain.mission.entity.Store;
import com.example.umc10th.domain.mission.repository.StoreRepository;
import com.example.umc10th.domain.review.converter.ReviewConverter;
import com.example.umc10th.domain.review.dto.ReviewReqDTO;
import com.example.umc10th.domain.review.dto.ReviewResDTO;
import com.example.umc10th.domain.review.entity.Review;
import com.example.umc10th.domain.review.entity.ReviewPhoto;
import com.example.umc10th.domain.review.enums.ReviewSortType;
import com.example.umc10th.domain.review.exception.ReviewException;
import com.example.umc10th.global.code.status.ReviewErrorCode;
import com.example.umc10th.domain.review.repository.ReviewPhotoRepository;
import com.example.umc10th.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public ReviewResDTO.CreateReviewResultDTO createReview(
            Long memberId, Long shopId, ReviewReqDTO.CreateReviewDTO request, List<MultipartFile> images
    ) {
        validateStar(request.getStar());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Store store = storeRepository.findById(shopId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.SHOP_NOT_FOUND));

        Review review = reviewRepository.save(ReviewConverter.toReview(request, member, store));

        List<ReviewPhoto> photos = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                if (image == null || image.isEmpty()) {
                    continue;
                }
                String url = uploadImage(image);
                photos.add(reviewPhotoRepository.save(ReviewConverter.toReviewPhoto(review, url)));
            }
        }

        return ReviewConverter.toCreateReviewResultDTO(review, photos);
    }

    private void validateStar(Float star) {
        if (star == null || star < 0.5f || star > 5.0f || (star * 2) % 1 != 0) {
            throw new ReviewException(ReviewErrorCode.INVALID_STAR_RATING);
        }
    }

    private String uploadImage(MultipartFile image) {
        // TODO: 실제 파일 스토리지(S3 등) 연동 시 업로드 후 반환된 URL을 사용하도록 변경
        String original = image.getOriginalFilename();
        if (original == null || original.isBlank()) {
            throw new ReviewException(ReviewErrorCode.PHOTO_UPLOAD_FAILED);
        }
        return "/uploads/reviews/" + original;
    }

    @Override
    public ReviewResDTO.MyReviewListDTO getMyReviews(
            Long memberId, ReviewSortType sort, Long cursorId, Float cursorStar, Integer size
    ) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(0, size + 1);
        List<Review> fetched = (sort == ReviewSortType.STAR)
                ? reviewRepository.findMyReviewsOrderByStar(memberId, cursorStar, cursorId, pageable)
                : reviewRepository.findMyReviewsOrderById(memberId, cursorId, pageable);

        boolean hasNext = fetched.size() > size;
        List<Review> page = hasNext ? fetched.subList(0, size) : fetched;

        Long nextCursorId = null;
        Float nextCursorStar = null;
        if (!page.isEmpty()) {
            Review last = page.get(page.size() - 1);
            nextCursorId = last.getId();
            if (sort == ReviewSortType.STAR) {
                nextCursorStar = last.getStar();
            }
        }

        return ReviewConverter.toMyReviewListDTO(page, nextCursorId, nextCursorStar, hasNext, sort);
    }
}
