package it.algos.wiki23.backend.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.enumeration.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 25-Jul-2022
 * Time: 07:36
 * <p>
 * Lista delle biografie per giorni <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadGiorni con appContext.getBean(ListaGiorni.class).nascita(nomeGiorno).testoBody() <br>
 * Il costruttore è senza parametri e serve solo per preparare l'istanza che viene ''attivata'' con nascita(nomeGiorno) <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaGiorni extends ListaGiorniAnni {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaGiorni.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     */
    public ListaGiorni() {
    }// end of constructor


    public ListaGiorni nascita(final String nomeGiorno) {
        this.nomeLista = nomeGiorno;
        super.typeLista = AETypeLista.giornoNascita;
        return this;
    }

    public ListaGiorni morte(final String nomeGiorno) {
        this.nomeLista = nomeGiorno;
        super.typeLista = AETypeLista.giornoMorte;
        return this;
    }

}
