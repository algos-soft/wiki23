package it.algos.vaad24.wizard.scripts;

import it.algos.vaad24.wizard.enumeration.*;
import static it.algos.vaad24.wizard.scripts.WizCost.TXT_SUFFIX;
import static it.algos.vaad24.wizard.scripts.WizCost.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import static it.algos.vaad24.wizard.scripts.WizElaboraNewProject.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: sab, 09-apr-2022
 * Time: 07:26
 */
public abstract class WizElabora {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public FileService fileService;

    protected String srcVaad24;

    protected String destNewProject;

    protected String newUpdateProject;

    boolean progettoEsistente;

    public void esegue() {
        String message;
        logger.info(new WrapLog().type(AETypeLog.spazio));

        if (progettoEsistente) {
            message = String.format("Aggiornato il progetto esistente: '%s'", newUpdateProject);
        }
        else {
            message = String.format("Creato il nuovo project: '%s'", newUpdateProject);
        }
        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));

        AEToken.reset();
        AEToken.setCrono();
        AEToken.targetProject.set(newUpdateProject);
        AEToken.targetProjectUpper.set(textService.primaMaiuscola(newUpdateProject));
        AEToken.targetProjectAllUpper.set(newUpdateProject.toUpperCase());
        AEToken.firstProject.set(newUpdateProject.substring(0, 1).toUpperCase());
    }

    public void directory(final AEWizProject wiz) {
        AResult result;
        String srcPath = srcVaad24 + wiz.getCopyDest() + SLASH;
        String destPath = destNewProject + wiz.getCopyDest() + SLASH;
        String dir = fileService.lastDirectory(destPath).toLowerCase();
        String tag = progettoEsistente ? "Update" : "New";

        result = fileService.copyDirectory(wiz.getCopy(), srcPath, destPath);
        mostraRisultato(result, wiz.getCopy(), dir, tag);
    }


    public void mostraRisultato(AResult result, AECopy copy, String dir, String tag) {
        String message;
        String messageType = VUOTA;
        Map<String, List> resultMap;
        List<String> filesSorgenti = null;
        List<String> filesDestinazioneAnte = null;
        List<String> filesDestinazionePost = null;
        List<String> filesAggiuntiNuovi = null;
        List<String> filesModificati = null;
        List<String> filesEliminati = null;
        List<String> filesUguali = null;
        List<String> filesTokenModificati = null;
        List<String> filesTokenUguali = null;

        if (result.isValido()) {
            resultMap = result.getMappa();
            if (resultMap != null) {
                filesSorgenti = resultMap.get(AEKeyMapFile.sorgenti.name());
                filesDestinazioneAnte = resultMap.get(AEKeyMapFile.destinazioneAnte.name());
                filesDestinazionePost = resultMap.get(AEKeyMapFile.destinazionePost.name());
                filesAggiuntiNuovi = resultMap.get(AEKeyMapFile.aggiuntiNuovi.name());
                filesModificati = resultMap.get(AEKeyMapFile.modificati.name());
                filesEliminati = resultMap.get(AEKeyMapFile.eliminati.name());
                filesUguali = resultMap.get(AEKeyMapFile.uguali.name());
                filesTokenModificati = resultMap.get(AEKeyMapFile.tokenModificati.name());
                filesTokenUguali = resultMap.get(AEKeyMapFile.tokenUguali.name());
            }
            filesSorgenti = filesSorgenti != null ? filesSorgenti : new ArrayList<>();
            filesDestinazioneAnte = filesDestinazioneAnte != null ? filesDestinazioneAnte : new ArrayList<>();
            filesDestinazionePost = filesDestinazionePost != null ? filesDestinazionePost : new ArrayList<>();
            filesAggiuntiNuovi = filesAggiuntiNuovi != null ? filesAggiuntiNuovi : new ArrayList<>();
            filesModificati = filesModificati != null ? filesModificati : new ArrayList<>();
            filesEliminati = filesEliminati != null ? filesEliminati : new ArrayList<>();
            filesUguali = filesUguali != null ? filesUguali : new ArrayList<>();
            filesTokenModificati = filesTokenModificati != null ? filesTokenModificati : new ArrayList<>();
            filesTokenUguali = filesTokenUguali != null ? filesTokenUguali : new ArrayList<>();

            switch (copy) {
                case dirOnly -> {}
                case dirDelete -> {}
                case dirFilesAddOnly -> {
                    if (result.getTagCode().equals(AEKeyDir.creataNuova.describeConstable())) {
                        messageType = "DirFilesAddOnly - Directory creata ex novo";
                        message = String.format("%s: %s (%s)", tag, textService.primaMinuscola(result.getMessage()), copy);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                        message = String.format("Files creati: %s", filesDestinazionePost);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                    }
                    if (result.getTagCode().equals(AEKeyDir.esistente)) {
                        messageType = "DirFilesAddOnly - Directory già esistente";
                        message = String.format("%s: %s (%s)", tag, textService.primaMinuscola(result.getMessage()), copy);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                    }
                    if (result.getTagCode().equals(AEKeyDir.integrata)) {
                        messageType = "DirFilesAddOnly - Directory esistente ma integrata";
                        message = String.format("%s: %s (%s)", tag, textService.primaMinuscola(result.getMessage()), copy);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                    }
                    if (FLAG_DEBUG_WIZ) {
                        System.out.println(messageType);
                        message = String.format("Files sorgenti (%s): %s", filesSorgenti.size(), filesSorgenti);
                        System.out.println(message);
                        message = String.format("Files destinazione preesistenti e rimasti (%s): %s", filesDestinazioneAnte.size(), filesDestinazioneAnte);
                        System.out.println(message);
                        message = String.format("Files aggiunti nuovi (%s): %s", filesAggiuntiNuovi.size(), filesAggiuntiNuovi);
                        System.out.println(message);
                        message = String.format("Files modificati (%s): %s", filesModificati.size(), filesModificati);
                        System.out.println(message);
                        message = String.format("Files destinazione finale risultanti (%s): %s", filesDestinazionePost.size(), filesDestinazionePost);
                        System.out.println(message);
                    }
                    else {
                        logger.info(new WrapLog().message(result.getValidMessage()).type(AETypeLog.wizard));
                    }
                }
                case dirFilesModifica, dirFilesModificaToken -> {
                    if (result.getTagCode().equals(AEKeyDir.creataNuova)) {
                        messageType = "DirFilesModifica - Directory creata ex novo";
                        message = String.format("%s: %s (%s)", tag, textService.primaMinuscola(result.getMessage()), copy);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                        message = String.format("Files creati: %s", filesDestinazionePost);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                    }
                    if (result.getTagCode().equals(AEKeyDir.esistente)) {
                        messageType = "DirFilesModifica - Directory esistente";
                        message = String.format("%s: %s (%s)", tag, textService.primaMinuscola(result.getMessage()), copy);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                    }
                    if (result.getTagCode().equals(AEKeyDir.integrata)) {
                        messageType = "DirFilesModifica - Directory integrata";
                        message = String.format("%s: %s (%s)", tag, textService.primaMinuscola(result.getMessage()), copy);
                        logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
                    }
                    if (FLAG_DEBUG_WIZ) {
                        System.out.println(messageType);
                        message = String.format("Files sorgenti (%s): %s", filesSorgenti.size(), filesSorgenti);
                        System.out.println(message);
                        message = String.format("Files destinazione preesistenti e rimasti (%s): %s", filesDestinazioneAnte.size(), filesDestinazioneAnte);
                        System.out.println(message);
                        message = String.format("Files aggiunti nuovi (%s): %s", filesAggiuntiNuovi.size(), filesAggiuntiNuovi);
                        System.out.println(message);
                        message = String.format("Files modificati (%s): %s", filesModificati.size(), filesModificati);
                        System.out.println(message);
                        message = String.format("Files destinazione finale risultanti (%s): %s", filesDestinazionePost.size(), filesDestinazionePost);
                        System.out.println(message);
                    }
                    else {
                        logger.info(new WrapLog().message(result.getValidMessage()).type(AETypeLog.wizard));
                    }
                }
                default -> {}
            }
        }
        else {
            message = String.format("%s: la directory %s non ha funzionato", tag, dir);
            logger.warn(new WrapLog().message(message).type(AETypeLog.wizard));
        }
    }


    public void file(AEWizProject wiz) {
        wiz = null;
    }


    public void source(final it.algos.vaad24.wizard.enumeration.AEWizProject wiz) {
        String message;
        AResult result;
        String dest = wiz.getCopyDest();
        String nomeFile = wiz.getFileSource();
        String sorcePath = srcVaad24 + SOURCE_PREFIX + VAADIN_MODULE + SOURCE_SUFFFIX + nomeFile;
        sorcePath += sorcePath.endsWith(TXT_SUFFIX) ? VUOTA : TXT_SUFFIX;
        String sourceText = fileService.leggeFile(sorcePath);
        sourceText = it.algos.vaad24.wizard.enumeration.AEToken.replaceAll(sourceText);
        String destPath = destNewProject + dest;
        destPath = it.algos.vaad24.wizard.enumeration.AEToken.replaceAll(destPath);
        String tag = progettoEsistente ? "Update" : "New";

        result = fileService.scriveFile(wiz.getCopy(), destPath, sourceText);
        if (result.isValido()) {
            message = String.format("%s: %s (%s)", tag, textService.primaMinuscola(result.getMessage()), wiz.getCopy());
            logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
        }
        else {
            message = String.format("%s: il file %s non ha funzionato", tag, nomeFile);
            logger.warn(new WrapLog().message(message).type(AETypeLog.wizard));
        }
    }

    public void eliminaSources() {
        String message;

        //--elimina la directory 'sources' che deve restare unicamente nel progetto 'vaadin23' e non nei derivati
        if (fileService.isEsisteDirectory(destNewProject + SOURCE_PREFIX + VAADIN_MODULE + SOURCE_SUFFFIX)) {
            if (fileService.deleteDirectory(destNewProject + SOURCE_PREFIX + VAADIN_MODULE + SOURCE_SUFFFIX).isValido()) {
                message = String.format("Delete: cancellata la directory 'sources' dal progetto %s", newUpdateProject);
                logger.info(new WrapLog().message(message).type(AETypeLog.wizard));
            }
            else {
                message = String.format("Non sono riuscito a cancellare la directory 'sources' dal progetto %s", newUpdateProject);
                logger.warn(new WrapLog().message(message).type(AETypeLog.wizard));
            }
        }
    }

}
