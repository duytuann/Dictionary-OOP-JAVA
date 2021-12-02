package Database;

import java.util.ArrayList;

public class Word {
	private String WordType;
	private String WordSpelling;
	private String WordTarget;
	private String Word_html_en;
	private String Word_html_vi;

	public Word(String WordType, String WordSpelling, String WordTarget,String Word_html_en) {
		this.WordType = WordType;
		this.WordSpelling = WordSpelling;
		this.WordTarget = WordTarget;
		this.Word_html_en = Word_html_en;
	}

	public String getWordType() {
		return WordType;
	}

	public void setWordType(String wordType) {
		WordType = wordType;
	}

	public String getWordSpelling() {
		return WordSpelling;
	}

	public void setWordSpelling(String wordSpelling) {
		WordSpelling = wordSpelling;
	}

	public String getWordTarget() {
		return WordTarget;
	}

	public void setWordTarget(String wordTarget) {
		WordTarget = wordTarget;
	}

	public String getWord_html_en() {
		return Word_html_en;
	}

	public void setWord_html_en(String word_html_en) {
		Word_html_en = word_html_en;
	}

}