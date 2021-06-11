import React from "react";
import {Button, Grid, Paper, TextField} from "@material-ui/core";
import {Link} from "react-router-dom";

class SingUpPage extends React.Component {

    render() {
        const onSubmit = (event) => {
            event.preventDefault();
            const data = new FormData(event.target);

            const requestOptions = {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({username: data.get('username'), password: data.get('password')})
            };
            fetch('/rest/auth/registration', requestOptions)
                .then(response => {
                    if (response.status !== 200) {
                        return response.json()
                    }
                }).then(data => {
                alert(data.message);
            });

            window.location.href = "/";
        }

        return (
            <div>
                <Grid container
                      direction="column"
                      justify="center"
                      alignItems="center"
                      className="vh-100">
                    <Grid item>
                        <div style={{fontSize: '2em'}}>Регистрация</div>
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
                                        id="email"
                                        name="email"
                                        label="e-mail"
                                        variant="outlined"
                                        size="small"/>
                                </Grid>
                                <Grid item xs={6}>
                                    <TextField
                                        id="password"
                                        label="Пароль"
                                        name="password"
                                        type="password"
                                        variant="outlined"
                                        size="small"/>
                                </Grid>
                                <Grid item xs={6}>
                                    <TextField
                                        error={false}
                                        id="password_repeat"
                                        name="password_repeat"
                                        type="password"
                                        label="Повторите пароль"
                                        variant="outlined"
                                        size="small"/>
                                </Grid>
                                <Grid item xs={12}>
                                    <Button className="w-100"
                                            variant="contained"
                                            color="primary"
                                            type="submit"> Зарегистрироваться </Button>
                                </Grid>
                                <Grid item xs={12}>
                                    <span>Уже зарегистрированы?</span>
                                    <Link to="/sing_in"> Войти </Link>
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
    };
}

export default SingUpPage;