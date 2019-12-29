package com.scarlett.vectormath.struct;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.exception.CalcDimensionException;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcVector implements CalcObject { //TODO create curl, projection, cross product, angle, div, grad, and del operators and determine if it is feasible to create new implementations of existing function evaluators
	
	private CalcObject[] elements;
	private static final char VECTOR_OPEN = '{';
	private static final char VECTOR_CLOSE = '}';
	private static final char VECTOR_DELIM = ',';
	
	/**
	 * Constructor for a zero vector of length len
	 * @param len
	 */
	public CalcVector(final int len) {
		this.elements = new CalcObject[len];
		
		for (int ii = 0; ii < len; ii++) {
			this.elements[ii] = CALC.ZERO;
		}
	}
	
	/**
	 * Construct a vector from a given array of elements
	 * @param elements
	 */
	public CalcVector(final CalcObject[] elements) {
		this.elements = elements;
	}
	
	/**
	 * Construct a vector from a given array of elements with one index excluded
	 * @param elements
	 * @param exclude
	 */
	public CalcVector(final CalcObject[] elements, final int exclude) {
		this.elements = new CalcObject[elements.length - 1];
		int index = 0;
		
		for (int ii = 0; ii < elements.length; ii++) {
			if (ii == exclude) {
				continue;
			}
			this.elements[index++] = elements[ii];
		}
	}
	
	public int size() {
		return this.elements.length;
	}
	
	public void set(final int index, final CalcObject obj) {
		if (index >= this.elements.length || index < 0) {
			throw new CalcDimensionException("Vector does not contain index " + index);
		}
		this.elements[index] = obj;
	}
	
	public CalcObject get(final int index) {
		if (index >= this.elements.length || index < 0) {
			throw new CalcDimensionException("Vector does not contain index " + index);
		}
		return this.elements[index];
	}
	
	public CalcObject[] getAll() {
		return this.elements;
	}
	
	/**
	 * Returns the sum of this vector and another vector
	 * @param input
	 */
	public CalcVector add(final CalcVector input) {
		if (input.size() != size()) {
			throw new CalcDimensionException("Different dimensions in vector addition");
		}
		CalcObject[] inputElements = input.getAll();
		try {
		    for (int ii = 0; ii < this.elements.length; ii++) {
		        this.elements[ii] = CALC.ADD.createFunction(this.elements[ii], inputElements[ii]).evaluate();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * Returns the scalar multiple of this vector
	 * @param input
	 */
	public CalcVector multiply(final CalcObject input) {
		CalcVector result = new CalcVector(size());
		try {
		    for (int ii = 0; ii < this.elements.length; ii++) {
		        result.set(ii, CALC.MULTIPLY.createFunction(input, this.elements[ii]).evaluate());
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Returns the dot product between this vector and another vector
	 * @param input
	 */
	public CalcObject dot(final CalcVector input) {	//computes the dot product between two vectors
		if (size() != input.size()) {
			throw new CalcDimensionException("Mismatched vector sizes in dot product");
		}
		
		CalcFunction returnVal = CALC.ADD.createFunction();
		
		CalcObject[] inputElements = input.getAll();
		try {
		    for (int ii = 0; ii < this.elements.length; ii++) {
		        returnVal.add(CALC.MULTIPLY.createFunction(this.elements[ii], inputElements[ii]).evaluate());
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return returnVal;
	}
	
	/**
	 * Returns the cross product between this vector and another vector
	 * @param input
	 */
	public CalcVector cross(final CalcVector input) {
		if(size() != 3 || input.size() != 3) {
			throw new CalcDimensionException("Both vectors must be three dimensional");
		}
		CalcVector result = new CalcVector(3);
		CalcObject a1, a2, a3, b1, b2, b3;
		a1 = this.elements[0];
		a2 = this.elements[1];
		a3 = this.elements[2];
		b1 = input.elements[0];
		b2 = input.elements[1];
		b3 = input.elements[2];
		try {
		    result.elements[0] = CALC.ADD.createFunction(CALC.MULTIPLY.createFunction(a2,b3),
				CALC.MULTIPLY.createFunction(CALC.NEG_ONE,a3,b2)).evaluate(); //c1 = a2*b3-a3*b2
		    result.elements[1] = CALC.ADD.createFunction(CALC.MULTIPLY.createFunction(a3,b1),
				CALC.MULTIPLY.createFunction(CALC.NEG_ONE,a1,b3)).evaluate(); // c2 = a3*b1-a1*b3
		    result.elements[2] = CALC.ADD.createFunction(CALC.MULTIPLY.createFunction(a1,b2),
				CALC.MULTIPLY.createFunction(CALC.NEG_ONE,a2,b1)).evaluate(); // c3 = a1*b2-a2*b1
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Returns the curl of this vector
	 * @param input
	 */
	public CalcVector curl() {
		if(size() != 3) {
			throw new CalcDimensionException("Vector must be three dimensional");
		}
		CalcVector result = new CalcVector(3);
		CalcObject a1 = this.elements[0];
		CalcObject a2 = this.elements[1];
		CalcObject a3 = this.elements[2];
		CalcSymbol x = new CalcSymbol("x");
		CalcSymbol y = new CalcSymbol("y");
		CalcSymbol z = new CalcSymbol("z");
		try {
		    result.elements[0] = CALC.ADD.createFunction(CALC.DIFF.createFunction(a3, y),
				CALC.MULTIPLY.createFunction(CALC.NEG_ONE,CALC.DIFF.createFunction(a2, z))).evaluate(); //c1 = dy*b3-dz*b2
		    result.elements[1] = CALC.ADD.createFunction(CALC.DIFF.createFunction(a1, z),
				CALC.MULTIPLY.createFunction(CALC.NEG_ONE,CALC.DIFF.createFunction(a3, x))).evaluate(); // c2 = dz*b1-dx*b3
		    result.elements[2] = CALC.ADD.createFunction(CALC.DIFF.createFunction(a2, x),
				CALC.MULTIPLY.createFunction(CALC.NEG_ONE,CALC.DIFF.createFunction(a1, y))).evaluate(); // c3 = dx*b2-dy*b1
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Returns the projection of this vector onto another vector
	 * @param input
	 */
	public CalcVector proj(CalcVector input) {
		if (size() != input.size()) {
			throw new CalcDimensionException("Mismatched vector sizes in dot product");
		}
		return input.multiply(CALC.MULTIPLY.createFunction(dot(input),
		        CALC.POWER.createFunction(input.dot(input), CALC.NEG_ONE))); // (a*b)/(b*b)b
	}

	/**
	 * Returns the magnitude of this vector
	 * @throws Exception 
	 */
	public CalcObject magnitude() {
		CalcObject result = CALC.ZERO;		
		CalcObject squared = CALC.TWO;
		for (int ii=0; ii < size() ; ii++) {
			result = CALC.ADD.createFunction(result,
			        CALC.POWER.createFunction(this.elements[ii], squared));
		}
		try {
            return CALC.POWER.createFunction(result, CALC.D_HALF).evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * Returns the angle of this vector with another vector
	 * @param input
	 */
	public CalcObject angle(CalcVector input) {
		CalcObject cos = CALC.MULTIPLY.createFunction(dot(input),
				CALC.POWER.createFunction(CALC.MULTIPLY.createFunction(magnitude(),input.magnitude()), CALC.NEG_ONE));
		try {
            return CALC.ACOS.createFunction(cos).evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * Returns the divergence of this vector
	 */
	public CalcObject div() {
		if(size() != 3) { //TODO write method that allows 5D, 4D, 2D, and 1D divergence
			throw new CalcDimensionException("Vector must be three dimensional");
		}
		CalcSymbol x = new CalcSymbol("x");
		CalcSymbol y = new CalcSymbol("y");
		CalcSymbol z = new CalcSymbol("z");
		CalcObject result;
        try {
            result = CALC.ADD.createFunction(CALC.DIFF.createFunction(this.elements[0],x),
            		CALC.DIFF.createFunction(this.elements[1],y),CALC.DIFF.createFunction(this.elements[2],z)).evaluate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * Returns the unit vector
	 */
	public CalcVector unit() {
		CalcVector result = this.multiply(CALC.MULTIPLY.createFunction(CALC.ONE, CALC.POWER.createFunction(magnitude(), CALC.NEG_ONE)));
		try {
            result.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result;
	}//TODO create operator for this function
	
	/**
	 * Finds a Gram-Schmidt orthonormal basis for a linearly independent set of vectors
	 */
	public CalcVector orthogonalize(CalcVector input) {
		return null; // TODO write this function
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


	public CalcObject evaluate() throws Exception {
		for (int ii = 0; ii < this.elements.length; ii++) {	//evaluate each element first
			this.elements[ii] = this.elements[ii].evaluate();
		}
		return this;
	}


	public CalcSymbol getHeader() {
		return CALC.VECTOR;
	}


	public int getHierarchy() {
		return CalcObject.VECTOR;
	}


	public int getPrecedence() {
		return 0;
	}


	public boolean isNumber() {
		return false;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(VECTOR_OPEN);
		
		for (int ii = 0; ii < this.elements.length; ii++) {
			if (ii > 0) buffer.append(VECTOR_DELIM);
			
			buffer.append(this.elements[ii].toString());
		}
		
		buffer.append(VECTOR_CLOSE);
		
		return buffer.toString();
	}

    public static CalcVector gradient(final CalcObject input) {
        CalcVector result = new CalcVector(3);
        result.set(0, CALC.DIFF.createFunction(input, new CalcSymbol("x")));
        result.set(1, CALC.DIFF.createFunction(input, new CalcSymbol("y")));
        result.set(2, CALC.DIFF.createFunction(input, new CalcSymbol("z")));
        try {
            result.evaluate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
