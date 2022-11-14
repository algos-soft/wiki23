package it.algos.wiki23.backend.packages.pagina;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.giorno.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.apache.commons.collections4.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

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

    @Override
    public List<Pagina> findAll() {
        return repository.findAll();
    }

    public int countCancellareByType(AETypePaginaCancellare type) {
        Long pagineLong = type != null ? repository.countByTypeAndCancella(type, true) : 0;
        return pagineLong.intValue();
    }

    public List<Pagina> findAllCancellareByType(AETypePaginaCancellare type) {
        return findAll().stream()
                .filter(pagina -> pagina.type == type)
                .filter(pagina -> pagina.cancella == true)
                .collect(Collectors.toList());
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        long inizio = System.currentTimeMillis();
        mongoService.deleteAll(Pagina.class);

        elaboraGiorni();
        elaboraAnni();
        elaboraAttivita();
        //        elaboraNazionalita();
        elaboraCognomi();
        elaboraUtenteBot();

        super.fixElaboraMinuti(inizio, "cancellazioni");
    }

    public List<Pagina> elaboraGiorni() {
        List<Pagina> pagineMongo = new ArrayList<>();
        List<String> valideMongoBase = giornoWikiBackend.findAllPagine();
        List<String> pagineServer = getPagineGiorni();

        pagineMongo.addAll(elaboraGiorniPagine(valideMongoBase, pagineServer));
        pagineMongo.addAll(elaboraGiorniSottoPagine(valideMongoBase, pagineServer));

        return pagineMongo;
    }

    /**
     * Quelle di primo livello che terminano con /
     * Quelle di primo livello che terminano con /...
     * Quelle di primo livello che non esistono in Giorno
     */
    public List<Pagina> elaboraGiorniPagine(List<String> valideMongoBase, List<String> pagineServer) {
        List<Pagina> pagineMongo = new ArrayList<>();
        List<String> paginePrimoLivello = this.getGiorniAnniPrimoLivello(pagineServer);
        int voci;

        for (String wikiTitle : paginePrimoLivello) {
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

            // i Redirect
            if (queryService.isRedirect(wikiTitle)) {
                pagineMongo.add(creaIfNotExist(wikiTitle, AETypePaginaCancellare.giornoRedirect, 0, false));
                continue;
            }

            // Quelle di primo livello che non esistono in Giorno
            if (!valideMongoBase.contains(wikiTitle)) {
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
    public List<Pagina> elaboraGiorniSottoPagine(List<String> valideMongoBase, List<String> pagineServer) {
        List<Pagina> pagineMongo = new ArrayList<>();
        List<String> pagineSecondoLivello = this.getGiorniAnniSotto(pagineServer);
        int voci = 0;
        String paginaParentePrimoLivello;
        String secolo;
        int sogliaMaxPagina = WPref.sogliaSottoPaginaGiorniAnni.getInt();

        for (String wikiTitle : pagineSecondoLivello) {
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
            if (!valideMongoBase.contains(paginaParentePrimoLivello)) {
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

        pagineMongo.addAll(elaboraAnniPagine(valideBase, getGiorniAnniPrimoLivello(pagineAll)));
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

        elaboraCognomiPagine(valideBase, getCognomi(pagineAll));
    }


    /**
     * Tutti i cognomi acritici
     */
    public ArrayList<String> getCognomiAcriticiAll(List<String> pagineGrezzeIndifferenziate) {
        ArrayList<String> pagineSporche = getCognomiDiacriticiAll(pagineGrezzeIndifferenziate);
        return new ArrayList<>((CollectionUtils.removeAll(pagineGrezzeIndifferenziate, pagineSporche)));
    }

    /**
     * Cognomi acritici senza corrispettivo diacritico
     */
    public ArrayList<String> getCognomiAcriticiSingoli(List<String> pagineGrezzeIndifferenziate) {
        ArrayList<String> pagineAcriticheAll = getCognomiAcriticiAll(pagineGrezzeIndifferenziate);
        ArrayList<String> pagineAcriticheDoppi = getCognomiAcriticiDoppi(pagineGrezzeIndifferenziate);
        return new ArrayList<>((CollectionUtils.removeAll(pagineAcriticheAll, pagineAcriticheDoppi)));
    }

    /**
     * Cognomi acritici col corrispettivo diacritico
     */
    public ArrayList<String> getCognomiAcriticiDoppi(List<String> pagineGrezzeIndifferenziate) {
        ArrayList<String> pagineDoppie = new ArrayList<>();
        ArrayList<String> pagineSporche = getCognomiDiacriticiAll(pagineGrezzeIndifferenziate);
        String paginaPulita;

        for (String paginaSporca : pagineSporche) {
            paginaPulita = wikiUtility.fixDiacritica(paginaSporca);

            if (pagineGrezzeIndifferenziate.contains(paginaPulita)) {
                pagineDoppie.add(paginaPulita);
            }
        }

        return pagineDoppie;
    }


    /**
     * Tutti i cognomi diacritici
     */
    public ArrayList<String> getCognomiDiacriticiAll(List<String> pagineGrezzeIndifferenziate) {
        ArrayList<String> pagineSporche = new ArrayList<>();

        for (String grezza : pagineGrezzeIndifferenziate) {
            if (wikiUtility.isDiacritica(grezza)) {
                pagineSporche.add(grezza);
            }
        }

        return pagineSporche;
    }


    /**
     * Cognomi diacritici senza corrispettivo acritico
     */
    public ArrayList<String> getCognomiDiacriticiSingoli(List<String> pagineGrezzeIndifferenziate) {
        ArrayList<String> pagineSingole = new ArrayList<>();

        ArrayList<String> pagineSporche = getCognomiDiacriticiAll(pagineGrezzeIndifferenziate);
        String paginaPulita;

        for (String paginaSporca : pagineSporche) {
            paginaPulita = wikiUtility.fixDiacritica(paginaSporca);

            if (!pagineGrezzeIndifferenziate.contains(paginaPulita)) {
                pagineSingole.add(paginaSporca);
            }
        }

        return pagineSingole;
    }

    /**
     * Cognomi diacritici col corrispettivo acritico
     */
    public ArrayList<String> getCognomiDiacriticiDoppi(List<String> pagineGrezzeIndifferenziate) {
        ArrayList<String> pagineDoppie = new ArrayList<>();
        ArrayList<String> pagineSporche = getCognomiDiacriticiAll(pagineGrezzeIndifferenziate);
        String paginaPulita;

        for (String paginaSporca : pagineSporche) {
            paginaPulita = wikiUtility.fixDiacritica(paginaSporca);

            if (pagineGrezzeIndifferenziate.contains(paginaPulita)) {
                pagineDoppie.add(paginaPulita);
            }
        }

        return pagineDoppie;
    }

    public void elaboraCognomiPagine(List<String> valideBase, List<String> pagine) {
        String tag = "Persone di cognome";
        String paginaBase;
        int voci = 0;
        int sogliaCognomi = WPref.sogliaCognomiWiki.getInt();
        sogliaCognomi = (sogliaCognomi * 8) / 10;
        List<String> cognomiAcriticiSingoli = this.getCognomiAcriticiSingoli(pagine);
        List<String> cognomiAcriticiDoppi = this.getCognomiAcriticiDoppi(pagine);
        List<String> cognomiDiacriticiSingoli = this.getCognomiDiacriticiSingoli(pagine);
        List<String> cognomiDiacriticiDoppi = this.getCognomiDiacriticiDoppi(pagine);
        //        AETypeCognome type;

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
            //            type = fixTypeCognome(cognomiAcritici, cognomiDiacritici, wikiTitle);
            //            switch (type) {
            //                case acriticoSingolo -> {
            //                    //nulla da fare
            //                }
            //                case acriticoDoppio -> {
            //                    //nulla da fare
            //                }
            //                case diacriticoSingolo -> {
            //                }
            //                case diacriticoDoppio -> {
            //                    //--c'è questo diacritico ed anche l'acritico (che può passare prima o dopo)
            //                }
            //                case nonSpecificato -> {
            //                    //nulla da fare
            //                }
            //                default -> {
            //                }
            //            } ;

            if (cognomiAcriticiSingoli.contains(wikiTitle)) {
                int a = 87;//non faccio nulla e proseguo
            }

            if (cognomiAcriticiDoppi.contains(wikiTitle)) {
                if (queryService.isRedirect(wikiTitle)) {
                    creaIfNotExist(wikiTitle, AETypePaginaCancellare.redirect, 0, false);
                    continue;
                }
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

    public AETypeCognome fixTypeCognome(List<String> cognomiAcritici, List<String> cognomiDiacritici, String cognomeBase) {
        AETypeCognome type = AETypeCognome.nonSpecificato;
        String cognomeAcriticizzato;

        //--cognome normale
        if (cognomiAcritici.contains(cognomeBase)) {
            if (cognomiDiacritici.contains(cognomeBase)) {
                return AETypeCognome.acriticoDoppio;
            }
            else {
                return AETypeCognome.acriticoSingolo;
            }
        }

        if (cognomiDiacritici.contains(cognomeBase)) {
            cognomeAcriticizzato = wikiUtility.fixDiacritica(cognomeBase);
            if (cognomiAcritici.contains(cognomeAcriticizzato)) {
                return AETypeCognome.diacriticoDoppio;
            }
            else {
                return AETypeCognome.diacriticoSingolo;
            }
        }

        return type;
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

    public int countGiorniPrimoLivelloErrati() {
        return countCancellareByType(AETypePaginaCancellare.giornoBase);
    }

    public int countGiorniSecondoLivelloErrati() {
        return countCancellareByType(AETypePaginaCancellare.giornoSotto);
    }

    public int countGiorniErrati() {
        return countGiorniPrimoLivelloErrati() + countGiorniSecondoLivelloErrati();
    }

    public List<String> getGiorniPrimoLivello() {
        List<String> pagineServer = this.getPagineGiorni();
        List<String> paginePrimoLivello = this.getGiorniAnniPrimoLivello(pagineServer);
        return paginePrimoLivello;
    }

    public List<String> getGiorniSecondoLivello() {
        List<String> pagineServer = this.getPagineGiorni();
        List<String> pagineSecondoLivello = this.getGiorniAnniSotto(pagineServer);
        return pagineSecondoLivello;
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

    public List<String> getGiorniAnniPrimoLivello(List<String> pagine) {
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
