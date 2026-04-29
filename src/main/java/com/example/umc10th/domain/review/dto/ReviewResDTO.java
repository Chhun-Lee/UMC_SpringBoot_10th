package com.example.umc10th.domain.review.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
