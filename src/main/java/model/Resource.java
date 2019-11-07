package main.java.model;

import java.io.Serializable;
import java.util.List;

/**
 * Classe Resource che identifica oggetti di tipo Risorse utilizzati dal servizio di prestiti temporanei.
 *
 * @author Reda Kassame, Simona Ramazzotti.
 * @version 5
 */
public class Resource implements Serializable {

    /**
     * Variabili istanza della superClasse.
     *
     * @param barcode codice a barre, chiave primaria identificatrice della risorsa.
     * @param author regista o autore della risorsa.
     * @param langues lingua o lingue in cui e\' tradotta o disponibile la risorsa.
     * @param yearPub anno di pubblicazione della risorsa.
     * @param genre genere della risorsa.
     * @param title titolo o nome della risorsa.
     * @param type il tipo di risorsa, la categoria.
     */
    String title, genre, type;
    int barcode, yearPub;
    List<String> langues, author;

    /**
     * Array license
     * Indice in posizione 0 indica il numero di copie totali
     * indice in posizione 1 indica il numero di copie in prestito
     * La differenza fra i valori all'interno dell'array indica le copie che sono disponibili: se minore o uguale a zero non vi sono copie disponibili al prestito,
     * ovvero non vi sono licenze disponibili.
     */
    Integer [] license;

    /**
     * Costruttore della superCLASSE, un oggetto generico di tipo Resource, con attributi comuni a tutte le sue sottoclassi.
     */
    public Resource(int barcode, String type, String title, List author, List langues, int yearPub, String genre, Integer [] license ){
        this.author= author;
        this.barcode= barcode;
        this.langues= langues;
        this.title=title;
        this.genre=genre;
        this.yearPub=yearPub;
        this.license=license;
        this.type=type;
    }

    /**
     * Metodi GET and SET
     */

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public int getYearPub() {
        return yearPub;
    }

    public List<String> getLangues() {
        return langues;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public Integer[] getLicense() {
        return license;
    }

    public void setLicense(Integer[] license) {
        this.license = license;
    }

    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        return "| | CATEGORIA= '" + type + '\'' + " | titolo= '" + title + '\'' +
                " | barcode=  " + barcode +
                " |  anno di pubblicazione= " + yearPub +
                " |  autore= " + author +
                "| | ";
    }

    /**
     * Metodo toString() rivisitato e utilizzato per la stampa nel menu admin.
     * @return elenco dei parametri della risorsa in stringa.
     */
    public String toStringRes() {
        return barcode + " , " +
                " , " + title +
                " , " + author +
                " , " + genre +
                " , " + langues +
                " , " + yearPub +
                " , " + toStringLicense();
    }

    public String toStringLicense(){
        return "( copie totali: "+ license[0] + " , copie in prestito: "+ license[1] +" )";
    }

    private void metodoCrea(){

    }

}