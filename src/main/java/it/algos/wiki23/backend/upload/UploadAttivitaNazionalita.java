package it.algos.wiki23.backend.upload;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.wrapper.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 05-Aug-2022
 * Time: 12:01
 * Superclasse astratta per le classi UploadAttivita e UploadNazionalita <br>
 */
public abstract class UploadAttivitaNazionalita extends Upload {



    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadAttivitaNazionalita() {
        super.usaParagrafi = WPref.usaParagrafiAttNaz.is();
        super.typeToc = (AETypeToc) WPref.typeTocAttNaz.getEnumCurrentObj();
    }// end of constructor


    public UploadAttivitaNazionalita singolare() {
        return this;
    }

    public UploadAttivitaNazionalita plurale() {
        return this;
    }

    public UploadAttivitaNazionalita noToc() {
        this.typeToc = AETypeToc.noToc;
        return this;
    }

    public UploadAttivitaNazionalita forceToc() {
        this.typeToc = AETypeToc.forceToc;
        return this;
    }

    public UploadAttivitaNazionalita test() {
        this.uploadTest = true;
        return this;
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload(final String nomeAttivitaNazionalita) {
        this.nomeLista = textService.primaMinuscola(nomeAttivitaNazionalita);

        if (textService.isValid(nomeLista)) {
            wikiTitle = switch (typeCrono) {
                case attivitaSingolare, attivitaPlurale -> wikiUtility.wikiTitleAttivita(nomeLista);
                case nazionalitaSingolare, nazionalitaPlurale -> wikiUtility.wikiTitleNazionalita(nomeLista);
                default -> VUOTA;
            };

            mappaWrap = switch (typeCrono) {
                case attivitaSingolare -> appContext.getBean(ListaAttivita.class).singolare(nomeLista).mappaWrap();
                case attivitaPlurale -> appContext.getBean(ListaAttivita.class).plurale(nomeLista).mappaWrap();
                case nazionalitaSingolare -> appContext.getBean(ListaNazionalita.class).singolare(nomeLista).mappaWrap();
                case nazionalitaPlurale -> appContext.getBean(ListaNazionalita.class).plurale(nomeLista).mappaWrap();
                default -> null;
            };

            if (uploadTest) {
                this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
            }

            if (textService.isValid(wikiTitle) && mappaWrap != null && mappaWrap.size() > 0) {
                this.esegueUpload(wikiTitle, mappaWrap);
            }
        }

        return WResult.crea();
    }

    /**
     * Esegue la scrittura della sottopagina <br>
     */
    public WResult uploadSottoPagina(final String wikiTitle, String attNazPrincipale, String attNazSottoPagina, List<WrapLista> lista) {
        this.wikiTitle = wikiTitle;
        this.nomeLista = attNazPrincipale;
        this.nomeSottoPagina = attNazSottoPagina;

        if (uploadTest) {
            this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
        }

        if (textService.isValid(this.wikiTitle) && lista != null) {
            this.esegueUploadSotto(this.wikiTitle, attNazPrincipale, attNazSottoPagina, lista);
        }

        return WResult.crea();
    }


    /**
     * Esegue la scrittura della sottosottopagina <br>
     */
    public WResult uploadSottoSottoPagina(final String wikiTitle, String attNazPrincipale, String attNazSottoPagina, String keyParagrafo, List<WrapLista> lista) {
        this.wikiTitle = wikiTitle;
        this.nomeLista = attNazPrincipale;
        this.nomeSottoPagina = attNazSottoPagina + SLASH + keyParagrafo;

        if (uploadTest) {
            this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
        }

        if (textService.isValid(this.wikiTitle) && lista != null) {
            this.esegueUploadSottoSotto(this.wikiTitle, attNazPrincipale, attNazSottoPagina, keyParagrafo, lista);
        }

        return WResult.crea();
    }

    protected WResult esegueUpload(String wikiTitle, LinkedHashMap<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSizeAllWrap(mappa);

        if (numVoci < WPref.sogliaAttNazWiki.getInt()) {
            logger.info(new WrapLog().message(String.format("Non creata la pagina %s perché ha solo %d voci", wikiTitle, numVoci)));
            return WResult.crea();
        }

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipit());
        buffer.append(CAPO);
        buffer.append(testoPagina(mappa));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
    }

    protected WResult esegueUploadSotto(String wikiTitle, String attNazPrincipale, String attNazSottoPagina, List<WrapLista> lista) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = lista.size();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(AETypeToc.noToc.get());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipitSottoPagina(attNazPrincipale, attNazSottoPagina, numVoci));
        buffer.append(CAPO);
        buffer.append(testoSottoPagina(attNazPrincipale, attNazSottoPagina, lista));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
    }


    protected WResult esegueUploadSottoSotto(String wikiTitle, String attNazPrincipale, String attNazSottoPagina, String keyParagrafo, List<WrapLista> lista) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = lista.size();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(AETypeToc.noToc.get());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipitSottoSottoPagina(attNazPrincipale, attNazSottoPagina, keyParagrafo, numVoci));
        buffer.append(CAPO);
        buffer.append(testoSottoSottoPagina(lista));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
    }


    public String testoPagina(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        int numVoci;
        int max = WPref.sogliaSottoPagina.getInt();
        int maxDiv = WPref.sogliaDiv.getInt();
        boolean usaDivBase = WPref.usaDivAttNaz.is();
        boolean usaDiv;
        String titoloParagrafoLink;
        String vedi;
        String parente;

        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            numVoci = lista.size();
            titoloParagrafoLink = lista.get(0).titoloParagrafoLink;
            buffer.append(wikiUtility.fixTitolo(VUOTA, titoloParagrafoLink, numVoci));

            if (numVoci > max) {
                parente = String.format("%s%s%s%s%s", titoloLinkVediAnche, SLASH, textService.primaMaiuscola(nomeLista), SLASH, keyParagrafo);
                vedi = String.format("{{Vedi anche|%s}}", parente);
                buffer.append(vedi + CAPO);
                uploadSottoPagine(parente, nomeLista, keyParagrafo, lista);
            }
            else {
                usaDiv = usaDivBase ? lista.size() > maxDiv : false;
                buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
                for (WrapLista wrap : lista) {
                    buffer.append(ASTERISCO);
                    buffer.append(wrap.didascaliaBreve);
                    buffer.append(CAPO);
                }
                buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);
            }
        }

        return buffer.toString().trim();
    }


    public String testoSottoPagina(String attNazPrincipale, String attNazSottoPagina, List<WrapLista> listaWrap) {
        StringBuffer buffer = new StringBuffer();
        attNazPrincipale = textService.primaMaiuscola(attNazPrincipale);
        attNazSottoPagina = textService.primaMaiuscola(attNazSottoPagina);
        String paragrafo;
        List<WrapLista> listaTmp;
        List<WrapLista> listaSotto;
        int numVoci;
        String vedi;
        String parente;
        int max = WPref.sogliaSottoPagina.getInt();
        LinkedHashMap<String, List<WrapLista>> mappaWrapSotto = new LinkedHashMap<>();
        int maxDiv = WPref.sogliaDiv.getInt();
        boolean usaDivBase = WPref.usaDivAttNaz.is();
        boolean usaDiv;

        for (WrapLista wrap : listaWrap) {
            paragrafo = wrap.titoloSottoParagrafo;

            if (mappaWrapSotto.containsKey(paragrafo)) {
                listaTmp = mappaWrapSotto.get(paragrafo);
            }
            else {
                listaTmp = new ArrayList();
            }
            listaTmp.add(wrap);
            mappaWrapSotto.put(paragrafo, listaTmp);
        }

        for (String keyParagrafoSotto : mappaWrapSotto.keySet()) {
            listaSotto = mappaWrapSotto.get(keyParagrafoSotto);
            numVoci = listaSotto.size();
            buffer.append(wikiUtility.fixTitolo(keyParagrafoSotto, mappaWrapSotto.get(keyParagrafoSotto).size()));

            if (numVoci > max && WPref.usaSottoSottoAttNaz.is()) {
                parente = String.format("%s%s%s%s%s%s%s", titoloLinkVediAnche, SLASH, attNazPrincipale, SLASH, attNazSottoPagina, SLASH, keyParagrafoSotto);
                vedi = String.format("{{Vedi anche|%s}}", parente);
                buffer.append(vedi + CAPO);
                uploadSottoSottoPagine(parente, attNazPrincipale, attNazSottoPagina, keyParagrafoSotto, listaSotto);
            }
            else {
                usaDiv = usaDivBase;
                buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
                for (WrapLista wrap : mappaWrapSotto.get(keyParagrafoSotto)) {
                    buffer.append(ASTERISCO);
                    buffer.append(wrap.didascaliaBreve);
                    buffer.append(CAPO);
                }
                buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);
            }
        }

        return buffer.toString().trim();
    }

    public void uploadSottoSottoPagine(String wikiTitle, String attNazPrincipale, String attNazSottoPagina, String keyParagrafo, List<WrapLista> lista) {
    }

    public String testoSottoSottoPagina(List<WrapLista> listaWrap) {
        StringBuffer buffer = new StringBuffer();
        boolean usaDivBase = WPref.usaDivAttNaz.is();
        boolean usaDiv;

        usaDiv = usaDivBase;
        buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
        for (WrapLista wrap : listaWrap) {
            buffer.append(ASTERISCO);
            buffer.append(wrap.didascaliaBreve);
            buffer.append(CAPO);
        }
        buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);

        return buffer.toString().trim();
    }

    protected String incipitSottoPagina(String attNazPrincipale, String attNazSottoPagina, int numVoci) {
        return VUOTA;
    }

    protected String incipitSottoSottoPagina(String attNazPrincipale, String attNazSottoPagina, String keyParagrafo, int numVoci) {
        return VUOTA;
    }

    /**
     * Esegue la scrittura di tutte le pagine di nazionalità <br>
     */
    public WResult uploadAll() {
        return null;
    }

}
