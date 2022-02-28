import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
const axios = require('axios');

class EmployeeAdd extends Component {

    emptyItem = {
        id: '',
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        birthday: null
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            status: '',
            errorMessage: '',
            errorDetails: ''
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({status: '', errorMessage: '', errorDetails: ''})
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        if (target.name === "birthday") {
            item[name] = new Intl.DateTimeFormat('en-US').format(new Date(value + " EST"));
        } else {
            item[name] = value;
        }
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        console.log(JSON.stringify(item))
        axios.post('/employees', item, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            this.setState({status: 'success', errorMessage: '', errorDetails: '', item: this.emptyItem});
            document.getElementById('birthday').value = null;
        }).catch(err=> {
            this.setState({status: 'failed', errorMessage: err.response.data.message,
                           errorDetails: err.response.data.details ? err.response.data.details.join('\n') : ''});
        });
    }

    render() {
        const {item} = this.state;
        const title = <h2>{'Add Employee'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="firstName">First Name</Label>
                        <Input type="text" name="firstName" id="firstName" value={item.firstName || ''}
                               onChange={this.handleChange} autoComplete="firstName"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="lastName">Last Name</Label>
                        <Input type="text" name="lastName" id="lastName" value={item.lastName || ''}
                               onChange={this.handleChange} autoComplete="lastName"/>
                        </FormGroup>
                    <FormGroup>
                        <Label for="email">Email</Label>
                        <Input type="text" name="email" id="email" value={item.email || ''}
                               onChange={this.handleChange} autoComplete="email"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="phoneNumber">Phone Number</Label>
                        <Input type="text" name="phoneNumber" id="phoneNumber" value={item.phoneNumber || ''}
                               onChange={this.handleChange} autoComplete="phoneNumber"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="birthday">Birthday</Label>
                        <Input type="date" name="birthday" id="birthday"
                               onChange={this.handleChange} autoComplete="birthday"/>
                        </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit" className='mt-2'>Save</Button>{' '}
                    </FormGroup>
                </Form>
                { this.state.errorMessage &&
                    <div className="isa_error">
                       <i className="fa fa-times-circle"></i>
                       {this.state.errorMessage}
                       <br/>{this.state.errorDetails}
                    </div>}
                { this.state.status === 'success' &&
                    <div className="isa_success">
                         <i className="fa fa-check"></i>
                         The employee was successfully added to the system.
                    </div>}
            </Container>
        </div>
    }
}

export default withRouter(EmployeeAdd);