package com.scarlett.vectormath.evaluator;

import android.util.Log;

import com.scarlett.vectormath.evaluator.extend.CalcNParamFunctionEvaluator;
import com.scarlett.vectormath.evaluator.extend.CalcOperatorEvaluator;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

public class CalcCROSS extends CalcNParamFunctionEvaluator implements CalcOperatorEvaluator {

	@Override //TODO why can't I do cross with 3 params
	protected CalcObject evaluateObject(CalcObject input1, CalcObject input2) {
		if (input1 instanceof CalcVector && input2 instanceof CalcVector) {
		    return evaluateVector((CalcVector)input1,(CalcVector)input2);
		}
		return null;
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input1, CalcInteger input2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CalcObject evaluateDouble(CalcDouble input1, CalcDouble input2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input1,
			CalcFraction input2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input1, CalcSymbol input2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input1,
			CalcFunction input2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CalcObject evaluateFunctionAndInteger(CalcFunction input1,
			CalcInteger input2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected CalcObject evaluateVector(CalcVector input1, CalcVector input2) {
		return input1.cross(input2);
	}

	public String toOperatorString(CalcFunction function) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 900;
	}

}
