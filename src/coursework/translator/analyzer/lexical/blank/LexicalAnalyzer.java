package coursework.translator.analyzer.lexical.blank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import coursework.translator.analyzer.util.handler.OutputHandler;
import coursework.translator.analyzer.util.output.ConstantTable;
import coursework.translator.analyzer.util.output.IdentifierTable;
import coursework.translator.analyzer.util.output.outputtable.OutputTable;
import coursework.translator.analyzer.util.tablereader.classtable.ClasstableReader;
import coursework.translator.analyzer.util.tablereader.lexic.LexicTableReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LexicalAnalyzer {

    protected File sourceCode;
    protected BufferedReader reader;
    protected OutputHandler outputHandler;
    protected ClasstableReader clReader;
    protected LexicTableReader ltReader;
    protected int errorCode;

    public LexicalAnalyzer(String sourceCodePath, String lexicTablePath,
            String classTablePath) throws FileNotFoundException {
        sourceCode = new File(sourceCodePath);
        clReader = new ClasstableReader(classTablePath);
        ltReader = new LexicTableReader(lexicTablePath);
        errorCode = 0;
        outputHandler = new OutputHandler();
    }

    public void readLexicAndClassTable() {
        try {
            clReader.readClasstable();
            ltReader.readLexicTable();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public abstract void analyzeCode() throws IOException;

    public ConstantTable getConstantTable() {
        return outputHandler.getConstantTable();
    }

    public IdentifierTable getIdTable() {
        return outputHandler.getIdTable();
    }

    public OutputTable getOutputTable() {
        return outputHandler.getOutputTable();
    }

    public OutputHandler getOutputHandler() {
        return outputHandler;
    }

    public boolean isCodeTranslated() {
        return outputHandler.getCodeWasTranslated();
    }

    public File getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(File sourceCode) {
        this.sourceCode = sourceCode;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public ClasstableReader getClReader() {
        return clReader;
    }

    public void setClReader(ClasstableReader clReader) {
        this.clReader = clReader;
    }

    public LexicTableReader getLtReader() {
        return ltReader;
    }

    public void setLtReader(LexicTableReader ltReader) {
        this.ltReader = ltReader;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean setSourceCode(String path) {
        File t = new File(path);
        if (t.exists()) {
            sourceCode = t;
            return true;
        }
        return false;
    }

    public boolean setClassTable(String path) {
        File t = new File(path);
        if (t.exists()) {
            try {
                clReader = new ClasstableReader(path);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        return false;
    }

    public boolean setLexicTable(String path) {
        File t = new File(path);
        if (t.exists()) {
            try {
                ltReader = new LexicTableReader(t);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        return false;
    }

}
