package org.apocryph.soundgen.ring;

import java.util.Iterator;

import org.apocryph.soundgen.sound.AdditiveSoundSource;
import org.apocryph.soundgen.sound.SilenceSoundSource;
import org.apocryph.soundgen.sound.SineWaveSoundSource;
import org.apocryph.soundgen.sound.SoundGenerator;
import org.apocryph.soundgen.sound.SoundSource;

public class ChirpRingComponent implements RingComponent {
	Chirp chirp;
	
	public ChirpRingComponent(Chirp chirp) {
		this.chirp = chirp;
	}

	public void generate(SoundGenerator generator) {		
		for (int idx = 0; idx < this.chirp.numTones; idx++) {
			generator.addSoundSource(generateChirpTone());
			
			if (idx + 1 < this.chirp.numTones) {
				generator.addSoundSource(generateSilence());
			}
		}
	}
	
	private SoundSource generateChirpTone() {
		AdditiveSoundSource combined = new AdditiveSoundSource();
		for (Iterator<ChirpComponent> it = this.chirp.components.iterator(); it.hasNext();) {
			ChirpComponent component = it.next();
			combined.addSoundSource(new SineWaveSoundSource(component.freq,
					this.chirp.toneDurationMs,
					component.volume));					
		}
		return combined;	
	}
	
	private SoundSource generateSilence() {
		  return new SilenceSoundSource(this.chirp.delayBetweenTonesMs);
	}

}
