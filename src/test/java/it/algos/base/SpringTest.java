package it.algos.base;

import it.algos.vaad23.backend.packages.utility.log.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: gio, 24-mar-2022
 * Time: 11:28
 * Layer per gestire ApplicationContext
 */
public abstract class SpringTest extends AlgosTest {

    @Autowired
    protected LoggerRepository loggerRepository;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUpAll() {
        super.setUpAll();
    }

    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        super.initMocks();
        assertNotNull(loggerRepository);
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
        loggerBackend.crudRepository = loggerRepository;
        loggerBackend.repository = loggerRepository;
    }

}
