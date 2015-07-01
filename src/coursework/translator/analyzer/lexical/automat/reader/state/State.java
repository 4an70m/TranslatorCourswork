package coursework.translator.analyzer.lexical.automat.reader.state;

import java.util.ArrayList;

public class State {

	private int state;
	private ArrayList<Rule> rules;

	public State(int state) {

		this.state = state;
		rules = new ArrayList<Rule>();
	}

	public int getState() {
		return state;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}

	public void addRule(Rule rule) {
		rules.add(rule);
	}
	
	@Override
	public String toString() {
		return "State [state=" + state + ", rules=" + rules + "]";
	}

}
