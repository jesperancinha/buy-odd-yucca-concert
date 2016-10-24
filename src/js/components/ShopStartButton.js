import React from 'react';
import AsyncButton from 'react-async-button';

export default class ShoptStartButton extends React.Component {
    clickHandler() {
      return new Promise(resolve, reject) => {

      };
    }

    render(){
      <AsyncButton
        className="btn"
        text="Reset counter"
        pendingText="Stopping counter"
        onClick={this.clickHandler}/>
    }
}
