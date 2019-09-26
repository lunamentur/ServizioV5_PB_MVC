package main.java.model.library;

import main.java.model.*;
import main.java.view.ViewLibraryGeneral;


import java.time.LocalDate;
import java.util.List;
import java.util.Random;


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
	 * Controlla che sia valida la proroga e il prestito contemporaneamente.
	 * @param codePrestito
	 * @return true se va bene
	 */
	public static boolean checkProrogaPrestito(String codePrestito){
		return (checkIfProroga(codePrestito) && Database.checkIfPrestito(codePrestito));
	}
	/**
	 * Metodo che permette la proroga di un prestito, controllando se presente nell'archivio e successivamente
	 * se ha i requisiti per essere prorogato. La proroga consta di 30 giorni in piu\' alla data di scadenza prevista.
	 * {@link #checkIfProroga(String)} and {@link Database#checkIfPrestito(String)} and {@link #incrementNumProroga(String)}
	 */
	public static void prorogaPrestito(String codePrestito){
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



}
