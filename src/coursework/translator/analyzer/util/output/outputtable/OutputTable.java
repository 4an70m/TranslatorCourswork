package coursework.translator.analyzer.util.output.outputtable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OutputTable {

	private ArrayList<OutputTableItem> outputTable;
	private File file;

	public OutputTable() {
		outputTable = new ArrayList<>();
		file = new File("Output/test.outputtable");
	}

	public void addItem(int lineNumber, String substring, int lexemeCode,
			int specializedNumber) {
		OutputTableItem otItem = new OutputTableItem(lineNumber, substring,
				lexemeCode, specializedNumber);
		outputTable.add(otItem);
	}

	public void writeToFile() throws IOException {
		BufferedWriter bWrighter = new BufferedWriter(new FileWriter(file));
		for (OutputTableItem item : outputTable) {
			bWrighter.write(item.toString());
		}
		bWrighter.close();
	}

	public ArrayList<OutputTableItem> getOutputTable() {
		return outputTable;
	}
	
	public OutputTableItem get(int index) {
		if (index < outputTable.size())
			return outputTable.get(index);
		return null;
	}
	
	public int size() {
		return outputTable.size();
	}
}
