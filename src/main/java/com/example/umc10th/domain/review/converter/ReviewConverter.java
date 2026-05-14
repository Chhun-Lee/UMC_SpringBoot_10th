package com.example.umc10th.domain.review.converter;

import com.example.umc10th.domain.member.entity.Member;
import com.example.umc10th.domain.mission.entity.Store;
import com.example.umc10th.domain.review.dto.ReviewReqDTO;
import com.example.umc10th.domain.review.dto.ReviewResDTO;
import com.example.umc10th.domain.review.entity.Review;
import com.example.umc10th.domain.review.entity.ReviewPhoto;
import com.example.umc10th.domain.review.enums.ReviewSortType;

import java.util.List;

public class ReviewConverter {

    public static Review toReview(ReviewReqDTO.CreateReviewDTO request, Member member, Store store) {
        return Review.builder()
                .member(member)
                .store(store)
                .star(request.getStar())
                .content(request.getContent())
                .build();
    }

    public static ReviewPhoto toReviewPhoto(Review review, String url) {
        return ReviewPhoto.builder()
                .review(review)
                .url(url)
                .build();
    }

    public static ReviewResDTO.ReviewPhotoDTO toReviewPhotoDTO(ReviewPhoto reviewPhoto) {
        return ReviewResDTO.ReviewPhotoDTO.builder()
                .reviewPhotoId(reviewPhoto.getId())
                .url(reviewPhoto.getUrl())
                .build();
    }

    public static ReviewResDTO.CreateReviewResultDTO toCreateReviewResultDTO(Review review, List<ReviewPhoto> photos) {
        List<ReviewResDTO.ReviewPhotoDTO> photoDTOs = photos.stream()
                .map(ReviewConverter::toReviewPhotoDTO)
                .toList();
        return ReviewResDTO.CreateReviewResultDTO.builder()
                .reviewId(review.getId())
                .shopId(review.getStore().getId())
                .shopName(review.getStore().getShopName())
                .star(review.getStar())
                .content(review.getContent())
                .photos(photoDTOs)
                .build();
    }

    public static ReviewResDTO.MyReviewDTO toMyReviewDTO(Review review) {
        return ReviewResDTO.MyReviewDTO.builder()
                .reviewId(review.getId())
                .shopId(review.getStore().getId())
                .shopName(review.getStore().getShopName())
                .star(review.getStar())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static ReviewResDTO.MyReviewListDTO toMyReviewListDTO(
            List<Review> reviews,
            Long nextCursorId,
            Float nextCursorStar,
            boolean hasNext,
            ReviewSortType sort
    ) {
        List<ReviewResDTO.MyReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewConverter::toMyReviewDTO)
                .toList();
        return ReviewResDTO.MyReviewListDTO.builder()
                .reviews(reviewDTOs)
                .nextCursorId(nextCursorId)
                .nextCursorStar(nextCursorStar)
                .hasNext(hasNext)
                .sort(sort)
                .build();
    }
}
