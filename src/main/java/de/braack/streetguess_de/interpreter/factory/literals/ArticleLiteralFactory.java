package de.braack.streetguess_de.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.literals.ArticleLiteral;
import de.braack.streetguess_de.interpreter.literals.Literal;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ArticleLiteralFactory extends XmlSourceLiteralFactory {
    public ArticleLiteralFactory(File content) {
        super(content);
        this.xpathString = "/literals/articles/literal";
    }

    @Override
    protected Literal[] getLiteralsFromNodeList(NodeList nodes) {
        List<Literal> retVal = new LinkedList<>();
        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            retVal.add(new ArticleLiteral(node.getTextContent()));
        }
        return retVal.toArray(Literal[]::new);
    }
}
