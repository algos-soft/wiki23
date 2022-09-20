package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.wrapper.*;
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
                didascalia = lista(bio);
                if (textService.isValid(didascalia)) {
                    listaDidascalie.add(didascalia);
                }
            }
        }

        return listaDidascalie;
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
     * Costruisce la didascalia completa per una lista di nati nel giorno <br>
     * WikiTitle (sempre)
     * AttivitàNazionalità (sempre)
     * Anno di morte (opzionale)
     *
     * @param bio completa
     *
     * @return didascalia completa
     */
    public String didascaliaGiornoNato(final Bio bio) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getWikiTitle(bio));
        if (textService.isValid(getAttivitaNazionalita(bio))) {
            buffer.append(VIRGOLA_SPAZIO);
            buffer.append(getAttivitaNazionalita(bio));
        }
        buffer.append(SPAZIO);
        buffer.append(annoMortoSimboloParentesi(bio));

        return buffer.toString().trim();
    }

    /**
     * Costruisce la didascalia completa per una lista di morti nel giorno <br>
     * WikiTitle (sempre)
     * AttivitàNazionalità (sempre)
     * Anno di nascita (opzionale)
     *
     * @param bio completa
     *
     * @return didascalia completa
     */
    public String didascaliaGiornoMorto(final Bio bio) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getWikiTitle(bio));
        if (textService.isValid(getAttivitaNazionalita(bio))) {
            buffer.append(VIRGOLA_SPAZIO);
            buffer.append(getAttivitaNazionalita(bio));
        }
        buffer.append(SPAZIO);
        buffer.append(annoNatoSimboloParentesi(bio));

        return buffer.toString();
    }


    /**
     * Costruisce la didascalia completa per una lista di nati nell'anno <br>
     * WikiTitle (sempre)
     * AttivitàNazionalità (sempre)
     * Anno di morte (opzionale)
     *
     * @param bio completa
     *
     * @return didascalia completa
     */
    public String didascaliaAnnoNato(final Bio bio) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getWikiTitle(bio));
        if (textService.isValid(getAttivitaNazionalita(bio))) {
            buffer.append(VIRGOLA_SPAZIO);
            buffer.append(getAttivitaNazionalita(bio));
        }
        buffer.append(SPAZIO);
        buffer.append(annoMortoSimboloParentesi(bio));

        return buffer.toString();
    }

    /**
     * Costruisce la didascalia completa per una lista di morti nell'anno <br>
     *
     * @param bio completa
     *
     * @return didascalia completa
     */
    public String didascaliaAnnoMorto(final Bio bio) {
        StringBuffer buffer = new StringBuffer();
        boolean nonBreaking = Pref.usaNonBreaking.is();

        buffer.append(getWikiTitle(bio));
        if (textService.isValid(getAttivitaNazionalita(bio))) {
            buffer.append(VIRGOLA_SPAZIO);
            buffer.append(getAttivitaNazionalita(bio));
        }
        buffer.append(nonBreaking ? Pref.nonBreaking.getStr() : SPAZIO);
        buffer.append(annoNatoSimboloParentesi(bio));

        return buffer.toString();
    }


    public String getWikiTitle(final Bio bio) {
        String wikiTitle = bio.wikiTitle;
        String nomeVisibile;

        if (textService.isValid(wikiTitle)) {
            if (wikiTitle.contains(PARENTESI_TONDA_END)) {
                nomeVisibile = textService.levaCodaDaUltimo(wikiTitle, PARENTESI_TONDA_INI);
                wikiTitle += PIPE;
                wikiTitle += nomeVisibile;
            }
            wikiTitle = textService.setDoppieQuadre(wikiTitle);
        }

        return wikiTitle;
    }


    /**
     * Costruisce una wrapLista specializzata per le righe delle pagine 'Nati nel giorno' <br>
     * Contiene il paragrafo 'secolo'
     * Contiene l'inizio riga 'anno nascita'
     * Contiene la didascalia con 'wikiTitle', 'attività/nazionalità', 'anno di morte'
     *
     * @param bio completa
     *
     * @return wrapLista
     */
    public WrapLista getWrapGiornoNato(final Bio bio) {
        String paragrafo = wikiUtility.fixSecoloNato(bio);
        String paragrafoLink;

        paragrafoLink = switch ((AETypeLink) WPref.linkGiorniAnni.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(paragrafo);
            case nessuno -> paragrafo;
            default -> paragrafo;
        };

        if (textService.isEmpty(paragrafo)) {
            paragrafo = TAG_LISTA_NO_ANNO;
            paragrafoLink = paragrafo;
        }

        String sottoParagrafo = wikiUtility.linkAnnoNatoTesta(bio);
        String didascalia = this.didascaliaGiornoNato(bio);

        return new WrapLista(paragrafo, paragrafoLink, sottoParagrafo, didascalia);
    }


    /**
     * Costruisce una wrapLista specializzata per le righe delle pagine 'Morti nel giorno' <br>
     * Contiene il paragrafo 'secolo'
     * Contiene l'inizio riga 'anno morte'
     * Contiene la didascalia con 'wikiTitle', 'attività/nazionalità', 'anno di nascita'
     *
     * @param bio completa
     *
     * @return wrapLista
     */
    public WrapLista getWrapGiornoMorto(final Bio bio) {
        String paragrafo = wikiUtility.fixSecoloMorto(bio);
        String paragrafoLink;

        paragrafoLink = switch ((AETypeLink) WPref.linkGiorniAnni.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(paragrafo);
            case nessuno -> paragrafo;
            default -> paragrafo;
        };

        if (textService.isEmpty(paragrafo)) {
            paragrafo = TAG_LISTA_NO_ANNO;
            paragrafoLink = paragrafo;
        }

        String sottoParagrafo = wikiUtility.linkAnnoMortoTesta(bio);
        String didascalia = this.didascaliaGiornoMorto(bio);

        return new WrapLista(paragrafo, paragrafoLink, sottoParagrafo, didascalia);
    }

    /**
     * Costruisce una wrapLista specializzata per le righe delle pagine 'Nati nell'anno' <br>
     * Contiene il paragrafo 'mese'
     * Contiene l'inizio riga 'giorno nascita'
     * Contiene la didascalia con 'wikiTitle', 'attività/nazionalità', 'anno di morte'
     *
     * @param bio completa
     *
     * @return wrapLista
     */
    public WrapLista getWrapAnnoNato(final Bio bio) {
        String paragrafo = wikiUtility.fixMeseNato(bio);
        String paragrafoLink;

        paragrafoLink = switch ((AETypeLink) WPref.linkGiorniAnni.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(paragrafo);
            case nessuno -> paragrafo;
            default -> paragrafo;
        };

        if (textService.isEmpty(paragrafo)) {
            paragrafo = TAG_LISTA_NO_GIORNO;
            paragrafoLink = paragrafo;
        }

        String sottoParagrafo = wikiUtility.linkGiornoNatoTesta(bio);
        String didascalia = this.didascaliaAnnoNato(bio);

        return new WrapLista(paragrafo, paragrafoLink, sottoParagrafo, didascalia);
    }

    /**
     * Costruisce una wrapLista specializzata per le righe delle pagine 'Morti nell'anno' <br>
     * Contiene il paragrafo 'mese'
     * Contiene l'inizio riga 'giorno morte'
     * Contiene la didascalia con 'wikiTitle', 'attività/nazionalità', 'anno di nascita'
     *
     * @param bio completa
     *
     * @return wrapLista
     */
    public WrapLista getWrapAnnoMorto(final Bio bio) {
        String paragrafo = wikiUtility.fixMeseMorto(bio);
        String paragrafoLink;

        paragrafoLink = switch ((AETypeLink) WPref.linkGiorniAnni.getEnumCurrentObj()) {
            case voce -> textService.setDoppieQuadre(paragrafo);
            case nessuno -> paragrafo;
            default -> paragrafo;
        };

        if (textService.isEmpty(paragrafo)) {
            paragrafo = TAG_LISTA_NO_GIORNO;
            paragrafoLink = paragrafo;
        }

        String sottoParagrafo = wikiUtility.linkGiornoMortoTesta(bio);
        String didascalia = this.didascaliaAnnoMorto(bio);

        return new WrapLista(paragrafo, paragrafoLink, sottoParagrafo, didascalia);
    }

    /**
     * Costruisce una wrapLista specializzata per le righe delle pagine 'Attività' <br>
     * Contiene il paragrafo 'nazionalità'
     * Contiene il sotto-paragrafo 'primoCarattere'
     * Contiene la didascalia con 'wikiTitle', 'attività/nazionalità', 'luogo e anno di nascita', 'luogo e anno di morte'
     *
     * @param bio completa
     *
     * @return wrapLista
     */
    public WrapLista getWrapAttivita(final Bio bio) {
        Nazionalita nazionalita = nazionalitaBackend.findFirstBySingolare(bio.nazionalita);
        String paragrafo;
        String paragrafoLink;

        if (nazionalita != null) {
            paragrafo = textService.primaMaiuscola(nazionalita.pluraleLista);
            if (nazionalita.esistePaginaLista) {
                paragrafoLink = switch ((AETypeLink) WPref.linkAttNaz.getEnumCurrentObj()) {
                    case voce -> textService.setDoppieQuadre(paragrafo);
                    case lista -> textService.setDoppieQuadre(PATH_NAZIONALITA + SLASH + paragrafo + PIPE + paragrafo);
                    case nessuno -> paragrafo;
                };
            }
            else {
                paragrafoLink = paragrafo;
            }
        }
        else {
            paragrafo = TAG_LISTA_ALTRE;
            paragrafoLink = TAG_LISTA_ALTRE;
        }

        String sottoParagrafo = bio.ordinamento.substring(0, 1);
        String didascalia = this.lista(bio);

        return new WrapLista(paragrafo, paragrafoLink, sottoParagrafo, didascalia);
    }

    /**
     * Costruisce una wrapLista specializzata per le righe delle pagine 'Nazionalità' <br>
     * Contiene il paragrafo 'attivita'
     * Contiene il sotto-paragrafo 'primoCarattere'
     * Contiene la didascalia con 'wikiTitle', 'attività/nazionalità', 'luogo e anno di nascita', 'luogo e anno di morte'
     *
     * @param bio completa
     *
     * @return wrapLista
     */
    public WrapLista getWrapNazionalita(final Bio bio) {
        Attivita attivita = attivitaBackend.findFirstBySingolare(bio.attivita);
        String paragrafo;
        String paragrafoLink;

        if (attivita != null) {
            paragrafo = textService.primaMaiuscola(attivita.pluraleLista);
            if (attivita.esistePaginaLista) {
                paragrafoLink = switch ((AETypeLink) WPref.linkAttNaz.getEnumCurrentObj()) {
                    case voce -> textService.setDoppieQuadre(paragrafo);
                    case lista -> textService.setDoppieQuadre(PATH_ATTIVITA + SLASH + paragrafo + PIPE + paragrafo);
                    case nessuno -> paragrafo;
                };
            }
            else {
                paragrafoLink = paragrafo;
            }
        }
        else {
            paragrafo = TAG_LISTA_ALTRE;
            paragrafoLink = TAG_LISTA_ALTRE;
        }

        String sottoParagrafo = bio.ordinamento.substring(0, 1);
        String didascalia = this.lista(bio);

        return new WrapLista(paragrafo, paragrafoLink, sottoParagrafo, didascalia);
    }



    /**
     * Costruisce una wrapLista <br>
     *
     * @param bio completa
     *
     * @return wrapLista
     */
    public WrapLista getWrap(final AETypeLista typeLista, final Bio bio) {
        return switch (typeLista) {
            case giornoNascita -> this.getWrapGiornoNato(bio);
            case giornoMorte -> this.getWrapGiornoMorto(bio);
            case annoNascita -> this.getWrapAnnoNato(bio);
            case annoMorte -> this.getWrapAnnoMorto(bio);
            case nazionalitaSingolare, nazionalitaPlurale -> this.getWrapNazionalita(bio);
            case attivitaSingolare, attivitaPlurale -> this.getWrapAttivita(bio);
            case listaBreve -> null;
            case listaEstesa -> null;
            default -> null;
        };
    }

    public String giornoNato(final Bio bio) {
        return wikiUtility.linkGiornoNatoTesta(bio);
    }

    public String giornoNatoSimbolo(final Bio bio) {
        return wikiUtility.linkGiornoNatoCoda(bio, false);
    }

    public String giornoNatoSimboloParentesi(final Bio bio) {
        return wikiUtility.linkGiornoNatoCoda(bio, true);
    }


    public String giornoMorto(final Bio bio) {
        return wikiUtility.linkGiornoMortoTesta(bio);
    }

    public String giornoMortoSimbolo(final Bio bio) {
        return wikiUtility.linkGiornoMortoCoda(bio, false);
    }

    public String giornoMortoSimboloParentesi(final Bio bio) {
        return wikiUtility.linkGiornoMortoCoda(bio, true);
    }


    public String annoNato(final Bio bio) {
        return wikiUtility.linkAnnoNatoTesta(bio);
    }

    public String annoNatoSimbolo(final Bio bio) {
        return wikiUtility.linkAnnoNatoCoda(bio, false);
    }

    public String annoNatoSimboloParentesi(final Bio bio) {
        return wikiUtility.linkAnnoNatoCoda(bio, true);
    }


    public String annoMorto(final Bio bio) {
        return wikiUtility.linkAnnoMortoTesta(bio);
    }

    public String annoMortoSimbolo(final Bio bio) {
        return wikiUtility.linkAnnoMortoCoda(bio, false);
    }

    public String annoMortoSimboloParentesi(final Bio bio) {
        return wikiUtility.linkAnnoMortoCoda(bio, true);
    }

    public String luogoNato(final Bio bio) {
        String luogoNato = textService.isValid(bio.luogoNato) ? bio.luogoNato : VUOTA;
        String luogoNatoLink = textService.isValid(bio.luogoNatoLink) ? bio.luogoNatoLink : VUOTA;
        if (textService.isValid(luogoNato) && textService.isValid(luogoNatoLink)) {
            luogoNato = luogoNatoLink + PIPE + luogoNato;
        }

        return textService.isValid(luogoNato) ? textService.setDoppieQuadre(luogoNato) : VUOTA;
    }

    public String luogoNatoAnno(final Bio bio) {
        String luogoNato = luogoNato(bio);
        String annoNato = annoNatoSimbolo(bio);

        if (textService.isValid(luogoNato) && textService.isValid(annoNato)) {
            return luogoNato + VIRGOLA_SPAZIO + annoNato;
        }
        else {
            if (textService.isValid(luogoNato)) {
                return luogoNato;
            }
            if (textService.isValid(annoNato)) {
                return annoNato;
            }
        }

        return VUOTA;
    }


    public String luogoMorto(final Bio bio) {
        String luogoMorto = textService.isValid(bio.luogoMorto) ? bio.luogoMorto : VUOTA;
        String luogoMortoLink = textService.isValid(bio.luogoMortoLink) ? bio.luogoMortoLink : VUOTA;
        if (textService.isValid(luogoMorto) && textService.isValid(luogoMortoLink)) {
            luogoMorto = luogoMortoLink + PIPE + luogoMorto;
        }

        return textService.isValid(luogoMorto) ? textService.setDoppieQuadre(luogoMorto) : VUOTA;
    }

    public String luogoMortoAnno(final Bio bio) {
        String luogoMorto = luogoMorto(bio);
        String annoMorto = annoMortoSimbolo(bio);

        if (textService.isValid(luogoMorto) && textService.isValid(annoMorto)) {
            return luogoMorto + VIRGOLA_SPAZIO + annoMorto;
        }
        else {
            if (textService.isValid(luogoMorto)) {
                return luogoMorto;
            }
            if (textService.isValid(annoMorto)) {
                return annoMorto;
            }
        }

        return VUOTA;
    }

    public String luogoNatoMorto(final Bio bio) {
        String luogoNatoAnno = luogoNatoAnno(bio);
        String luogoMortoAnno = luogoMortoAnno(bio);

        if (textService.isValid(luogoNatoAnno) && textService.isValid(luogoMortoAnno)) {
            return textService.setTonde(luogoNatoAnno + SEP + luogoMortoAnno);
        }
        else {
            if (textService.isValid(luogoNatoAnno)) {
                return textService.setTonde(luogoNatoAnno);
            }
            if (textService.isValid(luogoMortoAnno)) {
                return textService.setTonde(luogoMortoAnno);
            }
        }

        return VUOTA;
    }

    /**
     * Costruisce la didascalia completa per una lista: <br>
     * attività, nazionalità, persone di nome, persone di cognome <br>
     *
     * @param bio completa
     *
     * @return didascalia completa
     */
    public String lista(final Bio bio) {
        return getNomeCognome(bio) + VIRGOLA_SPAZIO + getAttivitaNazionalita(bio) + SPAZIO + luogoNatoMorto(bio);
    }

}