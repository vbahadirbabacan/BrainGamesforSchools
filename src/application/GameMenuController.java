package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.databaseconnection.util.ConnectToDatabase;

import javafx.scene.image.Image;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

public class GameMenuController implements Initializable{

	public GameMenuController() {
		conn = ConnectToDatabase.connect();
	}
	
    @FXML
    private Button btn1;

    @FXML
    private Button btn2;
    
    @FXML
    private Button showRanksButton;
    
    @FXML
    private AnchorPane centerPane;
    
    @FXML
    private Label rankLabel;

    @FXML
    private Label studentName;

    @FXML
    private Label teacherLabel;

    @FXML
    private AnchorPane topCenterPane;
    
    @FXML
    private HBox topHBox;
    
    @FXML
    private HBox centerHBox;
    
    @FXML
    private ImageView medalImageView;
    
    @FXML
    void btn1_click(ActionEvent event) {

    }
    
    Connection conn = null;
    PreparedStatement query = null;
    ResultSet resultSet = null;
    String sql = null;
    
    class foundStudents{
    	private int id;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}

		public foundStudents(int id) {
			this.id = id;
		}
    }
    
    ObservableList<foundStudents> list = FXCollections.observableArrayList();
    
    @FXML
    void btn2_click(ActionEvent event) throws IOException{
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FollowPathGame.fxml"));
	    Parent root1 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.initStyle(StageStyle.UNIFIED);
	    stage.setTitle("Yolu Takip Et");
	    stage.setScene(new Scene(root1));  
	    stage.show();
    }
    
    @FXML
    void showRanksButtonClick(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClassScoreboard.fxml"));
	    Parent root1 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.initStyle(StageStyle.DECORATED);
	    stage.setTitle("Sınıf Sıralaması");
	    stage.setScene(new Scene(root1));
	    stage.show();
    }
    
    @FXML
    void onMedalClicked(MouseEvent event) {

    }



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		centerPane.setPrefSize(centerPane.getMaxHeight(), centerPane.getMaxWidth());
		list.clear();
		ImageView game1BG = new ImageView(new Image(application.Main.class.getResourceAsStream("colors.png")));
		btn1.setGraphic(game1BG);
		
		ImageView game2BG = new ImageView(new Image(application.Main.class.getResourceAsStream("findpathicon.png")));
		btn2.setGraphic(game2BG);
		
		studentName.setText(GlobalVariables.globalVariables.getStudent().getFirstName() + " " + GlobalVariables.globalVariables.getStudent().getLastName());
		teacherLabel.setText("Öğretmeniniz: " + GlobalVariables.globalVariables.getTeacherName());
		
		topHBox.setHgrow(topCenterPane, Priority.ALWAYS);
				
		try {
			sql = "SELECT DISTINCT students.id, scores.score FROM students, scores WHERE students.id = scores.studentid AND students.classid = ? ORDER BY score DESC LIMIT 50";
			query = conn.prepareStatement(sql);
			query.setInt(1, GlobalVariables.globalVariables.getStudent().getClassId());
			resultSet = query.executeQuery();
			String medalPath;
			
			while (resultSet.next()) {
				list.add(new foundStudents(resultSet.getInt("id")));
				
			}
			
			if (list.get(0).getId() == GlobalVariables.globalVariables.getStudent().getId()) {
				medalPath = "Medal_GoldRank.png";
				rankLabel.setText("Sıralama: 1");
			}
			else if (list.get(1).getId() == GlobalVariables.globalVariables.getStudent().getId()) {
				medalPath = "Medal_SilverRank.png";
				rankLabel.setText("Sıralama: 2");
			}
			else if (list.get(2).getId() == GlobalVariables.globalVariables.getStudent().getId()) {
				medalPath = "Medal_BronzeRank.png";
				rankLabel.setText("Sıralama: 3");
			}
			else {
				medalPath = "Medal_BaseRank.png";
				
				for (int j = 2; j < list.size(); j++) {
					if (list.get(j).getId() == GlobalVariables.globalVariables.getStudent().getId()) {
						rankLabel.setText("Sıralama: " + j + 1);
						System.out.println("Geçilen sıra: " + j);
						break;
				}
				
			}
			list.clear();
			
			medalImageView.setImage(new Image(application.Main.class.getResourceAsStream(medalPath)));
			medalImageView.setRotate(-7.5);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		
		TranslateTransition medalTransition = new TranslateTransition();
		medalTransition.setNode(medalImageView);
		medalTransition.setByY(5);
		medalTransition.setDuration(Duration.millis(2000));
		medalTransition.setCycleCount(TranslateTransition.INDEFINITE);
		medalTransition.setAutoReverse(true);
		medalTransition.play();
		
		RotateTransition medalRotateTransition = new RotateTransition();
		medalRotateTransition.setNode(medalImageView);
		medalRotateTransition.setByAngle(15);
		medalRotateTransition.setDuration(Duration.millis(2000));
		medalRotateTransition.setInterpolator(Interpolator.EASE_BOTH);
		medalRotateTransition.setCycleCount(TranslateTransition.INDEFINITE);
		medalRotateTransition.setAutoReverse(true);
		medalRotateTransition.play();
		
		ScaleTransition medaScaleTransition = new ScaleTransition();
		medaScaleTransition.setNode(medalImageView);
		medaScaleTransition.setDuration(Duration.millis(150));
		medaScaleTransition.setAutoReverse(true);
		medaScaleTransition.setCycleCount(2);
		medaScaleTransition.setInterpolator(Interpolator.EASE_BOTH);
		medaScaleTransition.setByX(-0.25);
		medaScaleTransition.setByY(-0.25);
		
		ScaleTransition medaScaleTransition2 = new ScaleTransition();
		medaScaleTransition2.setNode(medalImageView);
		medaScaleTransition2.setDuration(Duration.millis(150));
		medaScaleTransition2.setAutoReverse(true);
		medaScaleTransition2.setCycleCount(4);
		medaScaleTransition2.setInterpolator(Interpolator.EASE_BOTH);
		medaScaleTransition2.setByX(-1);

		
		medalImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 int info = (int) (Math.random() * 8);
		    	 if (info < 6) {
		    		 medaScaleTransition.play();
			    	 playClickSound("src/application/click.mp3");
				}
		    	 else {
		    		 medaScaleTransition2.play();
			    	 playClickSound("src/application/flip.mp3");
				}
		    	 
		     }
		});

	}
	
	void playClickSound(String filepath) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				String musicFile = filepath;
				Media sound = new Media(new File(musicFile).toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(sound);
				mediaPlayer.play();
			}
		});
	}
}
