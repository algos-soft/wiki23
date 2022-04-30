package it.algos.test;

import it.algos.test.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29-apr-2022
 * Time: 20:57
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Tag("testAllValido")
//@DisplayName("Test W")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class WTest extends STest {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    protected final static String PAGINA_INESISTENTE = "Pippooz Belloz";

    protected final static String PAGINA_ESISTENTE_UNO = "Matteo Salvini";

    protected final static String PAGINA_ESISTENTE_DUE = "Matteo Renzi";

    protected final static String PAGINA_ESISTENTE_TRE = "Piozzano";

    protected WResult previstoRisultato;

    protected WResult ottenutoRisultato;

    protected WrapBio wrapBio;


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUpEach() {
        super.setUpEach();

        previstoRisultato = null;
        ottenutoRisultato = null;
        wrapBio = null;
    }

    protected void printRisultato(WResult result) {
        List lista = result.getLista();
        lista = lista != null && lista.size() > 20 ? lista.subList(0, 10) : lista;

        System.out.println(VUOTA);
        System.out.println("Risultato");
        System.out.println(String.format("Status: %s", result.isValido() ? "true" : "false"));
        System.out.println(String.format("Query: %s", result.getQueryType()));
        System.out.println(String.format("Title: %s", result.getWikiTitle()));
        System.out.println(String.format("Preliminary url: %s", result.getUrlPreliminary()));
        System.out.println(String.format("Secondary url: %s", result.getUrlRequest()));
        System.out.println(String.format("Preliminary response: %s", result.getPreliminaryResponse()));
        System.out.println(String.format("Token: %s", result.getToken()));
        System.out.println(String.format("Secondary response: %s", result.getResponse()));
        System.out.println(String.format("Message code: %s", result.getCodeMessage()));
        System.out.println(String.format("Message: %s", result.getMessage()));
        System.out.println(String.format("Error code: %s", result.getErrorCode()));
        System.out.println(String.format("Error message: %s", result.getErrorMessage()));
        System.out.println(String.format("Valid message: %s", result.getValidMessage()));
        System.out.println(String.format("Numeric value: %s", textService.format(result.getIntValue())));
        System.out.println(String.format("List value: %s", lista));
        System.out.println(String.format("Map value: %s", result.getMappa()));
        System.out.println(String.format("Risultato ottenuto in %s", dateService.toText(result.getDurata())));
        printWrapBio(result.getWrap());
    }


    protected void printWrapBio(WrapBio wrap) {
        if (wrap != null) {
            System.out.println(VUOTA);
            System.out.println(String.format("Wrap valido: %s", wrap.isValida() ? "true" : "false"));
            System.out.println(String.format("Wrap title: %s", wrap.getTitle()));
            System.out.println(String.format("Wrap pageid: %s", wrap.getPageid()));
            System.out.println(String.format("Wrap type: %s", wrap.getType()));
            System.out.println(String.format("Wrap templBio: %s", textService.isValid(wrap.getTemplBio()) ? "valido" : "non esiste"));
        }
    }

}