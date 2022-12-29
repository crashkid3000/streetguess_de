import de.braack.streetguess_de.properties.Resources;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class Quicktest {

    private static ResourceBundle resources;
    private static File litertalsFile;
    private static File rulesFile;

    @BeforeAll
    public static void init(){
        resources = ResourceBundle.getBundle("config");
        litertalsFile = Resources.getInstance().getLiteralsFile();
        rulesFile = Resources.getInstance().getRulesFile();
    }

    @Test
    void log() {
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }

    @Test
    public void quicktest(){
        final List<String> liste = new LinkedList<>();
        doof(liste);
        assertTrue(liste.size() > 0);
    }

    private void doof(List<String> liste){
        if(liste != null){
            liste.add("doof");
        }
    }
}
