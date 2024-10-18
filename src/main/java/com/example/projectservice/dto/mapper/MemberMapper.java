package com.example.projectservice.dto.mapper;

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
public class MemberMapper {

    private final MemberServiceClient memberServiceClient;

    /**
     * 멤버 조회 - By ID Mapper
     * @param id 멤버 ID
     * @return MemberResponse
     * @throws CustomException 멤버를 찾을 수 없는 경우
     * @throws CustomException 멤버 서비스 에러
     * @see ErrorCode
     */
    public MemberResponse getMemberById(Long id) {
        try{
            return memberServiceClient.getMemberById(id).data();

        } catch (FeignException.NotFound e){
            log.error("FROM Member-Service : Member not found");
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);

        } catch (FeignException e){
            log.error("FROM Member-Service : Member service error");
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
