package coursework.translator.rpn.executor;

import java.util.HashMap;
import java.util.LinkedList;

import coursework.translator.rpn.builder.RPNBuilder;
import coursework.translator.analyzer.util.handler.OutputHandler;
import coursework.translator.analyzer.util.output.ConstantTable;
import coursework.translator.analyzer.util.output.IdentifierTable;
import coursework.translator.analyzer.util.output.TableItem;
import java.util.Map;

public abstract class RPNExecutor {

	protected LinkedList<String> rpn;
	protected HashMap<String, Double> varMap;
	protected IdentifierTable idTable;
	protected ConstantTable cTable;
	protected RPNBuilder rpnBuilder;
	protected HashMap<String, Integer> labels;

	public RPNExecutor(OutputHandler oHandler, RPNBuilder rpnBuilder) {
		this.rpnBuilder = rpnBuilder;
		idTable = oHandler.getIdTable();
		cTable = oHandler.getConstantTable();
		varMap = new HashMap<>();
		labels = new HashMap<>();
		if (!rpnBuilder.getRpnWasBuilt())
			return;
		fillVarMap();
		fillLabelMap();
	}

	private void fillLabelMap() {
		int i = 0;
		for (String t : rpnBuilder.getRpn()) {
			if (t.matches("\\S*m[0-9]*:")) {
				String key = t.substring(t.lastIndexOf("m"));
				labels.put(key, i);
			}
			i++;
		}
	}

	private void fillVarMap() {
		for (TableItem t : idTable.getIdentifierTable()) {
			varMap.put(t.getItem(), 0d);
		}
	}

	public abstract boolean execute();

    public Map<String, Double> getVarMap() {
        return varMap;
    }

  
}
