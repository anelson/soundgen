package org.apocryph.soundgen.sound;

public class SilenceSoundSource implements SoundSource {
	int durationMs;
	
	public SilenceSoundSource(int durationMs) {
		this.durationMs = durationMs;
	}

	public double[] generateSound(int sampleRate) {
		return new double[getNumSamples(sampleRate)];
	}

	public int getNumSamples(int sampleRate) {
		return (int)((double)durationMs / 1000 * sampleRate);
	}

}
