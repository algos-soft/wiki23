package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
        this.typeUpload = AETypeUpload.pagina;
        this.usaParagrafi = true;
    }// end of constructor

    protected String fixToc() {
        return AETypeToc.noToc.get();
    }

    public UploadAttivitaNazionalita pagina() {
        this.typeUpload = AETypeUpload.pagina;
        return this;
    }

    public UploadAttivitaNazionalita sottoPagina() {
        this.typeUpload = AETypeUpload.sottoPagina;
        return this;
    }

    public UploadAttivitaNazionalita sottoSottoPagina() {
        this.typeUpload = AETypeUpload.sottoSottoPagina;
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
        this.nomeLista = nomeAttivitaNazionalita;

        if (textService.isValid(nomeAttivitaNazionalita)) {
            wikiTitle = switch (typeCrono) {
                case attivitaSingolare, attivitaPlurale -> wikiUtility.wikiTitleAttivita(nomeAttivitaNazionalita);
                case nazionalitaSingolare, nazionalitaPlurale -> wikiUtility.wikiTitleNazionalita(nomeAttivitaNazionalita);
                default -> VUOTA;
            };

            mappaWrap = switch (typeCrono) {
                //                case attivitaSingolare -> appContext.getBean(ListaAttivita.class).nascita(nomeLista).mappaWrap();
                //                case attivitaPlurale -> appContext.getBean(ListaAttivita.class).morte(nomeLista).mappaWrap();
                case nazionalitaSingolare -> appContext.getBean(ListaNazionalita.class).singolare(nomeAttivitaNazionalita).mappaWrap();
                case nazionalitaPlurale -> appContext.getBean(ListaNazionalita.class).plurale(nomeAttivitaNazionalita).mappaWrap();
                default -> null;
            };

            if (uploadTest) {
                this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
            }

            if (textService.isValid(wikiTitle) && mappaWrap != null) {
                this.esegueUpload(wikiTitle, mappaWrap);
            }
        }

        return WResult.crea();
    }

    /**
     * Esegue la scrittura della sottopagina <br>
     */
    public WResult uploadSottoPagina(final String wikiTitle, String nazionalita, String attivita, List<WrapLista> lista) {
        this.typeUpload = AETypeUpload.sottoPagina;
        this.wikiTitle = wikiTitle;

        if (uploadTest) {
            this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
        }

        if (textService.isValid(this.wikiTitle) && lista != null) {
            this.esegueUploadSotto(this.wikiTitle, nazionalita, attivita, lista);
        }

        return WResult.crea();
    }

    protected WResult esegueUpload(String wikiTitle, LinkedHashMap<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSizeAllWrap(mappa);

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
        buffer.append(testoBody(mappa));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
    }


    public String testoBody(Map<String, List<WrapLista>> mappa) {

        return switch (typeUpload) {
            case pagina -> testoPagina(mappa);
            case sottoPagina -> VUOTA;
            case sottoSottoPagina -> testoSottoSottoPagina(mappa);
        };
    }


    public String testoPagina(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        int numVoci;
        int max = WPref.sogliaSottoPagina.getInt();
        int maxDiv = WPref.sogliaDiv.getInt();
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
                if (uploadTest) {
                    appContext.getBean(UploadNazionalita.class).test().uploadSottoPagina(parente, nomeLista,keyParagrafo,lista);
                }
                else {
                    appContext.getBean(UploadNazionalita.class).uploadSottoPagina(parente, nomeLista,keyParagrafo,lista);
                }
            }
            else {
                usaDiv = lista.size() > maxDiv;
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

    public String testoSottoPagina(List<WrapLista> listaWrap) {
        StringBuffer buffer = new StringBuffer();
        String paragrafo;
        List<WrapLista> lista;
        mappaWrap = new LinkedHashMap<>();

        for (WrapLista wrap : listaWrap) {
            paragrafo = wrap.titoloSottoParagrafo;

            if (mappaWrap.containsKey(paragrafo)) {
                lista = mappaWrap.get(paragrafo);
            }
            else {
                lista = new ArrayList();
            }
            lista.add(wrap);
            mappaWrap.put(paragrafo, lista);
        }

        for (String key : mappaWrap.keySet()) {
            buffer.append(wikiUtility.fixTitolo(key, mappaWrap.get(key).size()));
            buffer.append("{{Div col}}" + CAPO);
            for (WrapLista wrap : mappaWrap.get(key)) {
                buffer.append(ASTERISCO);
                buffer.append(wrap.didascaliaBreve);
                buffer.append(CAPO);
            }
            buffer.append("{{Div col end}}" + CAPO);
        }

        return buffer.toString().trim();
    }

    public String testoSottoSottoPagina(Map<String, List<WrapLista>> mappa) {
        return VUOTA;
    }


    protected WResult esegueUploadSotto(String wikiTitle, String nazionalita, String attivita, List<WrapLista> lista) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = lista.size();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna(wikiTitle));
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipitSottoPagina(nazionalita, attivita));
        buffer.append(CAPO);
        buffer.append(testoSottoPagina(lista));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString().trim());
    }

    protected String incipitSottoPagina(String nazionalita, String attivita) {
        return VUOTA;
    }

}
