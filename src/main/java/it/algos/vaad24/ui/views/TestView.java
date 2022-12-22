package it.algos.vaad24.ui.views;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.confirmdialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.ui.dialog.*;

import javax.annotation.*;


/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Thu, 22-Dec-2022
 * Time: 09:04
 */
@SpringComponent
@PageTitle("Test")
@Route(value = TAG_TEST, layout = MainLayout.class)
//@RouteAlias(value = TAG_ROUTE_ALIAS_PARTE_PER_PRIMA, layout = MainLayout.class)
public class TestView extends VerticalLayout {

    @PostConstruct
    protected void postConstruct() {
        initView();
    }

    /**
     * Qui va tutta la logica iniziale della view principale <br>
     */
    protected void initView() {
        this.setMargin(true);
        this.setPadding(false);
        this.setSpacing(false);

        this.titolo();
        this.paragrafoNota();
        this.paragrafoAvviso();
        this.paragrafoConferma();
    }

    public void titolo() {
        H1 titolo = new H1("Pagina di test");
        titolo.getElement().getStyle().set("color", "green");
        this.add(titolo);
    }

    public void paragrafoNota() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout barra = new HorizontalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo = new H3("Nota");
        paragrafo.getElement().getStyle().set("color", "blue");
        Span span;
        String message;

        layout.add(new Label("Label di testo semplice (vert)"));
        layout.add(new Span("Span di testo semplice (vert)"));

        span = ASpan.getSpan("Prova iniziale span con <b>grassetto</b>");
        barra.add(span);
        barra.add(new Span("Span di testo semplice (oriz)"));

        //        message="Prova iniziale span con <b>grassetto</b>";
        //        span.getElement().setProperty("innerHTML", message);
        //        barra.add(span);

        this.add(paragrafo);
        this.add(layout);
        this.add(barra);
    }

    public void paragrafoAvviso() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout barra = new HorizontalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo = new H3("Avviso");
        paragrafo.getElement().getStyle().set("color", "blue");

        layout.add(new Label("Visualizzazione un dialogo di un avviso. Usa la classe Avviso con metodi statici. Posizione di default in basso a sinistra. Tempo di default di 2 secondi."));

        Button bottone = new Button("Base");
        bottone.addClickListener(event -> avviso());
        barra.add(bottone);

        Button bottone2 = new Button("Primary");
        bottone2.getElement().setAttribute("theme", "primary");
        bottone2.addClickListener(event -> avvisoPrimary());
        barra.add(bottone2);

        Button bottone3 = new Button("Successo");
        bottone3.getElement().setAttribute("theme", "success");
        bottone3.addClickListener(event -> avvisoSuccess());
        barra.add(bottone3);

        Button bottone4 = new Button("Contrasto");
        bottone4.getElement().setAttribute("theme", "contrast");
        bottone4.addClickListener(event -> avvisoContrasto());
        barra.add(bottone4);

        Button bottone5 = new Button("Errore");
        bottone5.getElement().setAttribute("theme", "error");
        bottone5.addClickListener(event -> avvisoError());
        barra.add(bottone5);

        Button bottone6 = new Button("Più lungo");
        bottone6.getElement().setAttribute("theme", "primary");
        bottone6.addClickListener(event -> avvisoLungo());
        barra.add(bottone6);

        Button bottone7 = new Button("Centrato");
        bottone7.addClickListener(event -> avvisoCentrato());
        barra.add(bottone7);

        Button bottone8 = new Button("Centrato primary");
        bottone8.getElement().setAttribute("theme", "primary");
        bottone8.addClickListener(event -> avvisoCentratoPrimary());
        barra.add(bottone8);

        Button bottone9 = new Button("Centrato successo");
        bottone9.getElement().setAttribute("theme", "success");
        bottone9.addClickListener(event -> avvisoCentratoSuccess());
        barra.add(bottone9);

        Button bottone10 = new Button("Centrato contrasto");
        bottone10.getElement().setAttribute("theme", "contrast");
        bottone10.addClickListener(event -> avvisoCentratoContrasto());
        barra.add(bottone10);

        Button bottone11 = new Button("Centrato errore");
        bottone11.getElement().setAttribute("theme", "error");
        bottone11.addClickListener(event -> avvisoCentratoErrore());
        barra.add(bottone11);

        this.add(paragrafo);
        layout.add(barra);
        this.add(layout);
    }

    public void avviso() {
        Avviso.open("Avviso semplice");
    }

    public void avvisoPrimary() {
        Avviso.text("Avviso primario").primary().open();
    }

    public void avvisoSuccess() {
        Avviso.text("Avviso positivo").success().open();
    }

    public void avvisoContrasto() {
        Avviso.text("Avviso di contrasto").contrast().open();
    }

    public void avvisoError() {
        Avviso.text("Avviso di errore").error().open();
    }

    public void avvisoLungo() {
        Avviso.text("Avviso lungo (3 secondi invece di 2)").durata(3).open();
    }

    public void avvisoCentrato() {
        Avviso.text("Posizione centrale base").middle().open();
    }

    public void avvisoCentratoPrimary() {
        Avviso.text("Posizione centrale primary").primary().middle().open();
    }

    public void avvisoCentratoSuccess() {
        Avviso.text("Posizione centrale successo").success().middle().open();
    }

    public void avvisoCentratoContrasto() {
        Avviso.text("Posizione centrale contrasto").contrast().middle().open();
    }

    public void avvisoCentratoErrore() {
        Avviso.text("Posizione centrale errore").error().middle().open();
    }


    public void paragrafoConferma() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout barra = new HorizontalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo = new H3("Conferma");
        paragrafo.getElement().getStyle().set("color", "blue");

        layout.add(new Label("Visualizzazione di un dialogo di conferma"));

        Button bottone = new Button("Conferma");
        bottone.getElement().setAttribute("theme", "primary");
        bottone.addClickListener(event -> conferma());
        barra.add(bottone);

        Button bottone2 = new Button("Conferma+Annulla");
        bottone2.getElement().setAttribute("theme", "primary");
        bottone2.addClickListener(event -> confermaAnnulla());
        barra.add(bottone2);

        this.add(paragrafo);
        layout.add(barra);
        this.add(layout);
    }

    public void conferma() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setText("Prova");
        dialog.open();
    }

    public void confermaAnnulla() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete \"Report Q4\"?");
        dialog.setText("Are you sure you want to permanently delete this item");

        dialog.setCancelable(true);
        //        dialog.addCancelListener(event -> setStatus("Canceled"));

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        //        dialog.addConfirmListener(event -> setStatus("Deleted"));
        dialog.open();
    }

}
