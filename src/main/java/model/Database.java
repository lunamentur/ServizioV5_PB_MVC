package main.java.model;
import main.java.model.library.LibraryResources;

import javax.xml.crypto.Data;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Classe Database che racchiude metodi e gestione del database in cui sono raccolti tutti gli user e oggetti inerenti.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class Database {


    /**
     * Elenco delle variabili utilizzate all'interno del Database.
     */
    private  int i=0;
    private  boolean end= false;
    private  int count=0;
    private  String temp;
    private  LocalDate date=LocalDate.now();
    private  int year;

    /**
     * @param yearLimit Anno limite utilizzato in alcuni metodi. {@link #numberOfPrestitiCalendarYear()}
     */
    private  int yearLimit = date.getYear() - 3;

    /**
     * Lista di user, oggetti di tipo User, con key il nome utente, username di tipo String.
     * Mediante l'utilizzo di tale HashMap siamo in grado di collegare ogni utente (con le relative informazioni anagrafiche) con una key, ovvero username.
     */
     Map<String, User> userList= new HashMap<>();
     Map<String, Admin> adminList= new HashMap<>();

    public  Map<String, User> getUserList() {
        return userList;
    }

    public  Map<String, Admin> getAdminList() {
        return adminList;
    }

    public  Map<Integer, Resource> getResourceList() {
        return resourceList;
    }

    public  Map<String, Prestito> getPrestitoList() {
        return prestitoList;
    }

    public  Map<String, Prestito> getPrestitoListForIteration() {
        return prestitoListForIteration;
    }


/**
     * METODI USER
     */

    /**
     * Medoto che permette l'inserimento di un nuovo user, oggetto di tipo User, all'interno della nostra lista di user, HashMap.
     */
    public  void insertUser(User newuser){
        userList.put(newuser.getUsername(),newuser);
        System.out.println("<+> New user added!");
    }

    /**
     * Medoto che permette l'inserimento di un nuovo admin, oggetto di tipo Admin, all'interno della nostra lista di admin, HashMap.
     */
    public  void insertAdmin(Admin newadmin) {
        adminList.put(newadmin.getUsername(), newadmin);
        System.out.println("<+> New admin added!");
    }

    /**
     * Metodo, di stampa, che permette di visualizzare a video la lista di tutti gli user all'interno del database, l'HashMap.
     */
    public  void listUsers(){
        for (Map.Entry<String, User> user : userList.entrySet()) {
            System.out.println(user.toString());
        }
    }

    /**
     * Metodo che permette all'user di effettuare il login. Verifica che esista il nome utente, username, e controlla che la password sia la medesima.
     * Se il login va a buon fine, ritorna true, l'user ha accesso ai servizi di prestito temporaneo, altrimenti false.
     * @param username {@link User}
     * @param password {@link User}
     * @return false login non riuscito.
     * @return true login riuscito corettamente.
     */

    public  boolean checkLoginIfTrue(String username, String password){
        boolean result = false;
        if(checkIfAdmin(username)) {
            boolean a = adminList.containsKey(username);
            String b = adminList.get(username).getPassword();
            result = a && b.equals(password);

        }
        if(checkIfUser(username)){
            boolean a = userList.containsKey(username);
            String b = userList.get(username).getPassword();
            result = a && b.equals(password);
        }
        return result;
    }

    /**
     * Metodo che permette di verificare se un user e\' presente nel Database.
     * @return true se e\' presente
     * @return false altrimenti
     */
    public  boolean checkIfUser(String username){
        return userList.containsKey(username);
    }
    public  boolean checkIfAdmin(String username){
        return adminList.containsKey(username);
    }

    /**
     * Metodo che ritorna l'user, l'oggetto di tipo User, preso dal Database.
     * @param username
     * @return user
     */

    public  User getUser(String username){
        return userList.get(username);
    }
    public   Admin getAdmin(String username){
        return adminList.get(username);
    }

    /**
     * Metodo di controllo che verifica che l'user, oggetto di tipo User, sia maggiorenne.
     * Pertanto confronta la data di nascita, di tipo LocalDate, con la data attuale, LocalDate.now(), affinche\' sia
     * maggiore o uguale a 18 anni.
     * @param birthDate  Data di nascita dell'user, di tipo LocalDate. E\' richiesto che sia maggiorenne per poter diventare utente dei servizi.
     * @return false se l'user non e\' maggiorenne. (e quindi non ha accesso ai servizi di prestito temporaneo)
     * @return true se l'user e\' maggiorenne.
     */
    public  boolean checkIf18(LocalDate birthDate){
        LocalDate now = LocalDate.now();
        int age = Period.between(birthDate,now).getYears();
        return age >= 18;
    }


    /**
     * METODI RESOURCE
     */

    /**
     * Lista di risorse, oggetti di tipo Resource, con key barcode di tipo Int.
     * Mediante l'utilizzo di tale HashMap siamo in grado di collegare ogni risorsa (con i relativi campi di informazioni) con una key, ovvero il barcode.
     */



    /**
     * Metodo che ritorna la risorsa, oggetto di tipo Resource, preso dal Database.
     * @param barcode codice a barre univoco associato a una risorsa.
     * @return Resource
     */
    public  Resource getResource(int barcode){
        return getResourceList().get(barcode);
    }

    /**
     * Metodo che permette di verificare se una risorsa e\' presente nel Database
     * @return true se la risorsa e\' presente, altrimenti false.
     */
    public  boolean checkIfResource(int barcode){
        return getResourceList().containsKey(barcode);
    }

    /**
     * Medoto che permette l'inserimento di una nuova, oggetto di tipo Resource, all'interno della nostra lista di risorse, HashMap.
     */
    public  void insertResource(Resource newRes){
        resourceList.put(newRes.getBarcode(), newRes);
        System.out.println("<+> New Resource added!");
    }

    /**
     * Metodo, di stampa, che permette di visualizzare a video la lista di tutte le risorse di una specifica categoria all'interno del database, l'HashMap.
     * Infatti tramite il parametro type e\' possibile scegliere il tipo di categoria da voler stampare.
     * @param type {@link Resource}
     */
    public  void printSpecificResource(String type){
        resourceList.forEach((k,v)-> {
            Resource res = resourceList.get(k);
            if(res.getType().equalsIgnoreCase(type)){
                System.out.println(res.toStringRes());
            }
        });
    }


    /**
     * METODI RESOURCE CHECK
     */

    /**
     * Metodo che permette di verificare se una risorsa all'interno del Database e\' un book o un film, in base alla stringa data.
     * {@link Resource}
     * @return true se la risorsa e\' book, altrimenti un film con false.
     */
    public  boolean checkType(int barcode, String type){
        return getResource(barcode).getType().equals(type);
    }


    /**
     * Metodo che controlla di che tipo &egrave; la risorsa e restituisce il numero dell'indice corrispondente legato all'utente.
     * Ovvero se un BOOK=0 e se FILM=1
     */
    public  int choiceTypeResource(int barcode) {
        if (checkType(barcode, Constant.BOOK)) {
            return 0;
        } else return 1;
    }

    /**
     * METODI PER LICENZE
     */

    /**
     * Metodo che incrementa il numero di copie disponibili al prestito di una risorsa oppure il numero di licenze di una risorsa,
     * ovvero il numero di copie attualmente in prestito.
     * @param number se 0 e\' associato al numero di copie, se 1 al numero di risorse in prestito. {@link Resource}
     */
    public  void incrementCopyOrLicenze(int barcode, int number){
        Integer[] copie= getResource(barcode).getLicense();
        copie[number]++;
        getResource(barcode).setLicense(copie);
    }

    /**
     * Metodo che decrementa il numero di licenze di una risorsa, ovvero il numero di copie attualmente in prestito oppure il numero di copie.
     * Se modifico il numero di licenze e\' perche\' il prestito e\' scaduto o terminato, mentre invece se modifico
     * il numero di copie e\' perche\' una copia della risorsa e\' stata eliminata dal database.
     * @param number {@link #incrementCopyOrLicenze(int, int)}
     */
    public  void decrementCopyOrLicenze(int barcode, int number){
        Integer[] copie= getResource(barcode).getLicense();
        copie[number]--;
        getResource(barcode).setLicense(copie);
    }

    /**
     * Metodo che aumenta il numero di licenze di un utente, ovvero il numero di risorse che può prendere in prestito.
     * Il numero passato corrisponde alla posizione all'interno della lista di licenze da modificare, ovvero
     * corrisponde al numero di licenze di ogni singola tipologia di risorsa che l'utente puo\' usufruire.
     * @see User
     */
    public  void incrementLicenzeUser(String username, int number){
        Integer [] value= getUser(username).getBorrowed();
        value[number]++;
        getUser(username).setBorrowed(value);
    }

    /**
     * Metodo che decrementa il numero di licenze di un utente, ovvero il numero di risorse che può prendere in prestito.
     * Il numero passato corrisponde alla posizione all'interno della lista di licenze da modificare, ovvero
     * corrisponde al numero di licenze di ogni singola tipologia di risorsa che l'utente puo\' usufruire.
     * @see User
     */
    public  void decrementLicenzeUser(String username, int number){
        Integer [] value= getUser(username).getBorrowed();
        value[number]--;
        getUser(username).setBorrowed(value);
    }


    /**
     * METODI REGISTRAZIONE PRESTITI
     */

    /**
     * Lista di prestiti, oggetti di tipo Prestito, con key codePrestito di tipo Int.
     * Mediante l'utilizzo di tale HashMap siamo in grado di collegare ogni prestito (con i relativi campi di informazioni) con una key univoca e primaria.
     */
     Map< String, Prestito> prestitoList= new HashMap<>();

     Map<String, Prestito> prestitoListForIteration = new HashMap<>();

     Map<Integer, Resource> resourceList= new HashMap<>();



    /**
     * Medoto che permette l'inserimento nell'archivio dei prestiti del prestito di una risorsa Book da parte di un User.
     * l'identificatore di tale prestito e\' un codice univoco e inoltre viene tenuta traccia della data di inizio prestito e fine.
     * {@link Prestito}
     */
    public  void insertPrestito(Prestito prestito){
        prestitoList.put(prestito.getCodePrestito(), prestito);
        System.out.println("<+> New loan added!");
    }

    /**
     * Metodo che permette di "rimuovere" un prestito dall'elenco di prestiti, una volta terminato o scaduto.
     * Se e\' terminato, a quel punto viene messo in off, modificando la variabile boolean contenuta al suo interno.
     * Inoltre viene anche aggiornato il valore delle licenze della risorsa.
     * {@link Prestito}
     * {@link Resource}
     */
    public  void removeAutomaticPrestito(){
        /**
         * Per ogni singolo prestito presente nell'hashmap dei prestiti cerca quelli scaduti.
         */
        prestitoListForIteration.putAll(prestitoList);
        for (Map.Entry<String, Prestito> entry : prestitoListForIteration.entrySet()) {
            temp = entry.getKey();
            /**
             * Se sono scaduti allora vengono eliminati in automatico dal sistema.
             */
            if (checkIfPrestitoScaduto(temp)) {
                i=entry.getValue().getBarcode();
                String name= entry.getValue().getUsername();
                /**
                 * Viene aggiornato il valore delle licenze della risorsa,
                 * percio\' decrementiamo il varole in posizione 1.
                 * Faccio la stessa cosa per le licenze dell'utente.
                 * Modifico la variabile boolean on_off del prestito rendendola false.
                 */
                removePrestito(i,name,temp);
            }
        }
    }

    /**
     * Metodo che permette di eliminare un prestito dall'elenco dei prestiti, decrementando anche le
     * licenze dell'user e della risorsa relativi.
     * @param barcode {@link Book}
     * @param username {@link User}
     * @param codicePrestito {@link Prestito}
     * {@link #decrementCopyOrLicenze(int, int)} and {@link #decrementLicenzeUser(String, int)} and {@link #choiceTypeResource(int)}
     */
    public  void removePrestito(int barcode, String username, String codicePrestito){
        decrementCopyOrLicenze(barcode,1);
        int type = choiceTypeResource(barcode);
        decrementLicenzeUser(username, type);
        prestitoList.get(codicePrestito).setOn_off(false);
    }

    /**
     * Metodo che permette di verificare se un prestito e\' presente e attivo nel Database.
     * @return true se il prestito e\' presente e se e\' attivo, altrimenti se e\' scaduto o non e\' presente ritorna false.
     * {@link Prestito}
     */
    public  boolean checkIfPrestito(String codePrestito) {
        return prestitoList.containsKey(codePrestito) && prestitoList.get(codePrestito).getOn_off();
    }

    /**
     * Metodo che permette di verificare se un prestito e\' scaduto, ovvero se la data di scadenza viene prima di quella del giorno in cui avviene la verifica.
     * @return true se il prestito e\' scaduto, altrimenti false.
     * {@link Prestito}
     */
    private  boolean checkIfPrestitoScaduto(String codePrestito) {
        LocalDate dataNow= LocalDate.now();
        LocalDate dataScadenza= getPrestito(codePrestito).getDataScadenza();
        return dataNow.isAfter(dataScadenza);
    }

    /**
     * Metodo che ritorna il prestito, oggetto di tipo Prestito, preso dal Database.
     * @param codePrestito codice univoco associato a un prestito tra risorsa e utente.
     * @return oggetto di tipo Prestito {@link Prestito}.
     */
    public  Prestito getPrestito(String codePrestito){
        return prestitoList.get(codePrestito);
    }

    //DA DIVIDERE E METTERE NELLA VIEW E CONTROLLER
    /**
     * Metodo che permette di visualizzare i prestiti attivi di un utente.
     * Controllo percio\' che il prestito sia attivo, ovvero l'etichetta on_off sia true, altrimenti se false significa che e\' scaduto.
     */
    public  void printActivePrestitiUser(String username){
        int count=0;
        for (Map.Entry<String, Prestito> entry : prestitoList.entrySet()) {
            if(entry.getValue().getUsername().equals(username) && entry.getValue().getOn_off()){
                System.out.println("Idprestito: "+entry.getValue().getCodePrestito()+ "\t Risorsa: "+resourceList.get(entry.getValue().getBarcode()).getTitle());
            } else count++;
        }
        /**
         * se non ha nessun prestito attivo allora viene stampato a video un messaggio di avviso.
         */
        if(count== prestitoList.size()){
            System.out.println(Constant.USER_NON_HA_PRESTITI);
        }
    }

    /**
     * Metodo che permette di controllare se un utente ha almeno un prestito attivo.
     * @param username {@link User}
     * @return true se ha almeno un prestito attivo, altrimenti nessuno.
     */
    public  boolean userHavePrestito(String username) {
        for (Map.Entry<String, Prestito> entry : prestitoList.entrySet()) {
            if (entry.getValue().getUsername().equals(username) && entry.getValue().getOn_off()) return true;
        }
        return false;
    }

    /**
     * METODI PER NUMERO DI PRESTITI PER ANNO SOLARE E USER.
     */

    //BISOGNEREBBE LA STAMPA METTERLA NELLA VIEW E UN PEZZO DEL CONTROLLER
    /**
     * Metodo che permette di visualizzare a video il numero di prestiti per anno solare da parte di un utente.
     * Viene utilizzata inizialmente la lista di user per ricercare nella lista prestiti il nome utente.
     * Successivamente viene fattao scorrere l'archivio partendo dalla data dell'interrogazione fino a quella di qualche anno fa.
     */
    public  void numberOfPrestitiUserCalendarYear(){
        year=date.getYear();
        boolean end = false;
        do {
            System.out.println(year + " : ");
            for (Map.Entry<String, User> entryName : userList.entrySet()) {
                String name = entryName.getKey();
                count=0;
                for (Map.Entry<String, Prestito> entryYear: prestitoList.entrySet()) {
                    if(entryYear.getValue().getDataInizio().getYear()==year && name.equals(entryYear.getValue().getUsername())) count++;
                }
                System.out.println("\t"+name + " : " + count);
            }
            if(year< yearLimit) end=true;
            else year--;
        }while (!end);
    }

    /**
     * Metodo che permette di visualizzare a video il numero di prestiti per anno solare.
     * Percio\' faccio scorrere l'archivio partendo dalla data dell'interrogazione fino a quella di qualche anno fa.
     */
    public  void numberOfPrestitiCalendarYear(){
        year=date.getYear();
        boolean end = false;
        do {
            prestitiCalendarYear(year);
            /**
             * Se l'anno con cui sto effettuando la ricerca e\' minore dell'anno corrente meno una quantita\' allora esco dal ciclo do-while.
             * Altrimenti continuo a ciclare stampando il numero di prestiti diminuendo l'anno della ricerca.
             * {@link #yearLimit}
             */
            if(year< yearLimit) end=true;
            else year--;
        }while (!end);
    }

    /**
     * Metodo che permette di contare tutti i prestiti in un dato anno, passato come valore.
     * @param year anno di cui si vuole visualizzare il numero di prestiti.
     */
    public  void prestitiCalendarYear(int year){
        count=0;
        for (Map.Entry<String, Prestito> entry : prestitoList.entrySet()) {
            /**
             * se la data di inizio del prestito e\' uguale a quella di quest'anno allora incremento la variabile che tiene traccia del numero dei prestiti.
             */
            if(entry.getValue().getDataInizio().getYear()==year) count++;
        }
        System.out.println(year + " : " + count);
    }

    //BISOGNEREBBE LA STAMPA METTERLA NELLA VIEW E UN PEZZO DEL CONTROLLER
    /**
     * Metodo che restituisce quale risorsa, oggetto di tipo Resource, ha avuto il maggior numero di prestiti per anno solare.
     */
    public  void resourceCalendarYear(){
        year=date.getYear();
        boolean end = false;
        do {
            /**
             * cicla all'interno delle due hashmap, alla ricerca della risorsa col maggior numero di prestiti.
             * svolge una semplice operazione di massimo, ovvero se il count precedente &egrave; maggiore di quello attuale,
             * sostituisce il vecchio valore max con il count e continua fino alla fine delle risorse.
             */
            int max = 0;
            int resourceMax = 0;
            System.out.println(year + " : ");
            for (Map.Entry<Integer, Resource> entryResource : resourceList.entrySet()) {
                int resource = entryResource.getKey();
                count=0;
                for (Map.Entry<String, Prestito> entryYear: prestitoList.entrySet()) {
                    if(entryYear.getValue().getDataInizio().getYear()==year && (resource==entryYear.getValue().getBarcode())) count++;
                }
                if(count>=max){
                    max=count;
                    resourceMax=entryResource.getKey();
                }
            }
            /**
             * se non ci sono stati prestiti durante l'anno solare allora visualizza a video un messaggio di errore, altrimenti le informazioni della risorsa.
             */
            if (max != 0) {
                System.out.println("\t" + getResource(resourceMax).getBarcode() + ", " + getResource(resourceMax).getTitle() + ", " + getResource(resourceMax).getAuthor() + "," + " : " + max);
            } else System.out.println("*** Non sono stati effettuati prestiti durante l'anno corrente ***");
            if(year< yearLimit) end=true;
            else year--;
        }while (!end);
    }

    /**
     * Metodo che permette di ottenere il numero di proroghe per anno solare.
     */
    public  void prorogaCalendarYear(){
        year=date.getYear();
        boolean end = false;
        do {
            count=0;
            for (Map.Entry<String, Prestito> entryYear: prestitoList.entrySet()) {
                if(entryYear.getValue().getDataInizio().getYear()==year) count= count + entryYear.getValue().getNumProroghe();
            }
            System.out.println( year + " : " + count);
            if(year< yearLimit) end=true;
            else year--;
        }while (!end);
    }


    /**
     * METODI PER LA RICERCA DI RISORSE, UTILIZZATA DALL'USER E DALL'ADMIN.
     */

    //BISOGNEREBBE LA STAMPA METTERLA NELLA VIEW E UN PEZZO DEL CONTROLLER
    /**
     * Metodo per la ricerca di una risorsa tramite dei parametri generici.
     * Se la stringa viene trovata allora vengono stampati a video alcuni parametri essenziali della risorsa cercata.
     * @param type stringa che permette di selezionare il tipo di ricerca da svolgere.
     * @param txt stringa che deve essere ricercata all'interno dell'archivio delle risorse.
     */
    public  boolean searchGeneral(String type,String txt){
        count=0;
        for(Resource resource : getResourceList().values()){
            switch(type) {
                case "title":
                    if(resource.getTitle().equalsIgnoreCase(txt)){
                        System.out.println(resource.toString());
                    }else{
                        count++;
                    }
                    break;

                case "author":
                    boolean k=false;
                    for(int i=0; i<resource.getAuthor().size(); i++) {
                        if(resource.getAuthor().get(i).toLowerCase().contains(txt.toLowerCase())){
                            k=true;
                        }
                    }
                    if(k){
                        System.out.println(resource.toString());
                    }else{
                        count++;                    }
                    break;

                case "genre":
                    if(resource.getGenre().equals(txt)){
                        System.out.println(resource.toString());
                    }else{
                        count++;                    }
                    break;

                case "year":
                    Integer year = Integer.valueOf(txt);
                    if(resource.getYearPub() == year){
                        System.out.println(resource.toString());
                    }else{
                        count++;
                    }
                    break;
            }
        }
        boolean constant;
        constant= (count!=resourceList.size());
        return constant;
    }

    /**
     * INIZIALIZZAZIONE DI OGGETTI PREDEFINITI
     */


    /**
     * Creazione di oggetti preimpostati.
     */


    /**
     * Metodo che consente di salvare le HashMap in modo generico.
     * @param name nome che vogliamo dare al file in cui salviamo i dati.
     */
    public  void saveAllHash(String name){

        File file = new File(name +".dat");
        file.delete();
        //il name lo trovi in view.FILE_ e ci sono tutti.
        Object hashmap = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            //permettere di utilizzare anche gli altri map, deve essere generale.
            switch(name) {
                case "user":
                    hashmap = userList;
                    break;
                case "prestito":
                    hashmap = prestitoList;
                    break;
                case "risorse":
                    hashmap = resourceList;
                    break;
                case "admin":
                    hashmap = adminList;
                default:
            }
            oos.writeObject(hashmap);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che permette di leggere dato un file.dat gli oggetti contenuti al suo interno, importandoli nel progetto.
     *
     * @param name il nome del file che si vuole importare\leggere.
     * @return true se l'azione &egrave; avvenuta con successo, altrimenti false.
     */
    public  boolean readAllHash(String name) {
        try {
            File file = new File(name + ".dat");
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(name + ".dat");
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);

                Object obj = objectIn.readObject();
                switch (name) {
                    case "user":
                        userList = (Map<String, User>) obj;
                        break;
                    case "prestito":
                        prestitoList = (Map<String, Prestito>) obj;
                        break;
                    case "risorse":
                        resourceList = (Map<Integer, Resource>) obj;
                        break;
                    case "admin":
                        adminList = (Map<String, Admin>) obj;
                    default:
                }
                objectIn.close();
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}