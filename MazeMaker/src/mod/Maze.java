package mod;

import cont.JOP;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jdk.nashorn.internal.scripts.JO;

import java.util.Random;

//The class representing the maze
public class Maze {
	private static int numOfMinotaurs;
	private boolean isDone = false;
	private int cnt =0;
	Random r = new Random();
	//private int _i;
	//private boolean[][] _Maze = {{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true},
			 					 //{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true},
								 //{true, true, true, true, true, true, true, true, true, true}};
	private boolean[][] _Maze;
	private Button[][] buttonGrid = new Button[10][10];
	private Button[][] buttonGridE = new Button[10][10];
	private Button[][] buttonGridPS = new Button[10][10];

	//private final int[][] _plyStartLocations = {{0,2}, {3,2}, {0,8}};
	//private final int[][] _minStartLocations = {{5,9}, {4,0}, {2,4}};
	//private final int[][] _exit = {{5,0}, {9,7}, {6,0}};
	//private final int[][] _sword = {{8,2}, {0,8}, {5,14}};
	
	// currentLocations
	private int[] _curPlyStartLocation;
	private int[][] _curMinStartLocation = {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
	private int[] _curExit;
	private int[] _curSwordLocations;
	private static int _i;
	private static int _j;

	//public void set_curPlyStartLocations(int i) {_curPlyStartLocations = _plyStartLocations[i];}
	//public void set_curMinStartLocation(int i) {_curMinStartLocation = _minStartLocations[i];}
	//public void set_curExit(int i) {_curExit = _exit[i];}
	//public void set_curSwordLocations(int i) { _curSwordLocations = _sword[i];}
	
	public boolean[][] getMaze() { return _Maze; }
	public int[] getPlyStart() { return _curPlyStartLocation; }
	public int[][] getMinStart() { return _curMinStartLocation; }
	public int[] getExit() { return _curExit; }
	public int[] get_swordLocations() { return _curSwordLocations; }
	//public int getMazeNum() {return _i;}
	public int getNumOfMinotaurs() {return numOfMinotaurs;}
	public boolean getIsDone() {return isDone;}
	public int getI() {return _i;}
	public int getJ() {return _j;}
	
	//public void setCurMaze(int mazeNum) {
		//_i = mazeNum;
		//_curMaze = _mazeList[_i];
		//set_curExit(_i);
		//set_curMinStartLocation(_i);
		//set_curPlyStartLocations(_i);
		//set_curSwordLocations(_i);
	//}

	//Helper methods to return random rows and columns
	public int randR() {
		int x = r.nextInt(_i-2)+1;
		return x;
	}

	public int randC() {
		int y = r.nextInt(_j-2)+1;
		return y;
	}

	public int randRWiz() {
		int x = r.nextInt(_i-2)+1;
		return x;
	}

	public int randCWiz() {
		int y = r.nextInt(_j-2)+1;
		return y;
	}

	//Sets the dimensions of the custom maze
	public void setDimensions() {
		Stage stage = new Stage();

		Label ins = new Label("Please input the dimensions of your maze (Must be a number greater than 2");

		TextField inputx = new TextField();
		inputx.setMaxSize(150,50);

		Label x = new Label("X");

		TextField inputy = new TextField();
		inputy.setMaxSize(150,50);

		Button enter = new Button("Enter");
		enter.setDefaultButton(true);
		enter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(inputx.getText() != null && inputy.getText() != null && !inputx.getText().isEmpty() && !inputy.getText().isEmpty()) {
					_i = Integer.parseInt(inputx.getText());
					_j = Integer.parseInt(inputy.getText());
					stage.close();
					makeMaze();
				}
				else {
					JOP.msg("You left one or more boxes blank please try again","Error");
				}

			}
		});

		HBox top = new HBox(ins);
		top.setAlignment(Pos.CENTER);
		top.setPadding(new Insets(50,0,0,0));
		HBox center = new HBox(5.0,inputx,x,inputy);
		center.setAlignment(Pos.BASELINE_CENTER);
		HBox bottom = new HBox(enter);
		bottom.setAlignment(Pos.BASELINE_RIGHT);
		bottom.setPadding(new Insets(50,30,0,0));

		VBox wrapper = new VBox(top,center,bottom);
		wrapper.setSpacing(100);

		Scene scene = new Scene(wrapper,720,480);
		stage.setScene(scene);
		stage.show();
	}

	//Creates the maze based on user input
	private void makeMaze() {
		_Maze = new boolean[_i][_j];
		for(int x = 0; x < _i; x++) {
			for(int y = 0; y < _j; y++) {
				_Maze[x][y] = true;
			}
		}
		JOP.msg("Select buttons to place walls at those positions", " ");
		Stage stage = new Stage();
		GridPane gridPane = new GridPane();

		gridPane.setMaxSize(300, 300);
		gridPane.setMinSize(300, 300);

		for( int i = 0 ; i < _i ; i++) {
			for( int j = 0 ; j < _j ; j++) {
				buttonGrid[i][j] = new Button(" ");
				buttonGrid[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				GridPane.setHgrow(buttonGrid[i][j], Priority.ALWAYS);
				GridPane.setVgrow(buttonGrid[i][j], Priority.ALWAYS);
				gridPane.add(buttonGrid[i][j], i, j);
			}
		}

		for(int i = 0; i<_Maze.length; i++) {
			for(int j = 0; j < _Maze[0].length; j++) {
				final Button myButton = buttonGrid[i][j];
				int finalI = i;
				int finalJ = j;
				myButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						//buttonGrid[finalI][finalJ].getStyleClass().removeAll("addBobOk, focus");
						buttonGrid[finalI][finalJ].setStyle("-fx-background-color: #00ff00");
						_Maze[finalJ][finalI] = false;
					}
				});
			}
		}

		Button close = new Button("Finish");
		close.setDefaultButton(true);
		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(event.getSource().equals(close)) {
					stage.close();
					setExit();
				}
			}
		});

		HBox bottomRow = new HBox(close);
		bottomRow.setAlignment(Pos.CENTER);

		StackPane gridFrame = new StackPane(gridPane);

		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(gridFrame);
		borderPane.setBottom(bottomRow);

		Scene scene = new Scene(borderPane,720,480);

		stage.setScene(scene);
		stage.show();
		stage.setTitle("Choose spaces to be walls");
	}

	//Sets the exit of the maze based on user input
	private void setExit() {
		JOP.msg("Choose where the exit should be", " ");
		Stage stage2 = new Stage();
		GridPane gridPane = new GridPane();

		gridPane.setMaxSize(300, 300);
		gridPane.setMinSize(300, 300);

		for( int r = 0 ; r < _i ; r++) {
			for( int c = 0 ; c < _j ; c++) {
				buttonGrid[r][c] = new Button(" ");
				buttonGrid[r][c].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				GridPane.setHgrow(buttonGrid[r][c], Priority.ALWAYS);
				GridPane.setVgrow(buttonGrid[r][c], Priority.ALWAYS);
				gridPane.add(buttonGrid[r][c], c, r);
				if(_Maze[r][c] == false) {
					buttonGrid[r][c].setStyle("-fx-background-color: #00ff00");
					//buttonGrid[r][c].setText("Wall");
				}
			}
		}

		for(int j = 0; j<_Maze.length; j++) {
			for(int i = 0; i < _Maze[0].length; i++) {
				final Button myButton = buttonGrid[i][j];
				int finalI = i;
				int finalJ = j;
				myButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if(_Maze[finalI][finalJ] == true) {
							//buttonGridE[finalI][finalJ].getStyleClass().removeAll("addBobOk, focus");
							buttonGrid[finalI][finalJ].setStyle("-fx-background-color: #e96df1");
							_curExit = new int[]{finalI, finalJ};
							stage2.close();
							setPlyStart();
						}
						else {
							JOP.msg("This space is already a wall"," ");
						}
					}
				});
			}
		}

		StackPane gridFrame = new StackPane(gridPane);

		Scene scene = new Scene(gridFrame,720,480);

		stage2.setScene(scene);
		stage2.show();
		stage2.setTitle("Choose where the exit should be");
	}

	//sets the player's start location based on user input
	private void setPlyStart() {
		JOP.msg("Choose where the player should start", " ");
		Stage stage = new Stage();
		GridPane gridPane = new GridPane();

		gridPane.setMaxSize(300, 300);
		gridPane.setMinSize(300, 300);

		for( int i = 0 ; i < _i ; i++) {
			for( int j = 0 ; j < _j ; j++) {
				buttonGrid[i][j] = new Button(" ");
				buttonGrid[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				GridPane.setHgrow(buttonGrid[i][j], Priority.ALWAYS);
				GridPane.setVgrow(buttonGrid[i][j], Priority.ALWAYS);
				gridPane.add(buttonGrid[i][j], j, i);
				if(_Maze[i][j] == false) {
					buttonGrid[i][j].setStyle("-fx-background-color: #00ff00");
					//buttonGrid[i][j].setText("Wall");
				}
				else if(_curExit[0] == i && _curExit[1] == j) {
					buttonGrid[i][j].setStyle("-fx-background-color: #e96df1");
					//buttonGrid[i][j].setText("Exit");
				}
			}
		}

		for(int j = 0; j<_Maze.length; j++) {
			for(int i = 0; i < _Maze[0].length; i++) {
				final Button myButton = buttonGrid[i][j];
				int finalI = i;
				int finalJ = j;
				myButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (_Maze[finalI][finalJ] == true && _curExit != new int[]{finalI,finalJ}) {
							//buttonGridE[finalI][finalJ].getStyleClass().removeAll("addBobOk, focus");
							buttonGrid[finalI][finalJ].setStyle("-fx-background-color: #f00000");
							_curPlyStartLocation = new int[]{finalI, finalJ};
							stage.close();
							setSwordLocation();
						}
						else {
							JOP.msg("This space is already a wall or the exit", " ");
						}
					}
				});
			}
		}

		StackPane gridFrame = new StackPane(gridPane);

		Scene scene = new Scene(gridFrame,720,480);

		stage.setScene(scene);
		stage.show();
		stage.setTitle("Choose where the player should start");
	}

	//sets the sword location based on user input
	private void setSwordLocation() {
		JOP.msg("Choose where the sword should be", " ");
		Stage stage = new Stage();
		GridPane gridPane = new GridPane();

		gridPane.setMaxSize(300, 300);
		gridPane.setMinSize(300, 300);

		for( int i = 0 ; i < _i ; i++) {
			for( int j = 0 ; j < _j ; j++) {
				buttonGrid[i][j] = new Button(" ");
				buttonGrid[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				GridPane.setHgrow(buttonGrid[i][j], Priority.ALWAYS);
				GridPane.setVgrow(buttonGrid[i][j], Priority.ALWAYS);
				gridPane.add(buttonGrid[i][j], j, i);
				if(_Maze[i][j] == false) {
					buttonGrid[i][j].setStyle("-fx-background-color: #00ff00");
					//buttonGrid[i][j].setText("Wall");
				}
				else if(_curExit[0] == i && _curExit[1] == j) {
					buttonGrid[i][j].setStyle("-fx-background-color: #e96df1");
					//buttonGrid[i][j].setText("Exit");
				}
				else if(_curPlyStartLocation[0] == i && _curPlyStartLocation[1] == j) {
					buttonGrid[i][j].setStyle("-fx-background-color: #f00000");
					//buttonGrid[i][j].setText("Start");
				}
			}
		}

		for(int j = 0; j<_Maze.length; j++) {
			for(int i = 0; i < _Maze[0].length; i++) {
				final Button myButton = buttonGrid[i][j];
				int finalI = i;
				int finalJ = j;
				myButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (_Maze[finalI][finalJ] == true && _curExit != new int[]{finalI,finalJ} && _curPlyStartLocation != new int[]{finalI,finalJ}) {
							//buttonGridE[finalI][finalJ].getStyleClass().removeAll("addBobOk, focus");
							buttonGrid[finalI][finalJ].setStyle("-fx-background-color: #000000");
							_curSwordLocations = new int[]{finalI, finalJ};
							stage.close();
							PickNumOfMin();
						}
						else {
							JOP.msg("This space is already a wall or the exit", " ");
						}
					}
				});
			}
		}

		StackPane gridFrame = new StackPane(gridPane);

		Scene scene = new Scene(gridFrame,720,480);

		stage.setScene(scene);
		stage.show();
		stage.setTitle("Choose where the sword should be");
	}

	//Allows the user to select the number of minotaurs
	private void PickNumOfMin() {
		Stage stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);

		TextField input = new TextField();
		input.setMaxSize(250,50);

		Button enter = new Button("Enter");
		enter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
				numOfMinotaurs = Integer.parseInt(input.getText());
				getNumOfMin();
			}
		});

		Text t = new Text("How many minotaurs would you like to play with? (Please enter a number)");

		VBox layout = new VBox(20, t, input, enter);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout,400,150);
		stage.setScene(scene);
		stage.show();
	}

	//places the minotaurs on the map depending on user input
	private void getNumOfMin() {
			if(numOfMinotaurs == 0) {
				World.play();
			}
			JOP.msg("Choose where the minotaur(s) should start", " ");
			Stage stage = new Stage();
			GridPane gridPane = new GridPane();

			gridPane.setMaxSize(300, 300);
			gridPane.setMinSize(300, 300);


			for( int i = 0 ; i < _i ; i++) {
				for( int j = 0 ; j < _j ; j++) {
					buttonGrid[i][j] = new Button(" ");
					buttonGrid[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
					GridPane.setHgrow(buttonGrid[i][j], Priority.ALWAYS);
					GridPane.setVgrow(buttonGrid[i][j], Priority.ALWAYS);
					gridPane.add(buttonGrid[i][j], j, i);
					if(_Maze[i][j] == false) {
						buttonGrid[i][j].setStyle("-fx-background-color: #00ff00");
						//buttonGrid[i][j].setText("Wall");
					}
					else if(_curExit[0] == i && _curExit[1] == j) {
						buttonGrid[i][j].setStyle("-fx-background-color: #e96df1");
						//buttonGrid[i][j].setText("Exit");
					}
					else if(_curPlyStartLocation[0] == i && _curPlyStartLocation[1] == j) {
						buttonGrid[i][j].setStyle("-fx-background-color: #f00000");
						//buttonGrid[i][j].setText("Start");
					}
					else if(_curSwordLocations[0] == i && _curSwordLocations[1] == j) {
						buttonGrid[i][j].setStyle("-fx-background-color: #000000");
						//buttonGrid[i][j].setText("Sword");
					}
				}
			}
			//int cnt = 0;

			for(int j = 0; j<_Maze.length; j++) {
				for(int i = 0; i < _Maze[0].length; i++) {
					final Button myButton = buttonGrid[i][j];
					int finalI = i;
					int finalJ = j;
					myButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (_Maze[finalI][finalJ] == true && _curExit != new int[]{finalI,finalJ} && _curPlyStartLocation != new int[]{finalI,finalJ}) {
								//buttonGridE[finalI][finalJ].getStyleClass().removeAll("addBobOk, focus");
								buttonGrid[finalI][finalJ].setStyle("-fx-background-color: #000ff0");
								//int[] temp = new int[]{finalI,finalJ};
								_curMinStartLocation[cnt] = new int[]{finalI, finalJ};
								//stage.close();
								cnt++;
								if(cnt == numOfMinotaurs){
									stage.close();
									isDone = true;
									World.play();
								}
							}
							else {
								JOP.msg("This space is already a wall, the player starting location or the exit", " ");
							}
						}
					});
				}
			}

			StackPane gridFrame = new StackPane(gridPane);

			Scene scene = new Scene(gridFrame,720,480);

			stage.setScene(scene);
			stage.show();
			stage.setTitle("Choose where the minotaur(s) should start");
	}


}
