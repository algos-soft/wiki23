package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.packages.crono.secolo.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 25-Jul-2022
 * Time: 07:36
 * <p>
 * Lista delle biografie per giorni <br>
 * <p>
 * La lista è una mappa di WrapLista suddivisa in paragrafi, che contiene tutte le informazioni per scrivere le righe della pagina <br>
 * Usata fondamentalmente da UploadGiorni con appContext.getBean(ListaGiorni.class).nascita/morte(nomeGiorno).mappaWrap() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con nascita/morte(nomeGiorno) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaGiorni extends Lista {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public SecoloBackend secoloBackend;

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: getBean(ListaGiorni.class).nascita/morte(nomeGiorno).mappaWrap() <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaGiorni() {
        super.paragrafoAltre = TAG_LISTA_NO_ANNO;
    }// end of constructor


    public ListaGiorni nascita(final String nomeGiorno) {
        this.nomeLista = nomeGiorno;
        super.typeLista = AETypeLista.giornoNascita;
        return this;
    }

    public ListaGiorni morte(final String nomeGiorno) {
        this.nomeLista = nomeGiorno;
        super.typeLista = AETypeLista.giornoMorte;
        return this;
    }


    /**
     * Ordina la mappa <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    public LinkedHashMap<String, List<WrapLista>> sortMap(LinkedHashMap<String, List<WrapLista>> mappaIn) {
        LinkedHashMap<String, List<WrapLista>> mappaOut = new LinkedHashMap<>();
        List<WrapLista> lista;
        List<String> secoli = secoloBackend.findNomiAscendenti();

        //Ordina le chiavi per secolo
        for (String key : secoli) {
            if (mappaIn.containsKey(key)) {
                lista = mappaIn.get(key);
                mappaOut.put(key, lista);
            }
        }

        //Regola la chiave 'Senza anno specificato'
        if (mappaIn.containsKey(TAG_LISTA_NO_ANNO)) {
            lista = mappaIn.get(TAG_LISTA_NO_ANNO);
            mappaOut.put(TAG_LISTA_NO_ANNO, lista);
        }

        //Ordina per nome all'interno della singola lista
        //        for (String key : mappaOut.keySet()) {
        //            lista = mappaOut.get(key);
        //            lista = lista.stream()
        //                    .sorted(Comparator.comparing(bio -> bio.ordinamento))
        //                    .collect(Collectors.toList());
        //            mappaOut.put(key, lista);
        //        }

        return mappaOut;
    }

}
