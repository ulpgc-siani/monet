var app = require('connect')();
var serveStatic = require('serve-static');
app.use(serveStatic('../../')).listen(8080);

var open = require('open');

open('http://localhost:8080/cotton-translator/demo/');
