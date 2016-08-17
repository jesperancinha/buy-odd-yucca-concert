import React from 'react';
import { render } from 'react-dom';
import AsyncButton from 'react-async-button';
import ButtonToolbar  from 'react-bootstrap/lib/ButtonToolbar';

export default class ShopButtons extends React.Component {
  clickHandler() {
    return new Promise((resolve, reject) => {
      setTimeout(resolve, 500);
    })
  }

  render() {
    return (
      <AsyncButton
      className="btn"
      text="Saves"
      pendingText="Saving..."
      fulFilledText="Saved Successfully!"
      rejectedText="Failed! Try Again"
      loadingClass="isSaving"
      fulFilledClass="btn-primary"
      rejectedClass="btn-danger"
      onClick={this.clickHandler}/>
    );
  }
}

