package coursework.translator.rpn.builder.util;

public class Workgroup {

	private int id;
	private String function;

	public Workgroup() {
		// TODO Auto-generated constructor stub
	}

	public Workgroup(String function, int id) {
		super();
		this.id = id;
		this.function = function;
	}

	public int getId() {
		return id;
	}

	public String getFunction() {
		return function;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public String toString() {
		return "Workgroup [id=" + id + ", function=" + function + "]\n";
	}

}
