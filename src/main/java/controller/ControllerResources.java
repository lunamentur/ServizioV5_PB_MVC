package main.java.controller;
import main.java.model.*;
import main.java.model.library.LibraryControllerInterface;
import main.java.model.library.LibraryResources;
import main.java.view.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * Classe Controller che si occupa della gestione dei metodi del model riguardanti le Risorse e prestiti.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class ControllerResources {

    private  View view =new View();
    private  Prestito prestito;
    public  LocalDate dataInizio, dataScadenza;
    /**
     * la durata massima, in numero di giorni, del prestito di una qualsiasi risorsa di tipo Book.
     */
    private  long dayMaxBook = 30;

    /**
     * Creazione di variabili e oggetti utili per i metodi di controllo.
     */
    private  String string;
    private Database db;
    private LibraryControllerInterface lr;
    private  int barcode;
    private  int vincolo=3;
    private  Integer [] licenseList= {0,0};//licenze con due componenti.

    /**
     * variabili che corrispondono agli indici della lista di licenze.
     * {@link Resource}
     */
    private  int copieinPrestito = 1;

    /**
     * Costruttore
     * @param db database
     */
    public ControllerResources(Database db){
        this.db = db;
        lr = new LibraryResources(db);
    }


    /**
     * Metodo per creare una risorsa e salvarla nel db.
     * Se la risorsa &egrave; gia\' presente nel database allora si incrementa il numero di copie disponibili al prestito.
     * Altrimenti si continua con la creazione, chiedendo l'inserimento di ogni parametro.
     * Si fa la sistinzione tra oggetto di tipo Book e di tipo Film.
     */
    public  void createResourceProcess(){
        View.stampaRichiestaSingola(View.CATEGORIA);
        int choice = ViewLibraryGeneral.readIntChoise(1,2);
        //vero se e\' un libro, oppure un film
        if (choice == 1) string = View.BOOK;
        else string = View.FILM;
        barcode= ViewLibraryGeneral.insertBarcode(vincolo);
        if(!lr.checkIfExist(barcode)){
            Resource res= createResource(barcode, string);
            switch(choice)
            {
                /**
                 * Creazione di una risorsa di tipo Book.
                 */
                case 1:
                    Book book = new Book(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, ViewLibraryGeneral.insertNum(View.NUM_PAG), ViewLibraryGeneral.insertString(View.CASA_EDIT));
                    db.insertResource(book);
                    View.stampaRichiestaSingola(View.MG_AZIONE_SUCCESSO);
                    break;
                /**
                 * Creazione di una risorsa di tipo Film.
                 */
                case 2:
                    Film film = new Film(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, ViewLibraryGeneral.insertNum(View.AGE_RESTRIC), ViewLibraryGeneral.insertNum(View.DURATA));
                    db.insertResource(film);
                    View.stampaRichiestaSingola(View.MG_AZIONE_SUCCESSO);
                    break;

                /**
                 * Errore, l'inserimento non &egrave; corretto
                 */
                default:
                    View.viewMsgError();
                    break;
            }
        } else View.stampaRichiestaSingola(View.MG_AZIONE_SUCCESSO);

    }

    /**
     * Metodo che assembla i metodi per la registrazione del prestito con i relativi controlli e lo salva all'interno del db.
     * Se la risorsa non &egrave; disponibile al prestito poiche\' non ha piu\' copie disponibili allora la registrazione del prestito fallisce.
     * @param barcode {@link #barcode}
     * @param codePrestito {@link Prestito}
     */
    public void createPrestito(String codePrestito, String username, int barcode, int bookOrFilm){
        /**
         * controllo se esiste la risorsa e se &egrave; disponibile al prestito.
         */
        if(db.checkIfResource(barcode)){
            if(lr.checkIfResourceFree(barcode)){
                /**
                 * Aumento di uno le copie in prestito, ovvero le licenze, in posizione 1 nell'array.
                 */
                db.incrementCopyOrLicenze(barcode, copieinPrestito);
                /**
                 * definisco gli attributi per creare un oggetto di tipo Prestito.
                 */
                dataInizio = LocalDate.now();
                dataScadenza = dataInizio.plusDays(dayMaxBook);
                prestito = new Prestito(codePrestito, username, barcode, dataInizio, dataScadenza);
                /**
                 * inserisco l'oggetto di tipo Prestito all'interno dell'archivio dei prestiti nel db.
                 */
                db.insertPrestito(prestito);
                db.incrementLicenzeUser(username, bookOrFilm);
                View.stampaRichiestaSingola(View.MG_AZIONE_SUCCESSO);
            } else View.stampaRichiestaSingola(View.MG_PRESTITO_NON_DISPONIBILE);
        } else View.stampaRichiestaSingola(View.NON_ESISTE_RISORSA);
    }



    /**
     * Gestione della ricerca di risorse dati in ingresso dei parametri.
     */
    public  void researchResource(){
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
                    exist= db.searchGeneral("title", searchString);

                    break;
                /**
                 * Ricerca per Autore.
                 */
                case 2:
                    exist=db.searchGeneral("author", searchString);

                    break;
                /**
                 * Ricerca per Genere.
                 */
                case 3:
                    exist=  db.searchGeneral("genre", searchString);

                    break;

                /**
                 * Ricerca per Anno di Pubblicazione.
                 */
                case 4:
                    exist= db.searchGeneral("year", searchString);
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
    public  String checkInsertId(String username){
        boolean end = false;
        while(!end){
            View.stampaRichiestaSingola(View.CODICE_PRESTITO);
            try {
                string= ViewLibraryGeneral.readString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(db.checkIfPrestito(string) && string.contains(username)){
                end=true;
            } else System.out.println(View.NON_ESISTE_PRESTITO);
        }
        return string;
    }


    public  void controllerProrogaPrestito(String codePrestito){
        /**
         * se vero allora procedi, altrimenti stampa errore.
         * controllo che il codice prestito esista, sia attivo e che sia possibile effettuare la proroga.
         */
        if(lr.checkProrogaPrestito(codePrestito)){
            lr.prorogaPrestito(codePrestito);
            View.stampaRichiestaSingola(View.MG_AZIONE_SUCCESSO);
        } else View.stampaRichiestaSingola(View.NO_PROROGA);
    }

    /**
     * Metodo per la creazione di una risorsa, oggetto di tipo Resource, generica utilizzando altri metodi di inserimento e controllo.
     * @param barcode codice identificativo della risorsa.
     * @param type categoria o tipo della risorsa.
     * @return oggetto di tipo Resource.
     */
    public  Resource createResource(int barcode, String type){
        String title = ViewLibraryGeneral.insertString(View.TITLE);
        List ll = ViewLibraryGeneral.insertList(View.AUTORI);
        List lingue = ViewLibraryGeneral.insertList(View.LINGUE);
        Resource res = new Resource(barcode, type, title, ll, lingue, ViewLibraryGeneral.insertNumberEqual(View.YEAR, 4), ViewLibraryGeneral.insertString(View.GENERE), licenseList);
        return res;
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
        if (!lr.checkBorrowed(db.getUser(username), bookOrFilm)){
            /**
             * genero il codice del prestito.
             */
            System.out.println(db.getResource(barcode).toString());
            String codePrestito = lr.generateId(username,barcode);
            /**
             * creo il prestito e lo salvo nel database, aumentando di uno il numero di licenze dell'utente.
             * Avendo già identivicato prima il tipo di risorsa ne modificando quindi il numero associato.
             */
            createPrestito(codePrestito, username, barcode, bookOrFilm);
        } else System.out.println(view.FINITE_LICENZE_PRESTITO_USER);
    }

    /**
     * Metodo per stampare a video le risorse del database
     */
    public  void controllerPrintSpecificResource(String type){
        View.viewPrintSpecificResource(type);
        db.printSpecificResource(type);
    }

    /**
     * Metodo genera il codice del prestito richiesto dall'utente di una risorsa.
     * @param barcode {@link #barcode}
     * @return codice prestito
     */
    public String generateId(String username, int barcode){
        Random random = new Random();
        String number = String.format("%04d", random.nextInt(10000));
        String string= username+barcode+number;
        return string;
    }

    /**
     * Metodo che permette di "rimuovere", in maniera fittizia, una risorsa dall'elenco di risorse.
     * Se la risorsa ha piu\' di una copia bisogna decrementarla di una, ovviamente il numero in posizione 0.
     * Viene tenuta traccia dello storico della risorsa, infatti anche se le copie sono esaurite rimangono all'interno dell'archivio.
     */
    public  void removeResource(int barcode) {
        if(db.checkIfResource(barcode)) {
            Integer[] copie = db.getResource(barcode).getLicense();
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
                    db.decrementCopyOrLicenze(barcode,0 );
                    View.stampaRichiestaSingola(View.MG_AZIONE_SUCCESSO);
                    if (copie[0] == 0) System.out.println(View.RISORSA_SCADUTA);
                }else{
                    //ovvero uguale a zero (perche\' <1)
                    View.stampaRichiestaSingola(View.RISORSA_SCADUTA);
                }
            } else System.out.println(View.RISORSA_IMPOSSIBILE_RIMUOVERE);
        } else View.stampaRichiestaSingola(View.NON_ESISTE_RISORSA);
    }

    public String generateIdController(String username, int barcode){
        return lr.generateId(username,barcode);
    }

}
