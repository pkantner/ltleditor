package de.prob.ui.ltl.util;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.junit.Assert;
import org.junit.Test;

public class CharacterScannerReaderTest {

	@Test
	public void testRead() {
		CharacterScannerReader reader = createReader("true or false");
		Assert.assertTrue(reader.isBOF());
		Assert.assertFalse(reader.isEOF());

		Assert.assertEquals('t', reader.read());
		Assert.assertEquals('r', reader.read());
		Assert.assertEquals('u', reader.read());
		Assert.assertEquals('e', reader.read());
		Assert.assertEquals(' ', reader.read());
	}

	@Test
	public void testUnread() {
		CharacterScannerReader reader = createReader("true");
		Assert.assertTrue(reader.isBOF());
		Assert.assertFalse(reader.isEOF());

		reader.unread();

		Assert.assertTrue(reader.isBOF());
		Assert.assertFalse(reader.isEOF());

		Assert.assertEquals('t', reader.read());
		Assert.assertEquals('r', reader.read());
		reader.unread();
		Assert.assertEquals('r', reader.read());
		Assert.assertEquals('u', reader.read());
		Assert.assertEquals('e', reader.read());
		Assert.assertEquals(CharacterScannerReader.EOF, reader.read());
		reader.unread();
		Assert.assertEquals(CharacterScannerReader.EOF, reader.read());
	}

	@Test
	public void testPeek() {
		CharacterScannerReader reader = createReader("true");

		Assert.assertTrue(reader.isBOF());
		Assert.assertFalse(reader.isEOF());
		Assert.assertEquals('t', reader.peek());
		Assert.assertTrue(reader.isBOF());
		Assert.assertFalse(reader.isEOF());

		Assert.assertEquals('t', reader.peek());

		Assert.assertEquals('t', reader.read());
		Assert.assertEquals('r', reader.read());
		Assert.assertEquals('u', reader.read());

		Assert.assertEquals('e', reader.peek());
		reader.unread();
		Assert.assertEquals('u', reader.peek());
	}

	@Test
	public void testReadBackward() {
		CharacterScannerReader reader = createReader("true");

		Assert.assertTrue(reader.isBOF());
		Assert.assertFalse(reader.isEOF());

		Assert.assertEquals(CharacterScannerReader.BOF, reader.readBackward());
		Assert.assertEquals(CharacterScannerReader.BOF, reader.readBackward());

		Assert.assertEquals('t', reader.read());
		Assert.assertEquals('t', reader.readBackward());
		Assert.assertEquals(CharacterScannerReader.BOF, reader.readBackward());

		Assert.assertEquals('t', reader.read());
		Assert.assertEquals('r', reader.read());
		Assert.assertEquals('u', reader.peek());
		Assert.assertEquals('r', reader.readBackward());
		Assert.assertEquals('t', reader.readBackward());

		Assert.assertEquals('t', reader.read());
		Assert.assertEquals('r', reader.read());
		Assert.assertEquals('u', reader.read());
		Assert.assertEquals('e', reader.read());
		Assert.assertEquals(CharacterScannerReader.EOF, reader.read());
		Assert.assertEquals(CharacterScannerReader.EOF, reader.read());
		Assert.assertEquals(CharacterScannerReader.EOF, reader.readBackward());
		Assert.assertEquals('e', reader.readBackward());
	}

	@Test
	public void testJump() {
		CharacterScannerReader reader = createReader("true or false");

		Assert.assertEquals(1, reader.jump(1));
		Assert.assertEquals('r', reader.read());

		Assert.assertEquals(3, reader.jump(3));
		Assert.assertEquals('o', reader.read());
		Assert.assertEquals('r', reader.read());

		Assert.assertEquals(-3, reader.jump(-3));
		Assert.assertEquals(' ', reader.read());
		Assert.assertEquals('o', reader.read());

		Assert.assertEquals(0, reader.jump(0));
		Assert.assertEquals('r', reader.read());
		Assert.assertEquals(' ', reader.read());

		Assert.assertEquals(5, reader.jump(10));
		Assert.assertEquals(CharacterScannerReader.EOF, reader.read());

		Assert.assertEquals(-14, reader.jump(-20));
		Assert.assertEquals('t', reader.read());
	}

	@Test
	public void testReadString() {
		CharacterScannerReader reader = createReader("true or false");

		Assert.assertEquals("true", reader.readString(4));
		reader.jump(1);
		Assert.assertEquals("or", reader.readString(2));
		reader.jump(1);
		Assert.assertEquals("false", reader.readString(5));

		reader.jump(-2);
		Assert.assertNull(reader.readString(5));
		Assert.assertEquals("se", reader.readString(2));
	}

	@Test
	public void testSaveResetPosition() {
		CharacterScannerReader reader = createReader("true or false");

		Assert.assertEquals("true", reader.readString(4));
		reader.savePosition();
		Assert.assertEquals(" or", reader.readString(3));
		reader.resetPosition();
		Assert.assertEquals(" or", reader.readString(3));

		reader.jump(-5);
		reader.resetPosition();
		Assert.assertEquals(" or", reader.readString(3));
	}

	// Helper
	public CharacterScannerReader createReader(final String text) {
		ICharacterScanner scanner = new ICharacterScanner() {
			private int column = 0;
			@Override
			public void unread() {
				column = Math.max(--column, 0);
			}

			@Override
			public int read() {
				int c = CharacterScannerReader.EOF;
				if (column < text.length()) {
					c = text.charAt(column);
				}
				column = Math.min(++column, text.length() + 1);
				return c;
			}

			@Override
			public char[][] getLegalLineDelimiters() {
				return null;
			}

			@Override
			public int getColumn() {
				return column;
			}
		};
		return new CharacterScannerReader(scanner);
	}

}
