package mod;

//The class representing a minotaur inside of the mage
public class Minotaur {

	//Integers representing the row and column that the minotaur is located on
	private int _row, _col;

	//The boolean indicating whether the minotaur is alive or not
	private boolean _isAlive;

	public int getRow() { return _row; }
	public int getCol() { return _col; }
	public void setPos(int r, int c) { _row = r; _col = c; }

	//Constructs a minotaur
	public Minotaur(int r, int c) {
		_row = r;
		_col = c;
		_isAlive = true;
	}

	public boolean isAlive() { return _isAlive; }
	public void kill() { _isAlive = false; }
}