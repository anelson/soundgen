package org.apocryph.soundgen.ring;

import org.apocryph.soundgen.sound.SilenceSoundSource;
import org.apocryph.soundgen.sound.SoundGenerator;

public class SilenceRingComponent implements RingComponent {
	int durationMs;
	
	public SilenceRingComponent(int durationMs) {
		this.durationMs = durationMs;
	}

	public void generate(SoundGenerator generator) {
		generator.addSoundSource(new SilenceSoundSource(this.durationMs));
	}

}
