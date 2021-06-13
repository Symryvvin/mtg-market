import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Grid, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import TopPanel from "../component/TopPanel";

class OrderPage extends React.Component {

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            orderId: this.props.match.params.id,
            order: null,
            errorMessage: '',
            error: null,
            isLoaded: false
        };
    }

    componentDidMount() {
        const {orderId, token} = this.state;

        fetch(`/rest/order/${orderId}`, {
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
                this.setState({
                    order: response,
                    isLoaded: true
                })
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    dateFormat(date) {
        const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric' };
        return new Intl.DateTimeFormat('Ru-ru', options).format(new Date(date));
    }


    render() {
        const {error, errorMessage, isLoaded, order} = this.state;

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
                        <h2>Заказ № {order.orderNumber}</h2>
                    </Grid>
                    <Grid item className="w-50">
                        <Grid>ID Продавца: {order.traderId}</Grid>
                        <Grid>Текущий статус: {order.status}</Grid>
                        <Grid>Стоимость доставки: {order.shippingCost}</Grid>
                        <Grid>Адрес доставки: {order.shippedTo}</Grid>
                        <Grid>Общая стоимость: {order.totalCost}</Grid>
                        <Grid>
                            <h5 className="py-3">Позиции в заказе</h5>
                            <TableContainer component={Paper}>
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell align="left">Наименование</TableCell>
                                            <TableCell align="center">Аттрибуты</TableCell>
                                            <TableCell align="center">Количество</TableCell>
                                            <TableCell align="center">Цена</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {order.items.map((single) => (
                                            <TableRow key={single.singleId}>
                                                <TableCell align="left">{single.name}</TableCell>
                                                <TableCell align="left">{single.attributes}</TableCell>
                                                <TableCell align="left">{single.quantity}</TableCell>
                                                <TableCell
                                                    align="left"> {(parseFloat(single.price) * parseInt(single.quantity)).toFixed(2)}</TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <h5 className="py-3">История</h5>
                            <ul>
                                {order.eventLogs.map((event) => (
                                    <li key={event.eventTime}>{this.dateFormat(event.eventTime)} - {event.message}</li>
                                ))}
                            </ul>
                        </Grid>
                    </Grid>

                </Grid>
            )
        }
    };
}

export default withCookies(OrderPage);