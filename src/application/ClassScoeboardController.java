package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.databaseconnection.util.ConnectToDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClassScoeboardController implements Initializable{
	
	public ClassScoeboardController() {
		conn = ConnectToDatabase.connect();
	}

    @FXML
    private Button goBackButton;

    @FXML
    private TableColumn<ScoreboardItem, Integer> lastNameCol;

    @FXML
    private TableColumn<ScoreboardItem, String> nameCol;

    @FXML
    private TableView<ScoreboardItem> scoreBoard;

    @FXML
    private TableColumn<ScoreboardItem, Integer> scoreCol;

    @FXML
    void goBackButtonClick(ActionEvent event) {
    	((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    Connection conn = null;
    PreparedStatement query = null;
    ResultSet resultSet = null;
    String sql = null;

    ObservableList<ScoreboardItem> items = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		try {
			sql = "SELECT firstname, lastname, score FROM students, scores WHERE students.id = scores.studentid AND students.classid = ? ORDER BY score DESC LIMIT 50";
			query = conn.prepareStatement(sql);
			query.setInt(1, GlobalVariables.globalVariables.getStudent().getClassId());
			resultSet = query.executeQuery();
			
			while (resultSet.next()) {
				items.add(new ScoreboardItem(resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("score")));
			}
			
			scoreBoard.setItems(items);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
