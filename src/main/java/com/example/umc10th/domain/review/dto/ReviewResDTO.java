package com.example.umc10th.domain.review.dto;

import com.example.umc10th.domain.review.enums.ReviewSortType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateReviewResultDTO {
        private Long reviewId;
        private Long shopId;
        private String shopName;
        private Float star;
        private String content;
        private List<ReviewPhotoDTO> photos;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ReviewPhotoDTO {
        private Long reviewPhotoId;
        private String url;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyReviewDTO {
        private Long reviewId;
        private Long shopId;
        private String shopName;
        private Float star;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyReviewListDTO {
        private List<MyReviewDTO> reviews;
        private Long nextCursorId;
        private Float nextCursorStar;
        private Boolean hasNext;
        private ReviewSortType sort;
    }
}
