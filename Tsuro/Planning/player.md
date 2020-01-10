# Player

## `interface IPlayer`
- Represents an entity playing the game Tsuro.
- `String getPlayerIdentifier()`
	- Returns the `String` identifying this `IPlayer`
- `void notifyInitialTurn(IPlayerBoard board, List<ITile> availableTiles, ITurnHandler turnHandler)`
	- Called by an `IGameManager` to notify this player when it is their turn to make an initial placement.
	- This player should call the given `ITurnHandler`'s `takeInitialTurn` method when this player's turn has been completed.
- `void notifyIntermediateTurn(IPlayerBoard board, List<ITile availableTiles, ITurnHandler turnHandler)`
	- Called by an `IGameManager` to notify this player when it is their turn to make an intermediate placement.
	- This player should call the given `ITurnHandler`'s `takeInitialTurn` method when this player's turn has been completed.
- `void notifyLoss(IPlayerBoard board)`
	- Called by an `IGameManager` to notify this player when they have lost the game.
- `void notifyWin(IPlayerBoard board)`
	- Called by an `IGameManager` to notify this player when they have won the game.
- `void notifyEndOfGame(IPlayerBoard board)`
	- Called by an `IGameManager` to notify this player when the game has ended.