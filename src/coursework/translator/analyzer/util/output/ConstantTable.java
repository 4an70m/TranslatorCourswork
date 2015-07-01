package coursework.translator.analyzer.util.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConstantTable {

	private ArrayList<TableItem> constantTable;
	private int number;
	private File file;

	public ConstantTable() {
		constantTable = new ArrayList<>();
		number = 0;
		file = new File("Output/test.consttable");
		
	}

	public int addTableItem(String constant) {

		int tempnum = -1;

		for (int i = 0; i < constantTable.size(); i++) {
			if (constantTable.get(i).getItem().equals(constant)) {
				tempnum = i + 1;
			}
		}

		if (tempnum == -1) {
			tempnum = ++number;
			TableItem tableItem = new TableItem(constant, tempnum);
			constantTable.add(tableItem);
		}

		return tempnum;
	}
	
	public void writeToFile() throws IOException {
		BufferedWriter bWrighter = new BufferedWriter(new FileWriter(file));
		for (TableItem item : constantTable) {
			bWrighter.write(item.toString());
		}
		bWrighter.close();
	}
	
	public String getItemByIndex(int index)
	{
		for(TableItem t : constantTable)
		{
			if(t.getCode() == index)
				return t.getItem();
		}
		return null; 
	}

    public ArrayList<TableItem> getConstantTable() {
        return constantTable;
    }
        
        
}
