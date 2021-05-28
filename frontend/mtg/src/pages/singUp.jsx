import React from "react";
import {Button, Grid, Paper, TextField} from "@material-ui/core";

class SingInPage extends React.Component {

    render() {
        const onSubmit = (event) => {
            event.preventDefault();
            const data = new FormData(event.target);

            const requestOptions = {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({username: data.get('username'), password: data.get('password')})
            };
            fetch('http://localhost:8080/registration', requestOptions)
                .then(response => {
                    if (response.status !== 200) {
                        return response.json()
                    }
                }).then(data => {
                alert(data.message);
            });
        }

        return (
            <div>
                <Grid container
                      direction="row"
                      justify="center"
                      alignItems="center"
                      className="vh-100">
                    <Paper variant="outlined" style={{width: 400, padding: 10}}>
                        <div style={{fontSize: '2em', padding: 5, marginBottom: 20}}>Регистрация</div>
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
                            </Grid>
                        </form>
                    </Paper>
                </Grid>
            </div>
        );
    };
}

export default SingInPage;