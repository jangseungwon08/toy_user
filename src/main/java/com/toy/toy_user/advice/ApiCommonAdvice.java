package com.toy.toy_user.advice;


import com.toy.toy_user.common.dto.ApiResponseDto;
import com.toy.toy_user.common.exception.AlreadyExists;
import com.toy.toy_user.common.exception.BadParameter;
import com.toy.toy_user.common.exception.ClientError;
import com.toy.toy_user.common.exception.NotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/*
📜공통 응답 Advice 구현
공통 Advice를 구현함으로써 중복 Try Catch제거, 동일한 ApiResponse, 예외처리 분리 등으로 사용함으로써 용이하게 사용가능하다.
* @RestControllerAdvice
RestController에서 발생하는 예외처리를 가로채서 중앙에서 처리할 수 있게끔 해주는 Advice로 구현해줄 수 있는 어노테이션이다.
@order(value=1)
*
* */

@Slf4j
@Order(value = 1)
@RestControllerAdvice
public class ApiCommonAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ClientError.class})
    public ApiResponseDto<String> handleClientError(ClientError e) {
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    //    모든 예외를 받을 수 있는 핸들러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ApiResponseDto<String> handleException(Exception e) {
        log.error("Unhandled server error occurred: {}", e.getMessage(), e);
        return ApiResponseDto.createError(
                "serverError",
                "서버 에러입니다.");
    }

    //    리소스 foundexception
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class})
    public ApiResponseDto<String> handleNoResourceFoundException(NoResourceFoundException e) {
        e.printStackTrace();
        return ApiResponseDto.createError(
                "No Resource",
                "잘못된 URL입니다."
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadParameter.class})
    public ApiResponseDto<String> handleBadParameter(BadParameter e) {
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFound.class})
    public ApiResponseDto<String> handleNotFound(NotFound e) {
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    //    충동을 의미하는 409 에러로 변경
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({AlreadyExists.class})
    public ApiResponseDto<String> handleAlreadyExists(AlreadyExists e) {
        return ApiResponseDto.createError(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }
}
