package main.java.controller;

import main.java.model.library.LibraryOperators;
import main.java.view.*;
import main.java.model.*;

import java.time.LocalDate;
import java.util.Map;

public class ControllerOperators {
    private LocalDate birthDate;
    private  View view =new View();
    private Database db;
    private LibraryOperators lo = new LibraryOperators(db);
    private ViewLibraryGeneral vlg = new ViewLibraryGeneral(); 
    private  int vincolo= 3;

    /**
     * Creazione di variabili e oggetti utili per i metodi di controllo relativi all'User.
     */
    private  String id, username, searchString;
    private  int year, month, choice, barcode, day, temp;
    private  long rangeYear=5;
    private  long rangeDay=-10;

    /**
     * inizializzazione delle licenze utente.
     */
    private  Integer [] borrowed={0,0};
    public ControllerOperators(Database db){
        this.db = db;
    }


    public  void controllerRenewalRegistration(User user) {
        int temp=0;
        if (lo.isRenewal(user)) {
            view.viewRenewalRegistrazion();
            int choice = vlg.readInt();
            if (choice == 0) {
                lo.renewalRegistration(user);
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
            username= vlg.insertString(Constant.USER_NAME);
            if(db.checkIfUser(username)){
                view.stampaRichiestaSingola(Constant.USERNAME_ESISTE);
            }else end=true;
        }while(!end);
        return username;
    }

    /**
     * Metodo che assembla i metodi per la registrazione con i relativi controlli e salva il nuovo user,
     * oggetto di tipo User, all'interno del db.
     */
    public  boolean registrationProcess(){
        User user = new User(vlg.insertString(Constant.NOME), vlg.insertString(Constant.COGNOME), insertUserName(), vlg.insertString(Constant.PASSWORD), insertDate(), LocalDate.now(), borrowed);
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

    /**
     * Medoto per inserire la data di nascita, di tipo LocalDate, dell'user.
     * Prende da tastiera anno, mese e giorno e crea una data di tipo LocalDate.
     * @return birthDate
     */

    public  boolean checkBorrowed(User user, int number){
       return lo.checkBorrowed(user,number);
    }
    public  void userExpired(User user) {
        lo.userExpired(user);
    }
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

    //PERCHE NON VIENE MAI USATO?
    /**
     * Metodo che permette di visualizzare i prestiti attivi di un utente.
     * Controllo percio\' che il prestito sia attivo, ovvero l'etichetta on_off sia true, altrimenti se false significa che e\' scaduto.
     */
    public  void printActivePrestitiUser(String username){
        int count=0;
        for (Map.Entry<String, Prestito> entry : db.getPrestitoList().entrySet()) {
            if(entry.getValue().getUsername().equals(username) && entry.getValue().getOn_off()){
                System.out.println("Idprestito: "+entry.getValue().getCodePrestito()+ "\t Risorsa: "+db.getResourceList().get(entry.getValue().getBarcode()).getTitle());
            } else count++;
        }
        /**
         * se non ha nessun prestito attivo allora viene stampato a video un messaggio di avviso.
         */
        if(count== db.getPrestitoList().size()){
            System.out.println(Constant.USER_NON_HA_PRESTITI);
        }
    }



}
