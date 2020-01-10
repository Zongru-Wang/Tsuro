# Design
## Game Software
### Presentation Layer: Responsible for server-client communication and relaying client messages to the game state by invoking controller procedure as necessary. 
- `TCPServer`: Responsible for communicating with the client, adhering to the pre-defined protocols. When certain `Game` needs to be updated, created, destroyed, invokes `ServerController` to delegate such responsibilities.
### Controller Layer: Responsible for executing game operations, by accessing domain models via the infrastructure layer and manipulating them, as necessitated by the presentation layer.
- `ServerController`: Responsible for receiving `Game`'s state change request from `TCPServer` and executing the update, destruction, creation, of the certain game state by invoking `GameManager` to query, update, destroy, create `Game`.
### Infrastructure Layer: Responsible for persisting Game model, querying, updating, destroying ended game, and creating a new game. 
- `GameManager`: Responsible for persisting one or more states of `Game`, providing the capability to query, create, update, destroy any of those state's
### Domain Layer: Responsible for retaining a state of Game, and allowing the legal modification of it according to the rules.
- `Game`: Responsible for retaining the information regarding where the player is currently located on `Board`/`Tile`
- `FirstTurn`: Responsible for retaining the valid turn play for a player for the first round
- `Turn`: Responsible for retaining the valid turn play for a player
- `Board`: Responsible for retaining the information regarding where each `Tile` is located, in which `Tile` `Player` is located, manipulating board information to produce the next `Board` as `Player` executes an action.
- `Tile`: Responsible for retaining the information regarding how each port is connected, wherein ports `Player` is located, generate `Tile` rotated with 90, 180, 270 degrees.
- `Player`: Responsible for retaining the information to identify which connection corresponds to which players, the color token of the player.
## Automated Player
### Presentation Layer: Responsible relaying server message to the agent via `AgentController`.
- `TCPClient`: Responsible for communicating with the server and relaying updates to `AgentController`.
### Controller Layer: Responsible for executing certain procedures to create and get the action of `Agent`.
- `AgentController`: Responsible for receiving game state update from `TCPServer` and returning the action decided by `Agent`.
### Domain Layer: Responsible for retaining agents that can act on the current game state and produce an action according to it.
- `Agent`: Responsible for retaining the internal model of the Game and providing action as the game progresses.
# Implementation Plan
## Considered Interfaces and Methods for Domain Layer of Game Software to implement.
### `Game`: Immutable class that keeps track of the current board, current round, current turn, and game status. 
`boolean isEmpty()`, `boolean hasEnded()`, `boolean isOnGoing()`, `Player nextPlayer()`, `List<Player> allPlayers()`, `int currentRound()`, `Turn createCurrentTurn()`, `FirstTurn createCurrentFirstTurn()`, `Game addPlayers(List<Player> players)`, `Game takeTurn(Turn turn)`, `Game takeFirstTurn(First turn)`
### `FirstTurn`: Immutable class that keeps track of 3 legal tile types, legal placements as x, y value, and  Player this turn is intended for.
`List<Tile> legalTiles(Player player)`, `boolean isLegalPlacement(int x, int y, Port port)`, `Player getTargetPlayer()`
### `Turn`: Immutable class that keeps track of 2 legal tile types, legal placements as x, y value, and Player this turn is intended for.
`List<Tile> legalTiles(Player player)`, `boolean isLegalPlacement(int x, int y)`, `Player getTargetPlayer()`
### `Board`: Immutable class that keeps track of tile on a board position with x, y position as key.
`boolean hasTile(int x, int y)`, `Tile getTile(int x, int y)`, `Board placeTile(Player player, Tile tile, int x, int y)`, `boolean isLegal(Player, player, Tile tile, int x, int y)`
### `Port`: enumeration class which is one of:
`NORTH_LEFT`, `NORTH_RIGHT`, `EAST_TOP`, `EAST_BOTTOM`, `SOUTH_RIGHT`, `SOUTH_LEFT`, `WEST_BOTTOM`, `WEST_TOP`
### `Tile`: Immutable class that keeps track of port connectivity, player existence with the port as the key value.
`Port nextPort(Port port)`, `Tile setPlayer(Port port)`, `boolean isOccupied()`, `boolean isOccupied(Port port)`, `List<Player> getPlayers()`, `Player getPlayer(Port port)`, `Tile rotateClockWise()`, `Tile rotateCounterClockWise()`
### `Player`: Immutable class that keeps track of sessionId, the current color token for the game, and connection date/time.
`int getSessionId()`, `String getColor()`, `String getName()`, `int getAge()`
## Other Layers Implementation Plan
Define `Protocol`, Define `JSON` Schema, Implement Unit Operations According to `Protocol` on `ServerController`, Implement Query, Update, Create, Destroy Operations on `GameManager`, Implement Unit Operations According to `Protocol` on `AgentController`, Implement internal game representation on `Agent`, Implement action policy on `Agent`