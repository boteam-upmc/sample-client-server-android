var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var constant = require('./constants');

server.listen(3000);

io.on(constant.EVENT_CONNECT, function (socket) {
    
    /*console.log('Client %s connected.', socket.id);    
    socket.emit(constant.EVENT_CONNECT, true);
    socket.emit('event', true);
    
    socket.on(constant.EVENT_DISCONNECT, function() {
        console.log('Client %s disconnected.', socket.id);        
    });*/
      
    console.log('Client %s connected.', socket.id);    

    //console.log('[Getting streaming...]');
    socket.on('getStream', function (stream) {
        //var raw = stream.raw;
        //console.log(raw[9]);
        socket.broadcast.emit('setStream', stream);
    });
    
    /*socket.on('setStream', function () {
        console.log('[Setting streaming...]');
        socket.broadcast.emit('getStream', 'Stream bytes....');
    });*/
    
});

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

/* 
 * Insert association Robot-User 
 * /!\ login & timestamp must be unique !
 * example : insertAssociation(sock, 'boteam', '1513288310');
 */
var insertAssociation = function(sock, login, numSerie) {
    
    // first get id user
    db.this.query('SELECT idUser FROM Users WHERE login = ?', login)
        .on('error', function (err) {
            sock.write(androidClient + 'Association insertion failed. No idUser found.\n');
            console.log(err);
        })
        .on('result', function(idUser) {
            console.log('idUser=' + idUser);
            
            // second get id robot
            db.this.query('SELECT idRobot FROM Robots WHERE numSerie = ?', numSerie)
                .on('error', function (err) {
                    sock.write(androidClient + 'Association insertion failed. No idRobot found.\n');
                    console.log(err);
                })
                .on('result', function(idRobot) {
                    console.log('idRobot=' + idRobot);
            
                    const association  = {
                        idUser : idUser,
                        idRobot : idRobot                        
                    };

                    // finally make the association
                    db.this.query('INSERT IGNORE INTO Users_Robots SET ?', association)
                        .on('error', function (err) {
                            sock.write(androidClient + 'Association insertion failed.\n');
                            console.log(err);
                        })
                        .on('result', function () {
                            sock.write(androidClient + 'Association insertion succeeded.\n');
                            console.log('Users insertion succeeded.');
                        });
                });            
        });            
};
