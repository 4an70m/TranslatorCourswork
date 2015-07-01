package coursework.translator.analyzer.lexical.automat.reader.state;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StateReader {

    private ArrayList<State> table;
    private File f;
    private BufferedReader br;

    public StateReader() {
        table = new ArrayList<>();
        f = new File("default.lst");
    }    
    
    public StateReader(String path) {
        table = new ArrayList<>();
        f = new File(path);
    }

    public void readStates() throws IOException {
        br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        Rule ruleForAdding = null;
        if (!line.equals("<lexicstatetable>")) {
            return;
        }
        boolean addedRule = false;
        for (line = br.readLine(); !line.equals("<!lexicstatetable>"); line = br
                .readLine()) {
            addedRule = false;
            String[] temp = line.split(" ");
            if (!table.isEmpty()) {
                State tempState = table.get(table.size() - 1);
                if (temp[0].equals(String.valueOf(tempState.getState()))) {
                    String type = temp[1];
                    int state = Integer.valueOf(temp[2]);
                    int alternativeState = Integer.valueOf(temp[3]);

                    ruleForAdding = new Rule(type, state, alternativeState);
                    addedRule = true;

                }
                if (addedRule) {
                    table.get(table.size() - 1).addRule(ruleForAdding);
                }
            }
            if (!addedRule) {
                State tempState = new State(Integer.valueOf(temp[0]));
                String type = temp[1];
                int state = Integer.valueOf(temp[2]);
                int alternativeState = Integer.valueOf(temp[3]);
                ruleForAdding = new Rule(type, state, alternativeState);
                tempState.addRule(ruleForAdding);
                table.add(tempState);
            }
        }
    }

    public ArrayList<State> getTable() {
        return table;
    }

    public State getState(int state) {
        for (State st : table) {
            if (st.getState() == state) {
                return st;
            }
        }
        return null;
    }

}
