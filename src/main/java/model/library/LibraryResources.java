package main.java.model.library;

import main.java.model.*;


import java.time.LocalDate;
import java.util.Random;


/**
 * Classe intermedia che permette l'interazione tra la classe Resource e la classe db.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class LibraryResources {
    public Database db;
    public LocalDate dataInizio, dataScadenza;
    private int giorniIntervalloProroga = 3;
    /**
     * la durata massima, in numero di giorni, del prestito di una qualsiasi risorsa di tipo Book.
     */
    private long dayMaxBook = 30;


    /**
     * Creazione di variabili e oggetti utili per i metodi di controllo.
     */
    private String string;

    /**
     * variabili che corrispondono agli indici della lista di licenze.
     * {@link Resource}
     */
    private int copieRisorsa = 0;
    private int copieinPrestito = 1;

    public LibraryResources(Database db) {
        this.db = db;
    }
    /**
     * METODI CONTROLLO RESOURCE
     */


    /**
     * Metodo che permette di verificare se la risorsa &egrave; presente all'interno del database tramite
     * un metodo di quella classe.
     * {@link #barcode}
     *
     * @return true se la risorsa gia\' &egrave; presente nel Database, altrimenti false.
     */
    public boolean checkIfExist(int barcode) {
        if (db.checkIfResource(barcode)) {
            /**
             * Incremenenta il numero di copie disponibili se gia\' presente una risorsa uguale.
             * Agendo sulle copie, vogliamo lavorare sul valore in posizione 0 dell'array.
             */
            db.incrementCopyOrLicenze(barcode, copieRisorsa);
            return true;
        } else return false;
    }


    /**
     * METODI PRESTITO
     */

    /**
     * Metodo che controlla se un prestito ha i requisiti per effettuare la proroga,
     * ovvero se la data in cui viene interpellato il sistema &egrave; in un intervallo di 3 giorni prima della data di Scadenza.
     *
     * @return true se puo\' essere effettuata.
     */
    public boolean checkIfProroga(String codePrestito) {
        dataInizio = LocalDate.now();
        dataScadenza = db.getPrestito(codePrestito).getDataScadenza();
        /**
         * sottraggo i 3 giorni per ottenere il primo giorno in cui e\' possibile richiedere la proroga.
         */
        LocalDate intervallo = dataScadenza.minusDays(giorniIntervalloProroga);
        return !dataInizio.isAfter(dataScadenza) && !dataInizio.isBefore(intervallo);
    }

    /**
     * Controlla che sia valida la proroga e il prestito contemporaneamente.
     *
     * @param codePrestito
     * @return true se va bene
     */
    public boolean checkProrogaPrestito(String codePrestito) {
        return (checkIfProroga(codePrestito) && db.checkIfPrestito(codePrestito));
    }

    /**
     * Metodo che permette la proroga di un prestito, controllando se presente nell'archivio e successivamente
     * se ha i requisiti per essere prorogato. La proroga consta di 30 giorni in piu\' alla data di scadenza prevista.
     * {@link #checkIfProroga(String)} and {@link Database#checkIfPrestito(String)} and {@link #incrementNumProroga(String)}
     */
    public void prorogaPrestito(String codePrestito) {
        dataInizio = LocalDate.now();
        dataScadenza = dataInizio.plusDays(dayMaxBook);
        /**
         * modifico l'oggetto prestito di tipo Prestito con le nuove date nell'archivio.
         */
        db.getPrestito(codePrestito).setDataScadenza(dataScadenza);
        db.getPrestito(codePrestito).setDataInizio(dataInizio);
        /**
         * Incremento il numero di progoghe richieste dall'user per quella risorsa.
         */
        incrementNumProroga(codePrestito);
    }

    /**
     * Metodo che permette di aumentare il numero di proroghe fatte dall'user su una spesicifica risorsa.
     */
    public void incrementNumProroga(String codePrestito) {
        int i = db.getPrestito(codePrestito).getNumProroghe() + 1;
        db.getPrestito(codePrestito).setNumProroghe(i);
    }

    /**
     * Metodo genera il codice del prestito richiesto dall'utente di una risorsa.
     *
     * @param barcode {@link #barcode}
     * @return codice prestito
     */
    public String generateId(String username, int barcode) {
        Random random = new Random();
        String number = String.format("%04d", random.nextInt(10000));
        string = username + barcode + number;
        return string;
    }

    /**
     * Metodo che controlla se la risorsa &egrave libera.
     * @param barcode
     * @return true or false
     */
    public boolean checkIfResourceFree(int barcode) {
        Integer[] copie = db.getResource(barcode).getLicense();
        /**
         * {@link #licenseList}
         * Indice in posizione 0 indica il numero di copie totali
         * Indice in posizione 1 indica il numero di copie in prestito
         */
        return copie[copieinPrestito] < copie[copieRisorsa];
    }
}
