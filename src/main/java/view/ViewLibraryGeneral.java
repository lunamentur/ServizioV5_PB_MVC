package main.java.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe intermedia che permette l'interazione tra la classe Admin-User, Resource e la classe Database.
 * I metodi presenti sono di inserimento e acquisizione da tastiera, generali a qualsiasi interazione.
 * @author Reda Kassame, Simona Ramazzotti
 * @version 5
 */
public class ViewLibraryGeneral {

    /**
     * Variabili interne utilizzate dalla classe.
     */
    private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    private static int number, year, barcode;
    private static String string;


    /**
     * Metodi di controllo dell'inserimento da tastiera di numeri di tipo interi.
     * @return numeroInserito numero valido inserito da tastiera.
     */
    public static int readInt (){
        Scanner readInt = new Scanner(System.in);
        int numeroInserito;
        do {
            while (!readInt.hasNextInt()) {
                String input = readInt.next();
                System.out.println(View.MG_ERRORE);
            }
            numeroInserito = readInt.nextInt();
        } while (numeroInserito < 0 && !(String.valueOf(numeroInserito).equals(null)));
        return numeroInserito;
    }

    /**
     * Metodi di controllo dell'inserimento da tastiera di numeri di tipo interi se compreso fra due valori.
     * @return numeroInserito numero valido inserito da tastiera, altrimenti cicla.
     */
    public static int readIntChoise(int num1, int num2){
        boolean end = false;
        int numeroInserito =0;
        do{
            numeroInserito =readInt();
            if(numeroInserito==num1 || numeroInserito==num2){
                end = true;
            } else System.out.println(View.MG_ERRORE);
        }while(!end);
        return numeroInserito;
    }



    /**
     * Metodo di controllo lettura da tastiera di una stringa.
     * @return string tipo String che contiene la stringa inserita da tastiera dall'user.
     */
    public static String readString() throws Exception{
        String string = read.readLine();
        return string;
    }

    /**
     * Metodo di controllo lettura da tastiera di una stringa, possibilmente non vuota. Richiama il metodo readString().
     * @return string tipo String che contiene la stringa inserita da tastiera dall'user.
     */
    public static String readStringNotNull()
    {
        boolean end = false;
        String string = null;

        do
        {
            try {
                string = readString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (string.length() > 0){
                end = true;
            } else System.out.println(View.MG_ERRORE);
        }
        while(!end);
        return string;
    }

    /**
     * Metodo che permette di inserire un numero che non sia più lungo di un vincolo dato.
     * Come il barcode che deve essere un numero con 6 o più cifre.
     * @param vincolo numero che definisce la lunghezza che deve avere il numero inserito da tastiera.
     * @return {@link #barcode}
     */
    public static int insertBarcode(int vincolo){
        boolean end = false;
        View.stampaRichiestaSingola(View.BARCODE);
        while(!end){
            barcode= readInt();
            if(String.valueOf(barcode).length()>=vincolo){
                end=true;
            }
            else {
                System.out.println(View.MG_ERRORE);
            }
        }
        return barcode;
    }

    /**
     * Metodo che permette di inserire un numero, senza vincoli di lunghezza. Come il numero di pagine.
     * @param tipoInserimento stringa che permette di generalizzare il metodo di inserimento, stampandola a video.
     * @return number ovvero un numero ricevuto da tastiera.
     */
    public static int insertNum(String tipoInserimento){
        View.stampaRichiestaSingola(tipoInserimento);
        number= readInt();
        return number;

    }

    /**
     * Metodo che permette di inserire numeri e controllare se rispettano la lunghezza. Come nel caso dell'anno, deve avere 4 cifre.
     * @param tipoInserimento stringa che permette di generalizzare il metodo di inserimento, stampandola a video.
     * @param vincolo numero che definisce la lunghezza che deve avere il numero inserito da tastiera.
     * @return number ovvero un numero ricevuto da tastiera.
     */
    public static int insertNumberEqual(String tipoInserimento, int vincolo){
        boolean end= false;
        View.stampaRichiestaSingola(tipoInserimento);
        while(!end){
            number= readInt();
            if(String.valueOf(number).length()==vincolo)
            {
                end=true;
            }
            else {
                System.out.println(View.MG_ERRORE);
            }
        }
        return number;
    }


    /**
     * Metodo che permette di inserire le lingue e gli autori. Uno o più.
     * @param tipoInserimento stringa che permette di generalizzare il metodo di inserimento, stampandola a video.
     * @return list ovvero la lista di stringhe.
     */
    public static List<String> insertList(String tipoInserimento){
        boolean end= false;
        List<String> list = new ArrayList<String>();
        View.stampaRichiestaSingola(tipoInserimento);
        //inserisce al primo giro almeno un autore o una lingua
        try {
            string= readStringNotNull();
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.add(string);
        while(!end){
            View.stampaRichiestaSingola(tipoInserimento + "Altrimenti premi 0 per uscire.");
            try {
                string= readStringNotNull();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(string.equals("0")) end= true;
            else list.add(string);
        }
        return list;
    }

    /**
     * Metodo che permette di acquisire da tastiera una stringa, purche\' non sia vuota.
     * @param tipoInserimento stringa che permette di generalizzare il metodo di inserimento, stampandola a video.
     * @return stringa di caratteri.
     */
    public static String insertString(String tipoInserimento) {
        View.stampaRichiestaSingola(tipoInserimento);
        try {
            string = readStringNotNull();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }


}
