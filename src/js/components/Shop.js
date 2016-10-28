import React from 'react';

export default class Shop extends React.Component{

    // postMessage(e){
    //   worker.postMessage('ali');
    // }

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

        // setTimeout(() => {
        //     this.setState({ counter: 2 });
        // }, 1000);
        // setInterval(() => {
        //     this.decount(this);
        // }, 1000);

        // this.worker = new Worker(function(){
        //   postMessage("I'm working before postMessage('ali').");
        //   this.onmessage = function(event) {
        //     postMessage('Hi ' + event.data);
        //     self.close();
        //   };
        // });
        // this.worker.onmessage = function(event) {
        //   console.log("Worker said : " + event.data);
        // };
    }


    render() {
        return (
            <div>
            {this.counter}
                <h1>Welcome to my webshop</h1>
                <div id='shop-main-buttons'>
                    <button id='shop-button-main-page'>Main page</button>
                    <button id='shop-button-about-us'>About us!</button>
                    <button id='shop-button-increase-counter' onClick={this.count.bind(this)}>Increase Counter</button>
                    {/* <button onClick={this.postMessage.bind(this)}>Post message</button> */}
                </div>
             </div>
        )
    };
}
