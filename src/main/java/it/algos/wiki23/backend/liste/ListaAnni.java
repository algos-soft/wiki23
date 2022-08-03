package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.enumeration.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 17-Jul-2022
 * Time: 06:37
 * <p>
 * Lista delle biografie per anni <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadAnni con appContext.getBean(ListaAnni.class).nascita(nomeAnno).testoBody() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con nascita(nomeGiorno) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAnni extends ListaGiorniAnni {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAnni.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaAnni() {
    }// end of constructor


    public ListaAnni nascita(final String nomeAnno) {
        this.nomeLista = nomeAnno;
        super.typeLista = AETypeLista.annoNascita;
        return this;
    }

    public ListaAnni morte(final String nomeAnno) {
        this.nomeLista = nomeAnno;
        super.typeLista = AETypeLista.annoMorte;
        return this;
    }

}

