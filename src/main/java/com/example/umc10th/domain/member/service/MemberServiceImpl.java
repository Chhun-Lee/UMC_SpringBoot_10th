package com.example.umc10th.domain.member.service;

import com.example.umc10th.domain.member.converter.MemberConverter;
import com.example.umc10th.domain.member.dto.MemberReqDTO;
import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.domain.member.entity.Food;
import com.example.umc10th.domain.member.entity.Member;
import com.example.umc10th.domain.member.entity.Term;
import com.example.umc10th.domain.member.entity.mapping.MemberFood;
import com.example.umc10th.domain.member.entity.mapping.MemberTerm;
import com.example.umc10th.domain.member.exception.MemberException;
import com.example.umc10th.global.code.status.MemberErrorCode;
import com.example.umc10th.domain.member.repository.FoodRepository;
import com.example.umc10th.domain.member.repository.MemberFoodRepository;
import com.example.umc10th.domain.member.repository.MemberRepository;
import com.example.umc10th.domain.member.repository.MemberTermRepository;
import com.example.umc10th.domain.member.repository.TermRepository;
import com.example.umc10th.domain.mission.entity.Location;
import com.example.umc10th.domain.mission.entity.mapping.MemberMission;
import com.example.umc10th.domain.mission.exception.MissionException;
import com.example.umc10th.global.code.status.MissionErrorCode;
import com.example.umc10th.domain.mission.repository.LocationRepository;
import com.example.umc10th.domain.mission.repository.MemberMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FoodRepository foodRepository;
    private final TermRepository termRepository;
    private final MemberFoodRepository memberFoodRepository;
    private final MemberTermRepository memberTermRepository;
    private final LocationRepository locationRepository;
    private final MemberMissionRepository memberMissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberResDTO.SignUpResultDTO signUp(MemberReqDTO.SignUpDTO request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = memberRepository.save(MemberConverter.toMember(request, encodedPassword));

        if (request.getPreferFoodIds() != null) {
            for (Long foodId : request.getPreferFoodIds()) {
                Food food = foodRepository.findById(foodId)
                        .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
                memberFoodRepository.save(MemberFood.builder()
                        .member(member)
                        .food(food)
                        .build());
            }
        }

        if (request.getTermIds() != null) {
            for (Long termId : request.getTermIds()) {
                Term term = termRepository.findById(termId)
                        .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
                memberTermRepository.save(MemberTerm.builder()
                        .member(member)
                        .term(term)
                        .build());
            }
        }

        return MemberConverter.toSignUpResultDTO(member);
    }

    @Override
    public MemberResDTO.HomeDTO getHome(Long memberId, Long locationId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Location location = null;
        if (locationId != null) {
            location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new MemberException(MemberErrorCode.LOCATION_NOT_FOUND));
        }

        int completed = memberMissionRepository.countByMemberIdAndIsCompleted(memberId, true);
        int total = memberMissionRepository.countByMemberId(memberId);

        return MemberConverter.toHomeDTO(member, location, completed, total);
    }

    @Override
    public MemberResDTO.MemberMissionListDTO getMemberMissions(
            Long memberId, String status, Long cursor, Integer size
    ) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Boolean isCompleted = parseStatus(status);
        Pageable pageable = PageRequest.of(0, size + 1);

        List<MemberMission> fetched = memberMissionRepository
                .findByMemberIdAndIsCompletedWithCursor(memberId, isCompleted, cursor, pageable);

        boolean hasNext = fetched.size() > size;
        List<MemberMission> page = hasNext ? fetched.subList(0, size) : fetched;
        Long nextCursor = page.isEmpty() ? null : page.get(page.size() - 1).getId();

        return MemberConverter.toMemberMissionListDTO(page, nextCursor, hasNext);
    }

    private Boolean parseStatus(String status) {
        if (status == null) {
            throw new MissionException(MissionErrorCode.INVALID_MISSION_STATUS);
        }
        return switch (status.toUpperCase()) {
            case "ACTIVE" -> false;
            case "COMPLETED" -> true;
            default -> throw new MissionException(MissionErrorCode.INVALID_MISSION_STATUS);
        };
    }
}
