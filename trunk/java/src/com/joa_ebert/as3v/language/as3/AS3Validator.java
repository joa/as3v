package com.joa_ebert.as3v.language.as3;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.debug.DebugEventListener;
import org.antlr.runtime.debug.ParseTreeBuilder;
import org.antlr.runtime.tree.ParseTree;

import com.joa_ebert.as3v.language.as3.antlr.AS3Lexer;
import com.joa_ebert.as3v.language.as3.antlr.AS3Parser;
import com.joa_ebert.as3v.projectModel.ICompilationUnit;
import com.joa_ebert.as3v.validator.IValidator;
import com.joa_ebert.as3v.validator.XMLBuilder;
import com.joa_ebert.as3v.validator.messages.ValidatorError;
import com.joa_ebert.as3v.validator.messages.ValidatorWarning;

public class AS3Validator implements IValidator {

	@Override
	public int errorAt(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int errorCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator<ValidatorError> errorIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(ICompilationUnit compilationUnit) throws Exception {
		final boolean USE_XML_BUILDER = true;
		System.out.println("[i] Validating " + compilationUnit.unitPath() + " (" + compilationUnit.filePath() + ")");

		DebugEventListener listener;

		ParseTreeBuilder builder = new ParseTreeBuilder("AS3.g");
		XMLBuilder xmlBuilder = new XMLBuilder(false);

		listener = USE_XML_BUILDER ? xmlBuilder : builder;

		AS3Lexer lexer = new AS3Lexer(new ANTLRReaderStream(
				new InputStreamReader(new FileInputStream(compilationUnit
						.filePath()), "UTF8")));

		AS3Parser parser = new AS3Parser(new CommonTokenStream(lexer), listener);

		try {

			parser.compilationUnit();

			if (USE_XML_BUILDER) {
				if (xmlBuilder.valid())
					xmlBuilder.print(System.out);
			} else {
				showParseTree(builder.getTree(), 0);
			}

		} catch (Exception exception) {
			System.out.println(exception);
		}

		if (USE_XML_BUILDER) {
			if (!xmlBuilder.valid()) {
				throw new Exception("One or more syntax errors in "
						+ compilationUnit.unitPath());
			}
		}
	}

	@SuppressWarnings("all")
	private void showParseTree(ParseTree tree, int depth) {
		if (null == tree)
			return;

		int m = depth;// + 2;

		for (int i = 0; i < m; ++i)
			System.out.print(" ");

		System.out.println(tree.getText());

		if (null != tree.getChildren()) {
			List children = tree.getChildren();

			for (Iterator iter = children.iterator(); iter.hasNext();) {
				showParseTree((ParseTree) iter.next(), depth + 1);
			}
		}
	}

	@Override
	public int warningAt(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int warningCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator<ValidatorWarning> warningIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
