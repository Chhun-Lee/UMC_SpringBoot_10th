package com.example.umc10th.domain.review.controller;

import com.example.umc10th.domain.review.dto.ReviewReqDTO;
import com.example.umc10th.domain.review.dto.ReviewResDTO;
import com.example.umc10th.global.code.status.ReviewSuccessCode;
import com.example.umc10th.domain.review.service.ReviewService;
import com.example.umc10th.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Review", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shops")
public class ReviewController {

    private final ReviewService reviewService;

    // TODO: 인증 도입 후 SecurityContext에서 memberId를 가져오도록 변경
    private static final Long TEMP_MEMBER_ID = 1L;

    @Operation(summary = "리뷰 작성", description = "특정 가게에 별점/내용/사진을 포함한 리뷰를 작성합니다.")
    @PostMapping(value = "/{shopId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<ReviewResDTO.CreateReviewResultDTO>> createReview(
            @PathVariable Long shopId,
            @RequestPart("request") ReviewReqDTO.CreateReviewDTO request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        ReviewResDTO.CreateReviewResultDTO result =
                reviewService.createReview(TEMP_MEMBER_ID, shopId, request, images);
        return CustomResponse.ok(ReviewSuccessCode.REVIEW_CREATED, result);
    }
}
