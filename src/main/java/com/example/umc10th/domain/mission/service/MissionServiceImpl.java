package com.example.umc10th.domain.mission.service;

import com.example.umc10th.domain.member.exception.MemberException;
import com.example.umc10th.global.code.status.MemberErrorCode;
import com.example.umc10th.domain.member.repository.MemberRepository;
import com.example.umc10th.domain.mission.converter.MissionConverter;
import com.example.umc10th.domain.mission.dto.MissionResDTO;
import com.example.umc10th.domain.mission.entity.Mission;
import com.example.umc10th.domain.mission.entity.mapping.MemberMission;
import com.example.umc10th.domain.mission.exception.MissionException;
import com.example.umc10th.global.code.status.MissionErrorCode;
import com.example.umc10th.domain.mission.repository.MemberMissionRepository;
import com.example.umc10th.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MemberMissionRepository memberMissionRepository;
    private final MemberRepository memberRepository;

    @Override
    public MissionResDTO.AvailableMissionListDTO getAvailableMissions(
            Long memberId, Long locationId, Long cursor, Integer size
    ) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(0, size + 1);
        List<Mission> fetched = missionRepository
                .findAvailableMissions(memberId, locationId, cursor, pageable);

        boolean hasNext = fetched.size() > size;
        List<Mission> page = hasNext ? fetched.subList(0, size) : fetched;
        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();

        return MissionConverter.toAvailableMissionListDTO(page, nextCursor, hasNext);
    }

    @Override
    @Transactional
    public MissionResDTO.MissionCompleteResultDTO completeMission(Long memberMissionId) {
        MemberMission memberMission = memberMissionRepository.findById(memberMissionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.MEMBER_MISSION_NOT_FOUND));

        if (Boolean.TRUE.equals(memberMission.getIsCompleted())) {
            throw new MissionException(MissionErrorCode.MISSION_ALREADY_COMPLETED);
        }

        memberMission.complete();

        return MissionConverter.toMissionCompleteResultDTO(memberMission);
    }
}
