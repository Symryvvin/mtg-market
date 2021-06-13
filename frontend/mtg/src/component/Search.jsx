import React from "react";
import Autocomplete from "@material-ui/lab/Autocomplete";
import {Grid, IconButton, TextField} from "@material-ui/core";
import SearchIcon from '@material-ui/icons/Search';

class Search extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            oracleId: null,
            options: [],
        };

        this.onSearchButtonClick = this.onSearchButtonClick.bind(this)
        this.onChangeValue = this.onChangeValue.bind(this)
    }

    onChangeInputValue(event, inputValue) {
        event.preventDefault();

        if (inputValue === '') {
            return undefined;
        }

        fetch("/rest/search/auto?search=" + inputValue)
            .then(response => response.json())
            .then((responseData) => {
                this.setState({
                    options: responseData
                })
            });
    }

    onChangeValue(event, value) {
        event.preventDefault()
        this.setState({
            oracleId: value.id
        })
    }

    onSearchButtonClick(event, oracleId) {
        event.preventDefault();
        if (oracleId) {
            window.location.replace("/search/" + oracleId)
        } else {
            alert("oracleId is null");
        }
    }

    render() {
        const {options, value, oracleId} = this.state;

        return (
            <Grid container item justify="center" alignItems="center" className="w-auto">
                <Autocomplete id="single-search"
                              noOptionsText={"Ничего не найдено"}
                              style={{width: 500}}
                              options={options} getOptionLabel={(option) => option.name}
                              getOptionSelected={(option => option.name)}
                              autoComplete autoHighlight autoSelect disableClearable
                              value={value}
                              onChange={(event, newValue) => {
                                  this.onChangeValue(event, newValue);
                              }}
                              onInputChange={(event, newInputValue) => {
                                  this.onChangeInputValue(event, newInputValue);
                              }}
                              renderInput={(params) => (
                                  <TextField {...params}
                                             label="Поиск по наименованию"
                                             size="small"

                                             InputProps={{
                                                 ...params.InputProps,
                                                 type: 'search'
                                             }}/>
                              )}/>
                <Grid item style={{width: 10}}/>
                <IconButton onClick={(event) => this.onSearchButtonClick(event, oracleId)}>
                    <SearchIcon/>
                </IconButton>
            </Grid>
        )
    }
}

export default Search;