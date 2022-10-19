package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 17-Oct-2022
 * Time: 19:07
 * Lista delle biografie per cognomi <br>
 * <p>
 * La lista è una mappa di WrapLista suddivisa in paragrafi, che contiene tutte le informazioni per scrivere le righe della pagina <br>
 * Usata fondamentalmente da UploadCognomi con appContext.getBean(ListaCognomi.class).mappaWrap() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con mappaWrap() <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaCognomi extends Lista {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: getBean(ListaGiorni.class).nascita/morte(nomeGiorno).mappaWrap() <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaCognomi() {
    }// end of constructor

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: getBean(ListaGiorni.class).nascita/morte(nomeGiorno).mappaWrap() <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaCognomi(String cognome) {
        this.nomeLista = cognome;
        super.typeLista = AETypeLista.cognomi;
        super.paragrafoAltre = TAG_LISTA_NO_ATTIVITA;
    }// end of constructor


    /**
     * Ordina la mappa <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    public LinkedHashMap<String, List<WrapLista>> sortMap(LinkedHashMap<String, List<WrapLista>> mappa) {
        LinkedHashMap<String, List<WrapLista>> mappaOut;
        List<WrapLista> lista;

        //Ordina le chiavi
        mappaOut = wikiUtility.sort(mappa);

        //Ordina per nome all'interno della singola lista
        for (String key : mappaOut.keySet()) {
            lista = mappaOut.get(key);
            lista = lista.stream()
                    .sorted(Comparator.comparing(bio -> bio.ordinamento))
                    .collect(Collectors.toList());
            mappaOut.put(key, lista);
        }

        return mappaOut;
    }

}
