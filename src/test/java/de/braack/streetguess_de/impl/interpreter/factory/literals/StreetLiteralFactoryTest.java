package de.braack.streetguess_de.impl.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.factory.rules.StreetNameRuleFactory;
import de.braack.streetguess_de.interpreter.factory.rules.XmlSourceRuleFactory;
import de.braack.streetguess_de.interpreter.literals.Literal;
import org.junit.jupiter.api.BeforeAll;

public class StreetLiteralFactoryTest {

    private static Literal[] literals;

    @BeforeAll
    public static void populateLiterals(){
        XmlSourceRuleFactory factory = new StreetNameRuleFactory()
    }
}
