package it.algos.vaad23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.interfaces.*;
import it.algos.vaad23.backend.packages.utility.preferenza.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Sun, 26-Jun-2022
 * Time: 09:32
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PreferenceService extends AbstractService {


    public void setValue(AETypePref type, String keyCode, Object javaValue) {
        Preferenza preferenza;

        if (preferenzaBackend == null) {
            return;
        }

        preferenza = preferenzaBackend.findByKeyCode(keyCode);
        if (preferenza == null) {
            return;
        }

        preferenza.setValue(type.objectToBytes(javaValue));
        preferenzaBackend.update(preferenza);
    }


    public Object getValue(AETypePref type, String keyCode) {
        Object javaValue;
        Preferenza preferenza = null;

        if (preferenzaBackend == null) {
            return null;
        }

        preferenza = preferenzaBackend.findByKeyCode(keyCode);
        javaValue = preferenza != null ? type.bytesToObject(preferenza.getValue()) : null;

        return javaValue;
    }

    public String getStr(AETypePref type, String keyCode) {
        Object obj;

        if (type == AETypePref.string) {
            obj = getValue(type, keyCode);
            if (obj instanceof String value) {
                return value;
            }
            return VUOTA;
        }
        else {
            log(type, keyCode, "getStr");
            return VUOTA;
        }
    }


    public boolean is(AETypePref type, String keyCode) {
        Object obj;

        if (type == AETypePref.bool) {
            obj = getValue(type, keyCode);
            if (obj instanceof Boolean value) {
                return value;
            }
            return false;
        }
        else {
            log(type, keyCode, "is");
            return false;
        }
    }

    public int getInt(AETypePref type, String keyCode) {
        Object obj;

        if (type == AETypePref.integer) {
            obj = getValue(type, keyCode);
            if (obj instanceof Integer value) {
                return value;
            }
            return 0;
        }
        else {
            log(type, keyCode, "getInt");
            return 0;
        }
    }


    /**
     * Tutti i valori della enum <br>
     */
    public String getEnumAll(AETypePref type, String keyCode) {
        Object obj;

        if (type == AETypePref.enumerationString) {
            obj = getValue(type, keyCode);
            if (obj instanceof String value) {
                return value;
            }
            return VUOTA;
        }
        else {
            log(type, keyCode, "getEnumAll");
            return VUOTA;
        }
    }

    /**
     * Valore selezionato della enum <br>
     */
    public String getEnumCurrentTxt(AETypePref type, String keyCode) {
        Object obj;

        if (type == AETypePref.enumerationString) {
            obj = getValue(type, keyCode);
            if (obj instanceof String value) {
                return textService.getEnumValue(value);
            }
            return VUOTA;
        }
        else {
            log(type, keyCode, "getEnumCurrentTxt");
            return VUOTA;
        }
    }

    /**
     * Valore selezionato della enum <br>
     */
    public AITypePref getEnumCurrentObj(AITypePref typeEnum, AETypePref type, String keyCode) {
        Object obj = null;

        if (type == AETypePref.enumerationType) {
            obj = getValue(type, keyCode);
            if (obj instanceof String value) {
                value = textService.getEnumValue(value);
                typeEnum = typeEnum.get(value);
                return typeEnum;
            }
            return null;
        }
        else {
            log(type, keyCode, "getEnumCurrentObj");
            return null;
        }
    }

    /**
     * Valore selezionato della enum <br>
     */
    public void setEnumCurrentObj(AETypePref type, String keyCode, AITypePref currentValue) {
        if (type == AETypePref.enumerationType) {
            setValue(type, keyCode, currentValue.getPref());
        }
        else {
            log(type, keyCode, "setEnumCurrentObj");
        }
    }

    /**
     * Valore selezionato della enum <br>
     */
    public void setEnumCurrentTxt(AETypePref type, String keyCode, String currentValue) {
        Object obj;
        String newTestoCompletoEnum;

        if (type == AETypePref.enumerationString) {
            obj = getValue(type, keyCode);
            if (obj instanceof String oldTestoCompletoEnum) {
                newTestoCompletoEnum = textService.setEnumValue(oldTestoCompletoEnum, currentValue);
                setValue(type, keyCode, newTestoCompletoEnum);
            }
        }
        else {
            log(type, keyCode, "setEnumCurrent");
        }
    }


    public void log(AETypePref type, String keyCode, String methodName) {
        String message;
        message = String.format("La preferenza %s è di type %s. Non puoi usare il metodo %s", keyCode, type, methodName);
        logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
    }

}
