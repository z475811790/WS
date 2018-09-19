package xyzdlcore;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Branch;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.InvalidXPathException;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Visitor;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;

public class XML implements Document {

	private Document _doc;

	public Object clone() {
		return _doc.clone();
	}

	public XML(ByteArray bytes) {
		try {
			SAXReader reader = new SAXReader();
			bytes.position(0);
			_doc = reader.read(bytes.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void accept(Visitor arg0) {
		_doc.accept(arg0);
	}

	public void add(Comment arg0) {
		_doc.add(arg0);
	}

	public void add(Element arg0) {
		_doc.add(arg0);
	}

	public void add(Node arg0) {
		_doc.add(arg0);
	}

	public void add(ProcessingInstruction arg0) {
		_doc.add(arg0);
	}

	public Document addComment(String arg0) {
		return _doc.addComment(arg0);
	}

	public Document addDocType(String arg0, String arg1, String arg2) {
		return _doc.addDocType(arg0, arg1, arg2);
	}

	public Element addElement(QName arg0) {
		return _doc.addElement(arg0);
	}

	public Element addElement(String arg0, String arg1) {
		return _doc.addElement(arg0, arg1);
	}

	public Element addElement(String arg0) {
		return _doc.addElement(arg0);
	}

	public Document addProcessingInstruction(String arg0, Map arg1) {
		return _doc.addProcessingInstruction(arg0, arg1);
	}

	public Document addProcessingInstruction(String arg0, String arg1) {
		return _doc.addProcessingInstruction(arg0, arg1);
	}

	public void appendContent(Branch arg0) {
		_doc.appendContent(arg0);
	}

	public String asXML() {
		return _doc.asXML();
	}

	public Node asXPathResult(Element arg0) {
		return _doc.asXPathResult(arg0);
	}

	public void clearContent() {
		_doc.clearContent();
	}

	public List content() {
		return _doc.content();
	}

	public XPath createXPath(String arg0) throws InvalidXPathException {
		return _doc.createXPath(arg0);
	}

	public Node detach() {
		return _doc.detach();
	}

	public Element elementByID(String arg0) {
		return _doc.elementByID(arg0);
	}

	public DocumentType getDocType() {
		return _doc.getDocType();
	}

	public Document getDocument() {
		return _doc.getDocument();
	}

	public EntityResolver getEntityResolver() {
		return _doc.getEntityResolver();
	}

	public String getName() {
		return _doc.getName();
	}

	public short getNodeType() {
		return _doc.getNodeType();
	}

	public String getNodeTypeName() {
		return _doc.getNodeTypeName();
	}

	public Element getParent() {
		return _doc.getParent();
	}

	public String getPath() {
		return _doc.getPath();
	}

	public String getPath(Element arg0) {
		return _doc.getPath(arg0);
	}

	public Element getRootElement() {
		return _doc.getRootElement();
	}

	public String getStringValue() {
		return _doc.getStringValue();
	}

	public String getText() {
		return _doc.getText();
	}

	public String getUniquePath() {
		return _doc.getUniquePath();
	}

	public String getUniquePath(Element arg0) {
		return _doc.getUniquePath(arg0);
	}

	public String getXMLEncoding() {
		return _doc.getXMLEncoding();
	}

	public boolean hasContent() {
		return _doc.hasContent();
	}

	public int indexOf(Node arg0) {
		return _doc.indexOf(arg0);
	}

	public boolean isReadOnly() {
		return _doc.isReadOnly();
	}

	public boolean matches(String arg0) {
		return _doc.matches(arg0);
	}

	public Node node(int arg0) throws IndexOutOfBoundsException {
		return _doc.node(arg0);
	}

	public int nodeCount() {
		return _doc.nodeCount();
	}

	public Iterator nodeIterator() {
		return _doc.nodeIterator();
	}

	public void normalize() {
		_doc.normalize();
	}

	public Number numberValueOf(String arg0) {
		return _doc.numberValueOf(arg0);
	}

	public ProcessingInstruction processingInstruction(String arg0) {
		return _doc.processingInstruction(arg0);
	}

	public List processingInstructions() {
		return _doc.processingInstructions();
	}

	public List processingInstructions(String arg0) {
		return _doc.processingInstructions(arg0);
	}

	public boolean remove(Comment arg0) {
		return _doc.remove(arg0);
	}

	public boolean remove(Element arg0) {
		return _doc.remove(arg0);
	}

	public boolean remove(Node arg0) {
		return _doc.remove(arg0);
	}

	public boolean remove(ProcessingInstruction arg0) {
		return _doc.remove(arg0);
	}

	public boolean removeProcessingInstruction(String arg0) {
		return _doc.removeProcessingInstruction(arg0);
	}

	public List selectNodes(String arg0, String arg1, boolean arg2) {
		return _doc.selectNodes(arg0, arg1, arg2);
	}

	public List selectNodes(String arg0, String arg1) {
		return _doc.selectNodes(arg0, arg1);
	}

	public List selectNodes(String arg0) {
		return _doc.selectNodes(arg0);
	}

	public Object selectObject(String arg0) {
		return _doc.selectObject(arg0);
	}

	public Node selectSingleNode(String arg0) {
		return _doc.selectSingleNode(arg0);
	}

	public void setContent(List arg0) {
		_doc.setContent(arg0);
	}

	public void setDocType(DocumentType arg0) {
		_doc.setDocType(arg0);
	}

	public void setDocument(Document arg0) {
		_doc.setDocument(arg0);
	}

	public void setEntityResolver(EntityResolver arg0) {
		_doc.setEntityResolver(arg0);
	}

	public void setName(String arg0) {
		_doc.setName(arg0);
	}

	public void setParent(Element arg0) {
		_doc.setParent(arg0);
	}

	public void setProcessingInstructions(List arg0) {
		_doc.setProcessingInstructions(arg0);
	}

	public void setRootElement(Element arg0) {
		_doc.setRootElement(arg0);
	}

	public void setText(String arg0) {
		_doc.setText(arg0);
	}

	public void setXMLEncoding(String arg0) {
		_doc.setXMLEncoding(arg0);
	}

	public boolean supportsParent() {
		return _doc.supportsParent();
	}

	public String valueOf(String arg0) {
		return _doc.valueOf(arg0);
	}

	public void write(Writer arg0) throws IOException {
		_doc.write(arg0);
	}

}
