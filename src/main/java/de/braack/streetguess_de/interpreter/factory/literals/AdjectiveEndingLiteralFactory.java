package de.braack.streetguess_de.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.literals.AdjectiveEndingLiteral;
import de.braack.streetguess_de.interpreter.literals.Literal;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

class AdjectiveEndingLiteralFactory extends XmlSourceLiteralFactory {

    public AdjectiveEndingLiteralFactory(File content) {
        super(content);
        this.xpathString = "/literals/adjectiveEndings/literal";
    }

    @Override
    protected Literal[] getInstancesFromNodeList(NodeList nodes) {
        List<Literal> retVal = new LinkedList<>();
        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            retVal.add(new AdjectiveEndingLiteral(node.getTextContent()));
        }
        return retVal.toArray(Literal[]::new);
    }
}
