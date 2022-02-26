package com.example.employees

import com.example.employees.model.EmployeeEntity
import com.example.employees.service.EmployeeService
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.text.DateFormat
import java.text.SimpleDateFormat

@DataJpaTest
@Import(EmployeeService)
class EmployeeServiceSpec extends Specification {

    @Autowired
    EmployeeService service

    @Shared
    List<EmployeeEntity> testEntities

    @Shared
    List<EmployeeEntity> matchingEntities

    def setupSpec() {
        DateFormat formatter = new SimpleDateFormat("MM/dd/YYYY")
        testEntities = [
                new EmployeeEntity("Bruce", "Wayne", "Bruce.Wayne@WayneCorp.com",
                        "555-123-3456", DateUtils.addYears(DateUtils.addMonths(new Date(), 1), -30)),
                new EmployeeEntity("Lex", "Luthor", "Lex.Luthor@LexCorp.com",
                        "123-456-7891", DateUtils.addYears(DateUtils.addMonths(new Date(), 2), -50))
        ]
        matchingEntities = [
                new EmployeeEntity("Should", "Match", "Should.Match@Company.com",
                        "456-789-1011", DateUtils.addYears(new Date(), -20)),
                new EmployeeEntity("ShouldMatch", "Also", null, null,
                        DateUtils.addYears(new Date(), -30))
        ]
        testEntities.addAll(matchingEntities)
    }

    @Unroll
    def "Create employee with #scenario" () {
        when: "Try to create new employee"
        EmployeeEntity responseEntity = service.create(entity)

        then: "Entity is created as expected"
        verifyAll {
            responseEntity.firstName == entity.firstName
            responseEntity.lastName == entity.lastName
            responseEntity.email == entity.email
            responseEntity.phoneNumber == entity.phoneNumber
            responseEntity.id
        }

        cleanup:
        service.delete(responseEntity.id)

        where:
        scenario                        | entity
        "name only"                     | new EmployeeEntity("fName", "lName", null, null, null)
        "name and email"                | new EmployeeEntity("fName", "lName", "first.last@company.com", null, null)
        "name, email, and phone number" | new EmployeeEntity("fName", "lName", "first.last@company.com", "555-123-4567", null)
        "all fields"                    | new EmployeeEntity("fName", "lName", "first.last@company.com", "555-123-4567", new Date())
    }

    def "Retrieve all employees" () {
        given: "Some employees exist"
        List<EmployeeEntity> createdEmployees = createEmployees(testEntities)

        when: "Retrieve all employees"
        List<EmployeeEntity> entities = service.getAll()

        then: "All created employees are returned"
        entities == createdEmployees

        cleanup:
        deleteAllEmployees()
    }

    def "Retrieve all employees with a birthday in the current month" () {
        given: "Some employees exist"
        List<EmployeeEntity> createdEmployees = createEmployees(testEntities)

        when: "Retrieve employees by birthday"
        List<EmployeeEntity> entities = service.getAllWithBirthdayInCurrentMonth()

        and: "Generate expected employees"
        List<EmployeeEntity> expectedEmployees = createdEmployees.findAll { employee ->
            employee.firstName in matchingEntities.collect { entity -> entity.firstName }
        }

        entities.each { entity ->
            println entity.firstName
        }

        then: "Expected employees are returned"
        entities == expectedEmployees

        cleanup:
        deleteAllEmployees()
    }

    private void deleteAllEmployees() {
        List<EmployeeEntity> employees = service.getAll()
        employees.each { employee ->
            service.delete(employee.id)
        }
    }

    private List<EmployeeEntity> createEmployees(List<EmployeeEntity> employees) {
        employees.collect { employee ->
            service.create(employee)
        }
    }
}
