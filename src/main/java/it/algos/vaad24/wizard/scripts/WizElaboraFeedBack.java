package it.algos.vaad24.wizard.scripts;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.wizard.scripts.WizCost.*;
import static it.algos.vaad24.wizard.scripts.WizElaboraNewProject.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import it.algos.vaad24.backend.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project sette
 * Created by Algos
 * User: gac
 * Date: lun, 11-apr-2022
 * Time: 17:18
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WizElaboraFeedBack extends it.algos.vaad24.wizard.scripts.WizElabora {

    public void esegue() {
        String message;
        boolean esisteSrc;
        boolean esisteDest;
        AResult result;
        String srcWizardProject = System.getProperty("user.dir");
        String currentProject = fileService.estraeClasseFinaleSenzaJava(srcWizardProject).toLowerCase();
        String destBaseVaad24 = textService.levaCoda(srcWizardProject, currentProject);
        if (destBaseVaad24.contains("tutorial")) {
            destBaseVaad24 = "/Users/gac/Documents/IdeaProjects/operativi/";
        }
        destBaseVaad24 += VAADIN_PROJECT + SLASH;

        String srcWizard = String.format("%s%s%s%s%s%s", srcWizardProject, SLASH, SOURCE_PREFIX, VAADIN_MODULE, SLASH, DIR_WIZARD);
        String destWizard = String.format("%s%s%s%s%s", destBaseVaad24, SOURCE_PREFIX, VAADIN_MODULE, SLASH, DIR_WIZARD);

        //@todo patch
        srcWizard= srcWizard.replace("24","23");
        //@todo patch
        esisteSrc = fileService.isEsisteDirectory(srcWizard);
        esisteDest = fileService.isEsisteDirectory(destWizard);

        if (esisteSrc && esisteDest) {
            result = fileService.copyDirectory(AECopy.dirFilesModifica, srcWizard, destWizard);
            if (result.isValido()) {
                mostraRisultato(result, AECopy.dirFilesModifica, destWizard, "Rollback");
            }
            else {
                message = "La directory 'wizard' non è stata aggiornata";
                logger.warn(new WrapLog().type(AETypeLog.wizard).exception(new AlgosException(message)));
            }
        }
        else {
            if (!esisteSrc) {
                message = String.format("Il path sorgente %s è errato", srcWizard);
                logger.warn(new WrapLog().type(AETypeLog.wizard).message(message));
            }
            if (!esisteDest) {
                message = String.format("Il path destinazione %s è errato", destWizard);
                logger.warn(new WrapLog().type(AETypeLog.wizard).message(message));
            }
        }
    }

}
