package de.braack.streetguess_de.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.literals.AdverbLiteral;
import de.braack.streetguess_de.interpreter.literals.Literal;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class AdverbLiteralFactory extends XmlSourceLiteralFactory {
    public AdverbLiteralFactory(File content) {
        super(content);
        this.xpathString = "/literals/adverbs/literal";
    }

    @Override
    protected Literal[] getInstancesFromNodeList(NodeList nodes) {
        List<Literal> retVal = new LinkedList<>();
        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            Node hasArticle = node.getAttributes().getNamedItem("hasArticle");
            retVal.add(new AdverbLiteral(node.getTextContent(), Boolean.parseBoolean(hasArticle.getTextContent())));
        }
        return retVal.toArray(Literal[]::new);
    }
}
