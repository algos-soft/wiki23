package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 11-mag-2022
 * Time: 06:48
 * Query per recuperare una lista di pageIds di una categoria wiki <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce una lista di pageIds <br>
 * <p>
 * Ripete la request finché riceve un valore valido di cmcontinue <br>
 * La query restituisce SOLO pageIds <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryCat extends AQuery {


    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param catTitle della categoria wiki (necessita di codifica) usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final String catTitle) {
        queryType = AETypeQuery.getCookies;
        return requestGet(QUERY_CAT_REQUEST, catTitle);
    }

}
