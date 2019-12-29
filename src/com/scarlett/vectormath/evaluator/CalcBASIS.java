package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcWrongParametersException;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcVector;


public class CalcBASIS implements CalcFunctionEvaluator {

	// u1 = v1
	// u2 = v2-proj(v2,u1)
	// u3 = v3-proj(v3,u1)-proj(v3,u2)
	// u4 = v4-proj(v4,u1)-proj(v4,u2)-proj(v4,u3)
	// ...
	// e1 = unit(u1)
	// e2 = unit(u2)
	// e3 = unit(u3)
	// ...
	@Override
	public CalcObject evaluate(CalcFunction input) {
		CalcFunction returnVal = new CalcFunction(CALC.SET);
		if (!(input.get(0) instanceof CalcVector)) {
			throw new CalcWrongParametersException("Basis is only defined for vectors.");
		}
		CalcVector vector = (CalcVector)input.get(0);
		final int dimension = vector.size();
		returnVal.add(vector.unit());
		for (int i = 1; i < input.size(); i++) {
			if (!(input.get(i) instanceof CalcVector)) {
				throw new CalcWrongParametersException("Basis is only defined for vectors.");
			}
			vector = (CalcVector)input.get(0);
			CalcVector sum = new CalcVector(dimension);
			for (int j = 0; j < i; j++) {
				CalcObject summand = CALC.SYM_EVAL(CALC.MULTIPLY.createFunction(CALC.PROJ.createFunction(vector,input.get(j)),CALC.NEG_ONE));
				if (!(summand instanceof CalcVector)) {
					throw new RuntimeException("Error during basis computation.");
				}
				sum.add((CalcVector)summand);
			}
			vector = vector.add(sum);
			returnVal.add(vector.unit());
		}
		return returnVal;
	}

}
