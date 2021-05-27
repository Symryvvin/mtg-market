import React from "react";

class SearchResultPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            items: []
        };
    }

    componentDidMount() {
        const detailsUri = "http://localhost:8081/rest/search/api/v1/details?oracle_id=";

        fetch(detailsUri + this.props.match.params.id)
            .then(res => res.json())
            .then((result) => {
                    this.setState({
                        isLoaded: true,
                        items: result
                    });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            )
    }

    render() {
        const {error, isLoaded, items} = this.state;
        if (error) {
            return <div>Ошибка: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Загрузка...</div>;
        } else {
            return (
                <div>
                    <h2>search result for id = {this.props.match.params.id}</h2>
                    <ul>
                        {items.map(item => (
                            <li key={item.id}>
                                {item.id} - {item.printedName}
                            </li>
                        ))}
                    </ul>
                </div>

            );
        }
    };
}

export default SearchResultPage;