import React from "react";
import {
    Button,
    Grid,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@material-ui/core";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Link} from "react-router-dom";
import RemoveShoppingCartIcon from '@material-ui/icons/RemoveShoppingCart';
import RemoveCircleTwoToneIcon from '@material-ui/icons/RemoveCircleTwoTone';
import {AddCircleTwoTone} from "@material-ui/icons";


class Cart extends React.Component {
    /**
     * @typedef {{ singleId: string, _links: {increase: {href: string}, decrease: {href: string}, remove: {href: string}}, quantity: number, price: number }} Single
     * @typedef {{ traderName: string, traderId: string
     * _links: {self: {href: string}}, quantity: number, price: number }} Store
     */
    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            store: null,
            cart: this.props.cart
        };
    }

    componentDidMount() {
        const {cart} = this.state;

        fetch(cart._links.info.href)
            .then(response => {
                if (!response.ok) {
                    console.log(response.error);
                    throw Error(response.statusText);
                }
                return response.json();
            })
            .then(response => {
                this.setState({
                    store: response
                })
            });
    }

    onIncreaseSingleClick(event, single) {
        const {token} = this.state;
        event.preventDefault();
        fetch(single._links.increase.href, {
            method: 'PUT',
            headers: {'Authorization': 'Bearer ' + token}
        }).then(response => {
            if (!response.ok) {
                response.json().then(response => {
                    alert(response.message);
                })
            } else {
                single.quantity += 1;
                this.setState(this.state);
            }
        });
    }

    onDecreaseSingleClick(event, single) {
        const {token} = this.state;
        event.preventDefault();
        fetch(single._links.decrease.href, {
            method: 'PUT',
            headers: {'Authorization': 'Bearer ' + token}
        }).then(response => {
            if (!response.ok) {
                console.log(response.error);
                throw Error(response.statusText);
            }
            if (single.quantity > 1) {
                single.quantity -= 1;
            }
            this.setState(this.state);
        });
    }

    onRemoveSingleClick(event, singles, single) {
        const {token} = this.state;
        event.preventDefault();
        fetch(single._links.remove.href, {
            method: 'PUT',
            headers: {'Authorization': 'Bearer ' + token}
        }).then(response => {
            if (!response.ok) {
                console.log(response.error);
                throw Error(response.statusText);
            }
            singles.splice(singles.indexOf(single), 1);
            this.setState(this.state);
        });
    }

    onClearStoreCartClick(event) {
        const {token} = this.state;
        const {cart} = this.state;
        event.preventDefault();
        fetch(cart._links.clear.href, {
            method: 'DELETE',
            headers: {'Authorization': 'Bearer ' + token}
        }).then(response => {
            if (!response.ok) {
                console.log(response.error);
                throw Error(response.statusText);
            }
            cart.singles.splice(0, cart.singles.length);
            this.setState(this.state);
        });
    }

    onCreateOrderClick(event) {
        const {token, cart} = this.state;
        event.preventDefault();
        fetch(cart._links.placeOrder.href, {
            method: 'POST',
            headers: {'Authorization': 'Bearer ' + token}
        }).then(response => {
            if (!response.ok) {
                response.json().then(response => {
                    alert(response.message);
                })
            } else {
                response.text().then(orderId => {
                    alert(orderId);
                    window.location.href = "/order/" + orderId;
                })


            }
        })
    }

    singleName(single) {
        if (single.oracleName === single.name) {
            return (<div>{single.oracleName}</div>)
        } else {
            return (<div>{single.name}<br/><small>{single.oracleName}</small></div>)
        }
    }

    render() {
        const {cart, store} = this.state;
        const singles = cart.singles;

        let trader = "";
        if (store) {
            let storeLink = "/store/" + store.traderName;
            trader = <Link to={storeLink}>{store.traderName}</Link>;
        }

        return (
            <Grid container item className="w-75 p-2">
                <Grid container
                      item
                      justify="space-between"
                      className="py-2">
                    {trader}
                    <Grid item>
                        <Button variant="contained"
                                color="primary"
                                onClick={(event) => this.onCreateOrderClick(event)}> Сделать заказ </Button>
                    </Grid>
                </Grid>
                <TableContainer component={Paper}>
                    <Table size="small">
                        <TableHead>
                            <TableRow>
                                <TableCell align="left">Наименование</TableCell>
                                <TableCell align="left">Сет</TableCell>
                                <TableCell align="left">Инфо</TableCell>
                                <TableCell align="center" width={200}>Количество</TableCell>
                                <TableCell align="center" width={100}>Цена</TableCell>
                                <TableCell align="center" width={100}>Удалить</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {singles.map((single) => (
                                <TableRow key={single.singleId}>
                                    <TableCell align="left">{this.singleName(single)}</TableCell>
                                    <TableCell align="center">
                                        <i className={"ss ss-2x ss-" + single.setCode}/></TableCell>
                                    <TableCell align="left">{single.attributes}</TableCell>
                                    <TableCell align="center">
                                        <IconButton onClick={(event) => this.onDecreaseSingleClick(event, single)}>
                                            <RemoveCircleTwoToneIcon/>
                                        </IconButton>
                                        {single.quantity}
                                        <IconButton onClick={(event) => this.onIncreaseSingleClick(event, single)}>
                                            <AddCircleTwoTone/>
                                        </IconButton>
                                    </TableCell>
                                    <TableCell align="center">
                                        {Math.round(parseFloat(single.price) * parseInt(single.quantity))}
                                    </TableCell>
                                    <TableCell align="center">
                                        <IconButton
                                            onClick={(event) => this.onRemoveSingleClick(event, singles, single)}>
                                            <RemoveShoppingCartIcon/>
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                <Grid container
                      item
                      justify="flex-end"
                      className="py-2">
                    <Grid item>
                        <Button variant="contained"
                                color="secondary"
                                onClick={(event) => this.onClearStoreCartClick(event)}> Очистить все </Button>
                    </Grid>
                </Grid>
            </Grid>
        )
    }
}

export default withCookies(Cart);