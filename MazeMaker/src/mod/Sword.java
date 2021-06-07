package mod;

//The class representing a sword
public class Sword {

    //The integer's representing the player's location
    private int _row, _col;

    //A boolean indicating if the player has taken the sword
    private boolean _isTaken;

    public int getRow() { return _row; }
    public int getCol() { return _col; }
    public void setPos(int r, int c) { _row = r; _col = c; }

    //Constructs a sword
    public Sword(int r, int c) {
        _row = r;
        _col = c;
        _isTaken = false;
    }

    public boolean isPresent() { return _isTaken; }
    public void isTaken() { _isTaken = true; }

}