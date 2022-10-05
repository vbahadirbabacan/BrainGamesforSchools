package application;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.databaseconnection.util.ConnectToDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginMenuController {
	
	public LoginMenuController() {
		conn = ConnectToDatabase.connect();
	}

    @FXML
    private RadioButton adminPanelRadio;

    @FXML
    private RadioButton controlPanelRadio;

    @FXML
    private TextField emailField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passField;

    @FXML
    private ToggleGroup radios;

    @FXML
    private RadioButton studentRadio;
    
    Connection conn = null;
    PreparedStatement query = null;
    ResultSet resultSet = null;
    String sql = null;
    
    private static String getSHA256Pass(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    void loginSucces(String fxml, String title) throws SQLException, IOException {
		System.out.println("ID: " + resultSet.getString("id"));
		System.out.println("Kullanııcı adı: " + resultSet.getString("firstname"));
		System.out.println("Kullanııcı soyadı: " + resultSet.getString("lastname"));
		System.out.println("Sınıf: " + resultSet.getString("classid"));
		System.out.println("Kullanıcı email: " + resultSet.getString("email"));
		System.out.println("Kullanıcı pass sha: " + getSHA256Pass(resultSet.getString("pass")));
		System.out.println("Şifre sha: " +  (resultSet.getString("pass")));
		System.out.println("Yetki: " + resultSet.getString("admin"));
		GlobalVariables.globalVariables.setTeacherName(resultSet.getString("firstname") + " " + resultSet.getString("lastname"));
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
	    Parent root1 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.initStyle(StageStyle.DECORATED);
	    stage.setTitle(title);
	    stage.setScene(new Scene(root1));
	    stage.show();
	    
	    
    }

    @FXML
    void loginButtonClick(ActionEvent event) {
    	
		sql = "SELECT * FROM teachers WHERE email = ? and pass = ?";
		System.out.println(passField.getText());
		System.out.println(getSHA256Pass(passField.getText()));
		
    	if (studentRadio.isSelected()) {
    		
    		try {
				query = conn.prepareStatement(sql);
				query.setString(1, emailField.getText());
				query.setString(2, getSHA256Pass(passField.getText()));
				
				resultSet = query.executeQuery();
				
				if (!resultSet.next()) {
					System.out.println("Kullanıcı adı veya şifre hatalı.");
				}
				
				else {
					GlobalVariables.globalVariables.setTeacherName(resultSet.getString("firstname") + " " + resultSet.getString("lastname"));
					GlobalVariables.globalVariables.setTeacherId(resultSet.getInt("id"));
					GlobalVariables.globalVariables.setTeacherClassId(resultSet.getInt("classid"));
					loginSucces("StudentSelectionPanel.fxml", "Numaranızı giriniz...");
					((Node)(event.getSource())).getScene().getWindow().hide();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	else if (adminPanelRadio.isSelected()) {
			try {
				query = conn.prepareStatement(sql);
				query.setString(1, emailField.getText().trim());
				query.setString(2, getSHA256Pass(passField.getText().trim()));
				
				resultSet = query.executeQuery();
				
				if (!resultSet.next()) {
					System.out.println("Kullanıcı adı veya şifre hatalı.");
				}
				else {
					if (resultSet.getString("admin").charAt(0) == 'N') {
						System.out.println("Not in admins list.");
					}
					else{
						GlobalVariables.globalVariables.setTeacherName(resultSet.getString("firstname") + " " + resultSet.getString("lastname"));
						loginSucces("AdminPanel.fxml", "Kontrol Paneli");
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
    		
    	}
    	else if (controlPanelRadio.isSelected()) {
    		try {
				query = conn.prepareStatement(sql);
				query.setString(1, emailField.getText());
				query.setString(2, getSHA256Pass(passField.getText()));
				
				resultSet = query.executeQuery();
				
				
				if (!resultSet.next()) {
					System.out.println("Kullanıcı adı veya şifre hatalı.");
				}
				else {
					GlobalVariables.globalVariables.setTeacherName(resultSet.getString("firstname") + " " + resultSet.getString("lastname"));
					GlobalVariables.globalVariables.setTeacherId(resultSet.getInt("id"));
					GlobalVariables.globalVariables.setTeacherClassId(resultSet.getInt("classid"));
					loginSucces("TeacherPanel.fxml", "Sınıf Düzeni Paneli");
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
    }
}
