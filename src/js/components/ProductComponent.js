import React from 'react'
import { render } from 'react-dom';
import Product from './../../../stories/Product'

export default class ProductComponent extends React.Component {

  render() {
    const name = 'Nike';
    const price = '36';
    const summary = 'Something about a product';
    const image = 'http://lorempixel.com/400/400/city/';
    const corner = 'Sale';
    return (
      <Product
          name={name}
          price={price}
          summary={summary}
          image={image}
          corner={corner}
        />
    )
  }


}
