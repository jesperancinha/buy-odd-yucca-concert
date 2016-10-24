import React from 'react';
import ReactDom from 'react-dom';
import ShopButtons from './components/ShopButtons';
import Button from 'react-bootstrap/lib/Button';
import Label from 'react-bootstrap/lib/Label';
import ButtonToolbar from 'react-bootstrap/lib/ButtonToolbar';
import Bootstrap from './../../node_modules/bootstrap/dist/css/bootstrap.css';

class Shop extends React.Component{

    postMessage(e){
      worker.postMessage('ali');
    }

    count(e){
      var counterValue = this.state.counter;
      counterValue++;
      this.setState({counter: counterValue})
    }
    decount(e){
      var counterValue = this.state.counter;
      counterValue--;
      this.setState({counter: counterValue})
    }
    constructor(){
        super();

        this.item1 = "Twisted glass";
        this.value1 = 143;
        this.state = { counter : 1000 };

        setTimeout(() => {
            this.setState({ counter: 2 });
        }, 1000);
        setInterval(() => {
            this.decount(this);
        }, 1000);

        this.worker = new Worker(function(){
          postMessage("I'm working before postMessage('ali').");
          this.onmessage = function(event) {
            postMessage('Hi ' + event.data);
            self.close();
          };
        });
        this.worker.onmessage = function(event) {
          console.log("Worker said : " + event.data);
        };
    }


    render() {
        return (
            <div>
            {this.counter}
                <h1>Welcome to my webshop</h1>
                <p>{this.item1}</p>
                <p>{this.value1}</p>
                <p>{this.state.counter}</p>
                <Label bsStyle="primary">{this.state.counter}</Label>
                <ButtonToolbar>
                    <Button bsStyle="primary">Main page</Button>
                    <Button bsStyle="info">About us!</Button>
                    <Button onClick={this.count.bind(this)} bsStyle="warning">Increase Counter</Button>
                    <Button onClick={this.postMessage.bind(this)} bsStyle="danger">Post message</Button>
                </ButtonToolbar>
             </div>
        )
    };
}

const shopapp = document.getElementById('shop-app');
const shopbuttons = document.getElementById('shop-buttons');

ReactDom.render(<Shop/>, shopapp);
ReactDom.render(<ShopButtons/>, shopbuttons);
