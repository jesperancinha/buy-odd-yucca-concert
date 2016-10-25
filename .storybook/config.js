import { configure, setAddon } from '@kadira/storybook';
import infoAddon from '@kadira/react-storybook-addon-info';
import { setOptions } from '@kadira/storybook-addon-options';
import '../src/css/styles.css';

const req = require.context('../stories', true, /story\.js$/);

function loadStories() {
  req.keys().forEach(req);
}

setAddon(infoAddon);
setOptions({
  name: 'Joao',
  url: 'http://jesper-anci.blogspot.nl/',
});

configure(loadStories, module);
