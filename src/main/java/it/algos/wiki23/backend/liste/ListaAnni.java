package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 17-Jul-2022
 * Time: 06:37
 * <p>
 * Lista delle biografie per anni <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadAnni con appContext.getBean(ListaAnni.class).mappaDidascalie() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con mappaDidascalie() <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAnni extends Lista {

    private String nomeAnno;

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAnni.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaAnni() {
    }// end of constructor


    public ListaAnni nascita(final String nomeAnno) {
        this.nomeAnno = nomeAnno;
        super.typeCrono = AETypeCrono.nascita;
        super.typeDidascalia = AETypeDidascalia.giornoNascita;
        return this;
    }

    public ListaAnni morte(final String nomeAnno) {
        this.nomeAnno = nomeAnno;
        super.typeCrono = AETypeCrono.morte;
        super.typeDidascalia = AETypeDidascalia.giornoMorte;
        return this;
    }

    /**
     * Lista ordinata (per cognome) delle biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     */
    @Override
    public List<Bio> listaBio() {
        super.listaBio();

        try {
            listaBio = switch (typeCrono) {
                case nascita -> bioService.fetchAnnoNato(nomeAnno);
                case morte -> bioService.fetchAnnoMorto(nomeAnno);
                default -> null;
            };
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            return null;
        }

        return listaBio;
    }


    /**
     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {
        super.mappaWrapDidascalie();

        LinkedHashMap<String, List<WrapDidascalia>> mappaMese = creaMappaMese(listaWrapDidascalie);
        LinkedHashMap<String, List<WrapDidascalia>> mappaLista;

        if (mappaMese != null) {
            for (String key : mappaMese.keySet()) {
                mappaLista = creaMappaGiorni(mappaMese.get(key));
                mappaWrapDidascalie.put(key, mappaLista);
            }
        }

        return mappaWrapDidascalie;
    }


    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaMese(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List<WrapDidascalia>> mappa = new LinkedHashMap<>();
        List lista;
        String mese;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                mese = switch (typeCrono) {
                    case nascita -> wrap.getMeseParagrafoNato();
                    case morte -> wrap.getMeseParagrafoMorto();
                    default -> VUOTA;
                };

                if (mappa.containsKey(mese)) {
                    lista = mappa.get(mese);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(mese, lista);
            }
        }

        return mappa;
    }


    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaGiorni(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List<WrapDidascalia>> mappa = new LinkedHashMap<>();
        List lista;
        String giorno;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                giorno = switch (typeCrono) {
                    case nascita -> wikiUtility.linkGiornoNato(wrap.getGiornoNato(),false);
                    case morte -> wikiUtility.linkGiornoMorto(wrap.getGiornoMorto(),false);
                };

                if (textService.isEmpty(giorno)) {
                    giorno = VUOTA;
                }

                if (mappa.containsKey(giorno)) {
                    lista = mappa.get(giorno);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(giorno, lista);
            }
        }

        for (String key : mappa.keySet()) {
            lista = mappa.get(key);
            lista = sortByCognome(lista);
            mappa.put(key, lista);
        }

        return mappa;
    }

    public List<WrapDidascalia> sortByMese(List<WrapDidascalia> listaWrapNonOrdinata) {
        return listaWrapNonOrdinata
                .stream()
                .filter(wrap -> textService.isValid(wrap.getMeseParagrafoNato()))
                .sorted(Comparator.comparing(funGiornoNato))
                .collect(Collectors.toList());
    }

    public String fixTitolo(String wikiTitleBase, String paragrafo) {
        if (textService.isValid(paragrafo)) {
            return wikiUtility.fixTitolo(titoloParagrafo, paragrafo);
        }
        else {
            return "Senza giorno di nascita";
        }
    }

}
