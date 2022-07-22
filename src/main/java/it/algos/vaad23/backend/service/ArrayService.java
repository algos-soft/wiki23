package it.algos.vaad23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.functional.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;


/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: sab, 12-mar-2022
 * Time: 17:08
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ArrayService.class); <br>
 * 3) @Autowired public ArrayService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ArrayService extends AbstractService {

    /**
     * Crea un array di stringhe con singolo valore <br>
     *
     * @param singoloValore da inserire
     *
     * @return array di un solo elemento oppure vuoto
     *
     * @since Java 9
     */
    public List<String> creaArraySingolo(final String singoloValore) {
        return textService.isValid(singoloValore) ? List.of(singoloValore) : List.of();
    }


    /**
     * Crea una mappa con una singola coppia chiave-valore <br>
     * Entrambe le stringhe devono essere valide <br>
     *
     * @param key   della mappa
     * @param value della mappa
     *
     * @return mappa chiave-valore con un solo elemento
     *
     * @since Java 9
     */
    public Map<String, String> creaMappaSingola(final String key, final String value) {
        return (textService.isValid(key) && textService.isValid(value)) ? Map.of(key, value) : Map.of();
    }


    /**
     * Controlla la validità di un array (lista). <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (size maggiore di 0) <br>
     * Non ci devono essere elementi con valore null <br>
     * Non sono accettate le stringhe vuote -> "" <br>
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isAllValid(final List array) {
        return array != null && array.size() > 0 && array.stream().filter(APredicate.nonValido).count() == 0;
    }


    /**
     * Controlla la validità di una matrice. <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (length maggiore di 0) <br>
     * Non ci devono essere elementi con valore null <br>
     * Non sono accettate le stringhe vuote -> "" <br>
     *
     * @param matrice (String[]) in ingresso da controllare
     *
     * @return vero se la matrice soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isAllValid(final String[] matrice) {
        return matrice != null && matrice.length > 0 && Arrays.asList(matrice).stream().filter(APredicate.nonValido).count() == 0;
    }


    /**
     * Controlla la validità di una mappa. <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (size maggiore di 0) <br>
     * Non ci devono essere chiavi con valore null <br>
     * Non sono accettate le chiavi vuote -> "" <br>
     *
     * @param mappa (Map) in ingresso da controllare
     *
     * @return vero se la mappa soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isAllValid(final Map mappa) {
        return mappa != null && mappa.size() > 0 && mappa.keySet().stream().filter(APredicate.nonValido).count() == 0;
    }

    /**
     * Controlla che l'array sia nullo o vuoto <br>
     * Non deve esistere (null) <br>
     * Se esiste, non deve avere elementi (size = 0) <br>
     * Se ci sono elementi devono avere tutti valore null <br>
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isEmpty(final List array) {
        return array == null || array.size() == 0 || array.stream().filter(APredicate.valido).count() == 0;
    }


    /**
     * Controlla che la matrice sia nulla o vuota. <br>
     * Non deve esistere (null) <br>
     * Se esiste, non deve avere elementi (size = 0) <br>
     * Se ci sono elementi devono avere tutti valore null o vuoto -> "" <br>
     *
     * @param matrice (String[]) in ingresso da controllare
     *
     * @return vero se la matrice soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isEmpty(final String[] matrice) {
        return matrice == null || matrice.length == 0 || Arrays.asList(matrice).stream().filter(APredicate.valido).count() == 0;
    }


    /**
     * Controlla che la mappa sia nulla o vuota. <br>
     * Non deve esistere (null) <br>
     * Se esiste, non deve avere elementi (size = 0) <br>
     * Se ci sono elementi devono avere tutti la chiave null o vuota -> "" <br>
     *
     * @param mappa (Map) in ingresso da controllare
     *
     * @return vero se la mappa soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isEmpty(final Map mappa) {
        return mappa == null || mappa.size() == 0 || mappa.keySet().stream().filter(APredicate.valido).count() == 0;
    }

    /**
     * Controlla se una mappa può essere semplificata. <br>
     * <p>
     * Esamina una mappa del tipo Map<String, List<String>> e se tutte <br>
     * le liste hanno un solo valore, semplifica in Map<String, String> <br>
     * <p>
     * La mappa deve essere valida <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (size maggiore di 0) <br>
     * Ogni lista deve avere un solo valore <br>
     *
     * @param multiParametersMap mappa di liste di stringhe
     *
     * @return true se è semplificabile sostituendo le liste con un singole valore
     *
     * @since Java 11
     */
    public boolean isMappaSemplificabile(Map<String, List<String>> multiParametersMap) {
        return multiParametersMap != null && multiParametersMap.size() > 0 && multiParametersMap.values().stream().filter(n -> n.size() > 1).count() == 0;
    }


    /**
     * route costruisce uan view che implementa l'interfaccia HasUrlParameter <br>
     * e nel metodo setParameter() riceve @OptionalParameter parameters che <br>
     * sono del tipo Map<String, List<String>> <br>
     * Se tutte le keys delle liste hanno un solo valore,
     * si può semplificare in Map<String, String> <br>
     * <p>
     * Semplifica la mappa (se è semplificabile) <br>
     *
     * @param multiParametersMap mappa di liste di stringhe
     *
     * @return mappa semplificata
     */
    public Map<String, String> semplificaMappa(final Map<String, List<String>> multiParametersMap) {
        Map<String, String> mappaSemplice = new HashMap<>();

        if (!isMappaSemplificabile(multiParametersMap)) {
            return null;
        }

        //@todo Funzionalità ancora da implementare in Java11
        //        Object alfa = multiParametersMap
        //                .entrySet()
        //                .stream()
        ////                .filter(n -> n.size() > 1)
        //                .map(getValue -> AFunction.riduce)
        //        .collect(Collectors.toUnmodifiableList());
        ////                .collect(Collectors.toMap(getKey, Map.Entry::getValue);
        //        Object beta = alfa;
        for (Map.Entry<String, List<String>> entry : multiParametersMap.entrySet()) {
            if (entry.getValue().size() == 1) {
                mappaSemplice.put(entry.getKey(), entry.getValue().get(0));
            }
            else {
                //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
                //                log.error("Qualcosa non ha funzionato");
            }
        }

        return mappaSemplice;
    }

    /**
     * Crea una stringa con i valori dell'array separati da virgola <br>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori separati da virgola
     *
     * @since Java 9
     */
    public String toStringaVirgola(final List array) {
        return toStringaBase(array, VIRGOLA);
    }

    /**
     * Crea una stringa con i valori dell'array separati da virgola e spazio <br>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori separati da virgola
     *
     * @since Java 9
     */
    public String toStringaVirgolaSpazio(final List array) {
        return toStringaBase(array, VIRGOLA_SPAZIO);
    }

    /**
     * Costruisce una stringa con i singoli valori separati da un pipe <br>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori separati da pipe
     */
    public String toStringaPipe(final List array) {
        return toStringaBase(array, PIPE);
    }

    /**
     * Crea una stringa con i valori dell'array separati da un separatore <br>
     *
     * @param array lista di valori
     * @param sep   caratteri separatore
     *
     * @return stringa con i singoli valori separati da separatore
     *
     * @since Java 9
     */
    private String toStringaBase(final List array, final String sep) {
        StringBuffer buffer = new StringBuffer();

        if (array != null) {
            array.forEach(obj -> {
                {
                    buffer.append(sep);
                    buffer.append(obj);
                }
            });
            buffer.delete(0, sep.length());
        }

        return buffer.toString();
    }

    /**
     * Ordina la mappa secondo la chiave
     *
     * @param mappaDisordinata in ingresso
     *
     * @return mappa ordinata, null se mappaDisordinata è null
     */
    public Map sort(final Map mappaDisordinata) {
        LinkedHashMap mappaOrdinata = new LinkedHashMap();
        Object[] listaChiavi;

        if (!isAllValid(mappaDisordinata)) {
            return mappaDisordinata;
        }

        listaChiavi = mappaDisordinata.keySet().toArray();

        try {
            Arrays.sort(listaChiavi);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        for (Object chiave : listaChiavi) {
            mappaOrdinata.put(chiave, mappaDisordinata.get(chiave));
        }

        return mappaOrdinata;
    }


    /**
     * Ordina la mappa secondo la chiave
     *
     * @param mappaDisordinata in ingresso
     *
     * @return mappa ordinata, null se mappaDisordinata è null
     */
    public Map sortVuota(final Map mappaDisordinata) {
        LinkedHashMap mappaOrdinata = new LinkedHashMap();
        Set listaChiaviDisordinata;
        List<String> listaChiaviOrdinata = null;

        listaChiaviDisordinata = mappaDisordinata.keySet();
        try {
            listaChiaviOrdinata = listaChiaviDisordinata.stream().filter(c -> textService.isValid(c)).sorted().toList();
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        for (Object chiave : listaChiaviOrdinata) {
            mappaOrdinata.put(chiave, mappaDisordinata.get(chiave));
        }

        if (mappaDisordinata.keySet().contains(VUOTA)) {
            mappaOrdinata.put(VUOTA, mappaDisordinata.get(VUOTA));
        }

        return mappaOrdinata;
    }

    /**
     * Differenza di due array <br>
     *
     * @param arrayA lista di valori A
     * @param arrayB lista di valori B
     *
     * @return lista dei valori mancanti
     */
    public List<Integer> differenzaInt(final List<Integer> arrayA, final List<Integer> arrayB) {
        return arrayA.stream().filter(a -> !arrayB.contains(a)).toList();
    }

    /**
     * Differenza di due array <br>
     *
     * @param arrayA lista di valori A
     * @param arrayB lista di valori B
     *
     * @return lista dei valori mancanti
     */
    public List<Long> differenzaLong(final List<Long> arrayA, final List<Long> arrayB) {
        return arrayA.stream().filter(a -> !arrayB.contains(a)).toList();
    }

    public List<String> getList(String testo) {
        List<String> lista = null;
        String tag = VIRGOLA;
        String[] parti = null;

        if (textService.isValid(testo) && testo.contains(tag)) {
            parti = testo.split(tag);
        }

        if (parti != null && parti.length > 0) {
            lista = Arrays.asList(parti);
        }

        return lista;
    }

}