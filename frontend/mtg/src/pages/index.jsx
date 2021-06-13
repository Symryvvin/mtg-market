import React from "react";
import {Grid} from "@material-ui/core";
import TopPanel from "../component/TopPanel";

const MainPage = () => {

    return (
        <Grid container direction="column" justify="space-between" alignItems="stretch" className="vh-100">
            <TopPanel/>
            <Grid container item direction="row" justify="center" alignItems="center">
                <img src={process.env.PUBLIC_URL + '/static/logo.png'} width={300} height={300} style={{opacity: 0.2}} alt="logo"/>
            </Grid>
            <Grid item className="border bg-light h-25">

            </Grid>
        </Grid>
    );
};


export default MainPage;