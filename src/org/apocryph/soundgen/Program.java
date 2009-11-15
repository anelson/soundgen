package org.apocryph.soundgen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.*;

import org.apocryph.soundgen.ring.Chirp;
import org.apocryph.soundgen.ring.ChirpComponent;
import org.apocryph.soundgen.ring.ChirpRingComponent;
import org.apocryph.soundgen.ring.Note;
import org.apocryph.soundgen.ring.NoteChirper;
import org.apocryph.soundgen.ring.Pitch;
import org.apocryph.soundgen.ring.RingComponent;
import org.apocryph.soundgen.ring.RingGenerator;
import org.apocryph.soundgen.ring.RingProgram;
import org.apocryph.soundgen.ring.SilenceRingComponent;
import org.apocryph.soundgen.sound.AdditiveSoundSource;
import org.apocryph.soundgen.sound.AudioDataLineSoundPort;
import org.apocryph.soundgen.sound.SilenceSoundSource;
import org.apocryph.soundgen.sound.SineWaveSoundSource;
import org.apocryph.soundgen.sound.SoundGenerator;
import org.apocryph.soundgen.sound.SoundSource;
import org.apocryph.soundgen.sound.WaveFileSoundPort;

public class Program {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Playing sound\n");
		//generateTone(690, 1000, 100, true);
		generate();
		System.out.println("Working\n");
	}
	
	public static void generate() throws LineUnavailableException,IOException {
		SoundGenerator generator = new SoundGenerator();
		generateRing(generator);
		AudioDataLineSoundPort speakerPort = new AudioDataLineSoundPort(44100, 8);
		speakerPort.startPlaying();
		generator.generateSound(speakerPort);
		speakerPort.finishPlaying();
		
		WaveFileSoundPort filePort = new WaveFileSoundPort("ring.wav", 44100, 8);
		generator.generateSound(filePort);
		filePort.writeFile();
	}
	
	static double VOLUME = 0.5;
	
	static int BEAT_DURATION_MS = 25;
	static int ONE_BEAT = 1*BEAT_DURATION_MS;
	static int TWO_BEATS = 2*BEAT_DURATION_MS;
	static int THREE_BEATS = 3*BEAT_DURATION_MS;
	static int FOUR_BEATS = 4*BEAT_DURATION_MS;
	static int SIX_BEATS = 6*BEAT_DURATION_MS;
	
	static int FIRST_RING_CHIRP_SOUND_DURATION_MS = ONE_BEAT;
	static int FIRST_RING_CHIRP_SILENCE_DURATION_MS = ONE_BEAT;
	static Pitch FIRST_RING_PITCH = new Pitch(Note.F, 5);

	static int SECOND_RING_CHIRP_SOUND_DURATION_MS = ONE_BEAT;
	static int SECOND_RING_CHIRP_SILENCE_DURATION_MS = ONE_BEAT;
	static Pitch SECOND_RING_PITCH = new Pitch(Note.B, 5);

	static int THIRD_RING_CHIRP_SOUND_DURATION_MS = ONE_BEAT;
	static int THIRD_RING_CHIRP_SILENCE_DURATION_MS = ONE_BEAT;
	static Pitch THIRD_RING_PITCH = new Pitch(Note.B, 4);
	
	
	public static void generateRing(SoundGenerator generator) {
		RingProgram program = new RingProgram();
		
		//Into silence
		program.addSilence(4);
		
		//Chirp-Chirp
		program.addChirp(FIRST_RING_PITCH, 3);
		program.addSilence(6);
		program.addChirp(FIRST_RING_PITCH, 3);
		
		//Pause
		program.addSilence(15);
		
		//High Chirp
		program.addChirp(SECOND_RING_PITCH, 7);
		
		//Pause
		program.addSilence(1);
		
		//Low chirp
		program.addChirp(THIRD_RING_PITCH, 15);
		
		//Final silence
		program.addSilence(4);
		
		program.generateRing(generator);
	}
	
	private static void generateFirstRingChirps(RingGenerator generator) {
		generateChirps(generator, FIRST_RING_PITCH, 2);
		generator.addRingComponent(generateSilence(SIX_BEATS));
		generateChirps(generator, FIRST_RING_PITCH, 2);
	}
	
	private static void generateSecondRingChirps(RingGenerator generator) {
		generateChirps(generator, SECOND_RING_PITCH, 4);
	}
	
	private static void generateThirdRingChirps(RingGenerator generator) {
		generateChirps(generator, THIRD_RING_PITCH, 8);
	}
	
	private static void generateChirps(RingGenerator generator, Pitch pitch, int numChirps) {
		Chirp chirp = NoteChirper.createChirpForNote(numChirps,
				ONE_BEAT,
				ONE_BEAT,
				pitch,
				VOLUME,
				VOLUME / 2,
				VOLUME / 4);
		
		generator.addRingComponent(new ChirpRingComponent(chirp));
		
	}
	private static RingComponent generateSilence(int durationMs) {
		return new SilenceRingComponent(durationMs);
	}	 
}
