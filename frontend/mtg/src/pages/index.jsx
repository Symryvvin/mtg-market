import React from "react";
import {Col, Container, Row} from 'react-bootstrap';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';

const MainPage = () => {
    const autocompleteUri = "http://localhost:8081/rest/search/api/v1/auto?search=";

    const [value, setValue] = React.useState(null);
    const [inputValue, setInputValue] = React.useState('');
    const [options, setOptions] = React.useState([]);

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

    return (
        <Container fluid>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <Autocomplete
                        //freeSolo
                        id="single-search"
                        style={{width: 500}}
                        options={options}
                        getOptionLabel={(option) => option.name}
                        autoComplete
                        autoHighlight
                        autoSelect
                        disableClearable
                        value={value}
                        onChange={(event, newValue) => {
                            setOptions(newValue ? [newValue, ...options] : options);
                            setValue(newValue);
                            window.location.replace("/search/" + newValue.id);
                        }}
                        onInputChange={(event, newInputValue) => {
                            setInputValue(newInputValue);
                        }}
                        renderInput={(params) => (
                            <TextField
                                {...params}
                                label="Поиск по наименованию"
                                margin="normal" variant="outlined"
                                InputProps={{...params.InputProps, type: 'search'}}/>
                        )}/>
                </Col>
            </Row>
        </Container>
    );
};


export default MainPage;