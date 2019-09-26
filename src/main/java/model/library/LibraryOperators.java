package main.java.model.library;

import database.Database;
import operator.Admin;
import operator.User;
import view.View;

import java.time.LocalDate;

import static view.View.MG_AZIONE_SUCCESSO;

/**
 * Classe intermedia che permette l'interazione tra la classe Admin-User e la classe Database.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class LibraryOperators {

	public static LocalDate birthDate;
	private static View view=new View();
	private static int vincolo= 3;

	/**
	 * Creazione di variabili e oggetti utili per i metodi di controllo relativi all'User.
	 */
	private static String id, username, searchString;
	private static int year, month, choice, barcode, day, temp;
	private static long rangeYear=5;
	private static long rangeDay=-10;

	/**
	 * inizializzazione delle licenze utente.
	 */
	private static Integer [] borrowed={0,0};

	/**
	 * METODI CONTROLLO USER
	 */

	/**
	 * Metodo che assembla i metodi per la registrazione con i relativi controlli e salva il nuovo user,
	 * oggetto di tipo User, all'interno del Database.
	 */
	public static boolean registrationProcess(){
		User user = new User(LibraryGeneral.insertString(View.NOME), LibraryGeneral.insertString(View.COGNOME), insertUserName(), LibraryGeneral.insertString(View.PASSWORD), insertDate(), LocalDate.now(), borrowed);
        if (Database.checkIf18(user.getBirthDate())) {
			Database.insertUser(user);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Metodo che permette la registrazione di un nuovo admin.
	 * Controlla preventivamente se l'username inserito &egrave; gi&agrave; esistente.
	 */
	public static void registrationAdmin() {
		boolean end = false;
		do {
			username = LibraryGeneral.insertString(View.USER_NAME);
			if (!Database.checkIfAdmin(username)) {
				String password = LibraryGeneral.insertString(View.PASSWORD);
				Admin newAdmin = new Admin(username, password);
				Database.insertAdmin(newAdmin);
				end = true;
			} else System.out.println(View.USERNAME_ESISTE);
		} while (!end);
	}

	/**
     * Metodo che controlla se l'username &egrave; gia presente nel Database.
	 * Se si continua a ciclare finche\' non ne viene inserito uno nuovo (inesistente).
	 * @return username
	 */
	public static String insertUserName(){
		boolean end=false;
		do{
			username=LibraryGeneral.insertString(View.USER_NAME);
			if(Database.checkIfUser(username)){
				view.stampaRichiestaSingola(View.USERNAME_ESISTE);
			}else end=true;
		}while(!end);
		return username;
	}

	/**
	 * Medoto per inserire la data di nascita, di tipo LocalDate, dell'user.
	 * Prende da tastiera anno, mese e giorno e crea una data di tipo LocalDate.
	 * @return birthDate
	 */
	public static LocalDate insertDate(){
		boolean end= false;
		view.stampaRichiestaSingola(View.DATA_NASCITA);
		while(!end){
			view.stampaRichiestaSingola(View.YEAR);
			year= LibraryGeneral.readInt();
			if(String.valueOf(year).length()==4)
			{
				view.stampaRichiestaSingola(View.MONTH);
				month= LibraryGeneral.readInt();
				view.stampaRichiestaSingola(View.DAY);
				day=LibraryGeneral.readInt();
				if((String.valueOf(month).length()<=2 && month <= 12 ) && (( String.valueOf(day).length() <= 2) && day <= 31)) {
					birthDate= LocalDate.of(year,month,day);
					end=true;
				}
			}
			else {
				System.out.println(View.MG_ERRORE);
			}
		}
		return birthDate;
	}

	/**
	 * Metooo di controllo del Login da parte dell'User. L'autenticazione va a buon fine se l'username è presente all'interno
	 * del Database e se la password inserita corrisponde.
	 */
	public static String checkLogin(){
		username=LibraryGeneral.insertString(View.USER_NAME);
		System.out.println(username);
		String password = LibraryGeneral.insertString(View.PASSWORD);
		System.out.println(password);
		if(Database.checkLoginIfTrue(username, password)) System.out.println(View.AUTENTICAZIONE_SUCCESSO);
		else {
			username = "_error_";
		}
		return username;
	}

	/**
	 * Metodo che permette il rinnovo dell'iscrizione dell'user se scaduta (o in procinto di scadere).
	 * Se si decide di rinnovare l'iscrizione allora viene incrementata l'etichetta del numero del rinnovo (utile per le infomarzioni storiche)
	 * @param user {@link User}
	 */
	public static void renewalRegistration(User user){
		temp=0;
        if (isRenewal(user)) {
			view.stampaRichiestaSingola(View.MG_SCADUTA_ISCRIZIONE);
			view.stampaRichiestaSingola(View.RINNOVO);
			choice = LibraryGeneral.readInt();
			if (choice == 0) {
				user.setRegistrationDate(LocalDate.now());
				temp= user.getNumRenewal()+1;
				user.setNumRenewal(temp);
			}
		}
	}

	/**
	 * Metodo che controlla che l'iscrizione dell'user sia scaduta o nel range impostato per il rinnovo anticipato.
	 * @return true se l'iscrizione dell'user è scaduta, quindi può essere rinnovata.
	 * @return false se l'iscrizione dell'user non è scaduta e non è nel range dei giorni di rinnovo.
	 */
    public static boolean isRenewal(User user) {
		return user.getRegistrationDate().plusYears(rangeYear).isBefore(LocalDate.now()) && user.getRegistrationDate().plusDays(rangeDay).isBefore(LocalDate.now());
	}

    /**
     * Metodo che controlla che l'iscrizione dell'user sia scaduta.
     *
     * @return false se l'iscrizione dell'user non è scaduta.
     */
    public static boolean isExpired(User user) {
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
    public static void userExpired(User user) {
        if (isExpired(user)) {
            View.stampaRichiestaSingola(View.SCADUTA_NON_RINNOVABILE);
            user.setName("_expired_");
        }
    }

	/**
	 * Metodo che controlla se l'user ha piu\' di 3 prestiti previsti per una tipologia, o categoria, di risorsa.
	 * @param user {@link User}
	 * @return true se non puo\' piu\' prendere in prestito altre risorse, false se invece ha ancora disponibili dei prestiti da poter fare.
	 */
	public static boolean checkBorrowed(User user, int number){
		Integer [] value= user.getBorrowed();
		return value[number] >= 3;
	}

	/**
	 * Metodo che raccoglie tutti i metodi per richiedere e verificare la creazione di un prestito.
	 * {@link LibraryResources#createPrestito(String, String, int,int)}
	 * Utilizzato all'interno di {@link #actionUser(String)}
	 */
	public static void createRequestPrestito(int barcode, String username){
		/**
		 * controllo il tipo della risorsa cercata.
		 * controllo che l'utente possa effettivamente prendere in prestito una risorsa.
		 */
		int bookOrFilm=0;
		bookOrFilm = Database.choiceTypeResource(barcode);
		if (!checkBorrowed(Database.getUser(username), bookOrFilm)){
			/**
			 * genero il codice del prestito.
			 */
			System.out.println(Database.getResource(barcode).toString());
			String codePrestito = LibraryResources.generateId(username,barcode);
			/**
			 * creo il prestito e lo salvo nel database, aumentando di uno il numero di licenze dell'utente.
			 * Avendo già identivicato prima il tipo di risorsa ne modificando quindi il numero associato.
			 */
			LibraryResources.createPrestito(codePrestito, username, barcode, bookOrFilm);
		} else System.out.println(View.FINITE_LICENZE_PRESTITO_USER);
	}


	/**
	 * METODI AZIONI E RICERCA PER ADMIN E USER
	 */

	/**
	 * Metodo che permette di gestire le azioni che possono essere svolte dall'User.
	 */
	public static void actionUser(String username){
		boolean end= false;
		do{
			view.stampaRichiestaSingola(View.MG_ANCORA + View.PREMI_INDIETRO);
			view.stampaMenuSpecifico(View.RICHIESTE_MENU_USER);
			choice = LibraryGeneral.readInt();
			switch(choice){

				/**
				 * Richiedere un prestito.
				 * {@link #createRequestPrestito(int, String)}
				 */
				case 1:
					barcode= LibraryGeneral.insertBarcode(vincolo);
					if(Database.checkIfResource(barcode)){
						createRequestPrestito(barcode,username);
					} else  System.out.println(View.NON_ESISTE_RISORSA);

					break;

				/**
				 *  Stampare i prestiti in atto, quindi attivi, dall'utente.
				 *  {@link Database#printActivePrestitiUser(String)}
				 */
				case 2:
					Database.printActivePrestitiUser(username);
					break;

				/**
				 * Richiedere una proroga.
				 * {@link LibraryResources#checkInsertId(String)} and {@link LibraryResources#prorogaPrestito(String)}
				 */
				case 3:
					/**
					 * se l'utente non ha nessun prestito attivo allora non puo\' effettuare la ricerca.
					 */
					if(Database.userHavePrestito(username)){
						id=LibraryResources.checkInsertId(username);
						LibraryResources.prorogaPrestito(id);
					}else System.out.println(View.USER_NON_HA_PRESTITI);
					break;

				/**
				 * Svolgere una ricerca. Uguale a quello dell'admin.
				 * {@link #researchResource()}
				 */
				case 4:
					researchResource();
					break;

				/**
				 * Terminare il prestito di una risorsa.
				 * Tramite il metodo {@link Database#removePrestito(int, String, String)} modifichiamo la variabile on_off impostandola a false, ovvero prestito inattivo.
				 */
				case 5:
					/**
					 * se l'utente non ha nessun prestito attivo allora non puo\' effettuare la ricerca.
					 */
					if(Database.userHavePrestito(username)){
						id = LibraryResources.checkInsertId(username);
						int temp = Database.getPrestito(id).getBarcode();
						Database.removePrestito(temp, username, id);
						System.out.println(MG_AZIONE_SUCCESSO);
					}else System.out.println(View.USER_NON_HA_PRESTITI);

					break;

				/**
				 * Fine della stampa delle richieste del Menu.
				 */
				case 0:
					end = true;
					break;

				/**
				 * Errore, l'inserimento non è corretto
				 */
				default:
					System.out.println(View.MG_ERRORE);
					break;
			}
		}while(!end);
	}


	/**
	 * Metodo che permette di gestire le azioni che possono essere svolte dall'Admin.
	 */
	public static void actionAdmin(){
		boolean end= false;
		do{
			view.stampaRichiestaSingola(View.MG_ANCORA + View.PREMI_INDIETRO);
			view.stampaMenuSpecifico(View.RICHIESTE_MENU_ADMIN);
			choice = LibraryGeneral.readInt();
			switch(choice)
			{
				/**
				 * Visualizzazione dell'elenco delle risorse.
				 * Prima i libri, oggetti di tipo BOOK, e successivamente dei film, oggetti di tipo FILM.
				 * {@link Database#printSpecificResource(String)}
				 */
				case 1:
					Database.printSpecificResource(View.BOOK);
					Database.printSpecificResource(View.FILM);
					break;
				/**
				 * Aggiungere una risorsa all'elenco.
				 * Se gia\' presente incrementa il numero di licenze disponibili.
				 * {@link LibraryResources#createResourceProcess()}
				 */
				case 2:
					LibraryResources.createResourceProcess();
					break;
				/**
				 * Rimozione fittizia di una risorsa dall'archivio.
				 * {@link Database#removeResource(int)}
				 */
				case 3:
					view.stampaRichiestaSingola(View.BARCODE);
					int number = LibraryGeneral.readInt();
					Database.removeResource(number);
					break;

				/**
				 * Ricerca di una risorsa nel Database in base a valori diversi.
				 * {@link #researchResource()}
				 */
				case 4:
					researchResource();
					break;

				/**
				 * Visualizzare il numero di prestiti per anno solare.
				 * {@link Database#numberOfPrestitiCalendarYear()}
				 */
				case 5:
					Database.numberOfPrestitiCalendarYear();
					break;

				/**
				 * Visualizzare le proroghe per anno solare.
				 * {@link Database#prorogaCalendaryear()}
				 */
				case 6:
					Database.prorogaCalendarYear();
					break;

				/**
				 * Visualizzare quale risorsa e\' stata oggetto del maggior numero di prestiti per anno solare.
				 * {@link Database#resourceCalendarYear()}
				 */
				case 7:
					Database.resourceCalendarYear();
					break;

				/**
				 * Visualizzare il numero di prestiti per fruitore per anno solare.
				 * {@link Database#numberOfPrestitiUserCalendarYear()}
				 */
				case 8:
					Database.numberOfPrestitiUserCalendarYear();
					break;

				/**
				 * Visualizzare l'elenco degli utenti.
				 */
				case 9:
					Database.listUsers();
					break;

				/**
				 * Fine della stampa delle richieste del Menu.
				 */
				case 0:
					end = true;
					break;

				/**
				 * Errore, l'inserimento non è corretto
				 */
				default:
					System.out.println(View.MG_ERRORE);
					break;
			}
		}while(!end);
	}

	/**
	 * Metodo per ricercare, in base parametri divesi, una risorsa all'interno del Database.
	 * Utilizzabile sia da User che da Admin.
	 */
	public static void researchResource(String searchString, int choice){
		boolean end= false;
		do{
			if (choice == 0) end = true;
			switch(choice)
			{
				/**
				 * Ricerca per Titolo opera.
				 */
				case 1:
					searchString = LibraryGeneral.insertString(View.RICHIESTE_MENU_RICERCA[0]);
					Database.searchGeneral("title", searchString);

					break;
				/**
				 * Ricerca per Autore.
				 */
				case 2:
					searchString = LibraryGeneral.insertString(View.RICHIESTE_MENU_RICERCA[1]);
					Database.searchGeneral("author", searchString);

					break;
				/**
				 * Ricerca per Genere.
				 */
				case 3:
					searchString = LibraryGeneral.insertString(View.RICHIESTE_MENU_RICERCA[2]);
					Database.searchGeneral("genre", searchString);

					break;

				/**
				 * Ricerca per Anno di Pubblicazione.
				 */
				case 4:
					searchString = LibraryGeneral.insertString(View.RICHIESTE_MENU_RICERCA[3]);
					Database.searchGeneral("year", searchString);
					break;

				/**
				 * Fine della stampa delle richieste del Menu.
				 */
				case 0:
					end = true ;
					break;

				/**
				 * Errore, l'inserimento non è corretto
				 */
				default:
					System.out.println(View.MG_ERRORE);
					break;
			}
		}while(!end);
	}

}
