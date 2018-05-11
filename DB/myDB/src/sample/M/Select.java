package sample.M;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Select {

	private Object[][] selectedList = new Object[0][0];
	private Split mySplit = new Split();

	public Object[][] getSelectedList() {
		return this.selectedList;
	}

	public void executeQuery(String query, String tableName, String dataBaseName, String[] columnsId)
			throws SQLException {

		boolean flag = true;
		/*
		 * SELECT column1, column2, ... FROM table_name;
		 */

		/* SELECT * FROM table_name; */
		File xmlFile = new File(System.getProperty("user.dir") +System.getProperty("file.separator") +"DataBases"+System.getProperty("file.separator")+dataBaseName + System.getProperty("file.separator") + tableName + ".xml");

		// convert xml to dom object tree
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// provide access to xml file
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!mySplit.split(query).get(1).equals("*")) {
			// first case

			// DOM tree structure object,Dom usedd to read xml file
			try {
				InputStream inputStream;
		        Reader reader = null;
		        try {
		            inputStream = new FileInputStream(xmlFile);
		            try {
		                reader = new InputStreamReader(inputStream, "ISO-8859-1");
		            } catch (UnsupportedEncodingException e) {
		                e.printStackTrace();
		            }
		        } catch (FileNotFoundException e1) {
		            e1.printStackTrace();
		        }
				InputSource is = new InputSource(reader);
		        is.setEncoding("ISO-8859-1");
				Document readDocument = docBuilder.parse(is );
				NodeList elements = readDocument.getDocumentElement().getElementsByTagName("tableColumn");
				for (int listIndex = 0; listIndex < columnsId.length; listIndex++) {
					String columnContent = "";
					for (int elementsIndex = 0; elementsIndex < elements.getLength(); elementsIndex++) {
						if (elements.item(elementsIndex).getAttributes().getNamedItem("column_id").getNodeValue()
								.equals(columnsId[listIndex])) {
							columnContent += elements.item(elementsIndex).getTextContent();
							LinkedList<String> splittedContent = mySplit.split(columnContent);
							if (flag) {
								flag = false;
								this.selectedList = new Object[splittedContent.size()][columnsId.length];
							}
							for (int rowIndex = 0; rowIndex < splittedContent.size(); rowIndex++) {
								if (splittedContent.get(rowIndex).equals("''")) {
									this.selectedList[rowIndex][listIndex] = " ";
								}
								this.selectedList[rowIndex][listIndex] = splittedContent.get(rowIndex);
							}

						}
					}
				}
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {// second case
			// DOM tree structure object,Dom used to read xml file
			try {
				InputStream inputStream;
		        Reader reader = null;
		        try {
		            inputStream = new FileInputStream(xmlFile);
		            try {
		                reader = new InputStreamReader(inputStream, "ISO-8859-1");
		            } catch (UnsupportedEncodingException e) {
		                e.printStackTrace();
		            }
		        } catch (FileNotFoundException e1) {
		            e1.printStackTrace();
		        }
				InputSource is = new InputSource(reader);
		        is.setEncoding("ISO-8859-1");
				Document readDocument = docBuilder.parse(is );
				NodeList elements = readDocument.getDocumentElement().getElementsByTagName("tableColumn");
				String allContent = elements.item(0).getTextContent();
				LinkedList<String> splitedAll = mySplit.split(allContent);
				int size = splitedAll.size();
				this.selectedList = new String[size][elements.getLength()];
				for (int columnIndex = 0; columnIndex < elements.getLength(); columnIndex++) {
					allContent = "";
					allContent = elements.item(columnIndex).getTextContent();
					splitedAll = mySplit.split(allContent);

					for (int rowIndex = 0; rowIndex < size; rowIndex++) {
						if (splitedAll.get(rowIndex).equals("''")) {
							this.selectedList[rowIndex][columnIndex] = " ";
						}

						this.selectedList[rowIndex][columnIndex] = splitedAll.get(rowIndex);
					}
				}

			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// return this.selectedList;
	}

	public void comparisionSelect(String query, String tableName, LinkedList<String> condition, String dataBaseName) {

		// Object[][] selectedItems = new Object[0][0];

		// select delete update
		// <
		// >

		// awl 7aga select b *

		// condition after where akid hna fl comparisoin sh8al 3la int

		// h7ot l conditions l b3d where f array of strings
		// Object[] condition = { "personID", ">", "18", "personID", "<", "31"
		// };
		boolean greaterThan = false, smallerThan = false;
		// h2ra l file
		File xmlFile = new File(System.getProperty("user.dir") +System.getProperty("file.separator") +"DataBases"+System.getProperty("file.separator")+dataBaseName + System.getProperty("file.separator")+ tableName + ".xml");

		// convert xml to dom object tree
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// provide access to xml file
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			InputStream inputStream;
	        Reader reader = null;
	        try {
	            inputStream = new FileInputStream(xmlFile);
	            try {
	                reader = new InputStreamReader(inputStream, "ISO-8859-1");
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	        } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	        }
			InputSource is = new InputSource(reader);
	        is.setEncoding("ISO-8859-1");
			Document readDocument = docBuilder.parse(is );
			NodeList elements = readDocument.getDocumentElement().getElementsByTagName("tableColumn");

			LinkedList<Integer> indexOfTheCorrectValue = new LinkedList<>();
			// kol index fl linked list dii by3br 3n row h5do l2eno 722 l
			// condition

			for (int conditionIndex = 0; conditionIndex * 3 < condition.size(); conditionIndex++) {
				for (int elementsIndex = 0; elementsIndex < elements.getLength(); elementsIndex++) {
					if (elements.item(elementsIndex).getAttributes().getNamedItem("column_id").getNodeValue()
							.equals(condition.get(elementsIndex * 3))) {// search
																		// for
																		// the
																		// id in
																		// the
																		// file
						String content = "";
						content += elements.item(elementsIndex).getTextContent();
						LinkedList<String> splittedContent = mySplit.split(content);
						// loop on the splitted content to find the indexes of
						// the values that match the condition
						for (int index = 0; index < splittedContent.size(); index++) {
							if (condition.get((conditionIndex * 3) + 1).equals(">")) {
								greaterThan = true;
								smallerThan = false;
							} else if (condition.get((conditionIndex * 3) + 1).equals("<")) {
								smallerThan = true;
								greaterThan = false;
							}
							if (greaterThan && !splittedContent.get(index).equals("''")) {
								if (Integer.valueOf(splittedContent.get(index)) > Integer
										.valueOf((String) condition.get((conditionIndex * 3) + 2))) {
									indexOfTheCorrectValue.add(index);
								}
							} else if (smallerThan && !splittedContent.get(index).equals("''")) {
								if (Integer.valueOf(splittedContent.get(index)) < Integer
										.valueOf((String) condition.get((conditionIndex * 3) + 2))) {
									indexOfTheCorrectValue.add(index);
								}
							}
						}
					}
				}
			}
			// hna l linked list bta3t l index kmla
			// a5d b2a l elements l tb3 l index dh ha5d l row kolo
			// h3ml select mn kol l columns ll index dh
			LinkedList<String> selectedValues = mySplit.split(elements.item(0).getTextContent());
			selectedList = new Object[indexOfTheCorrectValue.size()][elements.getLength()];
			for (int index = 0; index < elements.getLength(); index++) {// ha5d
																		// column
																		// column
																		// bl
																		// dor
				selectedValues = mySplit.split(elements.item(index).getTextContent());
				for (int eleIndex = 0; eleIndex < indexOfTheCorrectValue.size(); eleIndex++) {// h3ml
																								// select
																								// mn
																								// l
																								// colums
																								// di
																								// l
																								// indexes
																								// mo3yna
					selectedList[eleIndex][index] = selectedValues.get(indexOfTheCorrectValue.get(eleIndex));
				}
			}

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return selectedList;
	}

}

