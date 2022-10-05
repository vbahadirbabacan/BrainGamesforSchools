package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PauseMenuController {


    @FXML
    private Button exitButton;

    @FXML
    private Button restartButton;
    
    @FXML
    private AnchorPane backPane;



    @FXML
    void exitButtonClick(ActionEvent event) {
    	((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void restartButtonClick(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FollowPathGame.fxml"));
	    Parent root1 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.initStyle(StageStyle.UNIFIED);
	    stage.setTitle("Yolu Takip Et");
	    stage.setScene(new Scene(root1));  
	    stage.show();
	    ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}
