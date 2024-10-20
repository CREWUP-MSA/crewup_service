package com.example.projectservice.dto.client.mapper;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.exception.CustomException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.service.client.MemberServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class MemberClientMapper {

    private final MemberServiceClient memberServiceClient;

    /**
     * 멤버 조회 - By ID
     * @param id 멤버 ID
     * @return MemberResponse
     * @throws CustomException 멤버를 찾을 수 없는 경우
     * @throws CustomException 멤버 서비스 에러
     * @see ErrorCode
     */
    public MemberResponse getMemberById(Long id) {
        try{
            MemberResponse memberResponse = memberServiceClient.getMemberById(id).data();
            validateMemberDeleted(memberResponse);
            return memberResponse;

        } catch (FeignException e){
            throw handleFeignException(e);
        }
    }

    /**
     * 회원 삭제 여부 확인
     * @param memberResponse 회원 정보
     * @throws CustomException 회원이 삭제된 경우
     */
    private void validateMemberDeleted(MemberResponse memberResponse) {
        if (memberResponse.isDeleted())
            throw new CustomException(ErrorCode.MEMBER_IS_DELETED);
    }

    /**
     * FeignException 처리
     * @param e FeignException
     * @return CustomException (MEMBER_NOT_FOUND, INTERNAL_SERVER_ERROR)
     */
    private CustomException handleFeignException(FeignException e){
        if(e instanceof FeignException.NotFound){
            log.error("FROM Member-Service : Member not found");
            return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        } else {
            log.error("FROM Member-Service : Member service error");
            return new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
