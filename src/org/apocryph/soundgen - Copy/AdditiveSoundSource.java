package org.apocryph.soundgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdditiveSoundSource implements SoundSource {
	private List<SoundSource> soundSources = new ArrayList<SoundSource>();
	
	public void addSoundSource(SoundSource source) {
		this.soundSources.add(source);
	}
	
	@Override
	public double[] generateSound(int sampleRate) {
		int numSamples = getNumSamples(sampleRate);
		
		double[] samples = new double[numSamples];
		for (Iterator<SoundSource> it = this.soundSources.iterator(); it.hasNext();) {
			SoundSource src = it.next();
			
			double[] srcSamples = src.generateSound(sampleRate);
			for (int idx = 0; idx < srcSamples.length; idx++) {
				samples[idx] += srcSamples[idx];
			}
		}
		
		return samples;
	}

	@Override
	public int getNumSamples(int sampleRate) {
		int numSamples = 0;
		if (soundSources.size() > 0) {
			for (Iterator<SoundSource> it = this.soundSources.iterator(); it.hasNext();) {
				SoundSource src = it.next();
				
				int srcNumSamples = src.getNumSamples(sampleRate);
				if (numSamples == 0 || srcNumSamples > numSamples) {
					numSamples = srcNumSamples;
				}
			}
		}
		
		return numSamples;
	}

}
