import React, { PropTypes as p } from 'react';
import styled from 'styled-components';

const ProductContainer = styled.div`
  max-width: 15em;
  border: 1px solid hotpink;
  border-radius: 2px;
  position: relative;
  min-height: 321px;
`;

const DescriptionContainer = styled.div`
  background-color: white;
  height: 75px;
  margin-top: -0px;
  -moz-transition: height 1s, margin-top 1s;
  -o-transition: height 1s, margin-top 1s;
  -webkit-transition: height 1s, margin-top 1s;
  transition: height 1s, margin-top 1s;
  position:fixed;
  overflow: hidden;
  width: 238px;
    &:hover {
      height: 225px;
      margin-top: -155px;
      -moz-transition: height 1s, margin-top 1s;
      -o-transition: height 1s, margin-top 1s;
      -webkit-transition: height 1s, margin-top 1s;
  }
`;

const Price = styled.div`
  text-align: right;
  color: #2AD291;
`;
const Name = styled.div``;
const Summary = styled.div``;

const Image = styled.img`
  width: 100%;
`;

const Corner = styled.div`
  position: absolute;
  top: 0; right: 0;
  width: 0; height: 0;
  border-style: solid;
  text-indent: 1.3em;
  line-height: 2.2;
  color: #fff;
  font-weight: 400;
  text-transform: uppercase;
  border-width: 0 4em 4em 0;
  border-color: transparent #48DAA1 transparent transparent;

`;

export default function Product(props) {
  const { price, name, summary, image, corner } = props;
  return (<ProductContainer>
    <Corner>{corner}</Corner>
    <Image src={image} alt="image" />
    <DescriptionContainer>
      <Price>{price}</Price>
      <Name>{name}</Name>
      <Summary>{summary}</Summary>
    </DescriptionContainer>
  </ProductContainer>);
}

Product.propTypes = {
  name: p.string.isRequired,
  price: p.string.isRequired,
  summary: p.string.isRequired,
  image: p.string.isRequired,
  corner: p.string.isRequired,
};
