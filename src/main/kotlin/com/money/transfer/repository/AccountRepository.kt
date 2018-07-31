package com.money.transfer.repository

import com.money.transfer.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, Long> {
    fun getById(id: Long): Optional<Account>
}