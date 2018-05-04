package eg.edu.alexu.csd.oop.db.cs37_54;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import eg.edu.alexu.csd.oop.db.cs37_54.queries.CreateQuery;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.DeleteQuery;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.DropQuery;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.InsertQuery;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.Query;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.SelectQuery;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.UpdateQuery;

/**
 * semicolon union --deleted both are multiple statements setmain query which
 * sets first order and main key words and separate values
 *
 * @author ABDO
 */
public class Validator implements ValidatorI {

	/* private default constructor */
	public Validator() throws ParserConfigurationException {
		this.query = new StringBuilder();
		// Objcreate = new Query(Contact.createdbKeys.CREATE);
		// this.Objcreate = new CreateQuery();
		// this.ObjSelect = new SelectQuery();

		// Query Obj;

	}

	Query Obj;
	CreateQuery Objcreate = new CreateQuery();
	DeleteQuery ObjDelet = new DeleteQuery();
	DropQuery ObjDrop = new DropQuery();
	InsertQuery Objinsert = new InsertQuery();
	SelectQuery ObjSelect = new SelectQuery();
	UpdateQuery ObjUpdate = new UpdateQuery();

	private StringBuilder query;
	private String[] words;

	@Override
	public boolean setQuery(String fullQuery) throws SQLException {
		fullQuery = fullQuery.toUpperCase();
		fullQuery = fullQuery.trim();
		this.query = new StringBuilder();
		this.query.append(fullQuery);
		//System.out.println(this.query.toString());

		boolean semi = false;
		//System.out.println(fullQuery);
		try {
			semi = this.moreThanSemicolon();
		} catch (Exception e1) { // if not ended with semicolon
			e1.printStackTrace();
			try {
				throw new SQLSyntaxErrorException();
			} catch (SQLSyntaxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (semi) { // check if more than one semicolon //TODO what do you want
			// me to do in such case
			// this.query.deleteCharAt(this.query.length()-1);
			// String []queries =this.query.toString().split(";");
			// //System.out.println(queries.length);
			// for (int i =0 ; i <queries.length;i++){
			// Validator temp = new Validator();
			// if(!temp.setQuery(queries[i].trim()+";")){
			// return false;
			// }
			// }
			try {
				throw new SQLSyntaxErrorException();
			} catch (SQLSyntaxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else { // single statement
			words = this.query.toString().split("\\s+");

			try {
				this.setOrder(words[0].trim());

			} catch (Exception e) {
				throw new SQLSyntaxErrorException();

			}


		}
		return true;
	}

	private boolean moreThanSemicolon() throws Exception {
		String queryWithoutLastSemicolon = null;

		if (this.query.charAt(this.query.length() - 1) != ';') {
			throw new SQLSyntaxErrorException();			// semicolon
		} else {
			queryWithoutLastSemicolon = this.query.substring(0, this.query.length() - 2); // last
			//System.out.println(queryWithoutLastSemicolon);
			// one
			// does
			// exists
		}
		if (queryWithoutLastSemicolon.indexOf(";") >= 0) {
			//System.out.println(queryWithoutLastSemicolon);
			//System.out.println(queryWithoutLastSemicolon.indexOf(";"));

			// more than one
			// semicolon
			return true;
		} else {
			return false; // one semicolon
		}
	}

	private void setOrder(String order) throws Exception {
		if (order.compareToIgnoreCase(Contact.createKeys.CREATE) == 0) {
			this.create();
		} else if (order.compareToIgnoreCase(Contact.insertKeys.INSERT) == 0) {

			this.insert();
		} else if (order.compareToIgnoreCase(Contact.deleteKeys.DELETE) == 0) {
			this.delete();
		} else if (order.compareToIgnoreCase(Contact.selectKeys.SELECT) == 0) {
			this.select();
		} else if (order.compareToIgnoreCase(Contact.updatetKeys.UPDATE) == 0) {
			this.update();
		} else if (order.compareToIgnoreCase(Contact.DropKeys.Drop) == 0) {
			this.drop();
		} else {
			// this.Obj = new QueryInfo();
			throw new SQLSyntaxErrorException();
		}


	}

	public Query getItExploded() {

		return Obj;
	}

	private void removeSemicolon() {
		String columnsStr = this.query.toString();
		columnsStr = columnsStr.replace(';', ' '); // remove the semicolomn
		columnsStr = columnsStr.trim(); // trim
		this.words = columnsStr.split("\\s+");
	}

	private void create() throws Exception {

		/* create table */
		if (words.length >= 3 && words[1].compareToIgnoreCase(Contact.createKeys.TABLE) == 0) {


			this.Objcreate = new CreateQuery();
			if (words[2].contains("(")){
				String []arr=words[2].split("\\(");
				words[2]=arr[0];
				words[2]=words[2].trim();
			}
			if(words[2].length()==0){
				throw new SQLSyntaxErrorException();            }
			//  System.out.println(words[2].toLowerCase());
			//String tableNameInLowerCase = words[2].toLowerCase().trim();
			this.Objcreate.setCreationTableName(words[2]); // table name
			/* whatever after table name */
			String columnsStr = this.query.substring(
					query.indexOf(Objcreate.getCreationTableName()) + Objcreate.getCreationTableName().length());
			columnsStr = columnsStr.replace(';', ' '); // remove semicolmn
			columnsStr = columnsStr.trim(); // trim
			char left = columnsStr.charAt(0);
			char right = columnsStr.charAt(columnsStr.length() - 1);
			/* must have (' and ')' at start and end */
			if (left != '(' || right != ')') {
				throw new SQLSyntaxErrorException();
			} else { // remove ( and )
				columnsStr = columnsStr.substring(1, columnsStr.length() - 1);
			}
			/* split to get columns and their type */
			String[] columns = columnsStr.split(",");
			for (int i = 0; i < columns.length; i++) {
				columns[i] = columns[i].trim();
				String[] arrNameType = columns[i].split("\\s+");
				if (arrNameType.length < 2) {
					throw new SQLSyntaxErrorException();				}
				ColumnsAndValues nameanddatatype = new ColumnsAndValues(
						columns[i].substring(0, arrNameType[0].length()).toLowerCase(),
						columns[i].substring(arrNameType[0].length()).toLowerCase());
				Objcreate.getCreateMap().add(nameanddatatype);
			}

			Objcreate.setCreationTableName(this.Objcreate.getCreationTableName().toLowerCase());

			/* create database */
		} else if (words.length >= 2 && words[1].compareToIgnoreCase(Contact.createdbKeys.DATABASE) == 0) {
			this.removeSemicolon(); // remove the ; from the words and trim
			if (words.length == 3) { // must be 3 words create database name
				this.Objcreate.setsecType(Contact.createdbKeys.DATABASE);
				this.Objcreate.setCreationDBName(words[2].toLowerCase());
			} else { // otherwise throw exception
				throw new SQLSyntaxErrorException();			}
			/* error */
		} else {
			throw new SQLSyntaxErrorException();		}

		this.Obj = Objcreate;
		this.testcases+="\n"+this.query;


	}
	public static String testcases = "";
	private void drop() {
		this.ObjDrop = new DropQuery();
		this.removeSemicolon(); // remove semicolon and trim
		if (this.words.length == 3) {
			if (words[1].compareToIgnoreCase(Contact.createdbKeys.DATABASE) == 0) {
				this.ObjDrop.setsecType(Contact.createdbKeys.DATABASE);
			} else if (words[1].compareToIgnoreCase(Contact.createKeys.TABLE) == 0) {
				this.ObjDrop.setsecType(Contact.createKeys.TABLE);
			} else {
				try {
					throw new SQLSyntaxErrorException();
				} catch (SQLSyntaxErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			}
		} else {
			try {
				throw new SQLSyntaxErrorException();
			} catch (SQLSyntaxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		}
		this.ObjDrop.setDroppedName(words[2].toLowerCase()); // dropped table or database name
		this.Obj = ObjDrop;
	}

	private void delete() throws Exception {
		this.ObjDelet = new DeleteQuery();
		this.removeSemicolon();
		if (words.length >= 3 && words[1].compareToIgnoreCase(Contact.deleteKeys.FROM) == 0) {

			this.ObjDelet.setsecType(Contact.deleteKeys.FROM);
			if (words.length == 3) {
				this.ObjDelet.setDelAll(true);
				this.ObjDelet.setDelFrom(words[2].toLowerCase());
			} else if (words[3].compareToIgnoreCase(Contact.whereKeys.WHERE) == 0) {
				this.ObjDelet.setDelAll(false);
				this.ObjDelet.setDelFrom(words[2].toLowerCase());
				this.setWhereinobj(words[3], Contact.deleteKeys.DELETE);
			} else {
				throw new SQLSyntaxErrorException();			}
		} else if (words.length == 4 && words[1].compareToIgnoreCase("*") == 0
				&& words[2].compareToIgnoreCase(Contact.deleteKeys.FROM) == 0) {
			this.ObjDelet.setDelAll(true);
			this.ObjDelet.setsecType(Contact.deleteKeys.FROM);
			this.ObjDelet.setDelFrom(words[3].toLowerCase());

		} else {
			throw new SQLSyntaxErrorException();		}
		this.Obj = ObjDelet;
	}

	private void setWhereinobj(String theWordwhere, String kindofQuery) {
		int afterwhereIndex = this.query.indexOf(theWordwhere) + theWordwhere.length();
		String where="";
		if(afterwhereIndex>0){
			where = this.query.substring(afterwhereIndex, this.query.length() - 1).trim();
		}
		if (!(where.contains(Contact.whereKeys.equal) || where.contains(Contact.whereKeys.greater)
				|| where.contains(Contact.whereKeys.less))) {
			try {
				throw new SQLSyntaxErrorException();
			} catch (SQLSyntaxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		}
		if (kindofQuery.compareToIgnoreCase(Contact.deleteKeys.DELETE) == 0) {
			this.ObjDelet.setWhere(where);
		}else if(kindofQuery.compareToIgnoreCase(Contact.selectKeys.SELECT) == 0){
			this.ObjSelect.setWhere(where);
		}
		else {
			this.ObjUpdate.setWhere(where);

		}
	}

	public void insert() throws Exception {
		//System.out.println(this.query);
		this.removeSemicolon();
		// System.out.println(words[0] +" "+ words[1] +" "+ words[2] +"
		// "+words.length + " "+" "+ words[3]+" "+" "+ words[4]+ " "+words[5] );
		if (words[0].trim().compareToIgnoreCase(Contact.insertKeys.INSERT) == 0
				&& words[1].trim().compareToIgnoreCase(Contact.insertKeys.INTO) == 0) {
			this.Objinsert = new InsertQuery();
			if(words[2].contains("(")){
				String patition[]=words[2].split("\\(");
				this.Objinsert.setInsertTableName(patition[0].trim());
				//System.out.println(this.query.indexOf(this.Objinsert.getInsertTableName()));
//				System.out.println(this.Objinsert.getInsertTableName()
//						+this.query+
//						this.query.indexOf(this.Objinsert.getInsertTableName())
//						+"  "+this.Objinsert.getInsertTableName().length()
//						+"  "+this.query.indexOf(Contact.insertKeys.VALUES));


			}else{
				this.Objinsert.setInsertTableName(words[2].trim());}

			if (words[3].compareToIgnoreCase("VALUES") == 0) {

				// words[4].trim();
				if(query.indexOf(words[3])<0){
					throw new SQLSyntaxErrorException();				}

				String values = this.query.substring(query.indexOf(words[3]) + words[3].length());
				values = values.replace(';', ' ');
				values = values.trim();
				char left = values.charAt(0);
				char right = values.charAt(values.length() - 1);
				if (left != '(' || right != ')') {
					throw new SQLSyntaxErrorException();
				}
				try{
					values=values.substring(1, values.length() - 1);
				}catch(Exception e){
					throw new SQLSyntaxErrorException();}
				String[] elements = values.split(",");
				for (int h = 0; h < elements.length; h++) {
					elements[h]= elements[h].trim();
					ColumnsAndValues nameanddatatype = new ColumnsAndValues(null, elements[h]);
					Objinsert.getInsertionMap().add(nameanddatatype);
				}

			}
			else if (!words[2].contains(this.Objinsert.getInsertTableName())) {
				try {
					String therest = this.query.substring(query.indexOf(words[2]) + words[2].length());
					String all[] = therest.split("VALUES");
					String columns = all[0];
					String elements = all[1];
					elements = elements.replace(';', ' ');
					columns = columns.trim();
					elements = elements.trim();
					// System.out.println(therest + " "+ columns + " "+
					// elements);
					if (columns.charAt(0) != '(' || columns.charAt(columns.length() - 1) != ')'
							|| elements.charAt(0) != '(' || elements.charAt(elements.length() - 1) != ')') {

						throw new SQLSyntaxErrorException();
					}
					columns=columns.substring(1, columns.length() - 1);
					elements =elements.substring(1, elements.length() - 1);

					String nameOfColumns[] = columns.split(",");
					String nameOfelements[] = elements.split(",");
					if (nameOfColumns.length != nameOfelements.length) {
						throw new SQLSyntaxErrorException();
					}
					for (int h = 0; h < nameOfColumns.length; h++) {
						String ncolumns = nameOfColumns[h];
						String nelements = nameOfelements[h];
						ncolumns =ncolumns.trim();
						nelements=	nelements.trim();
						ColumnsAndValues nameanddatatype = new ColumnsAndValues(ncolumns, nelements);
						this.Objinsert.getInsertionMap().add(nameanddatatype);
					}

				} catch (Exception e) {
					throw new SQLSyntaxErrorException();
				}
			} else if(words[2].contains(this.Objinsert.getInsertTableName())){
				int x=0,y=0,z=0,a=0,b=0;
				String aa =new String();
				String bb =new String();

				try {
					String therest = this.query.substring(this.query.indexOf(this.Objinsert.getInsertTableName())+this.Objinsert.getInsertTableName().length());
					StringBuilder bd = new StringBuilder();
					bd.append(therest);
					x= therest.length();
					aa=therest;

					aa=bd.substring(0, bd.indexOf(")")+1);
					bb=bd.substring(bd.lastIndexOf("("));



					String columns = aa;
					String elements = bb;
					elements = elements.replace(';', ' ');
					columns = columns.trim();
					elements = elements.trim();
					//System.out.println(columns + elements);
					// System.out.println(therest + " "+ columns + " "+
					// elements);
					if (columns.charAt(0) != '(' || columns.charAt(columns.length() - 1) != ')'
							|| elements.charAt(0) != '(' || elements.charAt(elements.length() - 1) != ')') {

						throw new SQLSyntaxErrorException();
					}
					columns=columns.substring(1, columns.length() - 1);
					elements =elements.substring(1, elements.length() - 1);

					String nameOfColumns[] = columns.split(",");
					String nameOfelements[] = elements.split(",");
					if (nameOfColumns.length != nameOfelements.length) {
						throw new SQLSyntaxErrorException();
					}
					for (int h = 0; h < nameOfColumns.length; h++) {
						String ncolumns = nameOfColumns[h];
						String nelements = nameOfelements[h];
						ncolumns =ncolumns.trim();
						nelements=	nelements.trim();
						ColumnsAndValues nameanddatatype = new ColumnsAndValues(ncolumns, nelements);
						this.Objinsert.getInsertionMap().add(nameanddatatype);
					}

				} catch (Exception e) {
					//e.printStackTrace();
					throw new SQLSyntaxErrorException();
				}

			}

		} else {
			throw new SQLSyntaxErrorException();		}
		this.Obj = this.Objinsert;

		this.Objinsert.setInsertTableName(this.Objinsert.getInsertTableName().toLowerCase());

		LinkedList<ColumnsAndValues> temp = new LinkedList<ColumnsAndValues>();
		for(int i=0;i<this.Objinsert.getInsertionMap().size();i++){
			if(this.Objinsert.getInsertionMap().get(i).getColumn() != null){
				String col = this.Objinsert.getInsertionMap().get(i).getColumn().toLowerCase();

				String val = this.Objinsert.getInsertionMap().get(i).getValue().toLowerCase();
				ColumnsAndValues temp1 = new ColumnsAndValues(col,val);

				temp.add(temp1);}
			else{
				String col = null;

				String val = this.Objinsert.getInsertionMap().get(i).getValue().toLowerCase();
				ColumnsAndValues temp1 = new ColumnsAndValues(col,val);

				temp.add(temp1);

			}

		}

		this.Objinsert.setInsertionMap(temp);


	}

	public void update() throws Exception {
		this.removeSemicolon();
		if (words.length >= 4 && words[0].compareToIgnoreCase(Contact.updatetKeys.UPDATE) == 0
				&& words[2].trim().compareToIgnoreCase(Contact.updatetKeys.SET) == 0) {

			this.ObjUpdate = new UpdateQuery();
			this.ObjUpdate.setUpdateTableName(words[1]);
			String therest = this.query.substring(query.indexOf(words[2]) + words[2].length());

			if (therest.contains("WHERE")) {

				String[] all = therest.split("WHERE");
				this.setWhereinobj("WHERE", Contact.updatetKeys.UPDATE);

				String theWholeUpdates[] = all[0].split(",");
				for (int h = 0; h < theWholeUpdates.length; h++) {
					String twoSides[] = theWholeUpdates[h].split("=");
					String columnName = twoSides[0];
					String newValue = twoSides[1];
					newValue = newValue.replace(';', ' ');
					newValue = newValue.trim();
					columnName = columnName.trim();
					newValue = newValue.trim();
					ColumnsAndValues nameanddatatype = new ColumnsAndValues(columnName, newValue);
					this.ObjUpdate.getUpdateMap().add(nameanddatatype);
					// this.ObjUpdate.getUpdateMap().put(columnName, newValue);

				}


			} else {

				String theWholeUpdates[] = therest.split(",");
				for (int h = 0; h < theWholeUpdates.length; h++) {
					String twoSides[] = theWholeUpdates[h].split("=");
					String columnName = twoSides[0];
					String newValue = twoSides[1];
					newValue = newValue.replace(';', ' ');
					newValue = newValue.trim();
					// System.out.println(newValue);
					columnName = columnName.trim();
					newValue = newValue.trim();
					ColumnsAndValues nameanddatatype = new ColumnsAndValues(columnName, newValue);
					this.ObjUpdate.getUpdateMap().add(nameanddatatype);
				}

			}



		} else {

			throw new SQLSyntaxErrorException();		}
		this.Obj = ObjUpdate;

		this.ObjUpdate.setUpdateTableName(this.ObjUpdate.getetUpdateTableName().toLowerCase());
		LinkedList<ColumnsAndValues> temp = new LinkedList<ColumnsAndValues>();
		for(int i=0;i<this.ObjUpdate.getUpdateMap().size();i++){

			String col = this.ObjUpdate.getUpdateMap().get(i).getColumn().toLowerCase();
			String val = this.ObjUpdate.getUpdateMap().get(i).getValue().toLowerCase();
			ColumnsAndValues temp1 = new ColumnsAndValues(col,val);
			temp.add(temp1);

		}

		this.ObjUpdate.setUpdateMap(temp);
		//System.out.println(this.ObjUpdate.getetUpdateTableName());
		for(int j=0;j<this.ObjUpdate.getUpdateMap().size();j++){
			//System.out.println(this.ObjUpdate.getUpdateMap().get(j).getColumn()+" "+this.ObjUpdate.getUpdateMap().get(j).getValue());
		}


	}

	public void select() throws Exception {

		this.removeSemicolon();
		if (words.length >= 4 && words[0].compareToIgnoreCase(Contact.selectKeys.SELECT) == 0) {
			this.ObjSelect = new SelectQuery();
			if (words[1].equals("*")) {
				String insertablename = words[3];
				insertablename = insertablename.trim();
				this.ObjSelect.setSelectionTableName(insertablename.toLowerCase());
				this.ObjSelect.setSelectAll(true);
				if( this.query.toString().contains("WHERE")&&words[4].trim().compareToIgnoreCase(Contact.whereKeys.WHERE)==0){
					setWhereinobj(words[4].trim(), "SELECT");


				}


			} else {

				String therest = this.query.substring(query.indexOf(words[0]) + words[0].length());
				if (therest.contains("FROM")) {



					String theall[] = therest.split("FROM");
					String columns = theall[0];
					String tablename = theall[1];
					if(theall[1].contains("WHERE")){
						String part[]= theall[1].split("WHERE");
						tablename = part[0].trim();
						this.ObjSelect.setSelectionTableName(tablename);
						setWhereinobj("WHERE", "SELECT");
						String columnsNames[] = columns.split(",");

						for (int h = 0; h < columnsNames.length; h++) {
							String temp = columnsNames[h];
							temp = temp.trim();
							if(!words[1].trim().equals(Contact.selectKeys.FROM)){

								this.ObjSelect.getselectionArray().add(temp.toLowerCase());}
						}
					}else{
						tablename = tablename.replace(';', ' ');
						tablename = tablename.trim();
						this.ObjSelect.setSelectionTableName(tablename.toLowerCase());
						String columnsNames[] = columns.split(",");
						for (int h = 0; h < columnsNames.length; h++) {
							String temp = columnsNames[h];
							temp = temp.trim();
							this.ObjSelect.getselectionArray().add(temp.toLowerCase());
						}}


				} else {
					throw new SQLSyntaxErrorException();
				}

			}


		} else {
			throw new SQLSyntaxErrorException();		}

		this.Obj = ObjSelect;
		//System.out.println(this.ObjSelect.getSelectionTableName());
	}

}