////////////////////////////////////////////////////////////////////////
//
// CommandSwitch.java
//
// This file was generated by MapForce 2017sp2.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the MapForce Documentation for further details.
// http://www.altova.com/mapforce
//
////////////////////////////////////////////////////////////////////////

package com.altova.text.flex;
import com.altova.text.ITextNode;
import com.altova.text.TextNodeList;

public class CommandSwitch extends Command {
	public Condition[] conditions;
	private int mode;
	
	public CommandSwitch(String name, Condition[] conditions, int mode) {
		super(name);
		this.conditions = conditions;
		this.mode = mode;
	}
	
	public boolean readText(DocumentReader doc) {
		int nMatches = 0;
		boolean ret = true;
		
		doc.getOutputTree().enterElement(name, ITextNode.Group);
		
		for (int i = 0; i < conditions.length; ++i)
			if (conditions[i].readText(doc))	{
				nMatches++;
				if (mode == 0  /*FirstMatch*/)
					break;
			}
		if (nMatches == 0) // default
			ret = super.readText(doc);
		
		doc.getOutputTree().leaveElement(name);
		
		return ret;
	}
	
	public boolean writeText(DocumentWriter doc) {
		TextNodeList children = doc.getCurrentNode().getChildren().filterByName(getName());
		
		for (int i = 0; i < children.size(); ++i) {
			StringBuffer restStr = new StringBuffer();
			DocumentWriter restDoc = new DocumentWriter(children.getAt(i), restStr, doc.getLineEnd());
			
			for (int cond = 0; cond < conditions.length; ++cond)
				conditions[cond].writeText(restDoc);
			
			super.writeText(restDoc);
			
			doc.appendText(restStr);
		}
		return true;
	}
}
