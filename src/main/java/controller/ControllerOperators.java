package main.java.controller;

import main.java.model.library.LibraryControllerInterface;
import main.java.model.library.LibraryOperators;
import main.java.view.*;
import main.java.model.*;

import java.time.LocalDate;

/**
 * Classe Controller che si occupa della gestione dei metodi del model riguardanti User e Admin.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class ControllerOperators {
    private LocalDate birthDate;
    private  View view =new View();
    private Database db;
    //private LibraryOperators lo = new LibraryOperators(db);
    private ViewLibraryGeneral vlg = new ViewLibraryGeneral();
    private LibraryControllerInterface library;



    /**
     * Creazione di variabili e oggetti utili per i metodi di controllo relativi all'User.
     */
    private  String username;
    private  int year, month, day;

    /**
     * inizializzazione delle licenze utente.
     */
    private  Integer [] borrowed={0,0};

    /**
     * costruttore della classe
     */
    public ControllerOperators(Database db){
        this.db = db;
        this.library = new LibraryOperators(db);
    }


    /**
     * metodo che gestisce il rinnovo della registrazione utente usando i metodi che si trovano nel model.
     * @param user utente, fruitore.
     */
    public  void controllerRenewalRegistration(User user) {
        if (library.isRenewal(user)) {
            view.viewRenewalRegistrazion();
            int choice = vlg.readInt();
            if (choice == 0) {
                library.renewalRegistration(user);
            }
        }
    }

    /**
     * Metooo di controllo del Login da parte dell'User. L'autenticazione va a buon fine se l'username è presente all'interno
     * del Database e se la password inserita corrisponde.
     */
    public String checkLogin(){
        username= vlg.insertString(Constant.USER_NAME);
        String password = vlg.insertString(Constant.PASSWORD);
        if(db.checkLoginIfTrue(username, password)) System.out.println(Constant.AUTENTICAZIONE_SUCCESSO);
        else {
            username = "_error_";
        }
        return username;
    }


    /**
     * Metodo che controlla se l'username &egrave; gia presente nel db.
     * Se si continua a ciclare finche\' non ne viene inserito uno nuovo (inesistente).
     * @return username
     */
    public  String insertUserName(){
        boolean end=false;
        do{
            username= vlg.insertString(view.USER_NAME);
            if(db.checkIfUser(username)){
                view.stampaRichiestaSingola(view.USERNAME_ESISTE);
            }else end=true;
        }while(!end);
        return username;
    }

    /**
     * Metodo che assembla i metodi per la registrazione con i relativi controlli e salva il nuovo user,
     * oggetto di tipo User, all'interno del db.
     */
    public  boolean registrationProcess(){
        User user = new User(vlg.insertString(view.NOME), vlg.insertString(view.COGNOME), insertUserName(), vlg.insertString(view.PASSWORD), insertDate(), LocalDate.now(), borrowed);
        if (db.checkIf18(user.getBirthDate())) {
            db.insertUser(user);
            return true;
        }else{
            return false;
        }
    }

    //DA METTERE NEL MENUSTANDARD INIZIALE L'OPZIONE 666
    /**
     * Metodo che permette la registrazione di un nuovo admin.
     * Controlla preventivamente se l'username inserito &egrave; gi&agrave; esistente.
     */
    public  void registrationAdmin() {
        boolean end = false;
        do {
            username = vlg.insertString(Constant.USER_NAME);
            if (!db.checkIfAdmin(username)) {
                String password = vlg.insertString(Constant.PASSWORD);
                Admin newAdmin = new Admin(username, password);
                db.insertAdmin(newAdmin);
                end = true;
            } else System.out.println(Constant.USERNAME_ESISTE);
        } while (!end);
    }

    public  void userExpired(User user) {
        library.userExpired(user);
    }

    /**
     * Medoto per inserire la data di nascita, di tipo LocalDate, dell'user.
     * Prende da tastiera anno, mese e giorno e crea una data di tipo LocalDate.
     * @return birthDate
     */
    public  LocalDate insertDate(){
        boolean end= false;
        view.stampaRichiestaSingola(Constant.DATA_NASCITA);
        while(!end){
            view.stampaRichiestaSingola(Constant.YEAR);
            year= vlg.readInt();
            if(String.valueOf(year).length()==4)
            {
                view.stampaRichiestaSingola(Constant.MONTH);
                month= vlg.readInt();
                view.stampaRichiestaSingola(Constant.DAY);
                day= vlg.readInt();
                if(library.trueDate(month,day)) {
                    birthDate= LocalDate.of(year,month,day);
                    end=true;
                }
            }
            else {
                view.viewMsgError();
            }
        }
        return birthDate;
    }

}
