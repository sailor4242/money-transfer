package com.money.transfer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestPropertySource(locations = ["classpath:test.properties"])
@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@TestExecutionListeners(TransactionDbUnitTestExecutionListener::class, DependencyInjectionTestExecutionListener::class)
class TransferApplicationTests {

    @Autowired lateinit var mvc: MockMvc

    @Test
    fun contextLoads() {}

    @Throws(Exception::class)
    protected fun <T> execRequestWithResult(typeReference: TypeReference<T>, request: RequestBuilder): T {
        val mvcResultSave = mvc.perform(request).andExpect(status().is2xxSuccessful).andReturn()
        val responseJsonSave = mvcResultSave.response.contentAsString
        return jacksonObjectMapper().readValue(responseJsonSave, typeReference)
    }

    @Throws(Exception::class)
    protected fun execRequestExpectedFail(request: RequestBuilder) {
        mvc.perform(request).andExpect(status().is4xxClientError)
    }

}