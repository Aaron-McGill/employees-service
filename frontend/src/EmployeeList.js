import React, { Component } from 'react';
import { Button, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
const axios = require('axios');

class EmployeeList extends Component {

    constructor(props) {
        super(props);
        this.state = {employees: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        axios.get('/employees')
            .then(response => this.setState({employees: response.data.items}));
    }

    async remove(id) {
        axios.delete(`/employees/${id}`).then(() => {
            let updatedEmployees = [...this.state.employees].filter(i => i.id !== id);
            this.setState({employees: updatedEmployees});
        });
    }

    render() {
        const {employees} = this.state;

        const employeeList = employees.map(employee => {
            return <tr key={employee.id}>
                <td style={{whiteSpace: 'nowrap'}}>{employee.firstName + ' ' + employee.lastName}</td>
                <td>{employee.email ? employee.email : 'N/A'}</td>
                <td>{employee.phoneNumber ? employee.phoneNumber : 'N/A'}</td>
                <td>{employee.birthday ? employee.birthday : 'N/A'}</td>
                <td>
                    <Button size="sm" color="danger" onClick={() => this.remove(employee.id)}>Delete</Button>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <h3>Employees</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%">Name</th>
                            <th width="20%">Email</th>
                            <th width="20%">Phone Number</th>
                            <th width="20%">Birthday</th>
                            <th width="20%">Actions</th>
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

export default EmployeeList;