package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 29-Sep-2022
 * Time: 09:40
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryInfo extends AQuery {

    public int getLength(final String wikiTitoloGrezzoPagina) {
        int length = 0;
        Long longValue;
        WResult result = urlRequest(wikiTitoloGrezzoPagina);

        if (result.isValido()) {
            longValue = result.getLongValue();
            if (longValue > 0) {
                length = longValue.intValue();
            }
        }

        return length;
    }

    public String getTouched(final String wikiTitoloGrezzoPagina) {
        String touched = VUOTA;
        WResult result = urlRequest(wikiTitoloGrezzoPagina);

        if (result.isValido()) {
            touched = result.getNewtimestamp();
        }

        return touched;
    }

    public LocalDateTime getLast(final String wikiTitoloGrezzoPagina) {
        LocalDateTime lastTime = ROOT_DATA_TIME;
        String touched = getTouched(wikiTitoloGrezzoPagina);

        if (textService.isValid(touched)) {
            touched = textService.levaCoda(touched, "Z");
            lastTime = LocalDateTime.parse(touched);
        }

        return lastTime;
    }

    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param wikiTitoloGrezzoPagina della pagina wiki (necessita di codifica) usato nella urlRequest.
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final String wikiTitoloGrezzoPagina) {
        queryType = AETypeQuery.getSenzaLoginSenzaCookies;
        WResult result = requestGetTitle(WIKI_QUERY_INFO, wikiTitoloGrezzoPagina);

        return result;
    }


    @Override
    protected WResult elaboraResponse(WResult result, String rispostaDellaQuery) {
        List<String> listaNew = new ArrayList<>();
        List<String> listaOld;
        String title;
        JSONArray jsonPages;
        result = super.elaboraResponse(result, rispostaDellaQuery);

        if (result.isErrato()) {
            return result;
        }

        if (mappaUrlResponse.get(KEY_JSON_ZERO) != null && mappaUrlResponse.get(KEY_JSON_ZERO) instanceof JSONObject jsonQuery) {
            if (jsonQuery.get(KEY_JSON_TOUCHED) != null) {
                result.setNewtimestamp((String) jsonQuery.get(KEY_JSON_TOUCHED));
            }
            if (jsonQuery.get(KEY_JSON_LENGTH) != null) {
                result.setLongValue((Long) jsonQuery.get(KEY_JSON_LENGTH));
            }
        }

        return result;
    }

}
