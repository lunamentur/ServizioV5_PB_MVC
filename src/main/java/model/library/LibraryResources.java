package main.java.model.library;

import database.Database;
import prestito.Prestito;
import resource.*;
import view.View;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static view.View.*;

/**
 * Classe intermedia che permette l'interazione tra la classe Resource e la classe Database.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class LibraryResources {

	private static Prestito prestito;
	private static Resource res;
	private  static Book book;
	private  static Film film;
	public static LocalDate dataInizio, dataScadenza;
	private static int giorniIntervalloProroga=3;
	/**
	 * la durata massima, in numero di giorni, del prestito di una qualsiasi risorsa di tipo Book.
	 */
	private static long dayMaxBook = 30;


	/**
	 * Creazione di variabili e oggetti utili per i metodi di controllo.
	 */
	private static String string;
	private static int choice, year, barcode;
	private static int vincolo=3;
	private static Integer [] licenseList= {0,0};//licenze con due componenti.

	/**
	 * variabili che corrispondono agli indici della lista di licenze.
	 * {@link Resource}
	 */
	private static int copieRisorsa = 0;
	private static int copieinPrestito = 1;

	/**
	 * METODI CONTROLLO RESOURCE
	 */

	/**
	 * Metodo per creare una risorsa e salvarla nel Database.
	 * Se la risorsa &egrave; gia\' presente nel database allora si incrementa il numero di copie disponibili al prestito.
	 * Altrimenti si continua con la creazione, chiedendo l'inserimento di ogni parametro.
	 * Si fa la sistinzione tra oggetto di tipo Book e di tipo Film.
	 */
	public static void createResourceProcess(){
		stampaRichiestaSingola(CATEGORIA);
		choice = LibraryGeneral.readIntChoise(1,2);
		//vero se e\' un libro, oppure un film
		if (choice == 1) string = BOOK;
		else string = FILM;
		barcode=LibraryGeneral.insertBarcode(vincolo);
		if(!checkIfExist(barcode)){
			res= createResource(barcode, string);
			switch(choice)
			{
				/**
				 * Creazione di una risorsa di tipo Book.
				 */
				case 1:
					book = new Book(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, LibraryGeneral.insertNum(NUM_PAG), LibraryGeneral.insertString(CASA_EDIT));
					Database.insertResource(book);
					stampaRichiestaSingola(MG_AZIONE_SUCCESSO);
					break;
				/**
				 * Creazione di una risorsa di tipo Film.
				 */
				case 2:
					film = new Film(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, LibraryGeneral.insertNum(AGE_RESTRIC), LibraryGeneral.insertNum(DURATA));
					Database.insertResource(film);
					stampaRichiestaSingola(MG_AZIONE_SUCCESSO);
					break;

				/**
				 * Errore, l'inserimento non &egrave; corretto
				 */
				default:
					System.out.println(MG_ERRORE);
					break;
			}
		} else stampaRichiestaSingola(MG_AZIONE_SUCCESSO);
	}

	/**
	 * Metodo per la creazione di una risorsa, oggetto di tipo Resource, generica utilizzando altri metodi di inserimento e controllo.
	 * @param barcode codice identificativo della risorsa.
	 * @param type categoria o tipo della risorsa.
	 * @return oggetto di tipo Resource.
	 */
	public static Resource createResource(int barcode, String type){
		String title = LibraryGeneral.insertString(TITLE);
		List ll = LibraryGeneral.insertList(AUTORI);
		List lingue = LibraryGeneral.insertList(LINGUE);
		Resource res = new Resource(barcode, type, title, ll, lingue, LibraryGeneral.insertNumberEqual(YEAR, 4), LibraryGeneral.insertString(GENERE), licenseList);
		return res;
	}

	/**
	 * Metodo che permette di verificare se la risorsa &egrave; presente all'interno del database tramite
	 * un metodo di quella classe.
	 * {@link #barcode}
	 * @return true se la risorsa gia\' &egrave; presente nel Database, altrimenti false.
	 */
	public static boolean checkIfExist(int barcode){
		if (Database.checkIfResource(barcode)) {
			/**
			 * Incremenenta il numero di copie disponibili se gia\' presente una risorsa uguale.
			 * Agendo sulle copie, vogliamo lavorare sul valore in posizione 0 dell'array.
			 */
			Database.incrementCopyOrLicenze(barcode, copieRisorsa);
			return true;
		} else return false;
	}


	/**
	 * METODI PRESTITO
	 */

	/**
	 * Metodo che controlla se un prestito ha i requisiti per effettuare la proroga,
	 * ovvero se la data in cui viene interpellato il sistema &egrave; in un intervallo di 3 giorni prima della data di Scadenza.
	 * @return false se non puo\' essere effettuata.
	 * @return true se puo\' essere effettuata.
	 */
	public static boolean checkIfProroga(String codePrestito){
		dataInizio= LocalDate.now();
		dataScadenza= Database.getPrestito(codePrestito).getDataScadenza();
		/**
		 * sottraggo i 3 giorni per ottenere il primo giorno in cui e\' possibile richiedere la proroga.
		 */
		LocalDate intervallo= dataScadenza.minusDays(giorniIntervalloProroga);
		return !dataInizio.isAfter(dataScadenza) && !dataInizio.isBefore(intervallo);
	}

	/**
	 * Metodo che permette la proroga di un prestito, controllando se presente nell'archivio e successivamente
	 * se ha i requisiti per essere prorogato. La proroga consta di 30 giorni in piu\' alla data di scadenza prevista.
	 * {@link #checkIfProroga(String)} and {@link Database#checkIfPrestito(String)} and {@link #incrementNumProroga(String)}
	 */
	public static void prorogaPrestito(String codePrestito){
		/**
		 * se vero allora procedi, altrimenti stampa errore.
		 * controllo che il codice prestito esista, sia attivo e che sia possibile effettuare la proroga.
		 */
		if(checkIfProroga(codePrestito) && Database.checkIfPrestito(codePrestito)){
			dataInizio= LocalDate.now();
			dataScadenza= dataInizio.plusDays(dayMaxBook);
			/**
			 * modifico l'oggetto prestito di tipo Prestito con le nuove date nell'archivio.
			 */
			Database.getPrestito(codePrestito).setDataScadenza(dataScadenza);
			Database.getPrestito(codePrestito).setDataInizio(dataInizio);
			/**
			 * Incremento il numero di progoghe richieste dall'user per quella risorsa.
			 */
			incrementNumProroga(codePrestito);
			stampaRichiestaSingola(MG_AZIONE_SUCCESSO);
		} else stampaRichiestaSingola(NO_PROROGA);
	}

	/**
	 * Metodo che permette di aumentare il numero di proroghe fatte dall'user su una spesicifica risorsa.
	 */
	public static void incrementNumProroga(String codePrestito){
		int i= Database.getPrestito(codePrestito).getNumProroghe() +1;
		Database.getPrestito(codePrestito).setNumProroghe(i);
	}

	/**
	 * Metodo che permette di controllare se una risorsa, oggetto di tipo Resource, &egrave; disponibile al prestito.
	 * @param barcode {@link #barcode}
	 * @return true se il numero delle licenze del prestito &egrave; minore al numero delle copie esistenti.
	 * @return false se il numero delle licenze del prestito &egrave; maggiore o uguale del numero delle copie esistenti, quindi non ci sono piu\' copie disponibili.
	 */
	public static boolean checkIfResourceFree(int barcode){
		Integer[] copie= Database.getResource(barcode).getLicense().clone();
		/**
		 * {@link #licenseList}
		 * Indice in posizione 0 indica il numero di copie totali
		 * Indice in posizione 1 indica il numero di copie in prestito
		 */
		return copie[copieinPrestito] < copie[copieRisorsa];
	}

	/**
	 * Metodo che assembla i metodi per la registrazione del prestito con i relativi controlli e lo salva all'interno del Database.
	 * Se la risorsa non &egrave; disponibile al prestito poiche\' non ha piu\' copie disponibili allora la registrazione del prestito fallisce.
	 * @param barcode {@link #barcode}
	 * @param username {@link operator.User}
	 * @param codePrestito {@link Prestito}
	 */
	public static void createPrestito(String codePrestito, String username, int barcode, int bookOrFilm){
		/**
		 * controllo se esiste la risorsa e se &egrave; disponibile al prestito.
		 */
		if(Database.checkIfResource(barcode)){
			if(checkIfResourceFree(barcode)){
				/**
				 * Aumento di uno le copie in prestito, ovvero le licenze, in posizione 1 nell'array.
				 */
				Database.incrementCopyOrLicenze(barcode, copieinPrestito);
				/**
				 * definisco gli attributi per creare un oggetto di tipo Prestito.
				 */
				dataInizio = LocalDate.now();
				dataScadenza = dataInizio.plusDays(dayMaxBook);
				prestito = new Prestito(codePrestito, username, barcode, dataInizio, dataScadenza);
				/**
				 * inserisco l'oggetto di tipo Prestito all'interno dell'archivio dei prestiti nel Database.
				 */
				Database.insertPrestito(prestito);
				Database.incrementLicenzeUser(username, bookOrFilm);
				View.stampaRichiestaSingola(MG_AZIONE_SUCCESSO);
			} else View.stampaRichiestaSingola(MG_PRESTITO_NON_DISPONIBILE);
		} else View.stampaRichiestaSingola(NON_ESISTE_RISORSA);
	}

	/**
	 * Metodo genera il codice del prestito richiesto dall'utente di una risorsa.
	 * @param barcode {@link #barcode}
	 * @return codice prestito
	 */
	public static String generateId(String username, int barcode){
		Random random = new Random();
		String number = String.format("%04d", random.nextInt(10000));
		string= username+barcode+number;
		return string;
	}

	/**
	 * Metodo che controlla se il codice prestito e\' unico, ovvero non sia gia\' presente nel database.
	 * @param barcode {@link #barcode}
	 * @return codice prestito
	 */
	public static String checkGenerateId(String username, int barcode){
		do{
			string= generateId(username,barcode);
		}while(Database.checkIfPrestito(string));
		return string;
	}

	/**
	 * Metodo di controllo dell'inserimento del codice prestito da parte dell'utente.
	 * Si controlla, per avere una sicurezza che l'utente stia accedento solo ed esclusivamente ai prestiti associati al suo account,
	 * che il nome utente (username) compaia all'interno del codice prestito inserito.
	 * @return codice prestito {@link #generateId(String, int)}
	 */
	public static String checkInsertId(String username){
		boolean end = false;
		while(!end){
			stampaRichiestaSingola(CODICE_PRESTITO);
			try {
				string= LibraryGeneral.readString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(Database.checkIfPrestito(string) && string.contains(username)){
				end=true;
			} else System.out.println(NON_ESISTE_PRESTITO);
		}
		return string;
	}


}
