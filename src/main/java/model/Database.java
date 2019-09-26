package main.java.model;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Classe Database che racchiude metodi e gestione del database in cui sono raccolti tutti gli user e oggetti inerenti.
 * @author Reda Kassame, Simona Ramazzotti
 */

public class Database {
    private  int i=0;
    private  boolean end= false;
    private  int count=0;
    private  String temp;
    private  LocalDate date=LocalDate.now();
    private  int year;

    Map<String, User> userList = new HashMap<>();
    Map<String, Admin> adminList = new HashMap<>();


    ///////////////////////////metodi utente
    public void insertUser(User newuser){
        userList.put(newuser.getUsername(), newuser);
    }
    public void listUsers(){
        System.out.println(userList);
    }
    public boolean checkIfUser(String username){
        return userList.containsKey(username);
    }
    public boolean checkIfAdmin(String username){
        return adminList.containsKey(username);
    }
    public boolean checkIfLoginTrue(String username, String password){
        boolean result= false;
        if (checkIfAdmin(username)){
            boolean a = adminList.containsKey(username);
            String b = adminList.get(username).getPassword();
            if(a && b.equals(password)){
                result = true;
            }else{
                result = false;
            }
        }
        return result;
    }
    public User getUser(String username){
        return userList.get(username);
    }
    public Admin getAdmin(String username){
        return adminList.get(username);
    }
    ///////////////////////////metodi rerources
    Map<Integer, Resource> resourceList = new HashMap<>();

    public Resource getResource(int barcode) {
        return resourceList.get(barcode);
    }
    public boolean checkIfResource(int barcode){
        return resourceList.containsKey(barcode);
    }
    public void insertResource(Resource res){
        resourceList.put(res.getBarcode(), res);
    }
    public String printSpecificResource(String type){
        String output = null;
        resourceList.forEach((k,v)-> {
            Resource res = resourceList.get(k);
            if(res.getType().equalsIgnoreCase(type)){
                output.concat(res.getBarcode() + "" + res.getAuthor() + "" + "" + res.getTitle());
            }
        });
        return output;
    }
    public  void removeResource(int barcode){
        if(checkIfResource(barcode)) {
            Integer[] copie = getResource(barcode).getLicense();
            /**
             * Se il numero di copie e quello delle copie in prestito e\' uguale allora le copie
             * sono tutte in prestito, quindi non e\' possibile effettuare la rimozione fittizia.
             */
            if(copie[0]!=copie[1]){
                /**
                 * Se c'e\' una o piu\' di una copia della risorsa la decrementa e controlla che sia a zero.
                 * Altrimenti e\' nulla percio\' compare un messaggio di avviso.
                 */
                if(copie[0] >= 1){
                    decrementCopyOrLicenze(barcode,0 );
                    System.out.println(Constant.MG_AZIONE_SUCCESSO);
                    if(copie[0]==0) System.out.println(Constant.RISORSA_SCADUTA);
                }else{
                    //ovvero uguale a zero (perche\' <1)
                    System.out.println(Constant.RISORSA_SCADUTA);
                }
            }else System.out.println(Constant.RISORSA_IMPOSSIBILE_RIMUOVERE);
        }else {
            Constant.stampaRichiestaSingola(Constant.NON_ESISTE_RISORSA);
        }
    }
    public  boolean checkType(int barcode, String type){
        return getResource(barcode).getType().equals(type);
    }
    public void incrementCopyOrLicenze(int barcode, int number){
        Integer[] copie = getResource(barcode).getLicense();
        copie[number]++;
        getResource(barcode).setLicense(copie);
    }
    public void decrementCopyOrLicenze(int barcode, int number){
        Integer[] copie = getResource(barcode).getLicense();
        copie[number]--;
        getResource(barcode).setLicense(copie);
    }
    public  void incrementLicenzeUser(String username, int number){
        Integer [] value= getUser(username).getBorrowed();
        value[number]++;
        getUser(username).setBorrowed(value);
    }
    public  void decrementLicenzeUser(String username, int number){
        Integer [] value= getUser(username).getBorrowed();
        value[number]--;
        getUser(username).setBorrowed(value);
    }
    ///////////////////////////metodi prestiti
    Map<String, Prestito> prestitoList= new HashMap<>();

    public void insertPrestito(Prestito prestito){
        prestitoList.put(prestito.getCodePrestito(),prestito);
    }
    public void removeAutomaticPrestito(){
        /**
         * Per ogni singolo prestito presente nell'hashmap dei prestiti cerca quelli scaduti.
         */
        for (Map.Entry<String, Prestito> entry : prestitoList.entrySet()) {
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
    public int choiceTypeResource(int barcode) {
        if (checkType(barcode, Constant.BOOK)) {
            return 0;
        } else return 1;
    }
    public void removePrestito(int barcode, String username, String codicePrestito){
        decrementCopyOrLicenze(barcode,1);
        int type = choiceTypeResource(barcode);
        decrementLicenzeUser(username, type);
        prestitoList.get(codicePrestito).setOn_off(false);
    }
    public  boolean checkIfPrestito(String codePrestito) {
        if(prestitoList.containsKey(codePrestito) && prestitoList.get(codePrestito).getOn_off()) return true;
        else return false;
    }
    public boolean checkIfPrestitoScaduto(String codePrestito) {
        LocalDate dataNow = LocalDate.now();
        LocalDate dataScadenza = getPrestito(codePrestito).getDataScadenza();
        if(dataNow.isAfter(dataScadenza)){
            return true;
        }else{
            return false;
        }
    }
    public Prestito getPrestito(String codePrestito){
        return prestitoList.get(codePrestito);
    }
    public void printActivePrestitiUser(String username){
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

        /**
         * Metodo per la ricerca di una risorsa tramite dei parametri generici.
         * Se la stringa viene trovata allora vengono stampati a video alcuni parametri essenziali della risorsa cercata.
         * @param type stringa che permette di selezionare il tipo di ricerca da svolgere.
         * @param txt stringa che deve essere ricercata all'interno dell'archivio delle risorse.
         */
        public boolean searchGeneral(String txt String type){
            count=0;
            for(Resource resource : resourceList.values()){
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
            constant= (count==resourceList.size());
            return constant;
        }




}