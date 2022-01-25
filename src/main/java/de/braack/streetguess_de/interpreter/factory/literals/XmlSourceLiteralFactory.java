package de.braack.streetguess_de.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.factory.XmlSourceFactory;
import de.braack.streetguess_de.interpreter.literals.Literal;

import java.io.File;

public abstract class XmlSourceLiteralFactory extends XmlSourceFactory<Literal> {
    public XmlSourceLiteralFactory(File content) {
        super(content);
    }
}
