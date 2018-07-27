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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
/**
 * <code>FrameNetService</code> provides utilities for the creation and extraction of
 * semantic FrameNet annotations.
 * @author Kristian Kolthoff
 */
public class FrameNetService {

	private FrameNetOptions fnOpt;
	private List<String> configFile;
	
	private static final String SEMAFOR_HOME = "SEMAFOR_HOME=";
	private static final String MST_MODE = "MST_MODE=";
	private static final String JAVA_HOME = "JAVA_HOME=";
	private static final String GOLD_TARGET_FILE = "GOLD_TARGET_FILE=";
	private static final String AUTO_TARGET_ID_MODE = "AUTO_TARGET_ID_MODE=";
	private static final String USE_GRAPH_FILES = "USE_GRAPH_FILE=";
	private static final String DECODING_TYPE = "DECODING_TYPE=";
	
	public FrameNetService(FrameNetOptions fnOpt) {
		this.fnOpt = fnOpt;
		this.configFile = new ArrayList<String>();
		if(this.fnOpt.isServerModeOn()) {
			startServer();
		}
	}
	
	public void writeOptionsToConfig() {
		try {
			this.configFile = FileUtils.readLines(new File(FrameNetOptions.ABS_PATH_FILE_CONFIG));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(FrameNetOptions.ABS_PATH_FILE_CONFIG)));
			for(String s : configFile) {
				if(s.startsWith(SEMAFOR_HOME)) {
					s = SEMAFOR_HOME + FrameNetOptions.ABS_PATH_SEMAFOR;
				} else if(s.startsWith(MST_MODE)) {
					if(this.fnOpt.isServerModeOn()) {
						s = MST_MODE + "server";
					} else {
						s = MST_MODE + "noserver";
					}
				} else if(s.startsWith(JAVA_HOME)){
					s = JAVA_HOME + this.fnOpt.getJavaHomePath();
				} else if(s.startsWith(GOLD_TARGET_FILE)) {
					s = GOLD_TARGET_FILE + this.fnOpt.getGoldTargetsPath();
				} else if(s.startsWith(AUTO_TARGET_ID_MODE)) {
					if(this.fnOpt.isAutoTargetIDStrictModeOn()) {
						s = AUTO_TARGET_ID_MODE + "strict";
					} else {
						s = AUTO_TARGET_ID_MODE + "relaxed";
					}
				} else if(s.startsWith(USE_GRAPH_FILES)) {
					if(this.fnOpt.isGraphFilesOn()) {
						s = USE_GRAPH_FILES + "yes";
					} else {
						s = USE_GRAPH_FILES + "no";
					}
				} else if(s.startsWith(DECODING_TYPE)){
					s = DECODING_TYPE + this.fnOpt.getDecodingType();
				}
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			this.configFile.clear();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void runFNSemanticParsing() {
		try{
			Process proc = Runtime.getRuntime().exec(FrameNetOptions.ABS_PATH_DRIVER_SCRIPT + " " 
		 			+ FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_NAME);
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while((line = br.readLine()) != null) {
				System.out.println(line);
			}
			proc.waitFor();
         } catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer() {
//		try{
//			 Process proc = Runtime.getRuntime().exec(FrameNetOptions.ABS_PATH_MST_SERVER_SCRIPT + " " 
//					 			+ FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_NAME);
//			 BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//				String line;
//				while((line = br.readLine()) != null) {
//					System.out.println(line);
//				}
//				proc.waitFor();
//	         } catch (InterruptedException e) {
//	             e.printStackTrace();
//	         } catch (IOException e) {
//				e.printStackTrace();
//			}
	}
	
	public static void main(String[] args) {
		FrameNetService fnService;
		fnService = new FrameNetService(FrameNetOptions.getStandardOpt(""));
		List<String> list = new ArrayList<String>();
		list.add("The student sends his cv to the university");
		list.add("Invite to aptitude test");
		fnService.createAnnotationFile(list);
		fnService.runFNSemanticParsing();
	}
	
	public void createAnnotationFile(List<String> sentences) {
		try {
			final String file = FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_NAME;
			FileUtils.writeLines(new File(file), sentences);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cleanAll() {
		this.cleanTempFiles();
		this.cleanFNDataFiles();
	}
	
	public void cleanTempFiles() { 
		try {
			FileUtils.cleanDirectory(new File(FrameNetOptions.ABS_PATH_DIR_TEMP));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cleanFNDataFiles() {
		try {
			FileUtils.cleanDirectory(new File(FrameNetOptions.ABS_PATH_FNDATA));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FrameNetOptions getFnOpt() {
		return fnOpt;
	}

	public void setFnOpt(FrameNetOptions fnOpt) {
		this.fnOpt = fnOpt;
	}
	
}
