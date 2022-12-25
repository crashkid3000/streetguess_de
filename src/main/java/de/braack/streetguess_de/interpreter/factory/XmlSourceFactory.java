package de.braack.streetguess_de.interpreter.factory;

import de.braack.streetguess_de.interpreter.exception.InternalConfigError;
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

/**
 * A factory that creates instances of a given type by reading from an XML file. Implementing classes must specify
 * an Xpath String <code>xpathString</code> that will be used for finding the instances. Additionally, the implementing
 * classes must also specify how to extract the objects from the {@link NodeList}.
 * @param <T> The type of the instances that are to be created.
 */
public abstract class XmlSourceFactory<T> {

    protected String xpathString;
    protected File content;

    /**
     * Creates a new XmlSourceFactory
     * @param content The reference to the XML file.
     */
    public XmlSourceFactory(File content){
        this.content = content;
    }

    /**
     * Attempts to create the objects from the file provided in the constructor.
     * @return All instances of the given type <code>T</code> that could be extracted from the provided file.
     * @throws IOException If something goes wrong while attempting to access the file, including parsing errors.
     */
    public T[] createObjects() throws IOException {
        NodeList nodes = getNodesFromDocument();
        T[] retVal = getInstancesFromNodeList(nodes);
        return retVal;
    }

    /**
     * Reads and parses the provided XML file. Also creates a {@link NodeList} for the found XML bits and pieces.
     * @return The <code>Nodelist</code> from the XML.
     * @throws IOException If something goes wrong while attempting to access the file, including parsing errors.
     */
    private NodeList getNodesFromDocument() throws IOException {
        FileInputStream fis = new FileInputStream(content);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = builderFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException pce){
            throw new InternalConfigError(pce);
        }
        Document xmlDocument;
        try {
            xmlDocument = builder.parse(fis);
        }
        catch (SAXException se) {
            throw new IOException("Could not parse XML", se);
        }
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            return (NodeList) xPath.compile(xpathString).evaluate(xmlDocument, XPathConstants.NODESET);
        }
        catch (XPathExpressionException xpee){
            throw new InternalConfigError("Could not parse Xpath", xpee);
        }
    }

    protected abstract T[] getInstancesFromNodeList(NodeList nodes);
}
