package coursework.translator.analyzer.util.tablereader.lexic;

public class TableItem {

	private int code;
	private String lexeme;

	public TableItem(int code, String lexeme) {
		this.code = code;
		this.lexeme = lexeme.trim();
	}

	public String getLexeme() {
		return lexeme;
	}

	public int getItemCode() {
		return code;
	}

}
