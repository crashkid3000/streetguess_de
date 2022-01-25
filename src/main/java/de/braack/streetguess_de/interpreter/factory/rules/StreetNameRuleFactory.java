package de.braack.streetguess_de.interpreter.factory.rules;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class StreetNameRuleFactory extends XmlSourceRuleFactory{

    public StreetNameRuleFactory(File content) {
        super(content);
        this.xpathString = "/rules/streetNameRules/rule | /rules/streetNameRules/regexRule";
    }
}
