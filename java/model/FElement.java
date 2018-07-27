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
package de.unima.ki.mamba.semafor.model;

import java.util.Objects;

/**
 * <code>FElement</code> represents a frame element of a <code>Frame</code>.
 * Each <code>FElement</code> consists of an identifying name and an identifying
 * ID. Additionally, the frame element has a content field. For example, the 
 * <code>Sending</code> frame invoked by the sentence,
 * <p>"The student sends the letter to the university"</p>
 * <p>contains three frame elements, that is:</p>
 * <ul>
 * 	<li><code>Sender</code>(name): The student(content)</li>
 * 	<li><code>Theme</code>(name): the letter(content)</li>
 *  <li><code>Goal</code>(name): to the university(content)</li>
 * </ul>
 * @author Kristian Kolthoff
 */
public class FElement implements Comparable<FElement>{

	private String name;
	private String content;
	private int id;
	private int start;
	private int end;
	
	public FElement(String name, String content, int id) {
		this.name = name;
		this.content = content;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 31 * hash + Objects.hash(this.name);
		hash = 31 * hash + Objects.hash(this.content);
		hash = 31 * hash + Objects.hash(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof FElement)) {
			return false;
		}
		FElement that = (FElement) obj;
		return this.name.equals(that.name) &&
			   this.id == that.id &&
			   this.content == that.content;
	}
	
	/**
	 * Compares two <code>FElement</code>s in a way more
	 * less strict mode than the <code>equals</code> method.
	 * For equality, it compares only the frame element identifying 
	 * fields name and id and does not consider the content.
	 * @param obj the reference object with which to compare
	 * @return <code>true</code>, if identifiers of the two
	 * <code>FElement</code>s are equal, <code>false</code>
	 * otherwise
	 */
	public boolean equalsLessStrict(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof FElement)) {
			return false;
		}
		FElement that = (FElement) obj;
		return this.name.equals(that.name) &&
				this.id == that.id;
	}

	@Override
	public String toString() {
		return "FElement : [" + this.id + ", " + 
				this.name + ", " + this.content + ", " 
				+ this.start + ", " + this.end + "]";
	}

	@Override
	public int compareTo(FElement that) {
		return this.name.compareTo(that.name);
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
	
}
