package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import Database.DictionaryData;

public class Handle {
	static DictionaryData dicData;

	Handle() {
		dicData = new DictionaryData();
		this.getConnection();
		
		try {
			Statement statement = this.getConnection().createStatement();

			String sql = "SELECT * FROM add_word_by_user";

			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String word = rs.getString("word").toLowerCase().trim();
				String html = rs.getString("html");

				dicData.getWordList().add(word);
				dicData.getWord_html_en().put(word, html);
			}
			Collections.sort(dicData.getWordList());
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		try {
//			Statement statement = this.getConnection().createStatement();
//
//			String sql = "SELECT * FROM add_word_by_user_vi";
//			
//			ResultSet rs = statement.executeQuery(sql);
//			while (rs.next()) {
//				String word = rs.getString("word").toLowerCase().trim();
//				String html = rs.getString("html_vi");
//
//				this.wordList.add(word);
//				this.word_html_vi.put(word, html);
//			}
//			Collections.sort(this.wordList_vi);
//			statement.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
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

	public void add_en(String word, String spelling, String type, String gloss) {
		String html = "";
		html += "<html lang='en'><head><meta charset='UTF-8' /></head><body>";
		html += "<p style='margin: 5px; font-size: 25px; color: #474747'>" + word + "</p>";
		html += "<p style='margin: 5px; color: #40658b'>" + spelling + "</p>";
		html += "<p style='color: #043566'>" + type + "</p>";
		html += "<p>" + gloss + "</p>";
		html += "</body></html>";

		try {
			Statement statement = dicData.getConnection().createStatement();
			String sql = "INSERT INTO add_word_by_user (word,html) VALUES ('" + word + "'," + "\"" + html + "\"" + ");" ;
			// INSERT INTO table_name (column1, column2, column3,...) VALUES (value1, value2, value3,...)
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		dicData.getWordList().add(word);
//		Collections.sort(dicData.getWordList());
//		dicData.getWord_html_en().put(word, html);
	}
	
	public void add_vi(String word, String spelling, String type, String gloss) {
		String html = "";
		html += "<html lang='en'><head><meta charset='UTF-8' /></head><body>";
		html += "<p style='margin: 5px; font-size: 25px; color: #474747'>" + word + "</p>";
		html += "<p style='margin: 5px; color: #40658b'>" + spelling + "</p>";
		html += "<p style='color: #043566'>" + type + "</p>";
		html += "<p>" + gloss + "</p>";
		html += "</body></html>";

		try {
			Statement statement = dicData.getConnection().createStatement();
			String sql = "INSERT INTO tbl_edict (word,detail) VALUES ('" + word + "'," + "\"" + html + "\"" + ");" ;
			// INSERT INTO table_name (column1, column2, column3,...) VALUES (value1, value2, value3,...)
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public void delete_en(String s) {
		try {
			Statement statement = dicData.getConnection().createStatement();
			// DELETE FROM table_name WHERE condition;
			String sql = "DELETE FROM wn_synset where word = '" + s + "';";

			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete_vi(String s) {
		try {
			Statement statement = dicData.getConnection().createStatement();
			// DELETE FROM table_name WHERE condition;
			String sql = "DELETE FROM tbl_edict where word = '" + s + "';";

			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void edit_vi(String word, String spelling, String type, String gloss) {
		this.delete_vi(word);
		this.add_vi(word,spelling,type,gloss);
	}

	public static void main(String[] args) {
		Handle handle = new Handle();
		handle.add_vi("aaaaaaaaaaaa","aaaaaaa","aaaa","aaa");
	}
	
	
}
