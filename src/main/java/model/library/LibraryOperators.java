package main.java.model.library;
import main.java.controller.ControllerResources;
import main.java.model.*;


import java.time.LocalDate;

/**
 * Classe intermedia che permette l'interazione tra la classe Admin-User e la classe db.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class LibraryOperators implements LibraryControllerInterface {
	private Database db;

	/**
	 * Creazione di variabili e oggetti utili per i metodi di controllo relativi all'User.
	 */
	private  long rangeYear=5;
	private  long rangeDay=-10;

	/**
	 * Costruttore
	 * @param db database
	 */
	public LibraryOperators(Database db){
		this.db = db;
	}
	/**
	 * METODI CONTROLLO USER
	 */

	/**
	 * Metodo che permette il rinnovo dell'iscrizione dell'user se scaduta (o in procinto di scadere).
	 * Se si decide di rinnovare l'iscrizione allora viene incrementata l'etichetta del numero del rinnovo (utile per le infomarzioni storiche)
	 * @param user {@link User}
	 */
	public  void renewalRegistration(User user){
		user.setRegistrationDate(LocalDate.now());
		int temp= user.getNumRenewal()+1;
		user.setNumRenewal(temp);
	}

	/**
	 * Metodo che controlla che l'iscrizione dell'user sia scaduta o nel range impostato per il rinnovo anticipato.
	 * @return true se l'iscrizione dell'user è scaduta, quindi può essere rinnovata.
	 * @return false se l'iscrizione dell'user non è scaduta e non è nel range dei giorni di rinnovo.
	 */
	public  boolean isRenewal(User user) {
		return user.getRegistrationDate().plusYears(rangeYear).isBefore(LocalDate.now()) && user.getRegistrationDate().plusDays(rangeDay).isBefore(LocalDate.now());
	}

	/**
	 * Metodo che controlla che l'iscrizione dell'user sia scaduta.
	 *
	 * @return false se l'iscrizione dell'user non è scaduta.
	 */
	public  boolean isExpired(User user) {
		boolean equalYearCondition = user.getRegistrationDate().plusYears(rangeYear).getYear() == LocalDate.now().getYear() && LocalDate.now().isAfter(user.getRegistrationDate().plusYears(rangeYear));
		boolean differentYearCondition = LocalDate.now().getYear() > user.getRegistrationDate().plusYears(rangeYear).getYear();
		if (equalYearCondition || differentYearCondition) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo che se l'utente ha l'iscrizione scaduta, lo contrassegna come scaduto tramite il name e non la chiave primaria username.
	 * l'utente sarà costretto registrarsi nuovamente.
	 */
	public  void userExpired(User user) {
		if (isExpired(user)) {
			System.out.println(Constant.SCADUTA_NON_RINNOVABILE);
			user.setName("_expired_");
		}
	}


	/**
	 * Data corretta per l'inserimento.
	 */
	public boolean trueDate(int month, int day) {
		boolean corretta=false;
		if((String.valueOf(month).length()<=2 && month <= 12 ) && (( String.valueOf(day).length() <= 2) && day <= 31)) {
			corretta=true;
		}
		return corretta;
	}

	@Override
	public boolean checkIfExist(int barcode) {
		return false;
	}

	@Override
	public boolean checkIfProroga(String codePrestito) {
		return false;
	}

	@Override
	public boolean checkProrogaPrestito(String codePrestito) {
		return false;
	}

	@Override
	public void prorogaPrestito(String codePrestito) {

	}

	@Override
	public void incrementNumProroga(String codePrestito) {

	}

	@Override
	public String generateId(String username, int barcode) {
		return null;
	}

	@Override
	public boolean checkIfResourceFree(int barcode) {
		return false;
	}

	@Override
	public boolean checkBorrowed(User user, int number) {
		return false;
	}

}
