import React, { Component } from 'react';
import { Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class EmployeeListBirthday extends Component {

    constructor(props) {
        super(props);
        this.state = {employees: []};
    }

    componentDidMount() {
        fetch('/employeesByBirthday')
            .then(response => response.json())
            .then(data => this.setState({employees: data.items}));
    }

    render() {
        const {employees} = this.state;

        const employeeList = employees.map(employee => {
            return <tr key={employee.id}>
                <td style={{whiteSpace: 'nowrap'}}>{employee.firstName + ' ' + employee.lastName}</td>
                <td>{employee.email ? employee.email : 'N/A'}</td>
                <td>{employee.phoneNumber ? employee.phoneNumber : 'N/A'}</td>
                <td>{employee.birthday}</td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <h3>Employees With Birthday In Current Month</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="25%">Name</th>
                            <th width="25%">Email</th>
                            <th width="25%">Phone Number</th>
                            <th width="25%">Birthday</th>
                        </tr>
                        </thead>
                        <tbody>
                        {employeeList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default EmployeeListBirthday;