package it.betacom.myfilewriter;



import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;

import com.opencsv.CSVWriter;



/**
 * <h2>MyFileWriter</h2>
 * Classe per permettere di stampare stringhe su file di vario formato.
 * <p>
 * Formati supportati:
 * <ul>
 * <li>Plain text (.txt)
 * <li>PDF (.pdf)
 * <li>CSV (.csv)
 * </ul>
 * 
 * @author Davide Favaro
 */
public class MyFileWriter {
	
	/**
	 * Enum utilizzata per rappresentare i vari tipi di documento con cui si può lavorare.
	 * 
	 * @see MyFileWriter
	 * @see writeTitle
	 */
	public enum DocumentType { TXT, PDF, CSV }


	private static BufferedWriter txt_writer;
	private static Document document;
	private static PdfWriter pdf_writer;
	private static CSVWriter csv_writer;


	/** 
	 * Crea il file di tipo specificato in <code>file_path</code>, se il file non ha estensione,
	 * crea un file per ogni formato con il nome e la posizione specificata dal parametro.
	 * 
	 * @param file_path
	 * 		Path per il file da creare con o senza estensione specificata.
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void open( String file_path ) throws IOException, DocumentException {
		String ext = "";
		int i = file_path.lastIndexOf(".");
		if( i != -1 ) {
			ext = file_path.substring(i+1);
			file_path = file_path.substring(0, i);
		}
		// Se l'estensione del documento non è presente crea tutti i documenti
		 // Se è presente crea il documento corrispondente
		switch(ext) {
			case "":
			case "txt":
				txt_writer = new BufferedWriter(new FileWriter(file_path+".txt"));
				if(ext != "")
					break;
			case "pdf":
				document = new Document();
				pdf_writer = PdfWriter.getInstance( document, new FileOutputStream(file_path+".pdf") );
				document.open();
				if(ext != "")
					break;
			case "csv":
				csv_writer = new CSVWriter(
					new FileWriter(file_path+".csv"), ';',
					CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END
				);
				break;
			default:
				throw(new IllegalArgumentException("Unsuitable file extension"));
		}
	}



	/**
	 * Aggiungi ad ogni documento creato il titolo <code>title</code>.
	 * 
	 * @param title
	 * 		Titolo da aggiungere ai documenti creati.
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * 
	 * @see open
	 */
	public static void writeTitles( String title ) throws IOException, DocumentException {
		if(txt_writer != null)
			txt_writer.write(title);
		if(pdf_writer != null) {
			document.addTitle(title);
			document.add(new Paragraph(
				title + "\n\n",
				new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD)
			));
		}
	}

	/**
	 * Aggiungi al singolo documento di tipo <code>tipo</code> il titolo <code>title</code>.
	 * Il documento deve essere stato precedentemente creato.
	 * <p>
	 * <b>N.B.</b>: Non funziona per l'aggiunta dei nomi delle colonne ai file CSV.
	 * 
	 * @param title
	 * 		Titolo da aggiungere ai documenti creati.
	 * @param tipo
	 * 		<code>DocumentType</code> rappresentante il documento a cui aggiungere il titolo.
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * 
	 * @see open
	 * @see writeTitles
	 * @see writeCsvTitle
	 */
	public static void writeTitle( String title, DocumentType tipo ) throws IOException, DocumentException {
		switch(tipo) {
			case TXT: 
				txt_writer.write(title);
				break;
			case PDF:
				document.addTitle(title);
				document.add(new Paragraph(
					title + "\n\n",
					new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD)
				));
				break;
			case CSV:
				throw(new IllegalArgumentException("Use writeCSVTitleInstead") );
		}
	}

	/**
	 * Aggiungi al solo file CSV creato i nomi delle colonne specificati da <code>column_names</code>.
	 * 
	 * @param column_names
	 * 		Nomi da assegnare alle colonne del file.
	 * 
	 * @see open
	 * @see writeTitle
	 * @see writeTitles
	 */
	public static void writeCsvTitle( String[] column_names ) {
		csv_writer.writeNext(column_names);
	}



	/**
	 * Scrivi la stringa <code>content</code> sui documenti creati.
	 * <p>
	 * <b>N.B.</b>: Non scrive su eventuali file in formato tabellare.
	 * 
	 * @param content
	 * 		Stringhe da scrivere sui file.
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * 
	 * @see open
	 * @see writeCsv
	 */
	public static void write( String content ) throws IOException, DocumentException {
		if(txt_writer != null) {
			txt_writer.append(content);
			txt_writer.flush();
		}
		if(pdf_writer != null) {
			document.add(new Paragraph(
				content,
				new Font(FontFamily.TIMES_ROMAN, 14)
			));
		}
	}
	
	/**
	 * Scrivi la stringa <code>content</code> nel solo tipo di documento indicato da <code>tipo</code>.
	 * <p>
	 * <b>N.B.</b>: Non scrive su eventuali file in formato tabellare.
	 * 
	 * @param content
	 * 		Stringa da inserire nel documento.
	 * @param tipo
	 * 		<code>DocumentType</code> rappresentante il documento a cui aggiungere il titolo.
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * 
	 * @see open
	 * @see writeTitles
	 * @see writeCsvTitle
	 */
	public static void write( String content, DocumentType tipo ) throws IOException, DocumentException {
		switch(tipo) {
			case TXT:
				txt_writer.append(content);
				txt_writer.flush();
				break;
			case PDF:
				document.add(new Paragraph(
					content,
					new Font(FontFamily.TIMES_ROMAN, 14)
				));
				break;
			case CSV:
				throw(new IllegalArgumentException("Use writeCSVTitleInstead") );
		}
	}

	/**
	 * Scrivi una nuova riga sul file CSV con i dati contenuti in <code>content</code>.
	 * 
	 * @param content
	 * 		Valori da scrivere sul CSV (Sotto forma di stringhe).
	 * 
	 * @see open
	 * @see write
	 */
	public static void writeCsv( String[] content ) {
		csv_writer.writeNext(content);
	}


	/**
	 * Chiudi tutti i documenti aperti.
	 * 
	 * @throws IOException
	 * 
	 * @see open
	 */
	public static void close() throws IOException {
		if(txt_writer != null)
			txt_writer.close();
		if(pdf_writer != null) {
			document.close();
			pdf_writer.close();
		}
		if(csv_writer != null)
			csv_writer.close();
	}

}