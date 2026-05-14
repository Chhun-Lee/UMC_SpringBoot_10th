package com.example.umc10th.domain.review.controller;

import com.example.umc10th.domain.review.dto.ReviewResDTO;
import com.example.umc10th.domain.review.enums.ReviewSortType;
import com.example.umc10th.domain.review.service.ReviewService;
import com.example.umc10th.global.apiPayload.CustomResponse;
import com.example.umc10th.global.code.status.ReviewSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MyReview", description = "내 리뷰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class MyReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "내가 작성한 리뷰 목록 조회",
            description = "특정 회원이 작성한 리뷰 목록을 커서 페이지네이션으로 조회합니다. 정렬 기준은 ID 또는 STAR.")
    @GetMapping("/my")
    public ResponseEntity<CustomResponse<ReviewResDTO.MyReviewListDTO>> getMyReviews(
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "ID") ReviewSortType sort,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false) Float cursorStar,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        ReviewResDTO.MyReviewListDTO result =
                reviewService.getMyReviews(memberId, sort, cursorId, cursorStar, size);
        return CustomResponse.ok(ReviewSuccessCode.MY_REVIEW_LIST_OK, result);
    }
}
