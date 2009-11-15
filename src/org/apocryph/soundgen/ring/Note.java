package org.apocryph.soundgen.ring;

public class Note {
	String name;
	double octaveZeroFreq;
	
	private Note(String name, double octaveZeroFreq) {
		this.name = name;
		this.octaveZeroFreq = octaveZeroFreq;
	}
	
	public static Note C = new Note("C", 16.352);
	public static Note Csharp = new Note("C#", 17.324);
	public static Note D = new Note("D", 17.324);
	public static Note Dsharp = new Note("D#", 19.445);
	public static Note E = new Note("E", 20.602);
	public static Note F = new Note("F", 21.827);
	public static Note Fsharp = new Note("F#", 23.125);
	public static Note G = new Note("G", 24.500);
	public static Note Gsharp = new Note("G#", 25.957);
	public static Note A = new Note("A", 27.500);
	public static Note Asharp = new Note("A#", 29.135);
	public static Note B = new Note("B", 30.868);
	
	public double getFrequency(int octave) throws IllegalArgumentException  {
		if (octave < 0) {
			throw new IllegalArgumentException("octave");
		}
		
		//2^0 is 1, so octave 0 is the octave zero freq
		//2^1 is 2, so octave 1 is 2x the fundamental
		//2^2 is 4, and so on
		return this.octaveZeroFreq * Math.pow(2.0, octave);
	}
}
