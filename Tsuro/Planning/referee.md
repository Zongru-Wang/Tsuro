# Referee

## `interface ITurnHandler`
- Represents an entity that is notified when a player takes their turn.
- `boolean takeInitialTurn(IPlayer player, IInitialPlacement placement)`
	- Called by the given IPlayer to notify this `ITurnHandler` when the player has taken their initial turn.
	- returns `true` if the given `IInitialPlacement` updated the game board, `false` otherwise.
- `boolean takeIntermediateTurn(IPlayer player, IIntermediatePlacement placement)`
	- Called by the given IPlayer to notify this `ITurnHandler` when the player has taken their intermediate turn.
	- returns `true` if the given `IIntermediatePlacement` updated the game board, false otherwise.
- `IRuleChecker getRuleChecker()`
	- Returns the `IRuleChecker` used by this `ITurnHandler` to determine if a given tile placement is valid.

## `interface IGameManager extends ITurnHandler`
- Represents an entity that manages a game of Tsuro.
- `void addPlayer(IPlayer player)`
	- Adds a new `IPlayer` to the game managed by this `IGameManager`.
- `List<IPlayer> getPlayers();`
	- Returns a list of all the players playing the game managed by this `IGameManager`.
- `void startGame()`
	- Starts the game managed by this `IGameManager`.