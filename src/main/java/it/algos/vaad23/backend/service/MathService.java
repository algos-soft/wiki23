package it.algos.vaad23.backend.service;

import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.text.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Wed, 29-Jun-2022
 * Time: 06:45
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(MathService.class); <br>
 * 3) @Autowired public MathService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MathService extends AbstractService {

    public boolean multiploEsatto(int multiplo, int corrente) {
        if (corrente < multiplo) {
            return false;
        }
        else {
            return corrente % multiplo == 0;
        }
    }

    /**
     * Divisione tra due numeri double <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double divisione(double dividendo, double divisore) {
        double quoziente = dividendo / divisore;
        return quoziente;
    }

    /**
     * Percentuale tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo double
     */
    public double percentuale(int dividendo, int divisore) {
        return divisione((double) dividendo, (double) divisore) * 100;
    }

    /**
     * Percentuale tra due numeri interi <br>
     *
     * @param dividendo quantità da dividere
     * @param divisore  quantità che divide
     *
     * @return valore risultante di tipo String
     */
    public String percentualeDueDecimali(int dividendo, int divisore) {
        return new DecimalFormat("0.00").format(percentuale(dividendo, divisore)) + "%";
    }

}