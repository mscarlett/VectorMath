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

public class CalcCURL extends Calc1ParamFunctionEvaluator {

	@Override
	protected CalcObject evaluateObject(final CalcObject input) {
		if(input instanceof CalcVector) {
			return evaluateVector((CalcVector)input);
		}
		if (input instanceof CalcSymbol) {
		    return evaluateSymbol((CalcSymbol)input);
		}
		if (input instanceof CalcFunction) {
		    return evaluateFunction((CalcFunction)input);
		}
		if (input.isNumber()) {
		    throw new CalcWrongParametersException("Curl is only defined for vectors");
		}
		return null;
	}

	@Override
	protected CalcObject evaluateInteger(final CalcInteger input) {
	 // TODO Auto-generated method stub
        return null;
	}

	@Override
	protected CalcObject evaluateDouble(final CalcDouble input) {
	 // TODO Auto-generated method stub
        return null;
	}

	@Override
	protected CalcObject evaluateFraction(final CalcFraction input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CalcObject evaluateSymbol(final CalcSymbol input) { //TODO optimize this process later
	    CalcObject temp = CALC.getDefinedVariable(input);
	    if (temp instanceof CalcVector) {
	        return evaluateVector((CalcVector)temp);
	    }
	    throw new CalcWrongParametersException("Curl is only defined for vectors");
	}

	@Override
	protected CalcObject evaluateFunction(final CalcFunction input) {
		CalcObject temp;
        try {
            temp = input.evaluate();
            if (temp instanceof CalcVector) {
                return evaluateVector((CalcVector)temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new CalcWrongParametersException("Curl is only defined for vectors");
	}
	
	@Override
	protected CalcObject evaluateVector(final CalcVector input) {
		return input.curl();
	}

}
