package main.java.controller;
import main.java.model.*;
import main.java.model.library.LibraryResources;
import main.java.view.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ControllerResources {

    //////////////////////////////////////////////////////////////////////////////////
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
    private LibraryResources lr;
    private  int barcode;
    private  int vincolo=3;
    private  Integer [] licenseList= {0,0};//licenze con due componenti.

    /**
     * variabili che corrispondono agli indici della lista di licenze.
     * {@link Resource}
     */
    private  int copieRisorsa = 0;
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
        view.stampaRichiestaSingola(Constant.CATEGORIA);
        int choice = ViewLibraryGeneral.readIntChoise(1,2);
        //vero se e\' un libro, oppure un film
        if (choice == 1) string = Constant.BOOK;
        else string = Constant.FILM;
        barcode= ViewLibraryGeneral.insertBarcode(vincolo);
        if(!lr.checkIfExist(barcode)){
            Resource res= createResource(barcode, string);
            switch(choice)
            {
                /**
                 * Creazione di una risorsa di tipo Book.
                 */
                case 1:
                    Book book = new Book(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, ViewLibraryGeneral.insertNum(Constant.NUM_PAG), ViewLibraryGeneral.insertString(Constant.CASA_EDIT));
                    db.insertResource(book);
                    view.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
                    break;
                /**
                 * Creazione di una risorsa di tipo Film.
                 */
                case 2:
                    Film film = new Film(res.getBarcode(), string, res.getTitle(), res.getAuthor(), res.getLangues(), res.getYearPub(), res.getGenre(), licenseList, ViewLibraryGeneral.insertNum(Constant.AGE_RESTRIC), ViewLibraryGeneral.insertNum(Constant.DURATA));
                    db.insertResource(film);
                    view.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
                    break;

                /**
                 * Errore, l'inserimento non &egrave; corretto
                 */
                default:
                    view.viewMsgError();
                    break;
            }
        } else view.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);

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
                view.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
            } else view.stampaRichiestaSingola(Constant.MG_PRESTITO_NON_DISPONIBILE);
        } else view.stampaRichiestaSingola(Constant.NON_ESISTE_RISORSA);
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
                    System.out.println(view.MG_ERRORE);
                    break;
            }
            /**
             * se nessuna risorsa viene trovata allora compare il messaggio di avviso.
             */
            if (!exist) //se true diventa false, ovvero se sono uguali significa che non c'e risorsa
                view.stampaRichiestaSingola(view.NON_ESISTE_RISORSA);
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
            view.stampaRichiestaSingola(Constant.CODICE_PRESTITO);
            try {
                string= ViewLibraryGeneral.readString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(db.checkIfPrestito(string) && string.contains(username)){
                end=true;
            } else System.out.println(Constant.NON_ESISTE_PRESTITO);
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
            view.stampaRichiestaSingola(Constant.MG_AZIONE_SUCCESSO);
        } else view.stampaRichiestaSingola(Constant.NO_PROROGA);
    }

    /**
     * Metodo per la creazione di una risorsa, oggetto di tipo Resource, generica utilizzando altri metodi di inserimento e controllo.
     * @param barcode codice identificativo della risorsa.
     * @param type categoria o tipo della risorsa.
     * @return oggetto di tipo Resource.
     */
    public  Resource createResource(int barcode, String type){
        String title = ViewLibraryGeneral.insertString(Constant.TITLE);
        List ll = ViewLibraryGeneral.insertList(Constant.AUTORI);
        List lingue = ViewLibraryGeneral.insertList(Constant.LINGUE);
        Resource res = new Resource(barcode, type, title, ll, lingue, ViewLibraryGeneral.insertNumberEqual(Constant.YEAR, 4), ViewLibraryGeneral.insertString(Constant.GENERE), licenseList);
        return res;
    }

    /**
     * Metodo per stampare a video le risorse del database
     */
    public  void controllerPrintSpecificResource(String type){
        view.viewPrintSpecificResource(type);
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
                    System.out.println(Constant.MG_AZIONE_SUCCESSO);
                    if (copie[0] == 0) System.out.println(Constant.RISORSA_SCADUTA);
                }else{
                    //ovvero uguale a zero (perche\' <1)
                    System.out.println(Constant.RISORSA_SCADUTA);
                }
            } else System.out.println(Constant.RISORSA_IMPOSSIBILE_RIMUOVERE);
        } else view.stampaRichiestaSingola(Constant.NON_ESISTE_RISORSA);
    }
    public void initAllObject() {
        Integer[] borrowed_test = {0, 0};
        Integer[] license_book1 = {3, 2};
        Integer[] license_book2 = {3, 1};
        Integer[] borrowed1 = {2, 0};
        Integer[] license_film1 = {3, 1};
        Integer[] license_film2 = {3, 0};
        Integer[] borrowed2 = {1, 1};
        List<String> langues_test = new ArrayList<String>();
        List<String> author_test = new ArrayList<String>();
        langues_test.add("inglese");
        langues_test.add("spagnolo");
        author_test.add("Gino");
        author_test.add("Pino");
        //genero utenti
        Admin admin_reda = new Admin("admin_reda", "password");
        Admin admin_simona = new Admin("admin_simona", "password123");
        db.getAdminList().put(admin_reda.getUsername(), admin_reda);
        db.getAdminList().put(admin_simona.getUsername(), admin_simona);

        //genero utenti
        User user1 = new User("test", "test", "test1", "test1", LocalDate.of(1996, 01, 01), LocalDate.of(2019, 1, 1), borrowed1);
        User user3 = new User("test", "test", "test3", "test3", LocalDate.of(1996, 01, 01), LocalDate.of(2018, 1, 1), borrowed2);
        db.getUserList().put(user1.getUsername(), user1);
        db.getUserList().put(user3.getUsername(), user3);

        User userRinnovo = new User("test", "test", "rinnovo", "rinnovo", LocalDate.of(1996, 01, 01), LocalDate.of(2014, 04, 18), borrowed_test);
        db.getUserList().put(userRinnovo.getUsername(), userRinnovo);

        User userScaduto = new User("test", "test", "scaduto", "scaduto", LocalDate.of(1996, 01, 01), LocalDate.of(2012, 02, 03), borrowed_test);
        db.getUserList().put(userScaduto.getUsername(), userScaduto);

        //genero libri
        Book book1 = new Book(111, Constant.BOOK, "libro di test 1", langues_test, author_test, 2000, "Romanzo", license_book1, 220, "Giunti");
        Book book2 = new Book(222, Constant.BOOK, "libro di test 2", langues_test, author_test, 2000, "Romanzo", license_book2, 220, "Giunti");
        db.getResourceList().put(book1.getBarcode(), book1);
        db.getResourceList().put(book2.getBarcode(), book2);

        //genero film
        Film film1 = new Film(333, Constant.FILM, "Film 1", author_test, langues_test, 2001, "horror", license_film1, 18, 125);
        Film film2 = new Film(444, Constant.FILM, "Film 2", author_test, langues_test, 2003, "horror", license_film2, 18, 139);
        db.getResourceList().put(film2.getBarcode(), film2);
        db.getResourceList().put(film1.getBarcode(), film1);

        //genero prestiti
        Prestito pScaduto = new Prestito(lr.generateId(user1.getUsername(), book1.getBarcode()), user1.getUsername(), book1.getBarcode(), LocalDate.of(2019, 2, 3), LocalDate.of(2019, 3, 3));
        Prestito p4 = new Prestito(lr.generateId(user1.getUsername(), book2.getBarcode()), user1.getUsername(), book2.getBarcode(), LocalDate.of(2019, 4, 1), LocalDate.of(2019, 5, 1));
        Prestito p5 = new Prestito(lr.generateId(user3.getUsername(), book1.getBarcode()), user3.getUsername(), book1.getBarcode(), LocalDate.of(2019, 3, 31), LocalDate.of(2019, 4, 30));
        Prestito p3 = new Prestito(lr.generateId(user3.getUsername(), film1.getBarcode()), user3.getUsername(), film1.getBarcode(), LocalDate.of(2019, 4, 2), LocalDate.of(2019, 5, 2));
        db.getPrestitoList().put(pScaduto.getCodePrestito(), pScaduto);
        db.getPrestitoList().put(p3.getCodePrestito(), p3);
    }

}
