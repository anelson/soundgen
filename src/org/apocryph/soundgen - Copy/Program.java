package org.apocryph.soundgen;

import java.io.IOException;

import javax.sound.sampled.*;

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
		/*
		SineWaveSoundSource sound440hz = new SineWaveSoundSource(440, 1000, 0.6);
		SineWaveSoundSource sound440hzHarmonic = new SineWaveSoundSource(880, 1000, 0.4);
		AdditiveSoundSource combined = new AdditiveSoundSource();
		combined.addSoundSource(sound440hz);
		combined.addSoundSource(sound440hzHarmonic);
		
		generator.addSoundSource(combined);*/
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
	static int FIRST_RING_FREQ_1 = 690;
	static int FIRST_RING_FREQ_2 = 2076;
	
	static int SECOND_RING_FREQ_1 = 983;
	static int SECOND_RING_FREQ_2 = 2953;
	
	static int THIRD_RING_FREQ_1 = 495;
	static int THIRD_RING_FREQ_2 = 1477;
	static int THIRD_RING_FREQ_3 = 2461;
	
	static int FIRST_RING_CHIRP_SOUND_DURATION_MS = 25;
	static int FIRST_RING_CHIRP_SILENCE_DURATION_MS = 28;
	
	static int SECOND_RING_CHIRP_SOUND_DURATION_MS = 25;
	static int SECOND_RING_CHIRP_SILENCE_DURATION_MS = 28;
	
	static int THIRD_RING_CHIRP_SOUND_DURATION_MS = 29;
	static int THIRD_RING_CHIRP_SILENCE_DURATION_MS = 25;
	
	public static void generateRing(SoundGenerator generator) {
		//Into silence
		generator.addSoundSource(generateSilence(105));
		
		//Chirp-Chirp
		generateFirstRingChirps(generator);
		
		//Pause
		generator.addSoundSource(generateSilence(367));
		
		//High Chirp
		generateSecondRingChirp(generator);
		
		//Pause
		generator.addSoundSource(generateSilence(25+3));
		
		//Low chirp
		generateThirdRingChirp(generator);
		
		//Pause
		generator.addSoundSource(generateSilence(100-25));
	}

	private static void generateFirstRingChirps(SoundGenerator generator) {
		generateChirps(generator,
				2,
				FIRST_RING_CHIRP_SOUND_DURATION_MS, 
				FIRST_RING_CHIRP_SILENCE_DURATION_MS,
				FIRST_RING_FREQ_1, FIRST_RING_FREQ_2);
		
		//Pause
		generator.addSoundSource(generateSilence(140));

		generateChirps(generator,
				2,
				FIRST_RING_CHIRP_SOUND_DURATION_MS, 
				FIRST_RING_CHIRP_SILENCE_DURATION_MS,
				FIRST_RING_FREQ_1, FIRST_RING_FREQ_2);
	}

	private static void generateSecondRingChirp(SoundGenerator generator) {
		generateChirps(generator,
				4,
				SECOND_RING_CHIRP_SOUND_DURATION_MS, 
				SECOND_RING_CHIRP_SILENCE_DURATION_MS,
				SECOND_RING_FREQ_1, SECOND_RING_FREQ_2);
	}

	private static void generateThirdRingChirp(SoundGenerator generator) {
		generateChirps(generator,
				8,
				THIRD_RING_CHIRP_SOUND_DURATION_MS, 
				THIRD_RING_CHIRP_SILENCE_DURATION_MS,
				THIRD_RING_FREQ_1, THIRD_RING_FREQ_2);
	}

	private static void generateChirps(SoundGenerator generator,
			int numChirps,
			int soundDurationMs,
			int silenceDurationMs, 
			int freq1,
			int freq2) {
		generateChirps(generator,
				numChirps,
				soundDurationMs,
				silenceDurationMs,
				freq1,
				freq2,
				0);
	}

	private static void generateChirps(SoundGenerator generator,
			int numChirps,
			int soundDirationMs,
			int silenceDurationMs, 
			int freq1,
			int freq2,
			int freq3) {		
		for (int idx = 0; idx < numChirps; idx++) {
			generator.addSoundSource(generateRing(soundDirationMs, freq1, freq2, freq3));
			
			if (idx + 1 < numChirps) {
				generator.addSoundSource(generateSilence(silenceDurationMs));
			}
		}
	}

	private static SoundSource generateRing(int durationMs, int freq1, int freq2) {
		return generateRing(durationMs, freq1, freq2, 0);
	}

	private static SoundSource generateRing(int durationMs, int freq1, int freq2, int freq3) {
		AdditiveSoundSource combined = new AdditiveSoundSource();
		combined.addSoundSource(new SineWaveSoundSource(freq1, durationMs, VOLUME));
		//ANELSON TESTING
		//Instead of secondary freqs insert higher order harmonics
		/*
		combined.addSoundSource(new SineWaveSoundSource(freq2, durationMs, VOLUME));
		if (freq3 > 0) {
			combined.addSoundSource(new SineWaveSoundSource(freq3, durationMs, VOLUME));
		}
		*/
		//combined.addSoundSource(new SineWaveSoundSource(freq1*3, durationMs, VOLUME/2));
		combined.addSoundSource(new SineWaveSoundSource(freq1*3, durationMs, VOLUME/3));
		
		return combined;		
	}

	  private static SoundSource generateSilence(int durationMs) {
		  return new SilenceSoundSource(durationMs);
	}

	/** Generates a tone.
	  @param hz Base frequency (neglecting harmonic) of the tone in cycles per second
	  @param msecs The number of milliseconds to play the tone.
	  @param volume Volume, form 0 (mute) to 100 (max).
	  @param addHarmonic Whether to add an harmonic, one octave up. */
	  public static void generateTone(int hz,int msecs, int volume, boolean addHarmonic)
	    throws LineUnavailableException {
	 
	    float frequency = 44100;
	    byte[] buf;
	    AudioFormat af;
	    if (addHarmonic) {
	      buf = new byte[2];
	      af = new AudioFormat(frequency,8,2,true,false);
	    } else {
	      buf = new byte[1];
	      af = new AudioFormat(frequency,8,1,true,false);
	    }
	    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
	    sdl = AudioSystem.getSourceDataLine(af);
	    sdl.open(af);
	    sdl.start();
	    for(int i=0; i<msecs*frequency/1000; i++){
	      double angle = i/(frequency/hz)*2.0*Math.PI;
	      buf[0]=(byte)(Math.sin(angle)*volume);
	 
	      if(addHarmonic) {
	        double angle2 = (i)/(frequency/hz)*2.0*Math.PI;
	        buf[1]=(byte)(Math.sin(2*angle2)*volume*0.6);
	        sdl.write(buf,0,2);
	      } else {
	        sdl.write(buf,0,1);
	      }
	    }
	    sdl.drain();
	    sdl.stop();
	    sdl.close();
	  }
	 
}
