package it.algos.vaad24.ui.views;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.confirmdialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.vaad24.ui.dialog.*;
import org.springframework.beans.factory.annotation.*;

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
@RouteAlias(value = TAG_ROUTE_ALIAS_PARTE_PER_PRIMA, layout = MainLayout.class)
public class TestView extends VerticalLayout {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public HtmlService htmlService;

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
        this.paragrafoSpan();
        this.paragrafoAvviso();
        this.paragrafoConferma();
        this.paragrafoDelete();
        //        this.paragrafoConfermaAnnullaCancella();
    }


    public void titolo() {
        H2 titolo = new H2("Pagina di test");
        titolo.getElement().getStyle().set("color", "green");
        this.add(titolo);
    }

    public void paragrafoSpan() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout barra = new HorizontalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        barra.setMargin(false);
        barra.setPadding(false);
        barra.setSpacing(true);
        H3 paragrafo = new H3("ASpan");
        paragrafo.getElement().getStyle().set("color", "blue");

        layout.add(new Label("Label di testo semplice (non modificabile). Specifico per componenti e non per testi"));
        layout.add(ASpan.text("ASpan di testo semplice. -> ASpan.text(\"Un testo\")"));
        layout.add(ASpan.text("Testo html con inserti <span style=\"color:red;\">rosso</span> di sera e <span style=\"color:green;font-weight:bold\">verde</span> di mattina"));
        layout.add(ASpan.text("Verde bold small").verde().bold().small());
        layout.add(ASpan.text("Altro small").rosso().small());
        layout.add(ASpan.text("Blue big").blue().bold());
        layout.add(ASpan.text("Gli small funzionano solo nei layout verticali").verde().italic());

        barra.add(ASpan.text("-> ASpan.text(\"Un testo\").verde().bold().small().italic():"));
        barra.add(ASpan.text("Verde").verde().bold().italic());
        barra.add(ASpan.text("Altri ->"));
        barra.add(ASpan.text("Verde").verde());
        barra.add(ASpan.text("Blue").blue());
        barra.add(ASpan.text("Rosso").rosso());
        barra.add(ASpan.text("Italic").rosso().bold().italic());
        barra.add(ASpan.text("Bold").blue().bold());
        barra.add(ASpan.text("Normale").rosso().bold());
        barra.add(ASpan.text("Ancora").blue().bold().italic());//

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

        layout.add(new Label("Visualizzazione un dialogo di un avviso a tempo e senza bottoni. Usa la classe Avviso con metodi statici. Posizione di default in basso a sinistra. Tempo di default di 2 secondi."));

        Button bottone = new Button("Base");
        bottone.addClickListener(event -> Avviso.show("Avviso semplice"));
        barra.add(bottone);

        Button bottone2 = new Button("Primary");
        bottone2.getElement().setAttribute("theme", "primary");
        bottone2.addClickListener(event -> Avviso.message("Avviso primario").primary().open());
        barra.add(bottone2);

        Button bottone3 = new Button("Successo");
        bottone3.getElement().setAttribute("theme", "success");
        bottone3.addClickListener(event -> Avviso.message("Avviso positivo").success().open());
        barra.add(bottone3);

        Button bottone4 = new Button("Contrasto");
        bottone4.getElement().setAttribute("theme", "contrast");
        bottone4.addClickListener(event -> Avviso.message("Avviso di contrasto").contrast().open());
        barra.add(bottone4);

        Button bottone5 = new Button("Errore");
        bottone5.getElement().setAttribute("theme", "error");
        bottone5.addClickListener(event -> Avviso.message("Avviso di errore").error().open());
        barra.add(bottone5);

        Button bottone6 = new Button("Più lungo");
        bottone6.getElement().setAttribute("theme", "primary");
        bottone6.addClickListener(event -> Avviso.message("Avviso lungo (3 secondi invece di 2)").durata(3).open());
        barra.add(bottone6);

        Button bottone7 = new Button("Centrato");
        bottone7.addClickListener(event -> Avviso.message("Posizione centrale base").middle().open());
        barra.add(bottone7);

        Button bottone8 = new Button("Centrato primary");
        bottone8.getElement().setAttribute("theme", "primary");
        bottone8.addClickListener(event -> Avviso.message("Posizione centrale primary").primary().middle().open());
        barra.add(bottone8);

        Button bottone9 = new Button("Centrato successo");
        bottone9.getElement().setAttribute("theme", "success");
        bottone9.addClickListener(event -> Avviso.message("Posizione centrale successo").success().middle().open());
        barra.add(bottone9);

        Button bottone10 = new Button("Centrato contrasto");
        bottone10.getElement().setAttribute("theme", "contrast");
        bottone10.addClickListener(event -> Avviso.message("Posizione centrale contrasto").contrast().middle().open());
        barra.add(bottone10);

        Button bottone11 = new Button("Centrato errore");
        bottone11.getElement().setAttribute("theme", "error");
        bottone11.addClickListener(event -> Avviso.message("Posizione centrale errore").error().middle().open());
        barra.add(bottone11);

        this.add(paragrafo);
        layout.add(barra);
        this.add(layout);
    }


    public void paragrafoConferma() {
        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        HorizontalLayout barra = new HorizontalLayout();
        HorizontalLayout barra2 = new HorizontalLayout();
        HorizontalLayout barra3 = new HorizontalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout2.setMargin(false);
        layout2.setPadding(false);
        layout2.setSpacing(false);
        layout3.setMargin(false);
        layout3.setPadding(false);
        layout3.setSpacing(false);
        H3 paragrafo = new H3("Conferma");
        paragrafo.getElement().getStyle().set("color", "blue");

        layout.add(new Label("Visualizzazione di un dialogo di conferma con titolo, messaggio e un bottone"));

        Button bottone = new Button("Conferma");
        bottone.getElement().setAttribute("theme", "primary");
        bottone.addClickListener(event -> conferma());
        barra.add(bottone);

        Button bottone2 = new Button("Conferma");
        bottone2.getElement().setAttribute("theme", "primary");
        bottone2.addClickListener(event -> conferma2());
        barra.add(bottone2);

        Button bottone3 = new Button("Conferma");
        bottone3.getElement().setAttribute("theme", "primary");
        bottone3.addClickListener(event -> conferma3());
        barra.add(bottone3);

        Button bottone4 = new Button("Conferma");
        bottone4.getElement().setAttribute("theme", "primary");
        bottone4.addClickListener(event -> conferma4());
        barra.add(bottone4);

        Button bottone5 = new Button("Conferma");
        bottone5.getElement().setAttribute("theme", "primary");
        bottone5.addClickListener(event -> conferma5());
        barra.add(bottone5);
        layout.add(barra);

        layout2.add(new Label("Visualizzazione di un dialogo di conferma con titolo, messaggio e due bottoni. Conferma e annulla"));
        Button bottone6 = new Button("Conferma");
        bottone6.getElement().setAttribute("theme", "primary");
        bottone6.addClickListener(event -> conferma6());
        barra2.add(bottone6);
        layout2.add(barra2);

        layout3.add(new Label("Visualizzazione di un dialogo di conferma con titolo, messaggio e tre bottoni. Conferma, annulla e rifiuta"));
        Button bottone10 = new Button("Conferma");
        bottone10.getElement().setAttribute("theme", "primary");
        bottone10.addClickListener(event -> conferma8());
        barra3.add(bottone10);
        layout3.add(barra3);

        this.add(paragrafo);
        this.add(layout);
        this.add(layout2);
        this.add(layout3);
    }


    public void conferma() {
        String message = "Solo testo senza titolo.</br>Con show(Questo testo) si apre subito.</br>Oppure con html(Questo testo).</br>Testo sempre in formato html coi vari tag disponibili.";
        AConfirm.html(message);
    }

    public void conferma2() {
        String message = "Solo titolo. Dopo title(\"Un titolo\") si apre con open(). No html.";
        AConfirm.title(message).open();
    }

    public void conferma3() {
        String message = "Dopo title(\"Titolo\"),</br>message(\"Questo testo\")</br>si apre con open().";
        AConfirm.title("Titolo").message(message).open();
    }

    public void conferma4() {
        String message = "Dopo title(\"Salva\"),</br>message(\"Questo testo\")</br>confirm(\"Salva\")</br>si apre con open().";
        AConfirm.title("Salva").message(message).confirm("Salva").open();
    }

    public void conferma5() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Diverse righe formattate in html");
        buffer.append(CAPO_HTML);

        buffer.append(ASpan.text("Ogni riga resa diversamente").rosso().get());
        buffer.append(CAPO_HTML);
        buffer.append(ASpan.text("Con diversi stili").verde().bold().get());
        buffer.append(CAPO_HTML);
        buffer.append("Per ogni riga");
        buffer.append(CAPO_HTML);
        buffer.append("O anche <span style=\"color:green;\">misti</span> nella <span style=\"color:blue;font-weight:bold\">stessa</span> riga.");
        AConfirm.title("Salva").message(buffer.toString()).confirm("Salva").open();
    }

    public void conferma6() {
        String message = "Secondo bottone 'cancel'";
        AConfirm.title("Titolo").message(message).annulla().open();
    }

    public void conferma7() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Ripristina nel database i valori di default annullando le eventuali modifiche apportate successivamente");
        buffer.append(CAPO_HTML);
        buffer.append(ASpan.text("Sei sicuro di volerli cancellare tutti?").rosso().bold().get());
        buffer.append(CAPO_HTML);
        buffer.append(ASpan.text("L'operazione è irreversibile").blue().bold().get());
        AConfirm.title("Reset").message(buffer.toString()).confirmError("Reset").annullaPrimary().open();
    }

    public void conferma8() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Secondo bottone 'cancel' -> annulla");
        buffer.append(CAPO_HTML);
        buffer.append("Terzo bottone 'reject' -> rifiuta");
        buffer.append(CAPO_HTML);
        buffer.append("Se si usa rifiuta() per il terzo bottone, il secondo è automatico.");
        AConfirm.title("Titolo").message(buffer.toString()).rifiuta().open();
    }

    public void paragrafoDelete() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout barra = new HorizontalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo = new H3("Reset, Delete, DeleteAll");
        paragrafo.getElement().getStyle().set("color", "blue");

        layout.add(new Label("Visualizzazione di un dialogo di conferma per le operazioni di Reset, di Delete di una singola entity e di DeleteAll per tutta la collazione"));

        Button bottone7 = new Button("Reset");
        bottone7.getElement().setAttribute("theme", "error primary");
        bottone7.addClickListener(event -> AReset.reset(null));
        barra.add(bottone7);
        layout.add(barra);

        Button bottone8 = new Button("Delete");
        bottone8.getElement().setAttribute("theme", "error primary");
        bottone8.addClickListener(event -> ADelete.delete(null));
        barra.add(bottone8);
        layout.add(barra);

        Button bottone6 = new Button("Delete");
        bottone6.getElement().setAttribute("theme", "error primary");
        bottone6.addClickListener(event -> ADelete.delete("entityName", null));
        barra.add(bottone6);
        layout.add(barra);

        Button bottone9 = new Button("Delete all");
        bottone9.getElement().setAttribute("theme", "error primary");
        bottone9.addClickListener(event -> ADelete.deleteAll(null));
        barra.add(bottone9);
        layout.add(barra);

        this.add(paragrafo);
        this.add(layout);
    }

    public void confermaAnnullaCancella() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setText("Tre bottoni");
        dialog.open();
    }

    public Span getSpan(WrapSpan wrap) {
        int browserWidth = 270;

        if (wrap.getColor() == null) {
            wrap.color(AETypeColor.verde);
        }
        if (wrap.getWeight() == null) {
            wrap.weight(AEFontWeight.normal);
        }
        if (wrap.getFontHeight() == null) {
            if (browserWidth == 0 || browserWidth > 500) {
                wrap.fontHeight(AEFontSize.em9);
            }
            else {
                wrap.fontHeight(AEFontSize.em7);
            }
        }
        if (wrap.getLineHeight() == null) {
            if (browserWidth == 0 || browserWidth > 500) {
                wrap.lineHeight(AELineHeight.em12);
            }
            else {
                wrap.lineHeight(AELineHeight.em3);
            }
        }
        return htmlService.getSpan(wrap);
    }

}
