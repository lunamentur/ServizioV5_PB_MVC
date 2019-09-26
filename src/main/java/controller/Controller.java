package main.java.controller;
import main.java.model.*;
import main.java.model.library.LibraryGeneral;
import main.java.view.*;

public class Controller {
    private View view;
    private Database database;
    //private User user;
    private String username;
    //private Admin admin;

    public Controller() {
        view=new View();
        database=new Database();
        username= new String();
    }
    public void init(){

    }
    public void login(){


    }
    public void menuStandard(){

    }
    public void menuAdmin(Admin admin){
        //metodo menù

    }
    public void menuUser(User user){

    }

    public void researchResource(){
        boolean end= false;
        boolean exist= true;
        do{
            view.viewResearchResource();
            int choice = LibraryGeneral.readInt();
            String searchString = LibraryGeneral.insertString();
            if (choice == 0) end = true;
            switch(choice)
            {
                /**
                 * Ricerca per Titolo opera.
                 */
                case 1:
                    exist= Database.searchGeneral("title", searchString);

                    break;
                /**
                 * Ricerca per Autore.
                 */
                case 2:
                    exist=Database.searchGeneral("author", searchString);

                    break;
                /**
                 * Ricerca per Genere.
                 */
                case 3:
                    exist=Database.searchGeneral("genre", searchString);

                    break;

                /**
                 * Ricerca per Anno di Pubblicazione.
                 */
                case 4:
                   exist= Database.searchGeneral("year", searchString);
                    break;

                /**
                 * Fine della stampa delle richieste del Menu.
                 */
                case 0:
                    end = true ;
                    break;

                /**
                 * Errore, l'inserimento non è corretto
                 */
                default:
                    System.out.println(View.MG_ERRORE);
                    break;
            }
            /**
             * se nessuna risorsa viene trovata allora compare il messaggio di avviso.
             */
            if (!exist) //se true diventa false, ovvero se sono uguali significa che non c'e risorsa
                view.stampaRichiestaSingola(View.NON_ESISTE_RISORSA);
        }while(!end);
    }
}
