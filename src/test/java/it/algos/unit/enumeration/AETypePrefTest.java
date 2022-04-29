package it.algos.unit.enumeration;

import it.algos.test.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.packages.utility.preferenza.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.time.*;
import java.util.*;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: mer, 11-lug-2018
 * Time: 15:25
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@Tag("enums")
@DisplayName("Test sul service di preferenze")
public class AETypePrefTest extends ATest {

    @InjectMocks
    public PreferenzaBackend service;

    // alcuni parametri utilizzati
    private AETypePref typeString = AETypePref.string;

    private AETypePref typeBool = AETypePref.bool;

    private AETypePref typeInt = AETypePref.integer;

    private AETypePref typeDate = AETypePref.localdatetime;

    private AETypePref typeEmail = AETypePref.email;

    private Date dataOttenuta = null;

    private LocalDate localDataPrevista = null;

    private LocalDate localDataOttenuta = null;

    private LocalDateTime localDateTimePrevista = null;

    private LocalDateTime localDateTimeOttenuta = null;

    private byte[] bytesPrevisti = null;

    private byte[] bytesOttenuti = null;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
    }// end of method


    @Test
    public void stringa() {
        previsto = "";
        bytesOttenuti = typeString.objectToBytes(previsto);
        assertNotNull(bytesOttenuti);
        ottenuto = (String) typeString.bytesToObject(bytesOttenuti);
        assertEquals(previsto, ottenuto);

        previsto = " ";
        bytesOttenuti = typeString.objectToBytes(previsto);
        assertNotNull(bytesOttenuti);
        ottenuto = (String) typeString.bytesToObject(bytesOttenuti);
        assertEquals(previsto, ottenuto);

        previsto = "Prova di stringa";
        bytesOttenuti = typeString.objectToBytes(previsto);
        assertNotNull(bytesOttenuti);
        ottenuto = (String) typeString.bytesToObject(bytesOttenuti);
        assertEquals(previsto, ottenuto);
    }// end of single test


    @Test
    public void booleano() {
        previstoBooleano = true;
        bytesOttenuti = typeBool.objectToBytes(previstoBooleano);
        assertNotNull(bytesOttenuti);
        ottenutoBooleano = (boolean) typeBool.bytesToObject(bytesOttenuti);
        assertEquals(previstoBooleano, ottenutoBooleano);

        previstoBooleano = false;
        bytesOttenuti = typeBool.objectToBytes(previstoBooleano);
        assertNotNull(bytesOttenuti);
        ottenutoBooleano = (boolean) typeBool.bytesToObject(bytesOttenuti);
        assertEquals(previstoBooleano, ottenutoBooleano);
    }// end of single test

    @Test
    public void intero() {
        previstoIntero = -5;
        bytesOttenuti = typeInt.objectToBytes(previstoIntero);
        assertNotNull(bytesOttenuti);
        ottenutoIntero = (int) typeInt.bytesToObject(bytesOttenuti);
        assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = 0;
        bytesOttenuti = typeInt.objectToBytes(previstoIntero);
        assertNotNull(bytesOttenuti);
        ottenutoIntero = (int) typeInt.bytesToObject(bytesOttenuti);
        assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = 145875;
        bytesOttenuti = typeInt.objectToBytes(previstoIntero);
        assertNotNull(bytesOttenuti);
        ottenutoIntero = (int) typeInt.bytesToObject(bytesOttenuti);
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test

    @Test
    public void date() {
        localDateTimePrevista = null;
        bytesOttenuti = typeDate.objectToBytes(localDateTimePrevista);
        assertNotNull(bytesOttenuti);
        localDateTimeOttenuta = (LocalDateTime) typeDate.bytesToObject(bytesOttenuti);
        assertNull(localDateTimeOttenuta);

        localDateTimePrevista = LOCAL_DATE_TIME_UNO;
        bytesOttenuti = typeDate.objectToBytes(localDateTimePrevista);
        assertNotNull(bytesOttenuti);
        localDateTimeOttenuta = (LocalDateTime) typeDate.bytesToObject(bytesOttenuti);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);

        localDateTimePrevista = LOCAL_DATE_TIME_DUE;
        bytesOttenuti = typeDate.objectToBytes(localDateTimePrevista);
        assertNotNull(bytesOttenuti);
        localDateTimeOttenuta = (LocalDateTime) typeDate.bytesToObject(bytesOttenuti);
        assertEquals(localDateTimePrevista, localDateTimeOttenuta);
    }// end of single test

}// end of class
