package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
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
 * Usata fondamentalmente da UploadNazionalita con appContext.getBean(ListaNazionalita.class).plurale(nomeNazionalitaPlurale).xxx() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con 3 diversi metodi,
 * ognuno col suo parametro:
 * nazionalita(final Nazionalita nazionalita) <br>
 * singolare(final String nomeNazionalitaSingolare) <br>
 * plurale(final String nomeNazionalitaPlurale) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaNazionalita extends Lista {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaNazionalita() {
        super.titoloParagrafo = "Progetto:Biografie/Attività/";
    }// end of constructor


    public ListaNazionalita nazionalita(final Nazionalita nazionalita) {
        listaNomiSingoli = nazionalitaBackend.findSingolariByPlurale(nazionalita.plurale);
        return this;
    }

    public ListaNazionalita singolare(final String nomeNazionalitaSingolare) {
        listaNomiSingoli = arrayService.creaArraySingolo(nomeNazionalitaSingolare);
        return this;
    }

    public ListaNazionalita plurale(final String nomeNazionalitaPlurale) {
        listaNomiSingoli = nazionalitaBackend.findSingolariByPlurale(nomeNazionalitaPlurale);
        return this;
    }


    /**
     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     */
    @Override
    public List<Bio> listaBio() {
        super.listaBio();

        try {
            listaBio = bioService.fetchNazionalita(listaNomiSingoli);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            return null;
        }

        return listaBio;
    }

    /**
     * Costruisce una lista dei wrapper per gestire i dati necessari ad una didascalia <br>
     * La sottoclasse specifica esegue l'ordinamento <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public List<WrapDidascalia> listaWrapDidascalie() {
        listaWrapDidascalie = super.listaWrapDidascalie();
        return sortByAttivita(listaWrapDidascalie);
    }

    /**
     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {
        super.mappaWrapDidascalie();

        LinkedHashMap<String, List<WrapDidascalia>> mappaNaz = creaMappaAttivita(listaWrapDidascalie);
        LinkedHashMap<String, List<WrapDidascalia>> mappaLista;

        if (mappaNaz != null) {
            for (String key : mappaNaz.keySet()) {
                mappaLista = creaMappaCarattere(mappaNaz.get(key));
                mappaWrapDidascalie.put(key, mappaLista);
            }
        }

        return mappaWrapDidascalie;
    }

//
//    /**
//     * Mappa ordinata delle didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
//     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
//     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
//     */
//    @Override
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie() {
//        super.mappaDidascalie();
//
//        LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;
//        List<WrapDidascalia> listaWrap;
//        List<String> listaDidascalia;
//        String didascalia;
//
//        for (String key1 : mappaWrapDidascalie.keySet()) {
//            mappaWrap = mappaWrapDidascalie.get(key1);
//            mappaDidascalie.put(key1, new LinkedHashMap<>());
//
//            for (String key2 : mappaWrap.keySet()) {
//                listaWrap = mappaWrap.get(key2);
//                listaDidascalia = new ArrayList<>();
//                for (WrapDidascalia wrap : listaWrap) {
//                    didascalia = didascaliaService.getDidascaliaLista(wrap.getBio());
//                    listaDidascalia.add(didascalia);
//                }
//                mappaDidascalie.get(key1).put(key2, listaDidascalia);
//            }
//        }
//
//        return mappaDidascalie;
//    }


//    /**
//     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
//     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
//     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
//     */
//    @Override
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi() {
//        super.mappaParagrafi();
//
//        LinkedHashMap<String, List<String>> mappaSub;
//        String paragrafo;
//
//        for (String key : mappaDidascalie.keySet()) {
//            paragrafo = key;
//            mappaSub = mappaDidascalie.get(key);
//            paragrafo = wikiUtility.fixTitolo(titoloParagrafo, paragrafo);
//
//            mappaParagrafi.put(paragrafo, mappaSub);
//        }
//
//        return mappaParagrafi;
//    }

    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaAttivita(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List> mappa = new LinkedHashMap<>();
        List lista;
        String attivita;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                attivita = wrap.getAttivitaParagrafo();
                attivita = attivita != null ? attivita : VUOTA;

                if (mappa.containsKey(attivita)) {
                    lista = mappa.get(attivita);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(attivita, lista);
            }
        }

        return (LinkedHashMap) arrayService.sortVuota(mappa);
    }


    public List<WrapDidascalia> sortByAttivita(List<WrapDidascalia> listaWrapNonOrdinata) {
        List<WrapDidascalia> sortedList = new ArrayList<>();
        List<WrapDidascalia> listaConAttivitaOrdinata = new ArrayList<>(); ;
        List<WrapDidascalia> listaSenzaAttivitaOrdinata = new ArrayList<>(); ;

        listaConAttivitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isValid(wrap.getNazionalitaSingola()))
                .sorted(Comparator.comparing(funAttivita))
                .collect(Collectors.toList());

        listaSenzaAttivitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isEmpty(wrap.getNazionalitaSingola()))
                .collect(Collectors.toList());

        sortedList.addAll(listaConAttivitaOrdinata);
        sortedList.addAll(listaSenzaAttivitaOrdinata);
        return sortedList;
    }

}
