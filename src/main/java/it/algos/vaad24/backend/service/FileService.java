package it.algos.vaad24.backend.service;

import it.algos.vaad24.backend.boot.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import it.algos.vaad24.backend.wrapper.*;
import org.apache.commons.io.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: dom, 28-giu-2020
 * Time: 15:10
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private AArrayService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FileService extends AbstractService {

    public static final String PARAMETRO_NULLO = "Il parametro in ingresso è nullo";

    public static final String PATH_NULLO = "Il path in ingresso è nullo";

    public static final String PATH_VUOTO = "Il path in ingresso è vuoto";

    public static final String PATH_NOT_ABSOLUTE = "Il primo carattere del path NON è uno '/' (slash)";

    public static final String NON_ESISTE_FILE = "Il file non esiste";

    public static final String PATH_SENZA_SUFFIX = "Manca il 'suffix' terminale";

    public static final String PATH_FILE_ESISTENTE = "Esiste già il file";

    public static final String NON_E_FILE = "Non è un file";

    public static final String DIRECTORY_NOT_FILE = "Directory e non file";

    public static final String DIRECTORY_MANCANTE = "Mancava la directory per il file, ma è stata creata";

    public static final String CREATO_FILE = "Il file è stato creato";

    public static final String NON_CREATO_FILE = "Il file non è stato creato";

    public static final String NON_COPIATO_FILE = "Il file non è stato copiato";

    public static final String CANCELLATO_FILE = "Il file è stato cancellato";

    public static final String NON_CANCELLATO_FILE = "Il file non è stato cancellato";

    public static final String CANCELLATA_DIRECTORY = "La directory è stata cancellata";

    public static final String NON_CANCELLATA_DIRECTORY = "La directory non è stata cancellata";

    public static final String NON_ESISTE_DIRECTORY = "La directory non esiste";

    public static final String CREATA_DIRECTORY = "La directory è stata creata";

    public static final String NON_CREATA_DIRECTORY = "La directory non è stata creata";

    public static final String NON_E_DIRECTORY = "Non è una directory";

    public static final String DIR_PROGETTO = "/src/";

    public static final String DIR_PROGETTO_VUOTO = "/src/main/java/";

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Controlla l'esistenza di una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param directoryToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult checkDirectory(final File directoryToBeChecked) {
        AResult result = checkFileDirectory("checkDirectory", directoryToBeChecked);
        if (result.isErrato()) {
            return result;
        }
        String message;

        if (directoryToBeChecked.exists()) {
            if (directoryToBeChecked.isDirectory()) {
                Object alfa = getSubDirName(directoryToBeChecked.getAbsolutePath());
                Object beta = getFilesPath(directoryToBeChecked.getAbsolutePath());
                Object gamma = getFilesName(directoryToBeChecked.getAbsolutePath());
                message = String.format("Trovata la directory %s", directoryToBeChecked.getAbsolutePath());
                //                logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
                return result.validMessage(message);
            }
            else {
                message = String.format("%s%s%s", NON_E_DIRECTORY, FORWARD, directoryToBeChecked.getAbsolutePath());
                //                logger.error(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
                return result.errorMessage(message);
            }
        }
        else {
            message = String.format("%s%s%s", NON_ESISTE_DIRECTORY, FORWARD, directoryToBeChecked.getAbsolutePath());
            //            logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.errorMessage(message);
        }
    }

    /**
     * Controlla l'esistenza di una directory <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param absolutePathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult checkDirectory(final String absolutePathDirectoryToBeChecked) {
        AResult result = checkPath("checkDirectory", absolutePathDirectoryToBeChecked);
        if (result.isErrato()) {
            return result;
        }

        return checkDirectory(new File(absolutePathDirectoryToBeChecked));
    }


    /**
     * Controlla l'esistenza di una directory <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Una volta costruita la directory, getPath() e getAbsolutePath() devono essere uguali
     *
     * @param absolutePathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return true se la directory esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteDirectory(final String absolutePathDirectoryToBeChecked) {
        return checkDirectory(absolutePathDirectoryToBeChecked).isValido();
    }

    /**
     * Controlla l'esistenza di un modulo <br>
     * <p>
     * Il nomeModulo non deve essere nullo <br>
     * Il nomeModulo non deve essere vuoto <br>
     * La richiesta è CASE SENSITIVE (maiuscole e minuscole NON SONO uguali) <br>
     *
     * @param nomeModulo da ricercare
     *
     * @return true se il modulo esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteModulo(final String nomeModulo) {
        if (textService.isEmpty(nomeModulo)) {
            return false;
        }
        if (nomeModulo.equals(textService.primaMaiuscola(nomeModulo))) {
            return false;
        }

        String absolutePathDirectoryToBeChecked = getPathDir(nomeModulo);
        return isEsisteDirectory(absolutePathDirectoryToBeChecked);
    }

    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * Controlla che getPath() e getAbsolutePath() siano uguali <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeChecked con path completo che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult checkFile(final File fileToBeChecked) {
        AResult result = checkFileDirectory("checkFile", fileToBeChecked);
        if (result.isErrato()) {
            return result;
        }
        String message;
        boolean caseSensitiveUgualeFalse = false;

        try {
            caseSensitiveUgualeFalse = fileToBeChecked.getCanonicalPath().equals(fileToBeChecked.getAbsolutePath());
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        if (fileToBeChecked.exists() && caseSensitiveUgualeFalse) {
            if (fileToBeChecked.isFile()) {
                message = String.format("Trovato il file %s", fileToBeChecked.getAbsolutePath());
                //                logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
                return result.validMessage(message);
            }
            else {
                message = String.format("%s%s%s", NON_E_FILE, FORWARD, fileToBeChecked.getAbsolutePath());
                //                logger.error(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
                return result.errorMessage(message);
            }
        }
        else {
            if (this.isNotSuffix(fileToBeChecked.getAbsolutePath())) {
                message = String.format("%s al file %s", PATH_SENZA_SUFFIX, fileToBeChecked.getAbsolutePath());
                logger.error(new WrapLog().exception(new AlgosException(message)).usaDb().type(AETypeLog.file));
                return result.errorMessage(message);
            }

            if (!fileToBeChecked.exists()) {
                message = String.format("%s%s%s", NON_ESISTE_FILE, FORWARD, fileToBeChecked.getAbsolutePath());
                logger.info(new WrapLog().exception(new AlgosException(message)).usaDb().type(AETypeLog.file));
                return result.errorMessage(message);
            }

            message = String.format("Errore generico nel check del file %s", fileToBeChecked.getAbsolutePath());
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb().type(AETypeLog.file));
            return result.errorMessage(message);
        }
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeChecked path completo del file che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult checkFile(final String absolutePathFileWithSuffixToBeChecked) {
        String message;
        AResult result = checkPath("checkFile", absolutePathFileWithSuffixToBeChecked);
        if (result.isErrato()) {
            return result;
        }

        if (this.isSlashFinale(absolutePathFileWithSuffixToBeChecked)) {
            message = String.format("%s%s%s", DIRECTORY_NOT_FILE, FORWARD, absolutePathFileWithSuffixToBeChecked);
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb().type(AETypeLog.file));
            return result.errorMessage(message);
        }

        return checkFile(new File(absolutePathFileWithSuffixToBeChecked));
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeChecked path completo del file che DEVE cominciare con '/' SLASH
     *
     * @return true se il file esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteFile(final String absolutePathFileWithSuffixToBeChecked) {
        return checkFile(absolutePathFileWithSuffixToBeChecked).isValido();
    }


    /**
     * Controlla l'esistenza di un file <br>
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param pathDirectoryToBeChecked path completo della directory che DEVE cominciare con '/' SLASH
     * @param fileName                 da controllare
     *
     * @return true se il file esiste, false se non sono rispettate le condizioni della richiesta
     */
    public boolean isEsisteFile(final String pathDirectoryToBeChecked, final String fileName) {
        return isEsisteFile(pathDirectoryToBeChecked + SLASH + fileName);
    }


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult creaDirectory(final File directoryToBeCreated) {
        AResult result = checkFileDirectory("creaDirectory", directoryToBeCreated);
        if (result.isErrato()) {
            return result;
        }
        String message;

        if (!this.isNotSuffix(directoryToBeCreated.getAbsolutePath())) {
            message = String.format("%s%s%s", NON_E_DIRECTORY, FORWARD, directoryToBeCreated.getAbsolutePath());
            logger.error(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.errorMessage(message);
        }

        try {
            directoryToBeCreated.mkdirs();
            message = String.format("%s%s%s", CREATA_DIRECTORY, FORWARD, directoryToBeCreated.getAbsolutePath());
            logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.validMessage(message);
        } catch (Exception unErrore) {
            message = String.format("%s%s%s", NON_CREATA_DIRECTORY, FORWARD, directoryToBeCreated.getAbsolutePath());
            logger.error(new WrapLog().exception(unErrore).usaDb().message(message));
            return result.errorMessage(message);
        }
    }


    /**
     * Crea una nuova directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeCreated path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult creaDirectory(final String absolutePathDirectoryToBeCreated) {
        AResult result = checkPath("creaDirectory", absolutePathDirectoryToBeCreated);
        if (result.isErrato()) {
            return result;
        }

        return creaDirectory(new File(absolutePathDirectoryToBeCreated));
    }


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param fileToBeCreated con path completo che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult creaFile(final File fileToBeCreated) {
        AResult result = checkFileDirectory("creaFile", fileToBeCreated);
        if (result.isErrato()) {
            return result;
        }
        String message;

        if (this.isNotSuffix(fileToBeCreated.getAbsolutePath())) {
            message = String.format("%s%s%s", PATH_SENZA_SUFFIX, FORWARD, fileToBeCreated.getAbsolutePath());
            logger.error(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.errorMessage(message);
        }

        try {
            fileToBeCreated.createNewFile();
            message = String.format("%s%s%s", CREATO_FILE, FORWARD, fileToBeCreated.getAbsolutePath());
            //            logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.validMessage(message);
        } catch (Exception unErrore) {
            return creaDirectoryParentAndFile(fileToBeCreated);
        }
    }


    /**
     * Crea un nuovo file
     * <p>
     * Il file DEVE essere costruita col path completo, altrimenti assume che sia nella directory in uso corrente
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo ed iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     * Se manca la directory, viene creata dal System <br>
     *
     * @param absolutePathFileWithSuffixToBeCreated path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult creaFile(final String absolutePathFileWithSuffixToBeCreated) {
        AResult result = checkPath("creaFile", absolutePathFileWithSuffixToBeCreated);
        if (result.isErrato()) {
            return result;
        }

        return creaFile(new File(absolutePathFileWithSuffixToBeCreated));
    }


    /**
     * Creazioni di una directory 'parent' <br>
     * Se manca il path completo alla creazione di un file, creo la directory 'parent' di quel file <br>
     * Riprovo la creazione del file <br>
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult creaDirectoryParentAndFile(final File fileToBeCreated) {
        AResult result = AResult.build().method("creaDirectoryParentAndFile").target(fileToBeCreated.getAbsolutePath());
        String message;
        String parentDirectoryName = VUOTA;
        File parentDirectoryFile;

        if (fileToBeCreated != null) {
            parentDirectoryName = fileToBeCreated.getParent();
            parentDirectoryFile = new File(parentDirectoryName);
            parentDirectoryFile.mkdirs();
        }

        if (isEsisteDirectory(parentDirectoryName)) {
            try {
                fileToBeCreated.createNewFile();
                message = String.format("%s%s%s", CREATO_FILE, FORWARD, fileToBeCreated.getAbsolutePath());
                //                logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
                return result.validMessage(message);
            } catch (Exception unErrore) {
                message = String.format("Errore nel path per la creazione del file %s", fileToBeCreated.getAbsolutePath());
                logger.error(new WrapLog().exception(unErrore).message(message).type(AETypeLog.file));
                return result.errorMessage(message);
            }
        }
        else {
            //            message = String.format("%s%s%s", DIRECTORY_MANCANTE, FORWARD, parentDirectoryName + SLASH);
            message = String.format("Non sono riuscito a creare la directory necessaria per il file %s", fileToBeCreated.getAbsolutePath());
            logger.error(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.errorMessage(message);
        }
    }


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param directoryToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult deleteDirectory(final File directoryToBeDeleted) {
        AResult result = checkFileDirectory("deleteDirectory", directoryToBeDeleted);
        if (result.isErrato()) {
            return result;
        }
        String message;

        if (!directoryToBeDeleted.exists()) {
            message = String.format("%s%s%s", NON_ESISTE_DIRECTORY, FORWARD, directoryToBeDeleted.getAbsolutePath());
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }

        if (directoryToBeDeleted.delete()) {
            message = String.format("%s%s%s", CANCELLATA_DIRECTORY, FORWARD, directoryToBeDeleted.getAbsolutePath());
            logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.validMessage(message);
        }
        else {
            try {
                FileUtils.deleteDirectory(directoryToBeDeleted);
                message = String.format("%s%s%s", CANCELLATA_DIRECTORY, FORWARD, directoryToBeDeleted.getAbsolutePath());
                logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
                return result.validMessage(message);
            } catch (Exception unErrore) {
                message = String.format("%s%s%s", NON_CANCELLATA_DIRECTORY, FORWARD, directoryToBeDeleted.getAbsolutePath());
                logger.info(new WrapLog().message(message).type(AETypeLog.file));
                logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb().type(AETypeLog.file));
                return result.errorMessage(unErrore.getMessage());
            }
        }
    }


    /**
     * Cancella una directory
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathDirectoryToBeDeleted path completo della directory che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult deleteDirectory(final String absolutePathDirectoryToBeDeleted) {
        AResult result = checkPath("deleteDirectory", absolutePathDirectoryToBeDeleted);
        if (result.isErrato()) {
            return result;
        }

        return deleteDirectory(new File(absolutePathDirectoryToBeDeleted));
    }


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param fileToBeDeleted con path completo che DEVE cominciare con '/' SLASH
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult deleteFile(final File fileToBeDeleted) {
        AResult result = checkFileDirectory("deleteFile", fileToBeDeleted);
        if (result.isErrato()) {
            return result;
        }
        String message;

        if (this.isNotSuffix(fileToBeDeleted.getAbsolutePath())) {
            message = String.format("%s%s%s", PATH_SENZA_SUFFIX, FORWARD, fileToBeDeleted.getAbsolutePath());
            logger.error(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.errorMessage(message);
        }

        if (!fileToBeDeleted.exists()) {
            message = String.format("%s%s%s", NON_ESISTE_FILE, FORWARD, fileToBeDeleted.getAbsolutePath());
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }

        if (fileToBeDeleted.delete()) {
            message = String.format("%s%s%s", CANCELLATO_FILE, FORWARD, fileToBeDeleted.getAbsolutePath());
            logger.info(new WrapLog().exception(new AlgosException(message)).type(AETypeLog.file));
            return result.validMessage(message);
        }
        else {
            message = String.format("%s%s%s", NON_CANCELLATO_FILE, FORWARD, fileToBeDeleted.getAbsolutePath());
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }
    }


    /**
     * Cancella un file
     * <p>
     * Il path non deve essere nullo <br>
     * Il path non deve essere vuoto <br>
     * Il path deve essere completo e iniziare con uno 'slash' <br>
     * Il path deve essere completo e terminare con un 'suffix' <br>
     * La richiesta è CASE INSENSITIVE (maiuscole e minuscole SONO uguali) <br>
     *
     * @param absolutePathFileWithSuffixToBeCanceled path completo del file che DEVE cominciare con '/' SLASH e compreso il suffisso
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult deleteFile(final String absolutePathFileWithSuffixToBeCanceled) {
        AResult result = checkPath("deleteFile", absolutePathFileWithSuffixToBeCanceled);
        if (result.isErrato()) {
            return result;
        }

        return deleteFile(new File(absolutePathFileWithSuffixToBeCanceled));
    }


    /**
     * Copia un file <br>
     * <p>
     * Controlla che siano validi i path di riferimento <br>
     * Controlla che esista il path del file sorgente  <br>
     * Se manca il file sorgente, non fa nulla <br>
     * Se esiste il file di destinazione ed è AECopyFile.soloSeNonEsiste, non fa nulla <br>
     * Se esiste il file di destinazione ed è AECopyDir.sovrascriveSempreAncheSeEsiste, lo sovrascrive <br>
     * Se esiste il file di destinazione ed è AECopyFile.checkFlagSeEsiste, controlla il flag sovraScrivibile <br>
     * Nei messaggi di avviso, accorcia il destPath eliminando i livelli precedenti alla directory indicata <br>
     *
     * @param typeCopy    modalità di comportamento se esiste il file di destinazione
     * @param srcPathDir  path della directory sorgente
     * @param destPathDir path della directory destinazione
     * @param nomeFile    nome del file completo di suffisso
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult copyFile(final AECopy typeCopy, String srcPathDir, String destPathDir, final String nomeFile) {
        AResult result = AResult.build().method("copyFile").target(nomeFile);
        String message;
        srcPathDir = srcPathDir.endsWith(SLASH) ? srcPathDir : srcPathDir + SLASH;
        destPathDir = destPathDir.endsWith(SLASH) ? destPathDir : destPathDir + SLASH;
        String srcPath = srcPathDir + nomeFile;
        String destPath = destPathDir + nomeFile;
        String path = this.findPathBreve(destPathDir);
        File fileSrc = new File(srcPath);
        File fileDest = new File(destPath);
        String filePath = path + SLASH + nomeFile;
        String srcText;
        String destText;

        if (typeCopy == null) {
            message = "Manca il type AECopy";
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }
        result = result.type(typeCopy.getDescrizione());

        if (typeCopy.getType() != AECopyType.file) {
            message = String.format("Il type.%s previsto non è compatibile col metodo %s", typeCopy, result.getMethod());
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }

        if (textService.isEmpty(nomeFile)) {
            message = "Manca il nome del file";
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }

        if (textService.isEmpty(destPathDir)) {
            message = "Manca il nome della directory di destinazione";
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }

        result = checkPath("copyFile", destPath).target(nomeFile).type(typeCopy.getDescrizione());
        if (result.isErrato()) {
            return result;
        }

        if (!new File(srcPath).exists()) {
            message = String.format("Non sono riuscito a trovare il file sorgente %s nella directory %s", nomeFile, srcPathDir);
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }

        switch (typeCopy) {
            //esiste ed è uguale
            //esiste ma viene modificato
            //non esiste
            case fileDelete:
                if (fileDest.exists()) {
                    srcText = leggeFile(srcPath);
                    destText = leggeFile(destPath);
                    if (destText.equals(srcText)) {
                        result.setTagCode(AEKeyFile.esistente.name());
                        message = "Il file: " + path + " esisteva già e non è stato modificato.";
                        return result.validMessage(message);
                    }
                    else {
                        try {
                            FileUtils.copyFile(fileSrc, fileDest);
                            result.setTagCode(AEKeyFile.modificato.name());
                            message = "Il file: " + path + " esisteva già ma è stato modificato.";
                            return result.validMessage(message);
                        } catch (Exception unErrore) {
                            logger.error(new WrapLog().exception(unErrore).usaDb());
                            return result.errorMessage(unErrore.getMessage());
                        }
                    }
                }
                else {
                    try {
                        FileUtils.copyFile(fileSrc, fileDest);
                        result.setTagCode(AEKeyFile.creato.name());
                        message = "Il file: " + path + " non esisteva ed è stato copiato.";
                        return result.validMessage(message);
                    } catch (Exception unErrore) {
                        logger.error(new WrapLog().exception(unErrore).usaDb());
                        return result.errorMessage(unErrore.getMessage());
                    }
                }

            case fileOnly:
                if (fileDest.exists()) {
                    result.setTagCode(AEKeyFile.esistente.name());
                    message = String.format("Il file: %s esisteva già e non è stato modificato.", nomeFile);
                    return result.validMessage(message);
                }
                else {
                    try {
                        FileUtils.copyFile(fileSrc, fileDest);
                        result.setTagCode(AEKeyFile.creato.name());
                        message = "Il file: " + filePath + " non esisteva ed è stato copiato.";
                        return result.validMessage(message);
                    } catch (Exception unErrore) {
                        logger.error(new WrapLog().exception(unErrore).usaDb());
                        return result.errorMessage(unErrore.getMessage());
                    }
                }
            case fileCheck:
                logger.warn(AETypeLog.file, new AlgosException(SWITCH_FUTURO));
                return result.errorMessage(SWITCH_FUTURO);
            default:
                logger.warn(AETypeLog.file, new AlgosException(SWITCH));
                return result.errorMessage(SWITCH);
        }
    }

    public AResult copyDirectory(final AECopy typeCopy, String srcPath, String destPath) {
        return copyDirectory(typeCopy, srcPath, destPath, VUOTA, VUOTA);
    }

    /**
     * Copia una directory <br>
     * <p>
     * Controlla che siano validi i path di riferimento <br>
     * Controlla che esista la directory sorgente da copiare <br>
     * Se manca la directory sorgente, non fa nulla <br>
     * Se non esiste la directory di destinazione, la crea <br>
     * Se esiste la directory di destinazione ed è AECopyDir.soloSeNonEsiste, non fa nulla <br>
     * Se esiste la directory di destinazione ed è AECopyDir.deletingAll, la cancella e poi la copia <br>
     * Se esiste la directory di destinazione ed è AECopyDir.addingOnly, la integra aggiungendo file/cartelle <br>
     * Nei messaggi di avviso, accorcia il destPath eliminando i livelli precedenti alla directory indicata <br>
     *
     * @param typeCopy modalità di comportamento se esiste la directory di destinazione
     * @param srcPath  nome completo della directory sorgente
     * @param destPath nome completo della directory destinazione
     *
     * @return wrapper di informazioni risultanti
     */
    public AResult copyDirectory(final AECopy typeCopy, String srcPath, String destPath, String srcToken, String destToken) {
        AResult result = AResult.build().method("copyDirectory").target(destPath);
        String message;
        String tag;
        String test = "/test/";
        String dir = "directory";
        String path;
        File dirSrc = new File(srcPath);
        File dirDest = new File(destPath);
        List<String> filesSorgenti;
        List<String> filesDestinazioneAnte = new ArrayList<>();
        List<String> filesDestinazionePost;
        List<String> filesAggiunti = new ArrayList<>();
        List<String> filesModificati = new ArrayList<>();
        List<String> filesUguali = new ArrayList<>();
        List<String> filesTokenModificati = new ArrayList<>();
        List<String> filesTokenUguali = new ArrayList<>();
        List<String> filesRimossi;
        LinkedHashMap resultMap = new LinkedHashMap();

        if (typeCopy == null) {
            result.setType(NULL);
            message = "Manca il type AECopy";
            return result.errorMessage(message);
        }

        if (typeCopy.getType() == AECopyType.file || typeCopy.getType() == AECopyType.source) {
            result.setType(typeCopy.name());
            message = "Il type AECopy non è adatto ad una directory";
            return result.errorMessage(message);
        }

        if (textService.isEmpty(srcPath) || textService.isEmpty(destPath)) {
            tag = textService.isEmpty(srcPath) ? "srcPath" : "destPath";
            message = String.format("Manca il '%s' della directory da copiare.", tag);
            return result.setErrorMessage(message);
        }
        srcPath = srcPath.endsWith(SLASH) ? srcPath : srcPath + SLASH;
        destPath = destPath.endsWith(SLASH) ? destPath : destPath + SLASH;
        path = this.findPathBreve(destPath);
        dir = srcPath.contains(test) ? dir + " di test" : dir;

        result = result.type(typeCopy + FORWARD + typeCopy.getDescrizione());

        if (typeCopy.getType() != AECopyType.directory) {
            message = String.format("Il type.%s previsto non è compatibile col metodo %s", typeCopy, result.getMethod());
            logger.warn(AETypeLog.file, new AlgosException(message));
            return result.errorMessage(message);
        }

        if (!dirSrc.isDirectory()) {
            message = String.format("Non esiste la %s sorgente '%s' da copiare.", srcPath, dir);
            return result.setErrorMessage(message);
        }

        if (typeCopy == AECopy.dirFilesModificaToken) {
            if (textService.isEmpty(srcToken)) {
                message = "Manca il token sorgente";
                return result.errorMessage(message);
            }
            if (textService.isEmpty(destToken)) {
                message = "Manca il token destinazione";
                return result.errorMessage(message);
            }
        }

        filesSorgenti = getFilesName(srcPath);
        resultMap.put(AEKeyMapFile.sorgenti.name(), filesSorgenti);
        result.setMappa(resultMap);

        //--recupero i files esistenti nella destinazione
        if (dirDest.exists()) {
            filesDestinazioneAnte = getFilesName(destPath);
            resultMap.put(AEKeyMapFile.destinazioneAnte.name(), filesDestinazioneAnte);
        }

        switch (typeCopy) {
            case dirOnly:
                if (dirDest.exists()) {
                    result.setTagCode(AEKeyDir.esistente.name());

                    filesUguali = getFilesName(destPath);
                    resultMap.put(AEKeyMapFile.uguali.name(), filesUguali);

                    filesDestinazionePost = getFilesName(destPath);
                    resultMap.put(AEKeyMapFile.destinazionePost.name(), filesDestinazionePost);

                    message = String.format("La %s '%s' esisteva già e non è stata toccata.", dir, path);
                    result.setValidMessage(message);
                }
                else {
                    result = creaNuova(result, dirSrc, dirDest, path);
                }
                return result;

            case dirDelete:
                if (dirDest.exists()) {

                    try {
                        FileUtils.deleteDirectory(dirDest);
                        FileUtils.copyDirectory(dirSrc, dirDest);
                    } catch (Exception unErrore) {
                        logger.error(new WrapLog().exception(unErrore).usaDb());
                        return result.setErrorMessage(unErrore.getMessage());
                    }
                    result.setTagCode(AEKeyDir.creataCancellata.name());

                    filesRimossi = filesDestinazioneAnte;
                    resultMap.put(AEKeyMapFile.eliminati.name(), filesRimossi);

                    filesAggiunti = getFilesName(destPath);
                    resultMap.put(AEKeyMapFile.aggiuntiNuovi.name(), filesAggiunti);

                    filesDestinazionePost = getFilesName(destPath);
                    resultMap.put(AEKeyMapFile.destinazionePost.name(), filesDestinazionePost);

                    message = String.format("La %s '%s' esisteva già ma è stata cancellata e creata ex-novo.", dir, path);
                    result.setValidMessage(message);
                }
                else {
                    result = creaNuova(result, dirSrc, dirDest, path);
                }
                return result;

            case dirFilesAddOnly:
                if (dirDest.exists()) {
                    //--copio SOLO i sorgenti NON presenti nella destinazione
                    for (String nomeFile : filesSorgenti) {
                        if (!filesDestinazioneAnte.contains(nomeFile)) {
                            copyFile(AECopy.fileOnly, srcPath, destPath, nomeFile);
                            filesAggiunti.add(nomeFile);
                        }
                    }
                    resultMap.put(AEKeyMapFile.aggiuntiNuovi.name(), filesAggiunti);
                    resultMap.put(AEKeyMapFile.uguali.name(), filesDestinazioneAnte);

                    filesDestinazionePost = getFilesName(destPath);
                    resultMap.put(AEKeyMapFile.destinazionePost.name(), filesDestinazionePost);

                    if (filesAggiunti.size() > 0) {
                        message = String.format("La %s '%s' esisteva già ma è stata integrata con nuovi files.", dir, path);
                        result.setTagCode(AEKeyDir.integrata.name());
                    }
                    else {
                        message = String.format("La %s '%s' esisteva già e non è stato aggiunto nessun nuovo file.", dir, path);
                        result.setTagCode(AEKeyDir.esistente.name());
                    }
                    result.setValidMessage(message);
                }
                else {
                    result = creaNuova(result, dirSrc, dirDest, path);
                }
                return result;

            case dirFilesModifica, dirFilesModificaToken:
                if (dirDest.exists()) {
                    //--controlla TUTTI i sorgenti
                    for (String nomeFile : filesSorgenti) {
                        //--se c'è, lo controlla
                        if (filesDestinazioneAnte.contains(nomeFile)) {
                            //--se è diverso, lo modifica
                            if (!isUguale(srcPath, destPath, nomeFile)) {
                                //--diversi però controlla le differenze del token
                                if (typeCopy == AECopy.dirFilesModificaToken) {
                                    //--file uguali a parte il token
                                    if (!isUgualeToken(srcPath, destPath, nomeFile, srcToken, destToken)) {
                                        copyFile(AECopy.fileDelete, srcPath, destPath, nomeFile);
                                        filesTokenModificati.add(nomeFile);
                                    }
                                    else {
                                        copyFile(AECopy.fileDelete, srcPath, destPath, nomeFile);
                                        filesTokenUguali.add(nomeFile);
                                    }
                                }
                                //--diversi e non controlla le differenze del token
                                else {
                                    copyFile(AECopy.fileDelete, srcPath, destPath, nomeFile);
                                    filesModificati.add(nomeFile);
                                }
                            }
                            else {
                                if (typeCopy == AECopy.dirFilesModificaToken) {
                                    filesTokenUguali.add(nomeFile);
                                }
                                else {
                                    filesUguali.add(nomeFile);
                                }
                            }
                        }
                        //--se manca, lo aggiunge
                        else {
                            copyFile(AECopy.fileOnly, srcPath, destPath, nomeFile);
                            filesAggiunti.add(nomeFile);
                        }
                    }

                    for (String nomeFile : filesDestinazioneAnte) {
                        if (!filesAggiunti.contains(nomeFile) && !filesTokenUguali.contains(nomeFile)) {
                            filesUguali.add(nomeFile);
                        }
                    }
                    resultMap.put(AEKeyMapFile.aggiuntiNuovi.name(), filesAggiunti);
                    if (typeCopy == AECopy.dirFilesModificaToken) {
                        resultMap.put(AEKeyMapFile.tokenUguali.name(), filesTokenUguali);
                        resultMap.put(AEKeyMapFile.tokenModificati.name(), filesTokenModificati);
                    }
                    else {
                        resultMap.put(AEKeyMapFile.uguali.name(), filesUguali);
                        resultMap.put(AEKeyMapFile.modificati.name(), filesModificati);
                    }

                    filesDestinazionePost = getFilesName(destPath);
                    resultMap.put(AEKeyMapFile.destinazionePost.name(), filesDestinazionePost);

                    if (typeCopy == AECopy.dirFilesModificaToken) {
                        if (filesAggiunti.size() == 0 && filesTokenModificati.size() == 0) {
                            result.setTagCode(AEKeyDir.esistente.name());
                            message = String.format("La %s '%s' esisteva già e non è stato aggiunto/modificato nessun file.", dir, path);
                        }
                        else {
                            result.setTagCode(AEKeyDir.integrata.name());
                            message = String.format("La %s '%s' esisteva già; aggiunti: %s; modificati (token): %s", dir, path, filesAggiunti, filesTokenModificati);
                        }
                    }
                    else {
                        if (filesAggiunti.size() == 0 && filesModificati.size() == 0) {
                            result.setTagCode(AEKeyDir.esistente.name());
                            message = String.format("La %s '%s' esisteva già e non è stato aggiunto/modificato nessun file.", dir, path);
                        }
                        else {
                            result.setTagCode(AEKeyDir.integrata.name());
                            message = String.format("La %s '%s' esisteva già; aggiunti: %s; modificati: %s", dir, path, filesAggiunti, filesModificati);
                        }
                    }
                    result.setValidMessage(message);
                }
                else {
                    result = creaNuova(result, dirSrc, dirDest, path);
                }
                return result;

            default:
                result.errorMessage(SWITCH);
                logger.warn(AETypeLog.file, new AlgosException(SWITCH));
                break;
        }

        return result;
    }


    public AResult creaNuova(AResult result, File dirSrc, File dirDest, String path) {
        LinkedHashMap<String, List<String>> resultMap = result.getMappa();
        List<String> filesCreatiDestinazionePost;
        String message;

        try {
            FileUtils.copyDirectory(dirSrc, dirDest);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
            return result.setErrorMessage(unErrore.getMessage());
        }
        result.setTagCode(AEKeyDir.creataNuova.name());

        resultMap.put(AEKeyMapFile.destinazioneAnte.name(), new ArrayList<>());

        filesCreatiDestinazionePost = getFilesName(dirDest.getAbsolutePath());
        resultMap.put(AEKeyMapFile.aggiuntiNuovi.name(), filesCreatiDestinazionePost);
        resultMap.put(AEKeyMapFile.destinazionePost.name(), filesCreatiDestinazionePost);

        message = String.format("La directory '%s' non esisteva ed è stata creata.", path);

        return result.validMessage(message);
    }


    public boolean isUguale(String srcPath, String destPath, String nomeFile) {
        return leggeFile(srcPath + nomeFile).equals(leggeFile(destPath + nomeFile));
    }

    public boolean isUgualeToken(String srcPath, String destPath, String nomeFile, String srcToken, String destToken) {
        String sorgente = leggeFile(srcPath + nomeFile);
        String destinazione = leggeFile(destPath + nomeFile);
        sorgente = textService.sostituisce(sorgente, srcToken, destToken);
        return sorgente.equals(destinazione);
    }

    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     */
    public AResult scriveNewFile(String pathFileToBeWritten, String text) {
        AResult result;
        String message;

        if (isEsisteFile(pathFileToBeWritten)) {
            message = String.format("Il file: %s esisteva già e non è stato modificato", pathFileToBeWritten);
            result = AResult.errato(message);
        }
        else {
            creaFile(pathFileToBeWritten);
            sovraScriveFile(pathFileToBeWritten, text);
            message = String.format("Il file: %s non esisteva ed è stato creato", pathFileToBeWritten);
            result = AResult.valido(message);
        }

        return result;
    }


    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param testo               contenuto del file
     */
    public AResult scriveFile(AECopy typeCopy, String pathFileToBeWritten, String testo) {
        return scriveFile(typeCopy, pathFileToBeWritten, testo, VUOTA);
    }

    /**
     * Scrive un file
     * Se non esiste, lo crea
     *
     * @param pathFileToBeWritten nome completo del file
     * @param testo               contenuto del file
     */
    public AResult scriveFile(AECopy typeCopy, String pathFileToBeWritten, String testo, String firstDirectory) {
        AResult result = AResult.errato();
        String message = VUOTA;
        String path;

        if (textService.isValid(firstDirectory)) {
            path = this.findPathBreve(pathFileToBeWritten);
        }
        else {
            path = pathFileToBeWritten.substring((pathFileToBeWritten.lastIndexOf(SLASH) + SLASH.length()));
        }

        switch (typeCopy) {
            case fileCheck, sourceCheckFlagSeEsiste -> {
            }
            case fileOnly, sourceSoloSeNonEsiste -> {
                if (isEsisteFile(pathFileToBeWritten)) {
                    message = String.format("Il file '%s' esisteva già e non è stato modificato", path);
                    result.setErrorMessage(message);
                }
                else {
                    sovraScriveFile(pathFileToBeWritten, testo);
                    message = String.format("Il file '%s' non esisteva ed è stato creato", path);
                    result = AResult.valido(message);
                }
            }
            case fileDelete, sourceSovrascriveSempreAncheSeEsiste -> {
                if (isEsisteFile(pathFileToBeWritten)) {
                    message = String.format("Il file '%s' esisteva già ma è stato modificato", path);
                }
                else {
                    message = String.format("Il file '%s' non esisteva ed è stato creato", path);
                }
                sovraScriveFile(pathFileToBeWritten, testo);
                result = AResult.valido(message);
            }
            default -> {
            }
        }

        return result;
    }

    public boolean sovraScriveFile(File fileToBeWritten, String text) {
        return sovraScriveFile(fileToBeWritten.getAbsolutePath(), text);
    }

    /**
     * Sovrascrive un file
     *
     * @param pathFileToBeWritten nome completo del file
     * @param text                contenuto del file
     */
    public boolean sovraScriveFile(String pathFileToBeWritten, String text) {
        boolean status = false;
        File fileToBeWritten;
        FileWriter fileWriter = null;

        fileToBeWritten = new File(pathFileToBeWritten);
        creaDirectoryParentAndFile(fileToBeWritten);

        try {
            fileWriter = new FileWriter(fileToBeWritten);
            fileWriter.write(text);
            fileWriter.flush();
            status = true;
        } catch (Exception unErrore) {
            int a = 87;
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception unErrore) {
            }
        }

        return status;
    }


    /**
     * Legge un file
     *
     * @param pathFileToBeRead nome completo del file
     */
    public String leggeFile(String pathFileToBeRead) {
        String testo = VUOTA;
        String aCapo = CAPO;
        String currentLine;

        //-- non va, perché se arriva it/algos/Alfa.java becca anche il .java
        //        nameFileToBeRead=  nameFileToBeRead.replaceAll("\\.","/");

        try (BufferedReader br = new BufferedReader(new FileReader(pathFileToBeRead))) {
            while ((currentLine = br.readLine()) != null) {
                testo += currentLine;
                testo += "\n";
            }

            testo = textService.levaCoda(testo, aCapo);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb().type(AETypeLog.file));
        }

        return testo;
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     *
     * @return lista di liste di valori, senza titoli
     */
    public List<List<String>> leggeListaCSV(final String pathFileToBeRead) {
        return leggeListaCSV(pathFileToBeRead, VIRGOLA, CAPO);
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     * @param sepColonna       normalmente una virgola
     * @param sepRiga          normalmente un \n
     *
     * @return lista di liste di valori, senza titoli
     */
    public List<List<String>> leggeListaCSV(final String pathFileToBeRead, final String sepColonna, final String sepRiga) {
        List<List<String>> lista = new ArrayList<>();
        List<String> riga = null;
        String[] righe;
        String[] colonne;

        String testo = leggeFile(pathFileToBeRead);

        if (textService.isValid(testo)) {
            righe = testo.split(sepRiga);
            if (righe != null && righe.length > 0) {
                for (String rigaTxt : righe) {
                    riga = null;
                    colonne = rigaTxt.split(sepColonna);
                    if (colonne != null && colonne.length > 0) {
                        riga = new ArrayList<>();
                        for (String colonna : colonne) {
                            riga.add(colonna);
                        }
                    }
                    if (riga != null) {
                        lista.add(riga);
                    }
                }
            }
        }

        return arrayService.isAllValid(lista) ? lista.subList(1, lista.size()) : lista;
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     *
     * @return lista di mappe di valori
     */
    public List<LinkedHashMap<String, String>> leggeMappaCSV(final String pathFileToBeRead) {
        return leggeMappaCSV(pathFileToBeRead, VIRGOLA, CAPO);
    }


    /**
     * Legge un file CSV <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     * @param sepColonna       normalmente una virgola
     * @param sepRiga          normalmente un \n
     *
     * @return lista di mappe di valori
     */
    public List<LinkedHashMap<String, String>> leggeMappaCSV(final String pathFileToBeRead, final String sepColonna, final String sepRiga) {
        List<LinkedHashMap<String, String>> lista = new ArrayList<>();
        LinkedHashMap<String, String> mappa = null;
        String[] righe;
        String[] titoli;
        String[] colonne;

        String testo = leggeFile(pathFileToBeRead);
        if (textService.isValid(testo)) {
            righe = testo.split(sepRiga);
            titoli = righe[0].split(sepColonna);

            if (righe != null && righe.length > 0) {
                for (int k = 1; k < righe.length; k++) {
                    mappa = null;
                    colonne = righe[k].split(sepColonna);
                    if (colonne != null && colonne.length > 0) {
                        mappa = new LinkedHashMap<>();
                        for (int j = 0; j < colonne.length; j++) {
                            if (j < colonne.length) {
                                mappa.put(titoli[j], colonne[j]);
                            }
                        }
                    }
                    if (mappa != null) {
                        lista.add(mappa);
                    }
                }
            }
        }

        return lista;
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirectoriesAbsolutePathName(final String pathDirectoryToBeScanned) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(pathDirectoryToBeScanned);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getAbsolutePath());
            }
        }

        return subDirectoryName;
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirectoriesName(final String pathDirectoryToBeScanned) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(pathDirectoryToBeScanned);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getName());
            }
        }

        return subDirectoryName;
    }

    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirPath(final String pathDirectoryToBeScanned) {
        return getSubDirectories(pathDirectoryToBeScanned)
                .stream()
                .map(entry -> entry.getAbsolutePath())
                .collect(Collectors.toList());
    }

    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<String> getSubDirName(final String pathDirectoryToBeScanned) {
        return getSubDirectories(pathDirectoryToBeScanned)
                .stream()
                .map(entry -> entry.getName())
                .collect(Collectors.toList());
    }


    /**
     * Estrae le sub-directories da una directory <br>
     */
    public List<String> getSubDirectoriesName(final File fileSorgente) {
        List<String> subDirectoryName = new ArrayList<>();
        List<File> subDirectory = getSubDirectories(fileSorgente);

        if (subDirectory != null) {
            for (File file : subDirectory) {
                subDirectoryName.add(file.getName());
            }
        }

        return subDirectoryName;
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<File> getSubDirectories(final String pathDirectoryToBeScanned) {
        return getSubDirectories(new File(pathDirectoryToBeScanned));
    }


    /**
     * Estrae le sub-directories da una directory <br>
     *
     * @param directoryToBeScanned della directory
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubDirectories(final File directoryToBeScanned) {
        List<File> subDirectory = new ArrayList<>();
        File[] allFiles = null;

        if (directoryToBeScanned != null) {
            allFiles = directoryToBeScanned.listFiles();
        }

        if (allFiles != null) {
            subDirectory = new ArrayList<>();
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    subDirectory.add(file);
                }
            }
        }

        return subDirectory;
    }


    /**
     * Estrae le sub-directories da un sotto-livello di una directory <br>
     * La dirInterna non è, ovviamente, al primo livello della directory altrimenti chiamerei getSubDirectories <br>
     *
     * @param pathDirectoryToBeScanned della directory
     * @param dirInterna               da scandagliare
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubSubDirectories(final String pathDirectoryToBeScanned, final String dirInterna) {
        return getSubSubDirectories(new File(pathDirectoryToBeScanned), dirInterna);
    }


    /**
     * Estrae le sub-directories da un sotto-livello di una directory <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return lista di sub-directory SENZA files
     */
    public List<File> getSubSubDirectories(final File directoryToBeScanned, String dirInterna) {
        String subDir = directoryToBeScanned.getAbsolutePath();

        if (subDir.endsWith(SLASH)) {
            subDir = textService.levaCoda(subDir, SLASH);
        }

        if (dirInterna.startsWith(SLASH)) {
            dirInterna = textService.levaTesta(dirInterna, SLASH);
        }

        String newPath = subDir + SLASH + dirInterna;
        File subFile = new File(newPath);

        return getSubDirectories(subFile);
    }


    /**
     * Controlla se una sotto-directory esiste <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se esiste
     */
    public boolean isEsisteSubDirectory(final File directoryToBeScanned, final String dirInterna) {
        if (directoryToBeScanned.getAbsolutePath().endsWith(SLASH)) {
            return isEsisteDirectory(directoryToBeScanned.getAbsolutePath() + dirInterna);
        }
        else {
            return isEsisteDirectory(directoryToBeScanned.getAbsolutePath() + SLASH + dirInterna);
        }
    }


    /**
     * Controlla se una sotto-directory è piena <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se è piena
     */
    public boolean isPienaSubDirectory(final File directoryToBeScanned, final String dirInterna) {
        return arrayService.isAllValid(getSubSubDirectories(directoryToBeScanned, dirInterna));
    }


    /**
     * Controlla se una sotto-directory è vuota <br>
     *
     * @param directoryToBeScanned della directory
     * @param dirInterna           da scandagliare
     *
     * @return true se è vuota
     */
    public boolean isVuotaSubDirectory(final File directoryToBeScanned, final String dirInterna) {
        return arrayService.isEmpty(getSubSubDirectories(directoryToBeScanned, dirInterna));
    }


    /**
     * Elimina l'ultima directory da un path <br>
     * <p>
     * Esegue solo se il path è valido <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return path ridotto in uscita
     */
    public String levaDirectoryFinale(final String pathIn) {
        String pathOut = pathIn.trim();

        if (textService.isValid(pathOut) && pathOut.endsWith(SLASH)) {
            pathOut = textService.levaCoda(pathOut, SLASH);
            pathOut = textService.levaCodaDaUltimo(pathOut, SLASH) + SLASH;
        }

        return pathOut.trim();
    }

    /**
     * Recupera l'ultima directory da un path <br>
     * <p>
     * Esegue solo se il path è valido <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return directory finale del path
     */
    public String lastDirectory(final String pathIn) {
        String pathOut = pathIn.trim();

        if (textService.isValid(pathOut) && pathOut.endsWith(SLASH)) {
            pathOut = textService.levaCoda(pathOut, SLASH);
            pathOut = pathOut.substring(pathOut.lastIndexOf(SLASH) + 1);
        }

        return pathOut.trim();
    }


    /**
     * Recupera l'ultima classe da un path <br>
     * <p>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return classe finale del path
     */
    public String estraeClasseFinale(final String pathIn) {
        String pathOut = pathIn.trim();

        if (textService.isValid(pathOut)) {
            pathOut = textService.levaCoda(pathOut, SLASH);
            if (pathOut.contains(SLASH)) {
                pathOut = pathOut.substring(pathOut.lastIndexOf(SLASH) + SLASH.length());
            }
            if (pathOut.contains(PUNTO)) {
                pathOut = pathOut.substring(pathOut.lastIndexOf(PUNTO) + PUNTO.length());
            }
        }

        return pathOut.trim();
    }

    /**
     * Recupera l'ultima classe da un path <br>
     * <p>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param pathIn in ingresso
     *
     * @return classe finale del path
     */
    public String estraeClasseFinaleSenzaJava(final String pathIn) {
        String pathOut = textService.levaCoda(pathIn, JAVA_SUFFIX);
        return estraeClasseFinale(pathOut);
    }

    /**
     * Estrae il path che contiene la directory indicata <br>
     * <p>
     * Esegue solo se il path è valido
     * Se la directory indicata è vuota, restituisce il path completo <br>
     * Se la directory indicata non esiste nel path, restituisce una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn            in ingresso
     * @param directoryFindPath di cui ricercare il path che la contiene
     *
     * @return path completo fino alla directory selezionata
     */
    public String findPathDirectory(String pathIn, String directoryFindPath) {
        String pathOut = pathIn.trim();
        String message;

        if (textService.isEmpty(pathIn)) {
            message = "Nullo il path in ingresso";
            logger.warn(AETypeLog.file, new AlgosException(message));
            return pathOut;
        }

        if (textService.isEmpty(directoryFindPath)) {
            message = "Nulla la directory in ingresso";
            logger.warn(AETypeLog.file, new AlgosException(message));
            return pathOut;
        }

        if (pathOut.contains(directoryFindPath)) {
            pathOut = pathOut.substring(0, pathOut.indexOf(directoryFindPath));
        }
        else {
            pathOut = VUOTA;
            message = "Non esiste la directory indicata nel path indicato";
            logger.warn(AETypeLog.file, new AlgosException(message));
        }

        return pathOut.trim();
    }

    /**
     * Estrae il path parziale da una directory indicata, escludendo il percorso iniziale <br>
     * <p>
     * La directory indicata è la prima con quel nome <br>
     * Esegue solo se il path è valido
     * Se la directory indicata non esiste nel path, restituisce tutto il path completo <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param pathIn in ingresso
     *
     * @return path parziale da una directory
     */
    public String findPathBreve(String pathIn) {
        String pathBreve = pathIn;
        String prefix = "../";

        String pathOut = pathIn.trim();

        if (textService.isValid(pathOut) && pathOut.endsWith(SLASH)) {
            pathOut = textService.levaCoda(pathOut, SLASH);
            pathOut = pathOut.substring(pathOut.lastIndexOf(SLASH) + 1);
            pathOut = prefix + pathOut;
        }

        return pathOut.trim();
    }


    /**
     * Recupera i progetti da una directory <br>
     * Controlla che la sotto-directory sia di un project e quindi contenga la cartella 'src' e questa non sia vuota <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<File> getAllProjects(final String pathDirectoryToBeScanned) {
        List<File> listaProjects = null;
        List<File> listaDirectory = getSubDirectories(new File(pathDirectoryToBeScanned));

        if (listaDirectory != null && listaDirectory.size() > 0) {
            listaProjects = new ArrayList<>();

            for (File file : listaDirectory) {
                if (isEsisteSubDirectory(file, DIR_PROGETTO)) {
                    listaProjects.add(file);
                }
            }
        }

        return listaProjects;
    }


    /**
     * Recupera i progetti vuoti da una directory <br>
     * Controlla che la sotto-directory sia di un project e quindi contenga la cartella 'src.main.java' <br>
     * Controlla che il progetto sia vuoto; deve essere vuota la cartella 'src.main.java' <br>
     *
     * @param pathDirectoryToBeScanned nome completo della directory
     */
    public List<File> getEmptyProjects(String pathDirectoryToBeScanned) {
        List<File> listaEmptyProjects = null;
        List<File> listaProjects = getAllProjects(pathDirectoryToBeScanned);

        if (listaProjects != null) {
            listaEmptyProjects = new ArrayList<>();
            for (File file : listaProjects) {
                if (isVuotaSubDirectory(file, DIR_PROGETTO_VUOTO)) {
                    listaEmptyProjects.add(file);
                }
            }
        }

        return listaEmptyProjects;
    }

    public boolean isContieneProgettoValido(String pathDirectoryProject) {
        return !isVuotaSubDirectory(new File(pathDirectoryProject), DIR_PROGETTO_VUOTO);
    }

    /**
     * Crea una lista di tutte le Entity esistenti nel modul
     * indicato <br>
     */
    public List<String> getModuleSubFilesEntity(String moduleName) throws AlgosException {
        String tagFinale = "/backend/packages";
        return getAllSubFilesEntity(PATH_PREFIX + moduleName + tagFinale);
    }

    /**
     * Crea una lista di tutte le Entity esistenti nella directory packages <br>
     */
    public List<String> getAllSubFilesEntity(String path) throws AlgosException {
        return getAllSubFilesJava(path)
                .stream()
                .filter(n -> !n.endsWith(SUFFIX_BACKEND))
                .filter(n -> !n.endsWith(SUFFIX_REPOSITORY))
                .filter(n -> !n.endsWith(SUFFIX_VIEW))
                .filter(n -> !n.endsWith(SUFFIX_DIALOG))
                .collect(Collectors.toList());
    }

    /**
     * Crea una lista di soli files java ricorsiva nelle sub-directory <br>
     *
     * @return canonicalName con i PUNTI di separazione e NON lo SLASH
     */
    public List<String> getAllSubFilesJava(String path) {
        if (reflectionService.isJarRunning()) {
            return getAllSubFilesJavaJAR(PATH_PREFIX_ALGOS + path);
        }
        else {
            return getAllSubFilesJavaIDE(PATH_PREFIX + path);
        }
    }

    /**
     * Crea una lista di soli files java ricorsiva nelle sub-directory <br>
     *
     * @return canonicalName con i PUNTI di separazione e NON lo SLASH
     */
    public List<String> getAllSubFilesJavaJAR(String dirPath) {
        ProtectionDomain domain;
        CodeSource codeSource;
        URL url;
        String jarPath = VUOTA;

        try {
            domain = FileService.class.getProtectionDomain();
            codeSource = domain.getCodeSource();
            url = codeSource.getLocation();
            jarPath = url.toString();
            jarPath = textService.levaCoda(jarPath, JAR_PATH_SUFFIX);
            jarPath = textService.levaTesta(jarPath, JAR_FILE_PREFIX);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        return fileService.scanJarDir(jarPath, dirPath);
    }

    /**
     * Crea una lista di soli files java ricorsiva nelle sub-directory <br>
     *
     * @return canonicalName con i PUNTI di separazione e NON lo SLASH
     */
    public List<String> getAllSubFilesJavaIDE(String path) {
        List<String> listaCanonicalNamesOnlyFilesJava = new ArrayList<>();
        List<String> listaPathNamesOnlyFiles = getAllSubPathFiles(path);
        String canonicalName;

        if (arrayService.isAllValid(listaPathNamesOnlyFiles)) {
            for (String pathName : listaPathNamesOnlyFiles) {
                if (pathName.endsWith(JAVA_SUFFIX)) {
                    canonicalName = textService.levaCoda(pathName, JAVA_SUFFIX);
                    canonicalName = canonicalName.replaceAll(SLASH, PUNTO);
                    listaCanonicalNamesOnlyFilesJava.add(canonicalName);
                }
            }
        }

        return listaCanonicalNamesOnlyFilesJava;
    }


    /**
     * Crea una lista di soli files ricorsiva nelle sub-directory <br>
     *
     * @return lista
     */
    public List<String> getAllSubPathFiles(String path) {
        List<String> listaPathNamesOnlyFiles = new ArrayList<>();
        List<String> listaAllPathNames = null;
        File unaDirectory = new File(path);
        Path start = Paths.get(unaDirectory.getAbsolutePath());

        try {
            listaAllPathNames = recursionSubPathNames(start);
        } catch (Exception unErrore) {
            //            throw AlgosException.crea(unErrore);
            int a = 87;
        }

        if (arrayService.isAllValid(listaAllPathNames)) {
            for (String pathName : listaAllPathNames) {
                if (isEsisteFile(pathName)) {
                    pathName = textService.levaTestoPrimaDi(pathName, PATH_PREFIX_ALGOS);
                    listaPathNamesOnlyFiles.add(pathName);
                }
            }
        }

        return listaPathNamesOnlyFiles;
    }

    /**
     * Crea una lista (path completo) di tutti i files della directory package del modulo corrente <br>
     *
     * @return lista dei path completi
     */
    public List<String> getPathModuloPackageFiles() {
        List<String> filesAll = new ArrayList<>();
        List<String> filesVaad24 = getPathModuloPackageFiles(VaadVar.moduloVaadin24);
        List<String> filesCurrent = getPathModuloPackageFiles(VaadVar.projectNameModulo);

        filesAll.addAll(filesVaad24);
        filesAll.addAll(filesCurrent);

        return filesAll;
    }


    /**
     * Crea una lista (path completo) di tutti i files della directory package del modulo indicato <br>
     *
     * @return lista dei path completi
     */
    public List<String> getPathModuloPackageFiles(final String nomeModulo) {
        String pathPackage;

        if (textService.isEmpty(nomeModulo)) {
            logger.error(new AlgosException("Manca il nome del modulo"));
            return null;
        }

        pathPackage = getPathDir(nomeModulo);
        return getAllSubPathFiles(pathPackage);
    }

    /**
     * Crea un path completo della directory indicata <br>
     */
    public String getPathDir(final String nomeDirectory) {
        String pathDir = VUOTA;

        if (textService.isEmpty(nomeDirectory)) {
            logger.error(new AlgosException("Manca il nome della directory"));
            return null;
        }

        pathDir += System.getProperty("user.dir") + SLASH;
        pathDir += PATH_PREFIX;
        pathDir += nomeDirectory + SLASH;

        return pathDir;
    }


    /**
     * Crea una lista (path completo) di tutti i files della directory package del modulo corrente <br>
     * Senza il suffisso JAVA_SUFFIX <br>
     *
     * @return lista dei path completi
     */
    public List<String> getPathBreveAllPackageFiles() {
        List<String> listaTroncata = new ArrayList<>();
        List<String> listaEstesa = getPathModuloPackageFiles();

        for (String path : listaEstesa) {
            listaTroncata.add(textService.levaCoda(path, JAVA_SUFFIX));
        }

        return listaTroncata;
    }

    /**
     * Path completo di un file esistente nella directory package <br>
     *
     * @return path completo del file
     */
    public String getPath(String simpleName) {
        String pathCompleto = VUOTA;
        List<String> lista = getPathBreveAllPackageFiles();

        for (String path : lista) {
            if (path.endsWith(simpleName)) {
                pathCompleto = path;
                break;
            }
        }

        return pathCompleto;
    }


    /**
     * Crea una lista (canonicalName) di tutti i files della directory package del modulo indicato <br>
     *
     * @return lista dei canonicalName
     */
    public List<String> getCanonicalModuloPackageFiles(final String nomeModulo) throws AlgosException {
        List<String> listaCanonical = new ArrayList<>();
        List<String> listaPath = getPathModuloPackageFiles(nomeModulo);
        String canonicalName;

        for (String nome : listaPath) {
            canonicalName = textService.levaTestoPrimaDi(nome, DIR_PROGETTO_VUOTO);
            canonicalName = canonicalName.replaceAll(SLASH, PUNTO);
            listaCanonical.add(canonicalName);
        }

        return listaCanonical;
    }

    /**
     * Crea una lista (canonicalName) di tutti i files della directory package del modulo corrente <br>
     *
     * @return lista dei canonicalName
     */
    public List<String> getCanonicalAllPackageFiles() throws AlgosException {
        List<String> lista = new ArrayList<>();

        lista.addAll(getCanonicalModuloPackageFiles(VaadVar.projectNameModulo));

        return lista;
    }


    /**
     * Nome 'canonicalName' di un file esistente nella directory package <br>
     *
     * @return canonicalName del file
     */
    public String getCanonicalName(String simpleName) {
        String canonicalName = VUOTA;
        List<String> lista;
        String classeFinalePrevista;
        String classeFinalePath;

        if (textService.isEmpty(simpleName)) {
            if (simpleName == null) {
                logger.error(new AlgosException("Il parametro in ingresso è nullo"));
            }
            logger.error(new AlgosException("Il parametro in ingresso è vuoto"));
        }

        if (simpleName.endsWith(JAVA_SUFFIX)) {
            simpleName = textService.levaCoda(simpleName, JAVA_SUFFIX);
        }
        simpleName = textService.primaMaiuscola(simpleName);

        lista = getPathBreveAllPackageFiles();
        if (lista == null || lista.size() < 1) {
            logger.error(new AlgosException("Non sono riuscito a creare la lista dei files del package"));
        }

        classeFinalePrevista = estraeClasseFinale(simpleName);
        for (String path : lista) {
            classeFinalePath = estraeClasseFinale(path);
            if (classeFinalePath.equals(classeFinalePrevista)) {
                canonicalName = textService.levaTestoPrimaDi(path, DIR_PROGETTO_VUOTO);
                canonicalName = canonicalName.replaceAll(SLASH, PUNTO);
                break;
            }
        }

        if (textService.isEmpty(canonicalName)) {
            logger.error(new AlgosException(String.format("Nel package non esiste la classe %s", simpleName)));
        }

        return canonicalName;
    }


    /**
     * Crea una lista di files/directory ricorsiva nelle sub-directory <br>
     *
     * @return path name completo
     */
    public List<String> recursionSubPathNames(Path start) throws IOException {
        List<String> collect;

        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
            collect = stream
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toList());
        }

        return collect;
    }

    /**
     * Crea una lista di tutte le Entity esistenti nella directory packages <br>
     * Considera sia il modulo vaadflow14 sia il progetto corrente <br>
     */
    public List<Class> getAllEntityClass() {
        List<Class> lista = new ArrayList<>();

        return lista;
    }


    /**
     * Controlla se il primo carattere della stringa passata come parametro è quello previsto <br>
     *
     * @param testoIngresso          da elaborare
     * @param primoCarattereExpected da controllare
     *
     * @return true se il primo carattere NON è uno quello previsto
     */
    public boolean isNotPrimoCarattere(final String testoIngresso, final String primoCarattereExpected) {
        boolean status = false;
        String primoCarattereEffettivo;

        if (textService.isValid(testoIngresso)) {
            primoCarattereEffettivo = testoIngresso.substring(0, 1);
            if (!primoCarattereEffettivo.equals(primoCarattereExpected)) {
                status = true;
            }
        }

        return status;
    }

    /**
     * Controlla se l'ultimo carattere della stringa passata come parametro è quello previsto <br>
     *
     * @param testoIngresso           da elaborare
     * @param ultimoCarattereExpected da controllare
     *
     * @return true se l'ultimo carattere è quello previsto
     */
    public boolean isUltimoCarattere(final String testoIngresso, final String ultimoCarattereExpected) {
        boolean status = false;
        String ultimoCarattereEffettivo;

        if (textService.isValid(testoIngresso)) {
            ultimoCarattereEffettivo = testoIngresso.substring(testoIngresso.length() - 1);
            if (ultimoCarattereEffettivo.equals(ultimoCarattereExpected)) {
                status = true;
            }
        }

        return status;
    }


    /**
     * Controlla se il primo carattere della stringa passata come parametro è uno 'slash' <br>
     *
     * @param testoIngresso da elaborare
     *
     * @return true se NON è uno 'slash'
     */
    public boolean isNotSlashIniziale(final String testoIngresso) {
        return isNotPrimoCarattere(testoIngresso, SLASH);
    }

    /**
     * Controlla se l'ultimo carattere della stringa passata come parametro è uno 'slash' <br>
     *
     * @param testoIngresso da elaborare
     *
     * @return true se NON è uno 'slash'
     */
    public boolean isSlashFinale(final String testoIngresso) {
        return isUltimoCarattere(testoIngresso, SLASH);
    }


    /**
     * Controlla la stringa passata come parametro termina con un 'suffix' (3 caratteri terminali dopo un punto) <br>
     *
     * @param testoIngresso da elaborare
     *
     * @return true se MANCA il 'suffix'
     */
    public boolean isNotSuffix(String testoIngresso) {
        boolean status = true;
        String quartultimoCarattere;
        int gap = 4;
        int max;
        String tagPatchProperties = ".properties";
        String tagPatchGitIgnore = ".gitignore";
        String tagPatchJava = ".java";
        String tagPatchMd = ".md";

        if (textService.isValid(testoIngresso)) {
            max = testoIngresso.length();
            quartultimoCarattere = testoIngresso.substring(max - gap, max - gap + 1);
            if (quartultimoCarattere.equals(PUNTO)) {
                status = false;
            }
        }

        if (testoIngresso.endsWith(tagPatchProperties)) {
            status = false;
        }

        if (testoIngresso.endsWith(tagPatchGitIgnore)) {
            status = false;
        }

        if (testoIngresso.endsWith(tagPatchJava)) {
            status = false;
        }

        if (testoIngresso.endsWith(tagPatchMd)) {
            status = false;
        }

        return status;
    }

    public AResult checkFileDirectory(final String methodName, final File fileDirectoryToBeChecked) {
        AResult result = AResult.build().method(methodName).target(fileDirectoryToBeChecked.getAbsolutePath());
        String message;

        if (fileDirectoryToBeChecked == null) {
            logger.error(new WrapLog().exception(new AlgosException(PARAMETRO_NULLO)).usaDb().type(AETypeLog.file));
            return result.errorMessage(PARAMETRO_NULLO);
        }

        if (textService.isEmpty(fileDirectoryToBeChecked.getName())) {
            logger.error(new WrapLog().exception(new AlgosException(PATH_NULLO)).usaDb().type(AETypeLog.file));
            return result.errorMessage(PATH_NULLO);
        }

        if (!fileDirectoryToBeChecked.getPath().equals(fileDirectoryToBeChecked.getAbsolutePath())) {
            message = String.format("%s%s%s", PATH_NOT_ABSOLUTE, FORWARD, fileDirectoryToBeChecked.getAbsolutePath());
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb().type(AETypeLog.file));
            return result.errorMessage(message);
        }

        return result;
    }

    public AResult checkPath(final String methodName, final String absolutePathToBeChecked) {
        AResult result = AResult.build().method(methodName).target(absolutePathToBeChecked);

        if (absolutePathToBeChecked == null) {
            logger.error(new WrapLog().exception(new AlgosException(PATH_NULLO)).usaDb().type(AETypeLog.file));
            return result.errorMessage(PATH_NULLO);
        }

        if (absolutePathToBeChecked.length() == 0) {
            logger.error(new WrapLog().exception(new AlgosException(PATH_VUOTO)).usaDb().type(AETypeLog.file));
            return result.errorMessage(PATH_VUOTO).target("(vuoto)");
        }

        if (this.isNotSlashIniziale(absolutePathToBeChecked)) {
            logger.error(new WrapLog().exception(new AlgosException(PATH_NOT_ABSOLUTE)).usaDb().type(AETypeLog.file));
            return result.errorMessage(PATH_NOT_ABSOLUTE);
        }

        return result;
    }


    /**
     * Recupera una lista di files (sub-directory escluse) dalla directory <br>
     *
     * @param pathDirectory da spazzolare
     *
     * @return lista di files
     */
    public List<File> getFiles(String pathDirectory) {
        List<File> lista = new ArrayList();
        File unaDirectory = new File(pathDirectory);
        File[] array = unaDirectory.listFiles();

        for (File unFile : array) {
            if (unFile.isFile()) {
                lista.add(unFile);
            }
        }

        return lista;
    }

    /**
     * Recupera una lista di path di files (sub-directory comprese) dalla directory <br>
     *
     * @param pathDirectory da spazzolare
     *
     * @return lista di path di files
     */
    public List<String> getFilesPath(String pathDirectory) {
        List<String> lista = new ArrayList();
        Stream<Path> walk;

        try {
            walk = Files.walk(Paths.get(pathDirectory));
            lista = walk
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.endsWith(TAG_STORE))
                    .map(path -> path.toString())
                    .collect(Collectors.toList());
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        return lista;
    }

    /**
     * Recupera una lista di nomi di files (sub-directory comprese) dalla directory <br>
     *
     * @param pathDirectory da spazzolare
     *
     * @return lista di nomi (parziali) di files
     */
    public List<String> getFilesName(String pathDirectory) {
        List<String> lista = new ArrayList();
        Stream<Path> walk;
        final String dir = pathDirectory.endsWith(SLASH) ? pathDirectory : pathDirectory + SLASH;

        try {
            walk = Files.walk(Paths.get(pathDirectory));
            lista = walk
                    .filter(Files::isRegularFile)
                    .map(path -> textService.levaTesta(path.toString(), dir))
                    .collect(Collectors.toList());
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        return lista;
    }

    public List<String> scanJar(String jarPath) {
        JarFile jarFile;

        try {
            jarFile = new JarFile(jarPath);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            return null;
        }

        return jarFile
                .stream()
                .map(entry -> entry.getName())
                .collect(Collectors.toList());
    }

    public List<String> scanJarClasses(String jarPath) {
        return scanJar(jarPath)
                .stream()
                .filter(entry -> entry.startsWith(JAR_CLASSES_PREFIX))
                .filter(entry -> !entry.endsWith(SLASH))
                .filter(entry -> !entry.contains(TAG_DOLLARO))
                .map(entry -> entry.substring(JAR_CLASSES_PREFIX.length()))
                .map(entry -> entry.substring(0, entry.length() - JAR_CLASSES_SUFFIX.length()))
                .collect(Collectors.toList());
    }

    public List<String> scanJarDir(String jarPath, String dirPath) {
        return scanJarClasses(jarPath)
                .stream()
                .filter(entry -> entry.startsWith(dirPath))
                .collect(Collectors.toList());
    }

    public List<String> scanJarDirType(String jarPath, String dirPath, String suffix) {
        return scanJarDir(jarPath, dirPath)
                .stream()
                .filter(entry -> entry.endsWith(suffix))
                .collect(Collectors.toList());
    }

}