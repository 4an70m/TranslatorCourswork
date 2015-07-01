package coursework.translator.analyzer.syntax.blank;

import coursework.translator.analyzer.util.handler.OutputHandler;

public abstract class SyntaxAnalyzer {

    protected OutputHandler outputHandler;

    public SyntaxAnalyzer(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    public abstract void analyzeSyntax();

    public abstract String getErrorStack();

}
