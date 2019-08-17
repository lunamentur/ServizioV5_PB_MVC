package main.java;
import main.java.controller.Controller;
import main.java.model.Database;
public class Main {

    public static void main(String args[]) {
        /**
         * Inizializzazione di oggetti predefiniti.
         */
        if(!Database.readAllHash(View.FILE_USER) || !Database.readAllHash(View.FILE_ADMIN) || !Database.readAllHash(View.FILE_PRESTITO) || !Database.readAllHash(View.FILE_RESOURCE)) {
            Database.initAllObject();
        }
        /**
         * Una volta aperto il database elimina in automatico i prestiti scaduti/terminati.
         */
        Database.removeAutomaticPrestito();

        /**
         * Controller
         */
        Controller controller= new Controller();
        controller.init();
    }

}
