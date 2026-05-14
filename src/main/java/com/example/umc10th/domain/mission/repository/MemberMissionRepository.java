package com.example.umc10th.domain.mission.repository;

import com.example.umc10th.domain.mission.entity.mapping.MemberMission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberMissionRepository extends JpaRepository<MemberMission, Long> {

    int countByMemberIdAndIsCompleted(Long memberId, Boolean isCompleted);

    int countByMemberId(Long memberId);

    @Query("""
            SELECT mm FROM MemberMission mm
            JOIN FETCH mm.mission m
            JOIN FETCH m.store s
            WHERE mm.member.id = :memberId
              AND mm.isCompleted = :isCompleted
              AND (:cursor IS NULL OR mm.id < :cursor)
            ORDER BY mm.id DESC
            """)
    List<MemberMission> findByMemberIdAndIsCompletedWithCursor(
            @Param("memberId") Long memberId,
            @Param("isCompleted") Boolean isCompleted,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"mission", "mission.store"})
    Page<MemberMission> findByMemberIdAndIsCompletedFalse(Long memberId, Pageable pageable);
}
