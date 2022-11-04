package it.algos.wiki23.backend.upload;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.mese.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.anno.*;
import it.algos.wiki23.backend.packages.giorno.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;

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

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MeseBackend meseBackend;

    protected int ordineGiornoAnno;

    private GiornoWiki giorno;

    private AnnoWiki anno;

    protected boolean usaSottoGiorniAnni;

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
        int numVoci = 0;
        int numBio = 0;
        int sogliaIncludeAll = WPref.sogliaIncludeAll.getInt();
        int sogliaMaxPagina = WPref.maxBioPageAnniGiorni.getInt();

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

            //--controllo delle dimensioni in byte della pagina wiki
            usaSottoGiorniAnni = false;
            if (WPref.usaSottoGiorniAnni.is()) {
                numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
                if (numVoci > sogliaIncludeAll) {
                    numBio = wikiUtility.getSizeAllWrap(mappaWrap);
                    if (numBio > sogliaMaxPagina) {
                        usaSottoGiorniAnni = true;
                    }
                }
            }

            if (uploadTest) {
                this.wikiTitle = UPLOAD_TITLE_DEBUG + wikiTitle;
            }

            if (textService.isValid(wikiTitle) && mappaWrap != null && mappaWrap.size() > 0) {
                return this.esegueUpload(wikiTitle, mappaWrap);
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
        buffer.append(fixUnconnected());
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


    protected WResult esegueUploadSotto(String wikiTitle, String attNazPrincipale, String attNazSottoPagina, int ordineSottoPagina, List<WrapLista> lista) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = lista.size();

        if (numVoci < 1) {
            logger.info(new WrapLog().message(String.format("Non creata la pagina %s perché non ci sono voci", wikiTitle, numVoci)));
            return WResult.crea();
        }

        switch (typeCrono) {
            case giornoNascita, giornoMorte -> {
                giorno = giornoWikiBackend.findByNome(nomeLista);
                this.ordineGiornoAnno = giorno != null ? giorno.getOrdine() : 0;
            }
            case annoNascita, annoMorte -> {
                anno = annoWikiBackend.findByNome(nomeLista);
                this.ordineGiornoAnno = anno != null ? anno.getOrdine() + ordineSottoPagina : 0;
            }
            default -> {}
        }

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(fixToc());
        buffer.append(fixUnconnected());
        buffer.append(tornaSotto());
        buffer.append(tmpListaBio(numVoci));
        buffer.append(CAPO);
        buffer.append(tmpListaPersoneIni(numVoci));
        buffer.append(senzaParagrafiNonRaggruppate(lista));
        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        buffer.append(portale());
        buffer.append(categorieSotto());

        return registra(wikiTitle, buffer.toString().trim());
    }


    protected String torna() {
        return textService.isValid(nomeLista) ? String.format("{{Torna a|%s}}", nomeLista) : VUOTA;
    }

    protected String tornaSotto() {
        String torna = wikiUtility.wikiTitle(typeCrono, nomeLista) ;
        return textService.isValid(torna) ? String.format("{{Torna a|%s}}", torna) : VUOTA;
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


    /**
     * I giorni superano sempre le 200 voci (salvo il 29 febbraio) <br>
     * Gli anni:
     * 1) fino a 50 voci SENZA paragrafi
     * 2) da 50 a 200 voci con paragrafi ridotti
     * 3) sopra le 200 voci con paragrafi normali
     */
    public String testoBody(Map<String, List<WrapLista>> mappa) {
        int numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
        int sogliaIncludeAll = WPref.sogliaIncludeAll.getInt();
        int sogliaIncludeParagrafo = WPref.sogliaIncludeParagrafo.getInt();
        boolean righeRaggruppate;

        righeRaggruppate = switch (typeCrono) {
            case giornoNascita, giornoMorte -> WPref.usaRigheGiorni.is();
            case annoNascita, annoMorte -> WPref.usaRigheAnni.is();
            default -> false;
        };

        //--meno di 50 (secondo il flag)
        if (numVoci < sogliaIncludeParagrafo) {
            if (righeRaggruppate) {
                return senzaParagrafiMaRaggruppate(mappa);
            }
            else {
                return senzaParagrafiNonRaggruppate(mappa);
            }
        }

        //--da 50 a 200 (secondo il flag)
        if (numVoci < sogliaIncludeAll) {
            return conParagrafiSenzaSottopagine(mappa, true);
        }
        else {
            if (usaSottoGiorniAnni) {
                return conParagrafiConSottopagine(mappa);
            }
            else {
                return conParagrafiSenzaSottopagine(mappa, false);
            }
        }
    }


    public String senzaParagrafiNonRaggruppate(Map<String, List<WrapLista>> mappa) {
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


    public String senzaParagrafiNonRaggruppate(List<WrapLista> lista) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("{{Div col}}" + CAPO);
        for (WrapLista wrap : lista) {
            buffer.append(ASTERISCO);

            if (textService.isValid(wrap.titoloSottoParagrafo)) {
                buffer.append(wrap.titoloSottoParagrafo);
                buffer.append(SEP);
            }
            buffer.append(wrap.didascaliaBreve);
            buffer.append(CAPO);
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
            }
        }
        buffer.append("{{Div col end}}" + CAPO);

        return buffer.toString().trim();
    }


    public String conParagrafiConSottopagine(Map<String, List<WrapLista>> mappa) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        int numVoci;
        int maxDiv = WPref.sogliaDiv.getInt();
        int sogliaSottoPaginaGiorniAnni = WPref.sogliaSottoPaginaGiorniAnni.getInt();
        boolean usaDiv = true;
        String titoloParagrafoLink;
        String vedi;
        String sottoPagina;
        int ordineSottoPagina = 0;

        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            numVoci = lista.size();
            titoloParagrafoLink = lista.get(0).titoloParagrafoLink;
            buffer.append(fixTitolo(titoloParagrafoLink, numVoci, false));

            if (numVoci > sogliaSottoPaginaGiorniAnni) {
                ordineSottoPagina = fixOrdineSottoPagina(keyParagrafo);
                sottoPagina = String.format("%s%s%s", wikiTitle, SLASH, keyParagrafo);

                vedi = String.format("{{Vedi anche|%s}}", sottoPagina);
                buffer.append(vedi + CAPO);
                uploadSottoPagine(sottoPagina, nomeLista, keyParagrafo, ordineSottoPagina, lista);
            }
            else {
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
        }

        return buffer.toString().trim();
    }


    public String conParagrafiSenzaSottopagine(Map<String, List<WrapLista>> mappa, boolean includeOnly) {
        StringBuffer buffer = new StringBuffer();
        List<WrapLista> lista;
        int numVoci;
        int maxDiv = WPref.sogliaDiv.getInt();
        boolean usaDiv;
        String titoloParagrafoLink;

        for (String keyParagrafo : mappa.keySet()) {
            lista = mappa.get(keyParagrafo);
            numVoci = lista.size();
            usaDiv = lista.size() > maxDiv;
            titoloParagrafoLink = lista.get(0).titoloParagrafoLink;
            buffer.append(fixTitolo(titoloParagrafoLink, numVoci, includeOnly));
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

    protected String fixTitolo(String titoloParagrafoLink, int numVoci, boolean includeOnly) {
        String titolo = VUOTA;
        String tag = "<includeonly>=</includeonly>";
        String tagIniOld = "<span style";
        String tagIniNew = "<noinclude>" + tagIniOld;
        String tagEndOld = "</span>";
        String tagEndNew = tagEndOld + "</noinclude>";

        titolo = wikiUtility.fixTitolo(VUOTA, titoloParagrafoLink, numVoci);

        if (includeOnly) {
            titolo = textService.sostituisce(titolo, tagIniOld, tagIniNew);
            titolo = textService.sostituisce(titolo, tagEndOld, tagEndNew);
            titolo = titolo.trim();
            titolo = CAPO + tag + titolo + tag + CAPO;
        }

        return titolo;
    }

    protected int fixOrdineSottoPagina(String titoloParagrafoLink) {
        int ordine = meseBackend.getOrdine(titoloParagrafoLink);

        if (ordine == 0 && titoloParagrafoLink.equals(TAG_LISTA_NO_GIORNO)) {
            ordine = 13;
        }

        return ordine;
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


    /**
     * Esegue la scrittura della sottopagina <br>
     */
    public WResult uploadSottoPagina(final String wikiTitle, String parente, String sottoPagina, int ordineSottoPagina, List<WrapLista> lista) {
        this.wikiTitle = wikiTitle;
        this.nomeLista = parente;
        this.nomeSottoPagina = sottoPagina;

        if (textService.isValid(this.wikiTitle) && lista != null) {
            this.esegueUploadSotto(this.wikiTitle, parente, nomeSottoPagina, ordineSottoPagina, lista);
        }

        return WResult.crea();
    }

    protected String categorie() {
        StringBuffer buffer = new StringBuffer();

        if (uploadTest) {
            return VUOTA;
        }

        //        String title = switch (typeCrono) {
        //            case giornoNascita -> wikiUtility.wikiTitleNatiGiorno(nomeLista);
        //            case giornoMorte -> wikiUtility.wikiTitleMortiGiorno(nomeLista);
        //            case annoNascita -> wikiUtility.wikiTitleNatiAnno(nomeLista);
        //            case annoMorte -> wikiUtility.wikiTitleMortiAnno(nomeLista);
        //            default -> VUOTA;
        //        };

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:%s| ]]", wikiUtility.wikiTitle(typeCrono, nomeLista)));
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String categorieSotto() {
        StringBuffer buffer = new StringBuffer();

        if (uploadTest) {
            return VUOTA;
        }

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
        buffer.append(CAPO);

        return buffer.toString();
    }


}
