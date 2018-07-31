package com.money.transfer.service

import com.money.transfer.exceptions.AccountNotFoundException
import com.money.transfer.exceptions.AccountServiceException
import com.money.transfer.exceptions.InsufficientFundsException
import com.money.transfer.model.Account
import com.money.transfer.model.AccountTransferPair
import com.money.transfer.repository.AccountRepository
import com.money.transfer.utils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation.REPEATABLE_READ
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

interface IAccountService {
    /**
     * Retrieve account representation from db, throws NotFoundException if not present
     */
    fun get(id: Long): Account
    /**
     * Create new account with zero balance
     */
    fun create(): Account
    /**
     * Update account entity with new balance
     */
    fun update(acc: Account): Account
    /**
     * Withdraw money amount from account, throws InsufficientFundsException if balance is not enough
     */
    fun withdraw(id: Long, amount: BigDecimal): Account
    /**
     * Add money amount to account
     */
    fun add(id: Long, amount : BigDecimal): Account
    /**
     * Transfer money amount between accounts, throws InsufficientFundsException if "from" account balance is not enough
     */
    fun transfer(from: Long, to: Long, amount: BigDecimal): AccountTransferPair
}

@Service
class AccountService : IAccountService {

    private val log by logger()

    @Autowired private lateinit var accountRepository: AccountRepository

    @Transactional(readOnly = true)
    override fun get(id: Long) = accountRepository.getById(id).orElseThrow { AccountNotFoundException() }

    @Transactional
    override fun create() = accountRepository.save(Account())

    @Transactional(isolation = REPEATABLE_READ)
    override fun update(acc: Account) = accountRepository.save(acc)

    @Transactional(isolation = REPEATABLE_READ)
    override fun withdraw(id: Long, amount: BigDecimal): Account {
        log.info("Request to withdraw from $id amount $amount")
        amount.validateIsNegative()
        val account = get(id)
        account.balance -= amount
        return if (account.balance >= ZERO) accountRepository.save(account) else throw InsufficientFundsException()
    }

    @Transactional(isolation = REPEATABLE_READ)
    override fun add(id: Long, amount: BigDecimal): Account {
        log.info("Request to add to $id amount $amount")
        amount.validateIsNegative()
        val account = get(id)
        account.balance += amount
        return accountRepository.save(account)
    }

    @Transactional(isolation = REPEATABLE_READ)
    override fun transfer(from: Long, to: Long, amount: BigDecimal): AccountTransferPair {
        log.info("Request to transfer from $from to $to amount $amount")
        amount.validateIsNegative()
        val accountFrom = get(from)
        val accountTo = get(to)
        accountFrom.balance -= amount
        return when {
            accountFrom.balance < ZERO -> throw InsufficientFundsException()
            else -> {
                accountTo.balance += amount
                AccountTransferPair(update(accountFrom), update(accountTo))
            }
        }
    }

    private fun BigDecimal.validateIsNegative() =
            if (this < ZERO) throw AccountServiceException("Illegal argument") else {}

}