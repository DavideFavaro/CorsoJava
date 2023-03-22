package it.betacom.ProgettoBiblioteca.service;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;



public class DBHandler {

	private static Connection conn;
	private static Properties properties;
	private static DBHandler instance = null;



	private DBHandler (String url, String db, String user, String password ) {
		try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			System.out.println("Classe non trovata");
			return;
		}
		
		properties = new Properties();
		properties.setProperty("url", url);
		properties.setProperty("db", db);
		properties.setProperty("user", user);
		properties.setProperty("password", password);
		
		try {
			conn = DriverManager.getConnection(
				String.format("%s/%s", url, db),
				user, password
			);
		} catch( SQLException e ) {
			System.out.println("Connessione fallita\n" + e.getMessage());
		}
	}



	public static Connection getConnection(String url, String db, String user, String password) {
		if( instance == null )
			instance = new DBHandler(url, db, user, password);
		else {
			if( url != properties.getProperty("url") ||
				db != properties.getProperty("db") ||
				user != properties.getProperty("user") ||
				password != properties.getProperty("password") )
				return null;
		}
		return conn;
	}
	
	public static Connection getConnection() {
		if(instance != null)
			return getConnection(
					properties.getProperty("url"),
					properties.getProperty("db"),
					properties.getProperty("username"),
					properties.getProperty("password")
			);
		return null;
	}



	public static boolean closeConnection() {
		if(instance != null) {
			try {
				conn.close();
				conn = null;
				properties.clear();
				properties = null;
				instance = null;
				return true;
			} catch( SQLException e ) {
				System.out.println("Chiusura connessione fallita\n" + e.getMessage());
			}
		}
		return false;
	}
	
}
