package it.algos.wiki23.backend.wrapper;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.packages.bio.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sat, 04-Jun-2022
 * Time: 16:09
 * <p>
 * Semplice wrapper per gestire i dati necessari ad una didascalia <br>
 */
public class WrapDidascalia {

    private String attivitaSingola;

    private String attivitaParagrafo;

    private String nazionalitaSingola;

    private String nazionalitaParagrafo;

    private String wikiTitle;

    private String nome;

    private String cognome;

    private String primoCarattere;

    public String giornoNato;

    public String annoNato;


    public String luogoNato;

    public String luogoNatoLink;

    public String giornoMorto;

    public String annoMorto;

    public String luogoMorto;

    public String luogoMortoLink;

    public String meseParagrafoNato;

    public String meseParagrafoMorto;

    public String secoloParagrafoNato;

    public String secoloParagrafoMorto;

    public String attivita2;

    public String attivita3;

    public Bio bio;

    public String getAttivitaSingola() {
        return attivitaSingola;
    }

    public void setAttivitaSingola(String attivitaSingola) {
        this.attivitaSingola = attivitaSingola;
    }

    public String getAttivitaParagrafo() {
        return attivitaParagrafo;
    }

    public void setAttivitaParagrafo(String attivitaParagrafo) {
        this.attivitaParagrafo = attivitaParagrafo;
    }

    public String getNazionalitaSingola() {
        return nazionalitaSingola;
    }

    public void setNazionalitaSingola(String nazionalitaSingola) {
        this.nazionalitaSingola = nazionalitaSingola;
    }

    public String getNazionalitaParagrafo() {
        return nazionalitaParagrafo;
    }

    public void setNazionalitaParagrafo(String nazionalitaParagrafo) {
        this.nazionalitaParagrafo = nazionalitaParagrafo;
    }

    public String getMeseParagrafoNato() {
        return meseParagrafoNato != null ? meseParagrafoNato : VUOTA;
    }

    public void setMeseParagrafoNato(String meseParagrafoNato) {
        this.meseParagrafoNato = meseParagrafoNato;
    }

    public String getMeseParagrafoMorto() {
        return meseParagrafoMorto != null ? meseParagrafoMorto : VUOTA;
    }

    public void setMeseParagrafoMorto(String meseParagrafoMorto) {
        this.meseParagrafoMorto = meseParagrafoMorto;
    }

    public String getSecoloParagrafoNato() {
        return secoloParagrafoNato != null ? secoloParagrafoNato : VUOTA;
    }

    public void setSecoloParagrafoNato(String secoloParagrafoNato) {
        this.secoloParagrafoNato = secoloParagrafoNato;
    }

    public String getSecoloParagrafoMorto() {
        return secoloParagrafoMorto != null ? secoloParagrafoMorto : VUOTA;
    }

    public void setSecoloParagrafoMorto(String secoloParagrafoMorto) {
        this.secoloParagrafoMorto = secoloParagrafoMorto;
    }

    public String getWikiTitle() {
        return wikiTitle;
    }

    public void setWikiTitle(String wikiTitle) {
        this.wikiTitle = wikiTitle;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getPrimoCarattere() {
        return primoCarattere;
    }

    public void setPrimoCarattere(String primoCarattere) {
        this.primoCarattere = primoCarattere;
    }

    public String getGiornoNato() {
        return giornoNato;
    }

    public void setGiornoNato(String giornoNato) {
        this.giornoNato = giornoNato;
    }

    public String getAnnoNato() {
        return annoNato;
    }

    public void setAnnoNato(String annoNato) {
        this.annoNato = annoNato;
    }

    public String getLuogoNato() {
        return luogoNato;
    }

    public void setLuogoNato(String luogoNato) {
        this.luogoNato = luogoNato;
    }

    public String getLuogoNatoLink() {
        return luogoNatoLink;
    }

    public void setLuogoNatoLink(String luogoNatoLink) {
        this.luogoNatoLink = luogoNatoLink;
    }

    public String getGiornoMorto() {
        return giornoMorto;
    }

    public void setGiornoMorto(String giornoMorto) {
        this.giornoMorto = giornoMorto;
    }

    public String getAnnoMorto() {
        return annoMorto;
    }

    public void setAnnoMorto(String annoMorto) {
        this.annoMorto = annoMorto;
    }

    public String getLuogoMorto() {
        return luogoMorto;
    }

    public void setLuogoMorto(String luogoMorto) {
        this.luogoMorto = luogoMorto;
    }

    public String getLuogoMortoLink() {
        return luogoMortoLink;
    }

    public void setLuogoMortoLink(String luogoMortoLink) {
        this.luogoMortoLink = luogoMortoLink;
    }

    public String getAttivita2() {
        return attivita2;
    }

    public void setAttivita2(String attivita2) {
        this.attivita2 = attivita2;
    }

    public String getAttivita3() {
        return attivita3;
    }

    public void setAttivita3(String attivita3) {
        this.attivita3 = attivita3;
    }

    public Bio getBio() {
        return bio;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }

}
