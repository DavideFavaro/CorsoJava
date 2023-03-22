package it.betacom.ProgettoBiblioteca.dao.impl;



import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import it.betacom.ProgettoBiblioteca.dao.DAO;
import it.betacom.ProgettoBiblioteca.model.Autori;
import it.betacom.ProgettoBiblioteca.model.Autori.Sesso;



public class AutoriDAO extends DAO<Autori> {

	
	public AutoriDAO () {
		this.records = new HashMap<Integer, Autori>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}

	public AutoriDAO ( String properties_path ) {
		this.records = new HashMap<Integer, Autori>();
		this.pendingOps = new ArrayList<>();
		try { initProperties(properties_path); }
		catch(IOException e) {
			System.out.println("Properties initialization error\n" + e.getMessage());
		}
	}



	@Override
	protected Autori resultSetToRecord(ResultSet res) throws SQLException {
		short morte = res.getShort("morte");
		return new Autori(
			res.getInt("codice"),
			res.getString("nome"),
			res.getString("nazione"),
			res.getShort("nascita"),
			morte == 0 ? Optional.empty() : Optional.of(morte),
			res.getString("sesso").equalsIgnoreCase("maschio") ? Sesso.M : Sesso.F
		);
	}



	@Override
	protected void fillStatement( PreparedStatement stmt, Autori autore ) throws SQLException {
		Optional<Short> morte = autore.getMorte();
		stmt.setInt( 1, autore.getId() );
		stmt.setString( 2, autore.getNome() );
		stmt.setString( 3, autore.getNazione() );
		stmt.setShort( 4, autore.getNascita() );
		if( morte.isPresent() )
			stmt.setShort(5, morte.get());
		else
			stmt.setNull(5, java.sql.Types.SMALLINT);
		stmt.setString( 6, autore.getSesso() );
	}

}