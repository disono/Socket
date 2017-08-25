<?php
	// the port on which we are connecting to the "remote" machine
    $PORT = '4321';

    // the ip of the remote machine (in this case it's the same machine)
    $HOST = "host";

    $sock = socket_create(AF_INET, SOCK_STREAM, 0);

    echo socket_connect($sock, $HOST, $PORT);

    $msg = "Ping " . date('F d Y h:i A', time());
    $len = strlen($msg);

    echo socket_write($sock, $msg, $len);
    socket_close($sock);