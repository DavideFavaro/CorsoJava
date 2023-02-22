package it.betacom.service;



import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

import org.apache.log4j.Logger;

import it.betacom.myfilewriter.MyFileWriter;
import it.betacom.myutilities.MyUtilities;

import it.betacom.model.Chiamata;
import it.betacom.model.Contratto;
import it.betacom.model.ContrattoMobile;
import it.betacom.model.ContrattoFisso;



public class ContrattoMain {

	static Logger logger = Logger.getLogger(ContrattoMain.class.getName());

	public static void main(String[] args) {

		ArrayList<String> nomi;
		Chiamata chiamata;
		String file1 = "bollette1",
			   file2 = "bollette2";
		ArrayList<Contratto> contratti = new ArrayList<>();

		logger.debug("Creazione contratti");
		contratti.add(new ContrattoFisso("a", "b", "123", "c", file1));
		contratti.add(new ContrattoMobile("d", "e", "456", file2));
		logger.debug("Inzio generazione bollette");
		for( Contratto c : contratti ) {
			nomi = c.getNomiValori();
			try {
				MyFileWriter.open(c.getFileBollette());
				MyFileWriter.writeTitles("Bollette di " + c.getDatiUtente() + "\n\n");
				MyFileWriter.writeCsv(nomi.toArray(new String[nomi.size()]));
				for(int i = 0; i < 8; ++i) {
					c.aggiornaBolletta(MyUtilities.nextInt(1, 20) * 60);
					chiamata = c.getLastChiamata();
					MyFileWriter.write(c.getEntryString(chiamata));
					MyFileWriter.writeCsv(chiamata.toStringsArray());
				}
				MyFileWriter.close();
			} catch (IOException | DocumentException e) { logger.error(e); }
		}
		logger.debug("Fine genrazione bollette");

	}

}










/*
public static void main(String[] args) {

		String file1 = "bollette1",
			   file2 = "bollette2";
		ArrayList<Contratto> contratti = new ArrayList<>();

		logger.debug("Creazione contratti");
		contratti.add(new ContrattoFisso("a", "b", "123", "c", file1));
		contratti.add(new ContrattoMobile("d", "e", "456", file2));
		logger.debug("Inzio generazione bollette");
		for( Contratto c : contratti ) {
			for(int i = 0; i < 7; ++i)
				c.aggiornaBolletta(Utilities.nextInt(1, 20) * 60);
			try {
				MyFileWriter.open(c);
				MyFileWriter.write(c);
				c.aggiornaBolletta(Utilities.nextInt(1, 20) * 60);
				MyFileWriter.update(c);
				MyFileWriter.close();
			} catch (IOException | DocumentException e) { logger.error(e); }
		}
		logger.debug("Fine genrazione bollette");

	}
*/
