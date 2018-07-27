package de.unima.ki.mamba.semafor.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.unima.ki.mamba.semafor.model.Frame;

public class ContextExtractorFrameElement implements ContextExtractor {

	private boolean rmTarget;
	
	public ContextExtractorFrameElement(boolean rmTarget) {
		this.rmTarget = rmTarget;
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
			List<String> words = getFrameElementsContainedIn(target, frames);
			Context currContext = new Context(sentence, target.getName(), 
					target.getStart(), target.getEnd(), words);
			context.add(currContext);
		}
		return context;
	}
	
	public List<String> getFrameElementsContainedIn(Target target, List<Frame> frames) {
		return frames.stream().flatMap(frame -> 
				{return frame.getfElements().stream();})
				.filter(fe -> {return ((target.getStart() <= fe.getEnd()) && 
						(target.getStart() >= fe.getStart())) ||
						(target.getEnd() >= fe.getStart() &&
						target.getStart() <= fe.getStart()) ||
						(fe.getStart() <= target.getStart() && 
								target.getEnd() <= fe.getEnd());})
				.map(fe -> {return fe.getContent();})
				.flatMap(str -> {return Arrays.asList(str.split(" ")).stream();})
				.distinct()
				.filter(ctx -> {if(!rmTarget) return true; 
			       for(String str : target.getName().split(" ")) {
			    	   if(str.equals(ctx)) {
			    		   return false;
			    	   }
			       } return true;})
				.collect(Collectors.toList());
		
	}

}
