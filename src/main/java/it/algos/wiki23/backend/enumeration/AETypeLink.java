package it.algos.wiki23.backend.enumeration;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.interfaces.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 28-Jun-2022
 * Time: 07:57
 */
public enum AETypeLink implements AITypePref {
    voce, lista, pagina, nessuno;

    public static List<AETypeLink> getAllEnums() {
        return Arrays.stream(values()).toList();
    }

    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola, viene il valore corrente <br>
     *
     * @return stringa di valori e valore di default
     */
    @Override
    public String getPref() {
        StringBuffer buffer = new StringBuffer();

        getAllEnums().forEach(level -> buffer.append(level.name() + VIRGOLA));

        buffer.delete(buffer.length() - 1, buffer.length());
        buffer.append(PUNTO_VIRGOLA);
        buffer.append(name());

        return buffer.toString();
    }

    @Override
    public AITypePref get(String nome) {
        return getAllEnums()
                .stream()
                .filter(type -> type.name().equals(nome))
                .findAny()
                .orElse(null);
    }

}
