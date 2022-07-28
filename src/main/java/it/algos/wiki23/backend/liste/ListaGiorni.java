package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 25-Jul-2022
 * Time: 07:36
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaGiorni extends Lista {

    private String nomeGiorno;

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaGiorni.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaGiorni() {
    }// end of constructor


    public ListaGiorni nascita(final String nomeGiorno) {
        this.nomeGiorno = nomeGiorno;
        super.typeCrono = AETypeCrono.nascita;
        super.typeDidascalia = AETypeDidascalia.annoNascita;
        return this;
    }

    public ListaGiorni morte(final String nomeGiorno) {
        this.nomeGiorno = nomeGiorno;
        super.typeCrono = AETypeCrono.morte;
        super.typeDidascalia = AETypeDidascalia.annoMorte;
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
                case nascita -> bioService.fetchGiornoNato(nomeGiorno);
                case morte -> bioService.fetchGiornoMorto(nomeGiorno);
                default -> null;
            };
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            return null;
        }

        return listaBio;
    }

    /**
     * Lista ordinata di tutti i wrapLista che hanno una valore valido per la pagina specifica <br>
     */
    @Override
    public List<WrapLista> listaWrap() {
        WrapLista wrap;
        AETypeDidascalia typeDidascalia;

        super.listaWrap();

        if (listaBio != null && listaWrap != null) {
            typeDidascalia = switch (typeCrono) {
                case nascita -> AETypeDidascalia.giornoNascita;
                case morte -> AETypeDidascalia.giornoMorte;
                default -> null;
            };
            for (Bio bio : listaBio) {
                wrap = didascaliaService.getWrap(bio, typeDidascalia);
                listaWrap.add(wrap);
            }
        }

        return listaWrap;
    }


    /**
     * Mappa ordinata di tutti i wrapLista che hanno una valore valido per la pagina specifica <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public LinkedHashMap<String, List<WrapLista>> mappaWrap() {
        String paragrafo;
        List<WrapLista> lista;

        super.mappaWrap();

        if (listaWrap != null && mappaWrap != null) {
            for (WrapLista wrap : listaWrap) {
                paragrafo = wrap.titoloParagrafo;

                if (mappaWrap.containsKey(paragrafo)) {
                    lista = mappaWrap.get(paragrafo);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappaWrap.put(paragrafo, lista);

            }
        }

        return mappaWrap;
    }

    /**
     * Mappa ordinata di tutti le didascalie che hanno una valore valido per la pagina specifica <br>
     * Le didascalie usano SPAZIO_NON_BREAKING al posto di SPAZIO (se previsto) <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public LinkedHashMap<String, List<String>> mappaDidascalia() {
        String didascalia;
        List<WrapLista> listaWrap;
        List<String> listaDidascalie;

        super.mappaDidascalia();

        if (mappaWrap != null && mappaDidascalia != null) {
            for (String paragrafo : mappaWrap.keySet()) {
                listaWrap = mappaWrap.get(paragrafo);
                if (listaWrap != null) {
                    listaDidascalie = new ArrayList<>();
                    for (WrapLista wrap : listaWrap) {
                        didascalia = wrap.didascaliaLunga;
                        if (Pref.usaNonBreaking.is()) {
                            didascalia = didascalia.replaceAll(SPAZIO, SPAZIO_NON_BREAKING);
                        }
                        listaDidascalie.add(didascalia);
                    }
                    mappaDidascalia.put(paragrafo, listaDidascalie);
                }
            }
        }

        return mappaDidascalia;
    }

    /**
     * Testo del body di upload con paragrafi e righe <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Fino a 3 numVoci niente <div col></div>
     * Da 3 numVoci a 200 un <div col></div> generale
     * Con più di 200 numVoci, titoli dei paragrafi e <div col></div> ogni paragrafo
     */
    @Override
    public WResult testoBody() {
        StringBuffer buffer = new StringBuffer();
        List<String> lista;
        int numVoci = wikiUtility.getSizeAll(mappaDidascalia); ;
        boolean usaDiv;

        super.testoBody();

        if (mappaDidascalia != null && mappaDidascalia.size() > 0) {
            numVoci = wikiUtility.getSizeAll(mappaDidascalia);
            for (String paragrafo : mappaDidascalia.keySet()) {
                lista = mappaDidascalia.get(paragrafo);
                usaDiv = lista.size() > 3;

                buffer.append(DOPPIO_UGUALE);
                buffer.append(paragrafo);
                buffer.append(DOPPIO_UGUALE);
                buffer.append(CAPO);
                buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
                for (String didascalia : lista) {
                    buffer.append(ASTERISCO + didascalia);
                    buffer.append(CAPO);
                }
                buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);
                buffer.append(CAPO);
            }

        }

        return WResult.crea().content(buffer.toString().trim()).intValue(numVoci);
    }

    /**
     * Mappa ordinata dei wrapper (WrapDidascalia) per gestire i dati necessari ad una didascalia <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie() {
        super.mappaWrapDidascalie();

        LinkedHashMap<String, List<WrapDidascalia>> mappaMese = creaMappaSecolo(listaWrapDidascalie);
        LinkedHashMap<String, List<WrapDidascalia>> mappaLista;

        if (mappaMese != null) {
            for (String key : mappaMese.keySet()) {
                mappaLista = creaMappaAnni(mappaMese.get(key));
                mappaWrapDidascalie.put(key, mappaLista);
            }
        }

        return mappaWrapDidascalie;
    }


    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaSecolo(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List<WrapDidascalia>> mappa = new LinkedHashMap<>();
        List lista;
        String secolo;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                secolo = switch (typeCrono) {
                    case nascita -> wrap.getSecoloParagrafoNato();
                    case morte -> wrap.getSecoloParagrafoMorto();
                    default -> VUOTA;
                };

                if (mappa.containsKey(secolo)) {
                    lista = mappa.get(secolo);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(secolo, lista);
            }
        }

        return mappa;
    }


    public LinkedHashMap<String, List<WrapDidascalia>> creaMappaAnni(List<WrapDidascalia> listaWrapNonOrdinata) {
        LinkedHashMap<String, List<WrapDidascalia>> mappa = new LinkedHashMap<>();
        List lista;
        String anno;

        if (listaWrapNonOrdinata != null) {
            for (WrapDidascalia wrap : listaWrapNonOrdinata) {
                anno = switch (typeCrono) {
                    case nascita -> wikiUtility.linkAnnoNatoTesta(wrap.getAnnoNato());
                    case morte -> wikiUtility.linkAnnoMortoTesta(wrap.getAnnoMorto());
                };

                if (textService.isEmpty(anno)) {
                    anno = VUOTA;
                }

                if (mappa.containsKey(anno)) {
                    lista = mappa.get(anno);
                }
                else {
                    lista = new ArrayList();
                }
                lista.add(wrap);
                mappa.put(anno, lista);
            }
        }

        for (String key : mappa.keySet()) {
            lista = mappa.get(key);
            lista = sortByCognome(lista);
            mappa.put(key, lista);
        }

        return mappa;
    }

}
