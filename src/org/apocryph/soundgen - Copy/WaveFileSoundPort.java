package org.apocryph.soundgen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.sound.sampled.*;

public class WaveFileSoundPort implements SoundPort {
	int samplingRate;
	int bitsPerSample;
	String fileName;
	ByteArrayOutputStream sampleBuffer;
	
	public WaveFileSoundPort(String fileName, int samplingRate, int bitsPerSample) {
		this.fileName = fileName;
		this.samplingRate = samplingRate;
		this.bitsPerSample = bitsPerSample;
		sampleBuffer = new ByteArrayOutputStream(samplingRate * (bitsPerSample/8)); //Size for 1 second of audio
	}
	
	public void writeFile() throws IOException {
		AudioFormat af = new AudioFormat((float)this.samplingRate, bitsPerSample, 1, true, false);
		File outputFile = new File(this.fileName);
		byte[] sampleData = this.sampleBuffer.toByteArray();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(sampleData);
		AudioInputStream audioStream = new AudioInputStream(inputStream, af, sampleData.length);
		
		AudioSystem.write(audioStream,
				AudioFileFormat.Type.WAVE,
				outputFile);
	}

	public int getSampleRate() {
		return this.samplingRate;
	}

	public void writeSoundData(double[] samples) throws IOException {
		byte[] encodedSamples = encodeSamples(samples);
		sampleBuffer.write(encodedSamples);
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
