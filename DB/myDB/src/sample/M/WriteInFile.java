package sample.M;

import org.w3c.dom.Document;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/*
 * INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1,
 * value2, value3, ...);
 */
// OR
/*
 * INSERT INTO table_name VALUES (value1, value2, value3, ...);
 */

public class WriteInFile {

	private Split mySplit = new Split();
	private Set<Integer> noOfRows = new HashSet<>();


	public void write(Document readDocument, File xmlFile) {

		DOMSource domSource = new DOMSource(readDocument);

		// StreamResult(the file to save in)
		// StreamResult it writes in the file
		Result result = new StreamResult(xmlFile);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			try {
				transformer.transform(domSource, result);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}

		}



}

