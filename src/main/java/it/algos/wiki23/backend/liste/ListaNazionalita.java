package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 14-Jun-2022
 * Time: 07:06
 * Lista delle biografie per nazionalità <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadNazionalita con appContext.getBean(ListaNazionalita.class).plurale(nomeLista).testoBody() <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaNazionalita extends ListaAttivitaNazionalita {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaNazionalita() {
    }// end of constructor


    public ListaNazionalita nazionalita(final Nazionalita nazionalita) {
        this.nomeLista = nazionalita.plurale;
        super.typeLista = AETypeLista.nazionalitaPlurale;
        return this;
    }

    public ListaNazionalita singolare(final String nomeNazionalitaSingolare) {
        this.nomeLista = nomeNazionalitaSingolare;
        super.typeLista = AETypeLista.nazionalitaSingolare;
        return this;
    }

    public ListaNazionalita plurale(final String nomeNazionalitaPlurale) {
        this.nomeLista = nomeNazionalitaPlurale;
        super.typeLista = AETypeLista.nazionalitaPlurale;
        return this;
    }


//    /**
//     * Costruisce una lista dei wrapper per gestire i dati necessari ad una didascalia <br>
//     * La sottoclasse specifica esegue l'ordinamento <br>
//     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
//     */
//    @Deprecated
//    public List<WrapDidascalia> listaWrapDidascalie() {
//        listaWrapDidascalie = super.listaWrapDidascalie();
//        return listaWrapDidascalie != null ? sortByAttivita(listaWrapDidascalie) : null;
//    }
//
//    /**
//     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
//     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
//     */
//    @Deprecated
//    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {
//        super.mappaWrapDidascalie();
//
//        LinkedHashMap<String, List<WrapDidascalia>> mappaNaz = creaMappaAttivita(listaWrapDidascalie);
//        LinkedHashMap<String, List<WrapDidascalia>> mappaLista;
//
//        if (mappaNaz != null) {
//            for (String key : mappaNaz.keySet()) {
//                mappaLista = creaMappaCarattere(mappaNaz.get(key));
//                mappaWrapDidascalie.put(key, mappaLista);
//            }
//        }
//
//        return mappaWrapDidascalie;
//    }
//
//    //
//    //    /**
//    //     * Mappa ordinata delle didascalie che hanno una valore valido per la pagina specifica <br>
//    //     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
//    //     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
//    //     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
//    //     */
//    //    @Override
//    //    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie() {
//    //        super.mappaDidascalie();
//    //
//    //        LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;
//    //        List<WrapDidascalia> listaWrap;
//    //        List<String> listaDidascalia;
//    //        String didascalia;
//    //
//    //        for (String key1 : mappaWrapDidascalie.keySet()) {
//    //            mappaWrap = mappaWrapDidascalie.get(key1);
//    //            mappaDidascalie.put(key1, new LinkedHashMap<>());
//    //
//    //            for (String key2 : mappaWrap.keySet()) {
//    //                listaWrap = mappaWrap.get(key2);
//    //                listaDidascalia = new ArrayList<>();
//    //                for (WrapDidascalia wrap : listaWrap) {
//    //                    didascalia = didascaliaService.getDidascaliaLista(wrap.getBio());
//    //                    listaDidascalia.add(didascalia);
//    //                }
//    //                mappaDidascalie.get(key1).put(key2, listaDidascalia);
//    //            }
//    //        }
//    //
//    //        return mappaDidascalie;
//    //    }
//
//    //    /**
//    //     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
//    //     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
//    //     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
//    //     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
//    //     */
//    //    @Override
//    //    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi() {
//    //        super.mappaParagrafi();
//    //
//    //        LinkedHashMap<String, List<String>> mappaSub;
//    //        String paragrafo;
//    //
//    //        for (String key : mappaDidascalie.keySet()) {
//    //            paragrafo = key;
//    //            mappaSub = mappaDidascalie.get(key);
////                paragrafo = wikiUtility.fixTitolo(titoloParagrafo, paragrafo);
//    //
//    //            mappaParagrafi.put(paragrafo, mappaSub);
//    //        }
//    //
//    //        return mappaParagrafi;
//    //    }
//    @Deprecated
//    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaAttivita(List<WrapDidascalia> listaWrapNonOrdinata) {
//        LinkedHashMap<String, List> mappa = new LinkedHashMap<>();
//        List lista;
//        String attivita;
//
//        if (listaWrapNonOrdinata != null) {
//            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
//                attivita = wrap.getAttivitaParagrafo();
//                attivita = attivita != null ? attivita : VUOTA;
//
//                if (mappa.containsKey(attivita)) {
//                    lista = mappa.get(attivita);
//                }
//                else {
//                    lista = new ArrayList();
//                }
//                lista.add(wrap);
//                mappa.put(attivita, lista);
//            }
//        }
//
//        return (LinkedHashMap) arrayService.sortVuota(mappa);
//    }
//
//
//    public List<WrapDidascalia> sortByAttivita(List<WrapDidascalia> listaWrapNonOrdinata) {
//        List<WrapDidascalia> sortedList = new ArrayList<>();
//        List<WrapDidascalia> listaConAttivitaOrdinata = new ArrayList<>(); ;
//        List<WrapDidascalia> listaSenzaAttivitaOrdinata = new ArrayList<>(); ;
//
//        listaConAttivitaOrdinata = listaWrapNonOrdinata
//                .stream()
//                .filter(wrap -> textService.isValid(wrap.getNazionalitaSingola()))
//                .sorted(Comparator.comparing(funAttivita))
//                .collect(Collectors.toList());
//
//        listaSenzaAttivitaOrdinata = listaWrapNonOrdinata
//                .stream()
//                .filter(wrap -> textService.isEmpty(wrap.getNazionalitaSingola()))
//                .collect(Collectors.toList());
//
//        sortedList.addAll(listaConAttivitaOrdinata);
//        sortedList.addAll(listaSenzaAttivitaOrdinata);
//        return sortedList;
//    }

}
