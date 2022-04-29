package it.algos.wiki23.ui.dialog;

import com.vaadin.flow.component.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.dialog.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29-apr-2022
 * Time: 10:38
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogNewBio extends DialogInputText {

    /**
     * Preferenze usate da questo dialogo <br>
     * Primo metodo chiamato dopo AfterNavigationEvent <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixPreferenze() {
        super.fixPreferenze();

        super.titoloDialogo = "Nuova biografia";
        super.captionTextField = "WikiTitle";
    }

    protected void sincro(HasValue.ValueChangeEvent event) {
        String wikiTitle = textField.getValue();
        boolean esiste = false;

        try {
            esiste = appContext.getBean(QueryExist.class).urlRequest(wikiTitle);
        } catch (Exception unErrore) {
            logger.warn(new WrapLog().exception(unErrore).usaDb());
        }
        confirmButton.setEnabled(esiste);

        if (esiste) {
            confirmButton.setEnabled(true);
        }
        else {
            confirmButton.setEnabled(false);
            // messaggio rosso
        }
    }

}
