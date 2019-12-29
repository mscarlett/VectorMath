package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.evaluator.extend.Calc2ParamFunctionEvaluator;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

public class CalcANGLE extends Calc2ParamFunctionEvaluator {

	@Override
	protected CalcObject evaluateObject(CalcObject input1, CalcObject input2) {
		if(input1 instanceof CalcVector && input2 instanceof CalcVector) {
			return evaluateVector((CalcVector)input1,(CalcVector)input2);
		}
		if(input1 instanceof CalcInteger && input2 instanceof CalcInteger) {
            return evaluateInteger((CalcInteger)input1,(CalcInteger)input2);
        }
		if(input1 instanceof CalcDouble && input2 instanceof CalcDouble) {
		    return evaluateDouble((CalcDouble)input1,(CalcDouble)input2);
		}
		if(input1 instanceof CalcFraction && input2 instanceof CalcFraction) {
            return evaluateFraction((CalcFraction)input1,(CalcFraction)input2);
        }
		return null;
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input1, CalcInteger input2) {
	    CalcObject[] elements = new CalcObject[2];
        elements[0] = input1;
        elements[1] = input2;
        
        CalcObject[] i = new CalcObject[2];
        i[0] = new CalcInteger(1);
        i[1] = new CalcInteger(0);
        
        return evaluateVector(new CalcVector(elements), new CalcVector(i));
	}

	@Override
	protected CalcObject evaluateDouble(CalcDouble input1, CalcDouble input2) {
	    CalcObject[] elements = new CalcObject[2];
	    elements[0] = input1;
	    elements[1] = input2;
	    
	    CalcObject[] i = new CalcObject[2];
	    i[0] = new CalcInteger(1);
	    i[1] = new CalcInteger(0);
	    
		return evaluateVector(new CalcVector(elements), new CalcVector(i));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input1,
			CalcFraction input2) {
	    CalcObject[] elements = new CalcObject[2];
        elements[0] = input1;
        elements[1] = input2;
        
        CalcObject[] i = new CalcObject[2];
        i[0] = new CalcInteger(1);
        i[1] = new CalcInteger(0);
        
        return evaluateVector(new CalcVector(elements), new CalcVector(i));
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
		return input1.angle(input2);
	}

}
