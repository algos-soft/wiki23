package it.algos.wiki23.backend.packages.pagina;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.apache.commons.lang3.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import java.text.*;
import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 21-Sep-2022
 * Time: 17:39
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class PaginaBackend extends WikiBackend {

    public PaginaRepository repository;

    /**
     * Costruttore @Autowired (facoltativo) @Qualifier (obbligatorio) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Si usa un @Qualifier(), per specificare la classe che incrementa l'interfaccia repository <br>
     * Si usa una costante statica, per essere sicuri di scriverla uguale a quella di xxxRepository <br>
     * Regola la classe di persistenza dei dati specifica e la passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questo service <br>
     *
     * @param crudRepository per la persistenza dei dati
     */
    //@todo registrare eventualmente come costante in VaadCost il valore del Qualifier
    public PaginaBackend(@Autowired @Qualifier("Pagina") final MongoRepository crudRepository) {
        super(crudRepository, Pagina.class);
        this.repository = (PaginaRepository) crudRepository;
    }

    public Pagina creaIfNotExist(final String pagina, AETypePaginaCancellare type) {
        return creaIfNotExist(pagina, type, 0, false);
    }

    public Pagina creaIfNotExist(final String pagina, AETypePaginaCancellare type, final int voci, final boolean cancella) {
        return checkAndSave(newEntity(pagina, type, voci, cancella));
    }

    public Pagina checkAndSave(final Pagina pagina) {
        return isExist(pagina.pagina) ? null : repository.insert(pagina);
    }

    public boolean isExist(final String pagina) {
        return repository.findFirstByPagina(pagina) != null;
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Pagina newEntity() {
        return newEntity(VUOTA, null, 0, false);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param pagina   (obbligatorio, unico)
     * @param type     (obbligatorio)
     * @param voci     (facoltativo)
     * @param cancella (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Pagina newEntity(final String pagina, final AETypePaginaCancellare type, final int voci, final boolean cancella) {
        return Pagina.builder()
                .pagina(textService.isValid(pagina) ? pagina : null)
                .type(type)
                .voci(voci)
                .cancella(cancella)
                .build();
    }

    public Pagina findByPagina(final String pagina) {
        return repository.findFirstByPagina(pagina);
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        long inizio = System.currentTimeMillis();
        mongoService.deleteAll(Pagina.class);

        //        elaboraGiorni();
        //        elaboraAnni();
        //        elaboraAttivita();
        //        elaboraNazionalita();
        elaboraCognomi();
        elaboraUtenteBot();

        super.fixElaboraMinuti(inizio, "cancellazioni");
    }

    public List<Pagina> elaboraGiorni() {
        List<Pagina> pagineMongo = new ArrayList<>();
        List<String> valideBase = giornoWikiBackend.findAllPagine();
        List<String> pagineAll = getPagineGiorni();

        pagineMongo.addAll(elaboraGiorniPagine(valideBase, getGiorniAnni(pagineAll)));
        pagineMongo.addAll(elaboraGiorniSottoPagine(valideBase, getGiorniAnniSotto(pagineAll)));

        return pagineMongo;
    }

    /**
     * Quelle di primo livello che terminano con /
     * Quelle di primo livello che terminano con /...
     * Quelle di primo livello che non esistono in Giorno
     */
    public List<Pagina> elaboraGiorniPagine(List<String> valideBase, List<String> pagine) {
        List<Pagina> pagineMongo = new ArrayList<>();
        int voci;

        for (String wikiTitle : pagine) {
            // Quelle di primo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, 0, true));
                continue;
            }

            // Quelle di primo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, 0, true));
                continue;
            }

            // Quelle di primo livello che non esistono in Giorno
            if (!valideBase.contains(wikiTitle)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, 0, true));
                continue;
            }

            voci = getVociGiorno(wikiTitle);
            if (voci > 0) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, voci, false));
            }
            else {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, voci, true));
            }

        }

        return pagineMongo;
    }


    /**
     * Quelle di secondo livello che terminano con /
     * Quelle di secondo livello che terminano con /...
     * Quelle di secondo livello che non hanno un corrispondente primo livello
     * Quelle di secondo livello che non superano le 50 voci
     */
    public List<Pagina> elaboraGiorniSottoPagine(List<String> valideBase, List<String> pagineServer) {
        List<Pagina> pagineMongo = new ArrayList<>();
        int voci = 0;
        String paginaParentePrimoLivello;
        String secolo;
        int sogliaMaxPagina = WPref.sogliaSottoPaginaGiorniAnni.getInt();

        for (String wikiTitle : pagineServer) {
            // Quelle di secondo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true));
                continue;
            }

            // Quelle di secondo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true));
                continue;
            }

            // Quelle di secondo livello che non hanno un corrispondente primo livello
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            if (!valideBase.contains(paginaParentePrimoLivello)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true));
                continue;
            }

            // Quelle di secondo livello che non superano le 50 voci
            secolo = wikiTitle.substring(wikiTitle.indexOf(SLASH) + 1);
            voci = getVociGiorno(paginaParentePrimoLivello, secolo);
            if (voci >= sogliaMaxPagina) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, false));
            }
            else {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true));
            }
        }

        return pagineMongo;
    }


    public List<Pagina> elaboraAnni() {
        List<Pagina> pagineMongo = new ArrayList<>();
        List<String> valideBase = annoWikiBackend.findAllPagine();
        List<String> pagineAll = getPagineAnni();

        pagineMongo.addAll(elaboraAnniPagine(valideBase, getGiorniAnni(pagineAll)));
        pagineMongo.addAll(elaboraAnniSottoPagine(valideBase, getGiorniAnniSotto(pagineAll)));

        return pagineMongo;
    }


    /**
     * Quelle di primo livello che terminano con /
     * Quelle di primo livello che terminano con /...
     * Quelle di primo livello che non esistono in Anno
     */
    public List<Pagina> elaboraAnniPagine(List<String> valideBase, List<String> pagine) {
        List<Pagina> pagineMongo = new ArrayList<>();
        int voci;

        for (String wikiTitle : pagine) {
            // Quelle di primo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, 0, true));
                continue;
            }

            // Quelle di primo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, 0, true));
                continue;
            }

            // Quelle di primo livello che non esistono in Anno
            if (!valideBase.contains(wikiTitle)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, 0, true));
                continue;
            }

            voci = getVociAnno(wikiTitle);
            if (voci > 0) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, voci, false));
            }
            else {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, voci, true));
            }
        }

        return pagineMongo;
    }


    /**
     * Quelle di secondo livello che terminano con /
     * Quelle di secondo livello che terminano con /...
     * Quelle di secondo livello che non hanno un corrispondente primo livello
     * Quelle di secondo livello che non superano le 50 voci
     */
    public List<Pagina> elaboraAnniSottoPagine(List<String> valideBase, List<String> pagine) {
        List<Pagina> pagineMongo = new ArrayList<>();
        int voci = 0;
        String paginaParentePrimoLivello;
        String mese;
        int sogliaMaxPagina = WPref.sogliaSottoPaginaGiorniAnni.getInt();

        for (String wikiTitle : pagine) {
            // Quelle di secondo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true));
                continue;
            }

            // Quelle di secondo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true));
                continue;
            }

            // Quelle di secondo livello che non hanno un corrispondente primo livello
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            if (!valideBase.contains(paginaParentePrimoLivello)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true));
                continue;
            }

            // Quelle di secondo livello che non superano le 50 voci
            mese = wikiTitle.substring(wikiTitle.indexOf(SLASH) + 1);
            voci = getVociAnno(paginaParentePrimoLivello, mese);
            if (voci >= sogliaMaxPagina) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, false));
            }
            else {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true));
            }
        }

        return pagineMongo;
    }

    /**
     * Pagine di attività da cancellare:
     */
    public void elaboraAttivita() {
        int nameSpace = 102;
        String tag = "Biografie/Attività/";
        List<String> pagineAll = queryService.getList(tag, nameSpace);
        List<String> valideBase = attivitaBackend.findAllPlurali();

        elaboraAttivitaPagine(valideBase, getPagine(pagineAll));
        elaboraAttivitaSottoPagine(valideBase, getSottoPagine(pagineAll));
        elaboraAttivitaSottoSottoPagine(valideBase, getSottoSottoPagine(pagineAll));
    }

    /**
     * Quelle di primo livello che terminano con /
     * Quelle di primo livello che terminano con /...
     * Quelle di primo livello che non esistono in Attivita
     * Quelle di primo livello femminili
     * Quelle di primo livello singolari e non plurali
     * Quelle di primo livello che non superano le 50 voci
     */
    public void elaboraAttivitaPagine(List<String> valideBase, List<String> pagine) {
        String tagBase = PATH_ATTIVITA + SLASH;
        String paginaBase;
        int voci = 0;
        int sogliaAttNazWiki = WPref.sogliaAttNazWiki.getInt();

        for (String wikiTitle : pagine) {
            // Quelle di primo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaBase, voci, true);
                continue;
            }

            // Quelle di primo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaBase, voci, true);
                continue;
            }

            paginaBase = textService.levaTesta(wikiTitle, tagBase);
            paginaBase = textService.primaMinuscola(paginaBase);

            // Quelle di primo livello che non esistono in Attivita
            if (!valideBase.contains(paginaBase)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaBase, voci, true);
                continue;
            }

            // Quelle di primo livello femminili
            // ???

            // Quelle di primo livello singolari e non plurali
            String gamma = attivitaBackend.pluraleBySingolarePlurale(paginaBase);
            if (paginaBase.equals(gamma)) {
                voci = bioBackend.countAttivitaPlurale(paginaBase);
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaBase, voci, false);
                continue;
            }

            // Quelle di primo livello che non superano le 50 voci
            voci = bioBackend.countAttivitaPlurale(paginaBase);
            if (voci >= sogliaAttNazWiki) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaBase, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaBase, voci, true);
            }
        }
    }

    /**
     * Quelle di secondo livello che terminano con /
     * Quelle di secondo livello che terminano con /...
     * Quelle di secondo livello che non hanno un corrispondente primo livello
     * Quelle di secondo livello che non esistono in Nazionalita
     * Quelle di secondo livello femminili
     * Quelle di secondo livello singolari e non plurali
     * Quelle di secondo livello che non superano le 50 voci
     */
    public void elaboraAttivitaSottoPagine(List<String> valideBase, List<String> pagine) {
        String tagBase = PATH_ATTIVITA + SLASH;
        int voci = 0;
        String paginaParentePrimoLivello;
        String attivita;
        String nazionalita;
        int sogliaSottoPagina = WPref.sogliaSottoPagina.getInt();
        sogliaSottoPagina = (sogliaSottoPagina * 8) / 10;
        sogliaSottoPagina = 30;

        for (String wikiTitle : pagine) {
            // Quelle di secondo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non hanno un corrispondente primo livello
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            attivita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            attivita = textService.primaMinuscola(attivita);
            if (!valideBase.contains(attivita)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non superano le 50 voci
            nazionalita = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            nazionalita = textService.primaMinuscola(nazionalita);
            voci = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita);
            if (voci >= sogliaSottoPagina) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSotto, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSotto, voci, true);
            }
        }
    }

    /**
     * Quelle di terzo livello che terminano con /
     * Quelle di terzo livello che terminano con /...
     * Quelle di terzo livello che non hanno un corrispondente secondo livello
     * Quelle di terzo livello che hanno un secondo livello che non supera le 50 voci
     */
    public void elaboraAttivitaSottoSottoPagine(List<String> valideBase, List<String> pagine) {
        String tagBase = PATH_ATTIVITA + SLASH;
        int voci = 0;
        String paginaParentePrimoLivello;
        String paginaParenteSecondoLivello;
        String letteraIniziale;
        String attivita;
        String nazionalita;
        int sogliaSottoPagina = WPref.sogliaSottoPagina.getInt();
        sogliaSottoPagina = (sogliaSottoPagina * 8) / 10;
        sogliaSottoPagina = 30;

        for (String wikiTitle : pagine) {
            // Quelle di terzo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, true);
                continue;
            }

            // Quelle di terzo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, true);
                continue;
            }

            // Quelle di terzo livello che non hanno un corrispondente secondo livello
            paginaParenteSecondoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(paginaParenteSecondoLivello, SLASH);
            letteraIniziale = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            attivita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            attivita = textService.primaMinuscola(attivita);

            nazionalita = paginaParenteSecondoLivello.substring(paginaParenteSecondoLivello.lastIndexOf(SLASH) + 1);
            nazionalita = nazionalita.equals(TAG_LISTA_ALTRE) ? nazionalita : textService.primaMinuscola(nazionalita);
            voci = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita, letteraIniziale);
            if (voci >= sogliaSottoPagina) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, true);
            }
        }
    }

    /**
     * Pagine di nazionalità da cancellare:
     */
    public void elaboraNazionalita() {
        int nameSpace = 102;
        String tag = "Biografie/Nazionalità/";
        List<String> pagineAll = queryService.getList(tag, nameSpace);
        List<String> valideBase = nazionalitaBackend.findAllPluraliDistinti();

        elaboraNazionalitaPagine(valideBase, getPagine(pagineAll));
        elaboraNazionalitaSottoPagine(valideBase, getSottoPagine(pagineAll));
        elaboraNazionalitaSottoSottoPagine(valideBase, getSottoSottoPagine(pagineAll));
    }


    /**
     * Quelle di primo livello che terminano con /
     * Quelle di primo livello che terminano con /...
     * Quelle di primo livello che non esistono in Nazionalita
     * Quelle di primo livello femminili
     * Quelle di primo livello singolari e non plurali
     * Quelle di primo livello che non superano le 50 voci
     */
    public void elaboraNazionalitaPagine(List<String> valideBase, List<String> pagine) {
        String tagBase = PATH_NAZIONALITA + SLASH;
        String paginaBase;
        int voci = 0;
        int sogliaAttNazWiki = WPref.sogliaAttNazWiki.getInt();

        for (String wikiTitle : pagine) {
            // Quelle di primo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaBase, voci, true);
                continue;
            }

            // Quelle di primo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaBase, voci, true);
                continue;
            }

            // Patch
            if (wikiTitle.equals(PATH_NAZIONALITA + SLASH + "Riepilogo")) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.progetto, voci, false);
                continue;
            }

            paginaBase = textService.levaTesta(wikiTitle, tagBase);
            paginaBase = textService.primaMinuscola(paginaBase);

            // Quelle di primo livello che non esistono in Nazionalita
            if (!valideBase.contains(paginaBase)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaBase, voci, true);
                continue;
            }

            // Quelle di primo livello femminili
            // ???

            // Quelle di primo livello singolari e non plurali
            String gamma = nazionalitaBackend.pluraleBySingolarePlurale(paginaBase);
            Nazionalita delta = nazionalitaBackend.findFirstBySingolare(paginaBase);
            Nazionalita delta2 = nazionalitaBackend.findFirstByPluraleLista(paginaBase);
            if (paginaBase.equals(gamma)) {
                //                voci = bioBackend.countNazionalitaPlurale(paginaBase);
                //                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaBase, voci, false);
                continue;
            }

            // Quelle di primo livello che non superano le 50 voci
            voci = bioBackend.countNazionalitaPlurale(paginaBase);
            if (voci >= sogliaAttNazWiki) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaBase, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaBase, voci, true);
            }
        }
    }

    /**
     * Quelle di secondo livello che terminano con /
     * Quelle di secondo livello che terminano con /...
     * Quelle di secondo livello che non hanno un corrispondente primo livello
     * Quelle di secondo livello che non esistono in Attivita
     * Quelle di secondo livello femminili
     * Quelle di secondo livello singolari e non plurali
     * Quelle di secondo livello che non superano le 50 voci
     */
    public void elaboraNazionalitaSottoPagine(List<String> valideBase, List<String> pagine) {
        String tagBase = PATH_NAZIONALITA + SLASH;
        int voci = 0;
        String paginaParentePrimoLivello;
        String nazionalita;
        String attivita;
        int sogliaSottoPagina = WPref.sogliaSottoPagina.getInt();
        sogliaSottoPagina = (sogliaSottoPagina * 8) / 10;
        sogliaSottoPagina = 20;

        for (String wikiTitle : pagine) {
            // Quelle di secondo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non hanno un corrispondente primo livello
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            nazionalita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            nazionalita = textService.primaMinuscola(nazionalita);
            if (!valideBase.contains(nazionalita)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non superano le 50 voci
            attivita = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            attivita = textService.primaMinuscola(attivita);
            voci = bioBackend.countNazionalitaAttivitaAll(nazionalita, attivita);
            if (voci >= sogliaSottoPagina) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSotto, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSotto, voci, true);
            }
        }
    }

    /**
     * Quelle di terzo livello che terminano con /
     * Quelle di terzo livello che terminano con /...
     * Quelle di terzo livello che non hanno un corrispondente secondo livello
     * Quelle di terzo livello che hanno un secondo livello che non supera le 50 voci
     */
    public void elaboraNazionalitaSottoSottoPagine(List<String> valideBase, List<String> pagine) {
        String tagBase = PATH_NAZIONALITA + SLASH;
        int voci = 0;
        String paginaParentePrimoLivello;
        String paginaParenteSecondoLivello;
        String letteraIniziale;
        String attivita;
        String nazionalita;
        int sogliaSottoPagina = WPref.sogliaSottoPagina.getInt();
        sogliaSottoPagina = (sogliaSottoPagina * 8) / 10;
        sogliaSottoPagina = 20;

        for (String wikiTitle : pagine) {
            // Quelle di terzo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, true);
                continue;
            }

            // Quelle di terzo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, true);
                continue;
            }

            // Quelle di terzo livello che non hanno un corrispondente secondo livello
            paginaParenteSecondoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(paginaParenteSecondoLivello, SLASH);
            letteraIniziale = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            nazionalita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            nazionalita = textService.primaMinuscola(nazionalita);

            attivita = paginaParenteSecondoLivello.substring(paginaParenteSecondoLivello.lastIndexOf(SLASH) + 1);
            attivita = textService.primaMinuscola(attivita);
            voci = bioBackend.countNazionalitaAttivitaAll(nazionalita, attivita, letteraIniziale);
            if (voci >= sogliaSottoPagina) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, true);
            }
        }
    }

    public void elaboraCognomi() {
        String tag = "Persone di cognome";
        List<String> pagineAll = queryService.getList(tag);
        List<String> valideBase = cognomeBackend.findCognomi();

        //        pagineAll = elaboraCognomiRedirect(pagineAll);
        elaboraCognomiPagine(valideBase, getCognomi(pagineAll));
    }

    /**
     * Identifico quelli uguali con accenti differenti
     */
    public List<String> fixCognomiDiacritici(List<String> pagineGrezzeIndifferenziate) {
        List<String> pagineSporche = new ArrayList<>();

        for (String grezza : pagineGrezzeIndifferenziate) {
            if (wikiUtility.isDiacritica(grezza)) {
                pagineSporche.add(grezza);
            }
        }

        return pagineSporche;
    }

    /**
     * Identifico quelli uguali con accenti differenti
     * Identifico i redirect
     */
    public List<String> fixRedirect(List<String> pagineGrezzeIndifferenziate) {
        List<String> pagineSenzaRedirect = new ArrayList<>();
        List<String> pagineSporche = new ArrayList<>();
        String pulita;

        for (String grezza : pagineGrezzeIndifferenziate) {
            if (wikiUtility.isDiacritica(grezza)) {

            }

            pulita = StringUtils.stripAccents(grezza);
            if (pagineSenzaRedirect.contains(pulita)) {
                pagineSporche.add(grezza);
            }
            else {
                pagineSenzaRedirect.add(pulita);
            }
        }

        //        final Collator instance = Collator.getInstance();
        //
        //        // This strategy mean it'll ignore the accents
        //        instance.setStrength(Collator.NO_DECOMPOSITION);
        //
        //        // Will print 0 because its EQUAL
        //
        //        System.out.println(instance.compare(a, b));
        Object alfa = pagineSporche;
        return pagineSenzaRedirect;
    }

    public void elaboraCognomiPagine(List<String> valideBase, List<String> pagine) {
        String tag = "Persone di cognome";
        String paginaBase;
        int voci = 0;
        int sogliaCognomi = WPref.sogliaCognomiWiki.getInt();
        sogliaCognomi = (sogliaCognomi * 8) / 10;

        for (String wikiTitle : pagine) {
            // Quelle di primo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.cognomi, voci, true);
                continue;
            }

            // Quelle di primo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.cognomi, voci, true);
                continue;
            }

            paginaBase = textService.levaTesta(wikiTitle, tag).trim();
            paginaBase = textService.primaMaiuscola(paginaBase);

            // Identifico quelli uguali con accenti differenti
            // Controllo i redirect e li elimino
            if (wikiUtility.isDiacritica(paginaBase)) {
            }

            // Quelle di primo livello che non esistono in Attivita
            if (!valideBase.contains(paginaBase)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.cognomi, voci, true);
                continue;
            }

            // Quelle di primo livello che non superano le 50 voci
            voci = bioBackend.countCognome(paginaBase);
            if (voci >= sogliaCognomi) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.cognomi, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.cognomi, voci, true);
            }
        }
    }

    public void elaboraUtenteBot() {
        int nameSpace = 2;
        String tagNati = "Biobot/Nati";
        String tagMorti = "Biobot/Morti";
        List<String> pagine;

        pagine = queryService.getList(tagNati, nameSpace);
        if (pagine != null) {
            for (String wikiTitle : pagine) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.bioBotNati, 0, true);
            }
        }

        pagine = queryService.getList(tagMorti, nameSpace);
        if (pagine != null) {
            for (String wikiTitle : pagine) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.bioBotMorti, 0, true);
            }
        }
    }

    public List<String> getPagine(List<String> pagine) {
        return getLivello(pagine, 2);
    }

    public List<String> getSottoPagine(List<String> pagine) {
        return getLivello(pagine, 3);
    }

    public List<String> getSottoSottoPagine(List<String> pagine) {
        return getLivello(pagine, 4);
    }

    public List<String> getGiorniAnni(List<String> pagine) {
        return getLivello(pagine, 0);
    }

    public List<String> getGiorniAnniSotto(List<String> pagine) {
        return getLivello(pagine, 1);
    }

    public List<String> getCognomi(List<String> pagine) {
        return getLivello(pagine, 0);
    }

    public List<String> getLivello(List<String> pagine, int occorrenze) {
        List<String> livello;

        livello = pagine
                .stream()
                .filter(pagina -> regexService.count(pagina, SLASH) == occorrenze)
                .collect(Collectors.toList());

        return livello;
    }


    public int getVociGiorno(String wikiTitle) {
        return getVociGiorno(wikiTitle, VUOTA);
    }

    public int getVociGiorno(String wikiTitle, String nomeSecolo) {
        int voci = 0;
        String nomeGiorno;

        if (wikiTitle.contains(APICE)) {
            nomeGiorno = wikiTitle.substring(wikiTitle.indexOf(APICE) + 1).trim();
        }
        else {
            nomeGiorno = wikiTitle.substring(wikiTitle.indexOf(SPAZIO)).trim();
            nomeGiorno = nomeGiorno.substring(nomeGiorno.indexOf(SPAZIO)).trim();
        }

        if (wikiTitle.startsWith("Nati")) {
            if (textService.isEmpty(nomeSecolo)) {
                voci = bioBackend.countGiornoNato(nomeGiorno);
            }
            else {
                voci = bioBackend.countGiornoNatoSecolo(nomeGiorno, nomeSecolo);
            }
        }
        if (wikiTitle.startsWith("Morti")) {
            if (textService.isEmpty(nomeSecolo)) {
                voci = bioBackend.countGiornoMorto(nomeGiorno);
            }
            else {
                voci = bioBackend.countAnnoMortoMese(nomeGiorno, nomeSecolo);
            }
        }

        return voci;
    }


    public int getVociAnno(String wikiTitle) {
        return getVociAnno(wikiTitle, VUOTA);
    }

    public int getVociAnno(String wikiTitle, String nomeMese) {
        int voci = 0;
        String nomeAnno;

        if (wikiTitle.contains(APICE)) {
            nomeAnno = wikiTitle.substring(wikiTitle.indexOf(APICE) + 1).trim();
        }
        else {
            nomeAnno = wikiTitle.substring(wikiTitle.indexOf(SPAZIO)).trim();
            nomeAnno = nomeAnno.substring(nomeAnno.indexOf(SPAZIO)).trim();
        }

        if (wikiTitle.startsWith("Nati")) {
            if (textService.isEmpty(nomeMese)) {
                voci = bioBackend.countAnnoNato(nomeAnno);
            }
            else {
                voci = bioBackend.countAnnoNatoMese(nomeAnno, nomeMese);
            }
        }
        if (wikiTitle.startsWith("Morti")) {
            if (textService.isEmpty(nomeMese)) {
                voci = bioBackend.countAnnoMorto(nomeAnno);
            }
            else {
                voci = bioBackend.countAnnoMortoMese(nomeAnno, nomeMese);
            }
        }

        return voci;
    }

    public List<String> getListaPagine(List<String> listaTag) {
        List<String> listaPagine = new ArrayList<>();

        if (listaTag != null && listaTag.size() > 0) {
            for (String tag : listaTag) {
                listaPagine.addAll(queryService.getList(tag));
            }
        }

        return listaPagine;
    }

    public List<String> getPagineGiorni() {
        return getListaPagine(getTagGiorni());
    }

    public List<String> getPagineAnni() {
        String tagPatch = "Nati nello spazio";
        List<String> pagineAll = getListaPagine(getTagAnni());

        if (pagineAll.contains(tagPatch)) {
            pagineAll.remove(tagPatch);
        }

        return pagineAll;
    }

    public List<String> getTagGiorni() {
        String tagNatiA = "Nati il";
        String tagNatiB = "Nati l'";
        String tagMortiA = "Morti il";
        String tagMortiB = "Morti l'";

        return arrayService.crea(tagNatiA, tagNatiB, tagMortiA, tagMortiB);
    }

    public List<String> getTagAnni() {
        String tagNati = "Nati nel";
        String tagMorti = "Morti nel";

        return arrayService.crea(tagNati, tagMorti);
    }


}// end of crud backend class
