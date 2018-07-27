/**
 * 
 * SEMAFOR4J
 * 
 * Copyright (C) 2015 Kristian Kolthoff
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unima.ki.mamba.semafor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import de.unima.ki.mamba.semafor.model.Frame;

/**
 * 
 * <br>The <code>FrameNetAnnotator</code> is a wrapper class for extracting FrameNet semantic annotations from 
 * the <a href="https://github.com/Noahs-ARK/semafor-semantic-parser">SEMAFOR semantic parser</a>.
 * Since the semantic annotation of strings is a time-consuming process, this class also provides
 * an annotation cache that can be used to cache sentences that should be annotated. This way, the
 * various sentences can be annotated in one run by the SEMAFOR semantic parser.
 * @author Kristian Kolthoff
 */
public class FrameNetAnnotator {

	private FrameNetXMLParser fnParser;
	private FrameNetService fnService;
	private List<String> sentences;
	
	public FrameNetAnnotator(FrameNetOptions fnOpts) throws ParserConfigurationException, FileNotFoundException {
		this.fnService = new FrameNetService(fnOpts);
		this.fnParser = new FrameNetXMLParser();
		this.sentences = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param javaHomePath the absolute path of your java installation
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 */
	public FrameNetAnnotator(String javaHomePath) throws ParserConfigurationException, FileNotFoundException {
		this.fnService = new FrameNetService(FrameNetOptions.getStandardOpt(javaHomePath));
		this.fnParser = new FrameNetXMLParser();
		this.sentences = new ArrayList<String>();
	}
	
	/**
	 * Adds the sentences to the annotation cache
	 * @param sentences to be added to the annotation cache
	 * @return this
	 */
	public FrameNetAnnotator addToCache(Collection<String> sentences) {
		sentences.addAll(sentences);
		return this;
	}
	 
	
	/**
	 * Adds the sentence to the annotation cache
	 * @param sentence to be added to the annotation cache
	 * @return this
	 */
	public FrameNetAnnotator addToCache(String sentence) {
		this.sentences.add(sentence);
		return this;
	}
	
	/**
	 * Adds the objects string representation to the annotation cache
	 * @param obj to be added to the annotation cache
	 * @return this
	 */
	public FrameNetAnnotator addToCache(Object obj) {
		this.sentences.add(obj.toString());
		return this;
	}
	
	/**
	 * Fetches the annotations from the given sentences <code>Collection</code>
	 * with their corresponding invoked <code>Frame</code>s.
	 * @param sentences to be annotated
	 * @return sentences mapped to their invoked <code>Frame</code>s
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Map<String, List<Frame>> fetchFNResults(Collection<String> sentences) 
			throws ParserConfigurationException, SAXException, IOException {
		Objects.requireNonNull(sentences);
		if(sentences.isEmpty()) {
			return Collections.emptyMap();
		}
		this.sentences.addAll(sentences);
		return this.fetchFNResultsFromCache();
	}
	
	/**
	 * Fetches the annotations from the given sentence
	 * with its corresponding invoked <code>Frame</code>s.
	 * @param sentence to be annotated
	 * @return sentence mapped to its invoked <code>Frame</code>s
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Map<String, List<Frame>> fetchFNResults(String sentence) 
			throws ParserConfigurationException, SAXException, IOException {
		Objects.requireNonNull(sentence);
		if(sentence.isEmpty()) {
			return Collections.emptyMap();
		}
		this.sentences.add(sentence);
		return this.fetchFNResultsFromCache();
	}
	
	/**
	 * Fetches the annotations from the given objects
	 * string representation with its corresponding 
	 * invoked <code>Frame</code>s.
	 * @param obj to be annotated
	 * @return objects string representation mapped to 
	 * its invoked <code>Frame</code>s
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Map<String, List<Frame>> fetchFNResults(Object obj) 
			throws ParserConfigurationException, SAXException, IOException {
		this.sentences.add(obj.toString());
		return this.fetchFNResultsFromCache();
	}
	
	/**
	 * Fetches the annotations from the previously cached sentences 
	 * with their corresponding invoked <code>Frame</code>s and clears 
	 * the annotation cache.
	 * @return cached sentences mapped to their invoked <code>Frame</code>s
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Map<String, List<Frame>> fetchFNResultsFromCache() throws ParserConfigurationException, 
			SAXException, IOException {
		this.fnService.createAnnotationFile(sentences);
		this.fnService.runFNSemanticParsing();
		Map<String, List<Frame>> frameMap = this.fnParser.fetchFNData(FrameNetOptions.ABS_PATH_FNDATA + 
				FrameNetOptions.FN_FILE_OUT_NAME);
		//this.fnService.cleanAll();
		this.sentences.clear();
		return frameMap;
	}
	
	/**
	 * Clears the annotation cache.
	 */
	public FrameNetAnnotator clearCache() {
		this.sentences.clear();
		return this;
	}
	
	public boolean isCached(Collection<String> sentences) {
		return this.sentences.contains(sentences);
	}
	
	public boolean isCached(String sentence) {
		return this.sentences.contains(sentence);
	}
	
	public boolean isCached(Object obj) {
		return this.sentences.contains(obj.toString());
	}
	
}