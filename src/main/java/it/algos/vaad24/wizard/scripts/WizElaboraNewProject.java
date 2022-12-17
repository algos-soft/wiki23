package it.algos.vaad24.wizard.scripts;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.wizard.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;


/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 13-apr-2020
 * Time: 05:31
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WizElaboraNewProject extends it.algos.vaad24.wizard.scripts.WizElabora {

    public static final String SOURCE_PREFIX = "src/main/java/it/algos/";

    public static final String SOURCE_SUFFFIX = "/wizard/sources/";

    public static final String VAADIN_PROJECT = "vaad24";

    public static final String VAADIN_MODULE = "vaad24";


    public void esegue(final String pathNewUpdateProject) {
        progettoEsistente = fileService.isContieneProgettoValido(pathNewUpdateProject);
        srcVaad24 = System.getProperty("user.dir") + SLASH;
        destNewProject = pathNewUpdateProject + SLASH;
        newUpdateProject = fileService.lastDirectory(destNewProject).toLowerCase();
        super.esegue();

        for (AEWizProject wiz : AEWizProject.getAllNewProject()) {
            switch (wiz.getCopy().getType()) {
                case directory -> directory(wiz);
                case file -> file(wiz);
                case source -> source(wiz);
            }
        }

        super.eliminaSources();
    }

}
