import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {
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
import TopPanel from "../component/TopPanel";
import AddShoppingCartIcon from '@material-ui/icons/AddShoppingCart';

class SearchResultPage extends React.Component {
    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            oracleId: this.props.match.params.id,
            errorMessage: '',
            error: null,
            isLoaded: false,
            items: []
        };
    }

    componentDidMount() {
        const {oracleId} = this.state;

        fetch("/rest/single/search/" + oracleId, {
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

        const {token} = this.state;

        fetch(single._links.addToCart.href, {
            method: 'PUT',
            headers: [
                ["Authorization", 'Bearer ' + token],
                ['X-UserId', single.traderId]
            ],
        }).then(response => {
            if (!response.ok) {
                console.log(response.error);
                throw Error(response.statusText);
            }
        });
    }

    singleName(single) {
        if (single.oracleName === single.name) {
            return (<div>{single.oracleName}</div>)
        } else {
            return (<div>{single.name}<br/><small>{single.oracleName}</small></div>)
        }
    }

    /**
     * @typedef {{oracleName: string, name: string, setCode: string, langCode: string,
     * traderId: number, traderName: string, traderLocation: string, inStock: number,
     * _links: {addToCart: {href: string}}}} Single
     */

    render() {
        const {error, errorMessage, isLoaded, items} = this.state;

        let singles = items._embedded ? items._embedded.singles : [];

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
                                        <TableCell align="left">Наименование</TableCell>
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
                                    {singles.map((single) => (
                                        <TableRow key={single.singleId}>
                                            <TableCell align="left">{this.singleName(single)}</TableCell>
                                            <TableCell align="center">
                                                <i className={"ss ss-2x ss-" + single.setCode}/></TableCell>
                                            <TableCell align="right">{single.langCode}</TableCell>
                                            <TableCell align="right">{single.style}</TableCell>
                                            <TableCell align="right">{single.traderName}</TableCell>
                                            <TableCell align="right">{single.traderLocation}</TableCell>
                                            <TableCell align="right">{single.condition}</TableCell>
                                            <TableCell align="right">{single.inStock}</TableCell>
                                            <TableCell align="right">{single.price}</TableCell>
                                            <TableCell align="right">
                                                <IconButton onClick={(event) => this.addToCart(event, single)}>
                                                    <AddShoppingCartIcon/>
                                                </IconButton>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>
            );
        }
    };
}

export default withCookies(SearchResultPage);