package it.algos;

import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.server.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.wiki23.backend.boot.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.*;
import org.springframework.boot.autoconfigure.security.servlet.*;
import org.springframework.boot.web.servlet.support.*;
import org.springframework.context.*;

/**
 * Project Wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29 apr 22
 * <p>
 * The entry point of the Spring Boot application. <br>
 * Spring boot web application initializer. <br>
 * <p>
 * Questa classe contiene il metodo 'main' che è il punto di ingresso di una applicazione Java <br>
 * In fase di sviluppo si possono avere diverse configurazioni, ognuna delle quali punta un ''main' diverso <br>
 * Nel JAR finale (runtime) si può avere una sola classe col metodo 'main' <br>
 * Nel WAR finale (runtime) occorre (credo) inserire dei servlet di context diversi <br>
 * Senza @EntityScan, SpringBoot non 'vede' le classi con @SpringView che sono in una directory diversa da questo package <br>
 * <p>
 * Annotated with @SpringBootApplication (obbligatorio) <br>
 * Annotated with @EnableVaadin (obbligatorio) <br>
 * Annotated with @EntityScan (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * <p>
 * Tutte le view devono essere comprese nel path di questa classe (directory interne incluse) <br>
 * Una sola view può avere @Route("") <br>
 * <p>
 * Spring Boot introduces the @SpringBootApplication annotation. <br>
 * This single annotation is equivalent to using @Configuration, @EnableAutoConfiguration, and @ComponentScan. <br>
 * Se l'applicazione NON usa la security, aggiungere exclude = {SecurityAutoConfiguration.class} a @SpringBootApplication <br>
 */
@SpringBootApplication(scanBasePackages = {"it.algos"}, exclude = {SecurityAutoConfiguration.class})
@EnableVaadin({"it.algos"})
@EntityScan({"it.algos"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Wiki23Application extends SpringBootServletInitializer {

    /**
     * Constructor
     *
     * @param args eventuali parametri in ingresso
     */
    public static void main(String[] args) {
        SpringApplication.run(Wiki23Application.class, args);
        Wiki23Boot.start();

    }// end of SpringBoot constructor


}// end of main class
