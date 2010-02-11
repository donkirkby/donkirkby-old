/*
 * Copyright (c) 2004-2006 Chris Fong and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Chris Fong - initial API and implementation
 */
package com.google.code.donkirkby;


final public class EntryValue implements Comparable<EntryValue> {
	
    private int id = -1;
	private String traditionalChars;
	private String simplifiedChars;
    private String pinyin;
	private String definition;
	private int rank;
	private EntryValue sample;
	
    public int getId()
    {
        return this.id;
    }
    
    /**
     * Makes an exact copy of an EntryValue object
     * @return
     */
    public EntryValue copy() {
    	EntryValue ev = new EntryValue();
    	ev.setId(this.id);
    	ev.setSimplifiedChars(this.simplifiedChars);
    	ev.setTraditionalChars(this.traditionalChars);
    	ev.setPinyin(this.pinyin);
    	ev.setDefinition(this.definition);
    	return ev;
    }
	/**
	 * @return Returns the traditional characters.
	 */
	public String getTraditionalChars() {
		return traditionalChars;
	}
    
	/**
	 * @param characters The traditional characters to set.
	 */
	public void setTraditionalChars(String characters) {
		this.traditionalChars = characters;
	}
    
    /**
     * @return Returns the traditional characters.
     */
    public String getSimplifiedChars() {
        return simplifiedChars;
    }
    
    /**
     * @param characters The traditional characters to set.
     */
    public void setSimplifiedChars(String characters) {
        this.simplifiedChars = characters;
    }
    
    public void setId(int id)
    {
    	this.id = id;
    }
	/**
	 * @return Returns the pinyin.
	 */
	public String getPinyin() {
		return pinyin;
	}
	/**
	 * @param pinyin The pinyin to set.
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof EntryValue))
            return false;
        EntryValue ev = (EntryValue)o;
        return ( (ev.id == this.id) && 
                (simplifiedChars == null ? ev.simplifiedChars == null : simplifiedChars.equals(ev.simplifiedChars)) &&
                (traditionalChars == null ? ev.traditionalChars == null : traditionalChars.equals(ev.traditionalChars)) &&
                (pinyin == null ? ev.pinyin == null : pinyin.equals(ev.pinyin)) &&
                (definition == null ? ev.definition == null : definition.equals(ev.definition))
                );
        
    }
    
    public boolean valueEquals(EntryValue ev)
    {
    	return (simplifiedChars.equals(ev.getSimplifiedChars()) &&
                traditionalChars.equals(ev.getTraditionalChars()) &&
                pinyin.equals(ev.getPinyin()) &&
                definition.equals(ev.getDefinition()));
    }
    
    public int hashCode() 
    {
        int result = 17;
        result = 37 * result + this.id;
        result = 37 * result + (simplifiedChars == null ? 0 : simplifiedChars.hashCode());
        result = 37 * result + (traditionalChars == null ? 0 : traditionalChars.hashCode());
        result = 37 * result + (pinyin == null ? 0 : pinyin.hashCode());
        result = 37 * result + (definition == null ? 0 : definition.hashCode());
        return result;
    }
    
    public String toString()
    {
        return id + " : " + traditionalChars + " " + simplifiedChars + 
        	"[ " + pinyin + " ] " + definition;
    }

	public int compareTo(EntryValue anotherEntryValue) {
		String anotherPinyin = anotherEntryValue.getPinyin();
		String anotherDefinition = anotherEntryValue.getDefinition();
		if (pinyin.compareTo(anotherPinyin) == 0)
			return definition.compareTo(anotherDefinition);
		return this.pinyin.compareTo(anotherPinyin);
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public EntryValue getSample() {
		return sample;
	}

	public void setSample(EntryValue sample) {
		this.sample = sample;
	}
}
