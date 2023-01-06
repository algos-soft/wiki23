package it.algos.vaad24.backend.enumeration;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 21-gen-2021
 * Time: 14:10
 */
public enum AECopy {

    fileDelete("Sovrascrive sempre il file anche se esiste", AECopyType.file),
    fileOnly("Copia il file solo se non esisteva", AECopyType.file),
    fileCheck("Controlla un flag iniziale", AECopyType.file),
    elaboraFile("Elaborazione specifica del file", AECopyType.elaboraFile),

    dirDelete("Cancella sempre la vecchia cartella e poi ricopia tutto.", AECopyType.directory),
    dirOnly("Se esiste già, non fa nulla. Se non esiste, crea la cartella e il suo contenuto.", AECopyType.directory),
    dirFilesAddOnly("Aggiunge directories e files senza modificare o cancellare quelli esistenti.", AECopyType.directory),
    dirFilesModifica("Aggiunge directories e files e modifica quelli esistenti.", AECopyType.directory),
    dirFilesModificaToken("Aggiunge e modifica dirs/files con check del token.", AECopyType.directory),
    elaboraDir("Elaborazione specifica della directory", AECopyType.elaboraDir),

    sourceSovrascriveSempreAncheSeEsiste("", AECopyType.source),
    sourceSoloSeNonEsiste("", AECopyType.source),
    sourceCheckFlagSeEsiste("", AECopyType.source),
    ;

    private String descrizione;

    private AECopyType type;

    /**
     * Costruttore <br>
     */
    AECopy(String descrizione, AECopyType type) {
        this.descrizione = descrizione;
        this.type = type;
    }

    public static List<AECopy> getAllEnums() {
        return Arrays.stream(values()).toList();
    }

    public AECopyType getType() {
        return type;
    }

    public String getDescrizione() {
        return descrizione;
    }
}

