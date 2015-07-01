package coursework.translator.analyzer.util.handler;

public class ErrorHandler {

    public static String error(int errorCode, int line) {
        // Todo: implement error handler
        String errorString;
        switch (errorCode) {
            case 1: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Unknown symbol.";
                break;
            }
            case 2: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Not declared identificator.";
                break;
            }
            case 3: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Not declared label.";
                break;
            }
            case 99: {
                errorString = "Code was not translated.";
                break;
            }
            case 100: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". 'Program' not found";
                break;
            }
            case 101: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Identifier list not found.";
                break;
            }
            case 102: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Identifier not found.";
                break;
            }
            case 103: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Type not found.";
                break;
            }
            case 104: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Operation list not found.";
                break;
            }
            case 105: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Operation not found.";
                break;
            }
            case 106: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Logical expression not found.";
                break;
            }
            case 107: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Logical terminal not found.";
                break;
            }
            case 108: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Logical multiplier not found.";
                break;
            }
            case 109: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Logical ratio not found.";
                break;
            }
            case 110: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Math expression not found.";
                break;
            }
            case 111: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Math terminal not found.";
                break;
            }
            case 112: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Math multiplier not found.";
                break;
            }
            case 113: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". Program name not found.";
                break;
            }
            case 114: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". 'var' not found.";
                break;
            }
            case 115: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". 'begin' not found";
                break;
            }
            case 116: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". 'end.' not found.";
                break;
            }

            case 118: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". 'do' not found.";
                break;
            }
            case 119: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". ':' not found.";
                break;
            }
            case 120: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". '(' not found.";
                break;
            }
            case 121: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". ')' not found.";
                break;
            }
            case 122: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". 'then' not found.";
                break;
            }
            case 123: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". 'else' not found.";
                break;
            }
            case 124: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". '=' not found.";
                break;
            }
            case 125: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". ']' not found.";
                break;
            }
            case 126: {
                errorString = "Line: " + line + ". Error " + errorCode
                        + ". ',' not found.";
                break;
            }
            case -1: {
                errorString = "Line: " + (line - 1) + " has an error.";
                break;
            }
            default: {
                errorString = "Unknown condition";
            }
        }

        System.out.println(errorString);
        return errorString;
    }

}
