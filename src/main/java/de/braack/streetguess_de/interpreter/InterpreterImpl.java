package de.braack.streetguess_de.interpreter;

import de.braack.streetguess_de.interpreter.factory.rules.BaseRuleFactory;
import de.braack.streetguess_de.properties.Resources;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class InterpreterImpl implements Interpreter {

    private final File literalsFile;
    private final File rulesFile;

    public InterpreterImpl() throws IOException {
        final Resources resources = Resources.getInstance();

        this.literalsFile = resources.getLiteralsFile();
        this.rulesFile = resources.getRulesFile();
    }

    public String interpret(String street) throws IOException{
        final String baseRule = new BaseRuleFactory(rulesFile).createObjects()[0];

        throw new RuntimeException("todo");
    }


}
