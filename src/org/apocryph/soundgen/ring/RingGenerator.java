package org.apocryph.soundgen.ring;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apocryph.soundgen.sound.SoundGenerator;
import org.apocryph.soundgen.sound.SoundSource;

public class RingGenerator {
	List<RingComponent> components = new LinkedList<RingComponent>();
	
	public void addRingComponent(RingComponent component) {
		components.add(component);
	}
	
	public void generate(SoundGenerator generator) {
		for (Iterator<RingComponent> it = this.components.iterator(); it.hasNext();) {
			RingComponent component = it.next();
			component.generate(generator);
		}
	}
}
