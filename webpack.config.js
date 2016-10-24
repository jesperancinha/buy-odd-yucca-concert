var debug = process.env.NODE_ENV !=='production';
var CopyWebpackPlugin = require('copy-webpack-plugin');
var webpack = require('webpack');

module.exports = {
  context: __dirname + "/src",
  devtool: debug ? "inline-sourcemap" : null,
  entry: "./js/shop.js",
  module: {
    loaders: [
      {
        test: /\.js?$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
        query: {
          presets: ['react', 'es2015']
        },

      },
      {
        test: /\.less/,
        loader: 'less-loader'
      },
      {
        test: /\.css/, loader:
        'css-loader' },
        {
          test: /\.(woff2|woff|ttf|svg|eot)$/,
          loader: 'file-loader'
        }
      ]
    },
    output: {
      path: __dirname + '/app',
      filename: "shop.min.js",
      publicPath: '/app'
    },
    plugins: [
      new webpack.optimize.DedupePlugin(),
      new webpack.optimize.OccurenceOrderPlugin(),
      new webpack.optimize.UglifyJsPlugin({ mangle: false, sourcemap: false}),
      new CopyWebpackPlugin([
        {
          from: 'index.html',
          to: 'index.html',
        }
      ]
    )
  ]
}
