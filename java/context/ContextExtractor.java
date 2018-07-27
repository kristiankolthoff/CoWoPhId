package de.unima.ki.mamba.semafor.context;

import java.util.List;

import de.unima.ki.mamba.semafor.model.Frame;

public interface ContextExtractor {

	public List<Context> extract(String sentence, List<Target> targets, List<Frame> frames);
}
