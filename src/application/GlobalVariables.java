package application;

public class GlobalVariables {
	public static GlobalVariables globalVariables = new GlobalVariables();
	
	private String teacherName;
	private int teacherId;
	private String studentName;
	private int studentId;

	private int teacherClassId;
	private Student student;
	
	public GlobalVariables() {

	}
	public GlobalVariables(String teacherName, int teacherId, String studentName, int studentId, int teacherClassId) {
		this.teacherName = teacherName;
		this.teacherId = teacherId;
		this.studentName = studentName;
		this.studentId = studentId;
		this.teacherClassId = teacherClassId;
	}
	
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public int getTeacherClassId() {
		return teacherClassId;
	}
	public void setTeacherClassId(int teacherClassId) {
		this.teacherClassId = teacherClassId;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}


}
