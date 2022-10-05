package application;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminPanelController implements Initializable {

	public AdminPanelController() {
		conn = ConnectToDatabase.connect();
	}
	
	void refleshScoresTable() throws SQLException {
		scoresList.clear();
		sql = "SELECT * FROM scores";
		query = conn.prepareStatement(sql);
		resultSet = query.executeQuery();
		
		while (resultSet.next()) {
			scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"),resultSet.getInt("score"), resultSet.getString("scoredate")));
		}
		
		scoresTable.setItems(scoresList);
	}
	
	void refleshStudentsTable() throws SQLException {
		studentsTable.getItems().clear();
		sql = "SELECT * FROM students";
		query = conn.prepareStatement(sql);
		resultSet = query.executeQuery();
		
		while (resultSet.next()) {
			studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"),
					resultSet.getInt("num"), resultSet.getString("isbanned")));
		}
		studentsTable.setItems(studentList);
	}
	
	void refleshTeachersTable() throws SQLException {
		teacherList.clear();
		teachersTable.getItems().clear();
		sql = "SELECT * FROM teachers";
		query = conn.prepareStatement(sql);
		resultSet = query.executeQuery();
		
		while (resultSet.next()) {
			teacherList.add(new Teacher(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getString("email")
					, resultSet.getString("pass"), resultSet.getString("admin")));
		}
		teachersTable.setItems(teacherList);
	}
	
    @FXML
    private HBox bottomHbox;

    @FXML
    private CheckBox bannedCheck;
    
    @FXML
    private AnchorPane centerBottomAnchor;

    @FXML
    private ComboBox<String> classLetterCombo;

    @FXML
    private ComboBox<Integer> classNumCombo;

    @FXML
    private Button clearScoresButton;

    @FXML
    private DatePicker datePick;

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passField;

    @FXML
    private TextField gameIdField;

    @FXML
    private Button getStudentsOfTeacherButton;

    @FXML
    private HBox hBoxMid;

    @FXML
    private CheckBox isAdminCheck;


    @FXML
    private MenuBar menuBar;

    //Student table Columns
    //----------------------------------------------------------------------------------
    
    @FXML
    private TableColumn<Student, String> sClassCol;

    @FXML
    private TableColumn<Student, String> sIdCol;

    @FXML
    private TableColumn<Student, String> sLastNameCol;

    @FXML
    private TableColumn<Student, String> sNameCol;

    @FXML
    private TableColumn<Student, String> sNumberCol;

    @FXML
    private TableColumn<Student, String> isBannedCol;

    //----------------------------------------------------------------------------------
    
    @FXML
    private Button scoreAddButton;

    //Scores table Columns
    //----------------------------------------------------------------------------------
    @FXML
    private TableColumn<Score, Integer> scoreCol;

    @FXML
    private TableColumn<Score, String> scoreDateCol;
    
    @FXML
    private TableColumn<Score, Integer> scoreIdCol;

    @FXML
    private TableColumn<Score, Integer> studentIdCol;

    @FXML
    private TableColumn<Score, String> gameNameCol;
    
    //----------------------------------------------------------------------------------
    @FXML
    private Button scoreDeleteButton;

    @FXML
    private TextField scoreIdField;

    @FXML
    private Button scoreSearchButton;

    @FXML
    private Button scoreSearchOnStudentPanel;

    @FXML
    private Button scoreUpdateButton;

    @FXML
    private TableView<Score> scoresTable;

    @FXML
    private Button stdAddButton;

    @FXML
    private Button stdDeleteButton;

    @FXML
    private TextField stdNameField;
    
    @FXML
    private TextField stdNumField;

    @FXML
    private Button stdSearchButton;

    @FXML
    private Button stdUpdateButton;

    @FXML
    private TextField stfLastNameField;


    @FXML
    private TextField studentIdField;

    @FXML
    private TableView<Student> studentsTable;
    
    //Teachers Table columns
    //----------------------------------------------------------------------------------
    
    @FXML
    private TableColumn<Teacher, Integer> tClassCol;

    @FXML
    private TableColumn<Teacher, Integer> tIdCol;

    @FXML
    private TableColumn<Teacher, String> tLastNameCol;

    @FXML
    private TableColumn<Teacher, String> tNameCol;

    @FXML
    private TableColumn<Teacher, String> temailCol;
    
    @FXML
    private TableColumn<Teacher, String> tPassCol;
    
    @FXML
    private TableColumn<Teacher, String> tIsAdminCol;

    //----------------------------------------------------------------------------------
    
    @FXML
    private Button teacherAddButton;

    @FXML
    private ComboBox<String> teacherClassLetterCom;

    @FXML
    private ComboBox<Integer> teacherClassNumCom;

    @FXML
    private AnchorPane teacherControlsAnchor;

    @FXML
    private Button teacherDeleteButton;

    @FXML
    private TextField teacherLastNameField;

    @FXML
    private TextField teacherNameField;

    @FXML
    private Button teacherSearchButton;

    @FXML
    private Button teacherUpdateButton;

    @FXML
    private TableView<Teacher> teachersTable;

    @FXML
    private HBox topHbox;

    @FXML
    private Label usernameLabel;

    @FXML
    private RadioButton radioDate;

    @FXML
    private RadioButton radioGame;

    @FXML
    private RadioButton radioPoint;

    @FXML
    private RadioButton radioStd;

    @FXML
    private RadioButton radioStdClass;

    @FXML
    private RadioButton radioStdName;

    @FXML
    private RadioButton radioStdNum;

    @FXML
    private RadioButton radioTClass;

    @FXML
    private RadioButton radioTLastName;

    @FXML
    private RadioButton radioTName;

    @FXML
    private RadioButton radioTeamil;
    
    @FXML
    private RadioButton radioStdLastName;

    @FXML
    private ToggleGroup radios;

    @FXML
    private ToggleGroup radios2;

    @FXML
    private ToggleGroup radios3;

    @FXML
    private MenuItem aboutButton;

    ObservableList<Score> scoresList = FXCollections.observableArrayList();
    ObservableList<Student> studentList = FXCollections.observableArrayList();
    ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    ObservableList<ScClass> classList = FXCollections.observableArrayList(); // id list found from num and letter
    ObservableList<ReverseScClass> reverseClassList = FXCollections.observableArrayList();	// num and letter list found from id
    
    Connection conn = null;
    PreparedStatement query = null;
    ResultSet resultSet = null;
    String sql = null;
    
    Score selectedScore;
    Student selectedStudent;
    Teacher selectedTeacher;
    
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

    @FXML
    void clearScoresButtonClick(ActionEvent event) {
    	try {
			sql = "DELETE FROM scores";
			query = conn.prepareStatement(sql);
			query.executeUpdate();
			refleshScoresTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void datePickClick(ActionEvent event) {

    }

    @FXML
    void getStudentsOfTeacherButtonClick(ActionEvent event) {

		try {
			refleshStudentsTable();
			sql = "SELECT * FROM students WHERE classid = ? ";
			query = conn.prepareStatement(sql);
			classList.clear();
			
			String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, teacherClassNumCom.getSelectionModel().getSelectedItem());
			classSearchQuery.setString(2, teacherClassLetterCom.getSelectionModel().getSelectedItem());
			ResultSet classResults = classSearchQuery.executeQuery();
			classList.clear();
			
			while (classResults.next()) {
				classList.add(new ScClass(classResults.getInt("id")));
			}
			
			query.setInt(1, classList.get(0).getId());
			resultSet = query.executeQuery();
			
			studentList.clear();
			while (resultSet.next()) {
				studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
			}
			studentsTable.setItems(studentList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @FXML
    void isAdminCheckClick(ActionEvent event) {

    }


    
    @FXML
    void onScoresTabeClicked(MouseEvent event) {
    	if (event.getClickCount() > 0) {
            onScoresTableEdit();
        }
    }
    
    @FXML
    void onStudentsTableClicked(MouseEvent event) {
    	if (event.getClickCount() > 0) {
            try {
            	reverseClassList.clear();
            	classList.clear();
				onStudentsTableEdit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    @FXML
    void onTeachersTabeClicked(MouseEvent event) {
    	if (event.getClickCount() > 0) {
            try {
            	reverseClassList.clear();
            	classList.clear();
				onTeachersTableEdit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    void onScoresTableEdit() {
    	if (scoresTable.getSelectionModel().getSelectedItem() != null){
    		selectedScore = scoresTable.getSelectionModel().getSelectedItem();
    		scoreIdField.setText(String.valueOf(selectedScore.getStudentId()));
    		studentIdField.setText(String.valueOf(selectedScore.getGameId()));
    		gameIdField.setText(String.valueOf(selectedScore.getScore()));
    		datePick.setValue(LocalDate.parse(selectedScore.getDate()));
    	}
    }
    @FXML
    void onStudentsTableEdit() throws SQLException {
    	if (studentsTable.getSelectionModel().getSelectedItem() != null){
    		selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
    		stdNameField.setText(String.valueOf(selectedStudent.getFirstName()));
    		stfLastNameField.setText(String.valueOf(selectedStudent.getLastName()));
    		
			String classSearchSql = "SELECT num, letter FROM classes WHERE id = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, selectedStudent.getClassId());
			ResultSet classResults = classSearchQuery.executeQuery();
			
			while (classResults.next()) {
				reverseClassList.add(new ReverseScClass(classResults.getInt("num"), classResults.getString("letter")));
			}
    		classNumCombo.setValue(reverseClassList.get(0).getNum());
    		classLetterCombo.setValue(reverseClassList.get(0).getLetter());
    		stdNumField.setText(String.valueOf(selectedStudent.getNumber()));
    		//bannedCheck.setSelected(true);
    		if (selectedStudent.getIsBanned().charAt(0) == 'N') {
				bannedCheck.setSelected(false);
			}
    		else if (selectedStudent.getIsBanned().charAt(0) == 'Y'){
    			bannedCheck.setSelected(true);
			}
    	}
    }
    
    void onTeachersTableEdit() throws SQLException {
    	if (teachersTable.getSelectionModel().getSelectedItem() != null){
    		selectedTeacher = teachersTable.getSelectionModel().getSelectedItem();
    		teacherNameField.setText(selectedTeacher.getFirstName());
    		teacherLastNameField.setText(selectedTeacher.getLastName());
    		emailField.setText(selectedTeacher.getEmail());
    		
			if (selectedTeacher.getClassId() != 0) {
				String classSearchSql = "SELECT num, letter FROM classes WHERE id = ?";
				PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
				classSearchQuery.setInt(1, selectedTeacher.getClassId());
				ResultSet classResults = classSearchQuery.executeQuery();
				
				while (classResults.next()) {
					reverseClassList.add(new ReverseScClass(classResults.getInt("num"), classResults.getString("letter")));
				}
	    		teacherClassNumCom.setValue(reverseClassList.get(0).getNum());
	    		teacherClassLetterCom.setValue(reverseClassList.get(0).getLetter());
			}
			else {
				teacherClassNumCom.setValue(null);
	    		teacherClassLetterCom.setValue(null);
			}
    		
    		passField.setText(selectedTeacher.getPassword());
    		
    		if(selectedTeacher.getIsAdmin().charAt(0) == 'N') {
    			isAdminCheck.setSelected(false);
    		}
    		else {
				isAdminCheck.setSelected(true);
			}
    
    	}
    }
    
    @FXML
    void scoreAddButtonClick(ActionEvent event) {
    	
		try {
			sql = "INSERT INTO scores (studentid, gameid, score, scoredate) VALUES (?, ?, ?, ?)";
			query = conn.prepareStatement(sql);
			query.setString(1, scoreIdField.getText());
			query.setString(2, studentIdField.getText());
			query.setString(3, gameIdField.getText());
			query.setString(4, datePick.getValue().toString());
			query.executeUpdate();
			refleshScoresTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void scoreDeleteButtonClick(ActionEvent event) {
    	try {
			sql = "DELETE FROM scores WHERE studentid = ? and gameid = ? and score = ? and scoredate = ?";
			query = conn.prepareStatement(sql);
			query.setInt(1, selectedScore.getStudentId());
			query.setInt(2, selectedScore.getGameId());
			query.setInt(3, selectedScore.getScore());
			query.setString(4, selectedScore.getDate());
			query.executeUpdate();
			refleshScoresTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    @FXML
    void scoreSearchButtonClick(ActionEvent event) {
		if (radioStd.isSelected()) {
			try {
				sql = "SELECT * FROM scores WHERE studentid = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, scoreIdField.getText());
				resultSet = query.executeQuery();
				scoresList.clear();
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			scoresTable.setItems(scoresList);
		}
		else if (radioPoint.isSelected()) {
			try {
				sql = "SELECT * FROM scores WHERE score = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, gameIdField.getText());
				resultSet = query.executeQuery();
				scoresList.clear();
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			scoresTable.setItems(scoresList);
		}
		else if (radioGame.isSelected()) {
			try {
				sql = "SELECT * FROM scores WHERE gameid = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, studentIdField.getText());
				resultSet = query.executeQuery();
				scoresList.clear();
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			scoresTable.setItems(scoresList);
		}
		else if (radioDate.isSelected()) {
			try {
				sql = "SELECT * FROM scores WHERE scoredate = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, String.valueOf(datePick.getValue()));
				resultSet = query.executeQuery();
				scoresList.clear();
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			scoresTable.setItems(scoresList);
		}
		else {
			System.out.println("Radio seçimi yapılmadı");
		}
    }
    @FXML
    void scoreSearchOnStudentPanelClick(ActionEvent event) {
    	try {
			sql = "SELECT * FROM students WHERE id = ? ";
			query = conn.prepareStatement(sql);
			query.setInt(1, selectedScore.getStudentId());
			resultSet = query.executeQuery();
			studentList.clear();
			
			while (resultSet.next()) {
				studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	studentsTable.setItems(studentList);
    }

    @FXML
    void scoreUpdateButtonClick(ActionEvent event) {
    	try {
			sql = "UPDATE scores SET studentid = ?, gameid = ?, score = ?, scoredate = ? WHERE id = ?";
			
			query = conn.prepareStatement(sql);
			query.setString(1, scoreIdField.getText());
			query.setString(2, studentIdField.getText());
			query.setString(3, gameIdField.getText());
			query.setString(4, datePick.getValue().toString());
			query.setString(5, String.valueOf(selectedScore.getId()));
			query.executeUpdate();
			refleshScoresTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void stdAddButtonClick(ActionEvent event) {
		try {
			sql = "INSERT INTO students (firstname, lastname, classid, num, isbanned) VALUES (?, ?, ?, ?, ?)";
			
			String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, classNumCombo.getSelectionModel().getSelectedItem());
			classSearchQuery.setString(2, classLetterCombo.getSelectionModel().getSelectedItem());
			ResultSet classResults = classSearchQuery.executeQuery();
			
			while (classResults.next()) {
				classList.add(new ScClass(classResults.getInt("id")));
			}
			
			query = conn.prepareStatement(sql);
			query.setString(1, stdNameField.getText());
			query.setString(2, stfLastNameField.getText());
			query.setInt(3, classList.get(0).getId());
			query.setString(4, stdNumField.getText());
			
			if (bannedCheck.isSelected()) {
				query.setString(5, "Y");
			}
			else {
				query.setString(5, "N");
			}
			
			query.executeUpdate();
			refleshStudentsTable();
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		
    }


    @FXML
    void stdDeleteButtonClick(ActionEvent event) {
    	try {
			sql = "DELETE FROM students WHERE firstname=? and lastname=? and classid=? and num=? and isbanned=?";
			query = conn.prepareStatement(sql);
			query.setString(1, stdNameField.getText());
			query.setString(2, stfLastNameField.getText());
			
			String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, classNumCombo.getSelectionModel().getSelectedItem());
			classSearchQuery.setString(2, classLetterCombo.getSelectionModel().getSelectedItem());
			ResultSet classResults = classSearchQuery.executeQuery();
			classList.clear();
			
			while (classResults.next()) {
				classList.add(new ScClass(classResults.getInt("id")));
			}
			
			query.setInt(3, classList.get(0).getId());
			query.setInt(4, Integer.valueOf(stdNumField.getText()));
			
			if (bannedCheck.isSelected()) {
				query.setString(5, "Y");
			}
			else {
				query.setString(5, "N");
			}
			
			query.executeUpdate();
			refleshStudentsTable();
		} catch (SQLException e) {

			e.printStackTrace();
		}
    }

    @FXML
    void stdSearchButtonClick(ActionEvent event) {
    	if (radioStdName.isSelected()) {
    		try {
				sql = "SELECT * FROM students WHERE firstname LIKE ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + stdNameField.getText() + "%");
				resultSet = query.executeQuery();
				studentList.clear();
				
				while (resultSet.next()) {
					studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			studentsTable.setItems(studentList);
		}
    	else if (radioStdClass.isSelected()) {
    		try {
				sql = "SELECT * FROM students WHERE classid = ? ";
				query = conn.prepareStatement(sql);
				
				String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
				PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
				classSearchQuery.setInt(1, classNumCombo.getSelectionModel().getSelectedItem());
				classSearchQuery.setString(2, classLetterCombo.getSelectionModel().getSelectedItem());
				ResultSet classResults = classSearchQuery.executeQuery();
				classList.clear();
				
				while (classResults.next()) {
					classList.add(new ScClass(classResults.getInt("id")));
				}
				query.setInt(1, classList.get(0).getId());
				
				studentsTable.getItems().clear();
				resultSet = query.executeQuery();
				
				while (resultSet.next()) {
					studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else if (radioStdLastName.isSelected()) {
    		try {
				sql = "SELECT * FROM students WHERE lastname LIKE ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + stfLastNameField.getText() + "%");
				resultSet = query.executeQuery();
				studentList.clear();
				
				while (resultSet.next()) {
					studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else if (radioStdNum.isSelected()) {
    		try {
				sql = "SELECT * FROM students WHERE num LIKE ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + stdNumField.getText() + "%");
				resultSet = query.executeQuery();
				studentList.clear();
				
				while (resultSet.next()) {
					studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else {
			System.out.println("Kriter belirlenmedi");
		}
    }

    @FXML
    void stdUpdateButtonClick(ActionEvent event) {
    	try {
			sql = "UPDATE students SET firstname = ?, lastname = ?, classid = ?, num = ?, isbanned = ? WHERE id = ?";
			
			query = conn.prepareStatement(sql);
			query.setString(1, stdNameField.getText());
			query.setString(2, stfLastNameField.getText());
			
			String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, classNumCombo.getSelectionModel().getSelectedItem());
			classSearchQuery.setString(2, classLetterCombo.getSelectionModel().getSelectedItem());
			ResultSet classResults = classSearchQuery.executeQuery();
			classList.clear();
			
			while (classResults.next()) {
				classList.add(new ScClass(classResults.getInt("id")));
			}
			
			query.setInt(3, classList.get(0).getId());
			query.setString(4, stdNumField.getText());
			
			if (bannedCheck.isSelected()) {
				query.setString(5, "Y");
			}
			else {
				query.setString(5, "N");
			}
			
			query.setInt(6, selectedStudent.getId());
			
			query.executeUpdate();
			refleshStudentsTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void teacherAddButtonClick(ActionEvent event) {
		try {
			sql = "INSERT INTO teachers (firstname, lastname, email, classid, pass, admin) VALUES (?, ?, ?, ?, ?, ?)";
			query = conn.prepareStatement(sql);
			query.setString(1, teacherNameField.getText());
			query.setString(2, teacherLastNameField.getText());
			query.setString(3, emailField.getText());
			
			String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, (int) teacherClassNumCom.getSelectionModel().getSelectedItem());
			classSearchQuery.setString(2, (String) teacherClassLetterCom.getSelectionModel().getSelectedItem());
			ResultSet classResults = classSearchQuery.executeQuery();
			classList.clear();
			
			while (classResults.next()) {
				classList.add(new ScClass(classResults.getInt("id")));
			}
			
			query.setInt(4, classList.get(0).getId());
			query.setString(5, getSHA256Pass(passField.getText()));
			
			if(isAdminCheck.isSelected()) {
				query.setString(6, "Y");
			}
			else {
				query.setString(6, "N");
			}
			
			query.executeUpdate();
			refleshTeachersTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void teacherClassLetterComClick(ActionEvent event) {

    }

    @FXML
    void teacherClassNumComClick(ActionEvent event) {

    }

    @FXML
    void teacherDeleteButtonClick(ActionEvent event) {
    	try {
			sql = "DELETE FROM teachers WHERE firstname=? and lastname=? and email=? and classid=? and pass=? and admin=?";
			query = conn.prepareStatement(sql);
			query.setString(1, teacherNameField.getText());
			query.setString(2, teacherLastNameField.getText());
			query.setString(3, emailField.getText());
			
			String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, teacherClassNumCom.getSelectionModel().getSelectedItem());
			classSearchQuery.setString(2, teacherClassLetterCom.getSelectionModel().getSelectedItem());
			ResultSet classResults = classSearchQuery.executeQuery();
			classList.clear();
			
			while (classResults.next()) {
				classList.add(new ScClass(classResults.getInt("id")));
			}
			
			query.setInt(4, classList.get(0).getId());
			query.setString(5, passField.getText());
			
			if (isAdminCheck.isSelected()) {
				query.setString(6, "Y");
			}
			else {
				query.setString(6, "N");
			}
			
			query.executeUpdate();
			refleshTeachersTable();
		} catch (SQLException e) {

			e.printStackTrace();
		}
    }

    @FXML
    void teacherSearchButtonClick(ActionEvent event) {
    	if (radioTName.isSelected()) {
    		try {
				sql = "SELECT * FROM teachers WHERE firstname LIKE ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + teacherNameField.getText() + "%");
				resultSet = query.executeQuery();
				teacherList.clear();
				
				while (resultSet.next()) {
					teacherList.add(new Teacher(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
							resultSet.getInt("classid"), resultSet.getString("email"), resultSet.getString("pass"), resultSet.getString("admin")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			teachersTable.setItems(teacherList);
		}
    	else if (radioTLastName.isSelected()) {
    		try {
				sql = "SELECT * FROM teachers WHERE lastname LIKE ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + teacherLastNameField.getText() + "%");
				resultSet = query.executeQuery();
				teacherList.clear();
				
				while (resultSet.next()) {
					teacherList.add(new Teacher(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
							resultSet.getInt("classid"), resultSet.getString("email"), resultSet.getString("pass"), resultSet.getString("admin")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			teachersTable.setItems(teacherList);
		}
    	else if (radioTeamil.isSelected()) {
    		try {
				sql = "SELECT * FROM teachers WHERE email LIKE ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + emailField.getText() + "%");
				resultSet = query.executeQuery();
				teacherList.clear();
				
				while (resultSet.next()) {
					teacherList.add(new Teacher(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
							resultSet.getInt("classid"), resultSet.getString("email"), resultSet.getString("pass"), resultSet.getString("admin")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			teachersTable.setItems(teacherList);
		}
    	else if (radioTClass.isSelected()) {
    		try {
				sql = "SELECT * FROM teachers WHERE classid = ? ";
				query = conn.prepareStatement(sql);
				
				String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
				PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
				classSearchQuery.setInt(1, teacherClassNumCom.getSelectionModel().getSelectedItem());
				classSearchQuery.setString(2, teacherClassLetterCom.getSelectionModel().getSelectedItem());
				ResultSet classResults = classSearchQuery.executeQuery();
				classList.clear();
				
				while (classResults.next()) {
					classList.add(new ScClass(classResults.getInt("id")));
				}
				query.setInt(1, classList.get(0).getId());
				
				teacherList.clear();
				resultSet = query.executeQuery();
				
				while (resultSet.next()) {
					teacherList.add(new Teacher(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
							resultSet.getInt("classid"), resultSet.getString("email"), resultSet.getString("pass"), resultSet.getString("admin")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else {
			System.out.println("Kriter belirlenmedi");
		}
    }

    @FXML
    void teacherUpdateButtonClick(ActionEvent event) {
    	try {
			sql = "UPDATE teachers SET firstname = ?, lastname = ?, email = ?, classid = ?, pass = ?, admin = ? WHERE id = ?";
			
			query = conn.prepareStatement(sql);
			query.setString(1, teacherNameField.getText());
			query.setString(2, teacherLastNameField.getText());
			query.setString(3, emailField.getText());
			
			String classSearchSql = "SELECT id FROM classes WHERE num = ? and letter = ?";
			PreparedStatement classSearchQuery = conn.prepareStatement(classSearchSql);
			classSearchQuery.setInt(1, teacherClassNumCom.getSelectionModel().getSelectedItem());
			classSearchQuery.setString(2, teacherClassLetterCom.getSelectionModel().getSelectedItem());
			ResultSet classResults = classSearchQuery.executeQuery();
			classList.clear();
			
			while (classResults.next()) {
				classList.add(new ScClass(classResults.getInt("id")));
			}
			
			query.setInt(4, classList.get(0).getId());
			query.setString(5, getSHA256Pass(passField.getText()));
			
			if (isAdminCheck.isSelected()) {
				query.setString(6, "Y");
			}
			else {
				query.setString(6, "N");
			}
			
			query.setInt(7, selectedTeacher.getId());
			
			query.executeUpdate();
			refleshTeachersTable();;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void aboutButtonClick(ActionEvent event) throws IOException {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutPanel.fxml"));
	    Parent root1 = (Parent) fxmlLoader.load();
	    root1.getStylesheets().add("application.css");
	    Stage stage = new Stage();
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.initStyle(StageStyle.DECORATED);
	    stage.setTitle("Hakkında...");
	    stage.setScene(new Scene(root1));
	    stage.show();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bottomHbox.setHgrow(centerBottomAnchor, Priority.ALWAYS);
		hBoxMid.setHgrow(studentsTable, Priority.ALWAYS);
		topHbox.setHgrow(teacherControlsAnchor, Priority.ALWAYS);

		usernameLabel.setText("Yetkili kullanıcı: " + GlobalVariables.globalVariables.getTeacherName());
		
		try {
			sql = "SELECT * FROM students";
			resultSet = conn.createStatement().executeQuery(sql);
			
			while (resultSet.next()) {
				studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
						resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
				
				System.out.println("Sınıf: " + resultSet.getInt("classid"));
				System.out.println("İsim: " + resultSet.getString("firstname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		sNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		sLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		sClassCol.setCellValueFactory(new PropertyValueFactory<>("classId"));
		sNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
		isBannedCol.setCellValueFactory(new PropertyValueFactory<>("isBanned"));
		
		studentsTable.setItems(studentList);
		
		try {
			sql = "SELECT * FROM teachers";
			resultSet = conn.createStatement().executeQuery(sql);
			
			while (resultSet.next()) {
				teacherList.add(new Teacher(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
						resultSet.getInt("classid"), resultSet.getString("email"), resultSet.getString("pass"), resultSet.getString("admin")));
				
				System.out.println("Sınıf: " + resultSet.getInt("classid"));
				System.out.println("İsim: " + resultSet.getString("firstname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		tIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		tNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		tLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		tClassCol.setCellValueFactory(new PropertyValueFactory<>("classId"));
		temailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		tPassCol.setCellValueFactory(new PropertyValueFactory<>("password"));
		tIsAdminCol.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));
		
		teachersTable.setItems(teacherList);
		
		try {
			sql = "SELECT * FROM scores";
			resultSet = conn.createStatement().executeQuery(sql);
			
			while (resultSet.next()) {
				scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentId"), resultSet.getInt("gameid"),resultSet.getInt("score"), resultSet.getString("scoredate")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		scoreIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
		gameNameCol.setCellValueFactory(new PropertyValueFactory<>("gameId"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
		scoreDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		scoresTable.setItems(scoresList);
		
		try {
			String sql1 = "SELECT DISTINCT num FROM classes";
			String sql2 = "SELECT DISTINCT letter FROM classes";
			ResultSet resultSet1 = conn.createStatement().executeQuery(sql1);
			ResultSet resultSet2 = conn.createStatement().executeQuery(sql2);
			while (resultSet1.next()) {
				classNumCombo.getItems().add(resultSet1.getInt("num"));
				teacherClassNumCom.getItems().add(resultSet1.getInt("num"));
			}
			while (resultSet2.next()) {
				classLetterCombo.getItems().add(resultSet2.getString("letter"));
				teacherClassLetterCom.getItems().add(resultSet2.getString("letter"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}