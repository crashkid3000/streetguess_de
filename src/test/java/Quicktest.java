import de.braack.streetguess_de.interpreter.factory.literals.AdverbLiteralFactory;
import de.braack.streetguess_de.interpreter.factory.XmlSourceFactory;
import de.braack.streetguess_de.interpreter.literals.Literal;
import de.braack.streetguess_de.properties.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Quicktest {

    private static ResourceBundle resources;
    private static File litertalsFile;
    private static File rulesFile;
    private static final Logger log = LoggerFactory.getLogger(Quicktest.class);

    @BeforeAll
    public static void init(){
        resources = ResourceBundle.getBundle("config");
        litertalsFile = Resources.getInstance().getLiteralsFile();
        rulesFile = Resources.getInstance().getRulesFile();
    }

    @Test
    public void quicktest(){
        try{
            XmlSourceFactory factory = new AdverbLiteralFactory(litertalsFile);
            Literal[] l = factory.createLiterals();
            assertNotNull(l);
        }
        catch (Exception e) {
            log.error("Ah shit", e);
        }

    }
}
