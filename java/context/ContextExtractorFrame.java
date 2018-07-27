package de.unima.ki.mamba.semafor.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.unima.ki.mamba.semafor.model.FElement;
import de.unima.ki.mamba.semafor.model.Frame;

public class ContextExtractorFrame implements ContextExtractor{

	private boolean rmTarget;
	
	public ContextExtractorFrame(boolean rmTarget) {
		this.rmTarget = rmTarget;
	}
	
	@Override
	public List<Context> extract(String sentence, List<Target> targets, List<Frame> frames) {
		if(Objects.isNull(frames)) {
			System.err.println("No Frames invoked for sentence : " + sentence);
			return Collections.emptyList();
		}
		List<Context> context = new ArrayList<>();
		for(Target target : targets) {
			List<String> words = getFrameContainedIn(target, frames);
			Context currContext = new Context(sentence, target.getName(), 
					target.getStart(), target.getEnd(), words);
			context.add(currContext);
		}
		return context;
	}
	
	public List<String> getFrameContainedIn(Target target, List<Frame> frames) {
		List<String> words = new ArrayList<>();
		for(Frame frame : frames) {
			Set<String> s1 = new HashSet<>(Arrays.asList(frame.getTarget().split(" ")));
			Set<String> s2 = new HashSet<>(Arrays.asList(target.getName().split(" ")));
			s1.retainAll(s2);
			if(!s1.isEmpty()) {
				words.addAll(getFElementsContent(frame));
				words.addAll(Arrays.asList(frame.getTarget().split(" ")));
			}
			for(FElement fe : frame) {
				if(((target.getStart() <= fe.getEnd()) && 
						(target.getStart() >= fe.getStart())) ||
						(target.getEnd() >= fe.getStart() &&
						target.getStart() <= fe.getStart()) ||
						(fe.getStart() <= target.getStart() && 
								target.getEnd() <= fe.getEnd())) {
					words.addAll(getFElementsContent(frame));
				}
			}
		}
		return words.stream().distinct()
				.filter(ctx -> {if(!rmTarget) return true; 
               for(String str : target.getName().split(" ")) {
            	   if(str.equals(ctx)) {
            		   return false;
            	   }
               } return true;})
				.collect(Collectors.toList());
	}
	
	public List<String> getFElementsContent(Frame frame) {
		return frame.getfElements().stream()
				.map(fe -> {return fe.getContent();})
				.flatMap(str -> {return Arrays.asList(str.split(" ")).stream();})
				.distinct()
				.collect(Collectors.toList());
	}

}
