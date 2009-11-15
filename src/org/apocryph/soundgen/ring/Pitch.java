package org.apocryph.soundgen.ring;

public class Pitch {
	Note note;
	int octave;
	
	public Pitch(Note note, int octave) {
		this.note = note;
		this.octave = octave;
	}
	
	/** Gets the frequency of the fundamental tone (also called the 1st harmonic) */
	public double getFundamentalFrequency() {
		return this.note.getFrequency(this.octave);
	}
	
	/** Gets the frequency for a harmonic.  Harmonic 1 is the same as the fundamental 
	 * frequency, harmonic 2 is the second harmonic or the 1st overtone, etc.
	 * @param harmonic
	 * @return
	 */
	public double getHarmonicFrequency(int harmonic) throws IllegalArgumentException {
		if (harmonic < 1) {
			throw new IllegalArgumentException("harmonic");
		}
		
		return getFundamentalFrequency() * harmonic;
	}
}
