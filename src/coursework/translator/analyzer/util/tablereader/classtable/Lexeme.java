package coursework.translator.analyzer.util.tablereader.classtable;

import java.util.HashSet;

public class Lexeme {

	private HashSet<String> symbols;
	private String type;

	public Lexeme(String type) {
		symbols = new HashSet<>();
		this.type = type;
	}

	public void addLexeme(String symbol) {
		symbols.add(symbol);
	}

	public void addLexeme(String[] symbols) {
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] != "")
				this.symbols.add(symbols[i]);
		}
	}

	public boolean hasLexeme(String lexeme) {
		if (symbols.contains(lexeme))
			return true;
		return false;
	}

	public String getType() {
		return type;
	}

    public HashSet<String> getSymbols() {
        return symbols;
    }
        
        

}
