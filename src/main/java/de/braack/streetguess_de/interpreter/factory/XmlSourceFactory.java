package de.braack.streetguess_de.interpreter.factory;

import de.braack.streetguess_de.interpreter.literals.Literal;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class XmlSourceFactory<T> {

    protected String xpathString;
    protected File content;

    public XmlSourceFactory(File content){
        this.content = content;
    }

    public T[] createLiterals() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        NodeList nodes = getNodesFromDocument();
        T[] retVal = getLiteralsFromNodeList(nodes);
        return retVal;
    }

    private NodeList getNodesFromDocument() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        FileInputStream fis = new FileInputStream(content);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fis);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.compile(xpathString).evaluate(xmlDocument, XPathConstants.NODESET);
    }

    protected abstract T[] getLiteralsFromNodeList(NodeList nodes);

    protected String getXpathString(){
        return xpathString;
    }
}
