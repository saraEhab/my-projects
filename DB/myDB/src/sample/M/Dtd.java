package sample.M;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Dtd {

	public void createDtdFile(String tableName, String dataBaseName) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")
					+ System.getProperty("file.separator") + dataBaseName+ System.getProperty("file.separator") + tableName + ".dtd")));

			writer.write("<?xml encoding=" + "\"ISO-8859-1\"" + "?>");
			((BufferedWriter) writer).newLine();
			// write the root element and the tags in it, i use the + because
			// i always have the same tag elements name
			writer.write("<!ELEMENT table (tableColumn)+>");
			((BufferedWriter) writer).newLine();
			// #PCDATA means read the text inside the start tag'tableColumn' and
			// the end tag
			writer.write("<!ATTLIST table");
			((BufferedWriter) writer).newLine();
			// the first attribute in the tableColumn tag called column_id
			// i used ID because it always has this fixed name ->column_id
			// #REQUIRED means that you should read the value that i wrote to
			// this attribute and don't put it by a default value by yourself
			writer.write("xmlns CDATA #FIXED ''>");
			((BufferedWriter) writer).newLine();
			// the second attribute in the tableColumn tag called type
			writer.write("<!ELEMENT tableColumn (#PCDATA)>");
			((BufferedWriter) writer).newLine();
			writer.write("<!ATTLIST tableColumn");
			((BufferedWriter) writer).newLine();
			writer.write(" xmlns CDATA #FIXED ''");
			((BufferedWriter) writer).newLine();
			writer.write("column_id NMTOKEN #REQUIRED");
			((BufferedWriter) writer).newLine();
			writer.write("type NMTOKEN #REQUIRED>");

		} catch (IOException ex) {
			// report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}
	}
	
	public void deleteDtdFile(String tableName,String dataBaseName) {
		File file = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + dataBaseName + System.getProperty("file.separator") + tableName + ".dtd");
		file.delete();
	}

}
