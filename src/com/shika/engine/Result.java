package com.shika.engine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryParser.ParseException;

import com.search.ir.Bigarm;
import com.search.ir.Soundex;
import com.search.ir.SteamerSearch;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/**
 * Servlet implementation class Result
 */
@WebServlet("/Result")
public class Result extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	/*
	 * protected void service(HttpServletRequest request, HttpServletResponse
	 * response) throws ServletException, IOException { // TODO Auto-generated
	 * method stub
	 * 
	 * 
	 * /* request.getRequestDispatcher("WEB-INF/views//Result.jsp").forward(
	 * request, response); final File INDEX_DIRECTORY = new
	 * File("E:\\IndexDirectory");
	 * 
	 * ArrayList<String> arr = new ArrayList<>();
	 * 
	 * StandardAnalyzer analyzer = new StandardAnalyzer(); Searcher searcher =
	 * new IndexSearcher(IndexReader.open(INDEX_DIRECTORY));
	 * org.apache.lucene.search.Query query; try { query = new
	 * QueryParser("content",analyzer).parse((String)
	 * request.getAttribute("search"));
	 * 
	 * Hits hits = searcher.search(query); for (int i = 0; i < hits.length();
	 * i++){ String id = hits.doc(i).get("url"); arr.add(id);
	 * System.out.println(id); } System.out.println(hits.doc(0).toString());
	 * request.setAttribute("list", arr);
	 * request.getRequestDispatcher("WEB-INF/views//Result.jsp").forward(
	 * request, response);
	 * 
	 * } catch (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// PreparedStatement pstmt = conn.prepareStatement(sql);
	
		String bigram = request.getParameter("bigram");
		if (bigram!=null) {
			request.setAttribute("mean", new Bigarm().cuttingQuery(request.getParameter("search")));
		}
		String soundex = request.getParameter("soundex");
		if (soundex!=null) {
			try {
				request.setAttribute("list", new Soundex().cuttingQuery(request.getParameter("search")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(soundex==null&&bigram==null){
		request.setAttribute("search", request.getParameter("search"));
		try {
			request.setAttribute("list", new SteamerSearch(request.getParameter("search")).search());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		request.getRequestDispatcher("Result.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
