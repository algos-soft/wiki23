package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
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
 * La lista è una mappa di WrapLista suddivisa in paragrafi, che contiene tutte le informazioni per scrivere le righe della pagina <br>
 * Usata fondamentalmente da UploadAttivita con appContext.getBean(ListaAttivita.class).plurale(nomeAttività).mappaWrap() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con plurale(nomeAttività) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class).plurale(nomeAttività).mappaWrap() <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaAttivita() {
        super.paragrafoAltre = TAG_LISTA_ALTRE;
    }// end of constructor


    public ListaAttivita attivita(final Attivita attivita) {
        this.nomeLista = attivita.paragrafo;
        listaNomiSingoli = attivitaBackend.findSingolariByPlurale(attivita.paragrafo);
        return this;
    }

    public ListaAttivita singolare(final String nomeAttivitaSingolare) {
        this.nomeLista = nomeAttivitaSingolare;
        super.typeLista = AETypeLista.attivitaSingolare;
//        listaNomiSingoli = arrayService.creaArraySingolo(nomeAttivitaSingolare);
        return this;
    }

    public ListaAttivita plurale(final String nomeAttivitaPlurale) {
        this.nomeLista = nomeAttivitaPlurale;
        super.typeLista = AETypeLista.attivitaPlurale;
//        listaNomiSingoli = attivitaBackend.findSingolariByPlurale(nomeAttivitaPlurale);
        return this;
    }


    /**
     * Ordina la mappa <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    public LinkedHashMap<String, List<WrapLista>> sortMap(LinkedHashMap<String, List<WrapLista>> mappa) {
        return wikiUtility.sort(mappa);
    }



    //    /**
//     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
//     */
//    @Override
//    public List<Bio> listaBio() {
//        super.listaBio();
//
//        try {
//            listaBio = bioService.fetchAttivita(listaNomiSingoli);
//        } catch (Exception unErrore) {
//            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
//            return null;
//        }
//
//        return listaBio;
//    }
//
//
//    /**
//     * Costruisce una lista dei wrapper per gestire i dati necessari ad una didascalia <br>
//     * La sottoclasse specifica esegue l'ordinamento <br>
//     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
//     */
//    @Override
//    public List<WrapDidascalia> listaWrapDidascalie() {
//        listaWrapDidascalie = super.listaWrapDidascalie();
//        return listaWrapDidascalie != null ? sortByNazionalita(listaWrapDidascalie) : null;
//    }
//
//    /**
//     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
//     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
//     */
//    @Override
//    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {super.mappaWrapDidascalie();
//
//        LinkedHashMap<String, List<WrapDidascalia>> mappaNaz = creaMappaNazionalita(listaWrapDidascalie);
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
//
//
//    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaNazionalita(List<WrapDidascalia> listaWrapNonOrdinata) {
//        LinkedHashMap<String, List> mappa = new LinkedHashMap<>();
//        List lista;
//        String nazionalita;
//
//        if (listaWrapNonOrdinata != null) {
//            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
//                nazionalita = wrap.getNazionalitaParagrafo();
//                nazionalita = nazionalita != null ? nazionalita : VUOTA;
//
//                if (mappa.containsKey(nazionalita)) {
//                    lista = mappa.get(nazionalita);
//                }
//                else {
//                    lista = new ArrayList();
//                }
//                lista.add(wrap);
//                mappa.put(nazionalita, lista);
//            }
//        }
//
//        return (LinkedHashMap) arrayService.sortVuota(mappa);
//    }
//
//
//
//
//    public List<WrapDidascalia> sortByNazionalita(List<WrapDidascalia> listaWrapNonOrdinata) {
//        List<WrapDidascalia> sortedList = new ArrayList<>();
//        List<WrapDidascalia> listaConNazionalitaOrdinata = new ArrayList<>(); ;
//        List<WrapDidascalia> listaSenzaNazionalitaOrdinata = new ArrayList<>(); ;
//
//        listaConNazionalitaOrdinata = listaWrapNonOrdinata
//                .stream()
//                .filter(wrap -> textService.isValid(wrap.getNazionalitaSingola()))
//                .sorted(Comparator.comparing(funNazionalita))
//                .collect(Collectors.toList());
//
//        listaSenzaNazionalitaOrdinata = listaWrapNonOrdinata
//                .stream()
//                .filter(wrap -> textService.isEmpty(wrap.getNazionalitaSingola()))
//                .collect(Collectors.toList());
//
//        sortedList.addAll(listaConNazionalitaOrdinata);
//        sortedList.addAll(listaSenzaNazionalitaOrdinata);
//        return sortedList;
//    }

}
