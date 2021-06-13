import React from "react";
import {Grid, IconButton, Menu, MenuItem} from "@material-ui/core";
import {Link} from "react-router-dom";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import DehazeIcon from '@material-ui/icons/Dehaze';
import ShoppingCartTwoToneIcon from '@material-ui/icons/ShoppingCartTwoTone';
import HomeTwoToneIcon from '@material-ui/icons/HomeTwoTone';
import Search from "./Search";

class TopPanel extends React.Component {

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;

        this.state = {
            token: cookies.get('access_token'),
            isLogin: Boolean(cookies.get('access_token')),
            anchorEl: null
        };

        this.logout = this.logout.bind(this)
        this.handleClick = this.handleClick.bind(this)
        this.handleClose = this.handleClose.bind(this)
    }

    logout() {
        const {cookies} = this.props;

        cookies.remove('access_token');
        this.setState({
                token: null,
                isLogin: false,
                anchorEl: null
            }
        );
    }

    handleClick(event) {
        this.setState({
            anchorEl: event.currentTarget
        })
    }

    handleClose() {
        this.setState({
            anchorEl: null
        })
    }

    render() {
        return (
            <Grid container item>
                <Grid container item  className="border bg-light p-2">
                    <Grid item hidden={this.state.isLogin}>
                        <Link to="/sing_in">Войти</Link>
                        <span> или </span>
                        <Link to="/sing_up">Регистрация</Link>
                    </Grid>
                    <Grid container item
                          direction="row"
                          justify="space-between"
                          alignItems="center"
                          hidden={!this.state.isLogin}>
                        <IconButton aria-label="main-menu" aria-haspopup="true" onClick={this.handleClick}>
                            <DehazeIcon/>
                        </IconButton>
                        <Menu id="main-menu"
                              anchorEl={this.state.anchorEl}
                              keepMounted
                              open={Boolean(this.state.anchorEl)}
                              onClose={this.handleClose}>
                            <MenuItem onClick={this.handleClose}> <Link to="/profile">Профиль</Link></MenuItem>
                            <MenuItem onClick={this.handleClose}> <Link to="/store">Магазин</Link></MenuItem>
                            <MenuItem onClick={this.handleClose}>Заказы</MenuItem>
                            <MenuItem onClick={this.logout}>Выход</MenuItem>
                        </Menu>
                        <IconButton href="/" >
                            <HomeTwoToneIcon/>
                        </IconButton>
                        <IconButton href="/cart">
                            <ShoppingCartTwoToneIcon/>
                        </IconButton>
                    </Grid>
                </Grid>
                <Grid item className="w-100 py-4">
                    <Search/>
                </Grid>
            </Grid>
        )
    }
}

export default withCookies(TopPanel);