import React from "react";
import {Button, Grid, TextField} from "@material-ui/core";
import Autocomplete from '@material-ui/lab/Autocomplete';
import {useCookies} from 'react-cookie'
import TopPanel from "../component/TopPanel";

const MainPage = () => {
    const autocompleteUri = "http://localhost:8080/rest/search/auto?search=";

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

    const search = () => {
        if (oracleId) {
            window.location.replace("/search/" + oracleId)
        } else {
            alert("oracleId is null");
        }
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
            <TopPanel/>
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
                        onClick={search}>Поиск</Button>
            </Grid>
            <Grid item className="border bg-light ">

            </Grid>
        </Grid>
    );
};


export default MainPage;