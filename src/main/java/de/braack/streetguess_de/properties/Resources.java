package de.braack.streetguess_de.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class Resources {

    private ResourceBundle resources;
    private static Resources instance = null;
    private static Logger log = LoggerFactory.getLogger(Resources.class);

    private Resources(){
        resources = ResourceBundle.getBundle("config");
    }

    public static Resources getInstance(){
        if(instance == null){
            instance = new Resources();
        }
        return instance;
    }

    public File getLiteralsFile(){
        String fileName = resources.getString("interpreter.literals");
        return getResourceFile(fileName);
    }

    public File getRulesFile(){
        String fileName = resources.getString("interpreter.rules");
        return getResourceFile(fileName);
    }

    private File getResourceFile(String fileName){
        URL url = this.getClass().getResource(fileName);
        if(url == null){
            throw new NullPointerException("Could not find resource file " + fileName);
        }
        File retVal = null;
        try {
            retVal = new File(url.toURI());
        }
        catch ( URISyntaxException use) {
            log.error("Could not transform following URL to URI: " + url.toString(), use);
        }
        return retVal;
    }
}
