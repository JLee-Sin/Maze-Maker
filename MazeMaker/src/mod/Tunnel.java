package mod;

//The class representing a tunnel in the maze
public class Tunnel {

    //The integer's representing the tunnel's location
    private int _row, _col;

    //Constructs a tunnel
    public Tunnel(int r, int c) {
        _row = r;
        _col = c;
    }

    public int getRow() {
        return _row;
    }

    public int getCol() {
        return _col;
    }
}