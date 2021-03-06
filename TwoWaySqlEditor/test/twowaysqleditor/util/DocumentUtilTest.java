package twowaysqleditor.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;

import junit.framework.TestCase;

public class DocumentUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testBackto() {
		IDocument doc = new Document();
		String s = "aaa/*b/bb";
		doc.set(s);
		assertEquals("b/bb", DocumentUtil.backto(doc, s.length() - 1,
				new DocumentUtil.Detector() {
					public boolean detect(IDocument d, int index)
							throws BadLocationException {
						int f = d.getChar(index);
						if (-1 < --index) {
							int s = d.getChar(index);
							return f == '*' && s == '/';
						}
						return false;
					}
				}));
	}

	public void testBacktoWhitespace() {
		IDocument doc = new Document();
		String s = "aaa bbb";
		doc.set(s);
		assertEquals("bbb", DocumentUtil.backto(doc, s.length() - 1,
				DocumentUtil.whitespace));
	}

	public void testBacktoLineDelims() {
		IDocument doc = new Document();
		String s = "  /*END*/\r\n  /*\r\n/*END*/";
		doc.set(s);
		assertEquals("/*", DocumentUtil
				.backto(doc, 14, DocumentUtil.whitespace));
	}

	public void testSkip() throws Exception {
		IDocument doc = new Document();
		String s = " \t \r\n*/ ";
		skip(doc, s);
		s = " \t \r\n*/";
		skip(doc, s);
	}

	private void skip(IDocument doc, String s) throws BadLocationException {
		doc.set(s);
		int index = DocumentUtil.skip(doc, 0, DocumentUtil.whitespace);
		assertEquals(5, index);
		int end = DocumentUtil.skip(doc, index, DocumentUtil.ignoreWhitespace);
		assertEquals(7, end);
		assertEquals("*/", doc.get(index, end - index));
		assertEquals('*', doc.getChar(index));
	}

}
