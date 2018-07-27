package de.unima.ki.mamba.semafor.context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import ch.qos.logback.core.util.FileUtil;
import de.linguatools.disco.CorruptConfigFileException;
import de.unima.ki.mamba.pm.utils.Settings;
import de.unima.ki.mamba.semafor.FrameNetAnnotator;
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetContext {
	 

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, CorruptConfigFileException {
		FrameNetAnnotator fnAnno = new FrameNetAnnotator(Settings.getJavaHomeDirectory());
		Stream<String> stream = Files.lines(Paths.get("src/main/resources/cwi_data/cwishareddataset/traindevset/english/Wikipedia_Train.tsv"));
		List<String> lines = stream.collect(Collectors.toList());
		stream.close();
		final int batchSize = 50;
		Map<String, List<Target>> targetMap = new HashMap<>();
		Map<String, List<Frame>> allFrames = new HashMap<>();
		for(String line : lines) {
			String[] lineColumns = line.split("\\t");
			String sentence = lineColumns[1];
			int start = Integer.valueOf(lineColumns[2]);
			int end = Integer.valueOf(lineColumns[3]);
			String target = lineColumns[4];
			if(targetMap.containsKey(sentence)) {
				List<Target> currTargets = targetMap.get(sentence);
				currTargets.add(new Target(target, start, end));
				targetMap.put(sentence, currTargets);
			} else {
				List<Target> currTargets = new ArrayList<>();
				targetMap.put(sentence, currTargets);
			}
		}
		int currLine = 0;
		for(Map.Entry<String, List<Target>> example : targetMap.entrySet()){
			fnAnno.addToCache(example.getKey());
			currLine+=1;
			if(currLine % batchSize == 0 || currLine == targetMap.size()) {
				System.out.println("Annotating at line " + currLine);
				Map<String, List<Frame>> frames = fnAnno.fetchFNResultsFromCache();
				allFrames.putAll(frames);
				System.out.println("Frame size : " + allFrames.size());
				break;
			}
		}
		ContextExtractor ctxExtractor = new ContextExtractorFEWindow(3, true);
		List<Context> contexts = new ArrayList<>();
		for(Map.Entry<String, List<Target>> example : targetMap.entrySet()){
			String sentence = example.getKey();
			List<Target> targets = example.getValue();
			List<Frame> frames = allFrames.get(sentence);
			List<Context> currContext = ctxExtractor.extract(sentence, targets, frames);
			contexts.addAll(currContext);
		}
		contexts.forEach(ctx -> {System.out.println(ctx);});
		
		FileUtils.writeLines(new File("src/main/resources/cwi_data/output-Wikipedia_Train.tsv"), 
				contexts.stream()
						.map(ctx -> {return ctx.getSentence() + "\t" 
									+ ctx.getTarget() + "\t"
									+ ctx.getStart() + "\t"
									+ ctx.getEnd() + "\t"
									+ ctx.getContex();})
						.collect(Collectors.toList()));
	}
}
