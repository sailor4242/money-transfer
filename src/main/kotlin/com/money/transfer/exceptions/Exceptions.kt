package com.money.transfer.exceptions

open class AccountServiceException(override val message: String) : RuntimeException()

class AccountNotFoundException: AccountServiceException("Account not found")

class InsufficientFundsException: AccountServiceException("Insufficient funds")