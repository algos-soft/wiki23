package it.algos.wiki23.backend.upload;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.anno.*;
import it.algos.wiki23.backend.packages.giorno.*;
import it.algos.wiki23.backend.wrapper.*;

import java.time.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sat, 30-Jul-2022
 * Time: 07:39
 * <p>
 * Superclasse astratta per le classi UploadGiorni e UploadAnni <br>
 */
public abstract class UploadGiorniAnni extends Upload {


    protected static final int MAX_NUM_VOCI = 200;

    protected int ordineGiornoAnno;

    private GiornoWiki giorno;

    private AnnoWiki anno;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadGiorniAnni() {
        super.typeToc = AETypeToc.noToc;
        this.uploadTest = false;
        super.usaParagrafi = true;
    }// end of constructor


    public UploadGiorniAnni nascita() {
        return this;
    }

    public UploadGiorniAnni morte() {
        return this;
    }

    public UploadGiorniAnni conParagrafi() {
        this.usaParagrafi = true;
        return this;
    }

    public UploadGiorniAnni senzaParagrafi() {
        this.usaParagrafi = false;
        return this;
    }

    public UploadGiorniAnni test() {
        this.uploadTest = true;
        return this;
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload(final String nomeGiornoAnno) {
        this.nomeLista = nomeGiornoAnno;

        if (textService.isValid(nomeGiornoAnno)) {
            wikiTitle = switch (typeCrono) {
                case giornoNascita -> wikiUtility.wikiTitleNatiGiorno(nomeGiornoAnno);
                case giornoMorte -> wikiUtility.wikiTitleMortiGiorno(nomeGiornoAnno);
                case annoNascita -> wikiUtility.wikiTitleNatiAnno(nomeGiornoAnno);
                case annoMorte -> wikiUtility.wikiTitleMortiAnno(nomeGiornoAnno);
                default -> VUOTA;
            };

            switch (typeCrono) {
                case giornoNascita, giornoMorte -> {
                    giorno = giornoWikiBackend.findByNome(nomeLista);
                    this.ordineGiornoAnno = giorno != null ? giorno.getOrdine() : 0;
                }
                case annoNascita, annoMorte -> {
                    anno = annoWikiBackend.findByNome(nomeLista);
                    this.ordineGiornoAnno = anno != null ? anno.getOrdine() : 0;
                }
                default -> {}
            }
            mappaWrap = switch (typeCrono) {
                case giornoNascita -> appContext.getBean(ListaGiorni.class).nascita(nomeLista).mappaWrap();
                case giornoMorte -> appContext.getBean(ListaGiorni.class).morte(nomeLista).mappaWrap();
                case annoNascita -> appContext.getBean(ListaAnni.class).nascita(nomeLista).mappaWrap();
                case annoMorte -> appContext.getBean(ListaAnni.class).morte(nomeLista).mappaWrap();
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


    protected WResult esegueUpload(String wikiTitle, LinkedHashMap<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSizeAllWrap(mappa);

        if (numVoci < 1) {
            logger.info(new WrapLog().message(String.format("Non creata la pagina %s perché non ci sono voci", wikiTitle, numVoci)));
            return WResult.crea();
        }

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna());
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(tmpListaPersoneIni(numVoci));
        buffer.append(testoBody(mappa));
        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        buffer.append(includeIni());
        buffer.append(portale());
        buffer.append(categorie());
        buffer.append(includeEnd());

        return registra(wikiTitle, buffer.toString().trim());
    }

    protected String torna() {
        return textService.isValid(nomeLista) ? String.format("{{Torna a|%s}}", nomeLista) : VUOTA;
    }

    protected String tmpListaPersoneIni(int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(String.format("{{Lista persone per %s", typeCrono.getGiornoAnno()));
        buffer.append(CAPO);
        buffer.append("|titolo=");
        buffer.append(wikiTitle);
        buffer.append(CAPO);
        buffer.append("|voci=");
        buffer.append(numVoci);
        buffer.append(CAPO);
        buffer.append("|testo=");
        buffer.append("<nowiki></nowiki>");
        buffer.append(CAPO);

        return uploadTest ? VUOTA : buffer.toString();
    }


    public String testoBody(Map<String, List<WrapLista>> mappa) {
        String testo;
        int numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
        boolean righeRaggruppate;

        if (usaParagrafi && numVoci > MAX_NUM_VOCI) {
            testo = conParagrafi(mappa);
        }
        else {
            righeRaggruppate = switch (typeCrono) {
                case giornoNascita, giornoMorte -> WPref.usaRigheGiorni.is();
                case annoNascita, annoMorte -> WPref.usaRigheAnni.is();
                default -> false;
            };

            if (righeRaggruppate) {
                testo = senzaParagrafiMaRaggruppate(mappa);
            }
            else {
                testo = senzaParagrafi(mappa);
            }
        }

        return testo;
    }


    public String conParagrafi(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        int numVoci;
        boolean usaDiv;
        String titoloParagrafoLink;

        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            numVoci = lista.size();
            usaDiv = lista.size() > 3;
            titoloParagrafoLink = lista.get(0).titoloParagrafoLink;
            buffer.append(wikiUtility.fixTitolo(VUOTA, titoloParagrafoLink, numVoci));
            buffer.append(usaDiv ? "{{Div col}}" + CAPO : VUOTA);
            for (WrapLista wrap : lista) {
                buffer.append(ASTERISCO);
                if (textService.isValid(wrap.titoloSottoParagrafo)) {
                    buffer.append(wrap.titoloSottoParagrafo);
                    buffer.append(SEP);
                }
                buffer.append(wrap.didascaliaBreve);
                buffer.append(CAPO);
            }
            buffer.append(usaDiv ? "{{Div col end}}" + CAPO : VUOTA);
        }

        return buffer.toString().trim();
    }

    public String senzaParagrafi(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;

        buffer.append("{{Div col}}" + CAPO);
        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            for (WrapLista wrap : lista) {
                buffer.append(ASTERISCO);

                if (textService.isValid(wrap.titoloSottoParagrafo)) {
                    buffer.append(wrap.titoloSottoParagrafo);
                    buffer.append(SEP);
                }
                buffer.append(wrap.didascaliaBreve);
                buffer.append(CAPO);
            }
        }
        buffer.append("{{Div col end}}" + CAPO);

        return buffer.toString().trim();
    }

    public String senzaParagrafiMaRaggruppate(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        List<WrapLista> listaSub;
        LinkedHashMap<String, List<WrapLista>> mappaWrapSub;
        int numRighe;

        buffer.append("{{Div col}}" + CAPO);
        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            mappaWrapSub = creaSubMappa(lista);

            for (String inizioRiga : mappaWrapSub.keySet()) {
                listaSub = mappaWrapSub.get(inizioRiga);

                if (listaSub.size() == 1) {
                    buffer.append(ASTERISCO);
                    buffer.append(listaSub.get(0).didascaliaLunga);
                    buffer.append(CAPO);
                }
                else {
                    if (textService.isValid(inizioRiga)) {
                        buffer.append(ASTERISCO);
                        buffer.append(inizioRiga);
                        buffer.append(CAPO);
                        for (WrapLista wrapSub : listaSub) {
                            buffer.append(ASTERISCO);
                            buffer.append(ASTERISCO);
                            buffer.append(wrapSub.didascaliaBreve);
                            buffer.append(CAPO);
                        }
                    }
                    else {
                        for (WrapLista wrapSub : listaSub) {
                            buffer.append(ASTERISCO);
                            buffer.append(wrapSub.didascaliaBreve);
                            buffer.append(CAPO);
                        }
                    }
                }
                buffer.append(CAPO);
            }
        }
        buffer.append("{{Div col end}}" + CAPO);

        return buffer.toString().trim();
    }


    protected LinkedHashMap<String, List<WrapLista>> creaSubMappa(List<WrapLista> listaWrapSub) {
        LinkedHashMap<String, List<WrapLista>> mappaWrapSub = new LinkedHashMap<>();
        String inizioRiga;
        List<WrapLista> listaSub;

        if (listaWrapSub != null && listaWrapSub.size() > 0) {
            for (WrapLista wrap : listaWrapSub) {
                inizioRiga = wrap.titoloSottoParagrafo;

                if (mappaWrapSub.containsKey(inizioRiga)) {
                    listaSub = mappaWrapSub.get(inizioRiga);
                }
                else {
                    listaSub = new ArrayList();
                }
                listaSub.add(wrap);
                mappaWrapSub.put(inizioRiga, listaSub);
            }
        }

        return mappaWrapSub;
    }

    protected String categorie() {
        StringBuffer buffer = new StringBuffer();
        String tag = typeCrono.getTagUpper();

        String title = switch (typeCrono) {
            case giornoNascita -> wikiUtility.wikiTitleNatiGiorno(nomeLista);
            case giornoMorte -> wikiUtility.wikiTitleMortiGiorno(nomeLista);
            case annoNascita -> wikiUtility.wikiTitleNatiAnno(nomeLista);
            case annoMorte -> wikiUtility.wikiTitleMortiAnno(nomeLista);
            default -> VUOTA;
        };

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:%s| ]]", title));
        buffer.append(CAPO);

        return buffer.toString();
    }


}
