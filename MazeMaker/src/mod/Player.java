package mod;

//This class represents a player
public class Player {

	//Integers used to specify the player's location
	private int _row, _col;

	//A boolean indicating if the player is alive
	private boolean _isAlive;

	//A boolean indicating if the player has a sword
	private boolean _sword;

	//An integer representing the number of coins a player has
	private int _numOfCoins = 0;

	//A boolean indicating if the player has an invisibility potion
	private boolean _invisibilityPotion;

	//A boolean indicating if the player has a shield
	private boolean _shield;

	public int getRow() { return _row; }
	public int getCol() { return _col; }
	public void setPos(int r, int c) { _row = r; _col = c; }
	public int getNumOfCoins() {return _numOfCoins;}

	//Increments/decrements the player's coins to represent purchases
	public void warp() {_numOfCoins=-5;}
	public void spell() {_numOfCoins=-10;}
	public void invis() {_numOfCoins=-20;}
	public void shield() {_numOfCoins=-30;}

	//Constructs a player
	public Player(int r, int c) {
		_row = r;
		_col = c;
		_isAlive = true;
	}

	public void kill() { _isAlive = false; }

	public boolean hasSword() { return _sword; }
	public void giveSword() { _sword = true; }

	public boolean hasInvisibilityPotion() { return _invisibilityPotion;}
	public void giveInvisibilityPotion() {_invisibilityPotion = true;}
	public void useInvisibilityPotion() {_invisibilityPotion = false;}

	public boolean hasShield() { return _shield;}
	public void giveShield() {_shield = true;}
	public void useShield() {_shield = false;}

	public void addCoin() {
		_numOfCoins++;
	}
}
