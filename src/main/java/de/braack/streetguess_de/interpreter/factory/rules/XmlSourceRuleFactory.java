package de.braack.streetguess_de.interpreter.factory.rules;

import de.braack.streetguess_de.interpreter.factory.XmlSourceFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class XmlSourceRuleFactory extends XmlSourceFactory<String> {

    public XmlSourceRuleFactory(File content) {
        super(content);
    }

    @Override
    protected String[] getLiteralsFromNodeList(NodeList nodes) {
        List<String> retVal = new LinkedList<>();
        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            retVal.add(node.getTextContent());
        }
        return retVal.toArray(String[]::new);
    }
}
