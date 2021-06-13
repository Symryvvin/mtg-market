import React from "react";
import {Cookies, withCookies} from "react-cookie";
import {instanceOf} from "prop-types";
import {Button, Grid} from "@material-ui/core";
import TopPanel from "../component/TopPanel";

class ProfilePage extends React.Component {
    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            token: cookies.get('access_token'),
            username: this.props.match.params.username,
            data: null,
            errorMessage: '',
            error: null,
            isLoaded: false
        };
    }

    componentDidMount() {
        const {username, token} = this.state;

        if (username) {
            alert('Просмотр профилей пользователей не реализован');
        } else {
            fetch("/rest/user", {
                headers: {'Authorization': 'Bearer ' + token},
            })
                .then(response => {
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
                        data: response,
                        isLoaded: true
                    })
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }

    becomeTrader(event) {
        const {token} = this.state;
        event.preventDefault();
        fetch("/rest/user/become/trader", {
            method: 'PUT',
            headers: {'Authorization': 'Bearer ' + token}
        }).then(response => {
            return response.json()
        }).then(response => {
            if (!response.success) {
                alert(response.message);
            }
        });
    }

    render() {
        const {error, errorMessage, isLoaded, data} = this.state;

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
                        <h2>Профиль</h2>
                    </Grid>
                    <Grid item containter className="w-75">
                        <Grid>ID: {data.id}</Grid>
                        <Grid>Имя пользователя: {data.login}</Grid>
                        <Grid>Ф.И.О.: {data.fullName}</Grid>
                        <Grid>Адрес: {data.address}</Grid>
                        <Grid>Телефон: {data.phone}</Grid>
                        <Grid>Email: {data.email}</Grid>
                        <Grid>
                            <Button variant="contained"
                                    color="primary"
                                    onClick={(event) => this.becomeTrader(event)}>Стать продавцом</Button>
                        </Grid>
                    </Grid>
                </Grid>
            )
        }
    };

}

export default withCookies(ProfilePage);