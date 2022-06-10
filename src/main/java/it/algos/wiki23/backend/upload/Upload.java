package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.checkerframework.checker.units.qual.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 08-Jun-2022
 * Time: 06:54
 * <p>
 * Classe specializzata per caricare (upload) le liste biografiche sul server wiki. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 * <p>
 * La (List<Bio>) listaBio, la (List<String>) listaDidascalie, la (Map<String, List>) mappa e (String) testoConParagrafi
 * vengono tutte regolate alla creazione dell'istanza in @PostConstruct e sono disponibili da subito <br>
 * Si usa SOLO la chiamata appContext.getBean(UploadXxx.class, yyy) per caricare l'istanza ListaXxx.class <br>
 * L'effettivo upload su wiki avviene SOLO con uploadPagina() o uploadPaginaTest() <br>
 */
public abstract class Upload {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;


    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * La mappa viene creata nel @PostConstruct dell'istanza <br>
     */
    protected TreeMap<String, TreeMap<String, List<String>>> mappa;
    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;

    protected String newText;

    protected String mappaToText(LinkedHashMap<String, LinkedHashMap<String, List<String>>> treeMap) {
        String text = VUOTA;
        StringBuffer buffer = new StringBuffer();
        LinkedHashMap<String, List<String>> mappaSub;

        if (treeMap != null) {
            System.out.println(VUOTA);

            for (String key : treeMap.keySet()) {
                mappaSub = treeMap.get(key);
                buffer.append(setParagrafo(key, mappaSub.size()));
                buffer.append(CAPO);
                buffer.append(mappaSubToText(mappaSub));
            }
        }

        return buffer.toString();
    }

    protected String mappaSubToText(LinkedHashMap<String, List<String>> mappaSub) {
        String text = VUOTA;
        StringBuffer buffer = new StringBuffer();
        List<String> lista;
        int max = 12; //@todo da preferenze

        if (mappaSub != null) {
            System.out.println(VUOTA);

            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                if (lista.size() > max) {
                }
                else {
                    for (String didascalia : lista) {
                        buffer.append(didascalia+ CAPO);
                    }
                }
            }
        }

        return buffer.toString();
    }


    public String setParagrafo(final String titolo) {
        return setParagrafo(titolo, 0);
    }


    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param titolo da inglobare nei tag wiki (paragrafo)
     * @param numero da visualizzare (maggiore di zero)
     *
     * @return testo coi tag html
     */
    public String setParagrafo(final String titolo, final int numero) {
        String paragrafo = VUOTA;

        paragrafo += PARAGRAFO;
        paragrafo += SPAZIO;
        paragrafo += titolo;
        if (numero > 0) {
            paragrafo += SPAZIO;
            paragrafo += smallNumero(numero);
        }
        paragrafo += SPAZIO;
        paragrafo += PARAGRAFO;
        paragrafo += CAPO;

        return paragrafo;
    }


    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param numero da visualizzare
     *
     * @return testo coi tag html
     */
    public String smallNumero(final int numero) {
        String testo = VUOTA;

        testo += "<span style=\"font-size:70%\">(";
        testo += numero;
        testo += ")</span>";

        return testo;
    }

}
