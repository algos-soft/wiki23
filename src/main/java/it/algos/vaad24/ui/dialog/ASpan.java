package it.algos.vaad24.ui.dialog;

import com.vaadin.flow.component.html.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Thu, 22-Dec-2022
 * Time: 17:47
 */
public class ASpan extends Span {

    private String text;

    private AETypeColor color;

    private AEFontWeight weight;

    private AEFontSize fontSize;

    private AEFontStyle style;

    private AELineHeight lineHeight;


    public static ASpan text(String text) {
        ASpan span = new ASpan();
        span.setText(text);
        span.getElement().setProperty(TAG_INNER_HTML, text);
        span.getElement().getStyle().set(TAG_HTML_FONT_SIZE, AEFontSize.normal.getTag());
        span.getElement().getStyle().set(TAG_HTML_LINE_HEIGHT, AELineHeight.em12.getTag());
        return span;
    }

    public ASpan rosso() {
        this.color = AETypeColor.rosso;
        return color(color);
    }

    public ASpan verde() {
        this.color = AETypeColor.verde;
        return color(color);
    }

    public ASpan blue() {
        this.color = AETypeColor.blue;
        return color(color);
    }

    public ASpan color(AETypeColor typeColor) {
        this.color = typeColor;
        this.getElement().getStyle().set(TAG_HTML_COLOR, color.getTag());
        return this;
    }

    public ASpan bold() {
        this.weight = AEFontWeight.bold;
        return weight(weight);
    }

    public ASpan weight(AEFontWeight typeWeight) {
        this.weight = typeWeight;
        this.getElement().getStyle().set(TAG_HTML_FONT_WEIGHT, weight.getTag());
        return this;
    }

    public ASpan small() {
        this.fontSize = AEFontSize.em7;
        this.getElement().getStyle().set(TAG_HTML_LINE_HEIGHT, AELineHeight.em12.getTag());
        return size(fontSize);
    }

    public ASpan big() {
        this.fontSize = AEFontSize.em9;
        return size(fontSize);
    }

    public ASpan size(AEFontSize typeSize) {
        this.fontSize = typeSize;
        this.getElement().getStyle().set(TAG_HTML_FONT_SIZE, fontSize.getTag());
        return this;
    }

    public ASpan italic() {
        this.style = AEFontStyle.italic;
        return stile(style);
    }

    public ASpan stile(AEFontStyle typeStyle) {
        this.style = typeStyle;
        this.getElement().getStyle().set(TAG_HTML_FONT_STYLE, style.getTag());
        return this;
    }

    public String get() {
        return this.getElement().toString();
    }

    public String getPar() {
        return "<p>" + this.get() + "</p>";
    }

}
