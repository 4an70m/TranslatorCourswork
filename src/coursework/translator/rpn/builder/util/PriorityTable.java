package coursework.translator.rpn.builder.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PriorityTable {

	private ArrayList<PriorityTableItem> tableItems;

	public PriorityTable() {
		tableItems = new ArrayList<>();
	}

	public PriorityTable(ArrayList<PriorityTableItem> tableItems) {
		super();
		this.tableItems = tableItems;
	}

	public ArrayList<PriorityTableItem> getTableItems() {
		return tableItems;
	}

	public void setTableItems(ArrayList<PriorityTableItem> tableItems) {
		this.tableItems = tableItems;
	}

	public PriorityTableItem getItemByPriority(int priority) {
		for (PriorityTableItem t : tableItems) {
			if (t.getPriority() == priority)
				return t;
		}
		return null;
	}

	public Integer getPriorityByItem(String item) {
		for (PriorityTableItem t : tableItems) {
			for (String i : t.getItems()) {
				if (i.equals(item))
					return t.getPriority();
			}
		}
		return null;
	}
	
	public Integer getComparisionByItem(String item) {
		for (PriorityTableItem t : tableItems) {
			for (String i : t.getItems()) {
				if (i.equals(item))
					return t.getComparison();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "PriorityTable [tableItems=" + tableItems + "]";
	}

	public void readTable(String path) throws IOException {
		BufferedReader rd = new BufferedReader(new FileReader(path));
		String sline = rd.readLine();
		if (!sline.equals("<prioritytable>")) {
			rd.close();
			return;
		}
		String[] temp;
		PriorityTableItem pti;
		while (!(sline = rd.readLine()).equals("<!prioritytable>")) {
			if (sline.contains("//"))
				continue;
			temp = sline.split("\t");
			pti = new PriorityTableItem();
			pti.setPriority(Integer.valueOf(temp[0]));
			pti.setComparison(Integer.valueOf(temp[2]));
			temp = temp[1].split(" ");
			for (String t : temp) {
				pti.addItem(t);
			}
			tableItems.add(pti);
		}
		rd.close();
	}

}
