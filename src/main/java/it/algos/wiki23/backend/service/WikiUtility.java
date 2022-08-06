package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.regex.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 20-feb-2022
 * Time: 08:56
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WikiUtility extends WAbstractService {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GiornoBackend giornoBackend;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnoBackend annoBackend;

    public String fixTitolo(String titoloGrezzo) {
        return fixTitolo(VUOTA, titoloGrezzo, 0);
    }

    public String fixTitolo(String wikiTitleBase, String titoloGrezzo) {
        return fixTitolo(wikiTitleBase, titoloGrezzo, 0);
    }

    public String fixTitolo(String titoloGrezzo, int numVoci) {
        return fixTitolo(VUOTA, titoloGrezzo, numVoci);
    }

    public String fixTitolo(String wikiTitleBase, String titoloGrezzo, int numVoci) {
        String paragrafoVisibile = VUOTA;

        if (textService.isValid(wikiTitleBase)) {
            if (!wikiTitleBase.endsWith(SLASH)) {
                wikiTitleBase += SLASH;
            }
            paragrafoVisibile = wikiTitleBase;
        }

        if (textService.isValid(titoloGrezzo)) {
            if (titoloGrezzo.length() > 1) {
                titoloGrezzo = textService.primaMaiuscola(titoloGrezzo);
            }
            paragrafoVisibile += titoloGrezzo;

            if (textService.isValid(wikiTitleBase)) {
                if (queryService.isEsiste(paragrafoVisibile)) {
                    paragrafoVisibile += PIPE;
                    paragrafoVisibile += titoloGrezzo;
                    paragrafoVisibile = textService.setDoppieQuadre(paragrafoVisibile);
                }
                else {
                    paragrafoVisibile = titoloGrezzo;
                }
            }
            paragrafoVisibile = setParagrafo(paragrafoVisibile, numVoci);
        }
        else {
            paragrafoVisibile = "Altre...";
            paragrafoVisibile = setParagrafo(paragrafoVisibile, numVoci);
        }

        return paragrafoVisibile;
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

    public String setParagrafoSub(final String titolo) {
        String paragrafo = VUOTA;

        paragrafo += PARAGRAFO_SUB;
        paragrafo += SPAZIO;
        paragrafo += titolo;
        paragrafo += SPAZIO;
        paragrafo += PARAGRAFO_SUB;
        paragrafo += CAPO;

        return paragrafo;
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

        paragrafo += CAPO;
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
     * Contorna il testo con un wiki bold. <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa regolata secondo la property mediawiki
     */
    public String bold(String stringaIn) {
        String stringaOut = VUOTA;

        if (textService.isValid(stringaIn)) {
            stringaOut = TAG_BOLD;
            stringaOut += stringaIn;
            stringaOut += TAG_BOLD;
        }

        return stringaOut.trim();
    }


    public int getSizeAll(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
        int size = 0;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                size += getSize(mappa.get(key));
            }
        }

        return size;
    }

    public int getSizeAll(Map<String, List<String>> mappa) {
        int size = 0;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                size += mappa.get(key).size();
            }
        }

        return size;
    }

    public int getSizeAllWrap(Map<String, List<WrapLista>> mappa) {
        int size = 0;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                size += mappa.get(key).size();
            }
        }

        return size;
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

    public String wikiTitleNatiGiorno(String giorno) {
        return natiMortiGiorno("Nati", giorno);
    }

    public String wikiTitleMortiGiorno(String giorno) {
        return natiMortiGiorno("Morti", giorno);
    }


    public String wikiTitleNatiAnno(String anno) {
        return natiMortiAnno("Nati", anno);
    }

    public String wikiTitleMortiAnno(String anno) {
        return natiMortiAnno("Morti", anno);
    }

    public String wikiTitleAttivita(String attivitaPlurale) {
        return PATH_ATTIVITA + SLASH + textService.primaMaiuscola(attivitaPlurale);
    }

    public String wikiTitleNazionalita(String nazionalitaPlurale) {
        return PATH_NAZIONALITA + SLASH + textService.primaMaiuscola(nazionalitaPlurale);
    }

    public String fixSecoloNato(final Bio bio) {
        Anno anno = annoBackend.findByNome(bio.annoNato);
        return anno != null ? anno.getSecolo().nome : VUOTA;
    }

    public String fixSecoloMorto(final Bio bio) {
        Anno anno = annoBackend.findByNome(bio.annoMorto);
        return anno != null ? anno.getSecolo().nome : VUOTA;
    }

    public String fixMeseNato(final Bio bio) {
        Giorno giorno = giornoBackend.findByNome(bio.giornoNato);
        return giorno != null ? textService.primaMaiuscola(giorno.getMese().nome) : VUOTA;
    }

    public String fixMeseMorto(final Bio bio) {
        Giorno giorno = giornoBackend.findByNome(bio.giornoMorto);
        return giorno != null ? textService.primaMaiuscola(giorno.getMese().nome) : VUOTA;
    }


    public String fixParagrafoNazionalita(final Bio bio) {
        Nazionalita nazionalita = nazionalitaBackend.findFirstBySingolare(bio.nazionalita);
        return nazionalita != null ? textService.primaMaiuscola(nazionalita.plurale) : TAG_LISTA_ALTRE;
    }

    /**
     * I numeri che iniziano (parlato) con vocale richiedono l'apostrofo  <br>
     * Sono:
     * 1
     * 11
     * tutti quelli che iniziano con 8
     *
     * @param tag    nati/morti
     * @param giorno di riferimento
     *
     * @return titolo della pagina wiki
     */
    public String natiMortiGiorno(String tag, String giorno) {
        String testo = VUOTA;
        String tagNormale = tag + " il ";
        String tagSpecifico = tag + " l'";
        String regex = "(1|11.*|8.*)";

        if (textService.isEmpty(giorno)) {
            return VUOTA;
        }

        if (Pattern.matches(regex, giorno)) {
            return tagSpecifico + giorno;
        }
        else {
            return tagNormale + giorno;
        }
    }

    /**
     * I numeri che iniziano (parlato) con vocale richiedono l'apostrofo  <br>
     * Sono:
     * 1
     * 11
     * tutti quelli che iniziano con 8
     *
     * @param tag  nati/morti
     * @param anno di riferimento
     *
     * @return titolo della pagina wiki
     */
    private String natiMortiAnno(String tag, String anno) {
        String testo = VUOTA;
        String tagSpecifico = "l'";
        tag += " nel";
        List<Integer> lista = Arrays.asList(1, 8, 11);
        String regex = "(1|11|8.*)";

        if (textService.isEmpty(anno)) {
            return VUOTA;
        }

        if (Pattern.matches(regex, anno)) {
            testo = tag + tagSpecifico + anno;
        }
        else {
            testo = tag + SPAZIO + anno;
        }

        return testo;
    }


    public String linkGiornoNatoTesta(final Bio bio) {
        String giornoNato = bio.giornoNato;
        if (textService.isEmpty(giornoNato)) {
            return VUOTA;
        }

        return switch ((AETypeLinkCrono) WPref.linkCrono.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(giornoNato);
            case lista -> {
                giornoNato = this.wikiTitleNatiGiorno(giornoNato) + PIPE + giornoNato;
                yield textService.setDoppieQuadre(giornoNato);
            }
            case nessuno -> giornoNato;
        };
    }

    public String linkGiornoNatoCoda(final Bio bio) {
        String tagNato = WPref.usaSimboliCrono.is() ? WPref.simboloNato.getStr() : VUOTA;
        String giornoNatoLinkato = linkGiornoNatoTesta(bio);
        giornoNatoLinkato = textService.isValid(tagNato) ? tagNato + SPAZIO_NON_BREAKING + giornoNatoLinkato : giornoNatoLinkato;
        return textService.isValid(bio.giornoNato) ? textService.setTonde(giornoNatoLinkato) : VUOTA;
    }


    public String linkGiornoMortoTesta(final Bio bio) {
        String giornoMorto = bio.giornoMorto;
        if (textService.isEmpty(giornoMorto)) {
            return VUOTA;
        }

        return switch ((AETypeLinkCrono) WPref.linkCrono.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(giornoMorto);
            case lista -> {
                giornoMorto = this.wikiTitleMortiGiorno(giornoMorto) + PIPE + giornoMorto;
                yield textService.setDoppieQuadre(giornoMorto);
            }
            case nessuno -> giornoMorto;
        };
    }

    public String linkGiornoMortoCoda(final Bio bio) {
        String tagMorto = WPref.usaSimboliCrono.is() ? WPref.simboloMorto.getStr() : VUOTA;
        String giornoMortoLinkato = linkGiornoMortoTesta(bio);
        giornoMortoLinkato = textService.isValid(tagMorto) ? tagMorto + SPAZIO_NON_BREAKING + giornoMortoLinkato : giornoMortoLinkato;
        return textService.isValid(bio.giornoMorto) ? textService.setTonde(giornoMortoLinkato) : VUOTA;
    }


    public String linkAnnoNatoTesta(final Bio bio) {
        String annoNato = bio.annoNato;
        if (textService.isEmpty(annoNato)) {
            return VUOTA;
        }

        return switch ((AETypeLinkCrono) WPref.linkCrono.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(annoNato);
            case lista -> {
                annoNato = this.wikiTitleNatiAnno(annoNato) + PIPE + annoNato;
                yield textService.setDoppieQuadre(annoNato);
            }
            case nessuno -> annoNato;
        };
    }

    public String linkAnnoNatoCoda(final Bio bio) {
        String tagNato = WPref.usaSimboliCrono.is() ? WPref.simboloNato.getStr() : VUOTA;
        String annoNatoLinkato = linkAnnoNatoTesta(bio);
        annoNatoLinkato = textService.isValid(tagNato) ? tagNato + SPAZIO_NON_BREAKING + annoNatoLinkato : annoNatoLinkato;
        return textService.isValid(bio.annoNato) ? textService.setTonde(annoNatoLinkato) : VUOTA;
    }


    public String linkAnnoMortoTesta(final Bio bio) {
        String annoMorto = bio.annoMorto;
        if (textService.isEmpty(annoMorto)) {
            return VUOTA;
        }

        return switch ((AETypeLinkCrono) WPref.linkCrono.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(annoMorto);
            case lista -> {
                annoMorto = this.wikiTitleMortiAnno(annoMorto) + PIPE + annoMorto;
                yield textService.setDoppieQuadre(annoMorto);
            }
            case nessuno -> annoMorto;
        };
    }


    public String linkAnnoMortoCoda(final Bio bio) {
        String tagMorto = WPref.usaSimboliCrono.is() ? WPref.simboloMorto.getStr() : VUOTA;
        String annoMortoLinkato = linkAnnoMortoTesta(bio);
        annoMortoLinkato = textService.isValid(tagMorto) ? tagMorto + SPAZIO_NON_BREAKING + annoMortoLinkato : annoMortoLinkato;
        return textService.isValid(bio.annoMorto) ? textService.setTonde(annoMortoLinkato) : VUOTA;
    }

    /**
     * Ordina la mappa secondo la chiave
     *
     * @param mappaDisordinata in ingresso
     *
     * @return mappa ordinata, null se mappaDisordinata è null
     */
    public LinkedHashMap<String, List<WrapLista>> sort(final LinkedHashMap<String, List<WrapLista>> mappaDisordinata) {
        LinkedHashMap<String, List<WrapLista>> mappaOrdinata = new LinkedHashMap();
        Object[] listaChiavi;

        try {
            listaChiavi = mappaDisordinata.keySet().toArray();
            Arrays.sort(listaChiavi);
            for (Object chiave : listaChiavi) {
                mappaOrdinata.put((String) chiave, mappaDisordinata.get(chiave));
            }
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        return mappaOrdinata;
    }

}
