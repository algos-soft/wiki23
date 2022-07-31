package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sat, 30-Jul-2022
 * Time: 07:39
 * <p>
 * Superclasse astratta per le classi UploadGiorni e UploadAnni <br>
 */
public abstract class UploadGiorniAnni extends Upload {

    protected AETypeCrono typeCrono;

    protected String nomeGiornoAnno;

    protected boolean senzaParagrafi;


    protected int ordineGiornoAnno;

    protected String wikiTitle;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public UploadGiorniAnni() {
    }// end of constructor


    protected WResult esegue(String wikiTitle, String testoBody, int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(fixToc());
        buffer.append(torna());
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(tmpListaPersoneIni(numVoci));
        buffer.append(testoBody);
        buffer.append(uploadTest ? VUOTA : DOPPIE_GRAFFE_END);
        buffer.append(includeIni());
        buffer.append(portale());
        buffer.append(categorie());
        buffer.append(includeEnd());

        return registra(wikiTitle, buffer.toString().trim());
    }


    protected String torna() {
        return textService.isValid(nomeGiornoAnno) ? String.format("{{Torna a|%s}}", nomeGiornoAnno) : VUOTA;
    }

    protected String tmpListaPersoneIni(int numVoci) {
        StringBuffer buffer = new StringBuffer();

        buffer.append(String.format("{{Lista persone per %s", typeCrono.getGiornoAnno()));
        buffer.append(CAPO);
        buffer.append("|titolo=");
        buffer.append(wikiTitle);
        buffer.append(CAPO);
        buffer.append("|voci=");
        buffer.append(numVoci);
        buffer.append(CAPO);
        buffer.append("|testo=");
        buffer.append(senzaParagrafi ? VUOTA : "<nowiki></nowiki>");
        buffer.append(CAPO);

        return uploadTest ? VUOTA : buffer.toString();
    }


    protected String categorie() {
        StringBuffer buffer = new StringBuffer();
        String tag = typeCrono.getTagUpper();

        String title = switch (typeCrono) {
            case giornoNascita -> wikiUtility.wikiTitleNatiGiorno(nomeGiornoAnno);
            case giornoMorte -> wikiUtility.wikiTitleMortiGiorno(nomeGiornoAnno);
            case annoNascita -> wikiUtility.wikiTitleNatiAnno(nomeGiornoAnno);
            case annoMorte -> wikiUtility.wikiTitleMortiAnno(nomeGiornoAnno);
            default -> VUOTA;
        };

        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:Liste di %s per %s| %s]]", typeCrono.getTagLower(), typeCrono.getGiornoAnno(), ordineGiornoAnno));
        buffer.append(CAPO);
        buffer.append(String.format("*[[Categoria:%s| ]]", title));
        buffer.append(CAPO);

        return buffer.toString();
    }


    public void fixUploadMinuti(final long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        Long delta = fine - inizio;

        if (lastUpload != null) {
            lastUpload.setValue(LocalDateTime.now());
        }
        else {
            logger.warn(new WrapLog().exception(new AlgosException("lastUpload è nullo")));
            return;
        }

        if (durataUpload != null) {
            delta = delta / 1000 / 60;
            durataUpload.setValue(delta.intValue());
        }
        else {
            logger.warn(new WrapLog().exception(new AlgosException("durataUpload è nullo")));
            return;
        }

        message = String.format("Check");
        logger.info(new WrapLog().message(message).type(AETypeLog.upload).usaDb());
    }

}
