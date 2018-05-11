package sample.M;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Delete {
	/*
	 * DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';
	 */
	private Split mySplit = new Split();
	private Set<Integer> noOfRows = new HashSet<>();

	public int delete(String query, String tableName, String dataBaseName, LinkedList<String> condition) {

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
			// Set<Integer> noOfRows = new HashSet<>();
			int rowIndex = -1;
			for (int listIndex = 0; listIndex * 3 < condition.size(); listIndex++) {
				for (int elementsIndex = 0; elementsIndex < elements.getLength(); elementsIndex++) {
					if (elements.item(elementsIndex).getAttributes().getNamedItem("column_id").getNodeValue()
							.equals(condition.get(listIndex * 3))) {

						// readDocument to find the index of the
						// required
						// element.
						String content = elements.item(elementsIndex).getTextContent();
						LinkedList<String> splittedContent = mySplit.split(content);

						for (int contentIndex = 0; contentIndex < splittedContent.size(); contentIndex++) {
							if (splittedContent.get(contentIndex).equals(condition.get(listIndex + 2))) {
								rowIndex = contentIndex;// number of row
														// to
														// be
														// deleted
							}
						}
						for (int fileIndex = 0; fileIndex < elements.getLength(); fileIndex++) {
							String deletedContent = elements.item(fileIndex).getTextContent();
							LinkedList<String> splitteddeletedContent = mySplit.split(deletedContent);
							// put the array in alinked list to remove
							LinkedList<String> deletedItems = new LinkedList<>();
							for (int index = 0; index < splitteddeletedContent.size(); index++) {
								deletedItems.add(splitteddeletedContent.get(index));
							}
							deletedItems.remove(rowIndex);
							// l ms7to hd5l mkano fady
							deletedItems.add(rowIndex, "''");
							// put the items in the linked list again to
							// a
							// string to push the string in the file
							deletedContent = "";
							for (int index = 0; index < splitteddeletedContent.size(); index++) {
								deletedContent += deletedItems.get(index);
								deletedContent += " ";
							}
							// push the string into the file
							elements.item(fileIndex).setTextContent(deletedContent);
							// write from temp memory to a file
							DOMSource domSource = new DOMSource(readDocument);

							// StreamResult(the file to save in)
							Result result = new StreamResult(xmlFile);
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							try {
								noOfRows.add(elementsIndex);
								Transformer transformer = transformerFactory.newTransformer();
								transformer.setOutputProperty(OutputKeys.INDENT, "yes");
								transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

								try {
									transformer.transform(domSource, result);
								} catch (TransformerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} catch (TransformerConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
				}
			}
			// updateCounter = noOfRows.size();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noOfRows.size();
	}

	public int comparsionDelete(String tableName, LinkedList<String> condition, String dataBaseName) {
		// condition after where akid hna fl comparisoin sh8al 3la int
		// h7ot l conditions l b3d where f array of strings
		// Object[] condition = { "personID", ">", "18", "personID", "<", "31"
		// };
		boolean greaterThan = false, smallerThan = false;
		// h2ra l file
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
		Set<Integer> noOfRows = new HashSet<>();

		NodeList elements = null;
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
			elements = readDocument.getDocumentElement().getElementsByTagName("tableColumn");
			LinkedList<Integer> indexOfTheCorrectValue = new LinkedList<>();
			// kol index fl linked list dii by3br 3n row h5do l2eno 722 l
			// condition

			for (int conditionIndex = 0; conditionIndex * 3 < condition.size(); conditionIndex++) {
				for (int elementsIndex = 0; elementsIndex < elements.getLength(); elementsIndex++) {
					if (elements.item(elementsIndex).getAttributes().getNamedItem("column_id").getNodeValue()
							.equals(condition.get(conditionIndex * 3))) {// search
																			// for
																			// the
																			// id
																			// in
																			// the
																			// file
						String content = "";
						noOfRows.add(elementsIndex);
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
			// h3ml delete mn kol l columns ll index dh

			for (int eleIndex = 0; eleIndex < elements.getLength(); eleIndex++) {
				LinkedList<String> content = mySplit.split(elements.item(eleIndex).getTextContent());
				for (int index = 0; index < indexOfTheCorrectValue.size(); index++) {
					content.add(indexOfTheCorrectValue.get(index), "''");
				}
				String newElements = "";
				for (int index = 0; index < content.size(); index++) {
					newElements += content.get(index);
					newElements += " ";
				}
				// write to the file
				elements.item(eleIndex).setTextContent(newElements);
				// write from temp memory to a file
				DOMSource domSource = new DOMSource(readDocument);

				// StreamResult(the file to save in)
				Result result = new StreamResult(xmlFile);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				try {
					Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

					try {
						transformer.transform(domSource, result);
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return noOfRows.size();

	}

}

