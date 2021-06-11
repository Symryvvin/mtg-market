import React from "react";
import {Button, Grid, Paper, TextField} from "@material-ui/core";
import {useCookies} from "react-cookie";
import {Link} from "react-router-dom";


const SingInPage = () => {
    const [, setCookie] = useCookies(['access_token', 'refresh_token'])

    const onSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.target);

        const requestOptions = {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: data.get('username'), password: data.get('password')})
        };
        fetch('/rest/auth/login', requestOptions)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                if (data.success) {
                    //TODO set expires
                    let expires = new Date()
                    expires.setTime(expires.getTime())
                    /**
                     * @param data.token - java web token
                     */
                    setCookie('access_token', data.token, {path: '/'})
                    setCookie('refresh_token', data.token, {path: '/'})

                    window.location.href = "/";
                } else {
                    alert(data.message);
                }
            });
    }

    return (
        <div>
            <Grid container
                  direction="column"
                  justify="center"
                  alignItems="center"
                  className="vh-100">
                <Grid item>
                    <div style={{fontSize: '2em'}}>Вход</div>
                </Grid>
                <Paper variant="outlined" style={{width: 400, padding: 10}}>
                    <form onSubmit={onSubmit}>
                        <Grid container spacing={1}
                              justify="center"
                              alignItems="stretch">
                            <Grid item xs={12}>
                                <TextField
                                    className="w-100"
                                    id="username"
                                    name="username"
                                    label="Имя пользователя"
                                    variant="outlined"
                                    size="small"/>
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    className="w-100"
                                    id="password"
                                    label="Пароль"
                                    name="password"
                                    type="password"
                                    variant="outlined"
                                    size="small"/>
                            </Grid>
                            <Grid item xs={12}>
                                <Button
                                    className="w-100"
                                    variant="contained"
                                    color="primary"
                                    type="submit"> Войти </Button>
                            </Grid>
                            <Grid item xs={12}>
                                <span>Еще нет аккаунта?</span><Link to="/sing_up"> Зарегистрироваться </Link>
                            </Grid>
                        </Grid>
                    </form>
                </Paper>
                <Grid container spacing={2}
                      direction="row"
                      justify="flex-end"
                      alignItems="flex-end"
                      style={{width: 400}}>
                    <Grid item>
                        <Link to="/">На главную</Link>
                    </Grid>
                </Grid>
            </Grid>
        </div>
    );
}

export default SingInPage;