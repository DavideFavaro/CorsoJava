package it.betacom.ProgettoBiblioteca.service;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Map;
import java.util.Properties;



// Usato per gestire le "Properties" (al momento quelle del db,
// dovrebbe essere usata anche per quelle relative alla stampa).
 // Questa classe potrebbe esssere fatta diversamente,
 // sto ancora valutando.
public class PropertiesHandler {

	// Queste funzioni fanno il "load" delle properties dal file
	// indicato, se non lo trovano lo creano utilizzando i
	// parametri passati.
	public static Properties loadOrCreate( String path, String title, String property, String value ) throws IOException {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(path));
		} catch(FileNotFoundException e) {
			props.setProperty(property, value);
			props.store( new FileOutputStream(path), title );
		}
		return props;
	}
	
	public static Properties loadOrCreate( String path, String title, String[] properties, String[] values ) throws IOException {
		if( properties.length != values.length )
			return null;
		else {
			Properties props = new Properties();
			try {
				props.load(new FileInputStream(path));
			} catch(FileNotFoundException e) {
				for( int i = 0; i < properties.length; ++i )
					props.setProperty(properties[i], values[i]);
				props.store( new FileOutputStream(path), title );
			}
			return props;
		}
	}

	public static Properties loadOrCreate( String path, String title, Map<String, String> properties ) throws IOException {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(path));
		} catch(FileNotFoundException e) {
			props.putAll(properties);
			props.store( new FileOutputStream(path), title );
		}
		return props;
	}

}
