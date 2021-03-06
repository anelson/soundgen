package org.apocryph.soundgen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		generate();
		System.out.println("Working\n");
	}
	
	public static void generate() throws LineUnavailableException,IOException {
		/*
		generateRing(
				FIRST_RING_PITCH,
				SECOND_RING_PITCH,
				THIRD_RING_PITCH);
		System.out.println("\n");
		*/
		generateRandomRing();
	}
	
	static Pitch FIRST_RING_PITCH = new Pitch(Note.F, 5);
	static Pitch SECOND_RING_PITCH = new Pitch(Note.B, 5);
	static Pitch THIRD_RING_PITCH = new Pitch(Note.B, 4);
	
	static int MIN_OCTAVE = 5;
	static int MAX_OCTAVE = 7;

	public static void generateRandomRing() throws LineUnavailableException,IOException {
		/*
		Pitch pitchOne = Pitch.getRandomPitch(MIN_OCTAVE, MAX_OCTAVE);
		Pitch pitchTwo = pitchOne.getRandomPitch(1);
		Pitch pitchThree = pitchTwo.getRandomPitch(1);
		
		generateRing(pitchOne,
				pitchTwo,
				pitchThree);
				*/
		Random random = new Random(1);
		
		for (int idx = 0; idx < 20; idx++) {
			RingProgram program = RingProgram.generateRandomProgram(16, random);
			SoundGenerator soundGenerator = new SoundGenerator();
			program.generateRing(soundGenerator);
			
			System.out.println("Generated random ring program " + idx + ": " +
					program.getStringRepresentation());

			generateRing(program.getStringRepresentation() + ".wav",
					soundGenerator);
		}
		/*
		generateRing("RandomRing.wav",
				soundGenerator);*/
	}
	
	public static void generateRing(Pitch firstRing, 
			Pitch secondRing,
			Pitch thirdRing) throws LineUnavailableException,IOException {
		
		String fileName = "ring-" +
			firstRing.getName() + "-" +
			secondRing.getName() + "-" +
			thirdRing.getName() + 
			".wav";
		
		generateRing(fileName,
				firstRing,
				secondRing,
				thirdRing);
	}
	
	public static void generateRing(String fileName,
			Pitch firstRing, 
			Pitch secondRing,
			Pitch thirdRing) throws LineUnavailableException,IOException {
		SoundGenerator generator = new SoundGenerator();
		generateRing(generator,
				firstRing,
				secondRing,
				thirdRing);
		
		generateRing(fileName,
				generator);
	}
	
	public static void generateRing(String fileName,
			SoundGenerator generator) throws LineUnavailableException,IOException {		
		System.out.println("Generating file " + fileName);
		
		/*
		AudioDataLineSoundPort speakerPort = new AudioDataLineSoundPort(44100, 8);
		speakerPort.startPlaying();
		generator.generateSound(speakerPort);
		speakerPort.finishPlaying(); */
		
		WaveFileSoundPort filePort = new WaveFileSoundPort(fileName, 44100, 8);
		generator.generateSound(filePort);
		filePort.writeFile();
		System.out.println("Generated file " + fileName);
	}
	
	public static void generateRing(SoundGenerator generator) {
		generateRing(generator,
				FIRST_RING_PITCH,
				SECOND_RING_PITCH,
				THIRD_RING_PITCH);
	}
	
	public static void generateRing(SoundGenerator generator,
			Pitch firstRing,
			Pitch secondRing,
			Pitch thirdRing) {
		System.out.println("Generating ring with piches " +
				firstRing.getName() +
				"," + 
				secondRing.getName() +
				"," +
				thirdRing.getName());
		
		RingProgram program = new RingProgram();
		
		//Intro silence
		program.addSilence(1);

		//Chirp-Chirp
		program.addChirp(firstRing, 1);
		program.addSilence(2);
		program.addChirp(firstRing, 1);

		//Pause
		program.addSilence(5);

		//High Chirp
		program.addChirp(secondRing, 2);

		//Low chirp
		program.addChirp(thirdRing, 4);

		//Final silence
		program.addSilence(1);
		
		program.generateRing(generator);
	}
}
