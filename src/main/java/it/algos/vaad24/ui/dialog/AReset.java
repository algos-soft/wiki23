package it.algos.vaad24.ui.dialog;

import static it.algos.vaad24.backend.boot.VaadCost.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Sat, 24-Dec-2022
 * Time: 09:20
 */
public class AReset extends AConfirm {

    public static void reset(final Runnable confirmHandler) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Ripristina nel database i valori di default annullando le eventuali modifiche apportate successivamente");
        buffer.append(CAPO_HTML);
        buffer.append(ASpan.text("Sei sicuro di volerli cancellare tutti ?").rosso().bold().get());
        buffer.append(CAPO_HTML);
        buffer.append(ASpan.text("L'operazione è irreversibile").blue().bold().get());

        AConfirm.title("Reset").message(buffer.toString()).confirmError("Reset").annullaPrimary().open(confirmHandler);
    }

}
