// https://nodejs.org/api/net.html
const net = require('net');

var client = new net.Socket();

client.connect(4321, 'host', () => {
	console.log('Connected.');
	
	client.write('Ping ' + new Date());
	client.destroy();
});

client.on('close', () => {
	console.log('Connection Closed.');
});