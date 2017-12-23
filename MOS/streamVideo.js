var express = require('express');
var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var constant = require('./constants');
var handleRobotControlEvents = require('./events-receiver');

app.use(express.static('public'));
server.listen(3000);

io.on(constant.EVENT_CONNECT, function (socket) {
        
    console.log('Client %s connected.', socket.id);    
    
    socket.on('getStream', function (stream) {    
        socket.broadcast.emit('setStream', stream);
    });        
        
    handleRobotControlEvents(socket);
});
