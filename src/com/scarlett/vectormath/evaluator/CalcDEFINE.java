/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcOperatorEvaluator;
import com.scarlett.vectormath.exception.CalcWrongParametersException;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;

/**
 * Handles user defined functions (e.g. f(x)=x^2). Stores defined functions
 * in CALC.defined.
 * @see CALC
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcDEFINE implements CalcOperatorEvaluator {

	/**
	 * 
	 */
	public CalcDEFINE() {} //TODO when variable is redefined, the old variable must be deleted

	/* (non-Javadoc)
	 * @see javacalculus.evaluator.CalcFunctionEvaluator#evaluate(javacalculus.struct.CalcFunction)
	 */
	public CalcObject evaluate(CalcFunction input) { //TODO prevent vectors from being assigned nonvector values?
		if (input.size() == 2) { //TODO throw exception if first input is function but not lowercase
			if (input.get(0) instanceof CalcSymbol) {
			    CalcSymbol symbol = (CalcSymbol)input.get(0); //TODO handle input such as 5x+8=4y-2 by solving for x
			    if (symbol.equals(input.get(1))) {
		            CALC.removeDefinedVariable(symbol);
		        } else {
		            CALC.setDefinedVariable(symbol, input.get(1));
			    }
				return input;
			}
			if (input.get(0) instanceof CalcFunction) { //TODO allow input of functions
				CalcFunction function = (CalcFunction)input.get(0);
				if (function.isEvaluator()) {
				    throw new CalcWrongParametersException("DEFINE -> f(x,y...) must take only symbols");
				}
				for (int ii = 0; ii < function.size(); ii++) {
					CalcObject currentTerm = function.get(ii);
					if (!(currentTerm instanceof CalcSymbol)) {
						throw new CalcWrongParametersException("DEFINE -> f(x,y...) must take only symbols");
					}
                    function.addVariable((CalcSymbol)currentTerm);
				}
				CALC.setDefinedVariable(function.getHeader(), input.get(1));
				return input;
			}
            throw new CalcWrongParametersException("DEFINE -> first parameter must be a symbol");
		}
        throw new CalcWrongParametersException("DEFINE -> wrong number of parameters");
	}
	
    public String toOperatorString(CalcFunction function) {
	    CalcObject symbol = function.get(0);
	    CalcObject defined = function.get(1);
	    StringBuffer buffer = new StringBuffer();
	    char operatorChar = '=';
	    buffer.append(symbol);
	    buffer.append(operatorChar);
	    buffer.append(defined);
	    return buffer.toString();
	}

    public int getPrecedence() {
        return 0;
    }
}
