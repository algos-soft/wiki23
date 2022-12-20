package it.algos.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.service.*;
import org.apache.commons.io.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;

import java.io.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 13-mar-2022
 * Time: 08:11
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("service")
@DisplayName("File service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileServiceTest extends AlgosIntegrationTest {


    private static String NOME_FILE_UNO = "Alfa.txt";

    private static String NOME_FILE_UNO_POST = "Alfa.txt";

    private static String NOME_FILE_DUE = "Beta.txt";

    private static String NOME_FILE_DUE_POST = "Beta.txt";

    private static String NOME_FILE_TRE = "Gamma.txt";

    private static String NOME_FILE_TRE_POST = "Gamma.txt";

    private static String NOME_FILE_QUATTRO = "Delta.txt";

    private static String NOME_FILE_CINQUE = "Mantova.txt";

    private static String NOME_FILE_SEI = "Omega.txt";

    private static String NOME_FILE_SETTE = "Omicron.txt";

    private static String NOME_FILE_OTTO = "Css.css";

    private static String PATH_DIRECTORY_TEST = "/Users/gac/Desktop/fileServiceTest/";

    private static String PATH_DIRECTORY_TEST_NO = "/Users/gac/Desktop/test/";

    private static String PATH_DIRECTORY_UNO = PATH_DIRECTORY_TEST + "Prima/";

    private static String PATH_DIRECTORY_DUE = PATH_DIRECTORY_TEST + "Possibile/";

    private static String PATH_DIRECTORY_TRE = PATH_DIRECTORY_TEST + "Mantova/";

    private static String NOME_DIR_SUB = "Sotto/";

    private static String PATH_DIRECTORY_NON_ESISTENTE = PATH_DIRECTORY_TEST + "Genova/";

    private static String PATH_DIRECTORY_DA_COPIARE = PATH_DIRECTORY_TEST + "NuovaDirectory/";

    private static String PATH_DIRECTORY_MANCANTE = PATH_DIRECTORY_TEST + "CartellaCopiata/";

    private static String SOURCE = PATH_DIRECTORY_TEST + "Sorgente";

    private static String DEST = PATH_DIRECTORY_TEST + "Destinazione";

    private static String TESTO_TOKEN_ANTE = "Vaad24Application";

    private static String TESTO_TOKEN_POST = "Wiki23";

    private static String TESTO_SENZA = "Un qualsiasi testo senza token";

    private static String TESTO_CON = " Un qualsiasi testo oltre il token";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private FileService service;

    private File unFile;

    //--path
    //--esiste directory
    //--manca slash iniziale
    protected static Stream<Arguments> DIRECTORY() {
        return Stream.of(
                Arguments.of(null, false, false),
                Arguments.of(VUOTA, false, false),
                Arguments.of(String.format("%s", PATH_DIRECTORY_TEST_NO), false, false),
                Arguments.of(String.format("%s", PATH_DIRECTORY_TEST), true, false),
                Arguments.of(String.format("%sMantova", PATH_DIRECTORY_TEST_NO), false, false),
                Arguments.of(String.format("%sMantova", PATH_DIRECTORY_TEST), true, false),
                Arguments.of(String.format("%sMantova/Mantova.txt", PATH_DIRECTORY_TEST_NO), false, false),
                Arguments.of(String.format("%sMantova/Mantova.txt", PATH_DIRECTORY_TEST), false, false),
                Arguments.of(String.format("%sMantova/Sotto", PATH_DIRECTORY_TEST_NO), false, false),
                Arguments.of(String.format("%sMantova/Sotto", PATH_DIRECTORY_TEST), true, false),
                Arguments.of("Users/gac/Documents/IdeaProjects/operativi/vaad24/src/", false, true),
                Arguments.of("/Users/gac/Documents/IdeaProjects/operativi/vaad24/src/", true, false)
        );
    }


    //--path
    //--esiste
    protected static Stream<Arguments> FILE() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("/Users/gac/Desktop/test/Mantova/", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova.", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova.tx", false),
                Arguments.of("/Users/gac/Desktop/test/Mantova.txt", false),
                Arguments.of("/Users/gac/Documents/IdeaProjects/operativi/vaad24/src/vaad24", false),
                Arguments.of("Users/gac/Documents/IdeaProjects/operativi/vaad24/src/vaad24.iml", false),
                Arguments.of("/Users/gac/Documents/IdeaProjects/operativi/vaad24/.idea/vaad24.iml", true),
                Arguments.of("/Users/gac/Desktop/test/Pippo", false)
        );
    }


    //--type copy
    //--pathDir sorgente
    //--pathDir destinazione
    //--nome file
    //--flag copiato
    protected static Stream<Arguments> COPY_FILE() {
        return Stream.of(
                Arguments.of(null, VUOTA, VUOTA, VUOTA, false),
                Arguments.of(AECopy.sourceSoloSeNonEsiste, VUOTA, VUOTA, NOME_FILE_UNO, false),
                Arguments.of(AECopy.fileDelete, VUOTA, VUOTA, VUOTA, false),
                Arguments.of(AECopy.sourceSoloSeNonEsiste, PATH_DIRECTORY_TRE, PATH_DIRECTORY_DUE, NOME_FILE_UNO, false),
                Arguments.of(AECopy.fileOnly, PATH_DIRECTORY_TRE, PATH_DIRECTORY_DUE, VUOTA, false),
                Arguments.of(AECopy.fileDelete, PATH_DIRECTORY_TRE, PATH_DIRECTORY_DUE, VUOTA, false),
                Arguments.of(AECopy.fileOnly, PATH_DIRECTORY_TRE, PATH_DIRECTORY_DUE, NOME_FILE_UNO, true),
                Arguments.of(AECopy.fileDelete, PATH_DIRECTORY_TRE, PATH_DIRECTORY_DUE, NOME_FILE_UNO, true),
                Arguments.of(AECopy.fileOnly, PATH_DIRECTORY_TRE, VUOTA, NOME_FILE_UNO, false),
                Arguments.of(AECopy.fileDelete, PATH_DIRECTORY_TRE, VUOTA, NOME_FILE_UNO, false)
        );
    }

    //--type copy
    //--pathDir sorgente
    //--pathDir destinazione
    //--directory copiata
    //--src token (opzionale)
    //--dest token (opzionale)
    protected static Stream<Arguments> COPY_DIRECTORY() {
        return Stream.of(
                Arguments.of(null, VUOTA, VUOTA, false, VUOTA, VUOTA),
                Arguments.of(AECopy.fileOnly, VUOTA, VUOTA, false, VUOTA, VUOTA),
                Arguments.of(AECopy.dirOnly, VUOTA, VUOTA, false, VUOTA, VUOTA),
                Arguments.of(AECopy.dirOnly, VUOTA, DEST, false, VUOTA, VUOTA),
                Arguments.of(AECopy.dirOnly, SOURCE, VUOTA, false, VUOTA, VUOTA),
                Arguments.of(AECopy.dirOnly, PATH_DIRECTORY_MANCANTE, DEST, false, VUOTA, VUOTA),
                Arguments.of(AECopy.dirOnly, SOURCE, PATH_DIRECTORY_DUE, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirOnly, SOURCE, DEST, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirDelete, SOURCE, PATH_DIRECTORY_MANCANTE, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirDelete, SOURCE, PATH_DIRECTORY_DUE, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirFilesAddOnly, SOURCE, DEST, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirFilesAddOnly, SOURCE, PATH_DIRECTORY_TRE, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirFilesModifica, SOURCE, DEST, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirFilesModifica, SOURCE, PATH_DIRECTORY_TRE, true, VUOTA, VUOTA),
                Arguments.of(AECopy.dirFilesModificaToken, SOURCE, PATH_DIRECTORY_TRE, false, VUOTA, VUOTA),
                Arguments.of(AECopy.dirFilesModificaToken, SOURCE, PATH_DIRECTORY_TRE, false, VUOTA, TESTO_TOKEN_POST),
                Arguments.of(AECopy.dirFilesModificaToken, SOURCE, PATH_DIRECTORY_TRE, false, TESTO_TOKEN_ANTE, VUOTA),
                Arguments.of(AECopy.dirFilesModificaToken, SOURCE, PATH_DIRECTORY_TRE, true, TESTO_TOKEN_ANTE, TESTO_TOKEN_POST),
                Arguments.of(AECopy.dirFilesModificaToken, SOURCE, PATH_DIRECTORY_TRE, true, TESTO_TOKEN_ANTE, TESTO_TOKEN_POST)
        );
    }

    /**
     * Execute only once before running all tests <br>
     * Esegue una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpAll() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = fileService;
        this.creaCartelle();
    }


    /**
     * Qui passa prima di ogni test <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        unFile = null;
    }


    @ParameterizedTest
    @MethodSource(value = "DIRECTORY")
    @Order(1)
    @DisplayName("1 - Check di una directory")
        //--path
        //--esiste directory
        //--manca slash iniziale
    void checkDirectory(final String absolutePathDirectoryToBeChecked, final boolean previstoBooleano) {
        System.out.println("1 - Check di una directory");
        System.out.println(VUOTA);

        sorgente = absolutePathDirectoryToBeChecked;
        ottenutoRisultato = service.checkDirectory(sorgente);
        assertNotNull(ottenutoRisultato);
        assertEquals(previstoBooleano, ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

    @ParameterizedTest
    @MethodSource(value = "DIRECTORY")
    @Order(2)
    @DisplayName("2 - Esistenza di una directory")
        //--path
        //--esiste directory
        //--manca slash iniziale
    void isEsisteDirectory(final String absolutePathDirectoryToBeChecked, final boolean previstoBooleano) {
        System.out.println("2 - Esistenza di una directory");
        System.out.println(VUOTA);

        sorgente = absolutePathDirectoryToBeChecked;
        ottenutoBooleano = service.isEsisteDirectory(sorgente);
        assertEquals(previstoBooleano, ottenutoBooleano);
    }

    @ParameterizedTest
    @MethodSource(value = "FILE")
    @Order(3)
    @DisplayName("3 - Check di un file")
        //--path
        //--esiste directory
        //--manca slash iniziale
    void checkFile(final String sorgente, final boolean previstoBooleano) {
        System.out.println("3 - Check di un file");
        System.out.println(VUOTA);

        ottenutoRisultato = service.checkFile(sorgente);
        assertNotNull(ottenutoRisultato);
        assertEquals(previstoBooleano, ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }


    @ParameterizedTest
    @MethodSource(value = "FILE")
    @Order(4)
    @DisplayName("4 - Esistenza di un file")
        //--path
        //--esiste directory
        //--manca slash iniziale
    void isEsisteFile(final String sorgente, final boolean previstoBooleano) {
        System.out.println("4 - Esistenza di un file");
        System.out.println(VUOTA);

        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertEquals(previstoBooleano, ottenutoBooleano);
    }


    @Test
    @Order(5)
    @DisplayName("5 - Creo e cancello una directory")
    void directory() {
        System.out.println("5 - Creo e cancello una directory");
        System.out.println(VUOTA);

        sorgente = PATH_DIRECTORY_TEST + "test4522/";
        System.out.println(String.format("Nome (completo) della directory: %s", sorgente));
        System.out.println(VUOTA);

        System.out.println("A - Controlla l'esistenza (isEsisteDirectory)");
        ottenutoBooleano = service.isEsisteDirectory(sorgente);
        assertFalse(ottenutoBooleano);
        System.out.println("Prima non esiste");
        System.out.println(VUOTA);

        System.out.println("B - Crea la directory (creaDirectory)");
        ottenutoRisultato = service.creaDirectory(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("La directory è stata creata");
        System.out.println(VUOTA);

        System.out.println("C - Ricontrolla l'esistenza (isEsisteDirectory)");
        ottenutoBooleano = service.isEsisteDirectory(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println("La directory esiste");
        System.out.println(VUOTA);

        System.out.println("D - Cancella la directory (deleteDirectory)");
        ottenutoRisultato = service.deleteDirectory(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("La directory è stata cancellata");
        System.out.println(VUOTA);

        System.out.println("E - Controllo finale (isEsisteDirectory)");
        ottenutoBooleano = service.isEsisteDirectory(sorgente);
        assertFalse(ottenutoBooleano);
        System.out.println("La directory non esiste");
        System.out.println(VUOTA);
    }


    @Test
    @Order(6)
    @DisplayName("6 - Creo e cancello un file in una directory 'stabile'")
    void fileRoot() {
        System.out.println("6 - Creo e cancello un file in una directory 'stabile'");
        System.out.println("Il file viene creato VUOTO");
        System.out.println(VUOTA);

        sorgente = "/Users/gac/Desktop/Mantova.txt";
        System.out.println(String.format("Nome (completo) del file: %s", sorgente));
        System.out.println(VUOTA);

        System.out.println("A - Controlla l'esistenza (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertFalse(ottenutoBooleano);
        System.out.println("Prima non esiste");
        System.out.println(VUOTA);

        System.out.println("B - Creo il file (creaFile)");
        ottenutoRisultato = service.creaFile(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("Il file è stato creato");
        System.out.println(VUOTA);

        System.out.println("C - Ricontrolla l'esistenza (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println("Il file esiste");
        System.out.println(VUOTA);

        System.out.println("D - Cancello il file (deleteFile)");
        ottenutoRisultato = service.deleteFile(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("Il file è stato cancellato");
        System.out.println(VUOTA);

        System.out.println("E - Controllo finale (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertFalse(ottenutoBooleano);
        System.out.println("Il file non esiste");
        System.out.println(VUOTA);
    }

    @Test
    @Order(7)
    @DisplayName("7 - Creo e cancello un file in una directory 'inesistente'")
    void fileSottoCartella() {
        System.out.println("7 - Creo e cancello un file in una directory 'inesistente'");
        System.out.println("Il file viene creato VUOTO");
        System.out.println(VUOTA);

        sorgente2 = PATH_DIRECTORY_TEST + "Torino/";
        sorgente3 = sorgente2 + "Padova/";
        sorgente = sorgente3 + "Mantova.txt";
        System.out.println(String.format("Nome (completo) del file: %s", sorgente));
        System.out.println(VUOTA);

        System.out.println("A - Controlla l'esistenza (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertFalse(ottenutoBooleano);
        System.out.println("Prima non esiste");
        System.out.println(VUOTA);

        System.out.println("B - Creo il file (creaDirectoryParentAndFile)");
        ottenutoRisultato = service.creaFile(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("Il file è stato creato");
        System.out.println(VUOTA);

        System.out.println("C - Ricontrolla l'esistenza (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertTrue(ottenutoBooleano);
        System.out.println("Il file esiste");
        System.out.println(VUOTA);

        System.out.println("D - Cancello il file (deleteFile)");
        ottenutoRisultato = service.deleteFile(sorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("Il file è stato cancellato");
        System.out.println(VUOTA);

        System.out.println("E - Controllo finale del file (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(sorgente);
        assertFalse(ottenutoBooleano);
        System.out.println("Il file non esiste");
        System.out.println(VUOTA);

        System.out.println("F - Cancello anche la(e) cartella(e) intermedia(e) (deleteDirectory)");
        ottenutoRisultato = service.deleteDirectory(sorgente2);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        System.out.println("Cancellata la directory provvisoria");
        System.out.println(VUOTA);

        System.out.println("G - Controllo finale della directory (isEsisteDirectory)");
        ottenutoBooleano = service.isEsisteDirectory(sorgente2);
        assertFalse(ottenutoBooleano);
        System.out.println("La directory provvisoria non esiste");
        System.out.println(VUOTA);
    }

    @ParameterizedTest
    @MethodSource(value = "DIRECTORY")
    @Order(8)
    @DisplayName("8 - Controlla la slash iniziale del path")
        //--path
        //--esiste directory
        //--manca slash iniziale
    void isNotSlashIniziale(final String sorgente, final boolean nonUsato, final boolean mancaSlash) {
        System.out.println("8 - Controlla la slash iniziale del path");
        System.out.println(VUOTA);

        ottenutoBooleano = service.isNotSlashIniziale(sorgente);
        assertEquals(mancaSlash, ottenutoBooleano);
    }

    @ParameterizedTest
    @MethodSource(value = "COPY_FILE")
    @Order(9)
    @DisplayName("9 - Copia il file")
        //--type copy
        //--pathDir sorgente
        //--pathDir destinazione
        //--nome file
        //--flag copiato
    void copyFile(final AECopy typeCopy, final String srcPathDir, final String destPathDir, final String nomeFile, final boolean copiato) {
        System.out.println("9 - Copia il file");
        System.out.println(VUOTA);

        ottenutoRisultato = service.copyFile(typeCopy, srcPathDir, destPathDir, nomeFile);
        assertNotNull(ottenutoRisultato);
        assertEquals(copiato, ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);
    }

    @Test
    @Order(10)
    @DisplayName("10 - Copia un file NON esistente (AECopy.fileSoloSeNonEsiste)")
    void copyFile() {
        System.out.println("10 - Copia un file NON esistente (AECopy.fileSoloSeNonEsiste)");
        System.out.println("Il file viene creato VUOTO");
        System.out.println(VUOTA);
        String nomeFile = NOME_FILE_UNO;
        String dirSorgente = PATH_DIRECTORY_UNO;
        String dirDestinazione = PATH_DIRECTORY_TRE;
        String pathSorgente = dirSorgente + nomeFile;
        String pathDestinazione = dirDestinazione + nomeFile;

        cancellaCartelle();
        System.out.println(String.format("Nome (completo) del file: %s", pathDestinazione));

        System.out.println(VUOTA);
        System.out.println("A - Inizialmente non esiste (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathSorgente);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("B - Viene creato (creaFile)");
        ottenutoRisultato = service.creaFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("C - Controlla che NON esista nella directory di destinazione (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("D - Il file viene copiato (copyFile)");
        ottenutoRisultato = service.copyFile(AECopy.fileOnly, dirSorgente, dirDestinazione, nomeFile);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        System.out.println(VUOTA);
        System.out.println("E - Controlla che sia stato copiato (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertTrue(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("F - Cancellazione finale del file dalla directory sorgente (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("G - Cancellazione finale del file dalla directory destinazione (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathDestinazione);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
    }

    @Test
    @Order(11)
    @DisplayName("11 - Cerca di copiare un file GIA esistente (AECopy.fileSoloSeNonEsiste)")
    void copyFile2() {
        System.out.println("11 - Cerca di copiare un file GIA esistente (AECopy.fileSoloSeNonEsiste)");
        System.out.println("Il file viene creato VUOTO");
        System.out.println(VUOTA);

        String nomeFile = NOME_FILE_UNO;
        String dirSorgente = PATH_DIRECTORY_UNO;
        String dirDestinazione = PATH_DIRECTORY_TRE;
        String pathSorgente = dirSorgente + nomeFile;
        String pathDestinazione = dirDestinazione + nomeFile;

        cancellaCartelle();
        System.out.println(String.format("Nome (completo) del file: %s", pathDestinazione));

        System.out.println(VUOTA);
        System.out.println("A - Inizialmente non esiste nella directory sorgente (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathSorgente);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("B - Inizialmente non esiste nella directory destinazione (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("C - Viene creato nella directory sorgente (creaFile)");
        ottenutoRisultato = service.creaFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("D - Viene creato nella directory destinazione (creaFile)");
        ottenutoRisultato = service.creaFile(pathDestinazione);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("E - Controlla che esista nella directory sorgente (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathSorgente);
        assertTrue(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("F - Controlla che esista nella directory di destinazione (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertTrue(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("G - Prova a copiare il file sovrascrivendo quello esistente (copyFile)");
        ottenutoRisultato = service.copyFile(AECopy.fileOnly, dirSorgente, dirDestinazione, nomeFile);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        System.out.println(VUOTA);
        System.out.println("H - Cancellazione finale del file dalla directory sorgente (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(" - Cancellazione finale del file dalla directory destinazione (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathDestinazione);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
    }


    @Test
    @Order(12)
    @DisplayName("12 - Copia un file esistente (AECopy.fileSovrascriveSempreAncheSeEsiste)")
    void copyFile3() {
        System.out.println("12 - Copia un file esistente (AECopy.fileSovrascriveSempreAncheSeEsiste)");
        System.out.println("Il file viene creato VUOTO");
        System.out.println(VUOTA);

        String nomeFile = NOME_FILE_UNO;
        String dirSorgente = PATH_DIRECTORY_UNO;
        String dirDestinazione = PATH_DIRECTORY_TRE;
        String pathSorgente = dirSorgente + nomeFile;
        String pathDestinazione = dirDestinazione + nomeFile;

        cancellaCartelle();
        System.out.println(String.format("Nome (completo) del file: %s", pathDestinazione));

        System.out.println(VUOTA);
        System.out.println("A - Inizialmente non esiste nella directory sorgente (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathSorgente);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("B - Inizialmente non esiste nella directory destinazione (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("C - Viene creato nella directory sorgente (creaFile)");
        ottenutoRisultato = service.creaFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("D - Viene creato nella directory destinazione (creaFile)");
        ottenutoRisultato = service.creaFile(pathDestinazione);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("E - Controlla che esista nella directory sorgente (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathSorgente);
        assertTrue(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("F - Controlla che esista nella directory di destinazione (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertTrue(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("G - Copia il file sovrascrivendo quello esistente (copyFile)");
        ottenutoRisultato = service.copyFile(AECopy.fileDelete, dirSorgente, dirDestinazione, nomeFile);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        System.out.println(VUOTA);
        System.out.println("H - Cancellazione finale del file dalla directory sorgente (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println(" - Cancellazione finale del file dalla directory destinazione (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathDestinazione);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
    }

    @Test
    @Order(13)
    @DisplayName("13 - Copia un file NON esistente (AECopy.fileSovrascriveSempreAncheSeEsiste)")
    void copyFile4() {
        System.out.println("13 - Copia un file NON esistente (AECopy.fileSovrascriveSempreAncheSeEsiste)");
        System.out.println("Il file viene creato VUOTO");
        System.out.println(VUOTA);

        String nomeFile = NOME_FILE_UNO;
        String dirSorgente = PATH_DIRECTORY_UNO;
        String dirDestinazione = PATH_DIRECTORY_TRE;
        String pathSorgente = dirSorgente + nomeFile;
        String pathDestinazione = dirDestinazione + nomeFile;

        cancellaCartelle();
        System.out.println(String.format("Nome (completo) del file: %s", pathDestinazione));

        System.out.println(VUOTA);
        System.out.println("A - Inizialmente non esiste (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathSorgente);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("B - Viene creato (creaFile)");
        ottenutoRisultato = service.creaFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("C - Controlla che NON esista nella directory di destinazione (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertFalse(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("D - Il file viene copiato (copyFile)");
        ottenutoRisultato = service.copyFile(AECopy.fileDelete, dirSorgente, dirDestinazione, nomeFile);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
        printRisultato(ottenutoRisultato);

        System.out.println(VUOTA);
        System.out.println("E - Controlla che sia stato copiato (isEsisteFile)");
        ottenutoBooleano = service.isEsisteFile(pathDestinazione);
        assertTrue(ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println("F - Cancellazione finale del file dalla directory sorgente (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathSorgente);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(VUOTA);
        System.out.println("G - Cancellazione finale del file dalla directory destinazione (deleteFile)");
        ottenutoRisultato = service.deleteFile(pathDestinazione);
        assertNotNull(ottenutoRisultato);
        assertTrue(ottenutoRisultato.isValido());
    }


    @ParameterizedTest
    @MethodSource(value = "DIRECTORY")
    @Order(14)
    @DisplayName("14 - findPathBreve")
        //--path
        //--esiste directory
        //--manca slash iniziale
    void findPathBreve(final String absolutePathDirectoryToBeChecked) {
        System.out.println("14 - findPathBreve");
        System.out.println(VUOTA);

        sorgente = absolutePathDirectoryToBeChecked;
        if (textService.isValid(sorgente)) {
            ottenuto = service.findPathBreve(sorgente);
            assertTrue(textService.isValid(ottenuto));
        }

        System.out.println(VUOTA);
        System.out.print(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }

    @ParameterizedTest
    @MethodSource(value = "DIRECTORY")
    @Order(15)
    @DisplayName("15 - lastDirectory")
        //--path
        //--esiste directory
        //--manca slash iniziale
    void estraeDirectoryFinaleSenzaSlash(final String absolutePathDirectoryToBeChecked) {
        System.out.println("15 - lastDirectory");
        System.out.println(VUOTA);

        sorgente = absolutePathDirectoryToBeChecked;
        if (textService.isValid(sorgente)) {
            ottenuto = service.lastDirectory(sorgente);
            assertTrue(textService.isValid(ottenuto));
        }

        System.out.println(VUOTA);
        System.out.print(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }

    @ParameterizedTest
    @MethodSource(value = "COPY_DIRECTORY")
    @Order(16)
    @DisplayName("16 - Copia la directory")
        //--type copy
        //--pathDir sorgente
        //--pathDir destinazione
        //--directory copiata
        //--src token (opzionale)
        //--dest token (opzionale)
    void copyDirectory(final AECopy typeCopy, final String srcPathDir, final String destPathDir, final boolean copiato, String srcToken, String destToken) {
        System.out.println("16 - Copia la directory");
        System.out.println(VUOTA);

        this.creaCartelle();
        //--prepare due cartella regolate nelle condizioni iniziali
        fixSorgente(true);

        ottenutoRisultato = service.copyDirectory(typeCopy, srcPathDir, destPathDir, srcToken, destToken);
        assertNotNull(ottenutoRisultato);
        printRisultato(ottenutoRisultato);
        assertEquals(copiato, ottenutoRisultato.isValido());

        //--cancella le due cartella
        fixSorgente(false);
    }


    @Test
    @Order(31)
    @DisplayName("31 - Cerco i path delle subdirectory di 1° livello della directory")
    void getSubDirPath() {
        System.out.println("31 - Cerco i path delle subdirectory di 1° livello della directory");
        System.out.println(VUOTA);

        sorgente = PATH_DIRECTORY_TEST;
        listaStr = service.getSubDirPath(sorgente);
        assertTrue(listaStr != null);
        message = String.format("Nella directory '%s' ci sono %d sub-directories", sorgente, listaStr.size());
        System.out.println(message);
        print(listaStr);
    }

    @Test
    @Order(32)
    @DisplayName("32 - Cerco i nomi delle subdirectory di 1° livello della directory")
    void getDirPath() {
        System.out.println("32 - Cerco i nomi delle subdirectory di 1° livello della directory");
        System.out.println(VUOTA);

        sorgente = PATH_DIRECTORY_TEST;
        listaStr = service.getSubDirName(sorgente);
        assertTrue(listaStr != null);
        message = String.format("Nella directory '%s' ci sono %d sub-directories", sorgente, listaStr.size());
        System.out.println(message);
        print(listaStr);

    }


    @Test
    @Order(33)
    @DisplayName("33 - Cerco i path di tutti i files (ricorsivo) della directory")
    void getFilesPath() {
        System.out.println("33 - Cerco i path di tutti i files (ricorsivo) della directory");
        System.out.println(VUOTA);

        sorgente = PATH_DIRECTORY_TEST;
        listaStr = service.getFilesPath(sorgente);
        assertTrue(listaStr != null);
        message = String.format("Nella directory '%s' ed eventuali subdirectory ci sono %d files", sorgente, listaStr.size());
        System.out.println(message);
        print(listaStr);

    }

    @Test
    @Order(34)
    @DisplayName("34 - Cerco i nomi di tutti i files (ricorsivo) della directory")
    void getFilesNames() {
        System.out.println("34 - Cerco i nomi di tutti i files (ricorsivo) della directory ");
        System.out.println(VUOTA);

        sorgente = PATH_DIRECTORY_TEST;
        listaStr = service.getFilesName(sorgente);
        assertTrue(listaStr != null);
        message = String.format("Nella directory '%s' ed eventuali subdirectory ci sono %d files", sorgente, listaStr.size());
        System.out.println(message);
        print(listaStr);

        sorgente = PATH_DIRECTORY_DUE;
        listaStr = service.getFilesName(sorgente);
        assertTrue(listaStr != null);
        System.out.println(VUOTA);
        message = String.format("Nella directory '%s' ed eventuali subdirectory ci sono %d files", sorgente, listaStr.size());
        System.out.println(message);
        print(listaStr);
    }


    private void creaCartelle() {
        service.deleteDirectory(PATH_DIRECTORY_UNO);
        service.deleteDirectory(PATH_DIRECTORY_DUE);
        service.deleteDirectory(PATH_DIRECTORY_TRE);
        service.deleteDirectory(PATH_DIRECTORY_TRE + NOME_DIR_SUB);

        service.creaDirectory(PATH_DIRECTORY_UNO);
        service.creaDirectory(PATH_DIRECTORY_DUE);
        service.creaDirectory(PATH_DIRECTORY_TRE);
        service.creaDirectory(PATH_DIRECTORY_TRE + NOME_DIR_SUB);

        service.creaFile(PATH_DIRECTORY_UNO+NOME_FILE_SETTE);
        service.creaFile(PATH_DIRECTORY_DUE + NOME_FILE_UNO);
        service.creaFile(PATH_DIRECTORY_DUE + NOME_FILE_DUE);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_FILE_UNO);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_FILE_DUE);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_FILE_TRE);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_FILE_QUATTRO);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_DIR_SUB + NOME_FILE_CINQUE);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_DIR_SUB + NOME_FILE_SEI);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_FILE_SETTE);
        service.creaFile(PATH_DIRECTORY_TRE + NOME_FILE_OTTO);

        service.sovraScriveFile(PATH_DIRECTORY_TRE + NOME_FILE_UNO, TESTO_TOKEN_POST + TESTO_CON);
        service.sovraScriveFile(PATH_DIRECTORY_DUE + NOME_FILE_DUE, TESTO_TOKEN_POST + TESTO_CON);
        service.sovraScriveFile(PATH_DIRECTORY_TRE + NOME_FILE_DUE, TESTO_TOKEN_POST+"diverso");
        service.sovraScriveFile(PATH_DIRECTORY_TRE + NOME_FILE_TRE, TESTO_SENZA);
    }

    private void cancellaCartelle() {
        service.deleteDirectory(PATH_DIRECTORY_TEST);
    }

    void fixSorgente(boolean inizio) {
        String srcDir = SOURCE;
        String srcSub1 = SOURCE + SLASH + NOME_DIR_SUB;
        String file1 = NOME_FILE_UNO;
        String file2 = NOME_FILE_DUE;
        String file3 = NOME_FILE_TRE;
        String file4 = NOME_FILE_QUATTRO;

        File src = new File(srcDir);
        File sub1 = new File(srcSub1);

        File fileUno = new File(srcDir + SLASH + file1);
        File fileDue = new File(srcDir + SLASH + file2);
        File fileTre = new File(srcDir + SLASH + file3);
        File fileQuattro = new File(sub1 + SLASH + file4);

        if (inizio) {
            try {
                src.mkdirs();
                sub1.mkdirs();
                fileUno.createNewFile();
                fileDue.createNewFile();
                fileTre.createNewFile();
                fileQuattro.createNewFile();
            } catch (Exception unErrore) {
            }
            service.sovraScriveFile(fileUno, TESTO_TOKEN_ANTE + TESTO_CON);
            service.sovraScriveFile(fileDue, TESTO_TOKEN_ANTE + TESTO_CON);
            service.sovraScriveFile(fileTre, TESTO_SENZA);
            service.sovraScriveFile(fileQuattro, TESTO_SENZA);
        }
        else {
            try {
                FileUtils.deleteDirectory(src);
            } catch (Exception unErrore) {
            }
        }
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
        cancellaCartelle();
    }

}