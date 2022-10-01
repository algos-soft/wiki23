package it.algos.wiki23.backend.packages.pagina;

import it.algos.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.logic.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

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
        elaboraAttivita();
//        elaboraNazionalita();
//        elaboraUtenteBot();

        super.fixElaboraMinuti(inizio, "cancellazioni");
    }

    public void elaboraGiorni() {
        String tagNatiA = "Nati il";
        String tagNatiB = "Nati l'";
        String tagMortiA = "Morti il";
        String tagMortiB = "Morti l'";
        List<String> pagineAll = new ArrayList<>();

        pagineAll.addAll(queryService.getList(tagNatiA));
        pagineAll.addAll(queryService.getList(tagNatiB));
        pagineAll.addAll(queryService.getList(tagMortiA));
        pagineAll.addAll(queryService.getList(tagMortiB));
        List<String> valideBase = giornoWikiBackend.findAllPagine();

        elaboraGiornoPagine(valideBase, getLivello(pagineAll, 0));
        elaboraGiornoSottoPagine(valideBase, getLivello(pagineAll, 1));
    }

    /**
     * Quelle di primo livello che terminano con /
     * Quelle di primo livello che terminano con /...
     * Quelle di primo livello che non esistono in Giorno
     */
    public void elaboraGiornoPagine(List<String> valideBase, List<String> pagine) {
        int voci;

        for (String wikiTitle : pagine) {
            // Quelle di primo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, 0, true);
                continue;
            }

            // Quelle di primo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, 0, true);
                continue;
            }

            // Quelle di primo livello che non esistono in Giorno
            if (!valideBase.contains(wikiTitle)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, 0, true);
                continue;
            }

            voci = getVociGiorno(wikiTitle);
            if (voci > 0) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoBase, voci, true);
            }

        }
    }


    /**
     * Quelle di secondo livello che terminano con /
     * Quelle di secondo livello che terminano con /...
     * Quelle di secondo livello che non hanno un corrispondente primo livello
     * Quelle di secondo livello che non superano le 50 voci
     */
    public void elaboraGiornoSottoPagine(List<String> valideBase, List<String> pagine) {
        int voci = 0;
        String paginaParentePrimoLivello;
        String secolo;

        for (String wikiTitle : pagine) {
            // Quelle di secondo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non hanno un corrispondente primo livello
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            if (!valideBase.contains(paginaParentePrimoLivello)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non superano le 50 voci
            secolo = wikiTitle.substring(wikiTitle.indexOf(SLASH) + 1);
            voci = getVociGiorno(paginaParentePrimoLivello, secolo);
            if (voci > 50) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoSotto, voci, true);
            }
        }
    }


    public void elaboraAnni() {
        String tagNati = "Nati nel";
        String tagMorti = "Morti nel";
        List<String> pagineAll = new ArrayList<>();

        pagineAll.addAll(queryService.getList(tagNati));
        pagineAll.addAll(queryService.getList(tagMorti));
        List<String> valideBase = annoWikiBackend.findAllPagine();

        elaboraAnnoPagine(valideBase, getLivello(pagineAll, 0));
        elaboraAnnoSottoPagine(valideBase, getLivello(pagineAll, 1));
    }


    /**
     * Quelle di primo livello che terminano con /
     * Quelle di primo livello che terminano con /...
     * Quelle di primo livello che non esistono in Anno
     */
    public void elaboraAnnoPagine(List<String> valideBase, List<String> pagine) {
        int voci;

        for (String wikiTitle : pagine) {
            // Quelle di primo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, 0, true);
                continue;
            }

            // Quelle di primo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, 0, true);
                continue;
            }

            // Quelle di primo livello che non esistono in Anno
            if (!valideBase.contains(wikiTitle)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, 0, true);
                continue;
            }

            voci = getVociAnno(wikiTitle);
            if (voci > 0) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoBase, voci, true);
            }
        }
    }


    /**
     * Quelle di secondo livello che terminano con /
     * Quelle di secondo livello che terminano con /...
     * Quelle di secondo livello che non hanno un corrispondente primo livello
     * Quelle di secondo livello che non superano le 50 voci
     */
    public void elaboraAnnoSottoPagine(List<String> valideBase, List<String> pagine) {
        int voci = 0;
        String paginaParentePrimoLivello;
        String mese;

        for (String wikiTitle : pagine) {
            // Quelle di secondo livello che terminano con /
            if (wikiTitle.endsWith(SLASH)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che terminano con /...
            if (wikiTitle.endsWith(SLASH + TRE_PUNTI)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non hanno un corrispondente primo livello
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            if (!valideBase.contains(paginaParentePrimoLivello)) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true);
                continue;
            }

            // Quelle di secondo livello che non superano le 50 voci
            mese = wikiTitle.substring(wikiTitle.indexOf(SLASH) + 1);
            voci = getVociAnno(paginaParentePrimoLivello, mese);
            if (voci > 50) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.annoSotto, voci, true);
            }
        }
    }

    /**
     * Pagine di attività da cancellare:
     */
    public void elaboraAttivita() {
        int nameSpace = 102;
        String tag = "Biografie/Attività/";
        List<String> pagineAll = queryService.getList(tag, nameSpace);
        List<String> valideBase = attivitaBackend.findAllPlurali();

//        elaboraAttivitaPagine(valideBase, getPagine(pagineAll));
//        elaboraAttivitaSottoPagine(valideBase, getSottoPagine(pagineAll));
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
            Attivita delta = attivitaBackend.findFirstBySingolare(paginaBase);
            Attivita delta2 = attivitaBackend.findFirstByPluraleLista(paginaBase);
            if (paginaBase.equals(gamma)) {
                voci = bioBackend.countAttivitaPlurale(paginaBase);
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaBase, voci, false);
                continue;
            }

            // Quelle di primo livello che non superano le 50 voci
            voci = bioBackend.countAttivitaPlurale(paginaBase);
            if (voci > 50) {
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
            if (voci > 50) {
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
        Pagina pagina;
        String attivita;
        String nazionalita;

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
            pagina = findByPagina(paginaParenteSecondoLivello);



            nazionalita = paginaParenteSecondoLivello.substring(paginaParenteSecondoLivello.lastIndexOf(SLASH) + 1);
            nazionalita = textService.primaMinuscola(nazionalita);
            voci = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita, letteraIniziale);
            if (voci > 50) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, false);
            }
            else {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, true);
            }



//            if (pagina == null) {
//                creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, true);
//            }
//            else {
//                if (pagina.cancella) {
//                    creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, true);
//                }
//                else {
//                    nazionalita = paginaParenteSecondoLivello.substring(paginaParenteSecondoLivello.lastIndexOf(SLASH) + 1);
//                    nazionalita = textService.primaMinuscola(nazionalita);
//                    voci = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita, letteraIniziale);
//                    if (voci > 50) {
//                        creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, false);
//                    }
//                    else {
//                        creaIfNotExist(wikiTitle, AETypePaginaCancellare.attivitaSottoSotto, voci, true);
//                    }
//                }
//            }
        }
    }

    /**
     * Pagine di nazionalità da cancellare:
     */
    public void elaboraNazionalita() {
        int nameSpace = 102;
        String tag = "Biografie/Nazionalità/";
        List<String> pagineAll = queryService.getList(tag, nameSpace);
        List<String> valideBase = nazionalitaBackend.findAllPlurali();

//        elaboraNazionalitaPagine(valideBase, getPagine(pagineAll));
//        elaboraNazionalitaSottoPagine(valideBase, getSottoPagine(pagineAll));
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
                voci = bioBackend.countNazionalitaPlurale(paginaBase);
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaBase, voci, false);
                continue;
            }

            // Quelle di primo livello che non superano le 50 voci
            voci = bioBackend.countNazionalitaPlurale(paginaBase);
            if (voci > 50) {
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
            if (voci > 50) {
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
        Pagina pagina;
        String attivita;
        String nazionalita;

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
            pagina = findByPagina(paginaParenteSecondoLivello);
            if (pagina == null) {
                creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, true);
            }
            else {
                if (pagina.cancella) {
                    creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, true);
                }
                else {
                    attivita = paginaParenteSecondoLivello.substring(paginaParenteSecondoLivello.lastIndexOf(SLASH) + 1);
                    attivita = textService.primaMinuscola(attivita);
                    voci = bioBackend.countNazionalitaAttivitaAll(nazionalita, attivita, letteraIniziale);
                    if (voci > 50) {
                        creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, false);
                    }
                    else {
                        creaIfNotExist(wikiTitle, AETypePaginaCancellare.nazionalitaSottoSotto, voci, true);
                    }
                }
            }
        }
    }

    public void elaboraUtenteBot() {
        int nameSpace = 2;
        String tagNati = "Biobot/Nati";
        String tagMorti = "Biobot/Morti";
        List<String> pagine;

        pagine = queryService.getList(tagNati, nameSpace);
        for (String wikiTitle : pagine) {
            creaIfNotExist(wikiTitle, AETypePaginaCancellare.bioBotNati, 0, true);
        }
        pagine = queryService.getList(tagMorti, nameSpace);
        for (String wikiTitle : pagine) {
            creaIfNotExist(wikiTitle, AETypePaginaCancellare.bioBotMorti, 0, true);
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

    public List<String> getLivello(List<String> pagine, int occorrenze) {
        List<String> primoLivello;

        primoLivello = pagine
                .stream()
                .filter(pagina -> regexService.count(pagina, SLASH) == occorrenze)
                .collect(Collectors.toList());

        return primoLivello;
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
                voci = bioBackend.countAllGiornoNatoSecolo(nomeGiorno, nomeSecolo);
            }
        }
        if (wikiTitle.startsWith("Morti")) {
            if (textService.isEmpty(nomeSecolo)) {
                voci = bioBackend.countGiornoMorto(nomeGiorno);
            }
            else {
                voci = bioBackend.countAllAnnoMortoMese(nomeGiorno, nomeSecolo);
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
                voci = bioBackend.countAllAnnoNatoMese(nomeAnno, nomeMese);
            }
        }
        if (wikiTitle.startsWith("Morti")) {
            if (textService.isEmpty(nomeMese)) {
                voci = bioBackend.countAnnoMorto(nomeAnno);
            }
            else {
                voci = bioBackend.countAllAnnoMortoMese(nomeAnno, nomeMese);
            }
        }

        return voci;
    }

}// end of crud backend class
