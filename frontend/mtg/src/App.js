import './App.css';

import 'bootstrap/dist/css/bootstrap.min.css';

import {BrowserRouter as Router, Redirect, Route, Switch} from "react-router-dom";

import MainPage from "./pages";
import SearchResultPage from "./pages/search_result";
import NotFoundPage from "./pages/404";

function App() {
    return <Router>
        <Switch>
            <Route exact path="/" component={MainPage}/>
            <Route path="/search/:id" component={SearchResultPage}/>
            <Route exact path="/404" component={NotFoundPage}/>
            <Redirect to="404"/>
        </Switch>
    </Router>
}

export default App;
