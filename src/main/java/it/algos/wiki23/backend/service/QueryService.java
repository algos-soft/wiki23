package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 18-mag-2022
 * Time: 11:23
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(QueryService.class); <br>
 * 3) @Autowired public QueryService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class QueryService extends WAbstractService {


    public boolean esisteLogin() {
        return botLogin != null;
    }

    public boolean isBotLogin() {
        return botLogin != null && botLogin.isBot();
    }

    public AETypeUser getLoginUserType() {
        return botLogin != null ? botLogin.getUserType() : null;
    }

    public boolean logAsUser() {
        return appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.user).isValido();
    }

    public boolean logAsAdmin() {
        return appContext.getBean(QueryLogin.class).urlRequest(AETypeUser.admin).isValido();
    }

    public boolean logAsBot() {
        return appContext.getBean(QueryLogin.class).urlRequest().isValido();
    }

    public AETypeUser logAsType(final AETypeUser type) {
        return appContext.getBean(QueryLogin.class).urlRequest(type).getUserType();
    }


    public boolean assertBot() {
        return logAsBot();
    }

    public int getSizeCat(final String catTitleGrezzo) {
        return appContext.getBean(QueryInfoCat.class).urlRequest(catTitleGrezzo).getIntValue();
    }

    public List<Long> getListaPageIds(final String catTitleGrezzo) {
        return appContext.getBean(QueryCat.class).getListaPageIds(catTitleGrezzo);
    }

    public List<MiniWrap> getMiniWrap(final List<Long> listaPageids) {
        return appContext.getBean(QueryTimestamp.class).getWrap(listaPageids);
    }

    public List<WrapBio> getBioWrap(final List<Long> listaPageids) {
        return appContext.getBean(QueryWrapBio.class).getWrap(listaPageids);
    }

    public WrapBio getBioWrap(final String wikiTitleGrezzo) {
        return appContext.getBean(QueryBio.class).getWrap(wikiTitleGrezzo);
    }

    public String getBioTmpl(final String wikiTitleGrezzo) {
        return appContext.getBean(QueryBio.class).getWrap(wikiTitleGrezzo).getTemplBio();
    }

}