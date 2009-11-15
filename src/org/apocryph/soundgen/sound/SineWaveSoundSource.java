package org.apocryph.soundgen.sound;

public class SineWaveSoundSource implements SoundSource {
	double frequencyHz;
	int durationMs;
	double volume;
	
	public SineWaveSoundSource(double frequencyHz,
			int durationMs,
			double volume) {
		this.frequencyHz = frequencyHz;
		this.durationMs = durationMs;
		this.volume = volume;
	}

	@Override
	public double[] generateSound(int sampleRate) {
		double[] samples = new double[getNumSamples(sampleRate)];
		
		for (int sample = 0; sample < samples.length; sample++) {
		     //double angle = sample/(sampleRate/this.frequencyHz)*2.0*Math.PI;
			
			//1/(period) where period is sampleRate/hz.  Scale to radians (2pi radians in circle)
			double angle = sample/(sampleRate/this.frequencyHz)*2.0*Math.PI;
		     samples[sample] = Math.sin(angle) * this.volume;
		}

		return samples;
	}

	public int getNumSamples(int sampleRate) {
		return (int)((double)durationMs / 1000 * sampleRate);
	}

}
