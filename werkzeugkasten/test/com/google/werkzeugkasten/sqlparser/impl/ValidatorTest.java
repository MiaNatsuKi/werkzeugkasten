package com.google.werkzeugkasten.sqlparser.impl;

import static com.google.werkzeugkasten.sqlparser.Status.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ValidatorTest {

	private Scanner scanner;
	private Validator validator;

	@Before
	public void setUp() {
		scanner = new Scanner();
		validator = new Validator();
	}

	@Test
	public void testPickAround() throws Exception {
		String data = "0123456789abc";
		char[] fullText = new char[data.length()];
		data.getChars(0, data.length(), fullText, 0);

		for (int i = 0; i < data.length(); i++) {
			String s = validator.pickAround(i, fullText);
			System.out.printf("%2d %s \n", i, s);
			assertEquals(Validator.PICK_LENGTH, s.length());
		}

		fullText = new char[] { '0', '1', '2' };
		String s = validator.pickAround(1, fullText);
		assertEquals(fullText.length, s.length());
	}

	@Test
	public void testExecute() {
		String[] errordatas = {
		// {が存在しない
				" /*? abc(aaa)   \r\t*/",
				// (が存在しない
				" /*? abc{*/ bbb /*? } */",
				// {が存在しない
				" /*? abc(aaa)   */",
				// {が存在しない
				" /*?abc(aaa){*/bbb/*?}}*/",
				// 識別子が存在しない
				" /*?(aaa){*/bbb/*?}*/",
				// {がアンマッチ
				" /*? abc(aaa){*/",
				// (がアンマッチ
				" /*?abc((aaa){*/bbb/*?}*/",
				// (がアンマッチ
				" /*? abc(aaa{*/bbb/*?}*/",
				// /*がアンマッチ
				" /*? abc(aaa){}",
				// }の位置がオカシイ
				" /*? abc(aaa){}*/",
				// (の位置がオカシイ
				" /*? abc(aaa)({*/bbb/*?}*/",
				// 識別子の場所がオカシイ
				" /*? abc(aaa)){*/bbb/*?}*/", };

		for (String s : errordatas) {
			SqlTokenizeContextImplForUnitTest parameter = new SqlTokenizeContextImplForUnitTest(
					s);
			assertEquals(Success, scanner.execute(parameter));
			assertEquals(s, Fail, validator.execute(parameter));
			System.out.println(parameter.getMessages());
			assertEquals(s, 1, parameter.getMessages().size());
		}
		String[] safedatas = { "/*? a(aaa)  { */bbb/*?  }  */",
				"/*? a(aaa)\t{*/aaa/*?}*/", "/*? a(aaa()) {*/ddd/*?}*/",
				"/*? BEGIN() {*/aaa/*?}*/", "/* comment comment */",
				"/*? aaa() {*/bbb/*? } ccc() {*/ddd/*? }*/" };
		for (String s : safedatas) {
			SqlTokenizeContextImplForUnitTest parameter = new SqlTokenizeContextImplForUnitTest(
					s);
			try {
				assertEquals(Success, scanner.execute(parameter));
				assertEquals(s, Success, validator.execute(parameter));
				assertEquals(s, 0, parameter.getMessages().size());
			} catch (AssertionError e) {
				System.out.printf("[ERR!] %1$s", parameter.getMessages()
						.toString());
				throw e;
			}
		}
	}
}
