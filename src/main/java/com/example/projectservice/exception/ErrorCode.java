package com.example.projectservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    MUST_NEED_POSITION("포지션을 선택해주세요.", 400),
    MUST_NEED_KEYWORD("검색어를 입력해주세요.", 400),

    MEMBER_IS_DELETED("해당 회원은 탈퇴한 회원입니다.", 401),
    ALREADY_COMPLETED_PROJECT("이미 완료된 프로젝트입니다.", 401),
    MEMBER_ALREADY_EXISTS("이미 존재하는 회원입니다.", 401),
    ALREADY_LEADER("이미 리더인 회원입니다.", 401),
    CANNOT_DELETE_LEADER("리더는 삭제할 수 없습니다. 프로젝트를 삭제해주세요", 401),
    ALREADY_RESOLVED_RECRUIT("이미 처리된 지원요청입니다.", 401),

    FORBIDDEN("권한이 없습니다.", 403),

    MEMBER_NOT_FOUND("해당 회원을 찾을 수 없습니다.", 404),
    PROJECT_NOT_FOUND("해당 프로젝트를 찾을 수 없습니다.", 404),
    PROFILE_NOT_FOUND("해당 프로필을 찾을 수 없습니다.", 404),
    PROJECT_RECRUIT_NOT_FOUND("해당 프로젝트 지원자를 찾을 수 없습니다.", 404),

    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다.", 500),
    ;

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
