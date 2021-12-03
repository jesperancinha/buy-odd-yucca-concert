import React from 'react';
import ReactDom from 'react-dom';
import ShopButtons from './components/ShopButtons';
import ProductComponent from './components/ProductComponent';
import Shop from './components/Shop';
import '../css/main.shop.styles.css';



const shopapp = document.getElementById('shop-app');
const shopbuttons = document.getElementById('shop-buttons');
const productdescription = document.getElementById('product-description');

ReactDom.render(<Shop/>, shopapp);
ReactDom.render(<ShopButtons/>, shopbuttons);
ReactDom.render(<ProductComponent/>, productdescription);
