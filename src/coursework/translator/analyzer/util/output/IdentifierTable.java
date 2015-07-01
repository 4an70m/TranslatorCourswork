package coursework.translator.analyzer.util.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IdentifierTable {

	private ArrayList<TableItem> identifierTable;
	private int intId;
	private File file;

	public IdentifierTable() {
		identifierTable = new ArrayList<>();
		file = new File("Output/test.idtable");
		intId = 0;
	}

	public int addTableItem(String identifier) {
		int tempnum = -1;

		for (int i = 0; i < identifierTable.size(); i++) {
			if (identifierTable.get(i).getItem().equals(identifier)) {
				tempnum = i + 1;
			}
		}

		if (tempnum == -1) {
			tempnum = ++intId;
			TableItem tableItem = new TableItem(identifier, tempnum);
			identifierTable.add(tableItem);
		}

		return tempnum;
	}

	public boolean checkTableItem(String identifier) {
		int tempnum = -1;

		for (int i = 0; i < identifierTable.size(); i++) {
			if (identifierTable.get(i).getItem().equals(identifier)) {
				tempnum = i + 1;
			}
		}

		if (tempnum == -1) {
			return false;
		}

		return true;
	}

	public void writeToFile() throws IOException {
		BufferedWriter bWrighter = new BufferedWriter(new FileWriter(file));
		for (TableItem item : identifierTable) {
			bWrighter.write(item.toString());
		}
		bWrighter.close();
	}

	public ArrayList<TableItem> getIdentifierTable() {
		return identifierTable;
	}

	public void setIdentifierTable(ArrayList<TableItem> identifierTable) {
		this.identifierTable = identifierTable;
	}

	public String getItemByIndex(int index) {
		for (TableItem t : identifierTable) {
			if (t.getCode() == index)
				return t.getItem();
		}
		return null;
	}

	public boolean isIdentifier(String name) {
		for (TableItem t : identifierTable) {
			if (t.getItem().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
