package main.java.view;

/**
 * Classe View col compito di generare il menu' iniziale con cui si interfaccera' l'utente finale e
 * altri metodi simili .toString() al fine di visualizzare a video messaggi di errore, domande, simboli e altro.
 * @author Reda Kassame, Simona Ramazzotti.
 * @version 5
 */
public class View {

    public final static String CORNICETTA = "*******************************";
    public final static String CORNICETTA2 ="∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆∆";
    public final static String CORNICETTA3 ="∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞∞";
    public final static String FINE_MENU =CORNICETTA3 + "\n\nArrivederci, grazie per aver usato i Servizi di prestito temporaneo\n\n"+ CORNICETTA3;
    public final static String RICHIESTA="Quale opzione desideri svolgere? ";
    public final static String MG_ERRORE="\nRiprova, hai inserito un valore non valido";
    public final static String MG_ANCORA = "\n"+CORNICETTA2 +"\n\nCosa desideri fare?\n";
    public final static String MG_SCADUTA_ISCRIZIONE ="La sua iscrizione è scaduta poiche\' ha superato i termini prescritti.";
    public final static String MG_INIZIALE= CORNICETTA2+ CORNICETTA2+"\n\nBenvenuto nel Pannello dei Servizi di prestiti temporanei";
    public final static String AUTENTICAZIONE_SUCCESSO="Autenticazione avvenuta con successo, bentornato!";
    public final static String MG_AZIONE_SUCCESSO="L'azione appena completata e\' avvenuta con successo, grazie!";
    public final static String PREMI_INDIETRO =" Premi 0 per tornare al menu precedente, indietro.";
    public final static String PREMI_USCIRE=" Premi 0 per uscire.";
    public final static String ERRORE_LOGIN=" Errore durante il login ";
    public final static String MINORENNE= " Il sistema ha rilevato che sei minorenne, non puoi inscriverti.";
    public final static String GRAZIE_PER_ISCRIZIONE=" Grazie per esserti Iscritto ai Servizi di Prestito Temporaneo.";
    public final static String SCADUTA_NON_RINNOVABILE = "Devi reistrarti nuovamente, la tua iscrizione e\' scaduta e non puo\' essere rinnovata.";

    /**
     * Creazione di una stringa COSTANTE
     * @param RICHIESTE_MENU rappresenta le voci del menu che si vuole creare nel momento iniziale di accesso da parte dell'utente: ovvero se iscriversi come nuovo
     * utente oppure, se già in possesso di nome utente e password
     */
    public final static String [] MENU_INIZIALE={" ACCEDI al pannello Admin;",
            " ACCEDI al pannello Utenti;"};
    public final static String [] RICHIESTE_MENU_INIZIALE ={" ISCRIVITI ai servizi di prestito temporaneo se sei un nuovo utente;",
            " AUTENTICAZIONE se sei gia\' un'utente registrato ai servizi di prestito temporaneo;"};

    /**
     * @param RINNOVO rappresenta la richiesta di rinnovo dell'iscrizione ai servizi di prestito temporaneo da sottoporre all'utente.
     */
    public final static String RINNOVO ="Desideri RINNOVARE l'iscrizione ai servizi? premi 0";
    public final static String NOME="Inserisci NOME:";
    public final static String COGNOME="Inserisci COGNOME:";
    public final static String USER_NAME="Inserisci USERNAME:";
    public final static String DATA_NASCITA="Inserisci DATA DI NASCITA:";
    public final static String PASSWORD ="Inserisci PASSWORD:";
    public final static String MONTH ="Inserisci MESE:";
    public final static String DAY ="Inserisci GIORNO:";
    public final static String YEAR ="Inserisci ANNO:";
    public final static String USERNAME_ESISTE="Riprova, hai inserito un USERNAME esistente, scegline uno diverso.\n";
    public final static String [] RICHIESTE_MENU_USER ={
            " Richiedere il PRESTITO di una RISORSA.",
            " Stampa i PRESTITI in atto dall'utente.  ",
            " Richiedi una PROROGA. ",
            " Ricerca una RISORSA. ",
            " Termina il prestito di una RISORSA. "
    };
    /**
     * Stringhe COSTANTI associate all'Admin e Risorse.
     */
    public final static String [] RICHIESTE_MENU_ADMIN ={" VISUALIZZARE l'elenco delle risorse.",
            " AGGIUNGERE una risorsa dall'elenco. ",
            " RIMUOVERE una risorsa dall'elenco. ",
            " Cerca una RISORSA. ",
            " VISUALIZZARE il numero di prestiti per anno solare. ",
            " VISUALIZZARE le proroghe per anno solare. ",
            " VISUALIZZARE quale risorsa e\' stata oggetto del maggior numero di prestiti per anno solare. ",
            " VISUALIZZARE il numero di prestiti per fruitore per anno solare. ",
            "VISUALIZZARE elenco degli utenti."

    };
    public final static String BARCODE =" Inserisci BARCODE: ";
    public final static String GENERE =" Inserisci GENERE: ";
    public final static String AUTORI =" Inserisci AUTORE/I:  ";
    public final static String NUM_PAG =" Inserisci NUMERO DI PAGINE: ";
    public final static String LINGUE =" Inserisci LINGUA/E: ";
    public final static String TITLE =" Inserisci TITOLO: ";
    public final static String DURATA =" Inserisci DURATA (in minuti): ";
    public final static String CATEGORIA =" Che tipo di riorsa vuoi inserire? premi (1)Libro (2)Film \t";
    public final static String AGE_RESTRIC =" Inserisci l'ETA\' minima per la visione: ";
    public final static String CASA_EDIT=" Inserisci la CASA EDITRICE: ";
    public final static String CODICE_PRESTITO =" Inserisci il CODICE PRESTITO:  ";
    public final static String NON_ESISTE_RISORSA =" La risorsa NON e\' presente dell'elenco risorse. ";
    public final static String RISORSA_IMPOSSIBILE_RIMUOVERE=" La RISORSA non ha piu\' copie disponibili. \n NOTA BENE: Rimarra\' salvato nell'archivio.";
    public final static String RISORSA_SCADUTA=" La RISORSA non ha piu\' copie disponibili. \n NOTA BENE: Rimarra\' salvato nell'archivio.";

    /**
     * Stringe COSTANTI associate alla ricerca di Risorse.
     */
    public final static String [] RICHIESTE_MENU_RICERCA= { " Titolo opera;", " Cognome Autore; "," Genere;"," Anno di Pubblicazione opera;"};

    public final static String RICERCA =" Quale parametro vuoi utilizzare per la ricerca della risorsa? ";
    public final static String MG_PRESTITO_NON_DISPONIBILE=" La risorsa cercata non ha copie libere disponibili al prestito!";
    public final static String NON_ESISTE_PRESTITO =" Il prestito NON e\' presente dell'archivio. ";
    public final static String USER_NON_HA_PRESTITI =" NON hai nessuna RISORSA in prestito. ";
    public final static String FINITE_LICENZE_PRESTITO_USER=" Hai terminato le licenze per prendere in prestito una risorsa. Riprova tra qualche giorno.";
    public final static String NO_PROROGA =" NON e\' possibile richidere una proroga per questa RISORSA. ";
    public final static String BOOK= "BOOK";
    public final static String FILM= "FILM";

    /**
     * Stringhe COSTANTI utilizzate per il savataggio degli oggetti su file.
     */
    public final static String  FILE_USER ="user";
    public final static String  FILE_PRESTITO="prestito";
    public final static String  FILE_RESOURCE="risorse";
    public final static String  FILE_ADMIN="admin";


    /**
     * Metodo che permette di stampare il Menu'
     */
    public void stampaMenuSpecifico(String[] richiesteMenu)
    {
        System.out.println(CORNICETTA);
        for (int k=0; k < richiesteMenu.length; k++)
        {
            System.out.println((k+1) + " - " + richiesteMenu[k]);
        }
        System.out.println(CORNICETTA);
    }

    /**
     * Metodo che permette di stampare una singola richiesta con cornicette annesse.
     */
    public static void stampaRichiestaSingola(String richiestaMenu) {
        System.out.println(richiestaMenu);
    }


    /**
     * Metodo per l'acquisizione e visualizzazione della ricerca risorse all'interno del Database.
     */
    public void viewResearchResource() {
            stampaRichiestaSingola(MG_ANCORA + PREMI_INDIETRO);
            stampaMenuSpecifico(RICHIESTE_MENU_RICERCA);
            stampaRichiestaSingola(RICERCA);
    }

    /**
     * Metodo per l'acquisizione e visualizzazione
     */
    public void viewActionAdmin(){
        stampaRichiestaSingola(MG_ANCORA + PREMI_INDIETRO);
        stampaMenuSpecifico(RICHIESTE_MENU_ADMIN);
    }

    public void viewActionUser(){
        stampaRichiestaSingola(MG_ANCORA + PREMI_INDIETRO);
        stampaMenuSpecifico(View.RICHIESTE_MENU_USER);
    }

    public void viewMain(){
        int count=0;
        if(count>0){
            stampaMenuSpecifico(View.MENU_INIZIALE);
            count++;
        }
        else
        {
            stampaRichiestaSingola(MG_ANCORA + PREMI_USCIRE);
            stampaMenuSpecifico(View.MENU_INIZIALE);
        }
    }

    public void viewPannelUser(){
        stampaRichiestaSingola(MG_ANCORA + PREMI_INDIETRO);
        stampaMenuSpecifico(RICHIESTE_MENU_INIZIALE);
    }

    public void viewRenewalRegistrazion(){
        stampaRichiestaSingola(MG_SCADUTA_ISCRIZIONE);
        stampaRichiestaSingola(RINNOVO);
    }

    /**
     * Metodo visualizzazione messaggio d'errore
     */
    public void viewMsgError(){
        stampaRichiestaSingola(MG_ERRORE);
    }

}
