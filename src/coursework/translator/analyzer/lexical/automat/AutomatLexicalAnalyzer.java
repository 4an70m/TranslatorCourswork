package coursework.translator.analyzer.lexical.automat;

import java.io.FileNotFoundException;
import java.io.IOException;

import coursework.translator.analyzer.lexical.automat.reader.state.Rule;
import coursework.translator.analyzer.lexical.automat.reader.state.State;
import coursework.translator.analyzer.lexical.automat.reader.state.StateReader;
import coursework.translator.analyzer.lexical.blank.LexicalAnalyzer;
import coursework.translator.analyzer.util.handler.ErrorHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AutomatLexicalAnalyzer extends LexicalAnalyzer {

    private StateReader stateReader;
    private String[] codeInString;
    private String errorString;

    public AutomatLexicalAnalyzer() throws FileNotFoundException {
        super("", "", "");
        stateReader = new StateReader();
    }

    public AutomatLexicalAnalyzer(String sourceCodePath, String lexicTablePath, String classTablePath) throws FileNotFoundException {
        super(sourceCodePath, lexicTablePath, classTablePath);
        stateReader = new StateReader();
    }

    public boolean setStateReader(String path) {
        File t = new File(path);
        if (t.exists()) {
            stateReader = new StateReader(path);
            return true;
        }
        return false;
    }

    public void setSourceCodeInLines(String source) {
        codeInString = source.split("\n");
    }

    public void readSourceCodeFromFile() {
        if (sourceCode.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
                String sline;
                int i = 0;
                while ((sline = br.readLine()) != null) {
                    codeInString[i] = sline;
                    i++;
                }
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void readLexicAndClassTable() {
        super.readLexicAndClassTable();
        try {
            stateReader.readStates();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void analyzeCode() throws IOException {
        if (codeInString.length == 0) {
            return;
        }
        outputHandler.setCodeWasTranslated(false);
        int row = 1;
        State currentState = null;
        StringBuilder lexeme = new StringBuilder();
        String classType = null;
        String symb = null;
        String prevClassType = null;
        boolean needBack = false;
        boolean isComment = false;
        for (String sline : codeInString) {
            int state = 1;
            sline = sline.trim() + "  ";
            if (sline.contains("//")) {
                row++;
                continue;
            }
            if (sline.contains("/*")) {
                isComment = true;
                continue;
            }
            if (isComment && sline.contains("*/")) {
                row++;
                continue;
            }

            for (int i = 0; i < sline.length();) {
                needBack = true;
                currentState = stateReader.getState(state);
                prevClassType = classType;
                symb = String.valueOf(sline.charAt(i));
                classType = clReader.getVocabulary().checkType(symb);
                lexeme.append(symb);

                for (Rule rule : currentState.getRules()) {
                    state = rule.getAlternativeState();

                    if (classType.equals("L") && symb.equals("E")) {
                        state = 5;
                        break;
                    }

                    if (rule.getType().equals(classType)) {
                        state = rule.getState();
                        if (rule.getAlternativeState() != -1) {
                            needBack = false;
                            break;
                        }
                    }
                    if (state > -1) {
                        break;
                    }
                }

                i++;

                if (state == 0) {

                    if (prevClassType != classType && needBack) {
                        lexeme.deleteCharAt(lexeme.length() - 1);
                        i--;
                    }

                    int result = ltReader.checkLexeme(lexeme.toString().trim()
                            .equals("") ? " " : lexeme.toString().trim());
                    if (lexeme.toString().trim().equals("begin")) {
                        outputHandler.setBegin();
                    }
                    if (result == ltReader.findSpace()) {
                        lexeme = new StringBuilder("<space>");
                    } else {
                        errorCode = outputHandler.output(row, result, lexeme
                                .toString().trim(), ltReader.findIdentifier(),
                                ltReader.findConstant(), clReader);
                    }
                    if (errorCode != 0) {
                        errorString = ErrorHandler.error(errorCode, row);
                        return;
                    }
                    state = 1;
                    lexeme = new StringBuilder();
                    continue;
                }

                if (state <= -1) {
                    errorCode = 1;
                    errorString = ErrorHandler.error(errorCode, row);
                    return;
                }

            }
            row++;
        }
        outputHandler.outputToFile();
        outputHandler.setCodeWasTranslated(true);
        System.out.println("Code was translated.");

    }

    public String getErrorString() {
        return errorString;
    }
    
    
}
