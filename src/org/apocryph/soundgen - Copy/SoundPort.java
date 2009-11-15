package org.apocryph.soundgen;

import java.io.IOException;

public interface SoundPort {
	int getSampleRate();
	void writeSoundData(double[] samples) throws IOException;
}
