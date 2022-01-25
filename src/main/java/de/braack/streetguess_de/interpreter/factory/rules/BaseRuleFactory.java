package de.braack.streetguess_de.interpreter.factory.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class BaseRuleFactory extends XmlSourceRuleFactory {

    private static final Logger log = LoggerFactory.getLogger(BaseRuleFactory.class);

    public BaseRuleFactory(File content) {
        super(content);
        this.xpathString = "/rules/baseRule/rule | /rules/baseRule/regexRule";
    }

    @Override
    protected String[] getLiteralsFromNodeList(NodeList nodes) {
        List<String> retVal = new LinkedList<>();
        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            if(retVal.isEmpty()) {
                retVal.add(node.getTextContent());
            }
            else {
                log.warn("Base rule " + node.getTextContent() + " wont be added since a base rule has been added already");
            }
        }
        return retVal.toArray(String[]::new);
    }
}
