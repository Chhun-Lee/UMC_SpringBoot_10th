package com.example.umc10th.domain.member.service;

import com.example.umc10th.domain.member.dto.MemberReqDTO;
import com.example.umc10th.domain.member.dto.MemberResDTO;

public interface MemberService {

    MemberResDTO.SignUpResultDTO signUp(MemberReqDTO.SignUpDTO request);

    MemberResDTO.LoginResultDTO login(MemberReqDTO.LoginDTO request);

    MemberResDTO.HomeDTO getHome(Long memberId, Long locationId);

    MemberResDTO.MemberMissionListDTO getMemberMissions(Long memberId, String status, Long cursor, Integer size);
}
