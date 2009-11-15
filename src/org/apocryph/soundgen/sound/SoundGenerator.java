package org.apocryph.soundgen.sound;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SoundGenerator {
	List<SoundSource> soundSources;
	
	public SoundGenerator() {
		this.soundSources = new LinkedList<SoundSource>();
	}
	
	public void addSoundSource(SoundSource source) {
		this.soundSources.add(source);
	}
	
	public void generateSound(SoundPort port) throws IOException {
		for (Iterator<SoundSource> it = this.soundSources.iterator(); it.hasNext();) {
			SoundSource source = it.next();
			port.writeSoundData(source.generateSound(port.getSampleRate()));
		}
	}
}
