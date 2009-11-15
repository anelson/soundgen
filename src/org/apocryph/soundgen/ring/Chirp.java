package org.apocryph.soundgen.ring;

import java.util.ArrayList;
import java.util.List;

public class Chirp {
	int numTones;
	int toneDurationMs;
	int delayBetweenTonesMs;
	List<ChirpComponent> components = new ArrayList<ChirpComponent>();
	
	public Chirp(int numTones, int toneDurationMs, int delayBetweenTonesMs) {
		this.numTones = numTones;
		this.toneDurationMs = toneDurationMs;
		this.delayBetweenTonesMs = delayBetweenTonesMs;
	}
	
	public void addComponent(ChirpComponent component) {
		components.add(component);
	}
	
	public List<ChirpComponent> getComponents() {
		return this.components;
	}
	
	public int getNumTones() { return this.numTones; }
	public int getToneDurationMs() { return this.toneDurationMs; }
	public int getDelayBetweenTonesMs() { return this.delayBetweenTonesMs; }
}