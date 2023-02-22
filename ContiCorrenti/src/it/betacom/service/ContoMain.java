package it.betacom.service;



import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import it.betacom.model.Conto;
import it.betacom.model.ContoCorrente;
import it.betacom.model.ContoDeposito;
import it.betacom.model.ContoInvestimento;



public class ContoMain {

	public static void main(String[] args) {

		Conto c;
		List<Conto> conti = Arrays.asList(
			new ContoCorrente( "a", LocalDate.of(2021, 12, 1) ),
			new ContoDeposito( "b", LocalDate.of(2021, 5, 7) ),
			new ContoInvestimento( "c", LocalDate.of(2021, 8, 24) )
		);
				

		for( int i = 0; i < conti.size(); ++i  ) {
			c = conti.get(i);
			System.out.printf( "Conto Corrente di %s\n", c.getTitolare() );
			c.deposita(1500, c.getDataApertura());
			System.out.printf( "\tSaldo iniziale: €%.2f\n", c.getSaldo() );
			System.out.printf( "\tInteressi generati: €%.2f\n", c.generaInteressi(LocalDate.of(2021, 12, 31)) );
			System.out.printf( "\tSaldo: €%.2f\n", c.getSaldo() );
			System.out.println(
				"\tPrelievo di €900 " +
				( c.preleva(900, LocalDate.of(2022, 7, 1) ).isPresent() ?"riuscito" : "fallito" )
			);
			System.out.printf( "\tInteressi generati: €%.2f\n", c.generaInteressi(LocalDate.of(2022, 12, 31)) );
			System.out.printf( "\tSaldo: €%.2f\n", c.getSaldo() );
			System.out.printf( "\tInteressi generati: €%.2f\n", c.generaInteressi(LocalDate.now()) );
			System.out.printf( "\tSaldo finale: €%.2f\n\n", c.getSaldo() );
			c.chiudiConto(LocalDate.now());
		}

	}

}