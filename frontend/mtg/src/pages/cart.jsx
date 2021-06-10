import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Grid} from "@material-ui/core";
import {Link} from "react-router-dom";
import TopPanel from "../component/TopPanel";
import Cart from "../component/Cart";

class CartPage extends React.Component {
    /**
     * @typedef {{_embedded: {cartRepresentationList: array}}} CartModel
     * @typedef {{storeId: string, singles: array, _links: {create_order: {href: string}}}} Cart
     */

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            errorMessage: '',
            error: null,
            isLoaded: false,
            carts: []
        }
    }

    componentDidMount() {
        fetch("http://localhost:8080/rest/cart/edit", {
            headers: {'Authorization': 'Bearer ' + this.state.token},
        })
            .then(response => {
                if (!response.ok) {
                    response.json()
                        .then(response => {
                            console.log(response.error);
                            this.setState({
                                error: true,
                                errorMessage: response.error
                            })
                        });
                    throw Error(response.statusText);
                }
                return response.json()
            })
            .then(response => {
                this.setState({
                    carts: response,
                    isLoaded: true
                })
            })
            .catch(function (error) {
                console.log(error);
            });
    }


    render() {
        const {error, errorMessage, isLoaded, carts} = this.state;

        let cartRender;
        if (carts._embedded) {
            cartRender = carts._embedded.cartRepresentationList.map((cart) => (
                <Cart key={cart.storeId}
                      cart={cart}/>
            ))
        } else {
            cartRender = <div/>
        }

        if (error) {
            return <div>Ошибка: {errorMessage}</div>;
        } else if (!isLoaded) {
            return <div>Загрузка...</div>;
        } else {
            return (
                <Grid container
                      direction="column"
                      justify="flex-start"
                      alignItems="center">
                    <TopPanel/>
                    <Grid item>
                        <h2>Корзина</h2>
                    </Grid>
                    {cartRender}
                    <Grid container item justify="flex-end" className="w-75">
                        <Link to="/">Назад</Link>
                    </Grid>
                </Grid>
            );
        }
    };
}

export default withCookies(CartPage);