package coursework.translator.analyzer.util.output;

public class TableItem {

	private String item;
	private int code;

	public TableItem(String item, int code) {
		this.item = item;
		this.code = code;
	}

	public String getItem() {
		return item;
	}

	public int getCode() {
		return code;
	}

	public String toString() {
		return code + "\t" + item + "\n";
	}

}
