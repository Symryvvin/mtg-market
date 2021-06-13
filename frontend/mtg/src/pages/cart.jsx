import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Grid} from "@material-ui/core";
import TopPanel from "../component/TopPanel";
import Cart from "../component/Cart";

class CartPage extends React.Component {
    /**
     * @typedef {{_embedded: {carts: array}}} CartModel
     * @typedef {{storeId: string, singles: array,
     * _links: {placeOrder: {href: string} clear: {href: string}}, info: {href: string}}} Cart
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
        fetch("/rest/cart/edit", {
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
            cartRender = carts._embedded.carts.map((cart) => (
                <Cart key={cart.traderId}
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
                </Grid>
            );
        }
    };
}

export default withCookies(CartPage);