package com.search.ir;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

public class SteamerSearch {

	String text;
	Stemmer steamer;
	ArrayList<Row> rows;
	ArrayList<String> steems = new ArrayList<>(); 
	boolean exact = false;
	String mTest;
	String textExact;
	boolean oneWord = false;

	public SteamerSearch(String text) throws SQLException {
		this.text = text.toLowerCase();
		mTest = text.toLowerCase();
		steamer = new Stemmer();
		rows = new ArrayList<>();
		findSteam();
	}

	public void findSteam() throws SQLException {
		exact = ifExact(text);
		if (exact) {
			text = text.replaceAll("\"", "");
		}
		System.out.println(text);
		StringTokenizer token = new StringTokenizer(text, " ");
		if (token.countTokens() == 1) {
			oneWord = true;
		}
		String word, steam;
		while (token.hasMoreElements()) {
			word = token.nextToken();
			if (stopword(word)) {	
			steam = steamer.stem(word);
			System.out.println(steam);
			steems.add(steam);
			}
			
		}
		rows = DBConnection.retriveRowFromSteamer(steems);
	}

	public HashMap<Integer, Integer> findDocs() {
		HashMap<Integer, Integer> docIdFrequncy;
		HashMap<String, HashMap<Integer, Integer>> WordDocIdFrequncy = new HashMap<String, HashMap<Integer, Integer>>();

		for (Row element : rows) {
			String freq = element.getFrequency();
			String[] arr = freq.split(",");
			docIdFrequncy = new HashMap<Integer, Integer>();
			for (String item : arr) {
				String[] arr2 = item.split(":");
				docIdFrequncy.put(Integer.parseInt(arr2[0]),
						Integer.parseInt(arr2[1]));
			}
			WordDocIdFrequncy.put(element.getWord(), docIdFrequncy);
		}
		Set<Integer> intersectDocs = WordDocIdFrequncy.get(
				WordDocIdFrequncy.entrySet().iterator().next().getKey())
				.keySet();

		for (String word : WordDocIdFrequncy.keySet()) {
			intersectDocs.retainAll(WordDocIdFrequncy.get(word).keySet());
		}

		docIdFrequncy = new HashMap<>();
		for (Integer docId : intersectDocs) {
			int sum = 0;
			for (String word : WordDocIdFrequncy.keySet()) {
				sum += WordDocIdFrequncy.get(word).get(docId);

			}
			docIdFrequncy.put(docId, sum);
		}

		return sortByValues(docIdFrequncy, 1);

	}
	
	public Set<Integer> SortingByFrequency(Set<Integer> intersectDocs){
		HashMap<String, HashMap<Integer, Integer>> WordDocIdFrequncy = new HashMap<String, HashMap<Integer, Integer>>();
		HashMap<Integer, Integer> docIdFrequncy = new HashMap<>();
		for (Row element : rows) {
			String freq = element.getFrequency();
			String[] arr = freq.split(",");
			docIdFrequncy = new HashMap<Integer, Integer>();
			for (String item : arr) {
				String[] arr2 = item.split(":");
				docIdFrequncy.put(Integer.parseInt(arr2[0]),
						Integer.parseInt(arr2[1]));
			}
			WordDocIdFrequncy.put(element.getWord(), docIdFrequncy);
			docIdFrequncy = new HashMap<>();
			for (Integer docId : intersectDocs) {
				int sum = 0;
				for (String word : WordDocIdFrequncy.keySet()) {
					sum += WordDocIdFrequncy.get(word).get(docId);

				}
				docIdFrequncy.put(docId, sum);
			}

			
		}
		
		return sortByValues(docIdFrequncy, 1).keySet();
	}

	public ArrayList<String> search() throws CorruptIndexException,
			IOException, ParseException {
		ArrayList<String> urls = null;
		if (oneWord) {
			HashMap<Integer, Integer> docAndId = findDocs();
			urls = DBConnection.getUrl(docAndId.keySet());
		} else {
			if (exact) {
				urls = com.test.ahmed.SteamerSearch.exactSearch(mTest);
			} else {
				urls = com.test.ahmed.SteamerSearch.rankingByPosition(text);
			}
		}

		return urls;
	}

	public ArrayList<String> rankingByPosition(String text) {

		HashMap<Integer, Integer> idPosition = new HashMap<>();
		HashMap<String, HashMap<Integer, Set<String>>> wordDocIdPosition = new HashMap<>();
		wordDocIdPosition = findIdsAndPosition(rows);

		Set<Integer> intersectDocs = wordDocIdPosition.get(
				wordDocIdPosition.entrySet().iterator().next().getKey())
				.keySet();

		for (String word : wordDocIdPosition.keySet()) {
			intersectDocs.retainAll(wordDocIdPosition.get(word).keySet());
		}
		ArrayList<Integer> positions;
		for (Integer integer : intersectDocs) {
			positions = new ArrayList<>();
			for (int i = 0; i < wordDocIdPosition.size() - 1; i++) {
				Iterator<String> word = wordDocIdPosition.keySet().iterator();

				if (word.hasNext()) {

					Iterator<String> it = wordDocIdPosition.get(word.next())
							.get(integer).iterator();
					word.remove();
					Iterator<String> it1 = wordDocIdPosition.get(word.next())
							.get(integer).iterator();
					word.remove();
					int sub;
					if (it.hasNext() && it1.hasNext()) {
                         
						sub = Math.abs(Integer.parseInt(it1.next())
								- Integer.parseInt(it.next()));
						positions.add(sub);
						System.out.println(sub);
					} else {
						sub = Integer.parseInt(it.next());
					}
				}
			}
			int sum = 0;
			List<Integer> list = new ArrayList<Integer>();

			for (Integer pos : positions) {
				sum += pos;
			}
			idPosition.put(integer, sum);
		}
		
		return DBConnection.getUrl(sortByValues(idPosition , 2).keySet());

	}

	@SuppressWarnings({ "unchecked", "unchecked", "unchecked", "unchecked" })
	public static HashMap sortByValues(HashMap map, final int flag) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {

			public int compare(Object o1, Object o2) {
				if (flag == 1) {
					return ((Comparable) ((Map.Entry) (o2)).getValue())
							.compareTo(((Map.Entry) (o1)).getValue());
				} else {
					return ((Comparable) ((Map.Entry) (o1)).getValue())
							.compareTo(((Map.Entry) (o2)).getValue());
				}
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	private boolean ifExact(String query) {
		if (query.charAt(0) == '"' && query.charAt(query.length() - 1) == '"')
			return true;
		return false;
	}

	public HashMap<String, HashMap<Integer, Set<String>>> findIdsAndPosition(
			ArrayList<Row> allrows) {

		HashMap<String, HashMap<Integer, Set<String>>> ret = new HashMap<>();
		for (Row row : allrows) {
			String word = row.getWord();
			String idPositions = row.getIdAndPosition();
			idPositions = idPositions.substring(1, idPositions.length() - 1);
			String[] pairs = idPositions.split("]");
			HashMap<Integer, Set<String>> ids = null;
			//System.out.println(word + "\\\\\\" + idPositions);
			ids = new HashMap<Integer, Set<String>>();
			for (String element : pairs) {
				// System.out.println(element);
				String[] entry = element.split("=");
				// if (entry.length >= 2) {
				entry[1] = entry[1].replaceAll(" ", "");
				entry[1] = entry[1].replace("[", "");
				entry[1] = entry[1].replace("]", "");
				// entry[0] = entry[0].substring(2,entry[0].length());
				entry[0] = entry[0].replace(",", "");
				entry[0] = entry[0].replace(" ", "");
				String positions = entry[1];
				// System.out.println(entry[0] + "\\\\\\\\" + positions)
				Set<String> s = new HashSet<String>();
				s.addAll(Arrays.asList(positions.split(",")));
				ids.put(Integer.parseInt(entry[0]), s);
				// }
			}
			ret.put(word, ids);

		}

		return ret;
	}

	public ArrayList<String> exactSearch(String text) {
		HashMap<String, HashMap<Integer, Set<String>>> idAndPosition;
		idAndPosition = findIdsAndPosition(rows);
		Set<Integer> ret = new HashSet<Integer>();
		Set<Integer> intersectDocs = idAndPosition.get(
				idAndPosition.entrySet().iterator().next().getKey()).keySet();

		for (String word : idAndPosition.keySet()) {
			intersectDocs.retainAll(idAndPosition.get(word).keySet());
		}
		for (Integer docId : intersectDocs) {
			int count = 0;
			Set<String> intersectPosition = new HashSet<String>();
			for (String word : idAndPosition.keySet()) {
				Set<String> ss = new HashSet<String>();
				if (count == 0) {
					intersectPosition = idAndPosition.get(word).get(docId);
				} else {
					Set<String> s = idAndPosition.get(word).get(docId);
					for (String item : s) {
						// System.out.println(item);
						ss.add(Integer.parseInt(item) - count + "");
						// System.out.println(Integer.parseInt(item)-count +"");
					}
					System.out.println(ss);
					System.out.println(intersectPosition);
					intersectPosition.retainAll(ss);
					System.out.println(intersectPosition.toString());
				}

				count++;
			}
			if (intersectPosition.size() > 0)
				ret.add(docId);
		}
		System.out.println(ret.toString());
		return DBConnection.getUrl(SortingByFrequency(ret));
	}
	public static boolean stopword(String word) {

		String sword = " a about above after again against all aman and any are aren't as at be because been before "
				+ "being below between both but by can't cannotcould couldn't did didn't do does doesn't doing"
				+ "don't down during each few for from further had hadn't has hasn't have haven't having he"
				+ "he'll he's her here here's hersherself him himself his how how's i i'd i'll i'm i've if in "
				+ "intois isn't it it's its itself let's me more most mustn't my myself no nor not of off "
				+ "he'd on once only or other ought our ours"
				+ "ourselves outover own shan'tsheshe'd she'll she's should shouldn't so some such "
				+ "than that that's the their theirs them themselves then there there's these they they'd they'll "
				+ "they're they've this those through to too under until up very was wasn't we we'd we'll we're we've"
				+ "were weren't what what's when when's where where's which while who who's whom why why's with won't would "
				+ "wouldn't you you'd you'll you're you've your yours yourself yourselves";
		if (!sword.contains(word) && word.length()>1 &&StringUtils.isAlpha(word)) {
			return true;
		}

		return false;
	}



}

/*
 * System.out.println(intersectDocs.toString()); ArrayList<ArrayList<String>>
 * positions; ArrayList<String> temp = new ArrayList<String>();
 * ArrayList<String> temp1 = new ArrayList<String>(); Set<Integer> docs = new
 * HashSet<>();
 * 
 * // mn awl hena shof l temp w temp1 byfady wla 3lashan bya5od kol l //
 * intersect for (Integer docId : intersectDocs) { boolean flag=false; positions
 * = new ArrayList<>(); for (String word : idAndPosition.keySet()) {
 * positions.add(idAndPosition.get(word).get(docId)); }
 * System.out.println(positions.toString()); temp1.addAll(positions.get(0)); for
 * (int i = 1; i < positions.size(); i++) { temp.clear(); temp.addAll(temp1);
 * temp1.clear(); // System.out.println(temp1); for (String id :
 * positions.get(i)) { int idNext = Integer.parseInt(id) - 1; if
 * (temp.contains(idNext + "")){ temp1.add(id); flag=true; } } if(flag=false)
 * break; } if (temp1.size() > 0) { docs.add(docId); } } //
 * System.out.println(docs.toString()); return docs;
 */
