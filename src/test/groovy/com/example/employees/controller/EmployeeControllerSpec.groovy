package com.example.employees.controller

import com.example.employees.config.AppConfig
import com.example.employees.exceptions.RestExceptionHandler
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
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.hamcrest.collection.IsEmptyCollection.empty

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
    static final String birthdayEndpoint = "/employeesByBirthday"

    @Shared
    List<EmployeeEntity> singleEmployeeResponse

    @Shared
    List<EmployeeEntity> multipleEmployeeResponse

    def setupSpec() {
        singleEmployeeResponse = [new EmployeeEntity("fName", "lName", null, null, LocalDate.now().minusYears(25))]
        multipleEmployeeResponse = [
                new EmployeeEntity("e1", "e1", "555-555-5555", "something@something.com", LocalDate.now().minusYears(20)),
                new EmployeeEntity("e2", "e2", null, "company@something.com", LocalDate.now().minusYears(25)),
                new EmployeeEntity("e3", "e3", null, null, LocalDate.now().minusYears(35))
        ]
    }

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

        then: "Error response is returned"
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Bad Request"))

        where:
        scenario                | employee
        "missing first name"    | new EmployeeDTO(null, "last", null, null, null)
        "missing last name"     | new EmployeeDTO("first", null, null, null, null)
        "incorrect email"       | new EmployeeDTO("first", "last", "not a valid address", null, null)
    }

    @Unroll
    def "Call GET #endpoint endpoint when collection is empty" () {
        given: "Define mock response from service layer"
        Mockito.when(service."$serviceMethod"()).thenReturn([])

        when: "GET employees"
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(endpoint)
                .accept(MediaType.APPLICATION_JSON))

        then: "Request is successful and response has empty items array"
        result.andExpect(status().isOk())
                .andExpect(jsonPath("items", empty()))

        where:
        endpoint                | serviceMethod
        root                    | "getAll"
        birthdayEndpoint        | "getAllWithBirthdayInCurrentMonth"
    }

    def "Call GET #endpoint endpoint when collection #scenario" () {
        given: "Define mock response from service layer"
        Mockito.when(service."$serviceMethod"()).thenReturn(mockedResponse)

        when: "GET employees"
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get(endpoint)
                .accept(MediaType.APPLICATION_JSON))

        then: "Request is successful and expected items are returned"
        result.andExpect(status().isOk())
                .andExpect(jsonPath("items[*].firstName").value(mockedResponse.firstName))

        where:
        mockedResponse << [
                singleEmployeeResponse,
                multipleEmployeeResponse,
                singleEmployeeResponse,
                multipleEmployeeResponse
        ]
        scenario << [
                "has one item",
                "has three items",
                "has one item",
                "has three items"
        ]
        endpoint << [
                root,
                root,
                birthdayEndpoint,
                birthdayEndpoint
        ]
        serviceMethod << [
                "getAll",
                "getAll",
                "getAllWithBirthdayInCurrentMonth",
                "getAllWithBirthdayInCurrentMonth"
        ]
    }

    def "Call DELETE /employees/{id} where request succeeds" () {
        when: "DELETE employee"
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete(root + "/1"))

        then: "Request is successful and response is empty"
        result.andExpect(status().isNoContent())
                .andExpect(jsonPath("\$").doesNotExist())
    }

    def "Call DELETE /employees/{id} with #scenario" () {
        given: "Define exception response from service layer"
        Mockito.when(service.delete(Mockito.isA(Long))).thenThrow(new EmptyResultDataAccessException(1))

        when: "DELETE invalid employee"
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .delete(root + "/" + employeeId))

        then: "Request fails with expected error"
        result.andExpect(status()."$expectedErrorStatus"())
                .andExpect(jsonPath("message").value(expectedError))

        where:
        scenario            | employeeId    | expectedError | expectedErrorStatus
        "a non-numeric ID"  | "invalid"     | "Bad Request" | "isBadRequest"
        "a non-existent ID" | 100000        | "Not Found"   | "isNotFound"
    }
}
