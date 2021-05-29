import React from "react";
import {Button, Grid, TextField} from "@material-ui/core";
import Autocomplete from '@material-ui/lab/Autocomplete';
import {useCookies} from 'react-cookie'
import {Link} from "react-router-dom";

const MainPage = () => {
    const autocompleteUri = "http://localhost:8081/rest/search/api/v1/auto?search=";

    const [oracleId, setOracleId] = React.useState(null);
    const [value, setValue] = React.useState(null);
    const [inputValue, setInputValue] = React.useState('');
    const [options, setOptions] = React.useState([]);

    const [isLogin, setIsLogin] = React.useState(false);
    const [cookies, setCookie, removeCookie] = useCookies(['access_token', 'refresh_token'])

    const logout = () => {
        removeCookie('access_token');
        removeCookie('refresh_token');
        window.location.href = '/';
        setIsLogin(false);
    }

    React.useEffect(() => {
        if (inputValue === '') {
            setOptions(value ? [value] : []);
            return undefined;
        }

        fetch(autocompleteUri + inputValue)
            .then(response => response.json())
            .then((responseData) => {
                setOptions(responseData);
            });

    }, [value, inputValue]);

    React.useEffect(() => {
        if (cookies.access_token) {
            setIsLogin(true);
        }
    }, [cookies])

    return (
        <Grid container direction="column" justify="space-between" alignItems="stretch" className="vh-100">
            <Grid container item className="border bg-light p-2">
                <Grid item hidden={isLogin}>
                    <Link to="/sing_in">Войти</Link>
                    <span> или </span>
                    <Link to="/sing_up">Зарегистрироваться</Link>
                </Grid>
                <Grid container justify="space-between" alignItems="center" item hidden={!isLogin}>
                    <Link to="/profile/:username">Профиль</Link>
                    <Button variant="outlined" color="primary" style={{textTransform: "none"}}
                            onClick={logout}>Выход</Button>
                </Grid>
            </Grid>
            <Grid container item direction="row" justify="center" alignItems="center">
                <Autocomplete id="single-search"
                              style={{width: 500}}
                              options={options} getOptionLabel={(option) => option.name}
                              getOptionSelected={(option => option.name)}
                              autoComplete autoHighlight autoSelect disableClearable
                              value={value}
                              onChange={(event, newValue) => {
                                  setOptions(newValue ? [newValue, ...options] : options);
                                  setValue(newValue);
                                  setOracleId(newValue.id);
                              }}
                              onInputChange={(event, newInputValue) => {
                                  setInputValue(newInputValue);
                              }}
                              renderInput={(params) => (
                                  <TextField {...params}
                                             label="Поиск по наименованию"
                                             size="small"
                                             variant="outlined"
                                             InputProps={{
                                                 ...params.InputProps,
                                                 type: 'search'
                                             }}/>
                              )}/>
                <Grid item style={{width: 10}}/>
                <Button variant="outlined" color="primary" style={{textTransform: "none"}}
                        onClick={() => window.location.replace("/search/" + oracleId)}>Поиск</Button>
            </Grid>
            <Grid item className="border bg-light ">

            </Grid>
        </Grid>
    );
};


export default MainPage;