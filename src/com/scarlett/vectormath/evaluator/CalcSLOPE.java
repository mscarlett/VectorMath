package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcDimensionException;
import com.scarlett.vectormath.exception.CalcWrongParametersException;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcNumber;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcVector;

public class CalcSLOPE implements CalcFunctionEvaluator {

	public CalcObject evaluate(CalcFunction function) {
		if (function.get(0) instanceof CalcVector) {
			CalcVector vector = (CalcVector)function.get(0);
			if (vector.size() == 1) {
				throw new CalcDimensionException("Slope is not defined in one dimension");
			}
			if (function.size() == 1) {
				CalcObject first = vector.get(0);
				CalcObject last = vector.get(vector.size()-1);
				if (first.isNumber() && last.isNumber()) {
					double firstValue = ((CalcNumber)first).doubleValue();
					double lastValue = ((CalcNumber)last).doubleValue();
					double signum = Math.signum(firstValue)*Math.signum(lastValue);
					CalcVector second = new CalcVector(vector.size());
					for (int i = 0; i < vector.size()-1; i++) {
						second.set(i, vector.get(i));
					}
					CalcObject angle = CALC.ANGLE.createFunction(second, vector);
					return CALC.MULTIPLY.createFunction(CALC.TAN.createFunction(angle), new CalcDouble(signum));
				}
			}	else if (function.size() == 2) {
				return CALC.SLOPE.createFunction(CALC.ADD.createFunction(
						function.get(1),CALC.MULTIPLY.createFunction(function.get(0), CALC.NEG_ONE)));
			} else {
				throw new CalcWrongParametersException("Slope cannot have more than two parameters.");
			}
		} else if (function.get(0).isNumber()) {
			CalcVector vector = new CalcVector(function.size());
			for (int i = 0; i < function.size(); i++) {
				vector.set(i, function.get(i));
			}
			return CALC.SLOPE.createFunction(vector);
		}
		return null;
	}
}
