package main.java.controller;
import main.java.model.*;
import main.java.model.library.LibraryOperators;
import main.java.model.library.LibraryResources;
import main.java.view.ViewLibraryGeneral;
import main.java.view.*;

public class Controller {
    private View view;
    private Database database;
    //private User user;
    private String username;
    //private Admin admin;
    private static int vincolo= 3;

    /**
     * Creazione di variabili e oggetti utili per i metodi di controllo relativi all'User.
     */
    private static long rangeYear=5;
    private static long rangeDay=-10;
    int choice1, choice2;
    boolean end1, end2;

    /**
     * inizializzazione delle licenze utente.
     */
    private static Integer [] borrowed={0,0};


    public Controller() {
        view=new View();
        database=new Database();
        username= new String();
    }

    /**
     * Inizializzati i dati precompilati e avvio del menu iniziale del programma.
     */
    public void init(){
        /**
         * Inizializzazione di oggetti predefiniti.
         */
        if(!Database.readAllHash(View.FILE_USER) || !Database.readAllHash(View.FILE_ADMIN) || !Database.readAllHash(View.FILE_PRESTITO) || !Database.readAllHash(View.FILE_RESOURCE)) {
            Database.initAllObject();
        }
        /**
         * Una volta aperto il database elimina in automatico i prestiti scaduti/terminati.
         */
        Database.removeAutomaticPrestito();

        //ora apre il primo menu standard
        menuStandard();
        //fine del menu e del programma
        System.out.println(Constant.FINE_MENU);
    }


    //serve? davvero?
    public void login(){
        //inserire il nome e la pass
        //controllo autenticazione
        //rinvio a menù User opure menu action Admin

        //nota bene: tutto nella LibraryOperat e ControllerOp

    }


    /**
     * Menu d'apertura del programma
     */
    public void menuStandard(){
        end1 = false;
        do {
            view.viewMain();
            int choice = ViewLibraryGeneral.readInt();

            /**
             * Accesso al pannello admin
             */
            switch (choice) {
                case 1:
                    /////////////////////////////////////////////////////////////////////////////////////////////
                    /**
                     * L'utente inserisce il proprio nickname e viene cercato all'interno del database.
                     * Se la password &egrave; la stessa allora viene autenticato con successo, altrimenti
                     * continua a ciclare oppure si termina il programma.
                     */
                    username = ControllerOperators.checkLogin();
                    Admin admin = Database.getAdmin(username);
                    if (!username.equals("_error_")) {
                        System.out.println("Benvenuto " + admin.getUsername());
                        menuActionAdmin(admin);
                    } else {
                        System.out.println(View.ERRORE_LOGIN);
                    }
                    /////////////////////////////////////////////////////////////////////////////////////////////
                    break;

                /**
                 * Accesso al pannello utente
                 */
                case 2:
                    /////////////////////////////////////////////////////////////////////////////////////////////
                    pannelUser();
                    /////////////////////////////////////////////////////////////////////////////////////////////
                    break;
            }
        }while(!end1);
    }

    /**
     * Metodo che permette di gestire le azioni che possono essere svolte dall'Admin.
     */
    public void menuActionAdmin(Admin admin){
            boolean end= false;
            do{
                view.viewActionAdmin();
                int choice = ViewLibraryGeneral.readInt();
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
                        int number = ViewLibraryGeneral.readInt();
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
                        view.viewMsgError();
                        break;
                }
            }while(!end);
        }

    /**
     * Metodo che permette di gestire le azioni che possono essere svolte dall'User.
     */
    public void menuActionUser(User user){
        user.getUsername();
            boolean end= false;
            do{
               view.viewActionUser();
               int choice = ViewLibraryGeneral.readInt();
                switch(choice){

                    /**
                     * Richiedere un prestito.
                     * {@link #createRequestPrestito(int, String)}
                     */
                    case 1:
                        int barcode= ViewLibraryGeneral.insertBarcode(vincolo);
                        if(Database.checkIfResource(barcode)){
                        } else  System.out.println(Constant.NON_ESISTE_RISORSA);

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
                            String id=LibraryResources.checkInsertId(username);
                            LibraryResources.prorogaPrestito(id);
                        }else System.out.println(Constant.USER_NON_HA_PRESTITI);
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
                            String id = LibraryResources.checkInsertId(username);
                            int temp = Database.getPrestito(id).getBarcode();
                            Database.removePrestito(temp, username, id);
                            System.out.println(Constant.MG_AZIONE_SUCCESSO);
                        }else System.out.println(Constant.USER_NON_HA_PRESTITI);

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
                        view.viewMsgError();
                        break;
                }
            }while(!end);
        }

    /**
     * Metodo pannello dell'utente per Iscriversi, Autenticazione, Rinnovo iscrizione e Azioni user.
     */
    public void pannelUser(){
            view.viewPannelUser();
            end2 = false;
            do {

                choice2 = ViewLibraryGeneral.readInt();
                switch (choice2) {

                    /**
                     * Iscrizione
                     */
                    case 1:

                        /**
                         * L'utente inserisce il nome, il cognome, la password e la data di nascita.
                         * L'username viene ricercato all'interno del database e, se vi sono doppioni, viene richiesto l'inserimento.
                         * Sulla data di nascita viene fatto un controllo: se l'utente &egrave; maggiorenne allora pu&ograve; essere inserito nel database.
                         */

                        if (ControllerOperators.registrationProcess()) {
                            System.out.println(Constant.GRAZIE_PER_ISCRIZIONE);

                        } else {
                            System.out.println(Constant.MINORENNE);
                        }
                        break;

                    /**
                     * Autenticazione (Login), Rinnovo e Azioni User.
                     */
                    case 2:
                        /**
                         * L'utente inserisce il proprio nickname e viene cercato all'interno del database.
                         * Se la password &egrave; la stessa allora viene autenticato con successo, altrimenti
                         * continua a ciclare oppure si termina il programma.
                         */

                        username = ControllerOperators.checkLogin();
                        User user = Database.getUser(username);

                        /**
                         * Una volta che il Login &egrave; andato a buon fine controlliamo che l'iscrizione dell'user sia ancora valida.
                         * Se non lo &egrave;, ovvero sono decaduti i privilegi, può avvenire il rinnovo dell'iscrizione.
                         * Se l'iscrizione e\' valida allora l'User puo\' accedere a determinate azioni riguardanti il prestito.
                         * Avviene solo se non e\' admin.
                         */
                        if (!username.equals("_error_")) {
                            System.out.println("Benvenuto " + user.getUsername());
                            LibraryOperators.userExpired(user);
                            if (!user.getName().equals("_expired_")) {
                                ControllerOperators.controllerRenewalRegistration(Database.getUser(username));
                                menuActionUser(user);
                            }
                        } else System.out.println(View.ERRORE_LOGIN);

                        break;

                    default:
                        view.viewMsgError();
                        break;

                    case 0:
                        end2 = true;
                        break;
                }
            } while (!end2);
            System.out.println(View.FINE_MENU);
        }


        //da mettere nel controller Resource
    public void researchResource(){
        boolean end= false;
        boolean exist= true;
        do{
            view.viewResearchResource();
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
                view.stampaRichiestaSingola(View.NON_ESISTE_RISORSA);
        }while(!end);
    }
}
