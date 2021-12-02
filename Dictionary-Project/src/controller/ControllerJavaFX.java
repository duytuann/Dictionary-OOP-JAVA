package controller;

import Database.DictionaryData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ControllerJavaFX implements Initializable {
	@FXML
	private ListView<String> recommendWordsList_aa;
	
	@FXML
	private ListView<String> recommendWordsList_av;
	
	@FXML
	private TextField wordField_aa, wordField_av, deleteField, addField, spellingField, typeField;
	
	@FXML
	private TextArea glossaryField;

	@FXML
	private WebView webView;
	
	@FXML
    private Button Delete_btn, Add_btn, Edit_btn;
	
	@FXML
	private AnchorPane More_pane;
	
	@FXML
	private ImageView More_btn;


	private Trie trie;
	private Trie trie_vi;
	ObservableList<String> observableList_aa;
	ObservableList<String> observableList_av;
	private DictionaryData dicData;
	private Handle handle;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handle = new Handle();
		dicData = new DictionaryData();
		trie = new Trie(dicData.getWordList());
		trie_vi = new Trie(dicData.getWordList_vi());
		observableList_aa = FXCollections.observableList(dicData.getWordList());
		recommendWordsList_aa.setItems(observableList_aa);
		observableList_av = FXCollections.observableList(dicData.getWordList_vi());
		recommendWordsList_av.setItems(observableList_av);
	}

	@FXML
	void setOnKeyReleased(KeyEvent event) {
		textField_aa_event();
		textField_av_event();
		wordField_av.setOnKeyReleased(keyEvent -> {
			textField_av_event();
            if(keyEvent.getCode() == KeyCode.ENTER){
            	String str = wordField_av.getText();
            	String html = dicData.getWord_html_vi().get(str);
        		WebEngine engine = webView.getEngine();
        		engine.loadContent(html);
            }
        });
		wordField_aa.setOnKeyReleased(keyEvent -> {
			textField_aa_event();
            if(keyEvent.getCode() == KeyCode.ENTER){
            	String str = wordField_aa.getText();
            	String html = dicData.getWord_html_en().get(str);
        		WebEngine engine = webView.getEngine();
        		engine.loadContent(html);
            }
        });
	}

	@FXML
	void setOnMouseClicked_aa(MouseEvent event) {
		if (event.getClickCount() == 2) {
			Search_aa();
		}
	}
	
	@FXML
	void setOnMouseClicked_av(MouseEvent event) {
		if (event.getClickCount() == 2) {
			Search_av();
		}
	}
	
	@FXML
	void setOnMouseClicked(MouseEvent event) {
		if(event.getTarget() == More_btn) {
			More_pane.setVisible(!More_pane.isVisible());
		}
	}

	@FXML
	void setOnAction(ActionEvent event) {
		if(event.getTarget() == Delete_btn) {
			delete_event_vi();
		}
		if(event.getTarget() == Add_btn) {
			add_event_vi();
		}
		if(event.getTarget() == Edit_btn) {
			edit_event_vi();
		}
	}
	
	private void updateDictionary() {
		dicData = new DictionaryData();
		trie = new Trie(dicData.getWordList());
		trie_vi = new Trie(dicData.getWordList_vi());
		observableList_aa = FXCollections.observableList(dicData.getWordList());
		recommendWordsList_aa.setItems(observableList_aa);
		observableList_av = FXCollections.observableList(dicData.getWordList_vi());
		recommendWordsList_av.setItems(observableList_av);
	}
	
	private void textField_aa_event() {
		String s = wordField_aa.getText();
		List<String> a = trie.suggest(s);
		observableList_aa.clear();
		observableList_aa.addAll(a);
	}
	
	private void textField_av_event() {
		String s = wordField_av.getText();
		List<String> a = trie_vi.suggest(s);
		observableList_av.clear();
		observableList_av.addAll(a);
	}

	private void Search_aa() {
		String str1 = recommendWordsList_aa.getSelectionModel().getSelectedItems().toString();
		str1 = str1.substring(1, str1.length() - 1);
		wordField_aa.setText(str1);
		String html1 = dicData.getWord_html_en().get(str1);
		WebEngine engine = webView.getEngine();
		engine.loadContent(html1);
	}
	
	private void Search_av() {
		String str2 = recommendWordsList_av.getSelectionModel().getSelectedItems().toString();
		str2 = str2.substring(1, str2.length() - 1);
		wordField_av.setText(str2);
		String html2 = dicData.getWord_html_vi().get(str2);
		WebEngine engine = webView.getEngine();
		engine.loadContent(html2);
	}
	
	private void delete_event_vi() {
		String s_delete = deleteField.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete a Word");
        alert.setHeaderText("You're about delete a word English to Vietnamese!");
        if(s_delete.equals("")) {
            alert.setContentText("You have not entered the word, please type it");
            alert.show();
            return;
        }
        else {
        	handle.delete_vi(s_delete);
        	updateDictionary();
        	alert.setContentText("The word \"" + s_delete + "\" deleted!");	
            alert.show();
        }
	}
	
	private void add_event_vi() {
		String s_add = addField.getText();
		String s_spell = spellingField.getText();
		String s_type = typeField.getText();
		String s_glossary = glossaryField.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add a Word");
        alert.setHeaderText("You're about add a word English to Vietnamese!");
        if(s_add.equals("")) {
            alert.setContentText("You have not entered the word, please type it");
            alert.show();
            return;
        }
        else {
        	handle.add_vi(s_add, s_spell, s_type, s_glossary);
        	updateDictionary();
        	alert.setContentText("The word \"" + s_add + "\" added!");	
            alert.show();
        }
	}
	
	private void edit_event_vi() {
		String s_add = addField.getText();
		String s_spell = spellingField.getText();
		String s_type = typeField.getText();
		String s_glossary = glossaryField.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit a Word");
        alert.setHeaderText("You're about edit a word English to Vietnamese!");
        if(s_add.equals("")) {
            alert.setContentText("You have not entered the word, please type it");
            alert.show();
            return;
        }
        else {
        	handle.edit_vi(s_add, s_spell, s_type, s_glossary);
        	updateDictionary();
        	alert.setContentText("The word \"" + s_add + "\" edited!");	
            alert.show();
        }
	}
	
}
