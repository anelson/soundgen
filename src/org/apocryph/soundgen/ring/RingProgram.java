package org.apocryph.soundgen.ring;

import org.apocryph.soundgen.sound.SoundGenerator;

public class RingProgram {	
	static double VOLUME = 0.5;
	
	static int BEAT_DURATION_MS = 25;
	static int ONE_BEAT = 1*BEAT_DURATION_MS;
	static int TWO_BEATS = 2*BEAT_DURATION_MS;
	static int THREE_BEATS = 3*BEAT_DURATION_MS;
	static int FOUR_BEATS = 4*BEAT_DURATION_MS;
	static int SIX_BEATS = 6*BEAT_DURATION_MS;
	
	RingGenerator generator;
	
	public RingProgram() {
		this.generator = new RingGenerator();
	}
	
	public void addSilence(int numBeats) {
		generator.addRingComponent(new SilenceRingComponent(numBeats * BEAT_DURATION_MS));
	}
	
	public void addChirp(Pitch pitch, int numBeats) {
		int numChirps = (numBeats + 1) / 2;
		int numPauses = numBeats - numChirps;
		
		Chirp chirp = NoteChirper.createChirpForNote(numChirps,
				ONE_BEAT,
				ONE_BEAT,
				pitch,
				VOLUME,
				VOLUME / 2,
				VOLUME / 4);
		
		generator.addRingComponent(new ChirpRingComponent(chirp));
	}
	
	public void generateRing(SoundGenerator soundGenerator) {
		this.generator.generate(soundGenerator);
	}
}
