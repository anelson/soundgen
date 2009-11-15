package org.apocryph.soundgen;

public interface SoundSource {
	int getNumSamples(int sampleRate);
	double[] generateSound(int sampleRate);
}
