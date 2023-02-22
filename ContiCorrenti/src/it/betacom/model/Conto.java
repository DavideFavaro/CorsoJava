package it.betacom.model;



import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;



/**
 * Classe astratta rappresentante un conto bancario.
 * <p>
 * Sue implementazioni sono:
 * <ul>
 * <li><code>ContoCorrente</code>
 * <li><code>ContoDeposito</code>
 * <li><code>ContoInvestimento</code>
 * </ul>
 * 
 * @author Davide Favaro
 * 
 * @see ContoCorrente
 * @see ContoDeposito
 * @see ContoInvestimento
 */
public abstract class Conto {

	private String titolare;
	private double saldo, tasso;
	private ArrayList<Operazione> operazioni; 



	/**
	 * Crea un nuovo <code>Conto</code> con i dati indicati, andando ad inserire un <code>Operazione</code> di tipo
	 * <code>Operazione.OpType.APERTURA</code> nel registro delle operazioni del conto.
	 * 
	 * @param titolare
	 * 		Titolare del conto.
	 * @param data_apertura
	 * 		Data di apertura del conto.
	 * @param tasso
	 * 		Tasso di interesse sul conto.
	 * 
	 * @see Operazione
	 * @see Operazione.OpType
	 */
	protected Conto (String titolare, LocalDate data_apertura, double tasso) {
		this.titolare = titolare;
		this.saldo = 0.0;
		this.tasso = tasso;
		this.operazioni = new ArrayList<>();
		operazioni.add( new Operazione(0.0, 0.0, data_apertura, Operazione.OpType.APERTURA) );
	}



	public double getTasso() {
		return tasso;
	}



	public void setTasso(double tasso) {
		this.tasso = tasso;
	}



	public String getTitolare() {
		return titolare;
	}



	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}



	public double getSaldo() {
		return saldo;
	}



	public LocalDate getDataApertura() {
		return this.operazioni.get(operazioni.size()-1).data;
	}


	/**
	 * Ritorna la data dell'ultima riscossione degli interessi maturati, o,
	 * nel caso gli interessi non siano mai stati riscossi,
	 * la data di apertura del conto.
	 * 
	 * @return
	 * 		<code>LocalDate</code> rappresentante la data dell'ultima riscossione.
	 */
	public LocalDate getUltimaRiscossione() {
		int i = 0;
		for(; i < operazioni.size() &&
			operazioni.get(i).tipo != Operazione.OpType.RISCOSSIONE &&
			operazioni.get(i).tipo != Operazione.OpType.APERTURA; ++i );
		return operazioni.get(i).data;
	}



	/**
	 * Registra una nuova operazione bancaria.
	 * <p>
	 * <b>N.B.</b>: Non è consentito registrare operazioni di apertura o chiusura con questo metodo.
	 * 
	 * @param amount
	 * 		Valore della transazione.
	 * @param saldoSucc
	 * 		Saldo successivo alla transazione.
	 * @param data
	 * 		Data della transazione.
	 * @param tipo
	 * 		Tipo della transazione.
	 * 
	 * @return
	 * 		<code>true</code>, se la transazione è andata a buon fine, <code>false</code>, altrimenti.
	 * 
	 * @see Operazione
	 * @see Conto
	 * @see chiudiConto
	 */
	protected synchronized boolean addOperazione( double amount, double saldoSucc, LocalDate data, Operazione.OpType tipo ) {
		if( tipo == Operazione.OpType.APERTURA || tipo == Operazione.OpType.CHIUSURA )
			return false;
		this.operazioni.add(0, new Operazione(amount, saldoSucc, data, tipo));
		return true;
	}



	/**
	 * Deposita la cifra indicata in data indicata, pecificando se la transazione è una riscossione di interessi.
	 * <p>
	 * <b>N.B.</b>: il deposito non è possibile a seguito della chiusura di un conto.
	 * 
	 * @param amount
	 * 		Quantità di denaro da depositare.
	 * @param data
	 * 		Data del deposito.
	 * @param perInteressi
	 * 		Flag per indicare se il deposito è a seguito della risossione di interessi o meno.
	 * 
	 * @return
	 * 		<code>true</code>, se l'operazione è andata a buon fine, <code>false</code>, altrimenti.
	 * 
	 * @see addOperazione
	 */
	protected synchronized boolean deposita( double amount, LocalDate data, boolean perInteressi ) {
		if( amount <= 0.0 || operazioni.get(0).tipo == Operazione.OpType.CHIUSURA )
			return false;
		this.saldo += amount;
		if( perInteressi )
			addOperazione(amount, this.saldo, data, Operazione.OpType.RISCOSSIONE);
		else	
			addOperazione(amount, this.saldo, data, Operazione.OpType.DEPOSITO);
		return true;
	}

	/**
	 * Deposita una quantità di denaro pari a <code>amount</code> in data <code>data</code>.
	 * <p>
	 * <b>N.B.</b>: il deposito non è possibile a seguito della chiusura di un conto.
	 * 
	 * @param amount
	 * @param data
	 * 
	 * @return
	 * 		<code>true</code>, se l'operazione è andata a buon fine, <code>false</code>, altrimenti.
	 * 
	 * @see addOperazione
	 */
	public synchronized boolean deposita( double amount, LocalDate data ) {
		return deposita(amount, data, false);
	}


	/**
	 * Preleva una quantità di denaro pari a <code>amount</code> in data <code>data</code>.
	 * <p>
	 * <b>N.B.</b>: il prelievo non è possibile a seguito della chiusura di un conto.
	 * 
	 * @param amount
	 * @param data
	 * 
	 * @return
	 * 		<code>Optional&lt;Double&gt;</code> contenente il valore relevato, se l'operazione fallisce viene ritornato un
	 * 		Optional vuoto.
	 * 
	 * @see addOperazione
	 * @see Optional
	 */
	public synchronized Optional<Double> preleva( double amount, LocalDate data ) {
		if( amount > getSaldo() || operazioni.get(0).tipo == Operazione.OpType.CHIUSURA )
			return Optional.empty();
		this.saldo -= amount;
		addOperazione(amount,  this.saldo, data, Operazione.OpType.PRELIEVO);
		return Optional.of(amount);
	}



	/**
	 * Calcola il moltiplicatore da utilizzare per il calcolo degli interessi.
	 * 
	 * @param inizio
	 * 		Data da cui si è cominciato a maturare interessi, oppure data in cui vi è stata una variazione del saldo.
	 * @param fine
	 * 		Data di riscossione, oppure data in cui vi è stata una ulteriore variazione del saldo.
	 * @param tasso
	 * 		Tasso di interesse.
	 * 
	 * @return
	 * 		Valore da moltiplicare per il saldo per ottenere il guadagno.
	 * 
	 * @see generaInteressi
	 */
	public static double calcolaInteressi(LocalDate inizio, LocalDate fine, double tasso) {
		return ChronoUnit.DAYS.between(inizio, fine) / 365 * tasso;
	}


	/**
	 * Calcola il guadagno basato sugli interessi maturati.
	 * 
	 * @param data
	 * 		Data di riscossione.
	 * 
	 * @return
	 * 		Guadagno.
	 * 
	 * @see calcolaInteressi
	 */
	public synchronized double generaInteressi(LocalDate data) {

		double res = 0.0;
		Operazione op;

		if( data.isBefore(getDataApertura()) ||
			data.isAfter(LocalDate.now()) ||
			operazioni.get(0).tipo == Operazione.OpType.CHIUSURA )
			return res;

		for( int i = 0; i < this.operazioni.size(); ++i ) {
			op = operazioni.get(i);
			if( op.tipo == Operazione.OpType.RISCOSSIONE || op.tipo == Operazione.OpType.CHIUSURA )
				return res;
			res += op.saldoSucc * calcolaInteressi(op.data, data, getTasso());
			if(op.tipo == Operazione.OpType.APERTURA)
				break;
		}
		deposita(res, data, true);
		return res;
	}



	/**
	 * Chiudi il conto rendendo ogni operazione successiva impossibile.
	 * 
	 * @param data
	 * 		Data di chiusura.
	 */
	public synchronized void chiudiConto(LocalDate data) {
		this.operazioni.add(new Operazione(0.0, getSaldo(), data, Operazione.OpType.CHIUSURA));
	}

}