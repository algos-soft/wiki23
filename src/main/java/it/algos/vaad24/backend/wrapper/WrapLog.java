package it.algos.vaad24.backend.wrapper;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 21-mar-2022
 * Time: 14:08
 * Wrapper di informazioni (Fluent API) per accedere al logger <br>
 * Può contenere:
 * -type (AETypeLog) merceologico per il log
 * -message (String)
 * -errore (AlgosException) con StackTrace per recuperare classe, metodo e riga di partenza
 * -wrapCompany (WrapLogCompany) per recuperare companySigla, userName e addressIP se l'applicazione è multiCompany
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WrapLog {

    private AETypeLog type;

    private String message;

    private AlgosException exception;

    private String companySigla;

    private String userName;

    private String addressIP;

    private boolean multiCompany = false;

    private boolean usaDB = false;

    private boolean usaMail = false;

    private boolean usaStackTrace = false;

    public WrapLog() {
    }


    public static WrapLog build() {
        return new WrapLog();
    }

    public WrapLog message(String message) {
        this.message = message;
        return this;
    }

    public WrapLog type(AETypeLog type) {
        this.type = type;
        return this;
    }

    public WrapLog exception(Exception exception) {
        this.exception = new AlgosException(exception);
        this.usaStackTrace = true;
        return this;
    }

    public WrapLog exception(AlgosException exception) {
        this.exception = exception;
        this.usaStackTrace = true;
        return this;
    }

    public WrapLog company(String companySigla) {
        this.companySigla = companySigla;
        this.multiCompany = true;
        return this;
    }

    public WrapLog user(String userName) {
        this.userName = userName;
        this.multiCompany = true;
        return this;
    }

    public WrapLog address(String addressIP) {
        this.addressIP = addressIP;
        this.multiCompany = true;
        return this;
    }

    public WrapLog usaDb() {
        this.usaDB = true;
        return this;
    }

    public WrapLog usaMail() {
        this.usaMail = true;
        return this;
    }

    public AETypeLog getType() {
        return type;
    }

    public String getMessage() {
        return (message != null && message.length() > 0) ? message : exception != null ? exception.getMessage() : VUOTA;
    }

    /**
     * Per il database registra entrambi i messaggi, se esistono <br>
     */
    public String getMessageDB() {
        StringBuffer buffer = new StringBuffer();
        String messageErrore = exception != null ? exception.getMessage() : VUOTA;
        String messageSpecifico = (message != null && message.length() > 0) ? message : VUOTA;

        buffer.append(messageErrore);
        if (messageErrore != null && messageErrore.length() > 0 && messageSpecifico.length() > 0) {
            buffer.append(SEP);
        }
        buffer.append(messageSpecifico);

        return buffer.toString();
    }

    public AlgosException getException() {
        return exception;
    }

    public String getCompanySigla() {
        return companySigla;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddressIP() {
        return addressIP;
    }

    public boolean isMultiCompany() {
        return multiCompany;
    }

    public boolean isUsaDB() {
        return usaDB;
    }

    public boolean isUsaMail() {
        return usaMail;
    }

    public boolean isUsaStackTrace() {
        return usaStackTrace;
    }

    public void setUsaStackTrace(boolean usaStackTrace) {
        this.usaStackTrace = usaStackTrace;
    }

}
