package com.example.employees

import com.example.employees.config.AppConfig
import com.example.employees.controller.EmployeeController
import com.example.employees.controller.RestExceptionHandler
import com.example.employees.model.EmployeeEntity
import com.example.employees.representation.EmployeeDTO
import com.example.employees.service.EmployeeService
import com.example.employees.util.ConvertUtil
import groovy.json.JsonBuilder
import org.hamcrest.core.IsNull
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = EmployeeController)
@Import([EmployeeController, ConvertUtil, RestExceptionHandler])
@ContextConfiguration(classes = AppConfig)
class EmployeeControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ConvertUtil convertUtil

    @MockBean
    EmployeeService service

    static final String root = "/employees"

    def "Call POST /employees endpoint with valid entity" () {
        given: "Define an employee"
        EmployeeDTO employee = new EmployeeDTO("fName", "lName",
                "first.last@company.com", null, null)

        and: "Mock response from service layer"
        EmployeeEntity entity = convertUtil.convertToEntity(employee)
        entity.id = 1234
        Mockito.when(service.create(Mockito.isA(EmployeeEntity)))
                .thenReturn(entity)

        when: "POST to create employee"
        String requestBody = new JsonBuilder(employee).toPrettyString()
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(root)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        result.andDo(MockMvcResultHandlers.print())

        then: "Response is as expected"
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("firstName").value("fName"))
                .andExpect(jsonPath("lastName").value("lName"))
                .andExpect(jsonPath("id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("email").value("first.last@company.com"))
                .andExpect(jsonPath("phoneNumber").doesNotExist())
    }

    def "Call POST /employees endpoint with invalid input: #scenario" () {
        when: "POST to create employee"
        String requestBody = new JsonBuilder(employee).toPrettyString()
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post(root)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        result.andDo(MockMvcResultHandlers.print())

        then: "Correct error response is returned"
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Bad Request"))

        where:
        scenario                | employee
        "missing first name"    | new EmployeeDTO(null, "last", null, null, null)
        "missing last name"     | new EmployeeDTO("first", null, null, null, null)
        "incorrect email"       | new EmployeeDTO("first", "last", "not a valid address", null, null)
    }
}
