/**
 * 
 */
package com.scarlett.vectormath.struct;

import java.util.ArrayList;

import android.util.Log;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcConstantEvaluator;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.evaluator.extend.CalcNullEvaluator;
import com.scarlett.vectormath.evaluator.extend.CalcOperatorEvaluator;

/**
 * Represents a entity in a symbolic expression (functions, variables, etc).
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcSymbol implements CalcObject {
	/**
	 * Property constants (mostly used in evaluation step)
	 */
	public static final int NO_PROPERTY 	= 0x0000; 
	public static final int OPERATOR 		= 0x0001;	//x = operator(x)
	public static final int COMMUTATIVE		= 0x0002;	//f(x,y) = f(y,x)
	public static final int CONSTANT		= 0x0004;	//f(x) = c
	public static final int ASSOCIATIVE		= 0x0008;	//f(x,f(y,z)) = f(x,y,z)
	public static final int NO_EVAL			= 0x0010;	//f(x) = f(x)
	public static final int NO_EVAL_FIRST	= 0x0020;	//f(x,y,z) = f(y,z)
	public static final int ONLY_EVAL_FIRST	= 0x0040;	//f(x,y,z,w) = f(x)
	public static final int NUMERIC_FUNCTION	= 0x0080; //f(x) = f(c)
	public static final int UNIPARAM_IDENTITY	= 0x0100; //f(x) = x
	public static final int FAST_EVAL			= 0x0200; //do not check properties before evaluation
	
	private static boolean getDefinedVariable = false;
	
	/**
	 * Name of this symbol
	 */
	private String name;
	/**
	 * Properties of this symbol. Has unique bit for every property.
	 */
	private int properties;
	/**
	 * The function evaluator that is designed to evaluate this particular type of symbol and properties
	 */
	private CalcFunctionEvaluator evaluator;
	
	private ArrayList<CalcSymbol> variables = new ArrayList<CalcSymbol>();
	
	public CalcSymbol(String stringIn, CalcFunctionEvaluator evaluatorIn, int prop) {
		this.name = stringIn;
		this.properties = prop;
		this.evaluator = evaluatorIn;
	}
	
	public CalcSymbol(String stringIn, int prop) {
		this(stringIn, new CalcNullEvaluator(), prop);
	}
	
	public CalcSymbol(String stringIn) {
		this(stringIn, new CalcNullEvaluator(), CalcSymbol.NO_PROPERTY);
	}
	
	public CalcSymbol(StringBuffer stringBufferIn) {
		this(stringBufferIn.toString());
	}
	/**
	 * 
	 * @return a CalcFunction with this symbol as header
	 */
	public CalcFunction createFunction() {
		return new CalcFunction(this);
	}
	/**
	 * @param obj
	 * @return a CalcFunction with this symbol as header and a parameter obj
	 */	
	public CalcFunction createFunction(CalcObject obj) {
		return new CalcFunction(this, obj);
	}
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return a CalcFunction with this symbol as header and two parameters obj1,obj2
	 */
	public CalcFunction createFunction(CalcObject obj1, CalcObject obj2) {
		return new CalcFunction(this, obj1, obj2);
	}
	
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @param obj3
	 * @return a CalcFunction with this symbol as header and three parameters obj1,obj2,obj3
	 */
	public CalcObject createFunction(CalcObject obj1,
			CalcObject obj2, CalcObject obj3) {
		return new CalcFunction(this, obj1, obj2, obj3);
	}
	
	public void addVariable(CalcSymbol var) {
		if (!this.variables.contains(var)) {
			this.variables.add(var);
		}
	}
	
	public CalcSymbol getVariable(int index) {
		return this.variables.get(index);
	}
	
	public void removeVariable(int index) {
		if (index < this.variables.size() && index >= 0) 
			this.variables.remove(index);
	}
	
	public void removeAllVariables() {
		this.variables.removeAll(this.variables);
	}
	
	public int getNumberOfVariables() {
		return this.variables.size();
	}
	
	/**
	 * 
	 * @param symbol
	 * @return the index of <b>symbol</b> in <b>variables</b>. If not found, return -1.
	 */
	public int getVariableIndex(CalcSymbol symbol) {
		for (int ii = 0; ii < getNumberOfVariables(); ii++) {
			if (getVariable(ii).equals(symbol)) return ii;
		}
		return -1;
	}
	
	/**
	 * Finds if a property is one of the properties of this symbol
	 * @param propIn the property to be tested
	 * @return true if propIn is a symbol property, false otherwise
	 */
	public boolean hasProperty(int propIn) {
		return (propIn & this.properties) == propIn;
	}
	
	/**
	 * 
	 * @return the properties associated with this symbol
	 */
	public int getProperties() {
		return this.properties;
	}


	public boolean isNumber() {
		return false;
	}


	public CalcSymbol getHeader() {
		return CALC.SYMBOL;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setEvaluator(CalcFunctionEvaluator eval) {
		this.evaluator = eval;
	}
	
	public CalcFunctionEvaluator getEvaluator() {
		return this.evaluator;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CalcSymbol) {
			return ((CalcSymbol)obj).getName().equals(this.name);
		}
		return false;
	}
	
	@Override
	public Object clone() {
		return new CalcSymbol(this.name, this.evaluator, this.properties);
	}
	
	public CalcObject evaluateFunction(CalcFunction function) {
		if (hasProperty(CalcSymbol.FAST_EVAL)) {
		    return this.evaluator.evaluate(function);
		}
		CalcObject returnVal = function.evaluateParameters();		
		if (returnVal.getHeader() != null && !returnVal.getHeader().equals(function.getHeader())) {
		    return returnVal;
		}
		if (returnVal instanceof CalcFunction) {
		    CalcObject temp = this.evaluator.evaluate((CalcFunction)returnVal); //what happens if temp is null
		    return temp;
		}
        return returnVal;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public CalcObject evaluate() throws Exception {
	    if (evaluator instanceof CalcConstantEvaluator || getDefinedVariable) {
	    	return getDefinedVariable();
	    }
	    return this;
	}

	public CalcObject getDefinedVariable() throws Exception {
		if (this.evaluator instanceof CalcConstantEvaluator) {
			return ((CalcConstantEvaluator)this.evaluator).getValue();
		}
		CalcObject temp = CALC.getDefinedVariable(this);
		if (temp != null) {
		    return temp;
		}
		return this;
	}
		

	public int compareTo(CalcObject obj) {
		if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}
		else if (obj instanceof CalcSymbol) {
			CalcSymbol symbol = (CalcSymbol)obj;
			return symbol.getName().compareTo(this.name);
		}
		return 0;
	}


	public int getHierarchy() {
		return CalcObject.SYMBOL;
	}


	public int getPrecedence() {
		if (this.evaluator instanceof CalcOperatorEvaluator) {
			return ((CalcOperatorEvaluator)this.evaluator).getPrecedence();
		}
		return 9999999; //it's over NINE MILLION!!! OK I need sleep... 
	}
	
	public static void setDefinedVariableMode(final boolean newValue) {
	    CalcSymbol.getDefinedVariable = newValue;
	}
}
