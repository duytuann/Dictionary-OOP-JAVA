package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;

public class DictionaryData {
	private ArrayList<Word> Dictionary = new ArrayList<Word>();

	private ArrayList<String> wordList = new ArrayList<String>();
	private ArrayList<String> wordList_vi = new ArrayList<String>();
	private Map<String, String> word_html_vi = new HashMap<String, String>();

	private Map<String, HashSet<String>> word_type = new HashMap<String, HashSet<String>>();
	private Map<String, ArrayList<Integer>> word_id = new HashMap<String, ArrayList<Integer>>();
	private Map<Integer, ArrayList<String>> id_synset = new HashMap<Integer, ArrayList<String>>();
	private Map<Integer, String> id_type = new HashMap<Integer, String>();
	private Map<Integer, String> id_glossary = new HashMap<Integer, String>();
	private Map<String, String> word_spelling = new HashMap<String, String>();
	private Map<String, String> word_html_en = new HashMap<String, String>();

	public DictionaryData() {
		this.getDataFromSever();
		this.getDataFromFile();
		this.addHTML();
		List<String> Unique = this.wordList.stream().distinct().collect(Collectors.toList());
		wordList = new ArrayList<String>(Unique);
	}

	public ArrayList<String> getWordList_vi() {
		return wordList_vi;
	}

	public Map<String, String> getWord_html_vi() {
		return word_html_vi;
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}

	public Map<String, String> getWord_html_en() {
		return word_html_en;
	}

	private final String FILE_SPELLING_PATH = "src/en_US.txt";

	public String getFILE_SPELLING_PATH() {
		return FILE_SPELLING_PATH;
	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/wn_pro_mysql";
			String user = "root";
			String password = "";
			return DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}
	}

	public void getDataFromSever() {

		// id_synset word_id
		try {
			Statement statement = this.getConnection().createStatement();

			String sql = "SELECT * FROM wn_synset";

			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				String id_str = rs.getString("synset_id");

				Integer id = Integer.valueOf(id_str);
				String word = rs.getString("word").toLowerCase().trim();
				String type = rs.getString("ss_type");

				if (this.word_id.get(word) == null) {
					this.word_id.put(word, new ArrayList<Integer>());
				}
				this.word_id.get(word).add(id);

				this.wordList.add(word);
				if (this.id_synset.get(id) == null) {
					this.id_synset.put(id, new ArrayList<String>());
				}
				this.id_synset.get(id).add(word);

				this.id_type.put(id, type);

				if (this.word_type.get(word) == null) {
					this.word_type.put(word, new HashSet<String>());
				}
				this.word_type.get(word).add(type);
			}
			Collections.sort(this.wordList);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// id_glossary
		try {
			Statement statement = this.getConnection().createStatement();

			String sql = "SELECT * FROM wn_gloss";

			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				String id_str = rs.getString("synset_id");

				Integer id = Integer.valueOf(id_str);
				String glossary = rs.getString("gloss");

				this.id_glossary.put(id, glossary);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// vi
		// private ArrayList<String> wordList_vi = new ArrayList<String>();
		// private Map<String, String> word_html_vi = new HashMap<String, String>();
		try {
			Statement statement = this.getConnection().createStatement();

			String sql = "SELECT * FROM tbl_edict";

			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String word = rs.getString("word").trim();
				String detail = rs.getString("detail");
				this.wordList_vi.add(word);
				StringBuilder sb = new StringBuilder(detail);
				sb.deleteCharAt(15);
				String html = sb.toString();
				this.word_html_vi.put(word, html);
			}
			Collections.sort(this.wordList);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Map<String,String> word_spelling
	// can (word -> null)
	public void getDataFromFile() {
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE_SPELLING_PATH));
			while ((line = reader.readLine()) != null) {
				String word;
				String spelling;
				String[] temp = line.split("\\t");
				word = temp[0];
				spelling = temp[1];
				this.word_spelling.put(word, spelling);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(this.word_spelling.size());
			System.out.println("Can't read file:" + FILE_SPELLING_PATH);
		}
	}

	// HTMl
	public String convert_htmt_en(String word) {
		Integer id = this.word_id.get(word).get(0);
//		String type = this.id_type.get(id);
		String spelling = null;

		if (this.word_spelling.containsKey(word)) {
			spelling = this.word_spelling.get(word);
		}

		ArrayList<String> synset = this.id_synset.get(id);
		if (this.word_spelling.containsKey(word)) {
			spelling = this.word_spelling.get(word);
		}
		if (this.word_spelling.containsKey(word)) {
			spelling = this.word_spelling.get(word);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(
				"<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8' /><meta http-equiv='X-UA-Compatible' content='IE=edge' /><meta name='viewport' content='width=device-width, initial-scale=1.0' /><title>Document</title></head><body>");
		sb.append("<p style='margin: 5px; font-size: 25px; color: #474747'>");
		sb.append(word);
		sb.append("</p>");
		// If have SPELLING -> add 1 <p> tag
		if (spelling != null) {
			sb.append("<p style='margin: 5px; color: #40658b'>");
			sb.append(spelling);
			sb.append("</p>");
		}

		for (String type : this.word_type.get(word)) {
			sb.append("<p style = 'padding: 0px 5px; margin: 0'");
			sb.append("<b style='color: #043566'>");
			// Map<String, HashSet<String>> word_type
			if (type.equals("n")) {
				sb.append("Noun");
			} else if (type.equals("v")) {
				sb.append("Verb");
			} else if (type.equals("s") || type.equals("a")) {
				sb.append("Adjective");
			} else if (type.equals("r")) {
				sb.append("Adverb");
			}
			sb.append("</b></p>");
			sb.append("<ol style='padding-left: 20px'>");

			for (int i = 0; i < this.word_id.get(word).size(); i++) {
				Integer ID = this.word_id.get(word).get(i);
				String temp = this.id_glossary.get(ID);

				int count = temp.length() - temp.replace(";", "").length();
				// id_type Noun Verb Adjective Adverb
				String[] glass = temp.split(";");
				if (this.id_type.get(ID).equals(type)) {
					sb.append("<li><section style='padding-bottom: 5px'>");
					if (count == 0) {
						sb.append("<p style='margin: 5px'>");
						sb.append(glass[0].trim());
						sb.append("</p>");
						if (this.id_synset.get(ID).size() > 1) {
							sb.append("<p style = 'color: #666666; margin: 0px;'>");
							sb.append("(");
							ArrayList<String> trash = this.id_synset.get(ID);
							trash.remove(word);
							for (int k = 0; k < trash.size(); k++) {
								// Map<Integer, ArrayList<String>> id_synset

								if (k == this.id_synset.get(ID).size() - 1) {
									sb.append(this.id_synset.get(ID).get(k));
								} else {
									sb.append(this.id_synset.get(ID).get(k));
									sb.append("; ");
								}
							}
							sb.append(")");
							sb.append("</p>");
						}
					} else {
						sb.append("<p style='margin: 5px'>");
						sb.append(glass[0].trim());
						sb.append("</p>");
						if (this.id_synset.get(ID).size() > 1) {
							sb.append("<p style = 'color: #666666; margin: 0px;'>");
							sb.append("(");
							for (int k = 0; k < this.id_synset.get(ID).size(); k++) {
								if (this.id_synset.get(ID).get(k).equals(word)) {
									continue;
								} else if (k == this.id_synset.get(ID).size() - 1) {
									sb.append(this.id_synset.get(ID).get(k));
								} else {
									sb.append(this.id_synset.get(ID).get(k));
									sb.append("; ");
								}
							}
							sb.append(")");
							sb.append("</p>");
						}
						sb.append("<ul style='margin: 5px'>");
						for (int j = 1; j < glass.length; j++) {
							sb.append("<li><i>");
							sb.append(glass[j].replace('"', ' ').trim());
							sb.append("</i></li>");
							// <li>his love for his work</li>
						}
						sb.append("</ul>");
					}
					sb.append("</section></li>");
				}

			}
			sb.append("</ol>");
		}
		sb.append("</body></html>");
		String res = sb.toString();
		return res;
	}

	public void addHTML() {
		for (int i = 0; i < this.wordList.size(); i++) {
			String w_target = this.wordList.get(i);
			String html_en = this.convert_htmt_en(w_target);
			word_html_en.put(w_target, html_en);
		}
	}

	public static void main(String[] args) {
		DictionaryData dic = new DictionaryData();
		
		System.out.println(dic.wordList.size());
		System.out.println(dic.wordList_vi.size());
		
		System.out.println(dic.word_html_en.size());
		System.out.println(dic.word_html_en.get("love"));
		System.out.println(dic.word_html_en.get("good"));
	}

}
