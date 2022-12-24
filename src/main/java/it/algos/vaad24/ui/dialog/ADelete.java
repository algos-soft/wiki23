package it.algos.vaad24.ui.dialog;

import static it.algos.vaad24.backend.boot.VaadCost.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Sat, 24-Dec-2022
 * Time: 10:00
 */
public class ADelete extends AConfirm {

    public static final String IRREVERSIBILE = ASpan.text("L'operazione è irreversibile").blue().bold().get();

    public static void delete(final Runnable confirmHandler) {
        ADelete.delete(VUOTA, confirmHandler);
    }

    public static void delete(final String entityName, final Runnable confirmHandler) {
        StringBuffer buffer = new StringBuffer();

        if (entityName == null || entityName.length() < 1) {
            buffer.append("Cancella dal database la singola entity");
        }
        else {
            buffer.append("Cancella dal database ");
            buffer.append(ASpan.text(entityName).verde().bold().get());
        }

        buffer.append(CAPO_HTML);
        buffer.append(ASpan.text("Vuoi veramente cancellare questo elemento ?").rosso().bold().get());
        buffer.append(CAPO_HTML);
        buffer.append(IRREVERSIBILE);

        AConfirm.title("Delete").message(buffer.toString()).confirmError("Delete").annullaPrimary().open(confirmHandler);
    }

    public static void deleteAll(final Runnable confirmHandler) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Cancella dal database tutti i valori della collezione");
        buffer.append(CAPO_HTML);
        buffer.append(ASpan.text("Sei sicuro di volerli cancellare tutti ?").rosso().bold().get());
        buffer.append(CAPO_HTML);
        buffer.append(IRREVERSIBILE);

        AConfirm.title("Delete all").message(buffer.toString()).confirmError("Delete all").annullaPrimary().open(confirmHandler);
    }


}
