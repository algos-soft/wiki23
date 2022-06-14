package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 10:56
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ABioService.class); <br>
 * 3) @Autowired public ABioService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BioService extends WAbstractService {

    @Autowired
    public BioRepository repository;

    public static Function<Bio, String> forzaOrdinamento = bio -> bio.getOrdinamento() != null ? bio.getOrdinamento() : VUOTA;

    public static Function<Bio, String> cognome = bio -> bio.getCognome() != null ? bio.getCognome() : VUOTA;

    public static Function<Bio, String> wikiTitle = bio -> bio.getWikiTitle() != null ? bio.getWikiTitle() : VUOTA;

    public static Function<Bio, String> nazionalita = bio -> bio.getNazionalita() != null ? bio.getNazionalita() : VUOTA;


    /**
     * Estrae una mappa chiave/valore dal testo contenuto tutto in una riga <br>
     * Presuppone che la riga sia unica e i parametri siano separati da pipe <br>
     *
     * @param testo
     *
     * @return mappa chiave/valore
     */
    public static LinkedHashMap getMappaRigaUnica(String testo) {
        LinkedHashMap mappa = null;
        boolean continua = false;
        String sepRE = "\\|";
        String[] righe;
        String chiave;
        String valore;
        int pos;

        if (!testo.equals(VUOTA)) {
            continua = true;
        }

        if (continua) {
            righe = testo.split(sepRE);
            if (righe != null && righe.length > 0) {
                mappa = new LinkedHashMap();

                for (String stringa : righe) {
                    pos = stringa.indexOf(UGUALE_SEMPLICE);
                    if (pos != -1) {
                        chiave = stringa.substring(0, pos).trim();
                        valore = stringa.substring(pos + 1).trim();
                        if (!chiave.equals(VUOTA)) {
                            mappa.put(chiave, valore);
                        }
                    }
                }
            }
        }

        return mappa;
    }

    /**
     * Estrae una mappa chiave-valore dal testo del template <br>
     * Presuppone che le righe siano separate da pipe e return
     * Controlla che non ci siano doppie graffe annidate nel valore dei parametri
     *
     * @param bio da cui estrarre il tmplBio
     *
     * @return mappa chiave-valore
     */
    public LinkedHashMap<String, String> estraeMappa(final Bio bio) {
        return estraeMappa(bio.getTmplBio());
    }

    /**
     * Estrae una mappa chiave-valore dal testo del template <br>
     * Presuppone che le righe siano separate da pipe e return
     * Controlla che non ci siano doppie graffe annidate nel valore dei parametri
     *
     * @param tmplBioServer testo completo del template
     *
     * @return mappa chiave-valore
     */
    public LinkedHashMap<String, String> estraeMappa(final String tmplBioServer) {
        String tmplBio = tmplBioServer;
        LinkedHashMap<String, String> mappa = null;
        LinkedHashMap mappaGraffe = null;
        boolean continua = true;
        String sepRE = "\n\\|";
        String[] righe = null;
        String chiave;
        String valore;
        int pos;

        if (textService.isEmpty(tmplBioServer)) {
            return null;
        }

        if (tmplBio.startsWith(DOPPIE_GRAFFE_INI) && tmplBio.endsWith(DOPPIE_GRAFFE_END)) {
            tmplBio = textService.setNoDoppieGraffe(tmplBio);
        }

        if (continua) {
            mappaGraffe = checkGraffe(tmplBio);
            if (mappaGraffe.containsKey(KEY_MAP_GRAFFE_ESISTONO)) {
                //                testoTemplate = (String) mappaGraffe.get(KEY_MAP_GRAFFE_TESTO); //@todo non chiaro
            }
        }

        if (continua) {
            if (tmplBio.startsWith(PIPE)) {
                tmplBio = tmplBio.substring(1).trim();
            }

            righe = tmplBio.split(sepRE);
            if (righe.length == 1) {
                mappa = getMappaRigaUnica(tmplBio);
                continua = false;
            }
        }

        if (continua) {
            if (righe != null) {
                mappa = new LinkedHashMap();

                for (String stringa : righe) {
                    pos = stringa.indexOf(UGUALE_SEMPLICE);
                    if (pos != -1) {
                        chiave = stringa.substring(0, pos).trim();
                        valore = stringa.substring(pos + 1).trim();
                        if (!chiave.equals("")) {
                            mappa.put(chiave, valore);
                        }
                    }
                }
            }
        }

        // reinserisce il contenuto del parametro che eventualmente avesse avuto le doppie graffe
        if (continua) {
            if (mappaGraffe.containsKey(KEY_MAP_GRAFFE_ESISTONO)) {
                if ((Integer) mappaGraffe.get(KEY_MAP_GRAFFE_NUMERO) == 1) {
                    chiave = (String) mappaGraffe.get(KEY_MAP_GRAFFE_NOME_PARAMETRO);
                    valore = (String) mappaGraffe.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                    mappa.put(chiave, valore);
                }
                else {
                    for (int k = 0; k < (Integer) mappaGraffe.get(KEY_MAP_GRAFFE_NUMERO); k++) {
                        //                        chiave = mappaGraffe.nomeParGraffe[k];
                        //                        valore = mappaGraffe.valParGraffe[k];
                        //                        mappa.put(chiave, valore);
                    }
                }
            }
        }

        return mappa;
    }

    /**
     * Controlla le graffe interne al testo
     * <p>
     * Casi da controllare (all'interno delle graffe principali, già eliminate):
     * 1-...{{..}}...                   (singola)
     * 2-...{{}}...                     (vuota)
     * 3-{{..}}...                      (iniziale)
     * 3-...{{..}}                      (terminale)
     * 4-...{{..{{...}}...}}...         (interna)
     * 5-...{{..}}...{{...}}...         (doppie)
     * 6-...{{..}}..{{..}}..{{...}}...  (tre o più)
     * 7-...{{..}}..|..{{..}}...        (due in punti diversi)
     * 8-...{{...|...}}...              (pipe interno)
     * 8-...{{...|...|...}}...          (doppio pipe)
     * 7-...{{..|...}}..|..{{..}}...    (due pipe in due graffe)
     * 9-...{{....                      (singola apertura)
     * 10-...}}....                     (singola chiusura)
     * <p>
     * Se una o più graffe esistono, restituisce:
     * <p>
     * keyMapGraffeEsistono = se esistono                                       (boolean)
     * keyMapGraffeNumero = quante ce ne sono                                   (integer)
     * keyMapGraffeTestoPrecedente = testo che precede la prima graffa o testo originale se non ce ne sono
     * keyMapGraffeListaWrapper = lista di WrapTreStringhe coi seguenti dati:   (list<WrapTreStringhe>)
     * - prima = valore del contenuto delle graffe                              (stringa)
     * - seconda = nome del parametro interessato                               (stringa)
     * - terza = valore completo del parametro che le contiene                  (stringa)
     *
     * @param testoTemplate da analizzare
     *
     * @return mappa di risultati
     */
    public LinkedHashMap checkGraffe(String testoTemplate) {
        LinkedHashMap mappa = null;
        boolean continua = false;
        String tagIni = "{{";
        String tagEnd = "}}";
        int numIni = textService.getNumTag(testoTemplate, DOPPIE_GRAFFE_INI);
        int numEnd = textService.getNumTag(testoTemplate, DOPPIE_GRAFFE_END);

        mappa = new LinkedHashMap();
        mappa.put(KEY_MAP_GRAFFE_ESISTONO, false);
        mappa.put(KEY_MAP_GRAFFE_NUMERO, 0);
        mappa.put(KEY_MAP_GRAFFE_TESTO_PRECEDENTE, testoTemplate);
        mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, VUOTA);
        mappa.put(KEY_MAP_GRAFFE_NOME_PARAMETRO, VUOTA);
        mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, VUOTA);

        if (!testoTemplate.equals(VUOTA)) {
            continua = true;
        }

        // controllo di esistenza delle graffe
        if (continua) {
            if (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
                mappa.put(KEY_MAP_GRAFFE_ESISTONO, true);
            }
            else {
                continua = false;
            }
        }

        // spazzola il testo per ogni coppia di graffe
        if (continua) {
            if (numIni == numEnd) {
                while (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
                    testoTemplate = levaGraffa(mappa, testoTemplate);
                }
            }
            else {
            }
        }

        return mappa;
    }

    /**
     * Elabora ed elimina le prime graffe del testo
     * Regola la mappa
     * Restituisce il testo depurato delle prime graffe per ulteriore elaborazione
     *
     * @param testoTemplate testo completo del template
     */
    public String levaGraffa(HashMap mappa, String testoTemplate) {
        String testoElaborato = VUOTA;
        boolean continua = false;
        int posIni;
        int posEnd;
        String testoGraffa = VUOTA;

        if (mappa != null && !testoTemplate.equals("")) {
            testoElaborato = testoTemplate;
            continua = true;
        }

        // controllo di esistenza delle graffe
        if (continua) {
            if (testoTemplate.contains(DOPPIE_GRAFFE_INI) && testoTemplate.contains(DOPPIE_GRAFFE_END)) {
            }
            else {
                continua = false;
            }
        }

        // controllo (non si sa mai) che le graffe siano nell'ordine giusto
        if (continua) {
            posIni = testoTemplate.indexOf(DOPPIE_GRAFFE_INI);
            posEnd = testoTemplate.indexOf(DOPPIE_GRAFFE_END);
            if (posIni > posEnd) {
                continua = false;
            }
        }

        //spazzola il testo fino a pareggiare le graffe
        if (continua) {
            posIni = testoTemplate.indexOf(DOPPIE_GRAFFE_INI);
            posEnd = testoTemplate.indexOf(DOPPIE_GRAFFE_END, posIni);
            testoGraffa = testoTemplate.substring(posIni, posEnd + DOPPIE_GRAFFE_END.length());
            while (!textService.isPariGraffe(testoGraffa)) {
                posEnd = testoTemplate.indexOf(DOPPIE_GRAFFE_END, posEnd + DOPPIE_GRAFFE_END.length());
                if (posEnd != -1) {
                    testoGraffa = testoTemplate.substring(posIni, posEnd + DOPPIE_GRAFFE_END.length());
                }
                else {
                    mappa.put(KEY_MAP_GRAFFE_ESISTONO, false);
                    break;
                }
            }
        }

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            testoElaborato = regolaMappa(mappa, testoTemplate, testoGraffa);
        }

        return testoElaborato;
    }


    /**
     * Elabora il testo della singola graffa
     * Regola la mappa
     */
    public String regolaMappa(HashMap mappa, String testoTemplate, String testoGraffa) {
        String testoElaborato = VUOTA;
        boolean continua = false;
        ArrayList arrayValGraffe;
        ArrayList arrayNomeParGraffe;
        ArrayList arrayvValParGraffe;
        String valParGraffe = VUOTA;
        String nomeParGraffe = VUOTA;
        String valRiga;
        int posIni = 0;
        int posEnd = 0;
        String sep2 = "\n|";
        int numGraffe;

        // controllo di congruità
        if (mappa != null && !testoTemplate.equals(VUOTA) && !testoGraffa.equals(VUOTA)) {
            testoElaborato = textService.sostituisce(testoTemplate, testoGraffa, VUOTA);
            continua = true;
        }

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            posIni = testoTemplate.indexOf(testoGraffa);
            posIni = testoTemplate.lastIndexOf(sep2, posIni);
            posIni += sep2.length();
            posEnd = testoTemplate.indexOf(sep2, posIni + testoGraffa.length());
            if (posIni == -1) {
                continua = false;
            }
            if (posEnd == -1) {
                posEnd = testoTemplate.length();
            }
        }

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            valRiga = testoTemplate.substring(posIni, posEnd);
            posIni = valRiga.indexOf(UGUALE_SEMPLICE);
            //nomeParGraffe = valRiga.substring(0, posIni).trim()
            //valParGraffe = valRiga.substring(posIni + sepParti.length()).trim()
            if (posIni != -1) {
                nomeParGraffe = valRiga.substring(0, posIni).trim();
                valParGraffe = valRiga.substring(posIni + UGUALE_SEMPLICE.length()).trim();
            }
            else {
                continua = false;
            }
        }

        numGraffe = mappa.get(KEY_MAP_GRAFFE_NUMERO) != null ? (Integer) mappa.get(KEY_MAP_GRAFFE_NUMERO) : 0;
        numGraffe++;
        switch (numGraffe) {
            case 1:
                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, testoGraffa);
                mappa.put(KEY_MAP_GRAFFE_NOME_PARAMETRO, nomeParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, valParGraffe);
                break;
            case 2:
                arrayValGraffe = new ArrayList();
                String oldValGraffe;
                oldValGraffe = (String) mappa.get(KEY_MAP_GRAFFE_VALORE_CONTENUTO);
                arrayValGraffe.add(oldValGraffe);
                arrayValGraffe.add(testoGraffa);
                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, arrayValGraffe);

                arrayNomeParGraffe = new ArrayList();
                String oldNomeParGraffe;
                oldNomeParGraffe = (String) mappa.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                arrayNomeParGraffe.add(oldNomeParGraffe);
                arrayNomeParGraffe.add(nomeParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, arrayNomeParGraffe);

                //                arrayvValParGraffe = new ArrayList();
                //                String oldValParGraffe;
                //                arrayValGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_CONTENUTO);
                //                arrayvValParGraffe.add(oldValParGraffe);
                //                arrayvValParGraffe.add(valParGraffe);
                //                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, arrayvValParGraffe);
                break;
            default: // caso non definito
                arrayValGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_CONTENUTO);
                arrayValGraffe.add(testoGraffa);
                mappa.put(KEY_MAP_GRAFFE_VALORE_CONTENUTO, arrayValGraffe);

                arrayNomeParGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                arrayNomeParGraffe.add(nomeParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, arrayNomeParGraffe);

                arrayvValParGraffe = (ArrayList) mappa.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO);
                arrayvValParGraffe.add(valParGraffe);
                mappa.put(KEY_MAP_GRAFFE_VALORE_PARAMETRO, arrayvValParGraffe);
                break;
        }

        mappa.put(KEY_MAP_GRAFFE_NUMERO, numGraffe);
        mappa.put(KEY_MAP_GRAFFE_TESTO_PRECEDENTE, testoElaborato);

        return testoElaborato;
    }

    /**
     * Gestisce l'eccezione per i giorni 7 e 11 di ogni mese <br>
     */
    public String wikiTitleNati(final AEntity entityBean) {
        return wikiTitle(entityBean, true);
    }

    /**
     * Gestisce l'eccezione per i giorni 7 e 11 di ogni mese <br>
     */
    public String wikiTitleMorti(final AEntity entityBean) {
        return wikiTitle(entityBean, false);
    }

    private String wikiTitle(final AEntity entityBean, final boolean natiVsMorti) {
        String wikiTitle = natiVsMorti ? "Nati" : "Morti";
        String titolo = VUOTA;

        //        if (entityBean instanceof Giorno giorno) {
        //            titolo = giorno.titolo;
        //        }
        //        if (entityBean instanceof Anno anno) {
        //            titolo = anno.titolo;
        //        }

        if (textService.isValid(titolo)) {
            wikiTitle += (titolo.startsWith("8") || titolo.startsWith("11")) ? " l'" : " il ";
            wikiTitle += titolo;
        }

        return wikiTitle;
    }


    /**
     * Cerca tutte le entities di una collection filtrate con una serie di attività. <br>
     * Selects documents in a collection or view and returns a list of the selected documents. <br>
     *
     * @param listaNomiSingoli per costruire la query
     *
     * @return lista di entityBeans ordinata per cognome
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    public List<Bio> fetchAttivita(List<String> listaNomiSingoli) {
        List<Bio> listaNonOrdinata = new ArrayList<>();
        List<Bio> lista1;
        List<Bio> lista2;
        List<Bio> lista3;

        if (listaNomiSingoli == null) {
            logger.info(new WrapLog().exception(new AlgosException("Non ci sono attività singole")).usaDb());
            return null;
        }

        for (String nomeAttivitaSingola : listaNomiSingoli) {
            lista1 = repository.findAllByAttivitaOrderByCognome(nomeAttivitaSingola);
            listaNonOrdinata.addAll(lista1);
            if (WPref.usaTreAttivita.is()) {
                lista2 = repository.findAllByAttivita2OrderByCognome(nomeAttivitaSingola);
                listaNonOrdinata.addAll(lista2);
                lista3 = repository.findAllByAttivita3OrderByCognome(nomeAttivitaSingola);
                listaNonOrdinata.addAll(lista3);
            }
        }

        return sortByForzaOrdinamento(listaNonOrdinata);
    }


    /**
     * Cerca tutte le entities di una collection filtrate con una serie di nazionalità. <br>
     * Selects documents in a collection or view and returns a list of the selected documents. <br>
     *
     * @param listaNomiSingoli per costruire la query
     *
     * @return lista di entityBeans ordinata per cognome
     *
     * @see(https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find/)
     */
    public List<Bio> fetchNazionalita(List<String> listaNomiSingoli) {
        List<Bio> listaNonOrdinata = new ArrayList<>();

        if (listaNomiSingoli == null) {
            logger.info(new WrapLog().exception(new AlgosException("Non ci sono nazionalità singole")).usaDb());
            return null;
        }

        for (String nomeNazionalitaSingola : listaNomiSingoli) {
            listaNonOrdinata.addAll(repository.findAllByNazionalitaOrderByCognome(nomeNazionalitaSingola));
        }

        return sortByForzaOrdinamento(listaNonOrdinata);
    }


    public List<Bio> sortByNazionalita(List<Bio> listaNonOrdinata) {
        List<Bio> sortedList = new ArrayList<>();
        List<Bio> listaConNazionalitaOrdinata = new ArrayList<>(); ;
        List<Bio> listaSenzaNazionalitaOrdinata = new ArrayList<>(); ;

        listaConNazionalitaOrdinata = listaNonOrdinata
                .stream()
                .filter(bio -> textService.isValid(bio.nazionalita))
                .sorted(Comparator.comparing(nazionalita))
                .collect(Collectors.toList());

        listaSenzaNazionalitaOrdinata = listaNonOrdinata
                .stream()
                .filter(bio -> textService.isEmpty(bio.nazionalita))
                .collect(Collectors.toList());

        sortedList.addAll(listaConNazionalitaOrdinata);
        sortedList.addAll(listaSenzaNazionalitaOrdinata);
        return sortedList;
    }

    public List<Bio> sortByForzaOrdinamento(List<Bio> listaNonOrdinata) {
        return listaNonOrdinata
                .stream()
                .sorted(Comparator.comparing(forzaOrdinamento))
                .collect(Collectors.toList());
    }


    public List<Bio> sortByCognome(List<Bio> listaNonOrdinata) {
        List<Bio> sortedList = new ArrayList<>();
        List<Bio> listaConCognomeOrdinata = new ArrayList<>(); ;
        List<Bio> listaSenzaCognomeOrdinata = new ArrayList<>(); ;

        listaConCognomeOrdinata = listaNonOrdinata
                .stream()
                .filter(bio -> bio.getCognome() != null)
                .sorted(Comparator.comparing(cognome))
                .collect(Collectors.toList());

        listaSenzaCognomeOrdinata = listaNonOrdinata
                .stream()
                .filter(bio -> bio.getCognome() == null)
                .sorted(Comparator.comparing(wikiTitle))
                .collect(Collectors.toList());

        sortedList.addAll(listaConCognomeOrdinata);
        sortedList.addAll(listaSenzaCognomeOrdinata);
        return sortedList;
    }

}