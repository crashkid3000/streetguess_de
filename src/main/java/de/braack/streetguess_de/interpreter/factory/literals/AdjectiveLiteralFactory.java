package de.braack.streetguess_de.interpreter.factory.literals;

import de.braack.streetguess_de.interpreter.literals.AdjectiveEndingLiteral;
import de.braack.streetguess_de.interpreter.literals.AdjectiveLiteral;
import de.braack.streetguess_de.interpreter.literals.Literal;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;

public class AdjectiveLiteralFactory extends XmlSourceLiteralFactory {
    public AdjectiveLiteralFactory(File content) {
        super(content);
        this.xpathString = "/literals/adjectives/literal";
    }

    @Override
    protected Literal[] getInstancesFromNodeList(NodeList nodes) {
        LinkedList<Literal> retVal = new LinkedList<>();
        AdjectiveEndingLiteral[] endings;
        AdjectiveLiteral temp;
        try {
            endings = (AdjectiveEndingLiteral[]) new AdjectiveEndingLiteralFactory(content).createLiterals();
        }
        catch (Exception e){
            endings = new AdjectiveEndingLiteral[]{ new AdjectiveEndingLiteral("e"), new AdjectiveEndingLiteral("er"), new AdjectiveEndingLiteral("es")};
        }

        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            for(AdjectiveEndingLiteral ending: endings){
                temp = new AdjectiveLiteral(node.getTextContent() + ending.getValue());
                retVal.add(temp);
            }
        }

        return retVal.toArray(Literal[]::new);

    }


}
