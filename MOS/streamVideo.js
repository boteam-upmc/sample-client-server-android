var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var constant = require('./constants');

server.listen(3000);

io.on(constant.EVENT_CONNECT, function (socket) {
        
    console.log('Client %s connected.', socket.id);    
    
    socket.on('getStream', function (stream) {    
        socket.broadcast.emit('setStream', stream);
    });
});

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});
