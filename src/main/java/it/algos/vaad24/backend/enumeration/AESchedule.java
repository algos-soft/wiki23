package it.algos.vaad24.backend.enumeration;

import java.util.*;

/**
 * Project it.algos.vaadflow
 * Created by Algos
 * User: gac
 * Date: gio, 12-lug-2018
 * Time: 17:38
 * <p>
 * Template di schedule preconfigurati, con nota esplicativa utilizzabile nelle info
 * Pattern:
 * minuti, ore, giornoMese, mese, giornoSettimana
 * <p>
 * minuti -> i valori ammessi vanno da 0 a 59.
 * <p>
 * ore -> i valori ammessi vanno da 0 a 23.
 * <p>
 * giornoMese -> i valori ammessi vanno da 1 a 31
 * <p>
 * mese -> i valori ammessi vanno da 1 (gennaio) a 12 (dicembre) , oppure è possibile usare le stringhe-equivalenti:
 * "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov" e "dec".
 * <p>
 * I valori ammessi vanno da 0 (domenica) a 6 (sabato), oppure è possibile usare le stringhe-equivalenti:
 * "sun", "mon", "tue", "wed", "thu", "fri" e "sat".
 * <p>
 * asterisco -> tutti i minuti, tutte le ore, tutti i giorni del mese
 * <p>
 * 1,2,3,4,5 -> ogni lunedì, martedì, mercoledì, giovedì e venerdì
 * 1-5 -> ogni lunedì, martedì, mercoledì, giovedì e venerdì
 * <p>
 * 0 5 * * *|8 10 * * *|22 17 * * * -> ogni giorno di ogni mese alle ore 05:00, alle ore 10:08 e alle ore 17:22
 * <p>
 *
 * @see <a href="http://www.sauronsoftware.it/projects/cron4j/manual.php">...</a>
 */
public enum AESchedule {


    /**
     * Descrizione: ogni giorno a mezzanotte <br>
     */
    giorno("0 0 * * *", "ogni giorno a mezzanotte"),

    /**
     * Descrizione: ogni minuto
     */
    minuto("* * * * *", "ogni minuto."),

    /**
     * Pattern: 0 0 * * mon
     * Descrizione: ogni settimana ai cinque dopo mezzanotte tra domenica e lunedì
     */
    zeroCinqueLunedi("5 0 * * mon", "ogni settimana ai cinque dopo mezzanotte tra domenica e lunedì."),

    /**
     * Pattern: 0 0 * * sun,tue,wed,thu,fri,sat
     * Descrizione: ai cinque dopo mezzanotte di ogni giorno della settimana esclusa la notte tra domenica e lunedì
     */
    zeroCinqueNoLunedi("5 0 * * sun,tue,wed,thu,fri,sat", "ai cinque dopo mezzanotte di ogni giorno della settimana esclusa la notte tra domenica e lunedì."),

    /**
     * Pattern: 0 2 * * sun,tue,wed,thu,fri,sat
     * Descrizione: alle due di ogni mattina della settimana escluso il lunedì
     */
    dueNoLunedi("0 2 * * sun,tue,wed,thu,fri,sat", "alle due di ogni mattina della settimana escluso il lunedì."),

    /**
     * Pattern: 0 4 * * sun,tue,wed,thu,fri,sat
     * Descrizione: alle quattro di ogni mattina della settimana escluso il lunedì
     */
    quattroNoLunedi("0 4 * * sun,tue,wed,thu,fri,sat", "alle quattro di ogni mattina della settimana escluso il lunedì."),

    /**
     * Pattern: 0 10 * * tue
     * Descrizione: alle dieci di ogni martedì
     */
    dieciMartedi("0 10 * * tue", "alle dieci di ogni martedì."),

    /**
     * Pattern: 0 10 * * wed
     * Descrizione: alle dieci di ogni mercoledì
     */
    dieciMercoledi("0 10 * * wed", "alle dieci di ogni mercoledì."),

    /**
     * Pattern: 0 10 * * thu
     * Descrizione: alle dieci di ogni giovedì
     */
    dieciGiovedi("0 10 * * thu", "alle dieci di ogni giovedì."),

    /**
     * Pattern: 0 10 * * fri
     * Descrizione: alle dieci di ogni venerdì
     */
    dieciVenerdi("0 10 * * fri", "alle dieci di ogni venerdì."),

    /**
     * Pattern: 0 10 * * sat
     * Descrizione: alle dieci di ogni sabato
     */
    dieciSabato("0 10 * * sat", "alle dieci di ogni sabato."),

    ;

    /**
     * pattern di schedulazione di tipo 'UNIX'
     */
    private String pattern;

    /**
     * Nota esplicativa da inserire nei log
     */
    private String nota;


    /**
     * @param pattern di schedulazione di tipo 'UNIX'
     * @param nota    esplicativa da inserire nei log
     */
    AESchedule(String pattern, String nota) {
        this.setPattern(pattern);
        this.setNota(nota);
    }// fine del costruttore

    public static List<AESchedule> getAllEnums() {
        return Arrays.stream(values()).toList();
    }
    public static List<String> getAllPattern() {
        List<String> listaTags = new ArrayList<>();

        getAllEnums().forEach(type -> listaTags.add(type.getPattern()));
        return listaTags;
    }

    public String getPattern() {
        return pattern;
    }


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    public String getNota() {
        return nota;
    }


    public void setNota(String nota) {
        this.nota = nota;
    }

}// end of enum

