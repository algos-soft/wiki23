package it.algos.vaad24.backend.enumeration;

import it.algos.vaad24.backend.interfaces.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 29-set-2020
 * Time: 14:34
 */
public enum AEContinente implements AIType {

    europa("Europa", true),
    asia("Asia", true),
    africa("Africa", true),
    nordamerica("Nordamerica", true),
    sudamerica("Sudamerica", true),
    oceania("Oceania", true),
    antartide("Antartide", false),
    ;

    private String tag;

    boolean abitato;


    AEContinente(String tag, boolean abitato) {
        this.tag = tag;
        this.abitato = abitato;
    }

    public static List<AEContinente> getAllEnums() {
        return Arrays.stream(values()).toList();
    }

    public static List<String> getAllTags() {
        List<String> listaTags = new ArrayList<>();

        getAllEnums().forEach(type -> listaTags.add(type.getTag()));
        return listaTags;
    }

    public int getOrd() {
        return this.ordinal() + 1;
    }


    public boolean isAbitato() {
        return abitato;
    }


    @Override
    public List<AEContinente> getAll() {
        return Arrays.stream(values()).toList();
    }

    @Override
    public String getTag() {
        return tag;
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return getTag();
    }
}
