# Board Interfaces

## `interface IPlayerBoard`
Represents an read-only accessible TsuroBoard.
- `Optional<ITile> get(int x, int y)`: Returns Optional<ITile> that is placed on position x, y. Returns empty `Optional` if there is no tile placed.
	- throws `IndexOutOfBoundsException` if the specified `int x, y` falls outside the bounds of this board.
- `TileLocation getLocationOf(Token token) throws IllegalArgumentException`: Returns the `TileLocation` associated with the given `Token`.
	- throws `IllegalArgumentException` if the given `Token` is not present on this board.

## `class TileLocation`
Represents a location on a TsuroBoard that a Token can be placed on
- `ITile.Port port`: An `ITile.Port` that a `Token` can be placed on.
- `int x`: An x-coordinate on a TsuroBoard in the range [0, 10).
- `int y`: A y-coordinate on a TsuroBoard in the range [0, 10).

## `interface IRefereeBoard extends IPlayerBoard`
Represents an read-write accessible TsuroBoard.
- `void place(ITile tile, int x, int y) throws IllegalArgumentException, IndexOutOfBoundsException`: Place a tile on `x`, `y` position of the board.
	- If the position is already occupied, throws `IllegalArgumentException`.
	- If the position is out of board, throws `IndexOutOfBoundsException`.
- `void placeToken(Token token, ITile.Port port, int x, int y) throws IllegalArgumentException, IndexOutOfBoundsException`: Place a token for a player on x, y position of the board.
	- If `x`, `y` position is out of board, throw `IndexOutOfBoundsException`.
	- If `x`, `y`, port combination are not in periphery of the board, throw `IllegalArgumentException`.
	- If the token is already placed, throw `IllegalArgumentException`.
- `void advanceToken(Token token) throws IllegalArgumentException`: Advances token on the board to follow the path indicated by tiles.
	- If it advances to periphery of the board, removes the token.
	- If the token does not exists, throw `IllegalArgumentException`.
- `void placeFirsts(List<IInitialPlacement> initialPlacements) throws IllegalArgumentException, IndexOutOfBoundsException`: Place an initial tile of the player. 
	- throws `IlelgalArgumentException` if tile is neighbouring other tiles, tile is not place on periphery, or avatar is not placed on port that faces periphery.

## `interface IInitialPlacement` 
Represents an initial placement of a player specified by the location of tile, token type, tile type, port that the token occupies.
- `ITile getTile()`: Represents the tile type of the initial placement.
- `ITile.Port getPort()`: Represents the port in which the token is placed.
- `IToken getToken()`: Represents the token type.
- `int getX()`: Represents the x component of the location of tile placement.
- `int getY()`: Represents the y component of the location of tile placement.

## `enum Token`
Represents an avatar token that is colored. Must be one of `"red"`, `"green"`, `"blue"`, `"white"`, `"black"`
- `String getColorName()`: Represents an string representation of the colore of an avatar token, Must be one of `"red"`, `"green"`, `"blue"`, `"white"`, `"black"`

## `enum Direction`
Represents an general direction. Must be one of `NORTH`, `EAST`, `SOUTH`, `WEST`

## `interface Tile`
- Represents tile of a game of Tsuro
- `Port connectedPort(Port fromPort)`: return the `Port`, where `fromPort` is connected in this Tile
- `SimpleGraph<Port, DefaultEdge> asGraph()`: returns the `SimpleGraph` object of `JGraphT` library representing the port connectivity.
- `static Collection<Tile> loadAll(String jsonFileName) throws FileNotFoundException`: returns list of `Tile`s specified in a json file named `jsonFileName`, throws FileNotFoundException when no such file is found. Json Schema is specified as follow: [`Tile`, `Tile`, ..., `Tile`] where `Tile` is [[`Port`, `Port`],[`Port`, `Port`],[`Port`, `Port`],[`Port`, `Port`]], where `Port` is one of `"A"`,`"B"`,`"C"`,`"D"`,`"E"`,`"F"`,`"G"`,`"H"`.
- `static Collection<Tile> computeRotationWiseAll()`: compute all the possible tile configuration unclassified by rotation.
- `static Collection<Tile> computeAll()`: compute all the possible tile configuration classified by rotation.

## `interface Port extends`
- Represents port on a `Tile`
- `int asNumericIndex()`: represents this port as a numeric index which is in [0, 8). Ports are indexed clockwise starting from `NORT` left port.
- `char asAlphabeticIndex()`: represents this port as an alphabetic index which is one of `'A'`,`'B'`,`'C'`,`'D'`, `'E'`, `'F'`,`'G'`, `'H'`. Ports are indexed clockwise starting from `NORT` left port.
- `Direction getDirection()`: get the direction in which the `Port` is facing on an `Tile`.
- `int getSideIndex()`: facing the `Direction` of `Port`, get 0 if port is on left, 1 if port is on right.
- `static Collection<Port> all()`: get all 8 possible ports.

## `interface Rotatable<E extends Rotatable<E>>`
- Represents an Object that has semantic notion of rotation.
- `E rotateClockwise()`: returns a copy of `E` rotated an Object 90 degrees clockwise.
- `E rotateCounterClockwise()`: returns a copy of `E` rotated an Object 270 degrees clockwise.
- `E rotate(int degrees)`: returns a copy of `E` rotated `degrees`.
- `int angularDifference(E other)`: returns a amount of rotation in degrees for `this` Object to rotate to equal `other`.

# Board Data Representation
## `Board` is represented as JSON as follow:
`[
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
[Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile, Tile], 
]`
## `Tile` is represented as JSON as follow:
`[[Port, Port],[Port, Port],[Port, Port],[Port, Port]]`
## `Port` is one of:
`"A"`,`"B"`,`"C"`,`"D"`,`"E"`,`"F"`,`"G"`,`"H"`
Port are indexed clockwise starting from North Left port.
### `About tile data representation`
`[Port, Port]` represents a connectivity between two ports specified.

