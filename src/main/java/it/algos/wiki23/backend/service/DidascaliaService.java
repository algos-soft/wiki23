package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 15-ago-2021
 * Time: 08:18
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ADidascaliaService.class); <br>
 * 3) @Autowired public DidascaliaService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DidascaliaService extends WAbstractService {

    private static String NATI = "Nati nel ";

    private static String MORTI = "Morti nel ";

    /**
     * Restituisce una lista semplice di didascalie complete <br>
     *
     * @param listaBio di biografie in ingresso
     *
     * @return lista di didascalie completa
     */
    public List<String> getLista(final List<Bio> listaBio) {
        List<String> listaDidascalie = null;
        String didascalia;

        if (listaBio != null) {
            listaDidascalie = new ArrayList<>();
            for (Bio bio : listaBio) {
                didascalia = getDidascaliaLista(bio);
                if (textService.isValid(didascalia)) {
                    listaDidascalie.add(didascalia);
                }
            }
        }

        return listaDidascalie;
    }

    /**
     * Costruisce la didascalia completa per una lista (persone di nome, persone di cognome) <br>
     *
     * @param bio completa
     *
     * @return didascalia completa
     */
    public String getDidascaliaLista(final Bio bio) {
        String nomeCognome = this.getNomeCognome(bio);
        String attivitaNazionalita = this.getAttivitaNazionalita(bio);
        String natoMorto = this.getNatoMorto(bio);

        return String.format("%s%s%s%s%s%s", ASTERISCO, nomeCognome, VIRGOLA_SPAZIO, attivitaNazionalita, SPAZIO, natoMorto);
    }

    /**
     * Costruisce il nome e cognome (obbligatori) <br>
     * Si usa il titolo della voce direttamente, se non contiene parentesi <br>
     *
     * @param bio completa
     *
     * @return nome e cognome elaborati e inseriti nelle doppie quadre
     */
    public String getNomeCognome(final Bio bio) {
        return getNomeCognome(bio.wikiTitle, bio.nome, bio.cognome);
    }

    /**
     * Costruisce il nome e cognome (obbligatori) <br>
     * Si usa il titolo della voce direttamente, se non contiene parentesi <br>
     *
     * @param wikiTitle della pagina sul server wiki
     * @param nome      semplice (solo primo nome esclusi i nomi doppi)
     * @param cognome   completo
     *
     * @return nome e cognome elaborati e inseriti nelle doppie quadre
     */
    public String getNomeCognome(final String wikiTitle, final String nome, final String cognome) {
        String nomeCognome;
        String tagPar = PARENTESI_TONDA_INI;
        String tagPipe = PIPE;
        String nomePrimaDellaParentesi;
        boolean usaNomeCognomePerTitolo = false;

        if (usaNomeCognomePerTitolo) {
            nomeCognome = nome + SPAZIO + cognome;
            if (!nomeCognome.equals(wikiTitle)) {
                nomeCognome = wikiTitle + tagPipe + nomeCognome;
            }
        }
        else {
            // se il titolo NON contiene la parentesi, il nome non va messo perché coincide col titolo della voce
            if (wikiTitle.contains(tagPar)) {
                nomePrimaDellaParentesi = wikiTitle.substring(0, wikiTitle.indexOf(tagPar));
                nomeCognome = wikiTitle + tagPipe + nomePrimaDellaParentesi;
            }
            else {
                nomeCognome = wikiTitle;
            }
        }

        nomeCognome = nomeCognome.trim();
        nomeCognome = textService.setDoppieQuadre(nomeCognome);

        return nomeCognome;
    }

    /**
     * Costruisce attività e nazionalità (obbligatori) <br>
     *
     * @param bio completa
     *
     * @return attività e nazionalità elaborati
     */
    public String getAttivitaNazionalita(final Bio bio) {
        return getAttivitaNazionalita(bio.wikiTitle, bio.attivita, bio.attivita2, bio.attivita3, bio.nazionalita);
    }

    /**
     * Costruisce attività e nazionalità (obbligatori) <br>
     *
     * @param wikiTitle   della pagina sul server wiki
     * @param attivita    principale
     * @param attivita2   facoltativa
     * @param attivita3   facoltativa
     * @param nazionalita unica
     *
     * @return attività e nazionalità elaborati
     */
    public String getAttivitaNazionalita(final String wikiTitle, final String attivita, final String attivita2, final String attivita3, final String nazionalita) {
        String attivitaNazionalita = VUOTA;

        if (textService.isValid(attivita)) {
            attivitaNazionalita += attivita;
        }

        if (textService.isValid(attivita2)) {
            if (textService.isValid(attivita3)) {
                attivitaNazionalita += VIRGOLA_SPAZIO;
            }
            else {
                attivitaNazionalita += SPAZIO + "e" + SPAZIO;
            }
            attivitaNazionalita += attivita2;
        }

        if (textService.isValid(attivita3)) {
            attivitaNazionalita += SPAZIO + "e" + SPAZIO;
            attivitaNazionalita += attivita3;
        }

        if (textService.isValid(nazionalita)) {
            attivitaNazionalita += SPAZIO;
            attivitaNazionalita += nazionalita;
        }

        return attivitaNazionalita;
    }

    /**
     * Costruisce il blocco luogo-anno-nascita-morte (facoltativi) <br>
     *
     * @param bio completa
     *
     * @return luogo-anno-nascita-morte
     */
    public String getNatoMorto(final Bio bio) {
        String crono = VUOTA;
        String tagNato = WPref.simboloNato.getStr();
        String tagMorto = WPref.simboloMorto.getStr();
        String luogoNato = textService.isValid(bio.luogoNato) ? bio.luogoNato : VUOTA;
        String luogoNatoLink = bio.luogoNatoLink;
        String annoNato = linkAnnoNato(bio);
        String luogoMorto = textService.isValid(bio.luogoMorto) ? bio.luogoMorto : VUOTA;
        String luogoMortoLink = bio.luogoMortoLink;
        String annoMorto = linkAnnoMorto(bio);

        if (textService.isValid(luogoNatoLink)) {
            luogoNato = luogoNatoLink + PIPE + luogoNato;
        }
        luogoNato = textService.setDoppieQuadre(luogoNato);
        if (textService.isValid(luogoMortoLink)) {
            luogoMorto = luogoMortoLink + PIPE + luogoMorto;
        }
        luogoMorto = textService.setDoppieQuadre(luogoMorto);

        crono += textService.isValid(luogoNato) ? luogoNato : VUOTA;
        crono += textService.isValid(luogoNato) && textService.isValid(annoNato) ? VIRGOLA_SPAZIO : VUOTA;
        crono += annoNato;
        crono += textService.isValid(crono) && (textService.isValid(luogoMorto) || textService.isValid(annoMorto)) ? SEP : VUOTA;
        crono += textService.isValid(luogoMorto) ? luogoMorto : VUOTA;
        crono += textService.isValid(luogoMorto) && textService.isValid(annoMorto) ? VIRGOLA_SPAZIO : VUOTA;
        crono += annoMorto;

        //        crono = text.levaCoda(crono, SEP);
        return textService.isValid(crono) ? textService.setTonde(crono) : VUOTA;
    }

    public String linkAnnoNato(final Bio bio) {
        if (bio != null && textService.isValid(bio.annoNato)) {
            return linkAnnoNato(bio.annoNato);
        }
        else {
            return VUOTA;
        }
    }

    public String linkAnnoNato(String annoNato) {
        String annoNatoLinkato = annoNato;
        String tagNato = WPref.simboloNato.getStr();
        Object obj = WPref.linkCrono.getEnumCurrentObj();
        if (obj instanceof AETypeLinkCrono type) {
            switch (type) {
                case voce -> annoNatoLinkato = textService.setDoppieQuadre(annoNato);
                case lista -> {
                    annoNato = wikiUtility.nati(annoNato) + PIPE + annoNato;
                    annoNatoLinkato = textService.setDoppieQuadre(annoNato);
                }
                case nessuno -> annoNatoLinkato = annoNato;
                default -> annoNatoLinkato = annoNato;
            } ;

        }

        return tagNato + annoNatoLinkato;
    }

    public String linkAnnoMorto(final Bio bio) {
        if (bio != null && textService.isValid(bio.annoMorto)) {
            return linkAnnoMorto(bio.annoMorto);
        }
        else {
            return VUOTA;
        }
    }

    public String linkAnnoMorto(String annoMorto) {
        String annoMortoLinkato = annoMorto;
        String tagMorto = WPref.simboloMorto.getStr();
        Object obj = WPref.linkCrono.getEnumCurrentObj();
        if (obj instanceof AETypeLinkCrono type) {
            switch (type) {
                case voce -> annoMortoLinkato = textService.setDoppieQuadre(annoMorto);
                case lista -> {
                    annoMorto = wikiUtility.morti(annoMorto) + PIPE + annoMorto;
                    annoMortoLinkato = textService.setDoppieQuadre(annoMorto);
                }
                case nessuno -> annoMortoLinkato = annoMorto;
                default -> annoMortoLinkato = annoMorto;
            } ;

        }

        return tagMorto + annoMortoLinkato;
    }

    //    /**
    //     * Costruisce il blocco luogo-anno-nascita-morte (facoltativi) <br>
    //     *
    //     * @param wikiTitle      della pagina sul server wiki
    //     * @param luogoNato      facoltativo
    //     * @param luogoNatoLink  facoltativo
    //     * @param annoNato       facoltativo
    //     * @param luogoMorto     facoltativo
    //     * @param luogoMortoLink facoltativo
    //     * @param annoMorto      facoltativo
    //     *
    //     * @return luogo-anno-nascita-morte
    //     */
    //    public String getNatoMorto(final String wikiTitle, final String luogoNato, final String luogoNatoLink, final String annoNato, final String luogoMorto, final String luogoMortoLink, final String annoMorto) {
    //        String natoMorto = VUOTA;
    //
    //        natoMorto+=luogoNato;
    //        natoMorto+=luogoMorto;
    //
    //        return text.setTonde(natoMorto);
    //    }


}