# `interface Observer`

Represents an “observing component” which should get updated by the game system and provide renderings of the current game state.

- `void update(IPlayerBoard board, Map<Token, List<ITile>> choices, Token turn, int round, List<Token> winners, List<Token> losers);`
    - Get the updated state from the given `IPlayerBoard`, the `choices` for each token, the `turn`, and the `round` number, who is/are `winners`/`losers`
	- Arguments
		- `IPlayerBoard board`
		    - Current board with tiles and tokens' locations information
	    - `Map<Token, List<ITile>>  tokenChoices`
	        - Each Token with the current tiles that they received
	   - `Token turn`
	        - Whose turn?
	   - `int round`
	        - Which round?
	   - `List<Token> winners`
	        - Who has/have won
	   - `List<Token> losers`
	        - Who has/have lost

- `void rendering();`
    - After `Observer` gets the updated, rendering most updated game state that the `Observer` received
