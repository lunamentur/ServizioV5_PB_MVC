package main.java.model;

import java.util.List;

/**
 * Classe Film che rappresenta una categoria specifica di Resource, con i relativi attributi e metodi set e get.
 * @author Reda Kassame, Simona Ramazzotti
 */
public class Film extends Resource {

    /**
     * Nuove variabili istanza della sottoclasse Film, che estende Resoruce.
     */
    private int duration, ageRestriction;

    /**
     * Costruttore dell'oggetto Book.
     * {@link Resource}
     * @param title titolo della risorsa film.
     * @param barcode codice a barre, chiave primaria identificatrice della risorsa film.
     * @param author regista della risorsa film.
     * @param langues lingua o lingue in cui e\' tradotta la risorsa film.
     * @param yearPub anno di pubblicazione della risorsa film.
     * @param genre genere della risorsa film (per esempio Romantico, Horror, Documentiario, Thriller, ecc.)
     * @param license licenze di prestito di tale risorsa libro che sono disponibili e il numero di copie della risorsa film.
     * @param duration durata, espressa in minuti, della risorsa film.
     * @param ageRestriction eta\' minima consigliata per la visione della risorsa film.
     */
    public Film(int barcode, String type, String title, List author, List langues, int yearPub, String genre, Integer [] license, int ageRestriction, int duration ){
        super(barcode,type, title, author, langues, yearPub, genre, license);
        this.duration=duration;
        this.ageRestriction= ageRestriction;
    }

    /**
     * toString()
     */
    @Override
    public String toString() {
        return "| | CATEGORIA= '" + type + '\'' + " | titolo= '" + title + '\'' +
                " | barcode=  " + barcode +
                " |  anno di pubblicazione= " + yearPub +
                " |  autore= " + author +  " |  vietata al di sotto di anni: " + ageRestriction +
                "| | ";
    }

}
