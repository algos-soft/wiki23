package it.algos.wiki23.backend.statistiche;


import it.algos.vaad24.backend.packages.crono.anno.*;
import it.algos.vaad24.backend.packages.crono.giorno.*;
import it.algos.wiki23.backend.enumeration.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 19-nov-2019
 * Time: 09:10
 */
public class MappaStatistiche {

    private String chiave;

    private int numAttivitaUno = 0;

    private int numAttivitaDue = 0;

    private int numAttivitaTre = 0;

    private int numAttivitaTotali = 0;

    private int numNazionalita = 0;

    private int numGiornoNato = 0;

    private int numGiornoMorto = 0;

    private int numAnnoNato = 0;

    private int numAnnoMorto = 0;

    private int ordine;

    private int pos;

    private String nome;

    private int nati = 0;

    private int morti = 0;

    private String percNati;

    private String percMorti;

    private boolean superaSoglia;

    private boolean esistePagina;

    public MappaStatistiche(String chiave) {
        this.chiave = chiave;
    }


    public MappaStatistiche(String chiave, int numNazionalita) {
        this.chiave = chiave;
        this.numNazionalita = numNazionalita;
    }


    public MappaStatistiche(String chiave, int numAttivitaUno, int numAttivitaDue, int numAttivitaTre, boolean superaSoglia, boolean esistePagina) {
        this.chiave = chiave;
        this.numAttivitaUno = numAttivitaUno;
        this.numAttivitaDue = numAttivitaDue;
        this.numAttivitaTre = numAttivitaTre;
        this.numAttivitaTotali = numAttivitaUno + numAttivitaDue + numAttivitaTre;
        this.superaSoglia = esistePagina;
    }


    public MappaStatistiche(Giorno giorno, int numGiornoNato, int numGiornoMorto) {
        this.chiave = giorno.nome;
        this.ordine = giorno.ordine;
        this.numGiornoNato = numGiornoNato;
        this.numGiornoMorto = numGiornoMorto;
    }


    public MappaStatistiche(Anno anno, int ordine, int numAnnoNato, int numAnnoMorto) {
        this.chiave = anno.nome;
        this.ordine = ordine;
        this.numAnnoNato = numAnnoNato;
        this.numAnnoMorto = numAnnoMorto;
    }

    public MappaStatistiche(int pos, String nome, int nati, int morti) {
        this.pos = pos;
        this.nome = nome;
        this.nati = nati;
        this.morti = morti;
    }

    public String getChiave() {
        return chiave;
    }


    public int getNumAttivitaUno() {
        return numAttivitaUno;
    }


    public int getNumAttivitaDue() {
        return numAttivitaDue;
    }


    public int getNumAttivitaTre() {
        return numAttivitaTre;
    }


    public int getNumAttivitaTotali() {
        return numAttivitaTotali;
    }


    public int getNumNazionalita() {
        return numNazionalita;
    }


    public int getNumGiornoNato() {
        return numGiornoNato;
    }


    public int getNumGiornoMorto() {
        return numGiornoMorto;
    }


    public int getNumAnnoNato() {
        return numAnnoNato;
    }


    public int getNumAnnoMorto() {
        return numAnnoMorto;
    }


    public int getOrdine() {
        return ordine;
    }

    public boolean isUsata() {
        return numNazionalita > 0;
    }

    public boolean isUsata(boolean treAttivita) {
        if (treAttivita) {
            return isUsataTreAttivita();
        }
        else {
            return isUsataUnaAttivita();
        }
    }

    public boolean isUsataTreAttivita() {
        return numAttivitaTotali > 0;
    }

    public boolean isUsataUnaAttivita() {
        return numAttivitaUno > 0;
    }

    public boolean isUsataParzialmente() {
        return numAttivitaTotali > 0 && numAttivitaUno == 0;
    }

    public boolean isNonUsata() {
        return numAttivitaTotali < 1;
    }

    public int getPos() {
        return pos;
    }

    public String getNome() {
        return nome;
    }

    public int getNati() {
        return nati;
    }

    public int getMorti() {
        return morti;
    }

    public String getPercNati() {
        return percNati;
    }

    public void setPercNati(String percNati) {
        this.percNati = percNati;
    }

    public String getPercMorti() {
        return percMorti;
    }

    public void setPercMorti(String percMorti) {
        this.percMorti = percMorti;
    }

    public boolean isSuperaSoglia() {
        return superaSoglia;
    }

    public boolean isEsistePagina() {
        return esistePagina;
    }

}// end of class
