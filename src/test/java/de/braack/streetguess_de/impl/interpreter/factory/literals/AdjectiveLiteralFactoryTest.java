package de.braack.streetguess_de.impl.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.factory.literals.ArticleLiteralFactory;
import de.braack.streetguess_de.interpreter.factory.literals.XmlSourceLiteralFactory;
import de.braack.streetguess_de.interpreter.literals.ArticleLiteral;
import de.braack.streetguess_de.interpreter.literals.Literal;
import de.braack.streetguess_de.properties.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class AdjectiveLiteralFactoryTest {

    private static Literal[] literals;

    @BeforeAll
    public static void populateLiterals() throws SAXException, ParserConfigurationException, XPathExpressionException {
        XmlSourceLiteralFactory factory = new ArticleLiteralFactory(Resources.getInstance().getLiteralsFile());
        try {
            literals = factory.createLiterals();
        }
        catch (IOException ioe){
            assumeTrue(false, "Could not read configured file containing literals");
        }
    }

    @Test
    public void literalsPopulated() {
        assertTrue(literals.length > 0);
    }

    @Test
    public void literalsOfProperType() {
        for(Literal l: literals){
            assertTrue(l instanceof ArticleLiteral);
        }
    }
}
