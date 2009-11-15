package org.apocryph.soundgen.sound;

import java.io.IOException;

public interface SoundPort {
	int getSampleRate();
	void writeSoundData(double[] samples) throws IOException;
}
