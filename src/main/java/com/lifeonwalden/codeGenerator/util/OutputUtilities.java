package com.lifeonwalden.codeGenerator.util;

public class OutputUtilities {
	private static final String lineSeparator;

	static {
		String ls = System.getProperty("line.separator");
		if (ls == null) {
			ls = "\n";
		}
		lineSeparator = ls;
	}

	private OutputUtilities() {
		super();
	}

	public static StringBuilder javaIndent(StringBuilder sb, int indentLevel) {
		for (int i = 0; i < indentLevel; i++) {
			sb.append("    ");
		}

		return sb;
	}

	public static StringBuilder textIndent(StringBuilder sb, int indentLevel) {
		for (int i = 0; i < indentLevel; i++) {
			sb.append("  ");
		}

		return sb;
	}

	public static StringBuilder newLine(StringBuilder sb) {
		sb.append(lineSeparator);

		return sb;
	}
}
