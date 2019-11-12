package main.java.controller;
import main.java.model.*;
import main.java.model.library.LibraryResources;
import main.java.view.*;

/**
 * Classe Controller
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class Controller {
    private View view;
    private Database database;
    private ControllerOperators co;
    private ControllerResources cr;
    private ControllerDatabase cd;
    private String username;
    private static int vincolo= 3;

    /**
     * Creazione di variabili e oggetti utili per i metodi di controllo relativi all'User.
     */
    int  choice2;
    boolean end1, end2;


    /**
     * Costruttore della classe
     */
    public Controller() {
        view=new View();
        username= new String();
        database=new Database();

        co = new ControllerOperators(database);
        cr = new ControllerResources(database);
        cd = new ControllerDatabase(database);
    }

    /**
     * Inizializzati i dati precompilati e avvio del menu iniziale del programma.
     */
    public void init(){
        /**
         * inizializza dati
         */
        cd.initDatabase();

        /**
         * rimozione dei prestiti scaduti
         */
        cd.removeAutomaticPrestitoDatabase();

        /**
         * avvio programma
         */
        view.stampaRichiestaSingola(view.MG_INIZIALE);
        menuStandard();

        /**
         * salva tutte le modifiche fatte.
         */
        cd.saveData();

        /**
         * chiusura programma
         */
        System.out.println(view.FINE_MENU);
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
                    username = co.checkLogin();
                    Admin admin= cd.getAdmin(username);
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

                case 666:
                    co.registrationAdmin();
                    break;

                /**
                 * Uscita dal programma.
                 */
                case 0:
                    end1=true;
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
                        cr.controllerPrintSpecificResource(view.BOOK);
                        cr.controllerPrintSpecificResource(view.FILM);
                        break;
                    /**
                     * Aggiungere una risorsa all'elenco.
                     * Se gia\' presente incrementa il numero di licenze disponibili.
                     * {@link LibraryResources#createResourceProcess()}
                     */
                    case 2:
                        cr.createResourceProcess();
                        break;
                    /**
                     * Rimozione fittizia di una risorsa dall'archivio.
                     * {@link Database#removeResource(int)}
                     */
                    case 3:
                        view.stampaRichiestaSingola(view.BARCODE);
                        int number = ViewLibraryGeneral.readInt();
                        cr.removeResource(number);
                        break;

                    /**
                     * Ricerca di una risorsa nel Database in base a valori diversi.
                     * {@link #researchResource()}
                     */
                    case 4:
                        cr.researchResource();
                        break;

                    /**
                     * Visualizzare il numero di prestiti per anno solare.
                     * {@link Database#numberOfPrestitiCalendarYear()}
                     */
                    case 5:
                        cd.numberOfPrestitiCalendarYear();
                        break;

                    /**
                     * Visualizzare le proroghe per anno solare.
                     * {@link Database#prorogaCalendaryear()}
                     */
                    case 6:
                        cd.prorogaCalendarYear();
                        break;

                    /**
                     * Visualizzare quale risorsa e\' stata oggetto del maggior numero di prestiti per anno solare.
                     * {@link Database#resourceCalendarYear()}
                     */
                    case 7:
                        cd.resourceCalendarYear();
                        break;

                    /**
                     * Visualizzare il numero di prestiti per fruitore per anno solare.
                     * {@link Database#numberOfPrestitiUserCalendarYear()}
                     */
                    case 8:
                        cd.numberOfPrestitiUserCalendarYear();
                        break;

                    /**
                     * Visualizzare l'elenco degli utenti.
                     */
                    case 9:
                        cd.listUsers();
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
                        if(cd.checkIfResource(barcode)){
                            cr.createRequestPrestito(barcode,username);
                        } else System.out.println(view.NON_ESISTE_RISORSA);
                        break;

                    /**
                     *  Stampare i prestiti in atto, quindi attivi, dall'utente.
                     *  {@link Database#printActivePrestitiUser(String)}
                     */
                    case 2:
                        cd.printActivePrestitiUser(username);
                        break;

                    /**
                     * Richiedere una proroga.
                     * {@link LibraryResources#checkInsertId(String)} and {@link LibraryResources#prorogaPrestito(String)}
                     */
                    case 3:
                        /**
                         * se l'utente non ha nessun prestito attivo allora non puo\' effettuare la ricerca.
                         */
                        if(cd.userHavePrestito(username)){
                            String id=cr.checkInsertId(username);
                            cr.controllerProrogaPrestito(id);
                        }else System.out.println(view.USER_NON_HA_PRESTITI);
                        break;

                    /**
                     * Svolgere una ricerca. Uguale a quello dell'admin.
                     * {@link #researchResource()}
                     */
                    case 4:
                        cr.researchResource();
                        break;

                    /**
                     * Terminare il prestito di una risorsa.
                     * Tramite il metodo {@link Database#removePrestito(int, String, String)} modifichiamo la variabile on_off impostandola a false, ovvero prestito inattivo.
                     */
                    case 5:
                        /**
                         * se l'utente non ha nessun prestito attivo allora non puo\' effettuare la ricerca.
                         */
                        if(cd.userHavePrestito(username)){
                            String id = cr.checkInsertId(username);
                            int temp= cd.getPrestitoBarcode(id);
                            cd.removePrestito(temp, username, id);
                            System.out.println(view.MG_AZIONE_SUCCESSO);
                        }else System.out.println(view.USER_NON_HA_PRESTITI);

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

            end2 = false;
            do {
                view.viewPannelUser();
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

                        if (co.registrationProcess()) {
                            System.out.println(view.GRAZIE_PER_ISCRIZIONE);

                        } else {
                            System.out.println(view.MINORENNE);
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

                        username = co.checkLogin();
                        User user = cd.getUser(username);

                        /**
                         * Una volta che il Login &egrave; andato a buon fine controlliamo che l'iscrizione dell'user sia ancora valida.
                         * Se non lo &egrave;, ovvero sono decaduti i privilegi, può avvenire il rinnovo dell'iscrizione.
                         * Se l'iscrizione e\' valida allora l'User puo\' accedere a determinate azioni riguardanti il prestito.
                         * Avviene solo se non e\' admin.
                         */
                        if (!username.equals("_error_")) {
                            System.out.println("Benvenuto " + user.getUsername());
                            co.userExpired(user);
                            if (!user.getName().equals("_expired_")) {
                                co.controllerRenewalRegistration(user);
                                menuActionUser(user);
                            }
                        } else System.out.println(view.ERRORE_LOGIN);

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


}
