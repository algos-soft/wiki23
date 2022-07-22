package it.algos.vaad23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;


/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: sab, 12-mar-2022
 * Time: 17:08
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ClassService.class); <br>
 * 3) @Autowired public ClassService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ClassService extends AbstractService {

    /**
     * Check if the class is an entityBean class. <br>
     * 1) Controlla che il parametro in ingresso non sia vuoto <br>
     *
     * @param canonicalName of the class to be checked if is of type AEntity
     *
     * @return true if the class is of type AEntity
     */
    public boolean isEntity(String canonicalName) {
        Class clazz = null;

        if (textService.isEmpty(canonicalName)) {
            return false;
        }

        try {
            clazz = Class.forName(canonicalName);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)));
        }

        if (clazz != null) {
            return isEntity(clazz);
        }
        else {
            return false;
        }
    }


    /**
     * Controlla se la classe è una AEntity. <br>
     *
     * @param genericClazz to be checked if is of type AEntity
     *
     * @return the status
     */
    public boolean isEntity(final Class genericClazz) {
        return genericClazz != null && AEntity.class.isAssignableFrom(genericClazz);
    }

    /**
     * Istanza della sottoclasse xxxBackend (prototype) associata alla entity <br>
     *
     * @param entityClazz di riferimento
     *
     * @return istanza di xxxBackend associata alla Entity
     */
    public CrudBackend getBackendFromEntityClazz(Class<? extends AEntity> entityClazz) {
        return getBackendFromEntityClazz(entityClazz.getCanonicalName());
    }


    /**
     * Istanza della sottoclasse xxxBackend (prototype) associata alla entity <br>
     *
     * @param entityClazzCanonicalName the canonical name of entity class
     *
     * @return istanza di xxxBackend associata alla Entity
     */
    public CrudBackend getBackendFromEntityClazz(String entityClazzCanonicalName) {
        CrudBackend backend = null;
        String backendClazzCanonicalName;

        if (textService.isValid(entityClazzCanonicalName)) {
            backendClazzCanonicalName = textService.levaCoda(entityClazzCanonicalName, SUFFIX_ENTITY) + SUFFIX_BACKEND;
            try {
                backend = (CrudBackend) appContext.getBean(Class.forName(backendClazzCanonicalName));
            } catch (Exception unErrore) {
                logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            }
        }
        return backend;
    }

    /**
     * Controlla che esiste una classe xxxLogicForm associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicForm associata alla Entity
     */
    public boolean isLogicFormClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) {
        return getLogicFormClassFromEntityClazz(dovrebbeEssereUnaEntityClazz) != null;
    }


    /**
     * Classe xxxLogicForm associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     *
     * @return classe xxxLogicForm associata alla Entity
     */
    public Class getLogicFormClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz) {
        return getLogicFormClassFromEntityClazz(dovrebbeEssereUnaEntityClazz, false);
    }


    /**
     * Classe xxxLogicForm associata alla Entity inviata  <br>
     *
     * @param dovrebbeEssereUnaEntityClazz the entity class
     * @param printLog                     flag per mostrare l'eccezione (se non trova la classe specifica)
     *
     * @return classe xxxLogicForm associata alla Entity
     */
    public Class getLogicFormClassFromEntityClazz(final Class dovrebbeEssereUnaEntityClazz, boolean printLog) {
        Class listClazz = null;
        String canonicalNameEntity;
        String canonicalNameLogicForm;
        String message;
        String packageName;
        String simpleNameEntity;
        String simpleNameLogicForm = VUOTA;

        //        if (dovrebbeEssereUnaEntityClazz == null) {
        //            message = String.format("Manca la EntityClazz");
        //            logger.error(message, this.getClass(), "getLogicFormClassFromEntityClazz");
        //            return null;
        //        }

        //        canonicalNameEntity = dovrebbeEssereUnaEntityClazz.getCanonicalName();
        //        simpleNameEntity = fileService.estraeClasseFinale(canonicalNameEntity);
        //        packageName = text.levaCoda(simpleNameEntity, SUFFIX_ENTITY).toLowerCase();

        //        if (!annotation.isEntityClass(dovrebbeEssereUnaEntityClazz)) {
        //            message = String.format("La clazz ricevuta %s NON è una EntityClazz", simpleNameEntity);
        //            logger.info(message, this.getClass(), "getLogicFormClassFromEntityClazz");
        //            return null;
        //        }

        //        //--provo a creare la classe specifica xxxLogicForm (classe, non istanza)
        //        try {
        //            canonicalNameLogicForm = text.levaCoda(canonicalNameEntity, SUFFIX_ENTITY) + SUFFIX_LOGIC_FORM;
        //            simpleNameLogicForm = fileService.estraeClasseFinale(canonicalNameLogicForm);
        //
        //            listClazz = Class.forName(canonicalNameLogicForm);
        //        } catch (Exception unErrore) {
        //            if (printLog) {
        //                message = String.format("Nel package %s non esiste la classe %s", packageName, simpleNameLogicForm);
        //                logger.info(message, this.getClass(), "getLogicFormClassFromEntityClazz");
        //            }
        //        }

        return listClazz;
    }

    public String getProjectName() {
        String projectName;
        String pathCurrent = System.getProperty("user.dir") + SLASH;
        projectName = fileService.lastDirectory(pathCurrent); //@todo da controllarer

        if (projectName.endsWith(SLASH)) {
            projectName = textService.levaCoda(projectName, SLASH);
        }

        return projectName;
    }


    /**
     * Recupera la clazz dal nome Java nel package <br>
     * Il simpleName termina SENZA JAVA_SUFFIX <br>
     *
     * @param simpleName della classe
     *
     * @return classe individuata
     */
    public Class getClazzFromSimpleName(String simpleName) {
        String canonicalName;
        String message;

        if (textService.isEmpty(simpleName)) {
            return null;
        }

        canonicalName = fileService.getCanonicalName(simpleName);
        if (textService.isEmpty(canonicalName)) {
            message = String.format("Non esiste la classe [%s] nella directory package", simpleName);
            logger.info(new WrapLog().exception(new AlgosException(message)));
        }

        return getClazzFromCanonicalName(canonicalName);
    }


    /**
     * Recupera la clazz dal nome canonico <br>
     * Il canonicalName inizia da ...it/algos/... <br>
     * Il canonicalName termina SENZA JAVA_SUFFIX <br>
     *
     * @param canonicalName della classe relativo al path parziale di esecuzione
     *
     * @return classe individuata
     */
    public Class getClazzFromCanonicalName(String canonicalName) {
        Class clazz = null;
        String message;

        if (textService.isEmpty(canonicalName)) {
            return null;
        }

        if (canonicalName.endsWith(JAVA_SUFFIX)) {
            canonicalName = textService.levaCoda(canonicalName, JAVA_SUFFIX);
        }

        try {
            clazz = Class.forName(canonicalName);
        } catch (Exception unErrore) {
            message = String.format("Non esiste la classe [%s] nella directory package", canonicalName);
            logger.info(new WrapLog().exception(new AlgosException(message)));
        }

        return clazz;
    }

}