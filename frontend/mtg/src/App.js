import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';

import {BrowserRouter as Router, Redirect, Route, Switch} from "react-router-dom";

import MainPage from "./pages";
import SearchResultPage from "./pages/searchResult";
import NotFoundPage from "./pages/404";
import SingUpPage from "./pages/singUp";
import SingInPage from "./pages/singIn";
import {Component} from "react";


class App extends Component {

    render() {
        return <Router>
            <Switch>
                <Route exact path="/" component={MainPage}/>
                <Route exact path="/sing_up" component={SingUpPage}/>
                <Route exact path="/sing_in" component={SingInPage}/>
                <Route path="/search/:id" component={SearchResultPage}/>
                <Route exact path="/404" component={NotFoundPage}/>
                <Redirect to="404"/>
            </Switch>
        </Router>
    }

}

export default App;
