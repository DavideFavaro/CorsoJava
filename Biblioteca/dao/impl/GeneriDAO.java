package it.betacom.ProgettoBiblioteca.dao.impl;



import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import it.betacom.ProgettoBiblioteca.dao.DAO;
import it.betacom.ProgettoBiblioteca.model.Generi;



public class GeneriDAO extends DAO<Generi> {
	
	public GeneriDAO () {
		this.records = new HashMap<Integer, Generi>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}

	public GeneriDAO ( String properties_path ) {
		this.records = new HashMap<Integer, Generi>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(properties_path); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}



	@Override
	protected Generi resultSetToRecord(ResultSet res) throws SQLException {
		return new Generi( res.getInt("codice"), res.getString("descrizione") );
	}




	@Override
	protected void fillStatement( PreparedStatement stmt, Generi genere ) throws SQLException {
		stmt.setInt( 1, genere.getId() );
		stmt.setString( 2, genere.getDescrizione() );
	}

}