package org.apocryph.soundgen.sound;

import javax.sound.sampled.*;

public class AudioDataLineSoundPort implements SoundPort {
	int samplingRate;
	int bitsPerSample;
	SourceDataLine dataLine;
	
	public AudioDataLineSoundPort(int samplingRate, int bitsPerSample) {
		this.samplingRate = samplingRate;
		this.bitsPerSample = bitsPerSample;
	}
	
	public void startPlaying() throws LineUnavailableException {		
		AudioFormat af = new AudioFormat((float)this.samplingRate, this.bitsPerSample, 1, true, false);
		this.dataLine = AudioSystem.getSourceDataLine(af);
		this.dataLine.open(af);
		this.dataLine.start();
	}
	
	public void finishPlaying() {
		this.dataLine.drain();
		this.dataLine.close();
		this.dataLine = null;
	}

	public int getSampleRate() {
		return this.samplingRate;
	}

	public void writeSoundData(double[] samples) {
		byte[] encodedSamples = encodeSamples(samples);
		this.dataLine.write(encodedSamples,
				0,
				encodedSamples.length);
	}
	
	private byte[] encodeSamples(double[] samples) throws IllegalArgumentException {
		if (this.bitsPerSample == 8) {
			byte[] encodedSamples = new byte[samples.length];
			for (int idx = 0; idx < samples.length; idx++) {
				encodedSamples[idx] = (byte)(samples[idx]*127);
			}
			return encodedSamples;
		} else {
			throw new IllegalArgumentException("Don't know how to encode more than 8 bits per sample");
		}
	}

}
