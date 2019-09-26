package main.java.controller;

import main.java.model.library.LibraryOperators;
import main.java.view.*;
import main.java.model.*;

import java.time.LocalDate;

public class ControllerOperators {
    public static LocalDate birthDate;
    private static View view=new View();
    private static int vincolo= 3;

    /**
     * Creazione di variabili e oggetti utili per i metodi di controllo relativi all'User.
     */
    private static String id, username, searchString;
    private static int year, month, choice, barcode, day, temp;
    private static long rangeYear=5;
    private static long rangeDay=-10;

    /**
     * inizializzazione delle licenze utente.
     */
    private static Integer [] borrowed={0,0};


    public static void controllerRenewalRegistration(User user) {
        int temp=0;
        if (LibraryOperators.isRenewal(user)) {
            view.viewRenewalRegistrazion();
            int choice = ViewLibraryGeneral.readInt();
            if (choice == 0) {
                LibraryOperators.renewalRegistration(user);
            }
        }
    }

    /**
     * Metooo di controllo del Login da parte dell'User. L'autenticazione va a buon fine se l'username è presente all'interno
     * del Database e se la password inserita corrisponde.
     */
    public static String checkLogin(){
        username= ViewLibraryGeneral.insertString(Constant.USER_NAME);
        String password = ViewLibraryGeneral.insertString(Constant.PASSWORD);
        if(Database.checkLoginIfTrue(username, password)) System.out.println(Constant.AUTENTICAZIONE_SUCCESSO);
        else {
            username = "_error_";
        }
        return username;
    }



    /**
     * Metodo che controlla se l'username &egrave; gia presente nel Database.
     * Se si continua a ciclare finche\' non ne viene inserito uno nuovo (inesistente).
     * @return username
     */
    public static String insertUserName(){
        boolean end=false;
        do{
            username= ViewLibraryGeneral.insertString(Constant.USER_NAME);
            if(Database.checkIfUser(username)){
                view.stampaRichiestaSingola(Constant.USERNAME_ESISTE);
            }else end=true;
        }while(!end);
        return username;
    }

    /**
     * Metodo che assembla i metodi per la registrazione con i relativi controlli e salva il nuovo user,
     * oggetto di tipo User, all'interno del Database.
     */
    public static boolean registrationProcess(){
        User user = new User(ViewLibraryGeneral.insertString(Constant.NOME), ViewLibraryGeneral.insertString(Constant.COGNOME), insertUserName(), ViewLibraryGeneral.insertString(Constant.PASSWORD), insertDate(), LocalDate.now(), borrowed);
        if (Database.checkIf18(user.getBirthDate())) {
            Database.insertUser(user);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Metodo che permette la registrazione di un nuovo admin.
     * Controlla preventivamente se l'username inserito &egrave; gi&agrave; esistente.
     */
    public static void registrationAdmin() {
        boolean end = false;
        do {
            username = ViewLibraryGeneral.insertString(Constant.USER_NAME);
            if (!Database.checkIfAdmin(username)) {
                String password = ViewLibraryGeneral.insertString(Constant.PASSWORD);
                Admin newAdmin = new Admin(username, password);
                Database.insertAdmin(newAdmin);
                end = true;
            } else System.out.println(Constant.USERNAME_ESISTE);
        } while (!end);
    }

    /**
     * Medoto per inserire la data di nascita, di tipo LocalDate, dell'user.
     * Prende da tastiera anno, mese e giorno e crea una data di tipo LocalDate.
     * @return birthDate
     */
    public static LocalDate insertDate(){
        boolean end= false;
        view.stampaRichiestaSingola(Constant.DATA_NASCITA);
        while(!end){
            view.stampaRichiestaSingola(Constant.YEAR);
            year= ViewLibraryGeneral.readInt();
            if(String.valueOf(year).length()==4)
            {
                view.stampaRichiestaSingola(Constant.MONTH);
                month= ViewLibraryGeneral.readInt();
                view.stampaRichiestaSingola(Constant.DAY);
                day= ViewLibraryGeneral.readInt();
                if((String.valueOf(month).length()<=2 && month <= 12 ) && (( String.valueOf(day).length() <= 2) && day <= 31)) {
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
