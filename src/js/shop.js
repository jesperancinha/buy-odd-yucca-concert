import React from 'react';
import ReactDom from 'react-dom';
import ShopButtons from './components/ShopButtons';
import Button from 'react-bootstrap/lib/Button';
import ButtonToolbar from 'react-bootstrap/lib/ButtonToolbar';
import Bootstrap from './../../node_modules/bootstrap/dist/css/bootstrap.css';

class Shop extends React.Component{
    constructor(){
        super();
        this.item1 = "Twisted glass";
        this.value1 = 143;
    }
    render() {
        return (
            <div>
                <h1>Welcome to my webshop</h1>
                <p>{this.item1}</p><p>{this.value1}</p>
                <ButtonToolbar>
                    <Button bsStyle="primary">Main page</Button>
                    <Button bsStyle="info">About us!</Button>
                </ButtonToolbar>
             </div>
        )
    };
}

const shopapp = document.getElementById('shop-app');
const shopbuttons = document.getElementById('shop-buttons');

ReactDom.render(<Shop/>, shopapp);
ReactDom.render(<ShopButtons/>, shopbuttons);