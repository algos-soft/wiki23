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

    private String text;

    private Notification.Position posizione = POSIZIONE;

    private NotificationVariant themeVariant = null;

    private int durata = DURATA;

    public static void show(String text) {
         Notification.show(text, DURATA, POSIZIONE);
    }


//    public static Notification show1000(String text) {
//        return Notification.show(text, 1000, Notification.Position.BOTTOM_START);
//    }
//
//    public static Notification show2000(String text) {
//        return Notification.show(text, 2000, Notification.Position.BOTTOM_START);
//    }
//
//    public static Notification show3000(String text) {
//        return Notification.show(text, 3000, Notification.Position.BOTTOM_START);
//    }

    //    public static Notification center(String text) {
    //        return Notification.show(text, durata(), Notification.Position.MIDDLE);
    //    }
    //
    //    public static Notification centerPrimary(String text) {
    //        Notification notification = Notification.show(text, durata(), Notification.Position.MIDDLE);
    //        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    //        return notification;
    //    }
    //
    //    public static Notification centerSuccess(String text) {
    //        Notification notification = Notification.show(text, durata(), Notification.Position.MIDDLE);
    //        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    //        return notification;
    //    }

    //    public static Notification centerError(String text) {
    //        Notification notification = Notification.show(text, durata(), Notification.Position.MIDDLE);
    //        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    //        return notification;
    //    }


    public static void open(String text) {
         Notification.show(text, DURATA, POSIZIONE);
    }

    public static Avviso text(String text) {
        Avviso avviso = new Avviso();

        avviso.text = text;
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
            Notification.show(text, durata(), posizione);
        }
        else {
            Notification.show(text, durata(), posizione).addThemeVariants(themeVariant);
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
