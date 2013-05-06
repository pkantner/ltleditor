package de.prob.ui.ltl.util;

import org.eclipse.jface.text.rules.ICharacterScanner;

public class CharacterScannerReader {

	public static final int BOF = -2;
	public static final int EOF = ICharacterScanner.EOF;

	private final ICharacterScanner scanner;
	private int position = 0;

	public CharacterScannerReader(ICharacterScanner scanner) {
		this.scanner = scanner;
	}

	public int read() {
		return scanner.read();
	}

	public void unread() {
		scanner.unread();
	}

	public int peek() {
		int c = read();
		unread();
		return c;
	}

	public int readBackward() {
		if (isBOF()) {
			return BOF;
		}
		unread();
		int c = read();
		unread();
		return c;
	}

	public String readString(int n) {
		StringBuilder builder = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			if (isEOF()) {
				jump(-i);
				return null;
			}
			int c = read();
			builder.append((char) c);
		}

		return builder.toString();
	}

	public int jump(int n) {
		int realJumps = 0;
		if (n < 0) {
			for (int i = 0; i < -n && !isBOF(); i++) {
				unread();
				realJumps--;
			}
		} else if (n > 0) {
			for (int i = 0; i < n && !isEOF(); i++) {
				read();
				realJumps++;
			}
		}
		return realJumps;
	}

	public boolean isBOF() {
		return scanner.getColumn() == 0;
	}

	public boolean isEOF() {
		return peek() == EOF;
	}

	public void savePosition() {
		position = Math.abs(jump(-Integer.MAX_VALUE));
		jump(position);
	}

	public void resetPosition() {
		jump(-Integer.MAX_VALUE);
		jump(position);
	}

}
