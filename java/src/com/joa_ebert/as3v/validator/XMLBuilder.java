package com.joa_ebert.as3v.validator;

import java.io.OutputStream;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.debug.DebugEventListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLBuilder implements DebugEventListener {

	private boolean _debug;
	private boolean _valid;

	private DocumentBuilder _builder;
	private Document _document;
	private Node _node;
	private Stack<StringBuilder> _tokenStack;

	public XMLBuilder() throws Exception {
		this(false);
	}

	public XMLBuilder(boolean debug) throws Exception {
		_builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		_document = _builder.newDocument();
		_document.setXmlVersion("1.0");
		_document.setXmlStandalone(true);
		_tokenStack = new Stack<StringBuilder>(); 
		_debug = debug;
		_valid = true;
	}

	public boolean valid() {
		return _valid;
	}

	public void print(OutputStream out) throws Exception {
		serialize(_document, out);
	}

	private void serialize(Document doc, OutputStream out) throws Exception {

		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tfactory.newTransformer();
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			serializer.transform(new DOMSource(doc), new StreamResult(out));
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void LT(int i, Token t) {
		if (_debug)
			System.out.println("LT: " + i + ", " + t.toString());
	}

	@Override
	public void LT(int i, Object t) {
		if (_debug)
			System.out.println("LT: " + i + ", " + t.toString());
	}

	@Override
	public void addChild(Object root, Object child) {
		if (_debug)
			System.out.println("addChild: " + root.toString() + ", "
					+ child.toString());
	}

	@Override
	public void becomeRoot(Object newRoot, Object oldRoot) {
		if (_debug)
			System.out.println("becomeRoot: " + newRoot.toString() + ", "
					+ oldRoot.toString());
	}

	@Override
	public void beginBacktrack(int level) {
		if (_debug)
			System.out.println("beginBacktrack: " + level);
	}

	@Override
	public void beginResync() {
		if (_debug)
			System.out.println("beginResync");
	}

	@Override
	public void commence() {
		if (_debug)
			System.out.println("commence");
	}

	@Override
	public void consumeHiddenToken(Token t) {
		if (_debug)
			System.out.println("consumeHiddenToken: " + t.toString());
	}

	@Override
	public void consumeNode(Object t) {
		if (_debug)
			System.out.println("consumeNode: " + t.toString());
	}

	@Override
	public void consumeToken(Token t) {
		if (_debug)
			System.out.println("consumeToken: " + t.getText());
		
		if(null != t)
		{
			String text = t.getText();
			
			if(null != text)
			{
				if(!text.equals("\n") && !text.equals("\r") && !text.equals("\r\n"))
					_tokenStack.peek().append(text);
			}
		}
	}

	@Override
	public void createNode(Object t) {
		if (_debug)
			System.out.println("createNode: " + t.toString());
	}

	@Override
	public void createNode(Object node, Token token) {
		if (_debug)
			System.out.println("createNode: " + node.toString() + ", "
					+ token.toString());
	}

	@Override
	public void endBacktrack(int level, boolean success) {
		if (_debug)
			System.out.println("endBacktrack: " + level + ", " + success);
	}

	@Override
	public void endResync() {
		if (_debug)
			System.out.println("endResync");
	}

	@Override
	public void enterAlt(int alt) {
		if (_debug)
			System.out.println("enterAlt: " + alt);
	}

	@Override
	public void enterDecision(int decisionNumber) {
		if (_debug)
			System.out.println("enterDecision: " + decisionNumber);
	}

	@Override
	public void enterRule(String grammarFileName, String ruleName) {
		if (_debug)
			System.out.println("enterRule: " + grammarFileName + ", "
					+ ruleName);

		Element element = _document.createElement(ruleName);
		
		_tokenStack.push(new StringBuilder());

		if (null == _node)
			_node = _document.appendChild(element);
		else
			_node = _node.appendChild(element);
	}

	@Override
	public void enterSubRule(int decisionNumber) {
		if (_debug)
			System.out.println("enterSubRule: " + decisionNumber);
	}

	@Override
	public void errorNode(Object t) {
		if (_debug)
			System.err.println("errorNode: " + t.toString());
	}

	@Override
	public void exitDecision(int decisionNumber) {
		if (_debug)
			System.out.println("exitDecision: " + decisionNumber);
	}

	@Override
	public void exitRule(String grammarFileName, String ruleName) {
		if (_debug)
			System.out
					.println("exitRule: " + grammarFileName + ", " + ruleName);
		if (null != _node)
		{
			if(_tokenStack.peek().length() > 0)
			{
				_node.appendChild(_document.createCDATASection(_tokenStack.peek().toString()));				
			}
			
			_node = _node.getParentNode();
		}
		
		_tokenStack.pop();
	}

	@Override
	public void exitSubRule(int decisionNumber) {
		if (_debug)
			System.out.println("exitSubRule: " + decisionNumber);
	}

	@Override
	public void location(int line, int pos) {
		if (_debug)
			System.out.println("location: " + line + ", " + pos);
	}

	@Override
	public void mark(int marker) {
		if (_debug)
			System.out.println("mark: " + marker);
	}

	@Override
	public void nilNode(Object t) {
		if (_debug)
			System.out.println("nilNode: " + t.toString());
	}

	@Override
	public void recognitionException(RecognitionException e) {
		if (_debug)
			System.err.println("recognitionException: " + e.getMessage());

		if (e.charPositionInLine != 0 && e.line != 1)
			_valid = false;
	}

	@Override
	public void rewind() {
		if (_debug)
			System.out.println("rewind");
	}

	@Override
	public void rewind(int marker) {
		if (_debug)
			System.out.println("rewind: " + marker);
	}

	@Override
	public void semanticPredicate(boolean result, String predicate) {
		if (_debug)
			System.out.println("semanticPredicate: " + result + ", "
					+ predicate);
	}

	@Override
	public void setTokenBoundaries(Object t, int tokenStartIndex,
			int tokenStopIndex) {
		if (_debug)
			System.out.println("setTokenBoundaries: " + t + ", "
					+ tokenStartIndex + ", " + tokenStopIndex);
	}

	@Override
	public void terminate() {
		if (_debug)
			System.out.println("terminate");
	}
}
