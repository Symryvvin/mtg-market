import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Button, Grid, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import {Link} from "react-router-dom";
import TopPanel from "../component/TopPanel";

class SearchResultPage extends React.Component {
    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            oracleId: null,
            errorMessage: '',
            error: null,
            isLoaded: false,
            items: []
        };
    }

    componentDidMount() {
        const detailsUri = "/store/singles/";
        this.oracleId = this.props.match.params.id;

        fetch(detailsUri + this.oracleId, {
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
                    items: response,
                    isLoaded: true
                })
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    addToCart(event, single) {
        event.preventDefault();
        fetch("/rest/cart/add/" + single.storeId + "/" + single.singleId, {
            method: 'PUT',
            headers: {'Authorization': 'Bearer ' + this.state.token}
        }).then(response => {
            if (!response.ok) {
                console.log(response.error);
                throw Error(response.statusText);
            }
        });
    }

    render() {
        const {error, errorMessage, isLoaded, items} = this.state;

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
                        <h2>Результат поиска</h2>
                    </Grid>
                    <Grid item className="w-75">
                        <TableContainer component={Paper}>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell align="right">Наименование</TableCell>
                                        <TableCell align="right">Сет</TableCell>
                                        <TableCell align="right">Язык</TableCell>
                                        <TableCell align="right">Стиль</TableCell>
                                        <TableCell align="right">Продавец</TableCell>
                                        <TableCell align="right">Город</TableCell>
                                        <TableCell align="right">Состояние</TableCell>
                                        <TableCell align="right">Количество</TableCell>
                                        <TableCell align="right">Цена</TableCell>
                                        <TableCell align="right"> </TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {items.map((single) => (
                                        <TableRow key={single.singleId}>
                                            <TableCell align="right">{single.oracleName} - {single.name}</TableCell>
                                            <TableCell align="right">{single.setCode}</TableCell>
                                            <TableCell align="right">{single.langCode}</TableCell>
                                            <TableCell align="right">{single.style}</TableCell>
                                            <TableCell align="right">{single.ownerName}</TableCell>
                                            <TableCell align="right">{single.ownerLocation}</TableCell>
                                            <TableCell align="right">{single.condition}</TableCell>
                                            <TableCell align="right">{single.inStock}</TableCell>
                                            <TableCell align="right">{single.price}</TableCell>
                                            <TableCell align="right">
                                                <Button
                                                    onClick={(event) => this.addToCart(event, single)}> + </Button>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                    <Grid container item justify="flex-end" className="w-75">
                        <Link to="/">Назад</Link>
                    </Grid>
                </Grid>
            );
        }
    };
}

export default withCookies(SearchResultPage);