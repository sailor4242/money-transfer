package com.money.transfer.api

import com.money.transfer.model.Account
import com.money.transfer.model.AccountTransferPair
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RequestMapping("/account")
interface AccountServiceApi {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Account

    @PostMapping
    @ResponseStatus(code = CREATED)
    fun create(): Account

    @PutMapping("/{id}/add/{amount}")
    fun addMoney(@PathVariable id: Long, @PathVariable amount: BigDecimal): Account

    @PutMapping("/{id}/withdraw/{amount}")
    fun withdrawMoney(@PathVariable id: Long, @PathVariable amount: BigDecimal): Account

    @PatchMapping("/{from}/transfer/{to}/{amount}")
    fun transferMoney(@PathVariable from: Long,
                      @PathVariable to: Long,
                      @PathVariable amount: BigDecimal): AccountTransferPair
}
