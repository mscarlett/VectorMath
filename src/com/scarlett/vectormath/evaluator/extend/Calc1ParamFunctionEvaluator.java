package com.scarlett.vectormath.evaluator.extend;

import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcMatrix;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

/**
 * Abstract definition of a function evaluator that takes in one parameter.
 * 
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public abstract class Calc1ParamFunctionEvaluator implements CalcFunctionEvaluator {


	public CalcObject evaluate(CalcFunction input) {
		if (input.size() == 1) {
			CalcObject parameter = input.get(0);
			//parameter = CALC.SYM_EVAL(parameter);
			CalcObject returnVal = evaluateObject(parameter);
			if (returnVal != null) return returnVal;
			
			else if (parameter instanceof CalcInteger) return evaluateInteger((CalcInteger)parameter);
			else if (parameter instanceof CalcDouble) return evaluateDouble((CalcDouble)parameter);
			else if (parameter instanceof CalcFraction) return evaluateFraction((CalcFraction)parameter);
			else if (parameter instanceof CalcSymbol) return evaluateSymbol((CalcSymbol)parameter);
			else if (parameter instanceof CalcFunction) return evaluateFunction((CalcFunction)parameter);
			
			else return input;
		}
		return null; //function has more than 1 parameter. This evaluator does not apply so return null
	}
	
	protected abstract CalcObject evaluateObject(CalcObject input);
	protected abstract CalcObject evaluateInteger(CalcInteger input);
	protected abstract CalcObject evaluateDouble(CalcDouble input);
	protected abstract CalcObject evaluateFraction(CalcFraction input);
	protected abstract CalcObject evaluateSymbol(CalcSymbol input);
	protected abstract CalcObject evaluateFunction(CalcFunction input);
	//will maybe make these abstract later. Right now we need a default implementation.
	protected CalcObject evaluateVector(CalcVector input) {return null;}
	protected CalcObject evaluateMatrix(CalcMatrix input) {return null;}
}
