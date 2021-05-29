import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Grid, Link, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";

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
        const detailsUri = "http://localhost:8080/store/singles/";
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
                      alignItems="center"
                      className="vh-100">
                    <h2>Результат поиска</h2>
                    <TableContainer component={Paper} className="w-75">
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
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {items.map((row) => (
                                    <TableRow key={row.singleId}>
                                        <TableCell align="right">{row.oracleName} - {row.name}</TableCell>
                                        <TableCell align="right">{row.setCode}</TableCell>
                                        <TableCell align="right">{row.langCode}</TableCell>
                                        <TableCell align="right">{row.style}</TableCell>
                                        <TableCell align="right">{row.ownerName}</TableCell>
                                        <TableCell align="right">{row.ownerLocation}</TableCell>
                                        <TableCell align="right">{row.condition}</TableCell>
                                        <TableCell align="right">{row.inStock}</TableCell>
                                        <TableCell align="right">{row.price}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <Grid container item justify="flex-end" className="w-75">
                        <Link href={"/"}> Назад </Link>
                    </Grid>
                </Grid>
            );
        }
    };
}

export default withCookies(SearchResultPage);