package de.unima.ki.mamba.semafor.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unima.ki.mamba.pm.utils.Settings;
import de.unima.ki.mamba.semafor.FrameNetAnnotator;
import de.unima.ki.mamba.semafor.model.FElement;
import de.unima.ki.mamba.semafor.model.Frame;

public class TestExtractor {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		String sentence = "The university sends the documents to the student .";
		FrameNetAnnotator fnAnno = new FrameNetAnnotator(Settings.getJavaHomeDirectory());
		fnAnno.addToCache(sentence);
		Map<String, List<Frame>> frames = fnAnno.fetchFNResultsFromCache();
		for(Map.Entry<String, List<Frame>> sent : frames.entrySet()) {
			for(Frame frame : sent.getValue()) {
				System.out.println("Frame : " + frame.getName() + " " + frame.getTarget());
				for(FElement fe : frame) {
					System.out.println(fe + " " + fe.getStart() + " " + fe.getEnd());
				}
			}
		}
		ContextExtractor ctxExtractor = new ContextExtractorFEWindow(2, true);
		List<Target> targets = new ArrayList<>();
		targets.add(new Target("student", 41, 48));
		List<Context> contexts = ctxExtractor.extract(sentence, targets, frames.get(sentence));
		for(Context ctx : contexts) {
			System.out.println(ctx);
		}
	}
}
