package coursework.translator.rpn.builder.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StackWriterTable {

	private ArrayList<StackWriterItem> isWriteableToStack;

	public StackWriterTable() {
		isWriteableToStack = new ArrayList<>();
	}

	public StackWriterTable(ArrayList<StackWriterItem> isWriteableToStack) {
		super();
		this.isWriteableToStack = isWriteableToStack;
	}

	public ArrayList<StackWriterItem> getIsWriteableToStack() {
		return isWriteableToStack;
	}

	public void setIsWriteableToStack(
			ArrayList<StackWriterItem> isWriteableToStack) {
		this.isWriteableToStack = isWriteableToStack;
	}

	@Override
	public String toString() {
		return "StackWriterTable [isWriteableToStack=" + isWriteableToStack
				+ "]";
	}

	public void readTable(String path) throws IOException {
		BufferedReader rd = new BufferedReader(new FileReader(path));
		String sline = rd.readLine();
		if (!sline.equals("<itemstostack>")) {
			rd.close();
			return;
		}
		String[] temp;
		StackWriterItem swi;
		while (!(sline = rd.readLine()).equals("<!itemstostack>")) {
			if (sline.contains("//"))
				continue;
			temp = sline.split("~");
			swi = new StackWriterItem();
			swi.setItemId(Integer.valueOf(temp[0].trim()));
			swi.setStackRepresentation(temp[1].trim());
			swi.setRpnRepresentation(temp[2].trim());
			String[] fg = temp[3].trim().split(" ");
			for (int i = 0; i < fg.length; i++) {
				swi.addFunctionGroup(Integer.valueOf(fg[i]));
			}
			isWriteableToStack.add(swi);
		}
		rd.close();
	}

	public StackWriterItem getItemByIdAndFuncGroup(int id, int functionGroup) {
		for (StackWriterItem t : isWriteableToStack) {
			if (t.getItemId() == id && t.isInFunctionGroup(functionGroup)) {
				return t;
			}
		}
		return null;
	}

	public StackWriterItem getItemByStackRep(String representation) {
		for (StackWriterItem t : isWriteableToStack) {
			if (t.getStackRepresentation().equals(representation))
				return t;
		}
		return null;
	}

}
