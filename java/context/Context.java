package de.unima.ki.mamba.semafor.context;

import java.util.List;

public class Context {

	private String sentence;
	private String target;
	private int start;
	private int end;
	private List<String> contex;
	
	
	public Context(String sentence, String target, int start, 
			int end, List<String> contex) {
		this.sentence = sentence;
		this.target = target;
		this.start = start;
		this.end = end;
		this.contex = contex;
	}


	public String getSentence() {
		return sentence;
	}


	public void setSentence(String sentence) {
		this.sentence = sentence;
	}


	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getEnd() {
		return end;
	}


	public void setEnd(int end) {
		this.end = end;
	}


	public List<String> getContex() {
		return contex;
	}


	public void setContex(List<String> contex) {
		this.contex = contex;
	}


	@Override
	public String toString() {
		return "Context [sentence=" + sentence + ", target=" + target + ", "
				+ "start=" + start + ", end=" + end
				+ ", contex=" + contex + "]";
	}
	
	
}
