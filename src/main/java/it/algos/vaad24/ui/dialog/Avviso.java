package it.algos.vaad24.ui.dialog;

import com.vaadin.flow.component.notification.*;
import it.algos.vaad24.backend.enumeration.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: mer, 30-mar-2022
 * Time: 08:24
 */
public class Avviso {

    public static final int DURATA = 2000;

    public static final Notification.Position POSIZIONE = Notification.Position.BOTTOM_START;

    private String message;

    private Notification.Position posizione = POSIZIONE;

    private NotificationVariant themeVariant = null;

    private int durata = DURATA;

    public static void show(String messaggioImmediatamenteVisibile) {
         Notification.show(messaggioImmediatamenteVisibile, DURATA, POSIZIONE);
    }


    public static Avviso message(String message) {
        Avviso avviso = new Avviso();
        avviso.message = message;
        return avviso;
    }

    public Avviso primary() {
        this.themeVariant = NotificationVariant.LUMO_PRIMARY;
        return this;
    }

    public Avviso success() {
        this.themeVariant = NotificationVariant.LUMO_SUCCESS;
        return this;
    }

    public Avviso contrast() {
        this.themeVariant = NotificationVariant.LUMO_CONTRAST;
        return this;
    }

    public Avviso error() {
        this.themeVariant = NotificationVariant.LUMO_ERROR;
        return this;
    }

    public Avviso durata(int secondi) {
        this.durata = secondi * 1000;
        return this;
    }

    public Avviso middle() {
        this.posizione = Notification.Position.MIDDLE;
        return this;
    }

    public void open() {
        if (themeVariant == null) {
            Notification.show(message, durata(), posizione);
        }
        else {
            Notification.show(message, durata(), posizione).addThemeVariants(themeVariant);
        }
    }


    public int durata() {
        if (durata == 0) {
            durata = Pref.durataAvviso.getInt();
        }
        if (durata == 0) {
            durata = DURATA;
        }

        return durata;
    }

}
