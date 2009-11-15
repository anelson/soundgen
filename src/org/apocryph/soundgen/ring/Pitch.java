package org.apocryph.soundgen.ring;

import java.util.Random;

public class Pitch {
	Note note;
	int octave;
	
	public Pitch(Note note, int octave) {
		this.note = note;
		this.octave = octave;
	}
	
	public String getName() {
		return this.note.name + this.octave;
	}
	
	public static Pitch getRandomPitch() {
		return getRandomPitch(Note.MIN_OCTAVE,
				Note.MAX_OCTAVE,
				new Random());
	}
	
	public static Pitch getRandomPitch(int minOctave, int maxOctave) {
		return getRandomPitch(minOctave,
				maxOctave,
				new Random());
	}
	
	public static Pitch getRandomPitch(Random random) {
		return getRandomPitch(Note.MIN_OCTAVE,
				Note.MAX_OCTAVE,
				random);
	}

	/** Gets a random pitch within octaveRange octaves of referencePitch */
	public static Pitch getRandomPitch(Pitch referencePitch, int octaveRange) {
		return getRandomPitch(referencePitch,
				octaveRange,
				new Random());
	}

	/** Gets a random pitch within octaveRange octaves of referencePitch */
	public static Pitch getRandomPitch(Pitch referencePitch, int octaveRange, Random random) {
		int minOctave = referencePitch.octave - octaveRange;
		int maxOctave = referencePitch.octave + octaveRange;
		if (minOctave < Note.MIN_OCTAVE) {
			minOctave = Note.MIN_OCTAVE;
		}
		if (maxOctave > Note.MAX_OCTAVE) {
			maxOctave = Note.MAX_OCTAVE;
		}
		
		return getRandomPitch(minOctave,
				maxOctave,
				random);
	}
	
	public static Pitch getRandomPitch(int minOctave, int maxOctave, Random random) {
		Note note = Note.ALL_NOTES[random.nextInt(Note.ALL_NOTES.length)];
		int octave = minOctave + random.nextInt(maxOctave - minOctave + 1);
		
		return new Pitch(note, octave);
	}
	
	/** Gets a random pitch within octaveRange octaves of this one */
	public Pitch getRandomPitch(int octaveRange) {
		return Pitch.getRandomPitch(this, octaveRange);
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
