package com.example.umc10th.domain.review.service;

import com.example.umc10th.domain.review.dto.ReviewReqDTO;
import com.example.umc10th.domain.review.dto.ReviewResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {

    ReviewResDTO.CreateReviewResultDTO createReview(
            Long memberId, Long shopId, ReviewReqDTO.CreateReviewDTO request, List<MultipartFile> images
    );
}
