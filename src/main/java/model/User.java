package main.java.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Classe User, ovvero la classe associata all'utente/al fruitore dei servizi di prestito temporaneo.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class User extends Admin implements Serializable {
    private String name, surname;
    private LocalDate birthDate;
    private LocalDate registrationDate;

    /**
     *  @param numRenewal numero del rinnovi dell'iscrizione dell'user.  Alla creazione e\' inizializzato a zero.
     */
    private int numRenewal;

    /**
     * Array license
     * Se entrambi i numeri in tali indici sono maggiori o uguali a 3, allora l'utente non può piu\' prendere in prestito una risorsa.
     */
    private Integer [] borrowed;
    private int prestitoBook = 0;
    private int prestitoFilm = 1;


    /**
     * Costruttore della Classe User, che crea un oggetto di tipo Utente con particolari privilegi e
     *  metodi associati utili nella navigazione e utilizzo del servizio.
     *
     * @param name Nome dell'user.
     * @param surname Cognome dell'user.
     * @param username Nickname dell'user.
     * @param birthDate Data di nascita dell'user, di tipo LocalDate. E\' richiesto che sia maggiorenne per poter diventare utente dei servizi.
     * @param registrationDate Data di iscrizione ai servizi di borrow temporaneo.
     * @param password Password univoca associata all'user per poter autenticare la propria identita\' .
     * @param borrowed licenze disponibili per prendere in prestito risorse.
     */
    public User(String name, String surname, String username, String password, LocalDate birthDate, LocalDate registrationDate, Integer [] borrowed){
        super(username,password);
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.borrowed=borrowed;
        /**
         * {@link #numRenewal}
         */
        this.numRenewal=0;
    }

    /**
     * Metodi di Set e Get della classe User.
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Integer[] getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Integer[] borrowed) {
        this.borrowed = borrowed;
    }

    public int getNumRenewal() {
        return numRenewal;
    }

    public void setNumRenewal(int numRenewal) {
        this.numRenewal = numRenewal;
    }

    public String borrowedToString() {
        return " || prestiti libri: " + borrowed[prestitoBook] + "  prestiti film: " + borrowed[prestitoFilm];
    }

    @Override
    public String toString() {
        return "  {" +
                " name:  " + name +
                ", surname:   " + surname +
                ", birthDate: " + birthDate +
                ", registrationDate:  " + registrationDate + borrowedToString() +
                "  }";
    }
    public boolean checkIf18(){
        LocalDate now = LocalDate.now();
        int age = Period.between(this.birthDate,now).getYears();
        if(age>=18){
            return true;
        }else{
            return false;
        }
    }
}
