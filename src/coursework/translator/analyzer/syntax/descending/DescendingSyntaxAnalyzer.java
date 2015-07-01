package coursework.translator.analyzer.syntax.descending;

import coursework.translator.analyzer.syntax.blank.SyntaxAnalyzer;
import coursework.translator.analyzer.util.handler.ErrorHandler;
import coursework.translator.analyzer.util.handler.OutputHandler;

public class DescendingSyntaxAnalyzer extends SyntaxAnalyzer {

    private ProcedureHandler pHandler;
    private String[] errorStack;

    public DescendingSyntaxAnalyzer(OutputHandler outputHandler) {
        super(outputHandler);
    }

    @Override
    public void analyzeSyntax() {
        if (!outputHandler.getCodeWasTranslated()) {
            ErrorHandler.error(99, 0);
            return;
        }
        pHandler = new ProcedureHandler(outputHandler);

        if (pHandler.pProgram() != 0) {
            errorStack = new String[pHandler.getErrorCode().size()];
            for (int i = 0; i < pHandler.getErrorCode().size(); i++) {
                errorStack[i] = ErrorHandler.error(pHandler.getErrorCode().get(i), pHandler
                        .getErrorLine().get(i));
            }
        } else {
            System.out.println("Code was analyzed.");
            outputHandler.setCodeWasAnalyzed(true);
        }

    }

    @Override
    public String getErrorStack() {
        StringBuilder t = new StringBuilder();
        for (String y : errorStack) {
            t.append(y + "\n");
        }
        return t.toString();
    }

}
