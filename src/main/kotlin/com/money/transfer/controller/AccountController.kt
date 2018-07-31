package com.money.transfer.controller

import com.money.transfer.api.AccountServiceApi
import com.money.transfer.service.IAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
class AccountController : AccountServiceApi {

    @Autowired lateinit var accountService : IAccountService

    override fun get(id: Long) = accountService.get(id)

    override fun create() = accountService.create()

    override fun addMoney(id: Long, amount: BigDecimal) = accountService.add(id, amount)

    override fun withdrawMoney(id: Long, amount: BigDecimal) = accountService.withdraw(id, amount)

    override fun transferMoney(from: Long, to: Long, amount: BigDecimal) = accountService.transfer(from, to, amount)

}