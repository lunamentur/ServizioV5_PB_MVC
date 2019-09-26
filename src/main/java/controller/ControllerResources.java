package main.java.controller;
import main.java.model.*;
import main.java.model.library.LibraryResources;
import main.java.view.*;

import java.time.LocalDate;

public class ControllerResources {

    //////////////////////////////////////////////////////////////////////////////////
    private static Prestito prestito;
    private static Resource res;
    private static Book book;
    private static Film film;
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
    //////////////////////////////////////////////////////////////////////////////////

    /**
     * Metodo per creare una risorsa e salvarla nel Database.
     * Se la risorsa &egrave; gia\' presente nel database allora si incrementa il numero di copie disponibili al prestito.
     * Altrimenti si continua con la creazione, chiedendo l'inserimento di ogni parametro.
     * Si fa la sistinzione tra oggetto di tipo Book e di tipo Film.
     */
    public void createResourceProcess(){
        View.stampaRichiestaSingola(Constant.CATEGORIA);
        int choice = ViewLibraryGeneral.readIntChoise(1,2);
        //vero se e\' un libro, oppure un film
        if (choice == 1) string = Constant.BOOK;
        else string = Constant.FILM;
        barcode= ViewLibraryGeneral.insertBarcode(vincolo);
        if(!LibraryResources.checkIfExist(barcode)){
            Resource res= createResource(barcode, string);
            switch(choice)
            {
                /**
                 * Creazione di una risorsa di tipo Book.
                 */
                case 1:
                    Book book = new Book(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, ViewLibraryGeneral.insertNum(Constant.NUM_PAG), ViewLibraryGeneral.insertString(Constant.CASA_EDIT));
                    Database.insertResource(book);
                    View.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
                    break;
                /**
                 * Creazione di una risorsa di tipo Film.
                 */
                case 2:
                    Film film = new Film(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, ViewLibraryGeneral.insertNum(Constant.AGE_RESTRIC), ViewLibraryGeneral.insertNum(Constant.DURATA));
                    Database.insertResource(film);
                    View.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
                    break;

                /**
                 * Errore, l'inserimento non &egrave; corretto
                 */
                default:
                    View.viewMsgError();
                    break;
            }
        } else View.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);

    }

    /**
     * Metodo che assembla i metodi per la registrazione del prestito con i relativi controlli e lo salva all'interno del Database.
     * Se la risorsa non &egrave; disponibile al prestito poiche\' non ha piu\' copie disponibili allora la registrazione del prestito fallisce.
     * @param barcode {@link #barcode}
     * @param codePrestito {@link Prestito}
     */
    public void createPrestito(String codePrestito, String username, int barcode, int bookOrFilm){
        /**
         * controllo se esiste la risorsa e se &egrave; disponibile al prestito.
         */
        if(Database.checkIfResource(barcode)){
            if(LibraryResources.checkIfResourceFree(barcode)){
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
                View.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
            } else View.stampaRichiestaSingola(Constant.MG_PRESTITO_NON_DISPONIBILE);
        } else View.stampaRichiestaSingola(Constant.NON_ESISTE_RISORSA);
    }

    public void researchResource(){
        boolean end= false;
        boolean exist= true;
        do{
            View.viewResearchResource();
            int choice = ViewLibraryGeneral.readInt();
            String searchString = ViewLibraryGeneral.insertString("Inserisci ");
            if (choice == 0) end = true;
            switch(choice)
            {
                /**
                 * Ricerca per Titolo opera.
                 */
                case 1:
                    exist= Database.searchGeneral("title", searchString);

                    break;
                /**
                 * Ricerca per Autore.
                 */
                case 2:
                    exist=Database.searchGeneral("author", searchString);

                    break;
                /**
                 * Ricerca per Genere.
                 */
                case 3:
                    exist=  Database.searchGeneral("genre", searchString);

                    break;

                /**
                 * Ricerca per Anno di Pubblicazione.
                 */
                case 4:
                    exist= Database.searchGeneral("year", searchString);
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
            /**
             * se nessuna risorsa viene trovata allora compare il messaggio di avviso.
             */
            if (!exist) //se true diventa false, ovvero se sono uguali significa che non c'e risorsa
                View.stampaRichiestaSingola(View.NON_ESISTE_RISORSA);
        }while(!end);
    }

}
