import React from 'react';
import ReactDom from 'react-dom';


class Shop extends React Component{
    render() {
        return (
            <p>Welcome to my webshop</p>
        )
    };
}

const shopapp = document.getElementById('shop-app');

ReactDom.render(<Shop/>, shopapp);