package sample.M;

import java.util.LinkedList;

public class Split {

	private LinkedList<String> nameOfColsToInsertIn = new LinkedList<>();
	private LinkedList<String> valuesToInsert = new LinkedList<>();

	public LinkedList<String> getnameOfColsToInsertIn() {
		return this.nameOfColsToInsertIn;
	}

	public LinkedList<String> getvaluesToInsert() {
		return this.valuesToInsert;
	}

	public void findTheElementsToInsert(String query) {
		nameOfColsToInsertIn.clear();
		valuesToInsert.clear();

		boolean flag = true;
		int begin = 0;
		if (!split(query).get(3).equals("VALUES")) {
			// case1
			nameOfColsToInsertIn.add(split(query).get(3));
			begin = 4;
		} else {
			// case2
			valuesToInsert.add(split(query).get(4));
			begin = 5;
			flag = false;
		}
		for (int index = begin; index < split(query).size(); index++) {
			if (!split(query).get(index).equals("VALUES") && flag) {

				nameOfColsToInsertIn.add(split(query).get(index));
			} else if (split(query).get(index).equals("VALUES")) {
				flag = false;
				index++;
				valuesToInsert.add(split(query).get(index));

			} else {
				// put the values
				valuesToInsert.add(split(query).get(index));

			}
		}
	}

	public LinkedList<String> split(String query) {
		LinkedList<String> splittedQuery = new LinkedList<>();
		for (int k = 0; k < query.length(); k++) {
			if (query.charAt(k) == '(' || query.charAt(k) == ',' || query.charAt(k) == ')' || query.charAt(k) == ';') {
				query = query.substring(0, k) + " " + query.substring(k + 1, query.length());
			} else if (query.charAt(k) == '=' || query.charAt(k) == '<' || query.charAt(k) == '>') {
				query = query.substring(0, k) + " " + query.charAt(k) + " " + query.substring(k + 1, query.length());
				k = k + 2;
			}
		}
		query = query.toUpperCase();
		String[] tmpSplit = query.split("\\s+");
		for (int i = 0; i < tmpSplit.length; i++) {
			if (tmpSplit[i].charAt(0) == '\'') {
				if (tmpSplit[i].charAt(tmpSplit[i].length() - 1) == '\'') {
					splittedQuery.add(tmpSplit[i]);
				} else {
					String tmp = "";
					tmp += tmpSplit[i] + " ";
					for (int j = i + 1; j < tmpSplit.length; j++) {
						if (tmpSplit[j].charAt(tmpSplit[j].length() - 1) == '\'') {
							tmp += tmpSplit[j];
							splittedQuery.add(tmp);
							i = j;
							break;
						} else {
							tmp += tmpSplit[j];
						}
					}
				}
			} else {
				splittedQuery.add(tmpSplit[i]);

			}
		}

		return splittedQuery;
	}

}

