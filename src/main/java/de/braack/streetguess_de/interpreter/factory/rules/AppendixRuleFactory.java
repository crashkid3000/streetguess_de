package de.braack.streetguess_de.interpreter.factory.rules;

import java.io.File;

public class AppendixRuleFactory extends XmlSourceRuleFactory {

    public AppendixRuleFactory(File content) {
        super(content);
        this.xpathString = "/rules/appendixRules/rule | /rules/appendixRules/regexRule";
    }
}
