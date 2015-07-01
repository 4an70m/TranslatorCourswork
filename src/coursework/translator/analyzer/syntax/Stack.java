package coursework.translator.analyzer.syntax;

import java.util.LinkedList;

public class Stack<T> extends LinkedList<T> {

	public static final long serialVersionUID = 1;

	public void pushBack(T obj) {
		this.pushBack(obj);
	}

	public T popBack(T obj) {
		return this.pollLast();
	}

	public boolean isNull() {
		return this.isEmpty();
	}

	public int amountOfOccurance(T obj) {
		int amount = 0;
		for (int i = 0; i < this.size(); i++) {
			if (get(i).equals(obj))
				amount++;
		}
		return amount;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (int i = this.size() - 1; i >= 0; i--) {
			sb.append(this.get(i)).append(" ");
		}
		return sb.toString();
	}
}
