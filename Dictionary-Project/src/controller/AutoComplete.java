package controller;

import java.util.ArrayList;
import java.util.Scanner;

import Database.DictionaryData;

public class AutoComplete {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		DictionaryData dic = new DictionaryData();
		ArrayList<String> list = dic.getWordList();
		Trie trie = new Trie(list);
		String a = sc.nextLine();
		System.out.println(trie.suggest(a));
		
	}
}
