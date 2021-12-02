package Database;

import java.util.ArrayList;

public class HandleDataBase {
	private ArrayList<String> wordList = new ArrayList<String>();

	public static void main(String[] args) {
		DictionaryData dic = new DictionaryData();
		int count = 0;
		for (int i = 0; i < dic.getWordList_vi().size();i++) {
			if(dic.getWordList().contains(dic.getWordList_vi().get(i))) {
				count++;
			}
		}
		System.out.println(count);
	}
}
