package com.search.ir;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

public class main {

	
	public static void main(String[] args) throws SQLException, CorruptIndexException, IOException, ParseException {
	
		/*ArrayList<String> docsId = new SteamerSearch("computer science").search();
		System.out.println(docsId.toString());*/
		
		//new Bigarm().cuttingQuery("computer system department");
		//new Soundex().saveSoundex();
		new Soundex().cuttingQuery("system analysis");
	}

}
