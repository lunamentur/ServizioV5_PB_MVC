package main.java.test;
import main.java.model.*;
import main.java.model.library.*;
import main.java.controller.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class DatabaseTest{

    /**
     * User
     */
    private Database db=new Database();
    private Integer[] borrowed1 = {2, 0};
    private User user1 = new User("test", "test", "test1", "test1", LocalDate.of(1996, 01, 01), LocalDate.of(2019, 1, 1), borrowed1);




    /**
     * Risorse
     */
    List<String> langues_test = new ArrayList<String>();
    List<String> author_test = new ArrayList<String>();
    private Integer[] license_book1 = {3, 2};
    private Integer[] license_film1 = {3, 1};
    private Book book1 = new Book(111, Constant.BOOK, "libro di test 1", langues_test, author_test, 2000, "Romanzo", license_book1, 220, "Giunti");
    private Film film1 = new Film(333, Constant.FILM, "Film 1", author_test, langues_test, 2001, "horror", license_film1, 18, 125);

    /**
     * Prestiti
     */
    private LibraryResources library = new LibraryResources(db);
    Prestito pScaduto = new Prestito(library.generateId(user1.getUsername(), book1.getBarcode()), user1.getUsername(), book1.getBarcode(), LocalDate.of(2019, 2, 3), LocalDate.of(2019, 3, 3));
    Prestito p1 = new Prestito(library.generateId(user1.getUsername(), book1.getBarcode()), user1.getUsername(), book1.getBarcode(), LocalDate.of(2019, 3, 31), LocalDate.of(2019, 4, 30));

    void setUpRisorse(){
        langues_test.add("inglese");
        langues_test.add("spagnolo");
        author_test.add("Gino");
        author_test.add("Pino");
        db.getResourceList().put(film1.getBarcode(), film1);
        db.getResourceList().put(book1.getBarcode(), book1);
    }


    void setUpUtenti(){
        db.getUserList().put(user1.getUsername(), user1);
    }


    void setUpPrestito(){
        db.getPrestitoList().put(pScaduto.getCodePrestito(), pScaduto);
        db.getPrestitoList().put(p1.getCodePrestito(), p1);
    }

    @Test
    void testCheckTypeBookTrue() {
        setUpRisorse();
        String typeBook= "BOOK";
        boolean check = db.checkType(111, typeBook);
        assertEquals(true,check,"vero se libro:");
    }
    @Test
    void testCheckTypeFilmFalse() {
        setUpRisorse();
        String typeFilm= "FILM";
        boolean check = db.checkType(111,typeFilm);
        assertEquals(false, check,"falso se non libro");
    }
    @Test
    void testIncrementCopyOrLicenze() {
        setUpRisorse();
        int copieinPrestito = 1;
        int barcode = book1.getBarcode();
        db.incrementCopyOrLicenze(barcode, copieinPrestito);
        assertEquals(3,db.getResource(barcode).getLicense()[1]);
    }
    @Test
    void testDecrementCopyOrLicenze() {
        setUpRisorse();
        int copieinPrestito = 1;
        int barcode = book1.getBarcode();
        db.decrementCopyOrLicenze(barcode,0 );
        assertEquals(2,db.getResource(barcode).getLicense()[copieinPrestito]);
    }
    @Test
    void testIncrementLicenzeUser() {
        setUpRisorse();
        setUpUtenti();
        int bookOrFilm=0;
        db.getUser("test1").getBorrowed();
        db.incrementLicenzeUser(user1.getUsername(),bookOrFilm);
        int check = user1.getBorrowed()[bookOrFilm];
        assertEquals(3,check);

    }
    @Test
    void testDecrementLicenzeUser() {
        setUpRisorse();
        setUpUtenti();
        int bookOrFilm=0;
        db.getUser("test1").getBorrowed();
        db.decrementLicenzeUser(user1.getUsername(),bookOrFilm);
        int check = user1.getBorrowed()[bookOrFilm];
        assertEquals(1,check);
    }
    @Test
    void testRemovePrestito() {
        setUpPrestito();
        setUpUtenti();
        String code = p1.getCodePrestito();
        db.getPrestitoList().get(code).setOn_off(false);
        boolean status = db.getPrestitoList().get(code).getOn_off();
        assertTrue(status == false);

    }
    @Test
    void testUserHavePrestito() {
        setUpPrestito();
        setUpUtenti();
        boolean test = db.userHavePrestito(user1.getUsername());
        assertTrue(test == true);
    }
    @Test
    void testSearchGeneral() {
        setUpRisorse();
        boolean exist = db.searchGeneral("title", "libro di test 1");
        assertTrue(exist == true);
    }
}
