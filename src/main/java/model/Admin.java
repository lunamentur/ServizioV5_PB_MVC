package main.java.model;

import java.io.Serializable;

/**
 * Classe Admin, ovvero identifica l'operatore della gestione del servizio prestiti temporaneo e un oggetto di tipo User deriva da questa clase.
 *
 * @author Resa Kassame, Simona Ramazzotti
 * @version 5
 */
public class Admin implements Serializable {
    private String password, username;
    public Admin(String username, String  password){
        this.password=password;
        this.username=username;
    }

    /**
     * GET and SET
     */

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
