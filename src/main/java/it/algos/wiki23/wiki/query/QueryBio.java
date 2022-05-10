package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.interfaces.*;
import it.algos.vaad23.backend.service.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.service.*;
import static it.algos.wiki23.backend.service.WikiBotService.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29-apr-2022
 * Time: 19:42
 * UrlRequest:
 * urlDomain = "&rvslots=main&prop=revisions&rvprop=content|ids|timestamp"
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryBio extends AQuery {


    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final String wikiTitleGrezzo) {
        return requestGet(WIKI_QUERY_BASE_TITLE, wikiTitleGrezzo);
    }

    /**
     * Wrap della pagina biografica <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return wrap della pagina
     */
    public WrapBio getWrap(final String wikiTitleGrezzo) {
        return urlRequest(wikiTitleGrezzo).getWrap();
    }

    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param pageid della pagina wiki usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final long pageid) {
        return requestGet(WIKI_QUERY_BASE_PAGE, pageid);
    }

    /**
     * Wrap della pagina biografica <br>
     *
     * @param pageid della pagina wiki usato nella urlRequest
     *
     * @return wrap della pagina
     */
    public WrapBio getWrap(final long pageid) {
        return urlRequest(pageid).getWrap();
    }

}
