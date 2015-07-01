package coursework.translator.analyzer.util.tablereader.lexic;

import java.util.ArrayList;

public class LexicVocabulary {

	private ArrayList<TableItem> vocabulary;

	public LexicVocabulary() {
		vocabulary = new ArrayList<>();
	}

	public void addLexeme(int code, String lexeme) {
		TableItem tableItem = new TableItem(code, lexeme);
		vocabulary.add(tableItem);
	}
	
	public int findInVocabulary(String lexeme)
	{
		for(TableItem currentItem : vocabulary)
		{
			if(currentItem.getLexeme().equals(lexeme))
				return currentItem.getItemCode();
		}
		return -1;
	}

    public ArrayList<TableItem> getVocabulary() {
        return vocabulary;
    }
        
        
}
