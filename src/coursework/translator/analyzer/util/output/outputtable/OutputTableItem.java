package coursework.translator.analyzer.util.output.outputtable;

public class OutputTableItem {

	private int lineNumber;
	private String substring;
	private int lexemeCode;
	private int specializedNumber;

	public OutputTableItem(int lineNumber, String substring, int lexemeCode,
			int specializedNumber) {
		this.lineNumber = lineNumber;
		this.substring = substring;
		this.lexemeCode = lexemeCode;
		this.specializedNumber = specializedNumber;
	}

	public String toString() {
		return lineNumber + "\t" + substring + "\t" + lexemeCode + "\t"
				+ specializedNumber + "\n";
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getSubstring() {
		return substring;
	}

	public int getLexemeCode() {
		return lexemeCode;
	}

	public int getSpecializedNumber() {
		return specializedNumber;
	}
}
