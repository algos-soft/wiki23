package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import static it.algos.wiki23.backend.service.WikiApiService.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 19-lug-2021
 * Time: 11:58
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AJSonService.class); <br>
 * 3) @Autowired public AJSonService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class JSonService extends AbstractService {

    //    /**
    //     * Crea una mappa standard (valori reali) da una singola page JSON di una multi-pagina action=query <br>
    //     *
    //     * @param paginaJSON in ingresso
    //     *
    //     * @return mappa query (valori reali)
    //     */
    //    public HashMap<String, Object> getMappaJSON(JSONObject paginaJSON) {
    //        HashMap<String, Object> mappa = new HashMap<String, Object>();
    //        HashMap<String, Object> mappaRev;
    //        JSONArray arrayRev;
    //        String keyPage;
    //        Object value;
    //
    //        if (paginaJSON == null) {
    //            return null;
    //        }
    //
    //        //--recupera i valori dei parametri info
    //        for (PagePar par : PagePar.getRead()) {
    //            keyPage = par.toString();
    //            if (paginaJSON.get(keyPage) != null) {
    //                value = paginaJSON.get(keyPage);
    //                mappa.put(keyPage, value);
    //            }
    //        }
    //
    //        //--recupera i valori dei parametri revisions
    //        arrayRev = (JSONArray) paginaJSON.get(REVISIONS);
    //        if (arrayRev != null) {
    //            mappaRev = estraeMappaJsonPar(arrayRev);
    //            for (String key : mappaRev.keySet()) {
    //                value = mappaRev.get(key);
    //                mappa.put(key, value);
    //            }
    //        }
    //
    //        return mappa;
    //    }


    /**
     * Estrae una mappa standard da un JSONArray
     * Considera SOLO i valori della Enumeration PagePar
     *
     * @param arrayJson JSONArray in ingresso
     *
     * @return mappa standard (valori String)
     */
    private HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson) {
//        return estraeMappaJsonPar(arrayJson, 0);
        return null;
    }

    //    /**
    //     * Estrae una mappa standard da un JSONArray
    //     * Considera SOLO i valori della Enumeration PagePar
    //     *
    //     * @param arrayJson JSONArray in ingresso
    //     * @param pos       elemento da estrarre
    //     *
    //     * @return mappa standard (valori String)
    //     */
    //    public HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson, int pos) {
    //        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
    //        JSONObject mappaJSON = null;
    //        String key;
    //        Object value;
    //        String slots = "slots";
    //        String main = "main";
    //        String contentName = "content";
    //        String content = VUOTA;
    //        JSONObject slotsObject = null;
    //        JSONObject mainObject = null;
    //        JSONObject contentObject = null;
    //
    //        if (arrayJson != null && arrayJson.size() > pos) {
    //            if (arrayJson.get(pos) != null && arrayJson.get(pos) instanceof JSONObject) {
    //                mappaJSON = (JSONObject) arrayJson.get(pos);
    //            }
    //        }
    //
    //        if (mappaJSON != null) {
    //            for (PagePar par : PagePar.getRead()) {
    //                key = par.toString();
    //                if (mappaJSON.get(key) != null) {
    //                    value = mappaJSON.get(key);
    //                    mappaOut.put(key, value);
    //                }
    //            }
    //        }
    //
    //        //--content
    //        if (mappaJSON.get(slots) != null) {
    //            slotsObject = (JSONObject) mappaJSON.get(slots);
    //            mainObject = (JSONObject) slotsObject.get(main);
    //            mappaOut.put(contentName, mainObject.get(contentName));
    //        }
    //
    //        return mappaOut;
    //    }

    //    /**
    //     * Recupera le 'pagine' dal testo JSON di risposta ad una query <br>
    //     *
    //     * @param rispostaDellaQuery in ingresso
    //     *
    //     * @return array di 'pagine''
    //     */
    //    public JSONArray getJsonPagine(String rispostaDellaQuery) {
    //        JSONArray jsonPagine = null;
    //        JSONObject objectQuery = getPages(rispostaDellaQuery);
    //
    //        //--recupera i valori dei parametri
    //        if (objectQuery != null && objectQuery.get(CATEGORY) != null && objectQuery.get(CATEGORY) instanceof JSONArray) {
    //            jsonPagine = (JSONArray) objectQuery.get(CATEGORY);
    //        }
    //
    //        return jsonPagine;
    //    }

        /**
         * Recupera l'oggetto 'pages'' dal testo JSON di una pagina action=query
         *
         * @param rispostaDellaQuery in ingresso
         *
         * @return oggetto 'pages'
         */
        public JSONObject getPages(String rispostaDellaQuery) {
            JSONObject objectQuery = null;
            JSONObject objectAll = getObjectJSON(rispostaDellaQuery);

            //--recupera i valori dei parametri pages
            if (objectAll != null && objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
                objectQuery = (JSONObject) objectAll.get(QUERY);
            }

            return objectQuery;
        }

    /**
     * Costruisce l'oggetto (mappa) principale JSON dal testo di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return oggetto JSON
     */
    public JSONObject getObjectJSON(String rispostaDellaQuery) {
        return textService.isValid(rispostaDellaQuery) ? (JSONObject) JSONValue.parse(rispostaDellaQuery) : new JSONObject();
    }

    /**
     * Esamina quanti elementi contiene la mappa principale JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return oggetto JSON
     */
    public int getJSONSize(String rispostaDellaQuery) {
        JSONObject objectAll = getObjectJSON(rispostaDellaQuery);
        return objectAll != null ? objectAll.size() : 0;
    }

    /**
     * Recupera un oggetto JSON dalla mappa principale JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     * @param tagJSON            chiave della mappa principale JSON
     *
     * @return oggetto JSON
     */
    public JSONObject getObj(String rispostaDellaQuery, String tagJSON) {
        JSONObject objectJson = new JSONObject();
        JSONObject objectAll = getObjectJSON(rispostaDellaQuery);

        if (textService.isEmpty(rispostaDellaQuery) || textService.isEmpty(tagJSON)) {
            return objectJson;
        }

        if (objectAll != null && objectAll.get(tagJSON) != null) {
            objectJson = (JSONObject) objectAll.get(tagJSON);
        }

        return objectJson;
    }

    /**
     * Recupera un array di pages dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return array di pages
     */
    public JSONArray getArrayPagine(String rispostaDellaQuery) {
        JSONArray arrayQuery = null;
        JSONObject objectQuery = getPages(rispostaDellaQuery);

        //--recupera i valori dei parametri pages
        if (objectQuery != null && objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
            arrayQuery = (JSONArray) objectQuery.get(PAGES);
        }

        return arrayQuery;
    }


    /**
     * Recupera una singola page dal testo JSON di risposta ad una query <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return singola page (la prima)
     */
    public JSONObject getObjectPage(String rispostaDellaQuery) {
        JSONObject objectPage = null;
        JSONArray arrayPagine = this.getArrayPagine(rispostaDellaQuery);

        if (arrayPagine != null) {
            objectPage = (JSONObject) arrayPagine.get(0);
        }

        return objectPage;
    }


}