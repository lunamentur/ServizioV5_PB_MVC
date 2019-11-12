package main.java.model.library;

import main.java.model.Database;
import main.java.model.User;

public interface LibraryControllerInterface {
        /**
         * Metodo che permette il rinnovo dell'iscrizione dell'user se scaduta (o in procinto di scadere).
         * Se si decide di rinnovare l'iscrizione allora viene incrementata l'etichetta del numero del rinnovo (utile per le infomarzioni storiche)
         * @param user {@link User}
         */
        public void renewalRegistration(User user);


        /**
         * Metodo che controlla che l'iscrizione dell'user sia scaduta o nel range impostato
         per il rinnovo anticipato.
         * @return true se l'iscrizione dell'user è scaduta, quindi può essere rinnovata. * @return false se l'iscrizione dell'user non è scaduta e non è nel range dei
        giorni di rinnovo. */
        public boolean isRenewal(User user);

        /**
         * Metodo che controlla che l'iscrizione dell'user sia scaduta. *
         * @return false se l'iscrizione dell'user non è scaduta.
         */
        public boolean isExpired(User user);
        /**
         * Metodo che se l'utente ha l'iscrizione scaduta, lo contrassegna come scaduto
         tramite il name e non la chiave primaria username.
         * l'utente sarà costretto registrarsi nuovamente. */
        public void userExpired(User user);

        /**
         * Data corretta per l'inserimento. */
        public boolean trueDate(int month, int day);

        /**
         * Metodo che permette di verificare se la risorsa &egrave; presente all'interno del
         database tramite
         * un metodo di quella classe. */
        public boolean checkIfExist(int barcode);
        /**
         * Metodo che controlla se un prestito ha i requisiti per effettuare la proroga,
         * ovvero se la data in cui viene interpellato il sistema &egrave; in un intervallo
         di 3 giorni prima della data di Scadenza. */
        public boolean checkIfProroga(String codePrestito);


        public boolean checkProrogaPrestito(String codePrestito);

        public void prorogaPrestito(String codePrestito);

        /**
         * Metodo che permette di aumentare il numero di proroghe fatte dall'user su una
         spesicifica risorsa. */
        public void incrementNumProroga(String codePrestito);

        /**
         * Metodo genera il codice del prestito richiesto dall'utente di una risorsa. */
        public String generateId(String username, int barcode);
        /**
         * Metodo che controlla se la risorsa &egrave libera. */
        public boolean checkIfResourceFree(int barcode);

    public boolean checkBorrowed(User user, int number);

    }
