package com.money.transfer.model

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import javax.persistence.*

@Entity
@Table(name = "accounts")
data class Account(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                   var id : Long = 0,
                   @Column(name = "balance")
                   var balance : BigDecimal = ZERO)

data class AccountTransferPair(var from: Account, var to: Account)