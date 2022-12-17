package it.algos.vaad24.backend.service;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.server.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import static it.algos.vaad24.backend.service.FileService.*;
import it.algos.vaad24.backend.wrapper.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: gio, 24-set-2020
 * Time: 21:06
 * <p>
 * Static files: {project directory}/src/main/resources/META-INF/resources/
 * CSS files e JavaScript: {project directory}/frontend/styles/
 * Risorse esterne al JAR: {project directory}/config/
 *
 * @see(https://vaadin.com/docs/flow/importing-dependencies/tutorial-ways-of-importing.html)
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ResourceService extends AbstractService {


    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    /**
     * Legge un file di risorse da {project directory}/src/main/resources/META-INF/resources/ <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return testo completo grezzo del file
     */
    public String leggeMetaInf(final String simpleNameFileToBeRead) {
        String path = simpleNameFileToBeRead;
        String tag = "src/main/resources/META-INF/resources";

        if (simpleNameFileToBeRead != null && !simpleNameFileToBeRead.startsWith(tag)) {
            path = tag + File.separator + simpleNameFileToBeRead;
        }

        return fileService.leggeFile(path);
    }

    /**
     * Legge un file di risorse <br>
     *
     * @param nameFileToBeRead nome del file
     *
     * @return testo grezzo del file
     */
    public String leggeRisorsa(final String nameFileToBeRead) {
        File filePath = new File("META-INF.resources" + File.separator + nameFileToBeRead);
        return fileService.leggeFile(filePath.getAbsolutePath());
    }


    /**
     * Legge un file di risorse da {project directory}/frontend/ <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return testo completo grezzo del file
     */
    public String leggeFrontend(final String simpleNameFileToBeRead) {
        return fileService.leggeFile(FRONTEND + File.separator + simpleNameFileToBeRead);
    }


    /**
     * Legge un file di risorse da {project directory}/config/ <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return testo completo grezzo del file
     */
    public String leggeConfig(final String simpleNameFileToBeRead) {
        return fileService.leggeFile(CONFIG + File.separator + simpleNameFileToBeRead);
    }


    /**
     * Legge un file dal server <br>
     *
     * @param nameFileToBeRead nome del file
     *
     * @return testo grezzo del file
     */
    public String leggeServer(final String nameFileToBeRead) {
        return webService.leggeWebTxt(WebService.URL_BASE_VAADIN23_CONFIG + nameFileToBeRead);
    }

    /**
     * Legge una lista di righe di risorse da {project directory}/config/ <br>
     * La prima riga contiene i titoli
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return lista di righe grezze
     */
    public List<String> leggeListaConfig(final String simpleNameFileToBeRead) {
        String rawText = leggeConfig(simpleNameFileToBeRead);
        return leggeLista(rawText);
    }

    /**
     * Legge una lista di righe di risorse dal server <br>
     * La prima riga contiene i titoli
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return lista di righe grezze
     */
    public List<String> leggeListaServer(final String simpleNameFileToBeRead) {
        String rawText = leggeServer(simpleNameFileToBeRead);
        return leggeLista(rawText);
    }

    /**
     * Legge una lista di righe di risorse <br>
     * La prima riga contiene i titoli
     *
     * @param rawText testo grezzo del file
     *
     * @return lista di righe grezze
     */
    private List<String> leggeLista(final String rawText) {
        List<String> listaRighe = null;
        String[] righe;

        if (textService.isValid(rawText)) {
            listaRighe = new ArrayList<>();
            righe = rawText.split(CAPO);
            if (righe != null && righe.length > 0) {
                for (String riga : righe) {
                    riga = textService.estrae(riga, DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END);
                    riga = textService.levaCoda(riga, VIRGOLA).trim();
                    if (textService.isValid(riga)) {
                        listaRighe.add(riga);
                    }
                }
            }
        }

        return listaRighe;
    }


    /**
     * Legge una mappa di risorse da {project directory}/config/ oppure dal server URL_BASE_VAADIN23_CONFIG <br>
     * La mappa NON contiene i titoli <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return mappa dei titoli più le righe grezze
     */
    public Map<String, List<String>> leggeMappa(final String simpleNameFileToBeRead) {
        Map<String, List<String>> mappa;

        mappa = leggeMappaServer(simpleNameFileToBeRead);
        if (mappa == null) {
            mappa = leggeMappaConfig(simpleNameFileToBeRead);
        }

        return mappa;
    }

    /**
     * Legge una mappa di risorse da {project directory}/config/ <br>
     * La mappa NON contiene i titoli <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return mappa dei titoli più le righe grezze
     */
    public Map<String, List<String>> leggeMappaConfig(final String simpleNameFileToBeRead) {
        String rawText = leggeConfig(simpleNameFileToBeRead);
        return elaboraMappa(rawText);
    }


    /**
     * Legge una mappa di risorse dal server URL_BASE_VAADIN23_CONFIG <br>
     * La mappa NON contiene i titoli <br>
     *
     * @param simpleNameFileToBeRead nome del file senza path e senza directory
     *
     * @return mappa dei titoli più le righe grezze
     */
    public Map<String, List<String>> leggeMappaServer(final String simpleNameFileToBeRead) {
        String rawText = leggeServer(simpleNameFileToBeRead);
        return elaboraMappa(rawText);
    }

    /**
     * Legge una mappa di risorse <br>
     * La mappa NON contiene i titoli <br>
     *
     * @param rawText testo grezzo del file
     *
     * @return mappa delle righe grezze con eventualmente i titoli
     */
    private Map<String, List<String>> elaboraMappa(String rawText) {
        Map<String, List<String>> mappa = null;
        List<String> listaParti;
        String[] righe;
        String[] parti;
        boolean usaId = rawText.startsWith("id,") || rawText.startsWith("ID,");
        int prima = usaId ? 1 : 0;
        int numRiga = 0;

        if (textService.isValid(rawText)) {
            rawText = textService.levaTestoPrimaDi(rawText, CAPO).trim();
            righe = rawText.split(CAPO);
            if (righe != null && righe.length > 0) {
                mappa = new LinkedHashMap<>();
                for (String riga : righe) {
                    listaParti = new ArrayList<>();
                    parti = riga.split(VIRGOLA);

                    if (parti != null && parti.length > 0) {
                        for (int k = prima; k < parti.length; k++) {
                            listaParti.add(textService.levaCoda(parti[k], VIRGOLA).trim());
                        }
                    }
                    if (usaId) {
                        mappa.put(parti[0], listaParti);
                    }
                    else {
                        mappa.put(++numRiga + VUOTA, listaParti);
                    }
                }
            }
        }

        if (mappa != null) {
            mappa.remove("id");
            mappa.remove("0");
        }

        return mappa;
    }


    /**
     * Costruisce un file di risorse, partendo dal nome semplice <br>
     * La risorsa DEVE essere nel path 'src/main/resources/META-INF/resources/' <br>
     *
     * @param simpleNameFileToBeRead nome del file all'interno della directory 'resources'
     *
     * @return bytes
     */
    public File getFile(final String simpleNameFileToBeRead) {
        File resourceFile = null;
        String pathResourceFileName = VUOTA;

        if (textService.isEmpty(simpleNameFileToBeRead)) {
            return null;
        }

        if (simpleNameFileToBeRead.startsWith(PATH_RISORSE)) {
            pathResourceFileName = simpleNameFileToBeRead;
        }
        else {
            pathResourceFileName = PATH_RISORSE + simpleNameFileToBeRead;
        }

        resourceFile = new File(pathResourceFileName);

        return resourceFile;
    }


    /**
     * Legge i bytes di un file di risorse <br>
     * La risorsa DEVE essere nel path 'src/main/resources/META-INF/resources/' <br>
     *
     * @param simpleResourceFileName nome del file all'interno della directory 'resources'
     *
     * @return bytes
     */
    public byte[] getBytes(final String simpleResourceFileName) {
        byte[] bytes = null;
        File resourceFile = getFile(simpleResourceFileName);

        if (resourceFile != null && resourceFile.exists() && resourceFile.isFile()) {
            try {
                bytes = FileUtils.readFileToByteArray(resourceFile);
            } catch (Exception unErrore) {
                logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb().type(AETypeLog.resources));
            }
        }
        else {
            logger.error(new WrapLog().exception(new AlgosException(NON_ESISTE_FILE)).usaDb().type(AETypeLog.resources));
            return bytes;
        }

        return bytes;
    }


    /**
     * Costruisce una Image partendo dai bytes <br>
     *
     * @param bytes dell'immagine
     *
     * @return image
     */
    public Image getImageFromBytes(final byte[] bytes) {
        Image image = null;
        StreamResource resource;

        if (bytes != null) {
            try {
                resource = new StreamResource("manca.jpg", () -> new ByteArrayInputStream(bytes));
                image = new Image(resource, "manca");
            } catch (Exception unErrore) {
                logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb().type(AETypeLog.resources));
            }
        }

        return image;
    }


    /**
     * Costruisce una Image partendo da un file di risorse <br>
     * Legge i bytes di un file di risorse <br>
     * Legge le risorse di un file <br>
     *
     * @param simpleResourceFileName nome del file all'interno della directory 'resources'
     *
     * @return image
     */
    public Image getImageFromFile(final String simpleResourceFileName) {
        Image image = null;
        byte[] bytes = getBytes(simpleResourceFileName);

        if (bytes != null) {
            image = getImageFromBytes(bytes);
        }

        return image;
    }


    /**
     * Costruisce una Image partendo dal mongoDB <br>
     * Legge i bytes decodificando il valore memorizzato <br>
     *
     * @param mongoValue valore codificato nel mongoDB
     *
     * @return image
     */
    public Image getImageFromMongo(final String mongoValue) {
        Image image = null;
        byte[] bytes = Base64.decodeBase64(mongoValue);

        if (bytes != null) {
            image = getImageFromBytes(bytes);
        }

        return image;
    }


    public Image getImagePng(final String simpleResourceFileName) {
        return getImageFromFile(simpleResourceFileName + PUNTO + "png");
    }


    public Image getBandieraFromFile(final String simpleResourceFileName) {
        Image image = getImagePng("bandiere/" + simpleResourceFileName.toLowerCase());

        if (image != null) {
            image.setWidth("30px");
            image.setHeight("21px");
        }

        return image;
    }


    public Image getBandieraFromMongo(final String mongoValue) {
        Image image = getImageFromMongo(mongoValue);

        if (image != null) {
            image.setWidth("30px");
            image.setHeight("21px");
        }

        return image;
    }


    /**
     * Legge una stringa codificata dal file di risorse <br>
     *
     * @param simpleResourceFileName nome del file all'interno della directory 'resources'
     *
     * @return valore codificato
     */
    public String getSrc(final String simpleResourceFileName) {
        String bytesCodificati = VUOTA;
        byte[] bytes = getBytes(simpleResourceFileName);

        if (bytes != null) {
            bytesCodificati = Base64.encodeBase64String(bytes);
        }

        return bytesCodificati;
    }


    /**
     * Legge una stringa codificata dal file di risorse <br>
     *
     * @param simpleResourceFileNameWithoutSuffix nome del file all'interno della directory 'bandiere'
     *
     * @return valore codificato
     */
    public String getSrcBandieraPng(final String simpleResourceFileNameWithoutSuffix) {
        return getSrc("bandiere/" + simpleResourceFileNameWithoutSuffix.toLowerCase() + PUNTO + "png");
    }


    /**
     * Legge una mappa CSV da un file <br>
     * Prima lista (prima riga): titoli
     * Liste successive (righe successive): valori
     *
     * @param pathFileToBeRead nome completo del file
     *
     * @return lista di mappe di valori
     */
    public List<LinkedHashMap<String, String>> leggeMappaCSV(final String pathFileToBeRead) {
        return fileService.leggeMappaCSV(pathFileToBeRead);
    }

}