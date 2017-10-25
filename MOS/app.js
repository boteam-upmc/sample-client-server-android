var net = require('net');

net.createServer(function(client) {
	console.log('client connected');

	client.on('data', handleData);

	client.on('error', function(error) {
		throw error;
	});

	client.on('end', function() {
		console.log('client disconnected');
	});

	client.write('hello');

	/*c.write('hello\r\n');
	c.pipe(c);*/
}).listen(3000);

handleData = function(data) {
	console.log('data=' + data);
};