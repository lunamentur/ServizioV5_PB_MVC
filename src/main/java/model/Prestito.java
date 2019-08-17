package main.java.model;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Classe Prestito che specifica una relazione tra un User e una Resource, ovvero il prestito della risorsa a un User.
 * L'oggetto Prestito lega gli altri oggetti e facilita la gestione, con i relativi attributi e metodi get e set.
 * @author Reda, Simona
 */
public class Prestito implements Serializable {

    private String username, codePrestito;
    /**
     * @param on_off boolean che tiene traccia se il prestito e\' decaduto da molto tempo, ovvero se terminato.
     *               Alla creazione e\' inizializzato come true, ovvero attivo. Se false significa scaduto, terminato, decaduto.
     */
    private boolean on_off;
    /**
     *  @param numProroga numero delle proroghe richieste dall'user per la risorsa in questione. Alla creazione e\' inizializzato a zero.
     */
    private int barcode, numProroga;
    private LocalDate dataInizio, dataScadenza;

    /**
     * Costruttore dell'oggetto di tipo Prestito.
     *
     * @param codePrestito codice univoco, chiave primaria, per identificare un singolo prestito avvenuto tra una risorsa e un user.
     * @param username username dell'user che usufruisce di tale risorsa, identificatore univoco. {@link #username}
     * @param barcode codice univoco per identificare quale risorsa e\' data in prestito all'user. {@link #barcode}
     * @param dataInizio data di inizio del prestito della risorsa all'user.
     * @param dataScadenza data di scadenza del prestito della risorsa all'user.
     */
    public Prestito(String codePrestito, String username, int barcode, LocalDate dataInizio, LocalDate dataScadenza){
        this.barcode=barcode;
        this.codePrestito=codePrestito;
        this.dataInizio=dataInizio;
        this.username=username;
        this.dataScadenza=dataScadenza;
        /**
         * {@link Prestito#on_off}
         */
        this.on_off=true;
        /**
         * {@link Prestito#numProroga}
         */
        this.numProroga=0;
    }


    /**
     * Metodi get e set
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodePrestito() {
        return codePrestito;
    }

    public void setCodePrestito(String codePrestito) {
        this.codePrestito = codePrestito;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public boolean getOn_off() {
        return on_off;
    }

    public void setOn_off(boolean on_off) {
        this.on_off = on_off;
    }

    public int getNumProroghe() {
        return numProroga;
    }

    public void setNumProroghe(int numProroga) {
        this.numProroga = numProroga;
    }
}
