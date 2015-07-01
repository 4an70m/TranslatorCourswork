package coursework.translator.analyzer.util.tablereader.classtable;

import java.util.ArrayList;

public class ClassVocabulary {

	private ArrayList<Lexeme> vocabulary;

	public ClassVocabulary() {
		vocabulary = new ArrayList<Lexeme>();
	}

	public void addLexeme(String type, String rawSymbols) {
		Lexeme lexeme = new Lexeme(type.trim());
		lexeme.addLexeme(rawSymbols.trim().split(""));
		vocabulary.add(lexeme);
	}
	
	public String checkType(String lexeme)
	{
		for (Lexeme currentLexeme : vocabulary)
		{
			if(currentLexeme.hasLexeme(lexeme))
			return currentLexeme.getType();
		}
		return "unknown";
	}

    public ArrayList<Lexeme> getVocabulary() {
        return vocabulary;
    }
        
        
}
