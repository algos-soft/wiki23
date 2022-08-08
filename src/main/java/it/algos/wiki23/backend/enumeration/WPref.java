package it.algos.wiki23.backend.enumeration;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.interfaces.*;
import it.algos.vaad23.backend.packages.utility.preferenza.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project Wiki
 * Created by Algos
 * User: gac
 * Date: lun, 25 apr 22
 * <p>
 * Creazione da code di alcune preferenze del progetto <br>
 */
public enum WPref implements AIGenPref {

    downloadGenere("downloadGenere", AETypePref.localdatetime, ROOT_DATA_TIME, "Download di Modulo:Bio/Plurale attività genere."),

    downloadAttivita("downloadAttivita", AETypePref.localdatetime, ROOT_DATA_TIME, "Download di Modulo:Bio/Plurale attività."),
    downloadAttivitaTime("downloadAttivitaTime", AETypePref.integer, 0, "Durata download delle attività in minuti."),
    elaboraAttivita("elaboraAttivita", AETypePref.localdatetime, ROOT_DATA_TIME, "Elaborazione di tutte le attività."),
    elaboraAttivitaTime("elaboraAttivitaTime", AETypePref.integer, 0, "Durata elaborazione delle attività in minuti."),
    uploadAttivita("uploadAttivita", AETypePref.localdatetime, ROOT_DATA_TIME, "Upload di tutte le attività."),
    uploadAttivitaTime("uploadAttivitaTime", AETypePref.integer, 0, "Durata upload delle attività in minuti."),
    uploadAttivitaPrevisto("uploadAttivitaPrevisto", AETypePref.localdatetime, ROOT_DATA_TIME, "Prossimo upload previsto per le attività."),


    downloadNazionalita("downloadNazionalita", AETypePref.localdatetime, ROOT_DATA_TIME, "Download di Modulo:Bio/Plurale nazionalità."),
    downloadNazionalitaTime("downloadNazionalitaTime", AETypePref.integer, 0, "Durata download delle nazionalità in minuti."),
    elaboraNazionalita("elaboraNazionalita", AETypePref.localdatetime, ROOT_DATA_TIME, "Elaborazione di tutte le nazionalità."),
    elaboraNazionalitaTime("elaboraNazionalitaTime", AETypePref.integer, 0, "Durata elaborazione delle nazionalità in minuti."),
    uploadNazionalita("uploadNazionalita", AETypePref.localdatetime, ROOT_DATA_TIME, "Upload di tutte le nazionalità."),
    uploadNazionalitaTime("uploadNazionalitaTime", AETypePref.integer, 0, "Durata upload delle nazionalità in minuti."),
    uploadNazionalitaPrevisto("uploadNazionalitaPrevisto", AETypePref.localdatetime, ROOT_DATA_TIME, "Prossimo upload previsto per le nazionalità."),

    downloadBio("downloadBio", AETypePref.localdatetime, ROOT_DATA_TIME, "Download delle voci biografiche."),
    downloadBioTime("downloadBioTime", AETypePref.integer, ROOT_DATA_TIME, "Durata ciclo completo download delle biografie in minuti."),
    downloadBioPrevisto("downloadBioPrevisto", AETypePref.localdatetime, ROOT_DATA_TIME, "Prossimo download previsto delle voci biografiche."),
    elaboraBio("elaboraBio", AETypePref.localdatetime, ROOT_DATA_TIME, "Elaborazione di tutte le biografie."),
    elaboraBioTime("elaboraBioTime", AETypePref.integer, ROOT_DATA_TIME, "Durata elaborazione delle biografie in minuti."),

    //    uploadAttivitaTime("uploadAttivitaTime", AETypePref.integer, "Durata upload delle attività.", 0),
    //    downloadNazionalita("downloadNazionalita", AETypePref.localdatetime, "Download di Modulo:Bio/Plurale nazionalità.", ROOT_DATA_TIME),
    //    downloadProfessione("downloadProfessione", AETypePref.localdatetime, "Download di Modulo:Bio/Link attività.", ROOT_DATA_TIME),
    //    downloadNomi("downloadNomi", AETypePref.localdatetime, "Download di Progetto:Antroponimi/Nomi doppi.", ROOT_DATA_TIME),

    elaboraAnni("elaboraAnni", AETypePref.localdatetime, ROOT_DATA_TIME, "Elaborazione di tutti gli anni."),
    elaboraAnniTime("elaboraAnniTime", AETypePref.integer, 0, "Durata elaborazione di tutti gli anni in minuti."),
    elaboraGiorni("elaboraGiorni", AETypePref.localdatetime, ROOT_DATA_TIME, "Elaborazione di tutti i giorni."),
    elaboraGiorniTime("elaboraGiorniTime", AETypePref.integer, 0, "Durata elaborazione di tutti i giorni in minuti."),

    statisticaAttivita("statisticaAttivita", AETypePref.localdatetime, ROOT_DATA_TIME, "Creazione della pagina di statistiche per le attività."),
    statisticaNazionalita("statisticaNazionalita", AETypePref.localdatetime, ROOT_DATA_TIME, "Creazione della pagina di statistiche per le nazionalità."),


    uploadGiorni("uploadGiorni", AETypePref.localdatetime, ROOT_DATA_TIME, "Upload di tutti i nati/morti nei giorni."),
    uploadGiorniTime("uploadGiorniTime", AETypePref.integer, 0, "Durata upload dei giorni in minuti."),
    uploadGiorniPrevisto("uploadGiorniPrevisto", AETypePref.localdatetime, ROOT_DATA_TIME, "Prossimo upload previsto per i giorni."),
    statisticaGiorni("statisticaGiorni", AETypePref.localdatetime, ROOT_DATA_TIME, "Creazione della pagina di statistiche per i giorni."),


    uploadAnni("uploadAnni", AETypePref.localdatetime, ROOT_DATA_TIME, "Upload di tutti i nati/morti negli anni."),
    uploadAnniTime("uploadAnniTime", AETypePref.integer, 0, "Durata upload degli anni in minuti."),
    uploadAnniPrevisto("uploadAnniPrevisto", AETypePref.localdatetime, ROOT_DATA_TIME, "Prossimo upload previsto per gli anni."),
    statisticaAnni("statisticaAnni", AETypePref.localdatetime, ROOT_DATA_TIME, "Creazione della pagina di statistiche per gli anni."),


    categoriaBio("categoriaBio", AETypePref.string, "BioBot", "Categoria di riferimento per le Biografie"),
    sogliaAttNazWiki("sogliaAttNazWiki", AETypePref.integer, 50, "Soglia minima per creare la pagina di una attività o nazionalità sul " +
            "server wiki"),
    sogliaSottoPagina("sogliaSottoPagina", AETypePref.integer, 50, "Soglia minima per creare una sottopagina di una attività o nazionalità sul server wiki"),
    sogliaDiv("sogliaDiv", AETypePref.integer, 1, "Soglia minima per usare {{Div col}}"),

    usaTreAttivita("usaTreAttivita", AETypePref.bool, false, "Considera tutte le attività (tre) nelle liste di attività"),
    usaParagrafiDimensionati("usaParagrafiDimensionati", AETypePref.bool, true, "Nel titolo del paragrafo aggiunge la dimensione delle " +
            "voci elencate"),
    usaLinkStatistiche("usaLinkStatistiche", AETypePref.bool, false, "Link alle liste di attività nel template statistiche, anche se " +
            "rossi"),
    typeTocAttNaz("typeTocAttNaz", AETypePref.enumerationType, AETypeToc.noToc, "Type di TOC in attività e nazionalità",
            AETypeToc.noToc
    ),
    typeChiaveNulla("typeChiaveNulla", AETypePref.enumerationType, AETypeChiaveNulla.inCoda, "Posizione del paragrafo 'nullo'",
            AETypeChiaveNulla.inCoda
    ),
    usaParagrafiGiorni("usaParagrafiGiorni", AETypePref.bool, true, "Usa i paragrafi (secoli) nelle pagine dei giorni"),
    usaParagrafiAnni("usaParagrafiAnni", AETypePref.bool, true, "Usa i paragrafi (mesi) nelle pagine degli anni"),
    usaParagrafiAttNaz("usaParagrafiAttNaz", AETypePref.bool, false, "Usa i paragrafi (nazionalità/attività) nelle pagine attività/nazionalità"),

    simboloNato("simboloNato", AETypePref.string, "n.", "Simbolo della nascita nelle didascalie"),
    simboloMorto("simboloMorto", AETypePref.string, "†", "Simbolo della morte nelle didascalie"),
    usaSimboliCrono("usaSimboliCrono", AETypePref.bool, true, "Uso dei simboli crono per nati e morti"),

    linkCrono("linkCrono", AETypePref.enumerationType, AETypeLinkCrono.voce, "Type di link a giorni/anni nelle didascalie",
            AETypeLinkCrono.nessuno
    ),
    ;

    //--codice di riferimento.
    private String keyCode;

    //--tipologia di dato da memorizzare.
    //--Serve per convertire (nei due sensi) il valore nel formato byte[] usato dal mongoDb
    private AETypePref type;

    private AITypePref typeEnum;

    //--descrizione breve ma comprensibile. Ulteriori (eventuali) informazioni nel campo 'note'
    private String descrizione;

    //--Valore java iniziale da convertire in byte[] a seconda del type
    private Object defaultValue;

    //--preferenze che necessita di un riavvio del programma per avere effetto
    private boolean needRiavvio;

    //--Link injettato da un metodo static
    private PreferenceService preferenceService;

    //--Link injected da un metodo static
    private LogService logger;

    //--Link injected da un metodo static
    private DateService date;

    //--Link injettato da un metodo static
    private TextService text;

    WPref(final String keyCode, final AETypePref type, final Object defaultValue, final String descrizione) {
        this(keyCode, type, defaultValue, descrizione, null);
    }// fine del costruttore

    WPref(final String keyCode, final AETypePref type, final Object defaultValue, final String descrizione, AITypePref typeEnum) {
        this.keyCode = keyCode;
        this.type = type;
        this.defaultValue = defaultValue;
        this.descrizione = descrizione;
        this.typeEnum = typeEnum;
    }// fine del costruttore


    public static List<WPref> getAllEnums() {
        return Arrays.stream(values()).toList();
    }
    //------------------------------------------------
    //--copiare tutti i metodi (Instance Method e non Static Method) nelle sottoclassi xPref
    //--cambiando in static PreferenzaServiceInjector.postConstruct() Pref.values() -> xPref.values()
    //------------------------------------------------

    @Override
    public void setPreferenceService(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Override
    public void setLogger(LogService logger) {
        this.logger = logger;
    }

    @Override
    public void setDate(DateService date) {
        this.date = date;
    }

    @Override
    public void setText(TextService text) {
        this.text = text;
    }

    @Override
    public void setValue(Object javaValue) {
        preferenceService.setValue(type, keyCode, javaValue);
    }


    @Override
    public Object get() {
        return getValue();
    }

    @Override
    public Object getValue() {
        return preferenceService.getValue(type, keyCode);
    }

    @Override
    public String getStr() {
        return preferenceService.getStr(type, keyCode);
    }

    @Override
    public boolean is() {
        return preferenceService.is(type, keyCode);
    }

    @Override
    public int getInt() {
        return preferenceService.getInt(type, keyCode);
    }

    @Override
    public AETypePref getType() {
        return type;
    }

    @Override
    public String getKeyCode() {
        return keyCode;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public AITypePref getTypeEnum() {
        return typeEnum;
    }

    /**
     * Tutti i valori della enum <br>
     */
    @Override
    public String getEnumAll() {
        return preferenceService.getEnumAll(type, keyCode);
    }

    @Override
    public AITypePref getEnumCurrentObj() {
        return preferenceService.getEnumCurrentObj(typeEnum, type, keyCode);
    }

    /**
     * Valore selezionato della enum <br>
     */
    @Override
    public String getEnumCurrent() {
        return preferenceService.getEnumCurrentTxt(type, keyCode);
    }

    /**
     * Valore selezionato della enum <br>
     */
    @Override
    public void setEnumCurrent(String currentValue) {
        preferenceService.setEnumCurrentTxt(type, keyCode, currentValue);
    }

    @Override
    public void setEnumCurrentObj(AITypePref currentValue) {
        preferenceService.setEnumCurrentObj(type, keyCode, currentValue);
    }

    @Component
    public static class PreferenzaServiceInjector {

        @Autowired
        private PreferenceService preferenceService;

        @Autowired
        private LogService logger;

        @Autowired
        private DateService date;

        @Autowired
        private TextService text;

        @PostConstruct
        public void postConstruct() {
            for (WPref pref : WPref.values()) {
                pref.setPreferenceService(preferenceService);
                pref.setLogger(logger);
                pref.setDate(date);
                pref.setText(text);
            }
        }

    }
}
