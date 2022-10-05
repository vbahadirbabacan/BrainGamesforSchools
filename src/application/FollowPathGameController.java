package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.databaseconnection.util.ConnectToDatabase;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class FollowPathGameController implements Initializable{

	public FollowPathGameController() {
		conn = ConnectToDatabase.connect();
	}

	@FXML
    private AnchorPane pane;
    
    @FXML
    private AnchorPane centerPane;
    
    @FXML
    private AnchorPane sTilePane;
    
    @FXML
    private ImageView trueOrFalseImg;
	
    @FXML
    private Button pauseButton;
    
    @FXML
    private ButtonType continueButton;
    
    @FXML
    private Button putTileDowntButton;

    @FXML
    private Button putTileLeftButton;

    @FXML
    private Button putTileRightButton;

    @FXML
    private Button putTileUpButton;
    
    @FXML
    private Label promtLabel;
    
    @FXML
    private Label scoreLabel;
    
    @FXML
    private Label time;
    
    @FXML
    private HBox bottomHbox;

    @FXML
    private VBox centerVbox;
    
    @FXML
    private VBox leftVbox;
    
    @FXML
    private VBox rightVbox;

    TilePosition tilePosition;
    
    Parent pauseRoot;
    
    @FXML
    private HBox topHbox;
    
    @FXML
    private Pane topLeftPane;

    @FXML
    private Pane topRightPane;
    
    int counter = 60;
    int tileCount = 3;
    int tileTimerTick = 1000;
    int globalTileCount;
    static int currentPosX;
    int currentPosY;
    int level = 1;
    int score = 0;
    int direction = 1;    
    boolean isPauseActive = false;
    private ObservableList<TilePosition> tileDirections = FXCollections.observableArrayList();
    boolean onDraw = true; // true when computer draws, false when drawing is on player
    boolean isGameOver = false;
    int controlledIndex = 0;   
	Timer timer = new Timer();	//Oyun süresi timer
	Timer tileTimer = new Timer();	//Tile yerleştirme timer
	
    Connection conn = null;
    PreparedStatement query = null;
    ResultSet resultSet = null;
    String sql = null;
		
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			if (counter > 0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						time.setText(String.valueOf(counter));
						
					}
				});
				counter--;

			}
			else {
				System.out.println("hny");
				timer.cancel();
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						promtLabel.setText("Süre Doldu!");
						try {
							gameOver();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	};
	
	TimerTask tileTask = new TimerTask() {
		
		@Override
		public void run() {
			
			
			if (tileCount > 0) {
				
				onDraw = true;
				// Akılda tutma sırası
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						if(!isGameOver) {
							promtLabel.setText("Aklında tut!");
							determineNextTilePlace();
						}
						
					}
				});
			}
			
			else {
				removeTile();	// Oyuncu çizme aşaması
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						promtLabel.setText("Çiz!");
						
					}
				});
			}
			
		}
	
	};
	
	// Pause event
    @FXML
    void pauseButtonClick(ActionEvent event) throws IOException{
    	
    	Timeline timeline = new Timeline();
    	KeyValue key = new KeyValue(pauseRoot.translateXProperty(), 0, Interpolator.EASE_IN);
    	KeyFrame kf = new KeyFrame(Duration.seconds(0.5), key);
    	timeline.getKeyFrames().add(kf);
    	
       	if(!isPauseActive) {
        	Scene scene = pane.getScene();
        	
        	pauseRoot.translateXProperty().set(-(scene.getWidth() / 3));
        	pauseRoot.setLayoutY(100);
        	
        	pane.getChildren().add(pauseRoot);
       		
        	timeline.setRate(1);
       		timeline.play();
        	isPauseActive=true;
    	}
    	else {
			timeline.setRate(-1);
			timeline.play();
			pane.getChildren().remove(pauseRoot);
			isPauseActive = false;
			
		}
    }
    
    //Ok ekleme fonksiyonu
    public void addTile(InputStream imgPath, int posX, int posY, int rotation, AnchorPane pane) throws FileNotFoundException {
    	Image img = new Image(imgPath);
    	ImageView tileImg = new ImageView(img);
    	tileImg.setX(posX);
    	tileImg.setY(posY);
    	tileImg.setFitHeight(50);
    	tileImg.setFitWidth(50);
    	tileImg.setRotate(rotation);
    	tileImg.setPreserveRatio(true);
    	pane.getChildren().add(tileImg);
    	
    }

    
    void removeTile() {
    	Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				if (onDraw) {
					centerPane.getChildren().clear();
					onDraw = false;
					tilePosition = TilePosition.up;
					System.out.println(tileDirections.toString());
					currentPosX = (int)(centerPane.getPrefWidth() / 2);
					currentPosY = (int)(centerPane.getPrefHeight() / 2);
				}
					
			}
		});
    }
    
    void trueGuess() {
    	Image img = new Image(application.Main.class.getResourceAsStream("FPG_O.png"));
    	trueOrFalseImg.setImage(img);
    	trueOrFalseImg.setPreserveRatio(true);
    	Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				trueOrFalseImg.setVisible(true);
			}
		});
    	
    	promtLabel.setText("Doğru");
		onDraw = true;
		tileCount = globalTileCount;
		score += 10;
		
		if (score >= 20 && score < 40) {
			globalTileCount = 4;
		}
		
		else if (score >= 40 && score < 60) {
			globalTileCount = 5;
		}
		
		else if (score >= 60 && score < 80) {
			globalTileCount = 6;
		}
		scoreLabel.setText("Score: " + score);
		tileDirections.clear();
		controlledIndex = 0;
		centerPane.getChildren().clear();
		
    }
    
    void wrongGuess() throws FileNotFoundException {
    	Image img = new Image(application.Main.class.getResourceAsStream("FPG_X.png"));
    	trueOrFalseImg.setImage(img);
    	trueOrFalseImg.setPreserveRatio(true);
    	controlledIndex = 0;
    	Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				trueOrFalseImg.setVisible(true);
			}
		});
    	onDraw = true;
    	tileCount = globalTileCount;
    	tileDirections.clear();
    	centerPane.getChildren().clear();
    }
    
    void gameOver() throws Exception {
    	sTilePane.getChildren().clear();
    	sql = "INSERT INTO scores (studentid, gameid, score, scoredate) VALUES (?, 1, ?, curdate())";
    	query = conn.prepareStatement(sql);
    	query.setInt(1, GlobalVariables.globalVariables.getStudent().getId());
    	query.setInt(2, score);
    	query.executeUpdate();
    	isGameOver = true;
    }
    
    /*
    tilePosition == right: 0 aşağı, 1 sağ, 2 yukarı
    tilePosition == up: 0 sağ, 1 yukarı, 2 sol
    tilePosition == left: 0 yukarı, 1 sol, 2 aşağı
    tilePosition == down: 0 sol, 1 aşağı, 2 sağ
    */

    void determineNextTilePlace() {
    	onDraw = true;
    	if (tilePosition == TilePosition.right && !isGameOver) {
    		direction = (int) (Math.random() * 3);
			try {
				if (direction == 0 && !isGameOver) {	// r d
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY + 50, 180, centerPane);
					currentPosY += 50;
					tilePosition = TilePosition.down;
					tileDirections.add(tilePosition);
					
				}
				
				else if (direction == 1) {	//	r r
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX + 50, currentPosY, 90, centerPane);
					currentPosX += 50;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
				else {	// r u
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY - 50, 0, centerPane);
					currentPosY -= 50;
					tilePosition = TilePosition.up;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			tileCount--;
    	}
    	
    	else if (tilePosition == TilePosition.up && !isGameOver) {
    		direction = (int) (Math.random() * 3);
			try {
				if (direction == 0) {	// u r
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX + 50, currentPosY, 90, centerPane);
					currentPosX += 50;
					tilePosition = TilePosition.right;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
				else if (direction == 1) {	//	u u
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY - 50, 0, centerPane);
					currentPosY -= 50;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
				else {	// u l
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX - 50, currentPosY, -90, centerPane);
					currentPosX -= 50;
					tilePosition = TilePosition.left;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			tileCount--;
		}
    	else if (tilePosition == TilePosition.left && !isGameOver) {
    		direction = (int) (Math.random() * 3);
			try {
				if (direction == 0) {	// l u
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY - 50, 0, centerPane);
					currentPosY -= 50;
					tilePosition = TilePosition.up;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
				else if (direction == 1) {	//	l l
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX - 50, currentPosY, -90, centerPane);
					currentPosX -= 50;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
				else {	// l d
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY + 50, 180, centerPane);
					currentPosY += 50;
					tilePosition = TilePosition.down;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			tileCount--;
		}
    	else if (!isGameOver) {
    		direction = (int) (Math.random() * 3);
			try {	// d l
				if (direction == 0) {
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX - 50, currentPosY, -90, centerPane);
					currentPosX -= 50;
					tilePosition = TilePosition.left;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
				else if (direction == 1) {	// d d
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY + 50, 180, centerPane);
					currentPosY += 50;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
				else {	// d r
					addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX + 50, currentPosY, 90, centerPane);
					currentPosX += 50;
					tilePosition = TilePosition.right;
					tileDirections.add(tilePosition);
					System.out.println("X: " + currentPosX + "Y: " + currentPosY);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		if (!isGameOver) {
			tileCount--;
			}
		}
    }
    
    void drawTile(TilePosition pos) throws FileNotFoundException{	//0 right, 1 up, 2 left, 3 down
    	if (!isGameOver) {
    		if (pos == TilePosition.right) {
        		addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX + 50, currentPosY, 90, centerPane);
        		currentPosX += 50;
        	}
        	
        	else if (pos == TilePosition.up) {
        		addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY - 50, 0, centerPane);
        		currentPosY -= 50;
    		}
        	
        	else if (pos == TilePosition.left) {
        		addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX - 50, currentPosY, -90, centerPane);
        		currentPosX -= 50;
    		}
        	
        	else {
        		addTile(application.Main.class.getResourceAsStream("FPG_Arrow.png"), currentPosX, currentPosY + 50, 180, centerPane);
        		currentPosY += 50;
    		}
		}
    }
    
    @FXML
    void putTileDowntButtonClick(ActionEvent event) throws FileNotFoundException{
    	if (controlledIndex < tileDirections.size() && tileDirections.get(controlledIndex) == TilePosition.down) {
    		System.out.println("Clicked: " + String.valueOf(TilePosition.down));
    		drawTile(TilePosition.down);
    		controlledIndex++;
    		
    		if (controlledIndex == tileDirections.size()) {
				trueGuess();
				resetPosition();
			}
		}
    	else{
			centerPane.getChildren().clear();
			wrongGuess();
			resetPosition();
    	}
    }

    @FXML
    void putTileRightButtonClick(ActionEvent event) throws FileNotFoundException {
    	if (controlledIndex < tileDirections.size() && tileDirections.get(controlledIndex) == TilePosition.right) {
    		System.out.println("Clicked: " + String.valueOf(TilePosition.right));
    		drawTile(TilePosition.right);
    		controlledIndex++;
		
    		if (controlledIndex == tileDirections.size()) {
				trueGuess();
				resetPosition();
				
			}
    	}
    	else{
			centerPane.getChildren().clear();
			wrongGuess();
			resetPosition();
    	}
    }

    @FXML
    void putTileUpButtonClick(ActionEvent event) throws FileNotFoundException {
    	if (controlledIndex < tileDirections.size() && tileDirections.get(controlledIndex) == TilePosition.up) {
    		System.out.println("Clicked: " + String.valueOf(TilePosition.up));
    		drawTile(TilePosition.up);
    		controlledIndex++;
		
    		if (controlledIndex == tileDirections.size()) {
				trueGuess();
				resetPosition();
			}
    	}
    	else{
			centerPane.getChildren().clear();
			wrongGuess();
			resetPosition();
		}
    }
    
    @FXML
    void putTileLeftButtonClick(ActionEvent event) throws FileNotFoundException {
    	if (controlledIndex < tileDirections.size() && tileDirections.get(controlledIndex) == TilePosition.left) {
    		System.out.println("Clicked: " + String.valueOf(TilePosition.left));
    		drawTile(TilePosition.left);
    		controlledIndex++;
		
    		if (controlledIndex == tileDirections.size()) {
				trueGuess();
				resetPosition();
			}
    	}
    	else{
			centerPane.getChildren().clear();
			wrongGuess();
			resetPosition();
		}
    } 
   
    void resetPosition() {
    	currentPosX = (int)(centerPane.getPrefWidth() / 2);
		currentPosY = (int)(centerPane.getPrefHeight() / 2);
    }
    
    @FXML
    void loadPauseMenu(ActionEvent event) throws IOException {
    	

    	
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
				
		try {
			addTile(application.Main.class.getResourceAsStream("FPG_Start.png"), (int)(centerPane.getPrefWidth() / 2), (int)(centerPane.getPrefHeight() / 2), 0, sTilePane);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			pauseRoot = FXMLLoader.load(getClass().getResource("PauseMenu.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		timer.scheduleAtFixedRate(task, 0, 1000);
		tileTimer.schedule(tileTask, 0, tileTimerTick);
		globalTileCount = tileCount;
		trueOrFalseImg.setVisible(false);
		ImageView menuBackground = new ImageView(new Image(application.Main.class.getResourceAsStream("FPG_Burger.png")));
		pauseButton.setGraphic(menuBackground);
	
		currentPosX = (int)(centerPane.getPrefWidth() / 2);
		currentPosY = (int)(centerPane.getPrefHeight() / 2);
		
		tilePosition = TilePosition.up;
		
		topHbox.setHgrow(topLeftPane, Priority.SOMETIMES);
		topHbox.setHgrow(topRightPane, Priority.SOMETIMES);
		
		bottomHbox.setHgrow(leftVbox, Priority.SOMETIMES);
		bottomHbox.setHgrow(rightVbox, Priority.SOMETIMES);	
	}
}