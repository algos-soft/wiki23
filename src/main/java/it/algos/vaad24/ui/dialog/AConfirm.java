package it.algos.vaad24.ui.dialog;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.confirmdialog.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Fri, 23-Dec-2022
 * Time: 18:09
 */
public class AConfirm extends ConfirmDialog {

    protected Runnable confermaHandler;

    protected Runnable annullaHandler;


    public AConfirm() {
        this.setConfirmText(BUTTON_CONFERMA);
        this.setCancelText(BUTTON_CANCELLA);
        this.setRejectText(BUTTON_REJECT);
    }

    public static void html(String html) {
        AConfirm dialog = new AConfirm();
        dialog.setText(new Html("<p>" + html + "</p>"));
        dialog.open();
    }

    public static void show(String message) {
        AConfirm dialog = new AConfirm();
        dialog.setText(new Html("<p>" + message + "</p>"));
        dialog.open();
    }

    public static AConfirm title(String title) {
        AConfirm dialog = new AConfirm();
        dialog.setHeader(title);
        return dialog;
    }

    public AConfirm message(String message) {
        this.setText(new Html("<p>" + message + "</p>"));
        return this;
    }

    public AConfirm confirm(String confirmButtonText) {
        this.setConfirmText(confirmButtonText);
        return this;
    }

    public AConfirm confirmError() {
        this.setConfirmButtonTheme("error primary");
        return this;
    }

    public AConfirm confirmError(String confirmButtonText) {
        this.setConfirmButtonTheme("error primary");
        this.setConfirmText(confirmButtonText);
        return this;
    }

    public AConfirm annulla() {
        this.setCancelable(true);
        return this;
    }

    public AConfirm annullaPrimary() {
        this.setCancelButtonTheme("primary");
        return annulla();
    }


    public AConfirm rifiuta() {
        this.setCancelable(true);
        this.setRejectable(true);
        return this;
    }

    public AConfirm open(final Runnable confermaHandler) {
        this.confermaHandler = confermaHandler;
        this.addConfirmListener(event -> confermaHandler());
        this.addCancelListener(event -> Avviso.message("Annullato").primary().open());
        super.open();
        return this;
    }

    public AConfirm open(final Runnable confermaHandler,final Runnable annullaHandler) {
        this.confermaHandler = confermaHandler;
        this.annullaHandler = annullaHandler;
        this.addConfirmListener(event -> confermaHandler());
        this.addCancelListener(event -> annullaHandler());
        super.open();
        return this;
    }

    public void confermaHandler() {
        if (confermaHandler != null) {
            confermaHandler.run();
        }
        close();
    }


    public void annullaHandler() {
        if (annullaHandler != null) {
            annullaHandler.run();
        }
        close();
        Avviso.message("Annullato").open();
    }

}
