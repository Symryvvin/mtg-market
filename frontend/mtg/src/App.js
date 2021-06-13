import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';

import {BrowserRouter as Router, Redirect, Route, Switch} from "react-router-dom";

import MainPage from "./pages";
import SearchResultPage from "./pages/searchResult";
import NotFoundPage from "./pages/404";
import SingUpPage from "./pages/singUp";
import SingInPage from "./pages/singIn";
import {Component} from "react";
import CartPage from "./pages/cart";
import ProfilePage from "./pages/profile";
import StorePage from "./pages/store";
import OrdersPage from "./pages/orders";
import OrderPage from "./pages/order";


class App extends Component {

    render() {
        return <Router>
            <Switch>
                <Route exact path="/" component={MainPage}/>
                <Route exact path="/profile" component={ProfilePage}/>
                <Route exact path="/profile/:username" component={ProfilePage}/>
                <Route exact path="/store" component={StorePage}/>
                <Route exact path="/store/:username" component={StorePage}/>
                <Route exact path="/orders" component={OrdersPage}/>
                <Route exact path="/order/:id" component={OrderPage}/>
                <Route exact path="/cart" component={CartPage}/>
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
