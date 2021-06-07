package mod;

import cont.JOP;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.StringMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//The class representing the game itself
public class World {
	private static final Random rand = new Random();
	private static Player _player;
	//private Minotaur _minotaur;
	private static List<Minotaur> listOfMinotaurs = new ArrayList<>();
	private static Maze _maze;
	private static Sword _sword;
	private static Tunnel _tunnel;
	private static Wizard _wiz;
	private static StringMap _s;
	private static int _cnt;
	private static int _rpm = 0;
	private static int _cpm = 0;
	private static boolean _noMins = false;
	private static boolean _wizKill = false;
	private static int _inviscnt;
	private Stage instructionStage = new Stage(StageStyle.UNDECORATED);
	private final String _instructionText = "Welcome to the Maze Game. Use WASD to move or E to exit at anytime. To win either navigate yourself," +
											" identified as the letter P to the exit, marked X, or kill the Minotaur, M, after collecting the Sword, ⴕ. " +
											"Coins are given for each step you take and the amount of coins you have will reset each game, " +
											"Coins, Tunnels, and Wizards are hidden on the map and thus " +
											"cannot be seen. You can check your inventory by pressing I at anytime, the inventory shows if you have a sword or a shield and " +
											"the number of coins you have. You can access the shop at anytime by pressing Z, the Shop allows you to buy special items that you " +
											"can use to help you. There are also randomly placed events hidden throughout the maze. While making the maze select buttons to place important spots. Green represents Walls, Blue represents Minotaurs, Red is the start location, Black is where the Sword is and Pink is the Exit";

	//Starts the program
	public World() {
		instructionStage.setTitle("Instructions");

		Label instructions = new Label(_instructionText);
		instructions.setWrapText(true);
		instructions.setFont(Font.font("System",16));

		Button close = new Button("Close");
		close.setDefaultButton(true);
		close.setOnAction(e -> {
			try {
				start();
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		});

		VBox layout = new VBox(instructions, close);
		layout.setSpacing(70);
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setMargin(instructions, new Insets(60.0,20.0,10.0,20.0));

		Scene scene = new Scene(layout,720,480);
		instructionStage.setScene(scene);
		instructionStage.show();
	}

	//Constructs the appropriate number of minotaurs
	public static void makeMins() {
		int x = _maze.getNumOfMinotaurs();
		for(int i = 0; i < x; i++) {
			listOfMinotaurs.add(new Minotaur(_maze.getMinStart()[i][0],_maze.getMinStart()[i][1]));
		}
	}

	//Starts the gui
	private void start() throws InterruptedException {
		_maze = new Maze();
		instructionStage.close();
		selectMaze();
	}

	//Sets up the gui and constructs the necessary objects in the maze
	public static void play() {
		//JOP.msg("Enjoy the game!", " ");
		// startROW startCOL
		System.out.println("play started");
		_player = new Player(_maze.getPlyStart()[0], _maze.getPlyStart()[1]);
		System.out.println("made player");
		_sword = new Sword(_maze.get_swordLocations()[0], _maze.get_swordLocations()[1]);
		System.out.println("made sword");
		_tunnel = new Tunnel(_maze.randR(), _maze.randC());
		System.out.println("made tunnel");
		makeMins();
		if(listOfMinotaurs.size() == 0) {
			_noMins = true;
		}
		else {
			_noMins = false;
			makeWiz();
		}
		System.out.println("made mins");
		System.out.println("made wiz");
		while(!legitTun()) {
			if(_maze.randR() < _maze.getI()-2 &&  _maze.randC() < _maze.getJ()-2){
				_tunnel = new Tunnel(_maze.randR()+1, _maze.randC()+1);
			}
			else if(_maze.randR() >= _maze.getI()-2 && _maze.randC() < 2) {
				_tunnel = new Tunnel(_maze.randR()-1, _maze.randC()+1);
			}
			else if(_maze.randC() >= 2 && _maze.randR() < _maze.getI()-2) {
				_tunnel = new Tunnel(_maze.randR()+1, _maze.randC()-1);
			}
			else if(_maze.randR() >= _maze.getI() && _maze.randC() >= _maze.getJ()) {
				_tunnel = new Tunnel(_maze.randR()-1, _maze.randC()-1);
			}
		}
		System.out.println("checked tunnels");
		_s = new StringMap(_maze, _player, listOfMinotaurs, _sword);
		update();
	}

	//The loop that updates the gui
	public static void update() {
		boolean isPlaying = true;
		while (isPlaying) {
			boolean on = true;
			// Get the Map
			String map = _s.generateMap();
			String msg = "WASD to move, E to exit, I for inventory, Z for shop. What direction do you want to move?";
			hasSword();
			//if (hasSword())
				//JOP.msg("You got the Sword, you can now kill the Minotaur", "Sword");

			while (on) {
				// Get the player move
				String move = JOP.in(map + msg, "Maze Game");

				// move the player
				if (move == null) {

				} else if (getPlayerMove(move)) {
					on = false;
				}
			}

			// move the minotaur and account for invis potion
			if(!_player.hasInvisibilityPotion()) {
				moveMinotaur();
			}
			else if(_player.hasInvisibilityPotion()) {
				_inviscnt++;
				if(_inviscnt == 5) {
					_player.useInvisibilityPotion();
					_inviscnt = 0;
				}
			}
			for(int i = 0; i < listOfMinotaurs.size(); i++) {
				if(listOfMinotaurs.get(i).getRow() == _player.getRow() && listOfMinotaurs.get(i).getCol() == _player.getCol() && _player.hasShield()) {
					JOP.msg("You managed to block the Minotaur's blow but your shield crumbled and sent you flying", "Block");
					_player.useShield();
					if(_maze.getMaze()[_player.getRow() - 1][_player.getCol() - 1] == true) {
						_player.setPos(_player.getRow()-1, _player.getCol()-1);
					}
					else if(_maze.getMaze()[_player.getRow() + 1][_player.getCol() - 1] == true) {
						_player.setPos(_player.getRow()+1, _player.getCol()-1);
					}
					else if(_maze.getMaze()[_player.getRow() - 1][_player.getCol() + 1] == true) {
						_player.setPos(_player.getRow()-1, _player.getCol()+1);
					}
					else if (_maze.getMaze()[_player.getRow() + 1][_player.getCol() + 1] == true) {
						_player.setPos(_player.getRow()+1, _player.getCol()+1);
					}
					update();
				}
			}

			if(_noMins == false) {
				//check if minotaur has been killed
				if (kill() && !_wizKill) {
					isPlaying = false;
					JOP.msg("Congrats, you killed all the Minotaurs and found a way out of the maze in " + _cnt + " steps !", "Victory");
					JOP.msg("Maybe try a different maze???", "Victory");
					System.exit(0);
				}

				if(_wizKill) {
					isPlaying = false;
					JOP.msg("Congrats, you paid a Wizard to kill the Minotaur and found a way out of the maze in " + _cnt + " steps !", "Victory");
					JOP.msg("Maybe try a different maze???", "Victory");
					System.exit(0);
				}

				if(_player.getRow() == _wiz.getRow() && _player.getCol() == _wiz.getCol()) {
					cast();
				}
			}
			else{
				for(int i = 0; i < listOfMinotaurs.size(); i++) {
					if (victory() && listOfMinotaurs.get(i).isAlive()) {
						isPlaying = false;
						JOP.msg("Congrats, you escaped by finding the exit in " + _cnt + " steps !", "Victory");
						JOP.msg("Maybe try a different maze???", "Victory");
						System.exit(0);
					}
				}

				// check for death
				if (death()) {
					isPlaying = false;
					JOP.msg("Wow what a loser. You suck. You lost, even after " + _cnt + " steps.", "Lost");
					JOP.msg("Maybe try a different maze???", "Lost");
					System.exit(0);
				}

				if(_player.getRow() == _tunnel.getCol() && _player.getCol() == _tunnel.getCol()) {
					warp();
				}
			}
			// check for victory by escape
			if (victory()) {
				isPlaying = false;
				JOP.msg("Congrats, you escaped by finding the exit in " + _cnt + " steps !", "Victory");
				JOP.msg("Maybe try a different maze???", "Victory");
				System.exit(0);
			}

			// check for death
			if (death()) {
				isPlaying = false;
				JOP.msg("Wow what a loser. You suck. You lost, even after " + _cnt + " steps.", "Lost");
				JOP.msg("Maybe try a different maze???", "Lost");
				System.exit(0);
			}

			if(_player.getRow() == _tunnel.getCol() && _player.getCol() == _tunnel.getCol()) {
				warp();
			}
		}


	}

	// gets the player's input and updates the game
	private static boolean getPlayerMove(String s) {
		//Go to Shop
		if(s.equalsIgnoreCase("Z")) {
			JOP.msg("Shopkeeper: Welcome to the shop, what item would you like", "Shop");
			goToShop();
			return false;
		}
		//Check inventory
		if(s.equalsIgnoreCase("I")) {
			checkInventory();
			return false;
		}
		//Change Difficulty
		//if (s.equalsIgnoreCase("X")) {
			//play();
		//}
		//End program
		if (s.equalsIgnoreCase("E")) {
			JOP.msg("Thank you for playing, close this message to end the game. ", "Exit");
			System.exit(0);
		}
		// Moving North
		if (s.equalsIgnoreCase("W")) {
			if ((_player.getRow() - 1) >= 0 && _maze.getMaze()[_player.getRow() - 1][_player.getCol()]) {
				_player.setPos(_player.getRow() - 1, _player.getCol());
				coinAndStep();
				return true;
			} else {
				return false;
			}
		}
		// Moving South
		if (s.equalsIgnoreCase("S")) {
			if ((_player.getRow() + 1) < _maze.getMaze().length && _maze.getMaze()[_player.getRow() + 1][_player.getCol()]) {
				_player.setPos(_player.getRow() + 1, _player.getCol());
				coinAndStep();
				return true;
			} else {
				return false;
			}
		}
		// Moving East
		if (s.equalsIgnoreCase("D")) {
			if ((_player.getCol() + 1) < _maze.getMaze()[0].length && _maze.getMaze()[_player.getRow()][_player.getCol() + 1]) {
				_player.setPos(_player.getRow(), _player.getCol() + 1);
				coinAndStep();
				return true;
			} else {
				return false;
			}
		}
		// Moving West
		if (s.equalsIgnoreCase("A")) {
			if ((_player.getCol() - 1) >= 0 && _maze.getMaze()[_player.getRow()][_player.getCol() - 1]) {
				_player.setPos(_player.getRow(), _player.getCol() - 1);
				coinAndStep();
				return true;
			} else {
				return false;
			}
		}
		else if(s.isEmpty()) {
			JOP.msg("Input box was blank, try again", "Exit");
			return false;
		}
		else {
			JOP.msg("Please input a valid command.", "Error");
			return false;
		}
	}

	//The minotaur's AI
	private static void moveMinotaur() {
		for(int i = 0; i < listOfMinotaurs.size(); i++) {
			int rDist = _player.getRow() - listOfMinotaurs.get(i).getRow();
			int cDist = _player.getCol() - listOfMinotaurs.get(i).getCol();
			int r = listOfMinotaurs.get(i).getRow();
			int c = listOfMinotaurs.get(i).getCol();

			// Minotaur moving North
			if (rDist < 0 && _maze.getMaze()[r - 1][c]) {
				listOfMinotaurs.get(i).setPos(r - 1, c);
			}

			// Minotaur moving South.
			if (rDist > 0 && _maze.getMaze()[r + 1][c]) {
				listOfMinotaurs.get(i).setPos(r + 1, c);
			}
			// Minotaur moving East
			if (cDist > 0 && _maze.getMaze()[r][c + 1]) {
				listOfMinotaurs.get(i).setPos(r, c + 1);
			}
			// Minotaur moving west
			if (cDist < 0 && _maze.getMaze()[r][c - 1]) {
				listOfMinotaurs.get(i).setPos(r, c - 1);
			}
		}
	}

	//Checks if the player has a sword
	public static boolean hasSword() {
		if (_player.getRow() == _sword.getRow() && _player.getCol() == _sword.getCol() && _player.hasSword() == false) {
			_player.giveSword();
			JOP.msg("You got the Sword, you can now kill the Minotaur", "Sword");
			return true;
		}
		return false;
	}

	//Kills the player
	public static boolean death() {
		_player.kill();
		for(int i = 0; i < listOfMinotaurs.size(); i++) {
			if(listOfMinotaurs.get(i).getRow() == _player.getRow() && listOfMinotaurs.get(i).getCol() == _player.getCol() && !_player.hasSword()) {
				return true;
			}
		}
		return false;
	}

	//Kills the minotaur
	public static boolean kill() {
		int cnt = 0;
		for(int i = 0; i < listOfMinotaurs.size(); i++) {
			if (listOfMinotaurs.get(i).getRow() == _player.getRow() && listOfMinotaurs.get(i).getCol() == _player.getCol() && _player.hasSword()) {
				listOfMinotaurs.get(i).kill();
				cnt++;
			}
		}
		if(cnt == listOfMinotaurs.size()) {
			return true;
		}
		else {
			return false;
		}
	}

	//Ends the game
	public static boolean victory() {
		int cnt = 0;
		for(int i = 0; i < listOfMinotaurs.size(); i++) {
			if(!listOfMinotaurs.get(i).isAlive()) {
				cnt++;
			}
		}
		if (_player.getRow() == _maze.getExit()[0] && _player.getCol() == _maze.getExit()[1])
			return true;
		else if(cnt == listOfMinotaurs.size() && _noMins == false) {
			return true;
		}
		return false;
	}

	//Starts the creation of a maze based on user input
	private void selectMaze() throws InterruptedException {
		_maze.setDimensions();
	}

	//The helper method to prevent array out of bounds errors when creating a tunnel
	public static boolean legitTun() {
		int r = _tunnel.getRow();
		int c = _tunnel.getCol();
		boolean spot = _maze.getMaze()[r][c];
		return spot;
	}

	//Moves the player's position when they use a tunnel
	public static void warp() {
		JOP.msg("You found a Tunnel", "Tunnel");
		String decide = "Would you like to use it? (Y/N)";
		String decision = JOP.in(decide, "Tunnel");
		if (decision.equalsIgnoreCase("Y") && _player.getNumOfCoins() >= 5) {
			_player.warp();
			findExit();
			while(!_maze.getMaze()[_rpm][_cpm]) {
				if(_rpm > _maze.getI()-2) {
					_rpm--;
				}
				else if(_rpm < 2) {
					_rpm++;
				}
				if(_cpm > _maze.getJ()-2){
					_cpm--;
				}
				else if(_cpm < _maze.getJ()-2) {
					_cpm++;
				}
			}
			_player.setPos(_rpm,_cpm);
		}
		else if (decision.equalsIgnoreCase("N")) {
			JOP.msg("You back away from the tunnel and head back into the maze", "Tunnel");
		}
		else if (_player.getNumOfCoins() < 5) {
			JOP.msg("Sorry, you dont have enough coins to use the tunnel", "Tunnel");
		}
		else if (!decision.equalsIgnoreCase("Y") && !decision.equalsIgnoreCase("N")) {
			JOP.msg("Please give a valid input", "Error");
			warp();
		}
	}

	//Finds the exit of tunnel
	public static void findExit() {
		_rpm = rand.nextInt(_maze.getI()-2)+1;
		_cpm = rand.nextInt(_maze.getJ()-2)+1;
	}

	//Increments the number of coins and steps
	public static void coinAndStep() {
		_player.addCoin();
		_cnt++;
	}

	//Checks the player's inventory
	public static void checkInventory() {
		if(_player.hasSword() && _player.hasShield()) {
			JOP.msg("You have a sword, a shield and " + _player.getNumOfCoins() + " coins", "Inventory");
		}
		else if(_player.hasSword()) {
			JOP.msg("You have a sword and " + _player.getNumOfCoins() + " coins", "Inventory");
		}
		else if (_player.hasShield()) {
			JOP.msg("You have a shield and " + _player.getNumOfCoins() + " coins", "Inventory");
		}
		else {
			JOP.msg("You have " + _player.getNumOfCoins() + " coins", "Inventory");
		}
	}

	//The helper method to prevent array out of bounds errors when placing a wizard
	public static boolean legitWiz() {
		int rW = _wiz.getRow();
		int cW = _wiz.getCol();
		boolean spot = _maze.getMaze()[rW][cW];
		return spot;
	}

	//Starts the player's interaction with a wizard
	public static void cast() {
		JOP.msg("A mysterious cloaked figure approaches you", "Event");
		JOP.msg("Wizard: Hello young traveller, for a small price I will rid you of the beasts in this place.", "Wizard");
		String choice = "Would you like me to use my magic? (Y/N)";
		String madeChoice = JOP.in(choice, "Wizard");
		if (madeChoice.equalsIgnoreCase("Y") && _player.getNumOfCoins() >= 10) {
			JOP.msg("Wizard: Very well, it shall be done", "Wizard");
			_wizKill = true;
			_player.spell();
			for(int i = 0; i < listOfMinotaurs.size(); i++) {
				listOfMinotaurs.get(i).kill();
			}
		} else if (madeChoice.equalsIgnoreCase("N")) {
			JOP.msg("Wizard: Very well I shall await here in the meantime...", "Wizard");
		}
		else if (_player.getNumOfCoins() < 10) {
			JOP.msg("It appears you have insufficient funds, please return here once you have 10 coins", "Wizard");
		}
		else if (!madeChoice.equalsIgnoreCase("Y") && !madeChoice.equalsIgnoreCase("N")) {
			JOP.msg("Please give a valid input", "Error");
			cast();
		}
	}

	//The method that opens the shop and allows the player to purchase items
	public static void goToShop() {
		String items = "Press I for an Invisibility Potion, press S for a Shield, press R for a Return Potion or press E to leave";
		String chosenItem = JOP.in(items, "Shop");
		if(chosenItem.equalsIgnoreCase("I")) {
			String invis = "The Potion of Invisibility will stop the Minotaur for 5 moves, it costs 20 coins and takes effect immediately. Would you like this item? (Y/N)";
			String invischoice = JOP.in(invis, "Potion of Invisibility");
			if(_player.getNumOfCoins() < 20) {
				JOP.msg("It seems like you dont have enough for this...", "Shop");
				goToShop();
			}
			else if(invischoice.equalsIgnoreCase("Y") && _player.getNumOfCoins() >= 20) {
				_player.invis();
				_player.giveInvisibilityPotion();
				JOP.msg("Shopkeeper: Thank you, please come again soon", " ");
			}
			else if(invischoice.equalsIgnoreCase("N")) {
				JOP.msg("Shopkeeper: Is there anything else that catches your eye?", "Shop");
				goToShop();
			}
		}
		if(chosenItem.equalsIgnoreCase("S")) {
			String shield = "The Shield will protect you from the Minotaur once but looks flimsy and can't be used with a sword, it costs 30 coins. Would you like this item? (Y/N)";
			String shieldchoice = JOP.in(shield, "Shield");
			if(_player.getNumOfCoins() < 30) {
				JOP.msg("It seems like you dont have enough for this...", "Shop");
				goToShop();
			}
			else if(_player.hasSword()) {
				JOP.msg("This is too heavy to hold with your sword so you put it back on the shelf", "Shop");
				goToShop();
			}
			else if(shieldchoice.equalsIgnoreCase("Y") && _player.getNumOfCoins() >= 30 && !_player.hasSword()) {
				_player.shield();
				_player.giveShield();
				JOP.msg("Shopkeeper: Thank you, please come again soon", "Shop");
			}
			else if(shieldchoice.equalsIgnoreCase("N") && _player.getNumOfCoins() >= 30 && !_player.hasSword()) {
				JOP.msg("Shopkeeper: Is there anything else that catches your eye?", "Shop");
				goToShop();
			}
		}
		if(chosenItem.equalsIgnoreCase("R")) {
			String ret = "The Potion of Returning will bring you to the entrance of the maze, it costs 5 coins and takes effect immediately. Would you like this item? (Y/N)";
			String retchoice = JOP.in(ret, "Potion of Returning");
			if(_player.getNumOfCoins() < 5) {
				JOP.msg("It seems like you dont have enough for this...", "Shop");
				goToShop();
			}
			else if(retchoice.equalsIgnoreCase("Y") && _player.getNumOfCoins() >= 5) {
				_player.warp();
				JOP.msg("Shopkeeper: Thank you, please come again soon", "Shop");
				_player.setPos(_maze.getPlyStart()[0], _maze.getPlyStart()[1]);
				JOP.msg("You were suddenly moved in a gust of wind", "Shop");
				update();
			}
			else if(retchoice.equalsIgnoreCase("N")) {
				JOP.msg("Shopkeeper: Is there anything else that catches your eye?", "Shop");
				goToShop();
			}
		}
		if(chosenItem.equalsIgnoreCase("E")) {
			JOP.msg("Shopkeeper: Please come again soon", "Shop");
		}
	}

	//The method that places and constructs a wizard
	public static void makeWiz() {
		_wiz = new Wizard(_maze.randRWiz(), _maze.randCWiz());
		System.out.println("wizard constructor call");
		while(!_maze.getMaze()[_wiz.getRow()][_wiz.getCol()]) {
			System.out.println("while loop started");
			if(_maze.randRWiz() > 2 && _maze.randRWiz() < _maze.getI()-2 && _maze.randCWiz() > 2 && _maze.randCWiz() < _maze.getJ()-2){
				_wiz = new Wizard(_maze.randRWiz()+1, _maze.randCWiz()+1);
			}
			else if(_maze.randRWiz() >= _maze.getI()-2 && _maze.randCWiz() < _maze.getJ()-2) {
				_wiz = new Wizard(_maze.randRWiz()-1, _maze.randCWiz()+1);
			}
			else if(_maze.randCWiz() >= _maze.getJ()-2 && _maze.randRWiz() < _maze.getI()-2) {
				_wiz = new Wizard(_maze.randRWiz()+1, _maze.randCWiz()-1);
			}
			else if(_maze.randRWiz() >= _maze.getI()-2 && _maze.randRWiz() >= _maze.getI()-2) {
				_wiz = new Wizard(_maze.randRWiz()-1, _maze.randCWiz()-1);
			}
		}
	}
}