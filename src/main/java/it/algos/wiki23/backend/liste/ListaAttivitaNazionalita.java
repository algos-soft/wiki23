package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 03-Aug-2022
 * Time: 12:14
 * Superclasse astratta per le classi ListaAttivita e ListaNazionalita <br>
 */
public abstract class ListaAttivitaNazionalita extends Lista {

    protected static final String TITOLO_LINK_PARAGRAFO_ATTIVITA = "Progetto:Biografie/Attività/";

    protected static final String TITOLO_LINK_PARAGRAFO_NAZIONALITA = "Progetto:Biografie/Nazionalità/";

    protected String titoloLinkParagrafo;

    protected String titoloLinkVediAnche;

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAnni.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaAttivitaNazionalita() {
    }// end of constructor


//    /**
//     * Testo del body di upload con paragrafi e righe <br>
//     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
//     */
//    @Override
//    public WResult testoBody() {
//        String newText = VUOTA;
//        int numVoci = 0;
//
//        super.testoBody();
//
//        if (mappaDidascalia != null && mappaDidascalia.size() > 0) {
//            numVoci = wikiUtility.getSize(mappaDidascalia);
//            newText = testoConParagrafi();
//        }
//
//        return WResult.crea().content(newText).intValue(numVoci);
//    }


    public String testoConParagrafi() {
        StringBuffer buffer = new StringBuffer();
        List<String> lista;
        int max = WPref.sogliaSottoPagina.getInt();
        int numVoci;
        String nomeListaUpper = textService.primaMaiuscola(nomeLista);
        String keyParagrafoUpper;
        String parente;

        for (String keyParagrafo : mappaDidascalia.keySet()) {
            lista = mappaDidascalia.get(keyParagrafo);
            numVoci = lista.size();
            keyParagrafoUpper = textService.primaMaiuscola(keyParagrafo);
            buffer.append(wikiUtility.fixTitolo(titoloLinkParagrafo, keyParagrafo, numVoci));

            if (numVoci > max) {
                parente = String.format("%s%s%s%s", titoloLinkVediAnche, nomeListaUpper, SLASH, keyParagrafoUpper);
                String vedi = String.format("{{Vedi anche|%s}}", parente);
                buffer.append(vedi + CAPO);
                //                this.uploadSottoPagina(parente, numVoci, mappaSub);
            }
            else {
                for (String didascalia : lista) {
                    buffer.append(ASTERISCO + didascalia);
                    buffer.append(CAPO);
                }
            }

        }

        return buffer.toString().trim();
    }

}
