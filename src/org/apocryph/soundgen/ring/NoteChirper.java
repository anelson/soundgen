package org.apocryph.soundgen.ring;

public class NoteChirper {
	public static Chirp createChirpForNote(int numTones, int toneDurationMs, int delayBetweenTonesMs, Pitch pitch, double volume) {
		return createChirpForNote(numTones,
				toneDurationMs,
				delayBetweenTonesMs,
				pitch,
				volume,
				0,
				0);
	}
	
	public static Chirp createChirpForNote(int numTones, int toneDurationMs, int delayBetweenTonesMs, Pitch pitch, double fundamentalVolume, double thirdHarmonicVolume) {
		return createChirpForNote(numTones,
				toneDurationMs,
				delayBetweenTonesMs,
				pitch,
				fundamentalVolume,
				thirdHarmonicVolume,
				0);
	}
	
	public static Chirp createChirpForNote(int numTones, int toneDurationMs, int delayBetweenTonesMs, Pitch pitch, double fundamentalVolume, double thirdHarmonicVolume, double fifthHarmonicVolume) {
		return createChirpForNote(numTones,
				toneDurationMs,
				delayBetweenTonesMs,
				pitch,
				new double[] {fundamentalVolume, 0, thirdHarmonicVolume, 0, fifthHarmonicVolume});
	}
	
	public static Chirp createChirpForNote(int numTones, int toneDurationMs, int delayBetweenTonesMs, Pitch pitch, double[] fundamentalAndHarmonicVolumes) {
		Chirp chirp = new Chirp(numTones,
				toneDurationMs,
				delayBetweenTonesMs);
		
		for (int octaveIdx = 0; octaveIdx < fundamentalAndHarmonicVolumes.length; octaveIdx++) {
			chirp.addComponent(new ChirpComponent((int)pitch.getHarmonicFrequency(octaveIdx + 1),
					fundamentalAndHarmonicVolumes[octaveIdx]));			
		}
		
		return chirp;
	}
}
