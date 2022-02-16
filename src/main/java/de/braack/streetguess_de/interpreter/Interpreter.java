package de.braack.streetguess_de.interpreter;

import java.io.IOException;

public interface Interpreter {

    String interpret(String street) throws IOException;
}
