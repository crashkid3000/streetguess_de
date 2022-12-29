package de.braack.streetguess_de.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.literals.Literal;
import de.braack.streetguess_de.interpreter.literals.StreetLiteral;
import de.braack.streetguess_de.properties.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class StreetLiteralFactoryTest {

    private static Literal[] literals;

    @BeforeAll
    public static void populateLiterals() {
        XmlSourceLiteralFactory factory = new StreetLiteralFactory(Resources.getInstance().getLiteralsFile());
        try {
            literals = factory.createObjects();
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
            assertTrue(l instanceof StreetLiteral);
        }
    }
}
