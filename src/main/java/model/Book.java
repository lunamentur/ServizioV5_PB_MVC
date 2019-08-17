package main.java.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Book che rappresenta una categoria specifica di Resource, con i relativi attributi e metodi set e get.
 * @author Reda Kassame, Simona Ramazzotti.
 * @version 5
 */
public class Book extends Resource {

    private int numPage;
    private String casaEdit;

    /**
     * Array license
     * Indice in posizione 0 indica il numero di copie totali
     * indice in posizione 1 indica il numero di copie in prestito
     * La differenza fra i valori all'interno dell'array indica le copie che sono disponibili: se minore o uguale a zero non vi sono copie disponibili al prestito,
     * ovvero non vi sono licenze disponibili.
     */
    private Integer [] license;

    /**
     * Costruttore dell'oggetto Book.
     * {@link Resource}
     * @param title titolo dell'opera libro.
     * @param barcode codice a barre, chiave primaria identificatrice della risorsa libro.
     * @param author autore della risorsa libro.
     * @param langues lingua o lingue in cui e\' tradotta la risorsa libro.
     * @param numPage numero di pagine che compongono la risorsa libro.
     * @param yearPub anno di pubblicazione della risorsa libro.
     * @param genre genere della risorsa libro (per esempio Romanzo, Fumetto, Saggio, ecc.)
     * @param license licenze di prestito di tale risorsa libro che sono disponibili e il numero di copie della risorsa libro.
     */
    public Book(int barcode, String type, String title, List author, List langues, int yearPub, String genre, Integer [] license, int numPage, String casaEdit ){
        super(barcode,type,title,author,langues,yearPub,genre,license);
        this.numPage=numPage;
        this.casaEdit=casaEdit;
    }


    /**
     * Get and Set
     */

    public int getNumPage() {
        return numPage;
    }

    public void setNumPage(int numPage) {
        this.numPage = numPage;
    }

    public String getCasaEdit() {
        return casaEdit;
    }

    public void setCasaEdit(String casaEdit) {
        this.casaEdit = casaEdit;
    }
}