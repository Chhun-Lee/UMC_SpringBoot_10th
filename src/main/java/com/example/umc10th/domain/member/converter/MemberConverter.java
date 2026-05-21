package com.example.umc10th.domain.member.converter;

import com.example.umc10th.domain.member.dto.MemberReqDTO;
import com.example.umc10th.domain.member.dto.MemberResDTO;
import com.example.umc10th.domain.member.entity.Member;
import com.example.umc10th.domain.mission.entity.Location;
import com.example.umc10th.domain.mission.entity.mapping.MemberMission;

import java.util.List;

public class MemberConverter {

    public static Member toMember(MemberReqDTO.SignUpDTO request, String encodedPassword) {
        return Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .gender(request.getGender())
                .birth(request.getBirth())
                .address(request.getAddress())
                .addressDetail(request.getAddressDetail())
                .phoneNumber(request.getPhoneNumber())
                .point(0)
                .build();
    }

    public static MemberResDTO.SignUpResultDTO toSignUpResultDTO(Member member) {
        return MemberResDTO.SignUpResultDTO.builder()
                .memberId(member.getId())
                .build();
    }

    public static MemberResDTO.HomeDTO toHomeDTO(Member member, Location location, int completed, int total) {
        return MemberResDTO.HomeDTO.builder()
                .memberName(member.getName())
                .region(location != null ? location.getLocationName() : null)
                .point(member.getPoint())
                .missionProgress(MemberResDTO.MissionProgressDTO.builder()
                        .completed(completed)
                        .total(total)
                        .build())
                .build();
    }

    public static MemberResDTO.MemberMissionDTO toMemberMissionDTO(MemberMission memberMission) {
        return MemberResDTO.MemberMissionDTO.builder()
                .memberMissionId(memberMission.getId())
                .missionId(memberMission.getMission().getId())
                .shopName(memberMission.getMission().getStore().getShopName())
                .point(memberMission.getMission().getPoint())
                .condition(memberMission.getMission().getCondition())
                .deadline(memberMission.getMission().getDeadline())
                .isCompleted(memberMission.getIsCompleted())
                .build();
    }

    public static MemberResDTO.MemberMissionListDTO toMemberMissionListDTO(
            List<MemberMission> memberMissions, Long nextCursor, boolean hasNext) {
        List<MemberResDTO.MemberMissionDTO> missionDTOs = memberMissions.stream()
                .map(MemberConverter::toMemberMissionDTO)
                .toList();
        return MemberResDTO.MemberMissionListDTO.builder()
                .missions(missionDTOs)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
