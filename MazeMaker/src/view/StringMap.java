package view;

import mod.Maze;
import mod.Minotaur;
import mod.Player;
import mod.Sword;

import java.util.ArrayList;
import java.util.List;

//The class responsible for illustrating the game
public class StringMap {


	private final String _wall = "●";
	private final String _path = "○";
	private final String _ply = "P";
	private final String _min = "M";
	private final String _exit = "X";
	private final String _space = "     ";
	private final String _sword = "ⴕ";
	private Maze _maze;
	private Player _plyr;
	//private Minotaur _mint;
	private List<Minotaur> _mintL;
	private Sword _swrd;
	private boolean b = false;

	//The class' constructor that defines the objects in the gui
	public StringMap(Maze m, Player p, List<Minotaur> minotaurList, Sword s) {
		_maze = m;
		_plyr = p;
		_mintL = minotaurList;
		_swrd = s;
	}

	//Creates the map
	public String generateMap() {
		 String map = "";
		 for(int r = 0; r < _maze.getI(); r++) {
			 for(int c = 0; c < _maze.getJ(); c++) {
				 if(_plyr.getRow() == r && _plyr.getCol() == c) {
					 map += _ply + _space;
				 }
				 else if(_swrd.getRow() == r && _swrd.getCol() == c && _plyr.hasSword() == false) {
				 	map += _sword + _space;
				 }
				 else if(_maze.getExit()[0] == r && _maze.getExit()[1] == c) {
					 map += _exit + _space;
				 }
				 else if(_maze.getMaze()[r][c] == true) {
				 	/*for(int i = 0; i < _mintL.size(); i++) {
				 		if(_mintL.get(i).getRow() == r && _mintL.get(i).getCol() == c) {
				 			map += _min + _space;
				 			break;
						}
						else {
							map += _path + _space;
							//break; only works for 1 minotaur
						}
				 	 */
					 for(int i = 0; i < _mintL.size(); i++) {
					 	if (_mintL.get(i).getRow() == r && _mintL.get(i).getCol() == c) {
					 		map += _min + _space;
					 		b = true;
					 		break;
						}
					}
					 if(b == false) {
					 	map += _path + _space;
					 }
					 b = false;
				 }
				 else {
					map += _wall + _space;
				 }
			 }
			 map += "\n";
		 }
		 map += "\n";





		 return map;
	}

}
