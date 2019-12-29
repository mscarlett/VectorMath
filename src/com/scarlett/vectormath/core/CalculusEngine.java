package com.scarlett.vectormath.core;

import android.util.Log;

import com.scarlett.vectormath.exception.CalcSyntaxException;
import com.scarlett.vectormath.struct.CalcObject;

/**
 * The main programmable interface of the library. This is the class that
 * the user should import and call methods from.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 */
public final class CalculusEngine {
	//TODO how do I convert really large numbers to scientific notation?
	private String result = new String("No commands executed.");
	private long currentTime, deltaTime;
	/**
	 * Constructor
	 */
	public CalculusEngine() {
		
	}
	
	/**
	 * This is the most important function in CalculusEngine. The user
	 * specifies an input that is sent through the algorithm, producing a
	 * mathematical output that satisfies the grammar used in the command.
	 * @param command
	 * @return The result obtained by parsing and evaluating <b>command</b>.
	 */
    public String execute(String command) {
        this.currentTime = System.nanoTime();
        CALC.operator_notation = false;
        CalcObject parsed = null;
        CalcObject processed = null;
        CalcParser parser = new CalcParser();
        try {
            parsed = parser.parse(command);
            this.result = "Input: " + command + "\n";
            this.result += "Evaluation Queue: " + parsed + "\n";
            this.result += "Processed Queue: " + (processed = CALC.EVALUATE(parsed)) + "\n";
            CALC.toggleOperatorNotation();
            this.result += "Output: " + processed + "\n";
            this.deltaTime = System.nanoTime() - this.currentTime;
            this.result += "Time used: " + this.deltaTime + " nanoseconds\n";
            Log.i("results", this.result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processed + "";
    }
	/**
	 * 
	 * @return the previous result obtained by <b>execute</b>
	 */
	public String getResult() {
		return this.result;
	}
	/**
	 * Set the floating point precision to <b>precision</b> digits
	 * @param precision
	 */
	public static void setPrecision(int precision) {
		CALC.setMathContext(precision);
	}
}
