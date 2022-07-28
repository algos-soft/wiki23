package it.algos.wiki23.backend.enumeration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 17-Jul-2022
 * Time: 13:54
 */
public enum AETypeCrono {
    nascita("Nati", "nati"),
    morte("Morti", "morti"),
    ;

    private String tagLower;

    private String tagUpper;


    AETypeCrono(String tagUpper, String tagLower) {
        this.tagUpper = tagUpper;
        this.tagLower = tagLower;
    }

    public String getTagLower() {
        return tagLower;
    }

    public String getTagUpper() {
        return tagUpper;
    }
}
