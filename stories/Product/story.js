import expect from 'expect';
import { storiesOf } from '@kadira/storybook';
import { withKnobs, text } from '@kadira/storybook-addon-knobs';
import { specs, describe, it } from 'storybook-addon-specifications';
import { mount } from 'enzyme';
import React from 'react';
import Product from './index';


const stories = storiesOf('<Product />', module);
stories.addDecorator(withKnobs);

stories.addWithInfo('Basic home',
  'Product landing state',
  () => {
    const name = text('Name', 'Nike');
    const price = text('Price', '36');
    const summary = text('Summary', 'Something about a product');
    const image = text('Image', 'http://lorempixel.com/400/400/cats/1');
    const corner = text('Corner', 'Sale');
    const story = (
      <Product
        name={name}
        price={price}
        summary={summary}
        image={image}
        corner={corner}
      />);

    specs(() => describe('Basic home', () => {
        it('Should have the Hello Joao label', () => {
          let output = mount(story);
          expect(output.text()).toContain('Hello Joao');
        });
      }));

    return story;
  });
