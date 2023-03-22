package it.betacom.ProgettoBiblioteca.dao;



import java.io.IOException;
import java.nio.file.Paths;
import java.lang.reflect.Field;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.betacom.ProgettoBiblioteca.model.Operation;
import it.betacom.ProgettoBiblioteca.model.Operation.OpsType;
import it.betacom.ProgettoBiblioteca.service.DBHandler;
import it.betacom.ProgettoBiblioteca.service.PropertiesHandler;
import it.betacom.ProgettoBiblioteca.service.ReflectionClassHandler;
import it.betacom.ProgettoBiblioteca.model.TableRecord;



public abstract class DAO<T extends TableRecord> {

	// Per mantenere le informazioni sul db a cui ci si connette.
	private Properties properties;
	// Per memorizzare le righe della tabella del db.
	 // E' una Map che ha come chiavi gli id e come valori le righe
	 // corrispondenti sotto forma di ogetto del tipo associato alla
	 // tabella, permettendo di avere ricerca di oggetti per id in
	 // tempo costane ( O(1) ).
	protected Map<Integer, T> records;
	// Lista delle operazioni svolte sul DAO (insert, update, ecc.)
	// che devono ancora essere applicate al database.
	 // Tenere traccia delle operazioni svolte permette di applicarle
	 // al server solo quando viene richiesto, in modo da effettuarle
	 // tutte in un'unica transaction e senza dover creare connessioni
	 // multiple.
	protected List<Operation> pendingOps;



	// Istanzia il campo "properties" utilizzando il file con path "properties_path",
	// se questo file non esiste, lo crea, aggiungendovi il primo String litteral come
	// commento e utilizzando i due vettori di stringhe per definire le proprietà e i
	// rispettivi valori.
	protected void initProperties( String properties_path ) throws IOException {
		this.properties = PropertiesHandler.loadOrCreate(
			properties_path,
			"Databese Properties File",
			new String[]{ "url", "db", "user", "password" },
			new String[]{ "jdbc:mysql://localhost:3306", "es_autori", "root", "betacom1" }
		);
	}

	// Come sopra ma utilizza il file "db_config.properties", presente nella cartella
	// del progetto (anche qui, creandolo se non esiste)
	protected void initProperties() throws IOException {
		initProperties(
			Paths.get(".")
				.normalize()
				.toAbsolutePath()
				+ "\\db_config.properties"
		);
	}



	public String getUrl() {
		return this.properties.getProperty("url");
	}



	public String getDB() {
		return this.properties.getProperty("db");
	}



	public String getUser() {
		return this.properties.getProperty("user");
	}



	protected String getPassword() {
		return this.properties.getProperty("password");
	}



	// Ritorna il nome della classe passata come parametro per la classe DAO.
	 // Si assume che le classi rappresentanti le righe delle tabelle abbiano
	 // lo stesso nome della tabella ( es. tabella Autori -> classe Autori ).
	public String getTableName() {
		return ReflectionClassHandler.stripClassName(
			ReflectionClassHandler.getTypeParameterClass(this)
		);
	}



	// Ritorna una lista con i nomi dei campi della classe parametro.
	 // Si assume che i campi abbiano lo stesso nome delle corrispondenti
	 // colonne del database.
	 // Si assume, inoltre, che i campi siano anche nello stesso ordine
	 // delle colonne della tabella.
	@SuppressWarnings("unchecked")
	public List<String> getRecordFields() {
		ArrayList<String> fields_strs = new ArrayList<>();
		// Per ogni campo della classe parametro inserisci il suo
		// nome nella lista
		for (
			Field field : ( (Class<T>)
						      ReflectionClassHandler.getTypeParameterClass(this)
					      ).getDeclaredFields()
		)
			fields_strs.add(field.getName());
		return fields_strs;
	}



	public List<Operation> getPendingOps() {
		return this.pendingOps;
	}



	private Map<Integer, T> getRecords() {
		return this.records;
	}



	// Aggiunge una nuova operazione alla lista di operazioni da applicare.
	private void addOperation( int id, OpsType type ) {
		this.pendingOps.add(new Operation(id, type));
	}



	private void addRecord( T record ) {
		this.records.put( record.getId(), record );
	}



	// Rimuove tutte le operazioni da applicare
	private void clearOperations() {
		this.pendingOps.clear();
	}



	// Converte un ResultSet rappresentante una riga di una tabella
	// nel corrispondente oggetto.
	// Metodo astratto che dovrà essere implementato in ogni sottoclasse.
	 // Non è stato possibile implementare questo metodo in modo generico
	 // in quanto dovrebbe richiamare il costruttore della classe parametro
	 // (fattibile tramite Reflection) su un insieme di parametri che (per
	 // quanto ne so) devono essere specificati singolarmente e separatamente
	 // (non possono essere inseriti tramite un for o come vettore di valori).
	protected abstract T resultSetToRecord( ResultSet res ) throws SQLException;



	// Funzione con cui vengono aggiunti i valori ad un prepared statement.
	 // (ad esempio con "INSERT INTO Autori VALUES ( ?, ?, ...)" la funzione
	 // sostituisce ai "?" i valori corrispondenti ).
	 // L'implementazione viene delegata alle sottoclassi in qunato non riesco
	 // a trovare un modo efficace di utilizzare la reflection per gestire tipi
	 // di valore particolare (ad esempio inserire nello statement il valore
	 // contenuto in un "Optional", se presente, o "null", se assente, è inoltre
	 // un problema gestire l'inserimento dei valori delle enum).
	protected abstract void fillStatement( PreparedStatement stmt, T record ) throws SQLException;



	public Collection<T> getAll() {
		return getRecords().values();
	}



	// Avendo utilizzato una struttura dati diversa da una Map
	// l'operazione di ricerca sarebbe stata più complicata e
	// più lenta.
	public T getRecordByID(int id) {
		return getRecords().get(id);
	}



	// Tutte le operazioni su DAO ritornano l'ogetto stesso per
	// permettere il "chaining" dei metodi.
	public DAO<T> insert( T object ) {
		addOperation( object.getId(), OpsType.INSERT );
		addRecord(object);
		return this;
	}



	public <P> DAO<T> update( int id, String column, P new_value ) {
		try {
			T record = getRecordByID(id);
			// Modifica del field di "record" che ha nome "column"
			Field field = record.getClass().getDeclaredField(column);
			field.setAccessible(true);
			field.set(record, new_value);
			field.setAccessible(false);
			addOperation(id, OpsType.UPDATE);
			return this;
		}
		catch(Exception e) {
			if( e instanceof NoSuchFieldException )
				System.out.println("Field acquisition error");
			if( e instanceof IllegalArgumentException )
				System.out.println("Field value error");
			System.out.println(e.getMessage());
			return null;
		}
	}



	public DAO<T> delete( int id ) {
		addOperation(id, OpsType.DELETE);
		getRecords().remove(id);
		return this;
	}




	// Aggiorna la lista degli oggetti contenuti nel DAO, andando a
	// prendere quelli contenuti nel database.
	public DAO<T> updateDAO() {
		Connection conn = DBHandler.getConnection(
			getUrl(), getDB(), getUser(), getPassword()
		);
		if( conn == null ) {
			System.out.println("Connection failed");
			return null;
		}

		try( PreparedStatement stmt = conn.prepareStatement(
			"SELECT * FROM " + getTableName()
		)) {
			ResultSet res = stmt.executeQuery();
			while( res.next() )
				addRecord( resultSetToRecord(res) );
			DBHandler.closeConnection();
		} catch( SQLException e ) {
			DBHandler.closeConnection();
			System.out.println("DAO records update failed\n" + e.getMessage());
			return null;
		}
		
		return this;
	}



	// Applica tutti i cambiamenti registrati nella lista di operazioni al database in una singola
	// transazione ed utilizzando una singola connessione, alla conclusione della transazione
	// svuota la lista di operazioni (solo se la transazione ha avuto successo).
	 // La funzione ritorna un Optional<Integer>:
	 // - vuoto, per una transaction avvenuta con sucesso
	 // - contenente l'indice dell'operazione fallita
	 // - contenente "-1", in caso di connessione fallita
	public Optional<Integer> applyChanges() {
		Operation op;
		List<Operation> ops = getPendingOps();
		
		if( !ops.isEmpty() ) {
			List<String> recordfields;
			PreparedStatement stmt;
			int i = 0;
			Connection conn = DBHandler.getConnection(
				getUrl(), getDB(), getUser(), getPassword()
			);

			if( conn == null ) {
				System.out.println("Connection failed");
				return Optional.of(-1);
			}

			try {
				conn.setAutoCommit(false);
				for(; i < ops.size(); ++i ) {
					op = ops.get(i);
					recordfields = getRecordFields();
					switch(op.type) {
						case INSERT:
							stmt = conn.prepareStatement(
								"INSERT INTO " + getTableName() + " ( "
								// Crea una stringa unendo i valori di "recordfields"
								// mettendo tra di essi la stringa indicata
								+ String.join( ", ", recordfields )
								+ " ) VALUES "
									// Il pezzo di codice sottostante crea una stringa del tipo
									// ( ?, ?, ? ) con un numero di "?" pari al numero di
								    // elementi in "recordfields"
									+ Stream.generate(() -> "?")
								  		.limit(recordfields.size())
								  		.collect( Collectors.joining( ", ", "( ", " )" ) )
							);
							fillStatement( stmt, getRecordByID(op.id) );
							stmt.executeUpdate();
							break;
						case UPDATE:
							stmt = conn.prepareStatement(
								"UPDATE " + getTableName()
								+ " SET "
									// Crea una stringa contenente la lista di ogni campo
								    // presente seguito da "= ?".
									// (esempio di risultato: "codice = ?, descrizione = ?")
									+ recordfields.stream().collect(Collectors.joining(" = ?, ")) + " = ?"
									// "WHERE codice = ?" (codice è come chiamo le PK nelle mia tabelle)
								+ " WHERE " + recordfields.get(0) + " = ?"
							);
							// "filStatement" aggiunge tutti i campi dell'oggetto
							// allo statement in ordine, senza contare possibili
							// "WHERE", quindi bisogna comunque passargli il valore
							// dell'id
							fillStatement( stmt, getRecordByID(op.id) );
							stmt.setInt( recordfields.size() + 1, op.id );
							stmt.executeUpdate();
							break;
						case DELETE:
							stmt = conn.prepareStatement(
								"DELETE FROM " + getTableName()
								+ " WHERE " + recordfields.get(0) + " = ?"
							);
							stmt.setInt(1, op.id);
							stmt.executeUpdate();
							break;
					}
				}
				conn.commit();
				clearOperations();
				DBHandler.closeConnection();
			} catch( SQLException e ) {
				System.out.println("Operations execution error\n" + e.getMessage());
				try { conn.rollback(); } catch(SQLException rbe) {
					System.out.println("Rollback error\n" + rbe.getMessage());
				} finally {
					DBHandler.closeConnection();
				}
				// Se si verifica un errore si ritorna l'Optional con il
				// valore dell'indice usato per scorrere la lista di operazioni,
				// che dovrebbe avere il valore dell'operazione per cui si è
				// verificato l'errore
				return Optional.of(i);
			}
		}

		return Optional.empty();
	}


}