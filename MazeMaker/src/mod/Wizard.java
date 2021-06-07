package mod;

//The class representing a wizard
public class Wizard {

    //The integer's representing the wizard's location
    private int _row, _col;

    //Constructs a wizard
    public Wizard(int r, int c) {
        _row = r;
        _col = c;
    }

    public int getRow() { return _row; }
    public int getCol() { return _col; }
}