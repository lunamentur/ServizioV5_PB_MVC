package main.java.test;
import main.java.model.Database;
import main.java.model.User;
import main.java.controller.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControllerOperatorsTest {
    private Integer[] license_book1 = {3, 2};
    private Integer[] borrowed1 = {2, 0};
    private Integer[] license_film1 = {3, 1};
    private User user1 = new User("test", "test", "test1", "test1", LocalDate.of(1996, 01, 01), LocalDate.of(2017, 1, 1), borrowed1);
    private User user_minorenne = new User("minore", "minore", "minorenne", "test1", LocalDate.of(2012, 01, 01), LocalDate.of(2019, 1, 1), borrowed1);
    private Database db = new Database();
    private ControllerOperators co= new ControllerOperators(db);

    public void setUp(){
        // codice comune a tutti i test
        db.getUserList().put(user1.getUsername(), user1);
    }

    @Test
    void testCheckLogin() {
        setUp();
        boolean result = db.checkLoginIfTrue(user1.getUsername(),"test1");
        assertEquals(true,result,"Login non effettuato");
    }

    @Test
    void testRegistrationProcess() {
        setUp();
        //l'user maggiorenne, true registrato
        assertEquals(true, db.checkIf18(user1.getBirthDate()));
        //user minorenne, false non registrato
        //assertTrue(db.checkIf18(user_minorenne.getBirthDate())),false;
    }
    @Test
    void testProrogaPrestito() {

    }
    @Test
    void testIncrementNumProroga() {
    }
}