import React from "react";
import {Button, Grid, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";


class Cart extends React.Component {
    /**
     * @typedef {{ _links: {increase: {href: string}, decrease: {href: string}, remove: {href: string}}, quantity: number, price: number }} Single
     */

    constructor(props) {
        super(props);

        this.state = {
            trader: "",
            singles: props.singles
        };
    }

    componentDidMount() {
        const {traderId} = this.props;

        fetch("user/" + traderId + "/username")
            .then(response => {
                if (!response.ok) {
                    console.log(response.error);
                    throw Error(response.statusText);
                }
                return response.text();
            })
            .then(response => {
                this.setState({
                    trader: response
                })
            });
    }

    onIncreaseSingleClick(event, single) {
        event.preventDefault();
        fetch(single._links.increase.href, {
            method: 'PUT'
        }).then(response => {
            if (!response.ok) {
                console.log(response.error);
                throw Error(response.statusText);
            }
            single.quantity += 1;
            this.setState(this.state);
        });
    }

    onDecreaseSingleClick(event, single) {
        event.preventDefault();
        fetch(single._links.decrease.href, {
            method: 'PUT'
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
        event.preventDefault();
        fetch(single._links.remove.href, {
            method: 'PUT'
        }).then(response => {
            if (!response.ok) {
                console.log(response.error);
                throw Error(response.statusText);
            }
            singles.splice(singles.indexOf(single), 1);
            this.setState(this.state);
        });
    }


    render() {
        const {singles, trader} = this.state;

        return (
            <Grid container item className="w-75 p-2">
                <Grid item>
                    <a href="#">{trader}</a>
                </Grid>
                <TableContainer component={Paper}>
                    <Table size="small">
                        <TableHead>
                            <TableRow>
                                <TableCell align="left">Инфо</TableCell>
                                <TableCell align="center" width={200}>Количество</TableCell>
                                <TableCell align="center" width={100}>Цена</TableCell>
                                <TableCell align="center" width={100}>Удалить</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {singles.map((single) => (
                                <TableRow key={single.id}>
                                    <TableCell align="left">{single.info}</TableCell>
                                    <TableCell align="center">
                                        <Button
                                            onClick={(event) => this.onDecreaseSingleClick(event, single)}> - </Button>
                                        {single.quantity}
                                        <Button
                                            onClick={(event) => this.onIncreaseSingleClick(event, single)}> + </Button>
                                    </TableCell>
                                    <TableCell align="center">
                                        {Math.round(parseFloat(single.price) * parseInt(single.quantity))}
                                    </TableCell>
                                    <TableCell align="center">
                                        <Button
                                            onClick={(event) => this.onRemoveSingleClick(event, singles, single)}> x </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Grid>
        )
    }
}

export default Cart;