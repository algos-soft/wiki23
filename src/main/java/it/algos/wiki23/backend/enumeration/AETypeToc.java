package it.algos.wiki23.backend.enumeration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 01-Jul-2022
 * Time: 12:21
 */
public enum AETypeToc {

    noToc("__NOTOC__"),
    forceToc("__FORCETOC__"),
    toc("__TOC__");

    private final String tag;

    AETypeToc(final String tag) {
        this.tag = tag;
    }

    public String get() {
        return tag;
    }
}
