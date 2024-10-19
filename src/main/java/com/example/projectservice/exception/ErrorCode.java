package com.example.projectservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE("잘못된 입력 값입니다.", 400),

    MEMBER_IS_DELETED("해당 회원은 탈퇴한 회원입니다.", 401),
    ALREADY_COMPLETED_PROJECT("이미 완료된 프로젝트입니다.", 401),

    FORBIDDEN("권한이 없습니다.", 403),

    MEMBER_NOT_FOUND("해당 회원을 찾을 수 없습니다.", 404),
    PROJECT_NOT_FOUND("해당 프로젝트를 찾을 수 없습니다.", 404),

    INTERNAL_SERVER_ERROR("서버에 문제가 발생했습니다.", 500),
    ;

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
