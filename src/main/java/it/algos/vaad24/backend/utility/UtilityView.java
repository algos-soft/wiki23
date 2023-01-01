package it.algos.vaad24.backend.utility;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad24.backend.boot.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.vaad24.ui.dialog.*;
import it.algos.vaad24.ui.views.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Tue, 13-Dec-2022
 * Time: 11:36
 */
@SpringComponent
@Route(value = VaadCost.TAG_UTILITY, layout = MainLayout.class)
@RouteAlias(value = TAG_ROUTE_ALIAS_PARTE_PER_PRIMA, layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
public class UtilityView extends VerticalLayout {

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ClassService classService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public FileService fileService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;

    /**
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public UtilityView() {
        super();
    }// end of Vaadin/@Route constructor


    @PostConstruct
    protected void postConstruct() {
        initView();
    }

    /**
     * Qui va tutta la logica iniziale della view principale <br>
     */
    protected void initView() {
        this.setMargin(true);
        this.setPadding(false);
        this.setSpacing(false);

        this.titolo();

        this.paragrafoReset();

        //--spazio per distanziare i paragrafi
        this.add(new H3());
    }

    public void titolo() {
        H1 titolo = new H1("Gestione utility");
        titolo.getElement().getStyle().set("color", "green");
        this.add(titolo);
    }

    public void paragrafoReset() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo = new H3("Reset di tutte le collection [ordinate] che lo implementano");
        paragrafo.getElement().getStyle().set("color", "blue");

        layout.add(new Label(String.format("Esegue il %s() su tutte le collection [ordinate] che implementano %s()", TAG_RESET_FORCING, TAG_RESET_ONLY)));

        Button bottone = new Button("Reset all");
        bottone.getElement().setAttribute("theme", "primary");
        bottone.addClickListener(event -> AReset.reset(this::reset));

        this.add(paragrafo);
        layout.add(bottone);
        this.add(layout);
    }


    private void reset() {
        logger.info(new WrapLog().message(VUOTA).type(AETypeLog.reset));
        resetSingoloModulo(VaadVar.moduloVaadin24);
        logger.info(new WrapLog().message(VUOTA).type(AETypeLog.reset));
        resetSingoloModulo(VaadVar.projectNameModulo);
        logger.info(new WrapLog().message(VUOTA).type(AETypeLog.reset));
    }

    private void resetSingoloModulo(String nomeModulo) {
        AResult risultato;
        boolean esiste;
        String message;
        List<Class> listaClazz;

        esiste = fileService.isEsisteModulo(nomeModulo);
        if (!esiste) {
            message = String.format("Non esiste il modulo '%s'",nomeModulo);
            logger.warn(new WrapLog().message(message).type(AETypeLog.reset));
            return;
        }

        listaClazz = classService.allModuleBackendResetOrderedClass(nomeModulo);
        if (listaClazz != null && listaClazz.size() > 0) {
            message = String.format("Nel modulo %s ci sono %d classi che implementano il metodo %s", nomeModulo, listaClazz.size(), TAG_RESET_ONLY);
            logger.info(new WrapLog().message(message).type(AETypeLog.reset));
            for (Class clazz : listaClazz) {
                risultato = classService.esegueMetodo(clazz.getCanonicalName(), TAG_RESET_FORCING);
                logger.info(new WrapLog().message(risultato.getValidMessage()).type(AETypeLog.reset));
            }
        }
        else {
            message = String.format("Nel modulo %s non ci sono classi che implementino il metodo %s", nomeModulo, TAG_RESET_ONLY);
            logger.info(new WrapLog().message(message).type(AETypeLog.reset));
        }
    }

}
