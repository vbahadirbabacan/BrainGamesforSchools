package application;

public class Student {
	private String firstName, lastName, isBanned;
	 private int id, classId, number;
	
	public Student(int id, String firstName, String lastName, int classId, int number, String isBanned) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.classId = classId;
		this.id = id;
		this.number = number;
		this.isBanned = isBanned;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIsBanned() {
		return isBanned;
	}

	public void setIsBanned(String isBanned) {
		this.isBanned = isBanned;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	

}
