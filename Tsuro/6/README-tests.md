To spawn a server and clients
`./xrun`
In `STDIN`, specify clients by:
`[ { "name" : String, "strategy" : String }, ...  ]`
Then send `EOF`

Sample input:
[
{ "name" : "A", "strategy" : "second"},
{ "name" : "B", "strategy" : "second"},
{ "name" : "C", "strategy" : "Dumb"},
{ "name" : "E", "strategy" : "dumb"},
{ "name" : "D", "strategy" : "seCond"}
]


To spawn a server
`./xserver <host:optional> <port:optional>`
where host is a host name, defaulting to localhost, and port is port number, defaulting to 8000.

To spawn a client
`./xclient <host> <port> <name> <strategy>`
where host is a host name, port is port number, name is player name, and strategy is one of : `dumb` or `second`
