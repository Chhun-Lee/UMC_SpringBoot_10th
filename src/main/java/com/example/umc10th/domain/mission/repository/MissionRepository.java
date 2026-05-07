package com.example.umc10th.domain.mission.repository;

import com.example.umc10th.domain.mission.entity.Mission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Query("""
            SELECT m FROM Mission m
            JOIN FETCH m.store s
            WHERE (:locationId IS NULL OR s.location.id = :locationId)
              AND m.id NOT IN (
                  SELECT mm.mission.id FROM MemberMission mm
                  WHERE mm.member.id = :memberId
              )
              AND (:cursor IS NULL OR m.id < :cursor)
            ORDER BY m.id DESC
            """)
    List<Mission> findAvailableMissions(
            @Param("memberId") Long memberId,
            @Param("locationId") Long locationId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
