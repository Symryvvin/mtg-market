import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Grid, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import TopPanel from "../component/TopPanel";

class StorePage extends React.Component {

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            username: this.props.match.params.username,
            store: null,
            errorMessage: '',
            error: null,
            isLoaded: false
        };
    }

    componentDidMount() {
        const {username, token} = this.state;

        if (username) {
            alert('Просмотр магазинов пользователей не реализован');
        } else {
            fetch("/rest/store/edit", {
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
                        store: response,
                        isLoaded: true
                    })
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }

    singleName(single) {
        if (single.oracleName === single.name) {
            return (<div>{single.oracleName}</div>)
        } else {
            return (<div>{single.name}<br/><small>{single.oracleName}</small></div>)
        }
    }

    render() {
        const {error, errorMessage, isLoaded, store} = this.state;

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
                        <h2>Магазин</h2>
                    </Grid>
                    <Grid item className="w-75">
                        <TableContainer component={Paper}>
                            <Table size="small">
                                <TableHead>
                                    <TableRow>
                                        <TableCell align="left">Наименование</TableCell>
                                        <TableCell align="center">Сет</TableCell>
                                        <TableCell align="center">Язык</TableCell>
                                        <TableCell align="center">Стиль</TableCell>
                                        <TableCell align="center">Состояние</TableCell>
                                        <TableCell align="center">Количество</TableCell>
                                        <TableCell align="center">Цена</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {store.singles.map((single) => (
                                        <TableRow key={single.singleId}>
                                            <TableCell align="left">{this.singleName(single)}</TableCell>
                                            <TableCell align="center">
                                                <i className={"ss ss-2x ss-" + single.setCode}/></TableCell>
                                            <TableCell align="left">{single.langCode}</TableCell>
                                            <TableCell align="left">{single.style}</TableCell>
                                            <TableCell align="left">{single.condition}</TableCell>
                                            <TableCell align="left">{single.inStock}</TableCell>
                                            <TableCell align="left">{single.price}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>
            )
        }
    };

}

export default withCookies(StorePage);