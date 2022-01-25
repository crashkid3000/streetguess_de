package de.braack.streetguess_de.interpreter.factory.rules;

import org.w3c.dom.NodeList;

import java.io.File;

public class HouseNumberRuleFactory extends XmlSourceRuleFactory {

    public HouseNumberRuleFactory(File content) {
        super(content);
        this.xpathString = "/rules/houseNumberRules/rule | /rules/houseNumberRules/regexRule";
    }
}
