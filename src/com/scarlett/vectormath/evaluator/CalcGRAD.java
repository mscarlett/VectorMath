package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcWrongParametersException;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

public class CalcGRAD extends Calc1ParamFunctionEvaluator {

	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		// Create vector with ∂x, ∂y, and ∂z components
		if(input instanceof CalcFunction) {
			return evaluateFunction((CalcFunction)input);
		}
		if(input instanceof CalcSymbol) {
			return evaluateSymbol((CalcSymbol)input);
		}
		if(input instanceof CalcInteger) {
			return evaluateInteger((CalcInteger)input);
		}
		if(input instanceof CalcDouble) {
			return evaluateDouble((CalcDouble)input);
		}
		if(input instanceof CalcFraction) {
			return evaluateFraction((CalcFraction)input);
		}
		if (input instanceof CalcVector) {
		    throw new CalcWrongParametersException("Gradient is undefined for vectors");
		}
		return null;
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcVector(3);
	}

	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcVector(3);
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return new CalcVector(3);
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		return CalcVector.gradient(input);
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CalcVector.gradient(input); //does not work correctly
	}

}
