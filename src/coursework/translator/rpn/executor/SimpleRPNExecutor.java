package coursework.translator.rpn.executor;

import java.util.LinkedList;
import java.util.Scanner;

import coursework.translator.rpn.builder.RPNBuilder;
import coursework.translator.analyzer.util.handler.OutputHandler;
import java.util.ArrayList;

public class SimpleRPNExecutor extends RPNExecutor {

    private LinkedList<String> constrpn;
    private int globalIndex;
    private Scanner sc;
    private boolean isStopped;
    private ArrayList<String> output;
    private boolean needToRead;
    private boolean needToWrite;

    private String readKey;
    private Double readVal;
    private int index = -1;
    private LinkedList<String> workStack;

    @SuppressWarnings("unchecked")
    public SimpleRPNExecutor(OutputHandler oHandler, RPNBuilder rpnBuilder) {
        super(oHandler, rpnBuilder);
        constrpn = (LinkedList<String>) rpnBuilder.getRpn().clone();
        globalIndex = -1;
        output = new ArrayList<>();
        rpn = (LinkedList<String>) rpnBuilder.getRpn().clone();
        workStack = workStack = new LinkedList<>();
    }

    public void stop() {
        isStopped = true;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public ArrayList<String> getOutput() {
        return output;
    }

    public boolean isNeedToRead() {
        return needToRead;
    }

    public boolean isNeedToWrite() {
        return needToWrite;
    }

    public String getReadKey() {
        return readKey;
    }

    public void setReadVal(double readVal) {
        this.readVal = readVal;
        varMap.replace(readKey, readVal);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean execute() {

        isStopped = false;
        needToRead = false;
        if (!rpnBuilder.getRpnWasBuilt()) {
            return false;
        }
        index++;
        
        // main processing loop
        // write wait() here and control step by step flow in the mainIDE with notify!!!

        for (; index < rpn.size(); index++) {
            if (isStopped) {
                return false;
            }

            globalIndex++;
            String curItem = rpn.get(index);
            if (idTable.isIdentifier(curItem)) {
                workStack.push(curItem);
                continue;
            }

            if (isNumber(curItem)) {
                workStack.push(curItem);
                continue;
            }

            if (isBoolean(curItem)) {
                workStack.push(curItem);
                continue;
            }

            index = mainSwitch(index, curItem, workStack);

            if (needToRead) {
                isStopped = false;
                return false;
            }

        }
        isStopped = true;

        System.out.println("Finished " + varMap);
        return true;
    }

    private int writeOperation(int index, LinkedList<String> workStack) {
        String key = null;
        while (true) {
            index--;
            rpn.pollFirst();
            key = workStack.pollLast();
            Double val = varMap.get(key);
            if (val == null) {
                break;
            }
            output.add(key + " = " + val);
            if (output.size() > 10000) {
                isStopped = true;
            }
            needToWrite = true;
        }
        return index;
    }

    private int readOperation(int index, LinkedList<String> workStack) {
        needToRead = true;
        readKey = rpn.pollFirst();
        rpn.pollFirst();
        workStack.pollLast();
        double value = 0;


        /*
         if (sc == null) {
         sc = new Scanner(System.in);
         }
         try {
         String key = rpn.pollFirst();
         rpn.pollFirst();
         double value = 0;
         System.out.print(key + " : ");
         if (sc.hasNextDouble()) {
         value = sc.nextDouble();
         }
         varMap.replace(key, value);
         } catch (NumberFormatException e) {
         index--;
         }*/
        index-=2;
        //globalIndex++;
        return index;
    }

    private int assignment(int index, LinkedList<String> workStack) {
        rpn.remove(index);
        rpn.remove(index - 1);
        rpn.remove(index - 2);

        String id = workStack.pollLast();
        double value = getDoubleValue(workStack.pop());
        varMap.replace(id, value);
        index -= 3;
        return index;
    }

    private int arythmeticExpr(int index, String string,
            LinkedList<String> workStack) {
        rpn.remove(index);
        rpn.remove(index - 1);
        rpn.remove(index - 2);

        double value1 = getDoubleValue(workStack.pollFirst());
        double value2 = getDoubleValue(workStack.pollFirst());
        double result = 0;
        switch (string) {
            case "+":
                result = value1 + value2;
                break;
            case "-":
                result = value2 - value1;
                break;
            case "*":
                result = value1 * value2;
                break;
            case "/":
                result = value2 / value1;
                break;
        }

        rpn.add(index - 2, String.valueOf(result));
        workStack.pop();
        index -= 4;
        globalIndex -= 2;
        return index;
    }

    private int logicExpr(int index, String string, LinkedList<String> workStack) {
        rpn.remove(index);
        rpn.remove(index - 1);
        rpn.remove(index - 2);

        double value2 = getDoubleValue(workStack.pollFirst());
        double value1 = getDoubleValue(workStack.pollFirst());
        boolean result = false;
        switch (string) {
            case "<":
                result = value1 < value2;
                break;
            case ">":
                result = value1 > value2;
                break;
            case "<=":
                result = value1 <= value2;
                break;
            case ">=":
                result = value2 >= value1;
                break;
            case "==":
                result = value2 == value1;
                break;
            case "!=":
                result = value2 != value1;
                break;
        }
        rpn.add(index - 2, String.valueOf(result));
        index -= 3;
        globalIndex--;
        return index;
    }

    private double getDoubleValue(String item) {
        try {
            return Double.parseDouble(item);
        } catch (NumberFormatException e) {
            return Double.valueOf(varMap.get(item));
        }
    }

    private static boolean isNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBoolean(String bool) {

        if (bool.equals("true") || bool.equals("false")) {
            return true;
        }
        return false;

    }

    private int mainSwitch(int index, String curItem,
            LinkedList<String> workStack) {
        switch (curItem) {
            case "=": {
                index = assignment(index, workStack);
                break;
            }
            case "+": {
                index = arythmeticExpr(index, "+", workStack);
                break;
            }
            case "-": {
                index = arythmeticExpr(index, "-", workStack);
                break;
            }
            case "*": {
                index = arythmeticExpr(index, "*", workStack);
                break;
            }
            case "/": {
                index = arythmeticExpr(index, "/", workStack);
                break;
            }
            case "<": {
                index = logicExpr(index, "<", workStack);
                break;
            }
            case ">": {
                index = logicExpr(index, ">", workStack);
                break;
            }
            case "!=": {
                index = logicExpr(index, "!=", workStack);
                break;
            }
            case "==": {
                index = logicExpr(index, "==", workStack);
                break;
            }
            case "<=": {
                index = logicExpr(index, "<=", workStack);
                break;
            }
            case ">=": {
                index = logicExpr(index, ">=", workStack);
                break;
            }
            case "and": {
                index = logicCondition(index, "and", workStack);
                break;
            }
            case "or": {
                index = logicCondition(index, "or", workStack);
                break;
            }
            case "not": {
                index = logicCondition(index, "not", workStack);
                break;
            }
            case "WRITE": {
                index = writeOperation(index, workStack);
                break;
            }

            case "READ": {
                index = readOperation(index, workStack);
                break;
            }
            default: {
                if (curItem.contains("UPL")) {
                    Boolean cond = Boolean.parseBoolean(workStack.pop());
                    rpn.pollFirst();
                    rpn.pollFirst();

                    if (!cond) {
                        // false
                        String t = curItem.split("UPL")[0];
                        int newIndex = labels.get(t + ":");
                        if (globalIndex < newIndex) {
                            // int diff = newIndex - globalIndex;
                            while (globalIndex < newIndex) {
                                globalIndex++;
                                rpn.pollFirst();
                            }
                            return index - 2;
                        } else {
                            // true
                            // ToDo: finish this block
                            globalIndex = addListFromPos(newIndex);
                        }

                    } else {
                        return index - 2;
                    }
                }

                if (curItem.contains("BP")) {
                    rpn.pollFirst();
                    String t = curItem.split("BP")[0];
                    int newIndex = labels.get(t + ":");
                    if (globalIndex < newIndex) {
                        // int diff = newIndex - globalIndex;
                        while (globalIndex < newIndex) {
                            globalIndex++;
                            rpn.pollFirst();
                        }
                        return -1;
                    } else {
                        // ToDo: finish this block
                        globalIndex = addListFromPos(newIndex);
                        return -1;
                    }
                }

                if (labels.containsKey(curItem)) {
                    rpn.pollFirst();
                    index--;
                }
            }
        }
        return index;
    }

    private int logicCondition(int index, String string,
            LinkedList<String> workStack) {
        boolean secondCond = Boolean.parseBoolean(workStack.pollFirst());
        rpn.pollFirst();
        boolean firstCond = false;
        boolean result = false;

        switch (string) {
            case "and":
                firstCond = Boolean.parseBoolean(workStack.pollFirst());
                rpn.pollFirst();
                result = firstCond && secondCond;
                break;

            case "or":
                firstCond = Boolean.parseBoolean(workStack.pollFirst());
                rpn.pollFirst();
                result = firstCond || secondCond;
                break;

            case "not":
                result = !secondCond;
                break;
        }
        rpn.pollFirst();
        rpn.add(index - 2, String.valueOf(result));
        index -= 3;
        globalIndex--;
        return index;
    }

    private int addListFromPos(int newIndex) {
        for (int i = globalIndex; i >= newIndex; i--) {
            rpn.push(constrpn.get(i));
        }
        return newIndex - 1;
    }

}
