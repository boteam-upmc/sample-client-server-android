var express = require('express');
var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var constant = require('./constants');
var handleRobotControlEvents = require('./events-receiver');

server.listen(3000);

io.on(constant.EVENT_CONNECT, function (socket) {
        
    console.log('Client %s connected.', socket.id);    
    
    socket.on('getStream', function (stream) {    
        socket.broadcast.emit('setStream', stream);
    });
    
    handleRobotControlEvents(socket);
});

app.use(express.static('public'));
/*app.use('/', express.static(__dirname + '/public'));

app.all('*', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});*/
