package coursework.translator.analyzer.util.tablereader.lexic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LexicTableReader {

    private File lexictable;
    private BufferedReader reader;
    private LexicVocabulary vocabulary;

    public LexicTableReader() throws FileNotFoundException {
        this("test.lexictable");
    }

    public LexicTableReader(String path) throws FileNotFoundException {
        lexictable = new File(path);
        vocabulary = new LexicVocabulary();
    }

    public LexicTableReader(File file) throws FileNotFoundException {
        lexictable = file;
        vocabulary = new LexicVocabulary();
    }

    public void readLexicTable() throws IOException {
        reader = new BufferedReader(new FileReader(lexictable));
        String line = reader.readLine();
        if (!line.trim().equals("<lexictable>")) {
            return;
        }
        while (!(line = reader.readLine()).trim().equals("<!lexictable>")) {
            String[] item = line.trim().split("~");
            vocabulary.addLexeme(Integer.parseInt(item[0]), item[1]);
        }

        reader.close();
    }

    public int checkLexeme(String lexeme) {
        if (lexeme.equals(" ")) {
            return findSpace();
        }
        return vocabulary.findInVocabulary(lexeme);
    }

    public int findIdentifier() {
        return vocabulary.findInVocabulary("<identifier>");
    }

    public int findSpace() {
        return vocabulary.findInVocabulary("<space>");
    }

    public int findConstant() {
        return vocabulary.findInVocabulary("<constant>");
    }

    public LexicVocabulary getVocabulary() {
        return vocabulary;
    }
}
