package com.money.transfer.integration

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.github.springtestdbunit.annotation.DatabaseOperation.*
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import com.money.transfer.TransferApplicationTests
import com.money.transfer.integration.AccountServiceIT.Companion.DATASET
import com.money.transfer.model.Account
import com.money.transfer.model.AccountTransferPair
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@DatabaseSetup(type = CLEAN_INSERT, value = DATASET)
@DatabaseTearDown(type = DELETE_ALL, value = DATASET)
class AccountServiceIT : TransferApplicationTests() {

    @Test
    fun getAccountOk() {
        val id = 1L
        val account = execRequestWithResult(ACC_TYPE, get(GET_ACCOUNT, id))
        assertEquals(id, account.id)
        assertEquals(100, account.balance.intValueExact())
    }

    @Test
    fun getAccountNotExistingIdFail() {
        execRequestExpectedFail(get(GET_ACCOUNT, Long.MAX_VALUE))
    }

    @Test
    fun createNewAccountOk() {
        val account = execRequestWithResult(ACC_TYPE, post(PREFIX))
        assertEquals(6L, account.id)
        assertEquals(0, account.balance.intValueExact())
    }

    @Test
    fun addMoneyOk() {
        val id = 2L
        val account = execRequestWithResult(ACC_TYPE, put(ADD_MONEY, id, 100))
        assertEquals(id, account.id)
        assertEquals(300, account.balance.intValueExact())
    }

    @Test
    fun addMoneyWrongAmountTypeFail() {
        execRequestExpectedFail(put(ADD_MONEY, 3L, "abc"))
    }

    @Test
    fun addMoneyNotExistingIdFail() {
        execRequestExpectedFail(put(ADD_MONEY, Long.MAX_VALUE, 100))
    }

    @Test
    fun addMoneyNegativeAmountFail() {
        execRequestExpectedFail(put(ADD_MONEY, 2L, -100))
    }

    @Test
    fun withdrawMoneyOk() {
        val id = 4L
        val account = execRequestWithResult(ACC_TYPE, put(WITHDRAW_MONEY, id, 200))
        assertEquals(id, account.id)
        assertEquals(200, account.balance.intValueExact())
    }

    @Test
    fun withdrawAllMoneyOk() {
        val id = 1L
        val account = execRequestWithResult(ACC_TYPE, put(WITHDRAW_MONEY, id, 100))
        assertEquals(id, account.id)
        assertEquals(0, account.balance.intValueExact())
    }

    @Test
    fun withdrawMoneyWrongAmountTypeFail() {
        execRequestExpectedFail(put(WITHDRAW_MONEY, 3L, "abc"))
    }

    @Test
    fun withdrawMoneyInsufficientFundsFail() {
        execRequestExpectedFail(put(WITHDRAW_MONEY, 1L, 300))
    }

    @Test
    fun withdrawMoneyNotExistingIdFail() {
        execRequestExpectedFail(put(WITHDRAW_MONEY, Long.MAX_VALUE, 100))
    }

    @Test
    fun withdrawMoneyNegativeAmountFail() {
        execRequestExpectedFail(put(WITHDRAW_MONEY, 2L, -100))
    }

    @Test
    fun transferMoneyOk() {
        val from = 4L
        val to = 2L
        val accountsPair = execRequestWithResult(ACC_PAIR_TYPE, patch(TRANSFER_MONEY, from, to, 100))
        assertEquals(from, accountsPair.from.id)
        assertEquals(300, accountsPair.from.balance.intValueExact())
        assertEquals(to, accountsPair.to.id)
        assertEquals(300, accountsPair.to.balance.intValueExact())
    }

    @Test
    fun transferMoneyInsufficientFundsFail() {
        execRequestExpectedFail(patch(TRANSFER_MONEY, 1L, 2L, 200))
    }

    @Test
    fun transferMoneyNotExistingIdFail() {
        execRequestExpectedFail(patch(TRANSFER_MONEY, 1L, Long.MIN_VALUE, 200))
    }

    @Test
    fun transferMoneyWrongAmountTypeFail() {
        execRequestExpectedFail(patch(TRANSFER_MONEY, 4L, 1L, "abc"))
    }

    @Test
    fun transferMoneyNegativeAmountFail() {
        execRequestExpectedFail(patch(TRANSFER_MONEY, 4L, 1L, -100))
    }

    companion object {
        const val DATASET = "classpath:datasets/accounts.xml"
        const val PREFIX = "/account"
        const val GET_ACCOUNT = "$PREFIX/{id}"
        const val ADD_MONEY = "$PREFIX/{id}/add/{amount}"
        const val WITHDRAW_MONEY = "$PREFIX/{id}/withdraw/{amount}"
        const val TRANSFER_MONEY = "$PREFIX/{from}/transfer/{to}/{amount}"
        val ACC_TYPE = jacksonTypeRef<Account>()
        val ACC_PAIR_TYPE = jacksonTypeRef<AccountTransferPair>()
    }
}
