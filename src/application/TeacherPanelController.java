package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import com.databaseconnection.util.ConnectToDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Track;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TeacherPanelController implements Initializable {

	public TeacherPanelController() {
		conn = ConnectToDatabase.connect();
	}
	
    @FXML
    private Button findOnStdButton;
    
    @FXML
    private MenuItem aboutButton;

    @FXML
    private Button getScoresButton;

    @FXML
    private TableColumn<Score, Integer> scoreCol;

    @FXML
    private TextField scoreField;

    @FXML
    private Button scoresAddButton;

    @FXML
    private TableColumn<Score, String> scoresDateCol;

    @FXML
    private DatePicker scoresDateFİeld;

    @FXML
    private Button scoresDeleteButton;

    @FXML
    private TableColumn<Score, Integer> scoresGameCol;

    @FXML
    private TextField scoresGameFİeld;

    @FXML
    private TableColumn<Score, Integer> scoresStudentIdCol;
    
    @FXML
    private TextField scoresStdIdField;

    @FXML
    private Button scoresSearchButton;

    @FXML
    private TableView<Score> scoresTable;

    @FXML
    private Button scoresUpdateButton;

    @FXML
    private Button stdAddButton;
    
    @FXML
    private Button resetScoresSearch;

    @FXML
    private Button resetStdSearch;

    @FXML
    private TableColumn<Student, String> stdBanStatus;

    @FXML
    private CheckBox stdBanStatusCheck;

    @FXML
    private TableColumn<Student, Integer> stdClassCol;

    @FXML
    private Button stdDeleteButton;

    @FXML
    private TableColumn<Student, String> stdLastNameCol;

    @FXML
    private TextField stdLastNameField;

    @FXML
    private TableColumn<Student, String> stdNameCol;

    @FXML
    private TextField stdNameField;

    @FXML
    private TextField stdNumField;

    @FXML
    private TableColumn<Student, Integer> stdNumberCol;

    @FXML
    private Button stdSearchButton;

    @FXML
    private Button stdUpdateButton;

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label stdIdLabel;
    
    @FXML
    private VBox topVbox;
    
    @FXML
    private ToggleGroup toggle1;

    @FXML
    private RadioButton scoreGameIdRadio;

    @FXML
    private RadioButton scoreRadio;

    @FXML
    private RadioButton scoreStdRadio;
    
    @FXML
    private RadioButton scoreDateRadio;
    
    @FXML
    private ToggleGroup toggle2;

    @FXML
    private RadioButton stdNameRadio;
    
    @FXML
    private RadioButton stdLastNameRadio;
    
    @FXML
    private RadioButton stdNumRadio;
    
    @FXML
    private RadioButton stdClassRadio;
    
    Connection conn = null;
    PreparedStatement query = null;
    ResultSet resultSet = null;
    String sql = null;
    
    ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    ObservableList<Student> studentList = FXCollections.observableArrayList();
    ObservableList<Score> scoresList = FXCollections.observableArrayList();
    
    Score selectedScore;
    Student selectedStudent;
    
	void refleshScoresTable() throws SQLException {
		scoresList.clear();
		scoresTable.getItems().clear();
		sql = "SELECT * FROM scores WHERE studentid IN(SELECT studentid FROM students WHERE classid IN (SELECT classid FROM teachers WHERE id = ?))";
		query = conn.prepareStatement(sql);
		query.setInt(1, GlobalVariables.globalVariables.getTeacherId());
		resultSet = query.executeQuery();
		
		while (resultSet.next()) {
			scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"),
					resultSet.getString("scoredate")));
		}
		
		scoresTable.setItems(scoresList);
	}
	
	void refleshStudentsTable() throws SQLException {
		studentList.clear();
		studentsTable.getItems().clear();
		sql = "SELECT * FROM students WHERE classid = ?";
		query = conn.prepareStatement(sql);
		query.setInt(1, GlobalVariables.globalVariables.getTeacherClassId());
		resultSet = query.executeQuery();
		
		while (resultSet.next()) {
			studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
		}
		
		studentsTable.setItems(studentList);
	}

    @FXML
    void findOnStdButton(ActionEvent event) {
    	try {
			sql = "SELECT * FROM students WHERE id=?";
			query = conn.prepareStatement(sql);
			query.setInt(1, selectedScore.getStudentId());
			resultSet = query.executeQuery();
			studentsTable.getItems().clear();
			studentList.clear();
			
			if (resultSet.next()) {
				studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
						resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
				
				studentsTable.setItems(studentList);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void getScoresButtonClick(ActionEvent event) {
    	try {
			sql = "SELECT * FROM scores WHERE studentid=?";
			query = conn.prepareStatement(sql);
			query.setInt(1, selectedStudent.getId());
			resultSet = query.executeQuery();
			scoresTable.getItems().clear();
			scoresList.clear();
			
			while (resultSet.next()) {
				scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
			}
			
			scoresTable.setItems(scoresList);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void scoresAddButtonClick(ActionEvent event) {
		try {
			sql = "INSERT INTO scores (studentid, gameid, score, scoredate) VALUES (?, ?, ?, ?)";
			query = conn.prepareStatement(sql);
			query.setString(1, scoresStdIdField.getText());
			query.setString(2, scoresGameFİeld.getText());
			query.setString(3, scoreField.getText());
			query.setString(4, scoresDateFİeld.getValue().toString());
			query.executeUpdate();
			refleshScoresTable();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    
    }

    @FXML
    void scoresDeleteButtonClick(ActionEvent event) {
		try {
			sql = "DELETE FROM scores WHERE studentid = ? and gameid = ? and score = ? and scoredate = ?";
			query = conn.prepareStatement(sql);
			query.setString(1, scoresStdIdField.getText());
			query.setString(2, scoresGameFİeld.getText());
			query.setString(3, scoreField.getText());
			query.setString(4, scoresDateFİeld.getValue().toString());
			query.executeUpdate();
			refleshScoresTable();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void scoresSearchButtonClick(ActionEvent event) {
    	if (scoreStdRadio.isSelected()) {
    		try {
    			scoresTable.getItems().clear();
    			scoresList.clear();
				sql = "SELECT * FROM scores WHERE studentid = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, scoresStdIdField.getText());
				resultSet = query.executeQuery();
				
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
    	else if (scoreGameIdRadio.isSelected()) {
    		try {
    			scoresTable.getItems().clear();
    			scoresList.clear();
				sql = "SELECT * FROM scores WHERE gameid = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, scoresGameFİeld.getText());
				resultSet = query.executeQuery();
				
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else if (scoreRadio.isSelected()) {
    		try {
    			scoresTable.getItems().clear();
    			scoresList.clear();
				sql = "SELECT * FROM scores WHERE score = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, scoreField.getText());
				resultSet = query.executeQuery();
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else if (scoreDateRadio.isSelected()) {
    		try {
    			scoresTable.getItems().clear();
    			scoresList.clear();
				sql = "SELECT * FROM scores WHERE scoredate = ? ";
				query = conn.prepareStatement(sql);
				query.setString(1, scoresDateFİeld.getValue().toString());
				resultSet = query.executeQuery();
				
				while (resultSet.next()) {
					scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else {
			System.out.println("Arama kriteri seçilmedi.");
		}
    }

    @FXML
    void scoresUpdateButtonClick(ActionEvent event) {
    	try {
			sql = "UPDATE scores SET studentid = ?, gameid = ?, score = ?, scoredate = ? WHERE id = ?";
			
			query = conn.prepareStatement(sql);
			query.setString(1, scoresStdIdField.getText());
			query.setString(2, scoresGameFİeld.getText());
			query.setString(3, scoreField.getText());
			query.setString(4, scoresDateFİeld.getValue().toString());
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
			
			query = conn.prepareStatement(sql);
			query.setString(1, stdNameField.getText());
			query.setString(2, stdLastNameField.getText());
			query.setInt(3, GlobalVariables.globalVariables.getTeacherClassId());
			query.setString(4, stdNumField.getText());
			
			if (stdBanStatusCheck.isSelected()) {
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
			query.setString(2, stdLastNameField.getText());
			query.setInt(3, GlobalVariables.globalVariables.getTeacherClassId());
			query.setString(4, stdNumField.getText());
			
			if (stdBanStatusCheck.isSelected()) {
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
    void stdSearchButtonClick(ActionEvent event) {
    	if (stdNameRadio.isSelected()) {
    		try {
    			studentsTable.getItems().clear();
    			studentList.clear();
				sql = "SELECT * FROM students WHERE firstname LIKE ? and classid=?";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + stdNameField.getText() + "%");
				query.setInt(2, GlobalVariables.globalVariables.getTeacherClassId());
				resultSet = query.executeQuery();
				
				while (resultSet.next()) {
					studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"),resultSet.getInt("num"), resultSet.getString("isbanned")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
    	else if (stdLastNameRadio.isSelected()) {
    		try {
    			studentsTable.getItems().clear();
    			studentList.clear();
				sql = "SELECT * FROM students WHERE lastname LIKE ? and classid=?";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + stdLastNameField.getText() + "%");
				query.setInt(2, GlobalVariables.globalVariables.getTeacherClassId());
				resultSet = query.executeQuery();
				
				while (resultSet.next()) {
					studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"),resultSet.getInt("num"), resultSet.getString("isbanned")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else if (stdNumRadio.isSelected()) {
    		try {
    			studentsTable.getItems().clear();
    			studentList.clear();
				sql = "SELECT * FROM students WHERE num LIKE ? and classid=?";
				query = conn.prepareStatement(sql);
				query.setString(1, "%" + stdNumField.getText() + "%");
				query.setInt(2, GlobalVariables.globalVariables.getTeacherClassId());
				resultSet = query.executeQuery();
				
				while (resultSet.next()) {
					studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"),resultSet.getInt("num"), resultSet.getString("isbanned")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	else {
			System.out.println("Arama kriteri seçilmedi.");
		}
    }

    @FXML
    void stdUpdateButtonClick(ActionEvent event) {
    	try {
			sql = "UPDATE students SET firstname = ?, lastname = ?, classid = ?, num = ?, isbanned = ? WHERE id = ?";
			
			query = conn.prepareStatement(sql);
			query.setString(1, stdNameField.getText());
			query.setString(2, stdLastNameField.getText());
			query.setInt(3, GlobalVariables.globalVariables.getTeacherClassId());
			query.setString(4, stdNumField.getText());
			
			if (stdBanStatusCheck.isSelected()) {
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
    void onScoresTableClicked(MouseEvent event) {
    	if (event.getClickCount() > 0) {
           onScoresEdit();
        }
    }
    
    void onScoresEdit() {
    	if (scoresTable.getSelectionModel().getSelectedItem() != null) {
    		selectedScore = scoresTable.getSelectionModel().getSelectedItem();
    		scoresStdIdField.setText(String.valueOf(selectedScore.getStudentId()));
    		scoresGameFİeld.setText(String.valueOf(selectedScore.getGameId()));
    		scoreField.setText(String.valueOf(selectedScore.getScore()));
    		scoresDateFİeld.setValue(LocalDate.parse(selectedScore.getDate()));
    	}
    }

    @FXML
    void onStudentsTableClicked(MouseEvent event) {
    	if (event.getClickCount() > 0) {
            onStudentsEdit();
         }
    }
    
    void onStudentsEdit() {
    	if (studentsTable.getSelectionModel().getSelectedItem() != null) {
    		selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
    		stdNameField.setText(String.valueOf(selectedStudent.getFirstName()));
    		stdLastNameField.setText(String.valueOf(selectedStudent.getLastName()));
    		stdNumField.setText(String.valueOf(selectedStudent.getNumber()));
    		stdIdLabel.setText("Öğrenci ID: " + selectedStudent.getId());
    		if (selectedStudent.getIsBanned().charAt(0) == 'N') {
    			stdBanStatusCheck.setSelected(false);
    		}
    		else  {
    			stdBanStatusCheck.setSelected(true);
    		}
    		
    	}
    }
    
    @FXML
    void resetScoresSearchClick(ActionEvent event) {

    	try {
    		scoresList.clear();
    		scoresTable.getItems().clear();
    		sql = "SELECT * FROM scores WHERE studentid IN(SELECT studentid FROM students WHERE classid IN (SELECT classid FROM teachers WHERE id = ?)); ";
    		query = conn.prepareStatement(sql);
    		query.setInt(1, GlobalVariables.globalVariables.getTeacherId());
    		
    		resultSet = query.executeQuery();
    			
    		while (resultSet.next()) {
    			scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

    @FXML
    void resetStdSearchClick(ActionEvent event) {
		try {
			studentList.clear();
			studentsTable.getItems().clear();
			studentsTable.setItems(studentList);
			
			sql = "SELECT * FROM students WHERE classid = ?";
			query = conn.prepareStatement(sql);
			query.setInt(1, GlobalVariables.globalVariables.getTeacherClassId());
			resultSet = query.executeQuery();
			
			while (resultSet.next()) {
				studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
			}
			
			scoresTable.setItems(scoresList);
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
		scoresStudentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
		scoresDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		scoresGameCol.setCellValueFactory(new PropertyValueFactory<>("gameId"));
		
		stdNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		stdLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		stdNumberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
		stdClassCol.setCellValueFactory(new PropertyValueFactory<>("classId"));
		stdBanStatus.setCellValueFactory(new PropertyValueFactory<>("isBanned"));
		
		sql = "SELECT * FROM teachers WHERE classid = ?";
		try {
			query = conn.prepareStatement(sql);
			query.setInt(1, GlobalVariables.globalVariables.getTeacherClassId());
			
			resultSet = query.executeQuery();
			
			while (resultSet.next()) {
				teacherList.add(new Teacher(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getInt("classid")
						 , resultSet.getString("email"), resultSet.getString("pass"), resultSet.getString("admin")));
			}
			
			usernameLabel.setText(GlobalVariables.globalVariables.getTeacherName() );
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		studentList.clear();
		try {
			sql = "SELECT * FROM students WHERE classid = ?";
			query = conn.prepareStatement(sql);
			query.setInt(1, GlobalVariables.globalVariables.getTeacherClassId());
			resultSet = query.executeQuery();
			
			while (resultSet.next()) {
				studentList.add(new Student(resultSet.getInt("id"), resultSet.getString("firstname"), resultSet.getString("lastname"),
						resultSet.getInt("classid"), resultSet.getInt("num"), resultSet.getString("isbanned")));
				
			}
			
			
			sql = "SELECT * FROM scores WHERE studentid IN(SELECT studentid FROM students WHERE classid IN (SELECT classid FROM teachers WHERE id = ?)); ";
			query = conn.prepareStatement(sql);
			query.setInt(1, GlobalVariables.globalVariables.getTeacherId());
			
			resultSet = query.executeQuery();
				
			while (resultSet.next()) {
				scoresList.add(new Score(resultSet.getInt("id"), resultSet.getInt("studentid"), resultSet.getInt("gameid"), resultSet.getInt("score"), resultSet.getString("scoredate")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		scoresTable.setItems(scoresList);			
		studentsTable.setItems(studentList);
	
		topVbox.setVgrow(scoresTable, Priority.ALWAYS);
		
	}

}
