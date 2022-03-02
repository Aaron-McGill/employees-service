# Employee Management Service
This project defines a basic REST API for managing employee data. The API includes these endpoints:

* POST /employees adds a new employee to the system. These are the supported fields:
  * firstName - A String with the first name of the employee (required)
  * lastName - A String with the last name of the employee (required)
  * email - A String with the employee's email (optional)
  * phoneNumber - A String with the employee's phone number (optional)
  * birthday - A String with the employee's birthday, of the form M/d/yyyy (optional)
* GET /employees retrieves all the employees
* GET /employeesByBirthday retrieves all the employees with a birthday in the current month
* DELETE /employees/{id} removes the employee with the given ID from the system

There is a basic ReactJS app defined in the [frontend](frontend) folder that interacts with this API.