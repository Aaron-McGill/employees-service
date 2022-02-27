import React, {Component} from 'react';
import {Navbar, NavbarBrand, Dropdown, DropdownItem, DropdownToggle, DropdownMenu} from 'reactstrap';
import {Link} from 'react-router-dom';

export default class AppNavbar extends Component {
    constructor(props) {
        super(props);

        this.toggle = this.toggle.bind(this);
        this.state = {
          dropdownOpen: false
        };
      }

    toggle() {
      this.setState(prevState => ({
        dropdownOpen: !prevState.dropdownOpen
      }));
    }

    render() {
        return <Navbar color="dark">
            <Dropdown left isOpen={this.state.dropdownOpen} toggle={this.toggle} size="lg">
                <DropdownToggle caret>
                Actions
                </DropdownToggle>
                <DropdownMenu>
                    <DropdownItem tag={Link} to="/employees">View Employees</DropdownItem>
                    <DropdownItem tag={Link} to="/employeesByBirthday">View Employees With Birthday In Current Month</DropdownItem>
                    <DropdownItem tag={Link} to="/employees/new">Add Employee</DropdownItem>
                </DropdownMenu>
            </Dropdown>
        </Navbar>;
    }
}