/**
 * 
 */
package com.scarlett.vectormath.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import android.util.Log;
import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.evaluator.extend.CalcOperatorEvaluator;


/**
 * Represents all functions in an expression, including operators.
 * May take on an arbitrary number of CalcObject parameters, hence
 * forming a recursive N-ary tree. The type of function is represented by a
 * header CalcSymbol, which also contains a specific functionEvaluator that
 * evaluates this function.
 * @see CalcSymbol
 * @see javacalculus.evaluator.extend.CalcFunctionEvaluator
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */

public class CalcFunction implements CalcObject, Iterable<CalcObject> {
	
	/**
	 * Property constants
	 */
	public static final int NO_PROPERTY = 0x0000;
	public static final int ASSOCIATIVE_EVALUATED = 0x0001;
	public static final int COMMUTATIVE_EVALUATED = 0x0002;
	
	private CalcSymbol functionHeader;
	protected ArrayList<CalcObject> parameters = new ArrayList<CalcObject>();
	
	private int properties = 0x0000;
	
	/**
	 * Basic constructor with header CalcSymbol
	 * @param calcSymbol header symbol
	 */
	public CalcFunction(CalcSymbol calcSymbol) {
		this.functionHeader = calcSymbol;
	}
	
	/**
	 * Constructor with one parameter
	 * @param calcSymbol header symbol
	 * @param obj1 the parameter
	 */
	public CalcFunction(CalcSymbol calcSymbol, CalcObject obj1) {
		this.functionHeader = calcSymbol;
		add(obj1);
	}
	
	/**
	 * Constructor with two parameters
	 * @param calcSymbol header symbol
	 * @param obj1 first parameter
	 * @param obj2 second parameter
	 */
	public CalcFunction(CalcSymbol calcSymbol, CalcObject obj1, CalcObject obj2) {
		this.functionHeader = calcSymbol;
		add(obj1);
		add(obj2);
	}
	
	/**
	 * Constructor with three parameters
	 * @param calcSymbol
	 * @param obj1
	 * @param obj2
	 * @param obj3
	 */
	public CalcFunction(CalcSymbol calcSymbol, CalcObject obj1,
			CalcObject obj2, CalcObject obj3) {
		this.functionHeader = calcSymbol;
		add(obj1);
		add(obj2);
		add(obj3);
	}
	
	/**
	 * Constructor that creates a function from a certain range of parameters from another function
	 * @param calcSymbol the header
	 * @param function the function to be copied
	 * @param start start index on function
	 * @param end end index on function
	 */
	public CalcFunction(CalcSymbol calcSymbol, CalcFunction function, int start, int end) {
		this.functionHeader = calcSymbol;
		for (int ii = start; ii < end; ii++) {
			add(function.get(ii));
		}
	}
	
	/**
	 * Constructor that creates a function from a given header and parameter arraylist.
	 * @param calcSymbol
	 * @param params
	 */
	public CalcFunction(CalcSymbol calcSymbol, ArrayList<CalcObject> params) {
		this.functionHeader = calcSymbol;
		this.parameters = params;
	}

	/**
	 * 
	 * @param obj add obj to parameter list of this function
	 */
	public void add(CalcObject obj) {
		this.parameters.add(obj);
	}
	
	/**
	 * Adds all of the parameters from function into this function
	 * @param function
	 */
	public void addAll(CalcFunction function) {
		this.parameters.addAll(function.getAll());
	}
	
	public void addVariable(CalcSymbol var) {
		this.functionHeader.addVariable(var);
	}
	
	public CalcSymbol getVariable(int index) {
		return this.functionHeader.getVariable(index);
	}
	
	public void removeVariable(int index) {
		this.functionHeader.removeVariable(index);
	}
	
	public void removeAllVariables() {
		this.functionHeader.removeAllVariables();
	}
	
	public int getNumberOfVariables() {
		return this.functionHeader.getNumberOfVariables();
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
	 * @param index
	 * @return parameter of this function at index
	 */
	public CalcObject get(int index) {
		return this.parameters.get(index);
	}
	
	/**
	 * 
	 * @return all parameters of this function
	 */
	public ArrayList<CalcObject> getAll() {
		return this.parameters;
	}
	
	/**
	 * 
	 * @param index remove parameter of this function at index
	 */
	public void remove(int index) {
		this.parameters.remove(index);
	}
	
	/**
	 * replace the parameter at index with obj
	 * @param index
	 * @param obj
	 */
	public void set(int index, CalcObject obj) {
		this.parameters.set(index, obj);
	}
	
	public void sort() {
		Collections.sort(this.parameters);
	}
	
	/**
	 * Set the header for the function
	 * @param newHeader
	 */
	public void setHeader(CalcSymbol newHeader) {
		this.functionHeader = newHeader;
	}
	
	/**
	 * @return the header symbol of this function
	 * @see CalcSymbol
	 */
	public CalcSymbol getHeader() {
		return this.functionHeader;
	}
	
	/**
	 * @return the properties associated with this function
	 */
	public final int getProperty() {
		return this.properties;
	}
	
	/**
	 * 
	 * @param prop
	 * @return true if prop is a property of this function. False otherwise.
	 */
	public final boolean hasProperty(int prop) {
		return ((prop & this.properties) == prop);
	}
	
	/**
	 * 
	 * @param startIndex
	 * @param comparee
	 * @param compareeStartIndex
	 * @return Whether this function's parameters since startIndex are equal to comparee function's 
	 * parameters since compareeStartIndex on comparee.
	 */
	public boolean equalsFromIndex(int startIndex, CalcFunction comparee, int compareeStartIndex) {
		if ((size() - startIndex) != (comparee.size() - compareeStartIndex)) {
			return false;
		}
		
		for (int ii = startIndex; ii < size(); ii++) {
			if (!get(ii).equals(comparee.get(compareeStartIndex++))) {
			    return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return the number of parameters in this function
	 */
	public final int size() {
		return this.parameters.size();
	}
	
	@Override
	public Object clone() {
		return new CalcFunction(this.functionHeader, this.parameters);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CalcFunction) || !(obj instanceof CalcObject)) {
		    return false;
		}
		if (!((CalcFunction)obj).getHeader().equals(this.functionHeader)) {
		    return false;
		}
		if (((CalcFunction)obj).size() != size()) {
		    return false;
		}
		for (int ii = 0; ii < size(); ii++)	{
			if (!(get(ii).equals(((CalcFunction)obj).get(ii)))) {
			    return false;
			}
		}
		return true;
	}
	
	/**
	 * Convert the parameters of this function to StringBuffer
	 * @return the StringBuffer containing the parameters
	 */
	private StringBuffer parametersToString() {
		StringBuffer returnVal = new StringBuffer();
		
		for (int ii = 0; ii < this.parameters.size(); ii++) {
			returnVal.append(this.parameters.get(ii).toString());
			if (ii != this.parameters.size() - 1) {
				returnVal.append(",");		
			}
		}
		return returnVal;
	}
	
	/**
	 * 
	 * @param prop evaluation properties
	 * @return a copy of the function with appropriate parameters evaluated
	 * @see CalcSymbol
	 */
	public CalcObject evaluateParameters() {
		CalcObject temp;
		CalcFunction result = (CalcFunction)clone();
		boolean evaluated = false;
		
		if (this.functionHeader.hasProperty(CalcSymbol.UNIPARAM_IDENTITY) && size() == 1) {
			return get(0);
		}
		/*if (this.functionHeader.hasProperty(CalcSymbol.NO_SUBSTITUTION)) {
		    //evaluate function header first
		}*/
		if (!this.functionHeader.hasProperty(CalcSymbol.NO_EVAL_FIRST)) {
			temp = CALC.SYM_EVAL(get(0)); //should not happen before differentiation
			if (temp != null) {
				result.set(0, temp);
				evaluated = true;
			}
		}
		if (!this.functionHeader.hasProperty(CalcSymbol.ONLY_EVAL_FIRST)) {
			for (int ii = 1; ii < size(); ii++) {
				temp = CALC.SYM_EVAL(get(ii));
				if (temp != null) {
					result.set(ii, temp);
					evaluated = true;
				}
			}
		}
		//Function is commutative. Order of parameters does not matter. Sort parameters for consistency.
		if (this.functionHeader.hasProperty(CalcSymbol.COMMUTATIVE) && !hasProperty(COMMUTATIVE_EVALUATED)) {
			result.sort();
			//properties |= COMMUTATIVE_EVALUATED;
		}
		//Function is associative. Convert f(x,f(y,z)...) to f(x,y,z....)
		if (this.functionHeader.hasProperty(CalcSymbol.ASSOCIATIVE) && !hasProperty(ASSOCIATIVE_EVALUATED)) {
			result = associativeSimplify();
		}
		if (evaluated) {
		    Log.i("TAG", "Parameters evaluated, return: " + result);
		    return result;
		}
		return null;
	}
	
	/**
	 * 
	 * @return f(x,y, f(z,w..)..) = f(x,y,z,w...)
	 */
	public CalcFunction associativeSimplify() {
		CalcFunction tempFunction = new CalcFunction(this.functionHeader);
		for (int ii = 0; ii < size(); ii++) {
			CalcObject current = get(ii);
			if (current instanceof CalcFunction && this.functionHeader.equals(current.getHeader())) {
				tempFunction.addAll((CalcFunction)current);
			} else {
				tempFunction.add(current);
			}
		}
		//properties |= ASSOCIATIVE_EVALUATED;
		return tempFunction;
	}
	
	public CalcVector gradient() {
	    CalcVector result = new CalcVector(3);
        try {
            result.set(0, CALC.DIFF.createFunction(this, new CalcSymbol("x")).evaluate());
            result.set(1, CALC.DIFF.createFunction(this, new CalcSymbol("y")).evaluate());
            result.set(2, CALC.DIFF.createFunction(this, new CalcSymbol("z")).evaluate());
        } catch (Exception e) {
            result = null;
        }
        return result;
	}
	

	public CalcObject evaluate() throws Exception {
		return this.functionHeader.evaluateFunction(this);
	}
	
	@Override
	public String toString() {
		if (CALC.operator_notation) {
			CalcFunctionEvaluator e = this.functionHeader.getEvaluator();
			if (e instanceof CalcOperatorEvaluator) {
				return ((CalcOperatorEvaluator)e).toOperatorString(this);
			}
		}
		
		StringBuffer out = new StringBuffer();
			
		out.append(this.functionHeader.toString());
		out.append("(");
		out.append(parametersToString());
		out.append(")");
			
		return out.toString();
	}


	public boolean isNumber() {
		return false;
	}


	public int compareTo(CalcObject obj) {
		if (getHierarchy() > obj.getHierarchy()) {
			return 1;
		}
		else if (getHierarchy() < obj.getHierarchy()) {
			return -1;
		}
		return 0;
	}


	public int getHierarchy() {
		return CalcObject.FUNCTION;
	}


	public int getPrecedence() {
		return this.functionHeader.getPrecedence();
	}


	public Iterator<CalcObject> iterator() {
		return this.parameters.iterator();
	}
	
	public boolean isEvaluator() {
	    return CALC.isUpperCase(this.functionHeader.getName());
	}
}
