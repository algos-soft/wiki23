package it.algos.vaad23.backend.service;

import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.renderer.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.time.format.*;
import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 04-apr-2022
 * Time: 18:09
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(hService.class); <br>
 * 3) @Autowired public hService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ColumnService extends AbstractService {


    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     *
     * @param grid                  a cui aggiungere la colonna
     * @param entityClazz           modello-dati specifico
     * @param gridPropertyNamesList lista di property da creare
     */
    public void addColumnsOneByOne(final Grid grid, Class<? extends AEntity> entityClazz, final List<String> gridPropertyNamesList) {
        if (grid != null && gridPropertyNamesList != null) {
            for (String propertyName : gridPropertyNamesList) {
                this.crea(grid, entityClazz, propertyName);
            }
        }
    }

    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     *
     * @param grid         a cui aggiungere la colonna
     * @param entityClazz  modello-dati specifico
     * @param propertyName della property
     */
    public void crea(final Grid grid, Class<? extends AEntity> entityClazz, final String propertyName) {
        Grid.Column<AEntity> colonna = null;
        String messageEsterno;
        String width = annotationService.getWidth(entityClazz, propertyName);
        AETypeField type = annotationService.getFormType(entityClazz, propertyName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yy HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d-MMM-yy");
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("HH:mm:ss");
        boolean flexGrow = annotationService.isFlexGrow(entityClazz, propertyName);
        String header = annotationService.getHeader(entityClazz, propertyName);
        VaadinIcon headerIcon = annotationService.getHeaderIcon(entityClazz, propertyName);
        String colorHeaderIcon = annotationService.getHeaderIconColor(entityClazz, propertyName);
        boolean isSearchField = annotationService.isSearch(entityClazz, propertyName);
        String sortProperty = annotationService.getSortProperty(entityClazz, propertyName);

        colonna = switch (type) {
            case text, enumeration, link, localDateTime, localDate, localTime -> grid.addColumn(propertyName).setSortable(true);
            case integer, lungo -> grid.addColumn(propertyName).setSortable(true);
            case booleano -> addBoolean(grid, entityClazz, propertyName);

            //            case booleano -> {
            //                yield grid.addColumn(new ComponentRenderer<>(entity -> {
            //                    Field field = null;
            //                    Icon icona = null;
            //                    try {
            //                        field = reflectionService.getField(entityClazz, propertyName);
            //                    } catch (Exception unErrore) {
            //                        logger.error(new WrapLog().exception(unErrore).usaDb());
            //                    }
            //                    try {
            //                        icona = (boolean) field.get(entity) ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
            //                        icona.setColor((boolean) field.get(entity) ? COLOR_VERO : COLOR_FALSO);
            //
            //                    } catch (Exception unErrore) {
            //                        logger.error(new WrapLog().exception(unErrore).usaDb());
            //                    }
            //
            //                    return icona;
            //                })).setSortable(true);
            //            }
            default -> null;
        };

        //        colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
        //            Field field = null;
        //            String message;
        //            Icon icona;
        //            Object obj = reflectionService.getPropertyValue((AEntity) entity, propertyName);
        //
        //            if (obj == null) {
        //                //                message = String.format("Un valore del parametro %s è nullo", propertyName);
        //                //                logger.info(new WrapLog().exception(new AlgosException(message)).usaDb());
        //                return new Label();
        //            }
        //
        //            try {
        //                field = reflectionService.getField(entityClazz, propertyName);
        //            } catch (Exception unErrore) {
        //                logger.error(new WrapLog().exception(unErrore).usaDb());
        //            }
        //
        //            try {
        //                return switch (type) {
        //                    case text -> new Label((String) field.get(entity));
        //                    case integer -> new Label(field.get(entity) + VUOTA);
        //                    case lungo -> new Label(field.get(entity) + VUOTA);
        //                    case booleano -> {
        //                        icona = (boolean) field.get(entity) ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
        //                        icona.setColor((boolean) field.get(entity) ? COLOR_VERO : COLOR_FALSO);
        //                        yield icona;
        //                    }
        //                    case enumeration -> new Label(obj != null ? obj.toString() : VUOTA);
        //                    case link -> new Label(obj != null ? obj.toString() : VUOTA);
        //                    case localDateTime -> new Label(formatter.format((LocalDateTime) obj));
        //                    case localDate -> new Label(formatter2.format((LocalDate) obj));
        //                    case localTime -> new Label(formatter3.format((LocalTime) obj));
        //                    default -> {
        //                        message = String.format("Default Switch, manca il case %s", type);
        //                        logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
        //                        yield VaadinIcon.ALARM.create();
        //                    }
        //                };
        //            } catch (Exception unErrore) {
        //                logger.error(new WrapLog().exception(unErrore).usaDb());
        //            }
        //            logger.error(new WrapLog().exception(new AlgosException("Lo switch non ha funzionato")).usaDb());
        //            return new Label();
        //        }));//end of lambda expressions and anonymous inner class

        if (colonna != null) {
            colonna.setWidth(width).setFlexGrow(0).setHeader(textService.primaMaiuscola(header));
            if (textService.isEmpty(colonna.getKey())) {
                colonna.setKey(propertyName);
            }
            if (flexGrow) {
                colonna.setFlexGrow(1);
            }
            if (textService.isValid(sortProperty)) {
                colonna.setSortProperty(sortProperty);
            }

        }
        else {
            messageEsterno = String.format("La colonna del parametro %s non è stata creata", propertyName);
            logger.error(new WrapLog().exception(new AlgosException(messageEsterno)).usaDb());
        }

        //--eventuale aggiunta di una icona e l' header non è una String ma diventa un Component
        //--se c'è l' icona NON usa il nome della property ma solo l' icona
        if (headerIcon != null) {
            Icon icon = new Icon(headerIcon);
            //                icon.setSize(widthHeaderIcon);
            icon.setColor(colorHeaderIcon);
            colonna.setHeader(icon);
        }

        if (isSearchField) {
            Icon icon = new Icon(VaadinIcon.SEARCH);
            icon.setSize("12px");
            icon.getStyle().set("float", "center");
            icon.setColor("red");
            Label label = new Label();
            label.add(icon);
            label.add(textService.primaMaiuscola(header));
            colonna.setHeader(label);
        }
    }


    public Grid.Column<AEntity> addBoolean(final Grid grid, Class<? extends AEntity> entityClazz, final String propertyName) {
        Grid.Column<AEntity> colonna = null;

        colonna = grid.addColumn(new ComponentRenderer<>(entity -> {
            final AETypeBoolCol typeBool = annotationService.getTypeBoolCol(entityClazz, propertyName);
            //            final List<String> valori = annotation.getBoolEnumCol(field);
            Object value;
            boolean status = false;
            Icon icon;
            String testo = VUOTA;
            Label label = new Label();

            try {
                value = reflectionService.getPropertyValue((AEntity) entity, propertyName);
                if (value instanceof Boolean booleano) {
                    status = booleano;
                }
            } catch (Exception unErrore) {
                //                logger.error(unErrore.toString());
            }

            switch (typeBool) {
                case boolGrezzo:
                    testo = status ? "vero" : "falso";
                    label.setText(testo);
                    if (status) {
                        label.getStyle().set("color", "green");
                    }
                    else {
                        label.getStyle().set("color", "red");
                    }
                    return label;
                case checkBox:
                    return new Checkbox(status);
                case checkIcon:
                    if (status) {
                        icon = new Icon(VaadinIcon.CHECK);
                        icon.setColor("green");
                    }
                    else {
                        icon = new Icon(VaadinIcon.CLOSE);
                        icon.setColor("red");
                    }
                    icon.setSize("1em");
                    return icon;
                case checkIconReverse:
                    if (status) {
                        icon = new Icon(VaadinIcon.CLOSE);
                        icon.setColor("red");
                    }
                    else {
                        icon = new Icon(VaadinIcon.CHECK);
                        icon.setColor("green");
                    }
                    icon.setSize("1em");
                    return icon;
                case customLabel:
                    //                    try {
                    //                        testo = status ? valori.get(0) : valori.get(1);
                    //                    } catch (Exception unErrore) {
                    //                        logger.error(unErrore.toString());
                    //                    }
                    //
                    //                    if (textService.isValid(testo)) {
                    //                        label.setText(testo);
                    //                        if (status) {
                    //                            label.getStyle().set("color", "green");
                    //                        }
                    //                        else {
                    //                            label.getStyle().set("color", "red");
                    //                        }
                    //                    }
                    return label;
                case yesNo:
                    testo = status ? "si" : "no";
                    label.setText(testo);
                    if (status) {
                        label.getStyle().set("color", "green");
                    }
                    else {
                        label.getStyle().set("color", "red");
                    }
                    return label;
                case yesNoReverse:
                    testo = status ? "no" : "si";
                    label.setText(testo);
                    if (status) {
                        label.getStyle().set("color", "red");
                    }
                    else {
                        label.getStyle().set("color", "green");
                    }
                    return label;
                case yesNoBold:
                    testo = status ? "si" : "no";
                    label.setText(testo);
                    label.getStyle().set("font-weight", "bold");
                    if (status) {
                        label.getStyle().set("color", "green");
                    }
                    else {
                        label.getStyle().set("color", "red");
                    }
                    return label;
                case thumb:
                    if (status) {
                        icon = new Icon(VaadinIcon.THUMBS_UP);
                        icon.setColor("green");
                    }
                    else {
                        icon = new Icon(VaadinIcon.THUMBS_DOWN);
                        icon.setColor("red");
                    }
                    icon.setSize("1em");
                    return icon;
                default:
                    logger.error(new WrapLog().exception(new AlgosException("Switch - caso non definito")).usaDb());
                    break;
            }

            return new Label("g");
        }));//end of lambda expressions and anonymous inner class

        return colonna;
    }

}