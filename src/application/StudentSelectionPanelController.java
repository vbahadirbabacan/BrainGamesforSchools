package application;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StudentSelectionPanelController implements Initializable{
	
	public StudentSelectionPanelController() {
		conn = ConnectToDatabase.connect();
	}

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField numberField;
    
    @FXML
    private Label teacherNameLabel;
    
    @FXML
    private Label errorLabel;
    
    Connection conn = null;
    PreparedStatement query = null;
    ResultSet resultSet = null;
    Student selectedStudent;
    String sql = null;
    String enteredNumber;
    Boolean isStudentFound = false;
    
    ObservableList<Student> stds = FXCollections.observableArrayList();

    @FXML
    void loginButtonClick(ActionEvent event) {
    	stds.clear();
    	sql = "SELECT * FROM students WHERE num = ?";
    	enteredNumber = numberField.getText();
    	try {
			query = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE);
			if (enteredNumber.matches("\\b\\d+\\b")) {
    			query.setString(1, enteredNumber);
		    	resultSet = query.executeQuery();
		    	stds.clear();
		    	while (resultSet.next()) {
		    		stds.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
		    				resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned"))); 
		    		isStudentFound = true;
				}
		    	
    			if (isStudentFound) {
    				if (stds.get(0).getIsBanned().charAt(0) == 'N') {
    					try {
        					System.out.println("Giriş öğrenci: " + stds.get(0).getFirstName());
        					
        	        		GlobalVariables.globalVariables.setStudent(stds.get(0));
        	        	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMenuScene.fxml"));
        	        	    Parent root1 = (Parent) fxmlLoader.load();
        	        	    root1.getStylesheets().add("application.css");
        	        	    Stage stage = new Stage();
        	        	    stage.initStyle(StageStyle.DECORATED);
        	        	    stage.setTitle("Ana menü");
        	        	    stage.setScene(new Scene(root1));
        	        	    stage.show();
        	        	    System.out.println("Id of student just logged in: " + stds.get(0).getId());
        	        	    stds.clear();
        	        	}
        	        	catch (IOException e) {
        	    			System.out.println(e);
        	    		}
    				}
    				else if (stds.get(0).getIsBanned().charAt(0) == 'Y'){
						System.out.println("Engellendiniz.");
						errorLabel.setText("Girişiniz engellendi.\nÖğretmeninize danışınız.");
					}
    				else {
						System.out.println(stds.get(0).getIsBanned());
					}
    				
				}
    			else {
					System.out.println("Öğrenci bulunamadı...");
					errorLabel.setText("Numara bulunamadı.      ");
				}

		    	
       	 	}
    		else {
				System.out.println("Numara dışında karakter hatası");
				errorLabel.setText("Sayı dışı karakter hatası.");
			}
        	
		} catch (SQLException e) {
			e.printStackTrace();
		}		
    }

    @FXML
    void logoutButtonCLick(ActionEvent event) {
    	((Node)(event.getSource())).getScene().getWindow().hide();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		teacherNameLabel.setText("Öğretmeniniz: " + GlobalVariables.globalVariables.getTeacherName());
		
	}

}
