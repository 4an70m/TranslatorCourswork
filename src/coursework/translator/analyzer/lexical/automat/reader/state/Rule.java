package coursework.translator.analyzer.lexical.automat.reader.state;

public class Rule {

	private String type;
	private int state;
	private int alternativeState;

	public Rule(String type, int state, int alternativeState) {
		super();
		this.type = type;
		this.state = state;
		this.alternativeState = alternativeState;
	}

	public String getType() {
		return type;
	}

	public int getState() {
		return state;
	}

	public int getAlternativeState() {
		return alternativeState;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setAlternativeState(int alternativeState) {
		this.alternativeState = alternativeState;
	}

	@Override
	public String toString() {
		return "Rule [type=" + type + ", state=" + state
				+ ", alternativeState=" + alternativeState + "]";
	}

}
