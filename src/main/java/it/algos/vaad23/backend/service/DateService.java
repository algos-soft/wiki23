package it.algos.vaad23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 07-mar-2022
 * Time: 14:57
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ADateService.class); <br>
 * 3) @Autowired public DateService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DateService extends AbstractService {

    public static final String INFERIORE_SECONDO = "meno di un secondo";

    public static final String INFERIORE_MINUTO = "meno di un minuto";

    private static final String MILLI_SECONDI = " millisecondi";

    private static final String SECONDI = " sec.";

    private static final String MINUTI = " min.";

    private static final String ORA = " ora";

    private static final String ORE = " ore";

    private static final String GIORNO = " giorno";

    private static final String GIORNI = " gg.";

    private static final String ANNO = " anno";

    private static final String ANNI = " anni";

    private static final long MAX_MILLISEC = 1000;

    private static final long MAX_SECONDI = 60 * MAX_MILLISEC;

    private static final long MAX_MINUTI = 60 * MAX_SECONDI;

    private static final long MAX_ORE = 24 * MAX_MINUTI;

    private static final long MAX_GIORNI = 365 * MAX_ORE;

    /**
     * Restituisce come stringa (intelligente) un durata espressa in long <br>
     *
     * @return tempo esatto in millisecondi in forma leggibile
     */
    public String deltaTextEsatto(final long inizio) {
        return deltaTextEsatto(inizio, System.currentTimeMillis());
    }

    /**
     * Restituisce come stringa (intelligente) un durata espressa in long <br>
     *
     * @return tempo esatto in millisecondi in forma leggibile
     */
    public String deltaTextEsatto(final long inizio, final long fine) {
        return textService.format(fine - inizio) + MILLI_SECONDI;
    }

    /**
     * Restituisce come stringa (intelligente) un durata espressa in long <br>
     * Esegue degli arrotondamenti <br>
     *
     * @return tempo arrotondato in forma leggibile
     */
    public String deltaText(final long inizio) {
        long fine = System.currentTimeMillis();
        return toText(fine - inizio);
    }


    /**
     * Restituisce come stringa (intelligente) una durata espressa in long
     * - Meno di 1 secondo
     * - Meno di 1 minuto
     * - Meno di 1 ora
     * - Meno di 1 giorno
     * - Meno di 1 anno
     *
     * @param durata in millisecondi
     *
     * @return durata (arrotondata e semplificata) in forma leggibile
     */
    public String toText(final long durata) {
        String tempo = "null";
        long div;
        long mod;

        if (durata < MAX_MILLISEC) {
            tempo = INFERIORE_SECONDO;
        }
        else {
            if (durata < MAX_SECONDI) {
                div = Math.floorDiv(durata, MAX_MILLISEC);
                mod = Math.floorMod(durata, MAX_MILLISEC);
                if (div < 60) {
                    tempo = div + SECONDI;
                }
                else {
                    tempo = "1" + MINUTI;
                }
            }
            else {
                if (durata < MAX_MINUTI) {
                    div = Math.floorDiv(durata, MAX_SECONDI);
                    mod = Math.floorMod(durata, MAX_SECONDI);
                    if (div < 60) {
                        tempo = div + MINUTI;
                    }
                    else {
                        tempo = "1" + ORA;
                    }
                }
                else {
                    if (durata < MAX_ORE) {
                        div = Math.floorDiv(durata, MAX_MINUTI);
                        mod = Math.floorMod(durata, MAX_MINUTI);
                        if (div < 24) {
                            if (div == 1) {
                                tempo = div + ORA;
                            }
                            else {
                                tempo = div + ORE;
                            }
                            tempo += " e " + toText(durata - (div * MAX_MINUTI));
                        }
                        else {
                            tempo = "1" + GIORNO;
                        }
                    }
                    else {
                        if (durata < MAX_GIORNI) {
                            div = Math.floorDiv(durata, MAX_ORE);
                            mod = Math.floorMod(durata, MAX_ORE);
                            if (div < 365) {
                                if (div == 1) {
                                    tempo = div + GIORNO;
                                }
                                else {
                                    tempo = div + GIORNI;
                                }
                            }
                            else {
                                tempo = "1" + ANNO;
                            }
                        }
                        else {
                            div = Math.floorDiv(durata, MAX_GIORNI);
                            mod = Math.floorMod(durata, MAX_GIORNI);
                            if (div == 1) {
                                tempo = div + ANNO;
                            }
                            else {
                                tempo = div + ANNI;
                            }
                        }
                    }
                }
            }
        }

        return tempo;
    }

    public String toTextSecondi(final long durata) {
        return durata < 1 ? INFERIORE_SECONDO : toText(durata * 1000);
    }

    public String toTextMinuti(final long durata) {
        return durata < 1 ? INFERIORE_MINUTO : toTextSecondi(durata * 60);
    }

    public String get() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("d-MMM-yy"));
    }

    public String get(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("d-MMM-yy H:mm", Locale.ITALIAN));
    }

    /**
     * Costruisce tutti i giorni dell'anno <br>
     * Considera anche l'anno bisestile <br>
     * <p>
     * Restituisce un array di Map <br>
     * Ogni mappa ha: <br>
     * numeroMese <br>
     * #progressivoNormale <br>
     * #progressivoBisestile <br>
     * nome  (numero per il primo del mese) <br>
     * titolo (1° per il primo del mese) <br>
     */
    public List<HashMap> getAllGiorni() {
        List<HashMap> listaAnno = new ArrayList<HashMap>();
        List<HashMap> listaMese;
        int progAnno = 0;

        for (int k = 1; k <= 12; k++) {
            listaMese = getGiorniMese(k, progAnno);
            for (HashMap mappa : listaMese) {
                listaAnno.add(mappa);
            }
            progAnno += listaMese.size();
        }

        return listaAnno;
    }

    /**
     * Costruisce tutti i giorni del mese <br>
     * Considera anche l'anno bisestile <br>
     * <p>
     * Restituisce un array di Map <br>
     * Ogni mappa ha: <br>
     * numeroMese <br>
     * nomeMese <br>
     * #progressivoNormale <br>
     * #progressivoBisestile <br>
     * nome  (numero per il primo del mese) <br>
     * titolo (1° per il primo del mese) <br>
     *
     * @param numMese  numero del mese, partendo da 1 per gennaio
     * @param progAnno numero del giorno nell'anno, partendo da 1 per il 1° gennaio
     *
     * @return lista di mappe, una per ogni giorno del mese considerato
     */
    private List<HashMap> getGiorniMese(int numMese, int progAnno) {
        List<HashMap> listaMese = new ArrayList<HashMap>();
        HashMap mappa;
        int giorniDelMese;
        String nomeMese;
        AEMese mese = AEMese.getMese(numMese);
        nomeMese = AEMese.getLong(numMese);
        giorniDelMese = AEMese.getGiorni(numMese, 2016);
        final int taglioBisestile = 60;
        String tag;
        String tagUno;

        for (int k = 1; k <= giorniDelMese; k++) {
            progAnno++;
            tag = k + SPAZIO + nomeMese;
            mappa = new HashMap();

            mappa.put(KEY_MAPPA_GIORNI_MESE_NUMERO, numMese);
            mappa.put(KEY_MAPPA_GIORNI_MESE_TESTO, nomeMese);
            mappa.put(KEY_MAPPA_GIORNI_NOME, tag);
            mappa.put(KEY_MAPPA_GIORNI_BISESTILE, progAnno);
            mappa.put(KEY_MAPPA_GIORNI_NORMALE, progAnno);
            mappa.put(KEY_MAPPA_GIORNI_MESE_MESE, mese);

            if (k == 1) {
                mappa.put(KEY_MAPPA_GIORNI_TITOLO, PRIMO_GIORNO_MESE + SPAZIO + nomeMese);
            }
            else {
                mappa.put(KEY_MAPPA_GIORNI_TITOLO, tag);
            }
            //--gestione degli anni bisestili
            if (progAnno == taglioBisestile) {
                mappa.put(KEY_MAPPA_GIORNI_NORMALE, 0);
            }
            if (progAnno > taglioBisestile) {
                mappa.put(KEY_MAPPA_GIORNI_NORMALE, progAnno - 1);
            }
            listaMese.add(mappa);
        }
        return listaMese;
    }


    /**
     * Anno bisestile
     *
     * @param anno da validare
     *
     * @return true se l'anno è bisestile
     */
    public boolean isBisestile(int anno) {
        boolean bisestile = false;
        int deltaGiuliano = 4;
        int deltaSecolo = 100;
        int deltaGregoriano = 400;
        int inizioGregoriano = 1582;
        boolean bisestileSecolo = false;

        bisestile = divisibileEsatto(anno, deltaGiuliano);
        if (anno > inizioGregoriano && bisestile) {
            if (divisibileEsatto(anno, deltaSecolo)) {
                if (divisibileEsatto(anno, deltaGregoriano)) {
                    bisestile = true;
                }
                else {
                    bisestile = false;
                }
            }
        }

        return bisestile;
    }

    public boolean divisibileEsatto(int dividendo, int divisore) {
        return dividendo % divisore == 0;
    }

}