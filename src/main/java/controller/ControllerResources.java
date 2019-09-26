package main.java.controller;
import main.java.model.*;
import main.java.model.library.LibraryResources;
import main.java.view.*;

import java.time.LocalDate;
import java.util.List;

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
    public static void createResourceProcess(){
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

    /**
     * Gestione della ricerca di risorse dati in ingresso dei parametri.
     */
    public static void researchResource(){
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


    /**
     * Metodo di controllo dell'inserimento del codice prestito da parte dell'utente.
     * Si controlla, per avere una sicurezza che l'utente stia accedento solo ed esclusivamente ai prestiti associati al suo account,
     * che il nome utente (username) compaia all'interno del codice prestito inserito.
     */
    public static String checkInsertId(String username){
        boolean end = false;
        while(!end){
            View.stampaRichiestaSingola(Constant.CODICE_PRESTITO);
            try {
                string= ViewLibraryGeneral.readString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(Database.checkIfPrestito(string) && string.contains(username)){
                end=true;
            } else System.out.println(Constant.NON_ESISTE_PRESTITO);
        }
        return string;
    }


    public static void controllerProrogaPrestito(String codePrestito){
        /**
         * se vero allora procedi, altrimenti stampa errore.
         * controllo che il codice prestito esista, sia attivo e che sia possibile effettuare la proroga.
         */
        if(LibraryResources.checkProrogaPrestito(codePrestito)){
            LibraryResources.prorogaPrestito(codePrestito);
            View.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
        } else View.stampaRichiestaSingola(Constant.NO_PROROGA);
    }

    /**
     * Metodo per la creazione di una risorsa, oggetto di tipo Resource, generica utilizzando altri metodi di inserimento e controllo.
     * @param barcode codice identificativo della risorsa.
     * @param type categoria o tipo della risorsa.
     * @return oggetto di tipo Resource.
     */
    public static Resource createResource(int barcode, String type){
        String title = ViewLibraryGeneral.insertString(Constant.TITLE);
        List ll = ViewLibraryGeneral.insertList(Constant.AUTORI);
        List lingue = ViewLibraryGeneral.insertList(Constant.LINGUE);
        Resource res = new Resource(barcode, type, title, ll, lingue, ViewLibraryGeneral.insertNumberEqual(Constant.YEAR, 4), ViewLibraryGeneral.insertString(Constant.GENERE), licenseList);
        return res;
    }

    /**
     * Metodo per stampare a video le risorse del database
     */
    public static void controllerPrintSpecificResource(String type){
        View.viewPrintSpecificResource(type);
        Database.printSpecificResource(type);
    }

    /**
     * Metodo che permette di "rimuovere", in maniera fittizia, una risorsa dall'elenco di risorse.
     * Se la risorsa ha piu\' di una copia bisogna decrementarla di una, ovviamente il numero in posizione 0.
     * Viene tenuta traccia dello storico della risorsa, infatti anche se le copie sono esaurite rimangono all'interno dell'archivio.
     */
    public static void removeResource(int barcode) {
        if(Database.checkIfResource(barcode)) {
            Integer[] copie = Database.getResource(barcode).getLicense();
            /**
             * Se il numero di copie e quello delle copie in prestito e\' uguale allora le copie
             * sono tutte in prestito, quindi non e\' possibile effettuare la rimozione fittizia.
             */
            if(copie[0]!=copie[1]){
                /**
                 * Se c'e\' una o piu\' di una copia della risorsa la decrementa e controlla che sia a zero.
                 * Altrimenti e\' nulla percio\' compare un messaggio di avviso.
                 */
                if(copie[0] >= 1){
                    Database.decrementCopyOrLicenze(barcode,0 );
                    System.out.println(Constant.MG_AZIONE_SUCCESSO);
                    if (copie[0] == 0) System.out.println(Constant.RISORSA_SCADUTA);
                }else{
                    //ovvero uguale a zero (perche\' <1)
                    System.out.println(Constant.RISORSA_SCADUTA);
                }
            } else System.out.println(Constant.RISORSA_IMPOSSIBILE_RIMUOVERE);
        } else View.stampaRichiestaSingola(Constant.NON_ESISTE_RISORSA);
    }

}
