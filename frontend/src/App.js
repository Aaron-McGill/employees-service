import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import EmployeeList from './EmployeeList';
import EmployeeListBirthday from './EmployeeListBirthday'
import EmployeeAdd from './EmployeeAdd'

class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/employees' exact={true} component={EmployeeList}/>
            <Route path='/employeesByBirthday' exact={true} component={EmployeeListBirthday}/>
            <Route path='/employees/new' exact={true} component={EmployeeAdd}/>
          </Switch>
        </Router>
    )
  }
}

export default App;