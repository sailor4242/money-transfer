package com.money.transfer.controller

import com.money.transfer.exceptions.AccountServiceException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestController
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(AccountServiceException::class)
    fun defaultHandling(ex: AccountServiceException) = ResponseEntity(ex.message, BAD_REQUEST)
}