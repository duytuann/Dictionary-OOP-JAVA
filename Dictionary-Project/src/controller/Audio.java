package controller;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Audio {
	public String word;

	Audio() {
	}

	Audio(String word) {
		this.word = word;
	}

	public void pronounce() {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		Voice voice = VoiceManager.getInstance().getVoice("kevin16");

		if (voice != null) {
			voice.allocate();
		}
		try {
			voice.setRate(120);
			voice.setPitch(100);
			voice.setVolume(10);
			voice.speak(word);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Audio audio = new Audio("I love you so much");
		audio.pronounce();
	}
}