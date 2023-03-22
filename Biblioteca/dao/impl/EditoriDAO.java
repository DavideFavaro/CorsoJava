package it.betacom.ProgettoBiblioteca.dao.impl;



import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import it.betacom.ProgettoBiblioteca.dao.DAO;
import it.betacom.ProgettoBiblioteca.model.Editori;



public class EditoriDAO extends DAO<Editori> {

	public EditoriDAO () {
		this.records = new HashMap<Integer, Editori>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}

	public EditoriDAO ( String properties_path ) {
		this.records = new HashMap<Integer, Editori>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(properties_path); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}



	@Override
	protected Editori resultSetToRecord(ResultSet res) throws SQLException {
		return new Editori(
			res.getInt("codice"),
			res.getString("nome"),
			res.getString("sede")
		);
	}


	@Override
	protected void fillStatement( PreparedStatement stmt, Editori editore ) throws SQLException {
		stmt.setInt(1, editore.getId());
		stmt.setString(2, editore.getNome());
		stmt.setString(3, editore.getSede());
	}

}