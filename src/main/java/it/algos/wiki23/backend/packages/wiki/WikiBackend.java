package it.algos.wiki23.backend.packages.wiki;

import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.genere.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;

import java.time.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: mar, 26-apr-2022
 * Time: 08:38
 */
public abstract class WikiBackend extends CrudBackend {

    protected String message;

    public WPref lastDownload;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiApiService wikiApiService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GenereBackend genereBackend;


    public WikiBackend(final MongoRepository crudRepository, final Class<? extends AEntity> entityClazz) {
        super(crudRepository, entityClazz);
    }// end of constructor with @Autowired

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param wikiTitle della pagina su wikipedia
     */
    public void download(final String wikiTitle) {
    }

    public void fixDownload(final long inizio, final String wikiTitle, final int sizeServerWiki, final int sizeMongoDB) {
        long fine = System.currentTimeMillis();
        Long delta = fine - inizio;
        int durata;
        String wikiTxt = textService.format(sizeServerWiki);
        String mongoTxt = textService.format(sizeMongoDB);

        delta = delta / 1000;
        durata = delta.intValue();
        if (lastDownload != null) {
            lastDownload.setValue(LocalDateTime.now());
        }
        else {
            logger.warn(new WrapLog().exception(new AlgosException("lastDownload è nullo")));
            return;
        }

        if (sizeServerWiki == sizeMongoDB) {
            message = String.format("Download di %s righe da [%s]", wikiTxt, wikiTitle);
        }
        else {
            message = String.format("Download di %s righe da [%s] convertite in %s elementi su mongoDB", wikiTxt, wikiTitle, mongoTxt);
        }

        logger.info(new WrapLog().message(message));
    }

}
