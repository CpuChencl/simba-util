package com.simba.framework.util.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.simba.model.constant.ConstantData;

/**
 * xml工具类
 * 
 * @author caozj
 * 
 */
public class XmlUtil {

	/**
	 * 根据class类型，获取XStream对象
	 * 
	 * @param cs
	 * @return
	 */
	private static XStream getXStream(Class<?>... cs) {
		XStream xStream = new XStream();
		for (Class<?> c : cs) {
			xStream.alias(c.getSimpleName().toLowerCase(), c);
			xStream.processAnnotations(c);
		}
		return xStream;
	}

	/**
	 * 将list转化成XML
	 * 
	 * @param list
	 * @return
	 */
	public static String fromList(List<?> list) {
		if (CollectionUtils.isEmpty(list)) {
			return StringUtils.EMPTY;
		}
		Class<?> c = list.get(0).getClass();
		XStream xStream = getXStream(c);
		return xStream.toXML(list);
	}

	/**
	 * 将对象转化成XML
	 * 
	 * @param object
	 * @return
	 */
	public static String fromObject(Object object) {
		XStream xStream = getXStream(object.getClass());
		return xStream.toXML(object);
	}

	/**
	 * 将xml转化成对象
	 * 
	 * @param xml
	 * @param t
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T toOject(String xml, Class<T> t) {
		XStream xStream = getXStream(t);
		return (T) xStream.fromXML(xml);
	}

	/**
	 * 读取xml文件
	 * 
	 * @param xmlFile
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document parseXMLFile(String xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		return db.parse(new File(xmlFile));
	}

	/**
	 * 读取xml文件
	 * 
	 * @param xml
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document parseXMLStream(InputStream xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		return db.parse(xml);
	}

	/**
	 * 读取xml文件
	 * 
	 * @param xmlContent
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document parseXMLContent(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		return db.parse(IOUtils.toInputStream(xmlContent, ConstantData.DEFAULT_CHARSET));
	}

	/**
	 * 将node节点转化成xml字符串
	 * 
	 * @param node
	 * @return
	 * @throws TransformerException
	 */
	public static String toXml(Node node) throws TransformerException {
		Transformer transformer = getTransformer();
		DOMSource source = new DOMSource();
		source.setNode(node);
		StreamResult result = new StreamResult();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		result.setWriter(pw);
		transformer.transform(source, result);
		return sw.toString();
	}

	/**
	 * 查找节点，并返回第一个符合条件节点
	 * 
	 * @param express
	 *            xpath表达式
	 * @param source
	 * @return
	 * @throws XPathExpressionException
	 */
	public static Node selectSingleNode(String express, Object source) throws XPathExpressionException {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		Node result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
		return result;
	}

	/**
	 * 查找节点，返回符合条件的节点集
	 * 
	 * @param express
	 *            xpath表达式
	 * @param source
	 * @return
	 * @throws XPathExpressionException
	 */
	public static NodeList selectNodes(String express, Object source) throws XPathExpressionException {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		NodeList result = (NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
		return result;
	}

	/**
	 * 将Document输出到文件
	 * 
	 * @param fileName
	 * @param doc
	 * @throws FileNotFoundException
	 * @throws TransformerException
	 */
	public static void saveXML(String fileName, Document doc) throws FileNotFoundException, TransformerException {
		Transformer transformer = getTransformer();
		DOMSource source = new DOMSource();
		source.setNode(doc);
		StreamResult result = new StreamResult();
		result.setOutputStream(new FileOutputStream(fileName));
		transformer.transform(source, result);
	}

	private static Transformer getTransformer() throws TransformerConfigurationException {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty("encoding", ConstantData.DEFAULT_CHARSET);
		transformer.setOutputProperty("indent", "yes");
		return transformer;
	}
}
