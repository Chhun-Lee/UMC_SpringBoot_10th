package com.example.umc10th.domain.review.repository;

import com.example.umc10th.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
            SELECT r FROM Review r
            JOIN FETCH r.store s
            WHERE r.member.id = :memberId
              AND (:cursorId IS NULL OR r.id < :cursorId)
            ORDER BY r.id DESC
            """)
    List<Review> findMyReviewsOrderById(
            @Param("memberId") Long memberId,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("""
            SELECT r FROM Review r
            JOIN FETCH r.store s
            WHERE r.member.id = :memberId
              AND (
                :cursorStar IS NULL
                OR r.star < :cursorStar
                OR (r.star = :cursorStar AND r.id < :cursorId)
              )
            ORDER BY r.star DESC, r.id DESC
            """)
    List<Review> findMyReviewsOrderByStar(
            @Param("memberId") Long memberId,
            @Param("cursorStar") Float cursorStar,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}
