package de.unima.ki.mamba.semafor.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.unima.ki.mamba.pm.nlp.NLPHelper;
import de.unima.ki.mamba.semafor.model.FElement;
import de.unima.ki.mamba.semafor.model.Frame;

public class ContextExtractorFEWindow implements ContextExtractor{

	private int n;
	private boolean rmStopwords;
	
	public ContextExtractorFEWindow(int n, boolean rmStopwords) {
		this.n = n;
		this.rmStopwords = rmStopwords;
	}
	
	@Override
	public List<Context> extract(String sentence, List<Target> targets, 
			List<Frame> frames) {
		if(Objects.isNull(frames)) {
			System.err.println("No Frames invoked for sentence : " + sentence);
			return Collections.emptyList();
		}
		List<Context> context = new ArrayList<>();
		for(Target target : targets) {
			List<String> words = getNfelementsToken(target, frames);
			Context currContext = new Context(sentence, target.getName(), 
					target.getStart(), target.getEnd(), words);
			context.add(currContext);
		}
		return context;
	}
	
	public List<String> getNfelementsToken(Target target, List<Frame> frames) {
		List<String> words = new ArrayList<>();
		List<FElement> fElements = frames.stream()
			 .flatMap(frame -> {return frame.getfElements().stream();})
			 .collect(Collectors.toList());
		List<String> leftWords = fElements.stream().filter(fe -> 
				{return fe.getStart() <= target.getStart() &&
				 fe.getEnd() <= target.getStart();})
			     .map(fe -> {return fe.getContent();})
			     .flatMap(str -> {return Arrays.asList(str.split(" ")).stream();})
			     .filter(str -> {return (!rmStopwords || !NLPHelper.isStopword(str.toLowerCase()));})
			     .distinct()
			     .collect(Collectors.toList());
		List<String> rightWords = fElements.stream().filter(fe -> 
				{return fe.getStart() >= target.getEnd() &&
				 fe.getEnd() >= target.getEnd();})
			     .map(fe -> {return fe.getContent();})
			     .flatMap(str -> {return Arrays.asList(str.split(" ")).stream();})
			     .filter(str -> {return (!rmStopwords || !NLPHelper.isStopword(str.toLowerCase()));})
			     .distinct()
			     .collect(Collectors.toList());
		List<String> tokensLeft = leftWords.subList(0, Integer.valueOf(Math.min(n, leftWords.size())));
		List<String> tokensRight = rightWords.subList(0, Integer.valueOf(Math.min(n, rightWords.size())));
		words.addAll(tokensLeft);
		words.addAll(tokensRight);
		return words;
	}

}
