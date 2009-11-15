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
	
	static int FIRST_RING_CHIRP_SOUND_DURATION_MS = 25;
	static int FIRST_RING_CHIRP_SILENCE_DURATION_MS = 28;
	static Pitch FIRST_RING_PITCH = new Pitch(Note.F, 5);
	static double FIRST_RING_HARMONIC_VOLUME = 0.45;

	static int SECOND_RING_CHIRP_SOUND_DURATION_MS = 25;
	static int SECOND_RING_CHIRP_SILENCE_DURATION_MS = 28;
	static Pitch SECOND_RING_PITCH = new Pitch(Note.B, 5);
	static double SECOND_RING_HARMONIC_VOLUME = 0.2;

	static int THIRD_RING_CHIRP_SOUND_DURATION_MS = 29;
	static int THIRD_RING_CHIRP_SILENCE_DURATION_MS = 25;
	static Pitch THIRD_RING_PITCH = new Pitch(Note.B, 4);
	static double THIRD_RING_HARMONIC_VOLUME = 0.2;	
	
	
	public static void generateRing(SoundGenerator generator) {
		RingGenerator ringGen = new RingGenerator();
		
		//Into silence
		ringGen.addRingComponent(generateSilence(105));;
		
		//Chirp-Chirp
		generateFirstRingChirps(ringGen);
		
		//Pause
		ringGen.addRingComponent(generateSilence(367));
		
		//High Chirp
		generateSecondRingChirps(ringGen);
		
		//Pause
		ringGen.addRingComponent(generateSilence(25+3));
		
		//Low chirp
		generateThirdRingChirps(ringGen);
		
		//Pause
		ringGen.addRingComponent(generateSilence(100-25));
		
		ringGen.generate(generator);
	}
	
	private static void generateFirstRingChirps(RingGenerator generator) {
		Chirp chirp = NoteChirper.createChirpForNote(2,
				FIRST_RING_CHIRP_SOUND_DURATION_MS,
				FIRST_RING_CHIRP_SILENCE_DURATION_MS,
				FIRST_RING_PITCH,
				VOLUME,
				FIRST_RING_HARMONIC_VOLUME);
		
		generator.addRingComponent(new ChirpRingComponent(chirp));
		generator.addRingComponent(generateSilence(140));
		generator.addRingComponent(new ChirpRingComponent(chirp));
	}
	
	private static void generateSecondRingChirps(RingGenerator generator) {
		Chirp chirp = NoteChirper.createChirpForNote(4,
				SECOND_RING_CHIRP_SOUND_DURATION_MS,
				SECOND_RING_CHIRP_SILENCE_DURATION_MS,
				SECOND_RING_PITCH,
				VOLUME,
				SECOND_RING_HARMONIC_VOLUME);
		
		generator.addRingComponent(new ChirpRingComponent(chirp));
	}
	
	private static void generateThirdRingChirps(RingGenerator generator) {
		Chirp chirp = NoteChirper.createChirpForNote(8,
				THIRD_RING_CHIRP_SOUND_DURATION_MS,
				THIRD_RING_CHIRP_SILENCE_DURATION_MS,
				THIRD_RING_PITCH,
				VOLUME,
				THIRD_RING_HARMONIC_VOLUME,
				THIRD_RING_HARMONIC_VOLUME);
		
		generator.addRingComponent(new ChirpRingComponent(chirp));
	}
	private static RingComponent generateSilence(int durationMs) {
		return new SilenceRingComponent(durationMs);
	}	 
}
