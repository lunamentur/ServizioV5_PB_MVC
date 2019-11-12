package main.java.test;
import main.java.model.Database;
import main.java.model.User;
import main.java.model.library.*;
import main.java.model.library.LibraryOperators;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LibraryOperatorsTest{
    private Database db=new Database();
    private LibraryControllerInterface library = new LibraryOperators(db);
    private Integer[] borrowed1 = {2, 0};
    private User user1 = new User("test", "test", "test1", "test1", LocalDate.of(1996, 01, 01), LocalDate.of(2019, 1, 1), borrowed1);

    @Test
    void testRenewalRegistration() {
        library.renewalRegistration(user1);
        assertEquals(1, user1.getNumRenewal());
    }
}
