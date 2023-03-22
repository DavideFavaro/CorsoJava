package it.betacom.ProgettoBiblioteca.dao.impl;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import it.betacom.ProgettoBiblioteca.dao.DAO;
import it.betacom.ProgettoBiblioteca.model.Libri;
import it.betacom.ProgettoBiblioteca.service.DBHandler;



public class LibriDAO extends DAO<Libri> {

	public LibriDAO () {
		this.records = new HashMap<Integer, Libri>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}

	public LibriDAO ( String properties_path ) {
		this.records = new HashMap<Integer, Libri>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(properties_path); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}



	@Override
	protected Libri resultSetToRecord(ResultSet res) throws SQLException {
		return new Libri(
			res.getInt("codice"),
			res.getString("titolo"),
			res.getInt("pagine"),
			res.getShort("pubblicazione"),
			res.getInt("autore"),
			res.getInt("genere"),
			res.getInt("editore")
		);
	}



	@Override
	protected void fillStatement( PreparedStatement stmt, Libri libro ) throws SQLException {
		stmt.setInt(1, libro.getId());
		stmt.setString(2, libro.getTitolo());
		stmt.setInt(3, libro.getPagine());
		stmt.setShort(4, libro.getPubblicazione());
		stmt.setInt(5, libro.getAutore());
		stmt.setInt(6, libro.getGenere());
		stmt.setInt(7, libro.getEditore());
	}



	// "LibriDAO" ovverrida "insert" e "update" per poter fare
	// i controlli sulle foreign keys, in modo da bloccare
	// l'inserimento il prima possibilie e non durante la
	// transaction di "applyChanges"
	@Override
	public LibriDAO insert( Libri libro ) {
		Connection conn = DBHandler.getConnection(
			getUrl(), getDB(), getUser(), getPassword()
		);
		if( conn == null ) {
			System.out.println("Connection failed");
			return null;
		}
		
		try {
			PreparedStatement[] stmts = new  PreparedStatement[] {
				conn.prepareStatement("SELECT * FROM Autori WHERE codice = ?"),
				conn.prepareStatement("SELECT * FROM Generi WHERE codice = ?"),
				conn.prepareStatement("SELECT * FROM Editori WHERE codice = ?")
			};
			
			stmts[0].setInt(1, libro.getAutore());
			stmts[1].setInt(1, libro.getGenere());
			stmts[2].setInt(1, libro.getEditore());
			
			for( PreparedStatement stmt : stmts )
				if( !stmt.executeQuery().next() ) {
					System.out.println("Unknown foreign key reference");
					return null;
				}	
		} catch(SQLException e) {
			System.out.println("Record's references check error");
			return null;
		}

		super.insert(libro);
		return this; 
	}



	@Override
	public <P> LibriDAO update(int id, String column, P new_value) {
		if(
			column.equals("autore") ||
			column.equals("genere") ||
			column.equals("editore")
		) {
			String tableName = column.equals("autore") ? "Autori" :
					( column.equals("genere") ? "Generi" : "Editori" );
			Connection conn = DBHandler.getConnection(
				getUrl(), getDB(), getUser(), getPassword()
			);
			if( conn == null ) {
				System.out.println("Connection error");
				return null;
			}
			try( PreparedStatement stmt = conn.prepareStatement(
				"SELECT * FROM " + tableName + " WHERE codice = ?"
			) ) {
				stmt.setInt( 1, (int) new_value );
				if( !stmt.executeQuery().next() ) {
					System.out.println("Reference error");
					return null;
				}
			} catch(SQLException e) {
				System.out.println("Reference check error");
				return null;
			} finally {
				DBHandler.closeConnection();
			}
		}
		return (LibriDAO) super.update(id, column, new_value);
	}

}