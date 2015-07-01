package coursework.translator.rpn.builder.util;

import java.util.ArrayList;

public class StackWriterItem {

	private int itemId;
	private String stackRepresentation;
	private String rpnRepresentation;
	private ArrayList<Integer> functionGroup;

	public StackWriterItem() {
		functionGroup = new ArrayList<>();
	}

	public StackWriterItem(int itemId, String stackRepresentation,
			String rpnRepresentation, ArrayList<Integer> functionGroup) {
		super();
		this.itemId = itemId;
		this.stackRepresentation = stackRepresentation;
		this.rpnRepresentation = rpnRepresentation;
		this.functionGroup = functionGroup;
	}

	public int getItemId() {
		return itemId;
	}

	public String getStackRepresentation() {
		return stackRepresentation;
	}

	public String getRpnRepresentation() {
		return rpnRepresentation;
	}
	
	public String getRpnRepresentation(int m) {
		String t = rpnRepresentation;
		t = t.replace("%", "m" + String.valueOf(m + 1));
		t = t.replace("$", "m" + String.valueOf(m));
		return t;
	}

	public ArrayList<Integer> getFunctionGroup() {
		return functionGroup;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void setStackRepresentation(String stackRepresentation) {
		this.stackRepresentation = stackRepresentation;
	}

	public void setRpnRepresentation(String rpnRepresentation) {
		this.rpnRepresentation = rpnRepresentation;
	}

	public void setFunctionGroup(ArrayList<Integer> functionGroup) {
		this.functionGroup = functionGroup;
	}

	public boolean hasStackRepresentation() {
		return !stackRepresentation.equals("_");
	}

	public boolean hasRPNRepresentation() {
		return !rpnRepresentation.equals("_");
	}

	public boolean isInFunctionGroup(int functionGroup) {
		return this.functionGroup.contains(functionGroup);
	}

	public void addFunctionGroup(int functionGroup) {
		this.functionGroup.add(functionGroup);
	}

	@Override
	public String toString() {
		return "StackWriterItem [itemId=" + itemId + ", stackRepresentation="
				+ stackRepresentation + ", rpnRepresentation="
				+ rpnRepresentation + ", functionGroup=" + functionGroup + "]";
	}

}
