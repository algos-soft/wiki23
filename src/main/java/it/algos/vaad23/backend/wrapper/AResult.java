package it.algos.vaad23.backend.wrapper;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.service.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 27-nov-2020
 * Time: 14:31
 * <p>
 * Semplice wrapper per veicolare una risposta con diverse property <br>
 */
@Component
public class AResult {

    private boolean valido;

    //    private String webTitle = VUOTA;
    //
    //    private String wikiTitle = VUOTA;
    //
    //    private String urlPreliminary = VUOTA;
    //
    //    private String urlRequest = VUOTA;

    private String target = VUOTA;

    private String type = VUOTA;

    private String method = VUOTA;

    private String errorCode = VUOTA;

    private String errorMessage = VUOTA;

    private String codeMessage = VUOTA;

    private String validMessage = VUOTA;

    //    private String preliminaryResponse = VUOTA;

    //    private String response = VUOTA;

    //    private String wikiText = VUOTA;

    //    private String wikiBio = VUOTA;

    //    private String token = VUOTA;

    //    private String queryType = VUOTA;

    private int intValue = 0;

    private long longValue = 0;

    private String txtValue = VUOTA;

    private List lista = null;

    private Map mappa = null;

    protected AResult() {
        this(true, VUOTA);
    }

    protected AResult(final boolean valido, final String message) {
        this(valido, message, 0);
    }

    protected AResult(final boolean valido, final String message, final int intValue) {
        this.valido = valido;
        if (valido) {
            this.validMessage = message;
        }
        else {
            this.errorMessage = message;
        }
        this.intValue = intValue;
    }

    public static AResult build() {
        return new AResult();
    }

    public static AResult valido() {
        return AResult.build();
    }

    public AResult validMessage(final String validMessage) {
        this.valido = true;
        this.validMessage = validMessage;
        return this;
    }

    public AResult errorMessage(final String errorMessage) {
        this.valido = false;
        this.errorMessage = errorMessage;
        return this;
    }

    public AResult method(final String method) {
        this.method = method;
        return this;
    }

    public AResult target(final String target) {
        this.target = target;
        return this;
    }

    public AResult type(final String type) {
        this.type = type;
        return this;
    }


    public static AResult valido(final String validMessage) {
        return new AResult(true, validMessage);
    }

    public static AResult valido(final String validMessage, final int value) {
        return new AResult(true, validMessage, value);
    }

    public static AResult contenuto(final String text, final String source) {
        AResult result = new AResult();

        if (text != null && text.length() > 0) {
            result.setValido(true);
            //            result.setResponse(text);
            result.setValidMessage(JSON_SUCCESS);
        }
        else {
            result.setValido(false);
        }

        return result;
    }

    public static AResult contenuto(final String text) {
        return contenuto(text, VUOTA);
    }

    public static AResult errato() {
        return new AResult(false, "Non effettuato");
    }

    public static AResult errato(final int valore) {
        return new AResult(false, VUOTA, valore);
    }

    public static AResult errato(final String errorMessage) {
        AResult result = new AResult(false, errorMessage);
        result.setErrorCode(errorMessage);
        return result;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(final boolean valido) {
        this.valido = valido;
    }

    public boolean isErrato() {
        return !valido;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public void setCodeMessage(String codeMessage) {
        this.codeMessage = codeMessage;
    }

    public String getMessage() {
        return isValido() ? getValidMessage() : getErrorMessage();
    }

    public void setMessage(final String message) {
        if (isValido()) {
            setValidMessage(message);
        }
        else {
            setErrorMessage(message);
        }
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        this.setValido(false);
    }

    public AResult setErrorMessage(final String message) {
        errorMessage = message;
        this.setValido(false);
        return this;
    }

    public AResult setValidMessage(final String message) {
        validMessage = message;
        this.setValido(true);
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getValidMessage() {
        return validMessage;
    }

    //    public String getWebTitle() {
    //        return webTitle;
    //    }
    //
    //    public void setWebTitle(String webTitle) {
    //        this.webTitle = webTitle;
    //    }
    //
    //    public String getWikiTitle() {
    //        return wikiTitle;
    //    }
    //
    //    public void setWikiTitle(String wikiTitle) {
    //        this.wikiTitle = wikiTitle;
    //    }

    //    public String getResponse() {
    //        return response;
    //    }
    //
    //    public void setResponse(String response) {
    //        this.response = response;
    //    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public String getTxtValue() {
        return txtValue;
    }

    public void setTxtValue(String txtValue) {
        this.txtValue = txtValue;
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }

    //    public String getUrlPreliminary() {
    //        return urlPreliminary;
    //    }
    //
    //    public void setUrlPreliminary(String urlPreliminary) {
    //        this.urlPreliminary = urlPreliminary;
    //    }
    //
    //    public String getUrlRequest() {
    //        return urlRequest;
    //    }
    //
    //    public void setUrlRequest(String urlRequest) {
    //        this.urlRequest = urlRequest;
    //    }

    public Map getMappa() {
        return mappa;
    }

    public void setMappa(Map mappa) {
        this.mappa = mappa;
    }

    //    public String getPreliminaryResponse() {
    //        return preliminaryResponse;
    //    }
    //
    //    public void setPreliminaryResponse(String preliminaryResponse) {
    //        this.preliminaryResponse = preliminaryResponse;
    //    }
    //
    //    public String getToken() {
    //        return token;
    //    }
    //
    //    public void setToken(String token) {
    //        this.token = token;
    //    }
    //
    //    public String getQueryType() {
    //        return queryType;
    //    }
    //
    //    public void setQueryType(String queryType) {
    //        this.queryType = queryType;
    //    }
    //
    //    public String getWikiText() {
    //        return wikiText;
    //    }
    //
    //    public void setWikiText(String wikiText) {
    //        this.wikiText = wikiText;
    //    }
    //
    //    public String getWikiBio() {
    //        return wikiBio;
    //    }
    //
    //    public void setWikiBio(String wikiBio) {
    //        this.wikiBio = wikiBio;
    //    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void print(final LogService logger, final AETypeLog typeLog) {
        if (isValido()) {
            //            logger.log(typeLog, getValidMessage()); @todo rimettere
        }
        else {
            //            logger.log(typeLog, getErrorMessage()); @todo rimettere
        }
    }


}
