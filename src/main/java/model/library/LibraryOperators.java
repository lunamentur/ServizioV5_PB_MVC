package main.java.model.library;
import main.java.controller.Controller;
import main.java.controller.ControllerOperators;
import main.java.controller.ControllerResources;
import main.java.model.*;
import main.java.view.ViewLibraryGeneral;


import java.time.LocalDate;

/**
 * Classe intermedia che permette l'interazione tra la classe Admin-User e la classe db.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class LibraryOperators {
	private Database db;
	private ControllerResources cr;
	private ControllerResources co;

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
	 * Metodo che controlla se l'user ha piu\' di 3 prestiti previsti per una tipologia, o categoria, di risorsa.
	 * @param user {@link User}
	 * @return true se non puo\' piu\' prendere in prestito altre risorse, false se invece ha ancora disponibili dei prestiti da poter fare.
	 */
	private boolean checkBorrowed(User user, int number){
		Integer [] value= user.getBorrowed();
		return value[number] >= 3;
	}

	/**
	 * Metodo che raccoglie tutti i metodi per richiedere e verificare la creazione di un prestito.
	 */
	public  void createRequestPrestito(int barcode, String username){
		/**
		 * controllo il tipo della risorsa cercata.
		 * controllo che l'utente possa effettivamente prendere in prestito una risorsa.
		 */
		int bookOrFilm=0;
		bookOrFilm = db.choiceTypeResource(barcode);
		if (!checkBorrowed(db.getUser(username), bookOrFilm)){
			/**
			 * genero il codice del prestito.
			 */
			System.out.println(db.getResource(barcode).toString());
			String codePrestito = co.generateId(username,barcode);
			/**
			 * creo il prestito e lo salvo nel database, aumentando di uno il numero di licenze dell'utente.
			 * Avendo già identivicato prima il tipo di risorsa ne modificando quindi il numero associato.
			 */
			cr.createPrestito(codePrestito, username, barcode, bookOrFilm);
		} else System.out.println(Constant.FINITE_LICENZE_PRESTITO_USER);
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

}
