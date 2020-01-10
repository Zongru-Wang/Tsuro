# Protocol Specification
## Protocol for a Tsuro Server-Clients interaction
Message from server to client is described as `server=>client-i:MSG`, where `client-i` is `i`th client that this server interacts with, while `MSG` denotes one of message specified in Message Syntax section sent to the client. The message from Client to Server is denoted as `client-i=>server:MSG`
### Start-up
start accepting players and hand out tokens.
- for `i = 1,2,...,N`, where `N` is the number of players:
    - `client-i=>server:JOIN`
    - `server=>client-i:TOKEN`

after accepting 3 players, notify the start the game of Tsuro either after waiting 30 seconds or accepting 2 more players, and stop accepting any new players.
- for `i = 1,2,...,N`, where `N` is the number of players:
    - `server=>client-i:STARTED`

### Initial Placements
- for `i = 1,2,...,N`, where `N` is the number of players:
    - `server=>client-i:INIT_REQ`
    - `client-i=>server:INIT_PLACE`

### Intermediate Placements
- do unttil game is over
    - for `i = 1,2,...,N`, where `N` is the number of players:
        - `server=>client-i:INTER_REQ`
        - `client-i=>server:INTER_PLACE`

### End of the game
- for `i = 1,2,...,N`, where `N` is the number of players:
    - `server=>client-i:ENDED`

## Message Syntax
|Message|Well-formed JSON Syntax|Observation|
|-------|-----------------------|-----------|
|`JOIN`|`PLAYER`|player name|
|`TOKEN`|One of `"red"`, `"green"`, `"blue"`, `"white"`, `"black"`|token color|
|`STARTED`|`"started"`|notifies the start of the game to players|
|`ENDED`|`[[[PLAYER,...],...],[PLAYER,...],BOARD]`|list of list of winners ordered from 1st place winner(stayed on board until the end) to the last place winner(exited the earliest), list of losers, and final board state| 
|`PLAYER`|string|name of player
|`INIT_REQ`|`[BOARD, [TILE, TILE, TILE]]`|Initial turn request to players|
|`INIT_PLACE`|`[TOKEN, TILE, PORT, int, int]`|(Token, Tile, Port, x, y) values of initial token position|
|`INTER_REQ`|`[BOARD, [TILE, TILE]]`|Intermediate turn request to players|
|`INTER_PLACE`|`[TILE, int, int]`|(Token, x, y) values of intermediate tile placement|
|`BOARD`|`[[[TILE_OPT,...],...]. [[TOKEN, PORT, int, int],...]]`|10x10 2d array of optional tile and list of (Token, Port, x, y) value of initial token positions|
|`TILE_OPT`|One of `TILE` or `null`|Optional tile object|
|`TILE`|`[[PORT,PORT],[PORT,PORT],[PORT,PORT],[PORT,PORT]]`|list of port to port connectivity specification|
|`PORT`|One of `"A"`,`"B"`,`"C"`,`"D"`,`"E"`,`"F"`,`"G"`,`"H"`|port's alphabetic index|

