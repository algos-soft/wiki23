package it.algos.wiki23.backend.schedule;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.interfaces.*;
import it.algos.vaad24.backend.schedule.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import java.time.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 05-Jul-2022
 * Time: 14:06
 */
public abstract class WikiTask extends VaadTask {


    protected AIGenPref flagPrevisione;

    protected void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(1);
        if (flagPrevisione != null) {
            flagPrevisione.setValue(prossimo);
        }
    }

}
