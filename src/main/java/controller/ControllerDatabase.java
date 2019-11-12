package main.java.controller;

import main.java.model.*;
import main.java.model.library.LibraryResources;
import main.java.view.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControllerDatabase {

    private View view=new View();
    private Database db;
    private LibraryResources lr= new LibraryResources(db);


    /**
     * Costruttore
     */
    public ControllerDatabase(Database db){ this.db=db;}

    /**
     * Inizializzazione di oggetti predefiniti.
     */
    public void initDatabase(){
        if(!db.readAllHash(View.FILE_USER) || !db.readAllHash(View.FILE_ADMIN) || !db.readAllHash(View.FILE_PRESTITO) || !db.readAllHash(View.FILE_RESOURCE)) {
            initAllObject();
            view.stampaRichiestaSingola("Caricamento Database predefinito {OK}");
        }
    }

    /**
     * Una volta aperto il database elimina in automatico i prestiti scaduti/terminati.
     */
    public void removeAutomaticPrestitoDatabase(){
        db.removeAutomaticPrestito();
    }

    /**
     * Metodo che permette il salvataggio delle informazione del database prima della chiusura del programma.
     */
    public void saveData(){
        db.saveAllHash(View.FILE_ADMIN);
        db.saveAllHash(View.FILE_USER);
        db.saveAllHash(View.FILE_PRESTITO);
        db.saveAllHash(View.FILE_RESOURCE);
    }


    /**
     * restituisce un Admin
     */
    public Admin getAdmin(String username){
        Admin admin = db.getAdmin(username);
        return admin ;
    }

    /**
     * restituisce un User
     */
    public User getUser(String username){
        User user = db.getUser(username);
        return user;
    }


    public void numberOfPrestitiCalendarYear(){
        db.numberOfPrestitiCalendarYear();
    }

    /**
     * Visualizzare le proroghe per anno solare.
     */
    public void prorogaCalendarYear(){
        db.prorogaCalendarYear();
    }

    /**
     * Visualizzare quale risorsa e\' stata oggetto del maggior numero di prestiti per anno solare.
     * {@link Database#resourceCalendarYear()}
     */
    public void resourceCalendarYear(){
        db.resourceCalendarYear();
    }


    /**
     * Visualizzare il numero di prestiti per fruitore per anno solare.
     * {@link Database#numberOfPrestitiUserCalendarYear()}
     */
    public void numberOfPrestitiUserCalendarYear(){
        db.numberOfPrestitiUserCalendarYear();
    }

    /**
     * Visualizzare l'elenco degli utenti.
     */
    public void listUsers(){
        db.listUsers();
    }

    /**
     * controlla che sia possibile creare un prestito
     */
    public boolean checkIfResource(int barcode){
        if(db.checkIfResource(barcode)){
            /**
             * determino il tipo di risora (libro o film) e successivamente credo il prestito
             */
            return true;
        }
         else return false;
    }

    /**
     * Stampa prestiti attivi user
     * {@link Database#printActivePrestitiUser(String)}
     */
    public void printActivePrestitiUser(String username) {
        db.printActivePrestitiUser(username);
    }

    private void initAllObject() {
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

    /**
     * rimozione prestito
     */
    public void removePrestito(int temp, String username, String id) {
        db.removePrestito(temp,username,id);
    }

    /**
     * Controlla che un user abbia un prestito
     */
    public boolean userHavePrestito(String username) {
        return db.userHavePrestito(username);
    }

    /**
     * Prende il barcode del prestito passato
     */
    public int getPrestitoBarcode(String id) {
        int temp=db.getPrestito(id).getBarcode();
        return temp;
    }
}
