package it.algos.wiki23.backend.liste;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 29-Jul-2022
 * Time: 17:14
 * <p>
 * Superclasse astratta per le classi ListaGiorni e ListaAnni <br>
 */
public abstract class ListaGiorniAnni extends Lista {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAnni.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaGiorniAnni() {
    }// end of constructor



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
        String newText = VUOTA;
        List<String> lista;
        int numVoci = 0;

        super.testoBody();

        if (mappaDidascalia != null && mappaDidascalia.size() > 0) {
            numVoci = wikiUtility.getSizeAll(mappaDidascalia);

            if (numVoci < 200) {
                newText = testoSenzaParagrafi(numVoci);
            }
            else {
                newText = testoConParagrafi(numVoci);
            }
        }

        return WResult.crea().content(newText).intValue(numVoci);
    }


    public String testoSenzaParagrafi(int numVoci) {
        StringBuffer buffer = new StringBuffer();
        List<String> lista;
        boolean usaDiv = numVoci > 3;

        buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
        for (String paragrafo : mappaDidascalia.keySet()) {
            lista = mappaDidascalia.get(paragrafo);
            for (String didascalia : lista) {
                buffer.append(ASTERISCO + didascalia);
                buffer.append(CAPO);
            }
        }
        buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);

        return buffer.toString().trim();
    }


    public String testoConParagrafi(int numVoci) {
        StringBuffer buffer = new StringBuffer();
        List<String> lista;
        boolean usaDiv;

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

        return buffer.toString().trim();
    }

}
