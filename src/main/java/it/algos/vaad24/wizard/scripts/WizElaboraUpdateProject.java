package it.algos.vaad24.wizard.scripts;

import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.wizard.enumeration.*;
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

        super.eliminaSources();
    }

}
