/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework.translator.manager;

import coursework.translator.analyzer.syntax.blank.SyntaxAnalyzer;
import coursework.translator.analyzer.lexical.automat.AutomatLexicalAnalyzer;
import coursework.translator.analyzer.syntax.descending.DescendingSyntaxAnalyzer;
import coursework.translator.analyzer.util.handler.OutputHandler;
import coursework.translator.analyzer.util.output.TableItem;
import coursework.translator.analyzer.util.output.outputtable.OutputTableItem;
import coursework.translator.gui.FormController;
import coursework.translator.gui.Listener;
import coursework.translator.gui.MainIDEGui;
import coursework.translator.gui.PreferencesGui;
import coursework.translator.gui.TablesGui;
import coursework.translator.rpn.builder.PriorityRPNBuilder;
import coursework.translator.rpn.builder.RPNBuilder;
import coursework.translator.rpn.executor.SimpleRPNExecutor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class MainIDE implements Listener, FormController {
    
    private final String ANALYZER_STATUS_WAITING = "not analyzed";
    private final String ANALYZER_STATUS_FAIL = "analyze fail";
    private final String ANALYZER_STATUS_SUCCESS = "analyze succeed";
    private final String EXECUTOR_STATUS_WAITING = "stopped";
    private final String EXECUTOR_STATUS_WORKING = "in progress";
    private final String EXECUTOR_STATUS_SUCCESS = "succeded";
    private final String EXECUTOR_STATUS_FAILED = "failed";
    
    private PreferencesGui pGui;
    private final MainIDEGui mIdeGui;
    private AutomatLexicalAnalyzer la;
    private SyntaxAnalyzer sa;
    private RPNBuilder rpn;
    private File sourceCodeFile;
    private SimpleRPNExecutor exec;
    private boolean isSourceCodeSelected;
    private boolean finished;
    
    private String classTablePath;
    private String lexicTablePath;
    private String stateTablePath;
    private String sourceCodePath;
    private String priorityTablePath;
    private String stackWriterTablePath;
    
    public MainIDE() {
        try {
            la = new AutomatLexicalAnalyzer();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        mIdeGui = new MainIDEGui();
        mIdeGui.addListener(this);
        classTablePath = "default.classtable";
        lexicTablePath = "default.lexictable";
        stateTablePath = "default.lst";
        sourceCodePath = "default.fas";
        priorityTablePath = "default.pt";
        stackWriterTablePath = "default.wts";
        finished = false;
    }
    
    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticFormActions.buttonRun:
                run();
                break;
            case StaticFormActions.buttonPreferences:
                preferences();
                break;
            case StaticFormActions.buttonHelp:
                help();
                break;
            case StaticFormActions.buttonOpenFile:
                openFile();
                break;
            case StaticFormActions.buttonSaveFile:
                saveFile();
                break;
            case StaticFormActions.formLoaded:
                formLoaded();
                break;
            case StaticFormActions.buttonSubmit:
                submit();
                break;
            case StaticFormActions.buttonExit:
                System.exit(0);
                break;
            case StaticFormActions.buttonNewFile:
                newFile();
                break;
            case StaticFormActions.buttonStop:
                stop();
                break;
            case StaticFormActions.buttonSave:
                save();
                break;
            case StaticFormActions.buttonTables:
                openTables();
                break;
        }
    }
    
    @Override
    public void show() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mIdeGui.setVisible(true);
            }
        });
    }
    
    @Override
    public void hide() {
        mIdeGui.setVisible(false);
    }
    
    public void run() {
        updateStatus();
        finished = false;
        clearErrorPane();
        clearVarPane();
        clearVarInput();
        clearOutputPane();
        try {
            la = new AutomatLexicalAnalyzer();
            if (!la.setClassTable(classTablePath)) {
                JOptionPane.showMessageDialog(mIdeGui, "Propper .classtable was not found.\nPlease, locate propper .classtable file in 'Properties'.", "File not found!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!la.setLexicTable(lexicTablePath)) {
                JOptionPane.showMessageDialog(mIdeGui, "Propper .lexictable was not found.\nPlease, locate propper .lexictable file in 'Properties'.", "File not found!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!la.setStateReader(stateTablePath)) {
                JOptionPane.showMessageDialog(mIdeGui, "Propper .lst was not found.\nPlease, locate propper .lst file in 'Properties'.", "File not found!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            la.setSourceCodeInLines(mIdeGui.getjTextPaneCode().getText());
            
            la.readLexicAndClassTable();
            
            la.analyzeCode();
            
            OutputHandler handler = la.getOutputHandler();
            if (handler.getCodeWasTranslated()) {
                mIdeGui.getjLabelLAStatus().setText(ANALYZER_STATUS_SUCCESS);
            } else {
                mIdeGui.getjEditorPaneErrors().setText(la.getErrorString());
                mIdeGui.getjLabelLAStatus().setText(ANALYZER_STATUS_FAIL);
                return;
            }
            
            sa = new DescendingSyntaxAnalyzer(handler);
            sa.analyzeSyntax();
            if (handler.getCodeWasAnalyzed()) {
                mIdeGui.getjLabelSAStatus().setText(ANALYZER_STATUS_SUCCESS);
            } else {
                mIdeGui.getjEditorPaneErrors().setText(sa.getErrorStack());
                mIdeGui.getjLabelSAStatus().setText(ANALYZER_STATUS_FAIL);
                return;
            }
            
            rpn = new PriorityRPNBuilder();
            if (!rpn.readPriorityTable(priorityTablePath)) {
                JOptionPane.showMessageDialog(mIdeGui, "Propper .pt was not found.\nPlease, locate propper .pt file in 'Properties'.", "File not found!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!rpn.readStackWriterTable(stackWriterTablePath)) {
                JOptionPane.showMessageDialog(mIdeGui, "Propper .wts was not found.\nPlease, locate propper .wts file in 'Properties'.", "File not found!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            rpn.setOutputHandelr(la.getOutputHandler());
            rpn.buildRPN();
            
            exec = new SimpleRPNExecutor(la.getOutputHandler(), rpn);
            execution();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainIDE.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void preferences() {
        pGui = new PreferencesGui(classTablePath, lexicTablePath, stateTablePath, priorityTablePath, stackWriterTablePath);
        pGui.addListener(this);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                pGui.setVisible(true);
            }
        });
    }
    
    private void openFile() {
        clearVarPane();
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(mIdeGui.getjPanel1());
        jfc.setFileFilter(new FileFilter() {
            
            @Override
            public boolean accept(File f) {
                if (f.isFile()) {
                    return f.getName().contains(".fas");
                }
                return false;
            }
            
            @Override
            public String getDescription() {
                return "FAS sourcecode files";
            }
        }
        );
        
        sourceCodeFile = jfc.getSelectedFile();
        if (sourceCodeFile == null) {
            isSourceCodeSelected = false;
            JOptionPane.showMessageDialog(jfc, "The file has invalid extension (.fas needed).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        sourceCodePath = sourceCodeFile.getAbsolutePath();
        JTextPane codePane = mIdeGui.getjTextPaneCode();
        mIdeGui.getjTabbedPane1().setTitleAt(0, sourceCodeFile.getName());
        StringBuilder output = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(sourceCodeFile))) {
            String sline;
            while ((sline = br.readLine()) != null) {
                output.append(sline + "\n");
            }
        } catch (IOException e) {
        }
        codePane.setText(output.toString());
        isSourceCodeSelected = true;
    }
    
    private void saveFile() {
        JTextPane codePane = mIdeGui.getjTextPaneCode();
        String[] code = codePane.getText().split("\n");
        if (!isSourceCodeSelected) {
            JFileChooser jfc = new JFileChooser();
            jfc.showSaveDialog(mIdeGui.getjPanel1());
            sourceCodeFile = jfc.getSelectedFile();
        }
        if (sourceCodeFile == null) {
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(sourceCodeFile))) {
            for (String sline : code) {
                bw.write(sline);
                bw.newLine();
            }
        } catch (IOException e) {
        }
        mIdeGui.getjTabbedPane1().setTitleAt(0, sourceCodeFile.getName());
    }
    
    private void help() {
        String helpString = "This is a Translator with built-in Executor of a Language FAS (Fisher PASCAL).\n"
                + "The syntax of the language is very similar to the pascal as you can see in the simpliest program, "
                + "which can be written in this language."
                + "\nThe IDE provides basic things, like: "
                + "\n-area for code input"
                + "\n-for error output"
                + "\n-for variables input"
                + "\n-for output"
                + "\n-and area for all the variable"
                + "\n\nIf you find any bugs, please contact mrpsihopath@gmail.com\nFisher Alex. 2015.";
        JOptionPane.showMessageDialog(mIdeGui, helpString, "Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void formLoaded() {
        clearCodePane();
    }
    
    private void submit() {
        String readVal = mIdeGui.getjTextFieldVarValue().getText();
        if (!readVal.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")) {
            mIdeGui.getjLabelExecutionStatus().setText(EXECUTOR_STATUS_FAILED);
            mIdeGui.getjEditorPaneErrors().setText("Error. Invalid input. Number rexpected - got '" + readVal + "'");
            return;
        }
        exec.setReadVal(Double.parseDouble(readVal));
        clearVarInput();
        execution();
    }
    
    private void newFile() {
        saveFile();
        clearCodePane();
        clearErrorPane();
        clearVarPane();
        clearVarInput();
    }
    
    private void clearCodePane() {
        mIdeGui.getjTabbedPane1().setTitleAt(0, "New source");
        mIdeGui.getjTextPaneCode().setText("program test\nvar ,a : real\nbegin\n\ta = 1\n\t// Operations\nend.");
    }
    
    private void clearErrorPane() {
        mIdeGui.getjEditorPaneErrors().setText("");
    }
    
    private void clearVarPane() {
        ArrayList<String[]> table = new  ArrayList<>();
        String[] headline = new String[2];
        headline[0] = "Variable";
        headline[1] = "Value";
        mIdeGui.getjTableVarMap().setModel(outputToTable(headline, table));
    }
    
    private void clearVarInput() {
        mIdeGui.getjLabelVarName().setEnabled(false);
        mIdeGui.getjTextFieldVarValue().setEnabled(false);
        mIdeGui.getjButtonSubmit().setEnabled(false);
        mIdeGui.getjLabelVarName().setText("Variable:");
        mIdeGui.getjTextFieldVarValue().setText("0");
    }
    
    private void putVarsInVarlist(Map<String, Double> map) {
        String[] headline = {"Variable", "Value"};
        Set<Entry<String, Double>> set = map.entrySet();
        ArrayList<String[]> array = new ArrayList<>();
        for (Entry<String, Double> t : set) {
            String[] temp = new String[2];
            temp[0] = t.getKey();
            temp[1] = String.valueOf(t.getValue());
            array.add(temp);
        }
        
        mIdeGui.getjTableVarMap().setModel(outputToTable(headline, array));
    }
    
    private static DefaultTableModel outputToTable(String[] headline, ArrayList<String[]> table) {
        DefaultTableModel model = new DefaultTableModel();
        String[][] temp = new String[table.size()][headline.length];
        for (int i = 0; i < table.size(); i++) {
            temp[i] = table.get(i);
        }
        model.setDataVector(temp, headline);
        return model;
    }
    
    private void putVarsInInput(String readKey) {
        mIdeGui.getjLabelVarName().setEnabled(true);
        mIdeGui.getjTextFieldVarValue().setEnabled(true);
        mIdeGui.getjButtonSubmit().setEnabled(true);
        mIdeGui.getjTextFieldVarValue().setText(readKey);
        mIdeGui.getjTextFieldVarValue().setText("0");
    }
    
    public void execution() {
        finished = exec.execute();
        if (exec.isNeedToWrite()) {
            String t = "";
            for (String s : exec.getOutput()) {
                t += s + "\n";
            }
            mIdeGui.getjTextAreaOutput().setText(t);
        }
        
        if (finished) {
            mIdeGui.getjLabelExecutionStatus().setText(EXECUTOR_STATUS_SUCCESS);
        } else {
            mIdeGui.getjLabelExecutionStatus().setText(EXECUTOR_STATUS_WORKING);
        }
        putVarsInVarlist(exec.getVarMap());
        
        if (exec.isNeedToRead()) {
            putVarsInInput(exec.getReadKey());
        } else {
            finished = true;
            mIdeGui.getjLabelExecutionStatus().setText(EXECUTOR_STATUS_SUCCESS);
        }
    }
    
    public void updateStatus() {
        mIdeGui.getjLabelExecutionStatus().setText(EXECUTOR_STATUS_WAITING);
        mIdeGui.getjLabelSAStatus().setText(ANALYZER_STATUS_WAITING);
        mIdeGui.getjLabelLAStatus().setText(ANALYZER_STATUS_WAITING);
    }
    
    private void stop() {
        if (!finished) {
            finished = true;
            clearVarInput();
            mIdeGui.getjLabelExecutionStatus().setText(EXECUTOR_STATUS_WAITING);
        }
        
    }
    
    private void save() {
        classTablePath = pGui.getClassTablePath();
        lexicTablePath = pGui.getLexicTablePath();
        stateTablePath = pGui.getStateTablePath();
        priorityTablePath = pGui.getPriorityTablePath();
        stackWriterTablePath = pGui.getStackWriterTablePath();
        pGui.setVisible(false);
    }
    
    private void clearOutputPane() {
        mIdeGui.getjTextAreaOutput().setText("");
    }
    
    private void openTables() {
        if (!la.isCodeTranslated()) {
            JOptionPane.showMessageDialog(mIdeGui, "Translation was not made. Please, run program before viewing the table.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        final TablesGui tGui = new TablesGui();
        // constant table
        ArrayList<String[]> tableArray = new ArrayList<>();
        String[] headline = new String[2];
        headline[0] = "Code";
        headline[1] = "Item";
        String[] item;
        JTable table = tGui.getjTableConstantTable();
        for (TableItem tableItem : la.getOutputHandler().getConstantTable().getConstantTable()) {
            item = new String[2];
            item[0] = String.valueOf(tableItem.getCode());
            item[1] = tableItem.getItem();
            tableArray.add(item);
        }
        table.setModel(outputToTable(headline, tableArray));
        //identifier table
        tableArray = new ArrayList<>();
        headline = new String[2];
        headline[0] = "Code";
        headline[1] = "Item";
        table = tGui.getjTableIdentifierTable();
        for (TableItem tableItem : la.getOutputHandler().getIdTable().getIdentifierTable()) {
            item = new String[2];
            item[0] = String.valueOf(tableItem.getCode());
            item[1] = tableItem.getItem();
            tableArray.add(item);
        }
        table.setModel(outputToTable(headline, tableArray));
        //output table
        tableArray = new ArrayList<>();
        headline = new String[4];
        headline[0] = "Line";
        headline[1] = "Item";
        headline[2] = "Lexeme code";
        headline[3] = "Type";
        table = tGui.getjTableOutputTable();
        for (OutputTableItem tableItem : la.getOutputHandler().getOutputTable().getOutputTable()) {
            item = new String[4];
            item[0] = String.valueOf(tableItem.getLineNumber());
            item[1] = tableItem.getSubstring();
            item[2] = String.valueOf(tableItem.getLexemeCode());
            switch (tableItem.getSpecializedNumber()) {
                case -1:
                    item[3] = "";
                    break;
                case 1:
                    item[3] = "constant";
                    break;
                case 2:
                    item[3] = "identifier";
                    break;
                
            }
            tableArray.add(item);
        }
        table.setModel(outputToTable(headline, tableArray));
        //rpn
        tableArray = new ArrayList<>();
        headline = new String[1];
        headline[0] = "Command";
        table = tGui.getjTableRPNTable();
        for (String tableItem : rpn.getRpn()) {
            item = new String[1];
            item[0] = tableItem;
            tableArray.add(item);
        }
        table.setModel(outputToTable(headline, tableArray));
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                tGui.setVisible(true);
            }
        });
        
    }
    
}
