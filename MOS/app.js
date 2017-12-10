var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var constant = require('./constants');

// Server 1
server.listen(1337);

/*app.get('/', function (req, res) {
  res.sendfile(__dirname + '/index.html');
});*/

io.on(constant.EVENT_CONNECT, function (socket) {
    
    /*console.log('Client %s connected.', socket.id);    
    socket.emit(constant.EVENT_CONNECT, true);
    socket.emit('event', true);
    
    socket.on(constant.EVENT_DISCONNECT, function() {
        console.log('Client %s disconnected.', socket.id);        
    });*/
    
    socket.on('isSpring', handleSpring);
    
    socket.on('isAndroid', handleAndroid);
});

// Server 2
var server2 = require('http').Server(app);
var io2 = require('socket.io')(server2);
server2.listen(3000);

io2.on(constant.EVENT_CONNECT, function (socket) {
    console.log('**********');
    //socket.on('isSpring', handleSpring);
});

// ********************************************************************************************

function handleSpring () {
        console.log('[Spring side is ready]');
}

function handleAndroid () {
        console.log('[Android side is ready]');
}

// ********************************************************************************************

//var spring = require('socket.io-client')('http://localhost:3000');

var i = require('socket.io-client');
var serverUrl = 'http://localhost:3000'; // change port to 3000 to see difference
var spring = i.connect(serverUrl);
//var spring = new i.Socket('localhost', {port: 1337});

spring.on(constant.EVENT_CONNECT, function() {    
    spring.emit('isSpring', true);
    
    /*forSpring.on('event', function(data) {
        console.log('****************');        
    });*/
    
    //socket.on('disconnect', function(){});
});
