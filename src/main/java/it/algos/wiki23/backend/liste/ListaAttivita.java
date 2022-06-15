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
 * singolare(final String nomeAttivitaSingolare) <br>
 * plurale(final String nomeAttivitaPlurale) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {




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
        listaNomiSingoli = attivitaBackend.findSingolariByPlurale(attivita.plurale);
        return this;
    }

    public ListaAttivita singolare(final String nomeAttivitaSingolare) {
        listaNomiSingoli = arrayService.creaArraySingolo(nomeAttivitaSingolare);
        return this;
    }

    public ListaAttivita plurale(final String nomeAttivitaPlurale) {
        listaNomiSingoli = attivitaBackend.findSingolariByPlurale(nomeAttivitaPlurale);
        return this;
    }


    /**
     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     */
    @Override
    public List<Bio> listaBio() {
        super.listaBio();

        try {
            listaBio = bioService.fetchAttivita(listaNomiSingoli);
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




    public List<WrapDidascalia> sortByNazionalita(List<WrapDidascalia> listaWrapNonOrdinata) {
        List<WrapDidascalia> sortedList = new ArrayList<>();
        List<WrapDidascalia> listaConNazionalitaOrdinata = new ArrayList<>(); ;
        List<WrapDidascalia> listaSenzaNazionalitaOrdinata = new ArrayList<>(); ;

        listaConNazionalitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isValid(wrap.getNazionalitaSingola()))
                .sorted(Comparator.comparing(funNazionalita))
                .collect(Collectors.toList());

        listaSenzaNazionalitaOrdinata = listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isEmpty(wrap.getNazionalitaSingola()))
                .collect(Collectors.toList());

        sortedList.addAll(listaConNazionalitaOrdinata);
        sortedList.addAll(listaSenzaNazionalitaOrdinata);
        return sortedList;
    }





}
