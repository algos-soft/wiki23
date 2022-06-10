package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 03-Jun-2022
 * Time: 16:08
 * <p>
 * Lista delle biografie per attività <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadAttivita con appContext.getBean(ListaAttivita.class).plurale(nomeAttivitaPlurale).xxx() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con 3 diversi metodi,
 * ognuno col suo parametro:
 * attivita(final Attivita attivita) <br>
 * singolare(final String attivitaSingolare) <br>
 * plurale(final String attivitaPlurale) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {


    //--property
    private List<String> listaNomiAttivitaSingole;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaAttivita() {
        super.titoloParagrafo = "Progetto:Biografie/Nazionalità/";
    }// end of constructor


    public ListaAttivita attivita(final Attivita attivita) {
        listaNomiAttivitaSingole = attivitaBackend.findSingolariByPlurale(attivita.plurale);
        return this;
    }

    public ListaAttivita singolare(final String attivitaSingolare) {
        listaNomiAttivitaSingole = arrayService.creaArraySingolo(attivitaSingolare);
        return this;
    }

    public ListaAttivita plurale(final String attivitaPlurale) {
        listaNomiAttivitaSingole = attivitaBackend.findSingolariByPlurale(attivitaPlurale);
        return this;
    }


    /**
     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     */
    @Override
    public List<Bio> listaBio() {
        super.listaBio();

        try {
            listaBio = bioService.fetchAttivita(listaNomiAttivitaSingole);
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
        return sortByNazionalita(listaWrapDidascalie);
    }

    /**
     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {
        super.mappaWrapDidascalie();

        LinkedHashMap<String, List<WrapDidascalia>> mappaNaz = creaMappaNazionalita(listaWrapDidascalie);
        LinkedHashMap<String, List<WrapDidascalia>> mappaLista;

        if (mappaNaz != null) {
            for (String key : mappaNaz.keySet()) {
                mappaLista = creaMappaCarattere(mappaNaz.get(key));
                mappaWrapDidascalie.put(key, mappaLista);
            }
        }

        return mappaWrapDidascalie;
    }

    /**
     * Mappa ordinata delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie() {
        super.mappaDidascalie();

        LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;
        List<WrapDidascalia> listaWrap;
        List<String> listaDidascalia;
        String didascalia;

        for (String key1 : mappaWrapDidascalie.keySet()) {
            mappaWrap = mappaWrapDidascalie.get(key1);
            mappaDidascalie.put(key1, new LinkedHashMap<>());

            for (String key2 : mappaWrap.keySet()) {
                listaWrap = mappaWrap.get(key2);
                listaDidascalia = new ArrayList<>();
                for (WrapDidascalia wrap : listaWrap) {
                    didascalia = didascaliaService.getDidascaliaLista(wrap.getBio());
                    listaDidascalia.add(didascalia);
                }
                mappaDidascalie.get(key1).put(key2, listaDidascalia);
            }
        }

        return mappaDidascalie;
    }

    /**
     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi() {
        super.mappaParagrafi();

        LinkedHashMap<String, List<String>> mappaSub;
        String paragrafo;

        for (String key : mappaDidascalie.keySet()) {
            paragrafo = key;
            mappaSub = mappaDidascalie.get(key);
            paragrafo = fixTitolo(paragrafo);

            mappaParagrafi.put(paragrafo, mappaSub);
        }

        return mappaParagrafi;
    }

    public String fixTitolo(String titoloGrezzo) {
        String paragrafoVisibile = VUOTA;

        if (textService.isValid(titoloGrezzo)) {
            paragrafoVisibile = titoloParagrafo;
            paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
            paragrafoVisibile += PIPE;
            paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
            paragrafoVisibile = textService.setDoppieQuadre(paragrafoVisibile);
            paragrafoVisibile = PARAGRAFO + paragrafoVisibile + PARAGRAFO;
        }
        else {
            paragrafoVisibile = "Altre...";
            paragrafoVisibile = wikiUtility.setParagrafo(paragrafoVisibile);
        }

        return paragrafoVisibile;
    }


    /**
     * Mappa dei paragrafi delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che è il titolo visibile del paragrafo <br>
     * Nel titolo visibile del paragrafo viene riportato il numero di voci biografiche presenti <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     */
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafiDimensionati() {
        super.mappaParagrafiDimensionati();

        LinkedHashMap<String, List<String>> mappaSub;
        String paragrafoDimensionato;

        for (String key : mappaDidascalie.keySet()) {
            paragrafoDimensionato = key;
            mappaSub = mappaDidascalie.get(key);
            paragrafoDimensionato = fixTitoloDimensionato(paragrafoDimensionato, getSize(mappaSub));

            mappaParagrafiDimensionati.put(paragrafoDimensionato, mappaSub);
        }

        return mappaParagrafiDimensionati;
    }


    public String fixTitoloDimensionato(String titoloGrezzo, int numVoci) {
        String paragrafoVisibile = VUOTA;

        if (textService.isValid(titoloGrezzo)) {
            paragrafoVisibile = titoloParagrafo;
            paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
            paragrafoVisibile += PIPE;
            paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
            paragrafoVisibile = textService.setDoppieQuadre(paragrafoVisibile);
            paragrafoVisibile = wikiUtility.setParagrafo(paragrafoVisibile, numVoci);
        }
        else {
            paragrafoVisibile = "Altre...";
            paragrafoVisibile = wikiUtility.setParagrafo(paragrafoVisibile, numVoci);
        }

        return paragrafoVisibile;
    }

    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaNazionalita(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List> mappa = new LinkedHashMap<>();
        List lista;
        String nazionalita;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                nazionalita = wrap.getNazionalitaParagrafo();
                nazionalita = nazionalita != null ? nazionalita : VUOTA;

                if (mappa.containsKey(nazionalita)) {
                    lista = mappa.get(nazionalita);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(nazionalita, lista);
            }
        }

        return (LinkedHashMap) arrayService.sortVuota(mappa);
    }


    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaCarattere(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List<WrapDidascalia>> mappa = new LinkedHashMap<>();
        List lista;
        String primoCarattere;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                primoCarattere = wrap.getPrimoCarattere();
                if (mappa.containsKey(primoCarattere)) {
                    lista = mappa.get(primoCarattere);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(primoCarattere, lista);
            }
        }

        for (String key : mappa.keySet()) {
            lista = mappa.get(key);
            lista = sortByCognome(lista);
            mappa.put(key, lista);
        }

        //        return arrayService.sort(mappa);
        return mappa;
    }

    //    public TreeMap<String, TreeMap<String, List<String>>> creaMappa() {
    //        TreeMap<String, TreeMap<String, List<String>>> mappa = new TreeMap<>();
    //        LinkedHashMap<String, List<WrapDidascalia>> mappaSub;
    //        List<WrapDidascalia> listaWrap;
    //        List<String> listaDidascalia;
    //        String didascalia;
    //
    //        if (mappaWrapDidascalie != null) {
    //            for (String key1 : mappaWrapDidascalie.keySet()) {
    //                mappaSub = mappaWrapDidascalie.get(key1);
    //                mappa.put(key1, new TreeMap<>());
    //
    //                for (String key2 : mappaSub.keySet()) {
    //                    listaWrap = mappaSub.get(key2);
    //                    listaDidascalia = new ArrayList<>();
    //                    for (WrapDidascalia wrap : listaWrap) {
    //                        didascalia = didascaliaService.getLista(wrap.getBio());
    //                        listaDidascalia.add(didascalia);
    //                    }
    //                    mappa.get(key1).put(key2, listaDidascalia);
    //                }
    //            }
    //        }
    //        this.mappa = mappa;
    //        return mappa;
    //    }


    public List<WrapDidascalia> sortByNazionalita(List<WrapDidascalia> listaWrapNonOrdinata) {
        List<WrapDidascalia> sortedList = new ArrayList<>();
        List<WrapDidascalia> listaConNazionalitaOrdinata = new ArrayList<>(); ;
        List<WrapDidascalia> listaSenzaNazionalitaOrdinata = new ArrayList<>(); ;

        listaConNazionalitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isValid(wrap.getNazionalitaSingola()))
                .sorted(Comparator.comparing(nazionalita))
                .collect(Collectors.toList());

        listaSenzaNazionalitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isEmpty(wrap.getNazionalitaSingola()))
                .collect(Collectors.toList());

        sortedList.addAll(listaConNazionalitaOrdinata);
        sortedList.addAll(listaSenzaNazionalitaOrdinata);
        return sortedList;
    }


    public int getSize(LinkedHashMap<String, List<String>> mappa) {
        int size = 0;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                size += mappa.get(key).size();
            }
        }

        return size;
    }


    public static Function<WrapDidascalia, String> nazionalita = wrap -> wrap.getNazionalitaSingola() != null ? wrap.getNazionalitaSingola() : VUOTA;


}
