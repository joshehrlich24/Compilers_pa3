package edu.jmu.decaf;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

class MyDecafLexer extends DecafLexer {
	public Queue<Token> lex(final BufferedReader input, final String filename)
			throws IOException, InvalidTokenException {

		final Queue<Token> tokens = new ArrayDeque<Token>();

		if (input == null) {
			throw new IllegalArgumentException("Invalid token input stream.");
		}

		if (filename == null) {
			throw new IllegalArgumentException("Invalid filename.");
		}

		this.addIgnoredPattern("\\s+");
		this.addIgnoredPattern("//.*");
		this.addTokenPattern(Token.Type.HEX, "0x(0|[1-9a-fA-F][0-9a-fA-F]*)");
		this.addTokenPattern(Token.Type.DEC, "0|[1-9][0-9]*");
		this.addTokenPattern(Token.Type.STR, "\"([^\\\\\"]+|\\\\[nt\"\\\\])*\"");
		this.addTokenPattern(Token.Type.SYM, "<=|>=|&&|\\|\\||==|!=");
		this.addTokenPattern(Token.Type.SYM, "\\(|\\)|\\[|\\]|\\{|\\}|,|\\.|;|=|\\+|-|\\*|/|%|!|<|>");
		this.addTokenPattern(Token.Type.ID, "[a-zA-Z]\\w*");

		final String[] KEYWORDS = { "def", "if", "while", "return", "break", "continue", "else", "int", "bool", "void",
				"true", "false" };
		final String[] RESERVED = { "for", "callout", "class", "interface", "extends", "implements", "new", "this",
				"string", "float", "double", "null" };

		final Set<String> keywords = new HashSet<String>(Arrays.asList(KEYWORDS));
		final Set<String> reserved = new HashSet<String>(Arrays.asList(RESERVED));

		int lineNumber = 1;

		String line;

		while ((line = input.readLine()) != null) {

			
			final SourceInfo source = new SourceInfo(filename, lineNumber);
			final StringBuffer buffer = new StringBuffer(line);
			this.discardIgnored(buffer);

			while (buffer.length() > 0) {

				final Token t = this.nextToken(buffer);

				if (t == null) {
					throw new InvalidTokenException("Invalid text at " + source.toString());
				}

				if (keywords.contains(t.text)) {
					t.type = Token.Type.KEY;
				}

				else if (reserved.contains(t.text)) {
					throw new InvalidTokenException(
							"Invalid use of reserved word \"" + t.text + "\" at " + source.toString());
				}
				t.source = source;
				tokens.add(t);
				this.discardIgnored(buffer);
			}
			++lineNumber;
		}
		input.close();
		return tokens;
	}
}