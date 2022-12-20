package it.algos.vaad24.wizard.scripts;

import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.vaad24.ui.dialog.*;
import it.algos.vaad24.wizard.enumeration.*;
import static it.algos.vaad24.wizard.scripts.WizCost.*;
import static it.algos.vaad24.wizard.scripts.WizElaboraNewProject.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadbio
 * Created by Algos
 * User: gac
 * Date: mer, 13-apr-2022
 * Time: 06:49
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WizElaboraUpdateProject extends WizElabora {

    public WizElaboraUpdateProject() {
        super();
    }// end of constructor

    public WizElaboraUpdateProject(String updateProject) {
        super();
    }// end of constructor

    public void esegue(final LinkedHashMap<String, Checkbox> mappaCheckbox) {
        super.progettoEsistente = true;
        AEWizProject wiz;
        destNewProject = System.getProperty("user.dir");
        newUpdateProject = fileService.estraeClasseFinaleSenzaJava(destNewProject).toLowerCase();
        srcVaad24 = textService.levaCoda(destNewProject, newUpdateProject);
        srcVaad24 += VAADIN_PROJECT + SLASH;
        if (srcVaad24.contains("tutorial")) {
            srcVaad24 = "/Users/gac/Documents/IdeaProjects/operativi/vaadin23/";
        }

        destNewProject += SLASH;

        super.esegue();

        for (String key : mappaCheckbox.keySet()) {
            if (mappaCheckbox.get(key).getValue()) {
                wiz = AEWizProject.valueOf(key);
                switch (wiz.getCopy().getType()) {
                    case directory -> directory(wiz);
                    case file -> file(wiz);
                    case source -> source(wiz);
                    case elabora -> elabora(wiz);
                }
            }
        }
        Avviso.show("Update project").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        super.eliminaSources();
    }


    public void elabora(final AEWizProject wiz) {
        AResult result;
        String srcPath = srcVaad24 + wiz.getCopyDest() + SLASH;
        String destPath = destNewProject + wiz.getCopyDest() + SLASH;
        String dir = fileService.lastDirectory(destPath).toLowerCase();
        String oldToken = APPLICATION_VAADIN24;
        String newToken = fileService.estraeClasseFinaleSenzaJava(destNewProject);
        newToken = textService.primaMaiuscola(newToken) + APP_NAME;
        String tag = progettoEsistente ? "Update" : "New";

        switch (wiz) {
            case testService, testBackend -> {
//                result = fileService.copyDirectory(AECopy.dirFilesModifica, srcPath, destPath);
                result = fixToken(srcPath, destPath, oldToken, newToken);
                mostraRisultato(result, AECopy.dirFilesModifica, dir, tag);
            }
            default -> {}
        }
    }

    public AResult fixToken( String srcPath, String destPath, String oldToken, String newToken) {
        String testoBase;
        String testoSostituito;
        String path;
//        String destPath = result.getTarget();
//        Map<String, List> resultMap = result.getMappa();
//        List<String> files = resultMap.get(KEY_MAPPA_MODIFICATI);
//        result = fileService.copyDirectory(AECopy.dirFilesModifica, srcPath, destPath);

//        for (String nomeFile : files) {
//            path = destPath + nomeFile;
//            testoBase = fileService.leggeFile(path);
//            testoSostituito = textService.sostituisce(testoBase, oldToken, newToken);
//            fileService.sovraScriveFile(path, testoSostituito);
//            if (testoSostituito.equals(testoBase)) {
//            }
//        }

        return null;
    }

}
