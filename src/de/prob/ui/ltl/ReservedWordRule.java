package de.prob.ui.ltl;

import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class ReservedWordRule implements IPredicateRule {

	private List<String> allowedCharacterSequences;

	protected String word;
	protected IToken token;

	public ReservedWordRule(String word, IToken token, List<String> allowedCharacterSequences) {
		this.word = word;
		this.token = token;
		this.allowedCharacterSequences = allowedCharacterSequences;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		Reader reader = new Reader(scanner);
		boolean wordFound = false;

		if (word.charAt(0) == reader.peek()) {
			if (checkSeqBefore(reader)) {
				String readWord = reader.read(word.length());

				if (word.equals(readWord)) {
					wordFound = checkSeqAfter(reader);
				}
			}
		}

		if (wordFound) {
			return token;
		}
		reader.unreadAll();
		return Token.UNDEFINED;
	}

	@Override
	public IToken getSuccessToken() {
		return token;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return evaluate(scanner);
	}

	boolean checkSeqBefore(Reader reader) {
		if (allowedCharacterSequences == null) {
			return true;
		}
		if (reader.isBOF()) {
			return true;
		}
		for (String seq : allowedCharacterSequences) {
			int n = seq.length();

			int unread = reader.unread(n);
			if (n == unread) {
				String before = reader.read(n);
				if (seq.equals(before)) {
					return true;
				}
			} else {
				reader.read(unread);
			}
		}

		return false;
	}

	boolean checkSeqAfter(Reader reader) {
		if (allowedCharacterSequences == null) {
			return true;
		}
		if (reader.isEOF()) {
			return true;
		}
		for (String seq : allowedCharacterSequences) {
			int n = seq.length();

			String after = reader.read(n);
			reader.unread(n);
			if (seq.equals(after)) {
				return true;
			}
		}

		return false;
	}

	class Reader {
		public static final int BOF = -2;
		public static final int EOF = ICharacterScanner.EOF;

		private final ICharacterScanner scanner;
		private int readCount = 0;

		Reader(ICharacterScanner scanner) {
			this.scanner = scanner;
		}

		int peek() {
			int c = scanner.read();
			scanner.unread();
			return c;
		}

		String read(int count) {
			StringBuilder builder = new StringBuilder();
			int c = 0;
			for (int i = 0; i < count && c != EOF; i++) {
				c = scanner.read();
				if (c != EOF) {
					builder.append((char) c);
				}
			}

			if (builder.length() != count) {
				int length = builder.length();
				while (length-- > 0) {
					scanner.unread();
				}
				return null;
			}
			readCount += count;
			return builder.toString();
		}

		int unread(int count) {
			int unread = 0;
			while (count-- > 0 && scanner.getColumn() > 0) {
				scanner.unread();
				readCount--;
				unread++;
			}
			return unread;
		}

		void unreadAll() {
			while (readCount-- > 0) {
				scanner.unread();
			}
			readCount = 0;
		}

		boolean isBOF() {
			return scanner.getColumn() == 0;
		}

		boolean isEOF() {
			boolean eof = scanner.read() == EOF;
			scanner.unread();
			return eof;
		}

	}

}