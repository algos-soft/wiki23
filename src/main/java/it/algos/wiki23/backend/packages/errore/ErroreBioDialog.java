package it.algos.wiki23.backend.packages.errore;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.vaadin.crudui.crud.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sat, 27-Aug-2022
 * Time: 14:06
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ErroreBioDialog extends BioDialog {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiApiService wikiApiService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public RegexService regexService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public QueryService queryService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioBackend bioBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioService bioService;

    protected Button buttonFixSexM;

    protected Button buttonFixSexF;

    protected Button buttonFixNazionalita;

    /**
     * Constructor not @Autowired. <br>
     * Non utilizzato e non necessario <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Se c'è un SOLO costruttore questo diventa automaticamente @Autowired e IntelliJ Idea 'segna' in rosso i
     * parametri <br>
     * Per evitare il bug (solo visivo), aggiungo un costruttore senza parametri <br>
     */
    public ErroreBioDialog() {
    }// end of second constructor not @Autowired

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(PreferenzaDialog.class, operation, itemSaver, itemDeleter, itemAnnulla); <br>
     *
     * @param entityBean  The item to edit; it may be an existing or a newly created instance
     * @param operation   The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     * @param fields      da costruire in automatico
     */
    public ErroreBioDialog(final Bio entityBean, final CrudOperation operation, final BioBackend crudBackend, final List fields) {
        super(entityBean, operation, crudBackend, fields);
        super.currentItem = entityBean;
        super.backend = crudBackend;
        super.usaUnaSolaColonna = false;
    }// end of constructor not @Autowired

    @Override
    protected void fixBottom() {
        super.fixBottom();

        buttonFixSexM = new Button();
        buttonFixSexM.setText("Fix sex M");
        buttonFixSexM.getElement().setAttribute("theme", "error");
        buttonFixSexM.setIcon(new Icon(VaadinIcon.UPLOAD));
        buttonFixSexM.addClickListener(event -> fixSex("M"));
        bottomPlaceHolder.add(buttonFixSexM);

        buttonFixSexF = new Button();
        buttonFixSexF.setText("Fix sex F");
        buttonFixSexF.getElement().setAttribute("theme", "error");
        buttonFixSexF.setIcon(new Icon(VaadinIcon.UPLOAD));
        buttonFixSexF.addClickListener(event -> fixSex("F"));
        bottomPlaceHolder.add(buttonFixSexF);

        buttonFixNazionalita = new Button();
        buttonFixNazionalita.setText("Fix nazionalità");
        buttonFixNazionalita.getElement().setAttribute("theme", "error");
        buttonFixNazionalita.setIcon(new Icon(VaadinIcon.UPLOAD));
        buttonFixNazionalita.addClickListener(event -> fixNazionalita());
        bottomPlaceHolder.add(buttonFixNazionalita);
    }

    protected void fixSex(String newSex) {
        currentItem.sesso = newSex;
        String wikiTitle = currentItem.wikiTitle;
        String summary = "[[Utente:Biobot/fixPar|fixPar]]";
        String tagRegex = "\n*\\| *Sesso *= *[MF]*\n*\\|";
        String tag = "\n|Sesso = %s\n|";
        String tagNew;
        String oldText;
        String newText;
        WResult result = WResult.errato();
        Bio bio;
        String message;
        boolean esisteParametroVuoto = false;

        if (textService.isEmpty(newSex)) {
            Notification.show(String.format("Manca il parametro 'sesso' nella biografia %s", currentItem.wikiTitle)).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (newSex.equals("M") || newSex.equals("F")) {
        }
        else {
            Notification.show(String.format("Il parametro 'sesso' nella biografia %s è errato", currentItem.wikiTitle)).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        tagNew = String.format(tag, newSex);
        oldText = wikiApiService.legge(wikiTitle);
        esisteParametroVuoto = regexService.isEsiste(oldText, tagRegex);

        if (!esisteParametroVuoto) {
            message = String.format("La pagina %s non ha il parametro sesso", currentItem.wikiTitle);
            Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
            close();
            return;
        }

        newText = regexService.replaceFirst(oldText, tagRegex, tagNew);
        if (textService.isValid(newText)) {
            result = wikiApiService.scrive(wikiTitle, newText, summary);
        }

        if (result.isValido()) {
            bio = wikiApiService.downloadAndSave(wikiTitle);
            if (bio != null && bio.sesso != null) {
                if (bio.sesso.equals("M") || bio.sesso.equals("F")) {
                    bio.errato = false;
                    backend.save(bio);
                    message = String.format("La biografia %s ha adesso il parametro sesso = %s", currentItem.wikiTitle, bio.sesso);
                    Notification.show(message).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                else {
                    message = String.format("Non sono riuscito a modificare il parametro sesso della bio %s", currentItem.wikiTitle);
                    Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
            else {
                message = String.format("Non sono riuscito a registrare %s sul mongoDB", currentItem.wikiTitle);
                Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
        else {
            message = String.format("Non sono riuscito a modificare la pagina wiki %s", currentItem.wikiTitle);
            Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        close();
    }

    protected void fixNazionalita() {
        String nazionalitaNew = currentItem.nazionalita;
        String wikiTitle = currentItem.wikiTitle;
        String summary = "[[Utente:Biobot/fixPar|fixPar]]";
        String tagRegex = "\n*\\| *Nazionalità *= *%s*\n*\\|";
        String tagNew = "\n|Nazionalità = %s\n|";
        String oldText;
        String newText;
        WResult result = WResult.errato();
        WrapBio wrap = queryService.getBioWrap(wikiTitle);
        Bio bio = bioBackend.newEntity(wrap); ;
        Map<String, String> mappa = bioService.estraeMappa(bio);
        String nazionalitaOld = mappa.get("Nazionalità");
        String message;
        boolean esisteParametroNazionalita = false;

        tagRegex = String.format(tagRegex, nazionalitaOld);
        tagNew = String.format(tagNew, nazionalitaNew);

        oldText = wikiApiService.legge(wikiTitle);
        esisteParametroNazionalita = regexService.isEsiste(oldText, tagRegex);

        if (!esisteParametroNazionalita) {
            message = String.format("La pagina %s non ha il parametro nazionalità come previsto", currentItem.wikiTitle);
            Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
            close();
            return;
        }

        newText = regexService.replaceFirst(oldText, tagRegex, tagNew);
        if (textService.isValid(newText)) {
            result = wikiApiService.scrive(wikiTitle, newText, summary);
        }

        if (result.isValido()) {
            bio = wikiApiService.downloadAndSave(wikiTitle);
            if (bio != null && bio.nazionalita.equals(nazionalitaNew)) {
                bio.errato = false;
                backend.save(bio);
                message = String.format("La biografia %s ha adesso il parametro nazionalità = %s", currentItem.wikiTitle, nazionalitaNew);
                Notification.show(message).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
            else {
                message = String.format("Non sono riuscito a registrare %s sul mongoDB", currentItem.wikiTitle);
                Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
        else {
            message = String.format("Non sono riuscito a modificare la pagina wiki %s", currentItem.wikiTitle);
            Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        close();
    }

}
