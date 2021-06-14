import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Grid, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import TopPanel from "../component/TopPanel";
import {Link} from "react-router-dom";

class OrdersPage extends React.Component {

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            myOrders: [],
            storeOrders: [],
            errorMessage: '',
            error: null,
            isLoaded: false
        };
    }

    componentDidMount() {
        this.getOrders("/rest/order/all/trader").then((orders) => {
            this.setState({
                storeOrders: orders,
                isLoaded: true
            })
        });
        this.getOrders("/rest/order/all/client").then((orders) => {
            this.setState({
                myOrders: orders,
                isLoaded: true
            })
        });
    }

    getOrders(url) {
        const {token} = this.state;

        return fetch(url, {
            headers: {'Authorization': 'Bearer ' + token},
        })
            .then(response => {
                if (response.status === 401) {
                    this.setState({
                        error: true,
                        errorMessage: "Не авторизован"
                    })
                    throw Error("Не авторизован");
                }
                if (!response.ok) {
                    response.json().then(response => {
                        this.setState({
                            error: true,
                            errorMessage: response.message || response.error
                        })
                    })
                }
                return response.json()
            })
            .then(response => {
                if (response._embedded) {
                    return response._embedded.orders;
                }
                return [];
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    renderOrders(orders) {
        return (
            <TableContainer component={Paper} className="w-50">
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell align="left">Номер заказа</TableCell>
                            <TableCell align="left">Дата создания</TableCell>
                            <TableCell align="left">Текущий статус</TableCell>
                            <TableCell align="left">Общая стоимость</TableCell>
                            <TableCell align="left">Кол-во позиций</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {orders.map((order) => (
                            <TableRow key={order.orderId}>
                                <TableCell align="left"><Link to={`/order/${order.orderId}`}>{order.orderNumber}</Link></TableCell>
                                <TableCell align="left">{this.dateFormat(order.creationDate)}</TableCell>
                                <TableCell align="left">{order.status}</TableCell>
                                <TableCell align="left">{order.totalCost}</TableCell>
                                <TableCell align="left">{order.itemCount}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        )
    }

    dateFormat(date) {
        const options = {year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric'};
        return new Intl.DateTimeFormat('Ru-ru', options).format(new Date(date));
    }

    render() {
        const {error, errorMessage, isLoaded, myOrders, storeOrders} = this.state;

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
                    <h5 className="py-3">Мои заказы</h5>
                    {this.renderOrders(myOrders)}
                    <h5 className="py-3">Заказы мне</h5>
                    {this.renderOrders(storeOrders)}
                </Grid>
            )
        }
    };
}

export default withCookies(OrdersPage);