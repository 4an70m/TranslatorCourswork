package coursework.translator.rpn.builder;

import coursework.translator.analyzer.syntax.Stack;
import java.io.IOException;
import java.util.LinkedList;

import coursework.translator.rpn.builder.util.PriorityTable;
import coursework.translator.rpn.builder.util.StackWriterItem;
import coursework.translator.rpn.builder.util.StackWriterTable;
import coursework.translator.rpn.builder.util.Workgroup;
import coursework.translator.analyzer.util.output.ConstantTable;
import coursework.translator.analyzer.util.output.IdentifierTable;
import coursework.translator.analyzer.util.output.outputtable.OutputTable;
import coursework.translator.analyzer.util.output.outputtable.OutputTableItem;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PriorityRPNBuilder extends RPNBuilder {

    private PriorityTable priorityTable;
    private Stack<String> stack;
    private StackWriterTable swTable;
    private int workM;
    private int newWorkM;
    Stack<Workgroup> callstack = new Stack<>();

    public PriorityRPNBuilder() throws IOException {
        super();
        priorityTable = new PriorityTable();
        stack = new Stack<>();
        rpn = new LinkedList<>();
        swTable = new StackWriterTable();
        workM = -1;
        newWorkM = -1;
    }

    public PriorityRPNBuilder(String priorityTablePath) throws IOException {
        super();
        priorityTable = new PriorityTable();
        stack = new Stack<>();
        rpn = new LinkedList<>();
        priorityTable.readTable(priorityTablePath);
        swTable = new StackWriterTable();
        swTable.readTable("test.wts");
        workM = -1;
        newWorkM = -1;
    }

    public PriorityRPNBuilder(String priorityTablePath, String stackWriterTablePath) throws IOException {
        super();
        priorityTable = new PriorityTable();
        stack = new Stack<>();
        rpn = new LinkedList<>();
        priorityTable.readTable(priorityTablePath);
        swTable = new StackWriterTable();
        swTable.readTable(stackWriterTablePath);
        workM = -1;
        newWorkM = -1;
    }

    @Override
    public boolean readStackWriterTable(String path) {
        File t = new File(path);
        if (t != null) {
            try {
                swTable.readTable(path);
            } catch (IOException ex) {
                Logger.getLogger(PriorityRPNBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean readPriorityTable(String path) {
        File t = new File(path);
        if (t != null) {
            try {
                priorityTable.readTable(path);
            } catch (IOException ex) {
                Logger.getLogger(PriorityRPNBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        // TODO:
        // make output to jOutput
        // make status
        return false;
    }

    @Override
    public void buildRPN() {
        rpnWasBuilt = false;
        if (!outputHandelr.getCodeWasAnalyzed()) {
            return;
        }
        OutputTable outTable = outputHandelr.getOutputTable();
        ConstantTable constantTable = outputHandelr.getConstantTable();
        IdentifierTable idTable = outputHandelr.getIdTable();
        int index = 0;

        // move to the body of a program (find begin)
        for (OutputTableItem t = outTable.get(index); t.getLexemeCode() != 3; t = outTable
                .get(++index))
			;
        index++;
        int fGroup = -1;
        StackWriterItem item = null;
        workM = -1;
        for (OutputTableItem t = outTable.get(index); index < outTable.size(); t = outTable
                .get(++index)) {

            if (t.getSubstring().equals("if")) {
                index = checkIf(outTable, constantTable, idTable, index);
                workM = newWorkM;
                continue;
            }

            if (t.getSubstring().equals("while")) {
                index = checkWhile(outTable, constantTable, idTable, index);
                workM = newWorkM;
                continue;
            }

            item = swTable.getItemByIdAndFuncGroup(t.getLexemeCode(), fGroup);

            routineRPNBiulding(t, item, outTable, constantTable, idTable);
        }
        System.out.println("RPN was built.");
        rpnWasBuilt = true;
    }

    private int checkIf(OutputTable outTable, ConstantTable constantTable,
            IdentifierTable idTable, int index) {
        // callstack shows the order of calling if after if

        int m = 0; // indexes, used by callstack
        int fGroup = -1;
        StackWriterItem item = null;
        int thisOperationM = workM + 2;
        boolean mainIf = true;
        for (OutputTableItem t = outTable.get(index); index < outTable.size(); t = outTable
                .get(++index)) {

            if (t.getSubstring().equals("if") && !mainIf) {
                index = checkIf(outTable, constantTable, idTable, index);
                workM = newWorkM;
                continue;
            }

            if (t.getSubstring().equals("if") && mainIf) {
                mainIf = false;
                fGroup = 2;
                newWorkM += 2;
                workM += 2;
                m++;
                callstack.push(new Workgroup("if", m));
            }
            if (t.getSubstring().equals("else")) {
                fGroup = 2;
                workM = thisOperationM;
                callstack.push(new Workgroup("else", m));
                m--;
            }
            if (t.getSubstring().equals("while")) {
                index = checkWhile(outTable, constantTable, idTable, index);
                workM = newWorkM;
                continue;
            }

            item = swTable.getItemByIdAndFuncGroup(t.getLexemeCode(), fGroup);

            if (t.getSubstring().equals("then")) {
                fGroup = -1;
            }

            routineRPNBiulding(t, item, outTable, constantTable, idTable);

            if (callstack.size() > 1) {
                Workgroup t2 = callstack.pop(); // stack top
                Workgroup t1 = callstack.pop(); // under stack top
                workM -= 2;
                if ((t1.getId() == t2.getId())
                        && (t.getSubstring().equals(";") || t.getSubstring()
                        .equals("end."))) {
                    return index;
                }

                // ; or end.
                if ((t1.getId() == t2.getId())
                        && (t.getSubstring().equals(";") || t.getSubstring()
                        .equals("end."))) {
                    m = callstack.size() / 2;
                    while (!callstack.isNull()
                            && ((t1.getId() == t2.getId()) && (t.getSubstring()
                            .equals(";") || t.getSubstring().equals(
                                    "end.")))) {
                        if (callstack.size() > 1) {
                            callstack.pop();
                            callstack.pop();
                            m -= 2;
                            workM -= 2;
                        } else {
                            break;
                        }
                    }

                    // else and else
                } else if (t1.getId() != t2.getId()
                        && (t1.getFunction().equals("else") && t2.getFunction()
                        .equals("else"))) {
                    item = swTable.getItemByIdAndFuncGroup(13, fGroup);
                    rpn.pollLast();
                    workM += 2;
                    rpn.offer(item.getRpnRepresentation(workM));
                    workM -= 2;
                    return index - 1;
                } else {
                    callstack.push(t1);
                    callstack.push(t2);
                    workM += 2;
                }
            }
            if (callstack.size() == 0) {
                item = swTable.getItemByIdAndFuncGroup(13, fGroup);
                rpn.pollLast();
                rpn.offer(item.getRpnRepresentation(thisOperationM));
                if (t.getSubstring().equals(";")) {
                    return index;
                }
                return index - 1;
            }
        }
        return index - 1;
    }

    private int checkWhile(OutputTable outTable, ConstantTable constantTable,
            IdentifierTable idTable, int index) {
        // callstack shows the order of calling while after while
        int fGroup = -1;
        StackWriterItem item = null;
        boolean firstEnter = true;
        for (OutputTableItem t = outTable.get(index); index < outTable.size(); t = outTable
                .get(++index)) {
            if (t.getSubstring().equals("while") && firstEnter) {
                workM += 2;
                newWorkM += 2;
                fGroup = 1;
            }

            item = swTable.getItemByIdAndFuncGroup(t.getLexemeCode(), fGroup);

            if (t.getSubstring().equals("do")) {
                fGroup = -1;
            }

            if (t.getSubstring().equals("if")) {
                index = checkIf(outTable, constantTable, idTable, index) - 1;
                continue;
            }

            routineRPNBiulding(t, item, outTable, constantTable, idTable);

            if (t.getSubstring().equals("do") && stack.peek().equals("while")) {
                stack.pollFirst();
            }

            if (t.getSubstring().equals("while") && !firstEnter) {
                index = checkWhile(outTable, constantTable, idTable, index);
                continue;
            }

            if (t.getSubstring().equals("else")) {
                index--;
            }

            if (t.getSubstring().equals("end.") || t.getSubstring().equals(";")
                    || t.getSubstring().equals("else")) {

                item = swTable.getItemByIdAndFuncGroup(13, 1);
                routineRPNBiulding(t, item, outTable, constantTable, idTable);
                workM -= 2;
                return index;
            }
            firstEnter = false;
        }
        return index;
    }

    public void routineRPNBiulding(OutputTableItem t, StackWriterItem item,
            OutputTable outTable, ConstantTable constantTable,
            IdentifierTable idTable) {
        // if items isn't written to stack or rpn
        if (item == null) {
            return;
        }
        // id or constant is immediately written to rpn
        if (item.getItemId() == 36) {
            rpn.offer(constantTable.getItemByIndex(t.getSpecializedNumber()));
            return;
        }
        if (item.getItemId() == 35) {
            rpn.offer(idTable.getItemByIndex(t.getSpecializedNumber()));
            return;
        }

        if (stack.isNull() && item.hasStackRepresentation()) {
            if (item.getStackRepresentation().equals("while")) {
                rpn.offer(item.getRpnRepresentation(workM));
            }
            stack.push(item.getStackRepresentation());
            return;
        }

        // routine stack-rpn movements
        if (!stack.isNull()) {

            if (stack.peek().equals("(") && t.getSubstring().equals("(")) {
                stack.push("(");
                return;
            }
            if (stack.peek().equals("[") && t.getSubstring().equals("[")) {
                stack.push("[");
                return;
            }

            int priorityItem = priorityTable
                    .getPriorityByItem(t.getSubstring());
            int priorityStack = priorityTable.getPriorityByItem(stack.peek());
            int compStackPrior = priorityTable.getComparisionByItem(stack
                    .peek());
            int compItemPrior = priorityTable.getComparisionByItem(t
                    .getSubstring());
            if (priorityStack >= priorityItem) {
                while (priorityStack >= priorityItem) {
                    if (compStackPrior > compItemPrior) {
                        break;
                    }
                    if (swTable.getItemByStackRep(stack.peek())
                            .hasRPNRepresentation()) {
                        rpn.offer(swTable.getItemByStackRep(stack.pop())
                                .getRpnRepresentation(workM));
                    } else {
                        stack.pop();
                    }
                    if (!stack.isNull()) {
                        priorityStack = priorityTable.getPriorityByItem(stack
                                .peek());
                        compItemPrior = priorityTable
                                .getComparisionByItem(stack.peek());
                    } else {
                        break;
                    }
                }
            }

            // popping opening bracket
            if (!stack.isNull()) {
                if (t.getSubstring().equals(")") && stack.peek().equals("(")) {
                    stack.pop();
                }
                if (t.getSubstring().equals("]") && stack.peek().equals("[")) {
                    stack.pop();
                }
            }
            // in case priorityStack < priorityItem
            if (item.hasStackRepresentation()) {
                stack.push(item.getStackRepresentation());
                return;
            }

        }
        if (item.hasRPNRepresentation()) {
            rpn.offer(item.getRpnRepresentation(workM));
        }
    }
}
