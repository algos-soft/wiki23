package it.algos.vaad24.backend.annotation;

import static it.algos.vaad24.backend.boot.VaadCost.*;

import java.lang.annotation.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: ven, 13-ott-2017
 * Time: 14:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class and interface.
public @interface AIEntity {

    /**
     * (Optional) nome della collection (minuscolo) solo se diverso dal nome della EntityClass
     */
    String collectionName() default VUOTA;


    /**
     * (Optional) entity 'ancestor' indispensabile per il reset
     */
    String preReset() default VUOTA;


}// end of interface annotation
