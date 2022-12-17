package it.algos.wiki23.backend.packages.bio;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.vaad24.ui.dialog.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.function.*;

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

    protected Consumer<WrapBio> confirmHandlerNew;

    protected WrapBio wrap;

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


    public void openNew(final Consumer<WrapBio> confirmHandlerNew) {
        infoPlaceHolder.setVisible(false);
        this.confirmHandlerNew = confirmHandlerNew;
        super.open();
    }

    protected void sincro(HasValue.ValueChangeEvent event) {
        String wikiTitle = textField.getValue();
        infoPlaceHolder.removeAll();
        boolean esiste = false;
        boolean biografia = false;

        try {
            esiste = appContext.getBean(QueryExist.class).isEsiste(wikiTitle);
            if (esiste) {
                wrap = appContext.getBean(QueryBio.class).getWrap(wikiTitle);
            }
        } catch (Exception unErrore) {
            logger.warn(new WrapLog().exception(unErrore).usaDb());
        }

        if (wrap != null && wrap.isValida()) {
            confirmButton.setEnabled(true);
            infoPlaceHolder.setVisible(false);
        }
        else {
            confirmButton.setEnabled(false);
            if (esiste) {
                infoPlaceHolder.add(htmlService.getSpanRosso("La pagina esiste ma non è una bio"));
            }
            else {
                infoPlaceHolder.add(htmlService.getSpanRosso("La pagina non esiste"));
            }
            infoPlaceHolder.setVisible(true);
        }
    }
    public void confirmHandler() {
        confirmHandlerNew();
    }

    public void confirmHandlerNew() {
        close();
        confirmHandlerNew.accept(wrap);
    }

}
