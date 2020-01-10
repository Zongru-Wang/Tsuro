# `interface RuleChecker`

Represents a third party that determines the legality of a given move.  The information needed to determine a move depends on the phase of the game and is passed into this interface's methods when they are called.  Implementing classes are free to decide what counts as a legal or illegal move.

Referees *must* use an implementing instance of this interface in order to check and enforce the game's rules.  Players have access to an implementing intstance of this interface but don't have to use one to check moves if they don't want to.

- `boolean checkInitialPlacement(IRefereeBoard board, IInitialPlacement initialPlacement, List<ITile> choices);`
    - To be called during the start phase of a Tsuro game
	- Arguments
		- `IRefereeBoard board`
			- Should be queried to check if a tile is at the specified `x`, `y` location, and to check the state of surrounding tiles
		- `IInitialPlacement initialPlacement`
			- The initialplacement we should place, we can get:
			- `Token token`:Used to determine if the given token is allowed to place the given tile in the given location
			- `ITile tile`: Can this tile be placed in this location?
			- `int x, int y`: Can the given tile be placed at this location?
			- `Port port`: he port that the given token is to be placed at
		- `List<ITile> choices`
		    - Is the tile is one of choices? If the size is 2?


- `boolean checkIntermediatePlacement(IRefereeBoard board, Token token, ITile tile, int x, int y, List<ITile> choices);`
    - Makes sure the given `ITile` and 2d index `x` and `y` form a valid intermediate tile placement for the given `IToken` and `IRefereeBoard`
    - To be called during the intermediate phase of a Tsuro game
	- Arguments
		- `IRefereeBoard board`
			- Should be queried to check if a tile is at the specified `x`, `y` location, and to check the state of surrounding tiles
		- `Token token`
			- Used to determine if the given token is allowed to place the given tile in the given location
		- `ITile tile`
			- Can this tile be placed in this location?
		- `int x, int y`
			- The port that the given token is to be placed at
		- `List<ITile> choices`
		    - Is the tile is one of choices? If the size is 2?
		
