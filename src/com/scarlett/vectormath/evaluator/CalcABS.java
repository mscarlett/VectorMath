/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.evaluator.extend.CalcOperatorEvaluator;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

/**
 * Takes the absolute value of an object. Parsing token is the paired '|' or the 
 * function literal ABS();
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcABS extends Calc1ParamFunctionEvaluator implements CalcOperatorEvaluator {

	/**
	 * 
	 */
	public CalcABS() {}
	
	@Override
	public CalcObject evaluateObject(CalcObject obj) {
		if(obj instanceof CalcVector) {
			return evaluateVector((CalcVector)obj);
		}
		return null;
	}

	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		if (input.isNegative()) return input.negate();
		return input;
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		if (input.isNegative()) input.negate();
		return input;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.ABS.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		if (input.isNegative()) return input.negate();
		else return input;
	}
	
	@Override
	protected CalcObject evaluateVector(CalcVector input) {
		return input.magnitude();
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		return CALC.ABS.createFunction(input);
	}

	
	public int getPrecedence() {
		return 700;
	}


	public String toOperatorString(CalcFunction function) {
		StringBuffer buffer = new StringBuffer();
		char operatorChar = '|';
		CalcObject temp = function.get(0);
		
//    	if (temp.getPrecedence() < getPrecedence()) {
//    		buffer.append('(');
//    	}

    	buffer.append(operatorChar + temp.toString() + operatorChar);

//    	if (temp.getPrecedence() < getPrecedence()) {
//    		buffer.append(')');
//    	}
//    	
    	return buffer.toString();		
	}
}
