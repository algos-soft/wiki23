package it.algos.vaad23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.regex.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Sat, 27-Aug-2022
 * Time: 17:03
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(RegexService.class); <br>
 * 3) @Autowired public RegexService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RegexService extends AbstractService {

    /**
     * Esistenza di un pattern in una stringa <br>
     *
     * @param textMatcher della stringa da spazzolare
     * @param textPattern da trovare
     *
     * @return vero se il pattern esiste nel matcher
     */
    public boolean isEsiste(final String textMatcher, final String textPattern) {
        Pattern pattern = Pattern.compile(textPattern);
        Matcher matcher = pattern.matcher(textMatcher);

        return matcher.find();
    }


    /**
     * Testo congruente al pattern ed effettivamente esistente nella stringa <br>
     *
     * @param textMatcher della stringa da spazzolare
     * @param textPattern da trovare
     *
     * @return pattern effettivo della stringa
     */
    public String getReal(final String textMatcher, final String textPattern) {
        String value = VUOTA;
        Pattern pattern;
        Matcher matcher;

        if (isEsiste(textMatcher, textPattern)) {
            pattern = Pattern.compile(textPattern);
            matcher = pattern.matcher(textMatcher);
            if (matcher.find()) {
                value = matcher.group(0);
            }
        }

        return value;
    }

    /**
     * Sostituisce il primo pattern della stringa col nuovo valore <br>
     *
     * @param textMatcher della stringa da spazzolare
     * @param textPattern da trovare
     * @param textNew     da sostituire
     *
     * @return vero se il pattern è stato sostituito nella stringa
     */
    public String replaceFirst(final String textMatcher, final String textPattern, final String textNew) {
        String textReplaced = textMatcher;
        String oldText = getReal(textMatcher, textPattern);

        if (textService.isValid(oldText)) {
            textReplaced = textMatcher.replaceFirst(textPattern, textNew);
        }

        return textReplaced;
    }

}