package com.databaseconnection.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectToDatabase {
	static Connection connection = null;
	
	String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static Connection connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/schoolgames", "root", "pass");
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
