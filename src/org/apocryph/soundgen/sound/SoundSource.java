package org.apocryph.soundgen.sound;

public interface SoundSource {
	int getNumSamples(int sampleRate);
	double[] generateSound(int sampleRate);
}
