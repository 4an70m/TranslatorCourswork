package coursework.translator.rpn.builder.util;

import java.util.ArrayList;

public class PriorityTableItem {

	private int priority;
	private ArrayList<String> items;
	private int comparison;

	public PriorityTableItem() {
		priority = 0;
		items = new ArrayList<>();
		comparison = 0;
	}

	public PriorityTableItem(int priority, ArrayList<String> items,
			int comparison) {
		super();
		this.priority = priority;
		this.items = items;
		this.comparison = comparison;
	}

	public int getPriority() {
		return priority;
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public int getComparison() {
		return comparison;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}

	public void setComparison(int comparison) {
		this.comparison = comparison;
	}
	
	@Override
	public String toString() {
		return "PriorityTableItem [priority=" + priority + ", items=" + items
				+ ", comparison=" + comparison + "]";
	}

	public void addItem(String item) {
		items.add(item);
	}
}
