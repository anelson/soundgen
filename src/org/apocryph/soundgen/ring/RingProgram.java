package org.apocryph.soundgen.ring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apocryph.soundgen.sound.SoundGenerator;
import org.apocryph.soundgen.sound.SoundSource;

public class RingProgram {	
	static double VOLUME = 0.5;
	
	static int BEAT_DURATION_MS = 25;
	static int ONE_BEAT = 1*BEAT_DURATION_MS;
	static int TWO_BEATS = 2*BEAT_DURATION_MS;
	static int THREE_BEATS = 3*BEAT_DURATION_MS;
	static int FOUR_BEATS = 4*BEAT_DURATION_MS;
	static int SIX_BEATS = 6*BEAT_DURATION_MS;
	
	static int MIN_OCTAVE = 4;
	static int MAX_OCTAVE = 5;
	
	List<Pitch> notes = new ArrayList<Pitch>();
	
	private interface Pattern {
		int getLength();
		void generate(RingProgram program, Random random);
	}
	
	/** Special case pattern that provides a length of silence */
	static class SilencePattern implements Pattern {
		int length;
		
		public SilencePattern(int length) {
			this.length = length;
		}
		
		public int getLength() { return this.length; }
		public void generate(RingProgram program, Random random) {
			program.addSilence(this.length);
		}
	};
	
	static Pattern[] PATTERNS = new Pattern[] {
		//Null pattern, stands for silence
		null,
		
		//Four count tone pattern
		new Pattern() {
			public int getLength() { return 4; }
			public void generate(RingProgram program, Random random) {
				program.addChirp(Pitch.getRandomPitch(MIN_OCTAVE, MAX_OCTAVE, random),
						getLength());
			}
		},
		
		//Eight count tone pattern
		new Pattern() {
			public int getLength() { return 8; }
			public void generate(RingProgram program, Random random) {
				program.addChirp(Pitch.getRandomPitch(MIN_OCTAVE, MAX_OCTAVE, random),
						getLength());
			}
		},
		
		//Tone, two rest, tone
		new Pattern() {
			public int getLength() { return 4; }
			
			public void generate(RingProgram program, Random random) {
				Pitch pitch = Pitch.getRandomPitch(MIN_OCTAVE, MAX_OCTAVE, random);
				
				program.addChirp(pitch, 1);
				program.addSilence(1);
				program.addChirp(pitch, 1);
			}
		}
	};
	
	RingGenerator generator;
	
	public RingProgram() {
		this.generator = new RingGenerator();
	}
	
	public static RingProgram generateRandomProgram(int length) {
		return generateRandomProgram(length,
				new Random());
	}
	
	public static RingProgram generateRandomProgram(int length, Random random) {
		//Keep generating randomly selected patterns until we reach the target length
		RingProgram program = new RingProgram();
		
		//Some rules:
		// The first and last pattern cannot be pattern 0, which is silence
		int lengthRemaining = length;
		while (lengthRemaining > 0 && doesSuitablePatternExist(length, lengthRemaining)) {
			//Pick a suitable pattern.
			Pattern pattern = findSuitablePattern(length, lengthRemaining, random);
			
			pattern.generate(program, random);
			
			lengthRemaining -= pattern.getLength();
		}
		
		if (lengthRemaining > 0) {
			//Couldn't perfectly fill the program with patterns.
			//Just generate a single tone to fill the rest of the space
			program.addChirp(Pitch.getRandomPitch(MIN_OCTAVE, MAX_OCTAVE, random), 
						1);
		}
		
		return program;
	}
	
	private static boolean doesSuitablePatternExist(int length,
			int lengthRemaining) {
		for (int idx = 0; idx < PATTERNS.length; idx++) {
			if (isPatternSuitable(length, 
					lengthRemaining,
					PATTERNS[idx])) {
				return true;
			}
		}
		
		return false;
	}

	private static Pattern findSuitablePattern(int length, int lengthRemaining, Random random) {
		while (true) {
			Pattern pattern = PATTERNS[random.nextInt(PATTERNS.length)];
			if (isPatternSuitable(length, lengthRemaining, pattern)) {
				//Pattern is suitable.  If it's the silence pattern, create an instance
				//of SilencePattern
				if (pattern == null) {
					int maxSilenceDuration = 3;
					if (maxSilenceDuration > lengthRemaining - 1) {
						maxSilenceDuration = lengthRemaining - 1;
					}
					
					pattern = new SilencePattern(1 + random.nextInt(maxSilenceDuration));
				}
				return pattern;
			}
		}
	}
	
	private static boolean isPatternSuitable(int length, int lengthRemaining, Pattern pattern) {
		if (pattern == null) {
			//The silence pattern was selected.  Is that allowed?
			if (lengthRemaining == length) {
				//The silence pattern was selected but this is the first
				//pattern of the ring, so silence isn't allowed
				return false;
			} else if (lengthRemaining == 1) {
				//Only one count remaining; no ending on silence
				return false;
			}
			
			//Silence is OK here
			return true;
		}
		
		//If this pattern is too long for the remaining length, reject it
		if (pattern.getLength() > lengthRemaining) {
			return false;
		}
		
		//Ok, this pattern is good
		return true;
	}

	public void addSilence() {
		addSilence(1);
	}
	
	public void addSilence(int count) {
		for (int idx = 0; idx < count; idx++) {
			this.notes.add(null);
		}
	}
	
	public void addChirp(Pitch pitch, int count) {
		for (int idx = 0; idx < count; idx++) {
			this.notes.add(pitch);
		}
	}
	
	public String getStringRepresentation() {
		String program = "";
		
		for (int idx = 0; idx < this.notes.size(); idx++) {
			Pitch pitch = this.notes.get(idx);
			
			if (pitch == null) {
				program += "_";
			} else {
				program += pitch.getName();
			}
			
			if (idx + 1 < this.notes.size()) {
				program += " ";
			}
		}	
		
		return program;
	}
	
	public void generateRing(SoundGenerator soundGenerator) {
		for (int idx = 0; idx < this.notes.size(); ) {
			Pitch pitch = this.notes.get(idx);
			int count = getPitchDuration(pitch, idx);
			
			if (pitch == null) {
				//Silence
				generator.addRingComponent(new SilenceRingComponent(ONE_BEAT*3*count));
			} else {
				//One "count" is a chirp tone, a rest, and another chirp tone
				//That's two chirps
				Chirp chirp = NoteChirper.createChirpForNote(count*2,
						ONE_BEAT,
						ONE_BEAT,
						pitch,
						VOLUME,
						VOLUME / 2,
						VOLUME / 4);
				
				generator.addRingComponent(new ChirpRingComponent(chirp));
				
				//If the next component is a chirp, insert a one beat pause,
				//if it's silence, that's obviously its own pause
				if (idx + count < this.notes.size()) {
					if (this.notes.get(idx+count) != null) {
						generator.addRingComponent(new SilenceRingComponent(ONE_BEAT));
					}
				}
			}
			
			idx+=count;
		}
		this.generator.generate(soundGenerator);
	}

	private int getPitchDuration(Pitch pitch, int idx) {
		int count = 1;
		while (++idx < this.notes.size()) {
			if (this.notes.get(idx) != pitch) {
				break;
			}
			
			count++;
		}
		
		return count;
	}
}
