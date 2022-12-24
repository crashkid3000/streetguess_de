package de.braack.streetguess_de.interpreter;

import de.braack.streetguess_de.interpreter.exception.InternalConfigError;
import de.braack.streetguess_de.interpreter.factory.rules.BaseRuleFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public class InterpreterImpl implements Interpreter {

    private final File literalsFile;
    private final File rulesFile;

    public InterpreterImpl() throws IOException {
        URL literalsFileUrl = this.getClass().getResource("/literals.xml");
        if(literalsFileUrl == null){
            throw new FileNotFoundException("Could not find file with literals");
        }
        try {
            literalsFile = new File(literalsFileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new IOException("Error creating file object from resource-URI", e);
        }

        URL rulesFileUrl = this.getClass().getResource("/rules.xml");
        if(rulesFileUrl == null){
            throw new FileNotFoundException("Could not find file with rules");
        }
        try {
            rulesFile = new File(rulesFileUrl.toURI());
        }
        catch (URISyntaxException e){
            throw new IOException("Error creating file object from resource-URI", e);
        }

    }

    public String interpret(String street) throws IOException{
        final String baseRule = new BaseRuleFactory(rulesFile).createLiterals()[0];

        throw new RuntimeException("todo");
    }
}
