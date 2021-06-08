import React from "react";
import {Button, Grid, Menu, MenuItem} from "@material-ui/core";
import {Link} from "react-router-dom";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";

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
            <Grid container item className="border bg-light p-2">
                <Grid item hidden={this.state.isLogin}>
                    <Link to="/sing_in">Войти</Link>
                    <span> или </span>
                    <Link to="/sing_up">Регистрация</Link>
                </Grid>
                <Grid container justify="space-between" alignItems="center" item hidden={!this.state.isLogin}>
                    <Button aria-controls="main-menu"
                            style={{textTransform: "none"}}
                            aria-haspopup="true"
                            onClick={this.handleClick}> Меню </Button>
                    <Menu id="main-menu"
                          anchorEl={this.state.anchorEl}
                          keepMounted
                          open={Boolean(this.state.anchorEl)}
                          onClose={this.handleClose}>
                        <MenuItem onClick={this.handleClose}> <Link to="/profile/:username">Профиль</Link></MenuItem>
                        <MenuItem onClick={this.handleClose}>Магазины</MenuItem>
                        <MenuItem onClick={this.handleClose}>Заказы</MenuItem>
                        <MenuItem onClick={this.logout}>Выход</MenuItem>
                    </Menu>
                    <Link to="/cart">Корзина</Link>
                </Grid>
            </Grid>
        )
    }
}

export default withCookies(TopPanel);