package com.scarlett.vectormath;

import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.ui.ButtonPress;
import com.scarlett.vectormath.ui.Keystroke;
import com.scarlett.vectormath.ui.KeystrokeSequence;

/**
 * Activity that allows the user to interact with the calculator by pressing buttons
 * @author Michael Scarlett
 */
//TODO make input multiline
//TODO why don't pi or euler's number work?
//TODO implement one more vector function (rejection? triple product? tensor? slope? inverse? max? min? manhattan distance?)
//SLOPE function: find slope relative to a particular vector (or find slope of an angle?)
//definition of slope:
//for one vector:
//   tan of angle between this vector and other vector that is identical to this vector except last component equals zero
//for two vectors:
//   tan of angle between this vector and other vector
//TODO delete icon
//TODO change back button's color
//TODO: make it possible to define multiple variables at once

//idea for a logo: division sign with arrow coming out of horizontal bar
public class VectorMath extends Activity implements ButtonPress { //TODO have a way to delete previous and make menu
	/*
	 * Declare global variables //TODO reminder: change storage from prefer external
	 */
	private static final HashMap<Button, Keystroke> buttons = new HashMap<Button, Keystroke>();
    private static Vibrator vibrator; //TODO can I use mathml or a similar xml framework to handle input?
    private static LinearLayout l1;
    private static LinearLayout l2;
    private final HashMap<EditText, Input> editTexts = new HashMap<EditText, Input>(); //TODO real time input
    private LinearLayout inputLayout;
    private ScrollView scroll; //TODO what to do when there are too many decimal points?
	/**
	 * Called when the activity is first created.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) { //TODO create basis, span, and kernel operators
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test9);
        /*
         * Initialize global variables
         */
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        l1 = (LinearLayout) findViewById(R.id.L1);
        l2 = (LinearLayout) findViewById(R.id.L2);
        this.inputLayout  = (LinearLayout) findViewById(R.id.DisplayLayout);
        this.scroll = (ScrollView) findViewById(R.id.DisplayScroll);
        /*
         * Initialize first editText and resultView
         */
        final EditText editText = (EditText) findViewById(R.id.entry);
        final InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        final OnTouchListener otl = new InputListener();
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setOnTouchListener(otl);
        this.editTexts.put(editText, new Input(new KeystrokeSequence(),
                (TextView) findViewById(R.id.result)));
        /*
         * Put buttons into HashMap, associating each one with a keystroke
         */
        buttons.put((Button) findViewById(R.id.button0),
                new Keystroke(Keystroke.ZERO, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button1),
                new Keystroke(Keystroke.ONE, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button2),
                new Keystroke(Keystroke.TWO, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button3),
                new Keystroke(Keystroke.THREE, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button4),
                new Keystroke(Keystroke.FOUR, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button5),
                new Keystroke(Keystroke.FIVE, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button6),
                new Keystroke(Keystroke.SIX, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button7),
                new Keystroke(Keystroke.SEVEN, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button8),
                new Keystroke(Keystroke.EIGHT, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.button9),
                new Keystroke(Keystroke.NINE, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_NUMBER));
        buttons.put((Button) findViewById(R.id.buttonPlus),
                new Keystroke(Keystroke.PLUS, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonDecimal),
                new Keystroke(Keystroke.DECIMAL, Keystroke.PROP_FAST_INPUT));
        buttons.put((Button) findViewById(R.id.buttonMinus),
                new Keystroke(Keystroke.MINUS, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonMult),
                new Keystroke(Keystroke.MULTIPLY, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonDivide),
                new Keystroke(Keystroke.DIVIDE, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonLeftParen),
                new Keystroke(Keystroke.LEFTPAREN, Keystroke.PROP_LEFT_PAREN));
        buttons.put((Button) findViewById(R.id.buttonRightParen),
                new Keystroke(Keystroke.RIGHTPAREN, Keystroke.PROP_RIGHT_PAREN));
        buttons.put((Button) findViewById(R.id.buttonCaret),
                new Keystroke(Keystroke.CARET, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonDot),
                new Keystroke(Keystroke.DOT, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonCross),
                new Keystroke(Keystroke.CROSS, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonDiv),
                new Keystroke(Keystroke.DIV, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonGrad),
                new Keystroke(Keystroke.GRAD, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonCurl),
                new Keystroke(Keystroke.CURL, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonAns),
                new Keystroke(Keystroke.ANS, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonAngle),
                new Keystroke(Keystroke.ANGLE, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonMagnitude),
                new Keystroke(Keystroke.MAGNITUDE, Keystroke.PROP_FAST_INPUT));
        buttons.put((Button) findViewById(R.id.buttonX),
                new Keystroke(Keystroke.X, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonY),
                new Keystroke(Keystroke.Y, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonZ),
                new Keystroke(Keystroke.Z, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonComma),
                new Keystroke(Keystroke.COMMA, Keystroke.PROP_FAST_INPUT));
        buttons.put((Button) findViewById(R.id.buttonAssign),
                new Keystroke(Keystroke.ASSIGN, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonProj),
                new Keystroke(Keystroke.PROJ, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonUnit),
                new Keystroke(Keystroke.UNIT, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonBasis),
                new Keystroke(Keystroke.BASIS, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonDistance),
                new Keystroke(Keystroke.DISTANCE, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonSlope),
                new Keystroke(Keystroke.SLOPE, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonSqrt),
                new Keystroke(Keystroke.SQRT, Keystroke.PROP_FAST_INPUT));
        buttons.put((Button) findViewById(R.id.buttonSin),
                new Keystroke(Keystroke.SIN, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonCos),
                new Keystroke(Keystroke.COS, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonTan),
                new Keystroke(Keystroke.TAN, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonASin),
                new Keystroke(Keystroke.ASIN, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonACos),
                new Keystroke(Keystroke.ACOS, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonATan),
                new Keystroke(Keystroke.ATAN, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonA),
                new Keystroke(Keystroke.A, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonB),
                new Keystroke(Keystroke.B, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonC),
                new Keystroke(Keystroke.C, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonPi),
                new Keystroke(Keystroke.PI, Keystroke.PROP_FAST_INPUT));
        buttons.put((Button) findViewById(R.id.buttonE),
                new Keystroke(Keystroke.E, Keystroke.PROP_FAST_INPUT));
        buttons.put((Button) findViewById(R.id.buttonT),
                new Keystroke(Keystroke.T, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonLn),
                new Keystroke(Keystroke.LN, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonInt),
                new Keystroke(Keystroke.INT, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonDiff),
                new Keystroke(Keystroke.DIFF, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonProj2),
                new Keystroke(Keystroke.PROJ, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonUnit2),
                new Keystroke(Keystroke.UNIT, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonBasis2),
                new Keystroke(Keystroke.BASIS, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonDistance2),
                new Keystroke(Keystroke.DISTANCE, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonSlope2),
                new Keystroke(Keystroke.SLOPE, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonDot2),
                new Keystroke(Keystroke.DOT, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonCross2),
                new Keystroke(Keystroke.CROSS, Keystroke.PROP_BINARY_OPERATOR));
        buttons.put((Button) findViewById(R.id.buttonDiv2),
                new Keystroke(Keystroke.DIV, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonGrad2),
                new Keystroke(Keystroke.GRAD, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonCurl2),
                new Keystroke(Keystroke.CURL, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonAngle2),
                new Keystroke(Keystroke.ANGLE, Keystroke.PROP_TWO_PARAM));
        buttons.put((Button) findViewById(R.id.buttonLog),
                new Keystroke(Keystroke.LOG, Keystroke.PROP_ONE_PARAM));
        buttons.put((Button) findViewById(R.id.buttonV1),
                new Keystroke(Keystroke.V1, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV2),
                new Keystroke(Keystroke.V2, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV3),
                new Keystroke(Keystroke.V3, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV4),
                new Keystroke(Keystroke.V4, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV5),
                new Keystroke(Keystroke.V5, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV6),
                new Keystroke(Keystroke.V6, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV7),
                new Keystroke(Keystroke.V7, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV8),
                new Keystroke(Keystroke.V8, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        buttons.put((Button) findViewById(R.id.buttonV9),
                new Keystroke(Keystroke.V9, Keystroke.PROP_FAST_INPUT * Keystroke.PROP_VARIABLE));
        
		for (Entry<Button, Keystroke> entry : buttons.entrySet()) {
			Button b = entry.getKey();
			b.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
						VectorMath.this.buttonClick(v);
					}
					return true;
				}
			});
		}
		
		((Button) findViewById(R.id.buttonBackspace)).setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					VectorMath.this.buttonBackspace(v);
				}
				return true;
			}
		});
		((Button) findViewById(R.id.buttonBackspace2)).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					VectorMath.this.buttonBackspace(v);
				}
				return true;
			}
		});
		
        /*
         * Set on long click listener for backspace
         */
        ((Button) findViewById(R.id.buttonBackspace)).setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(final View v) {
                KeystrokeSequence sequence = VectorMath.this.editTexts.get(getCurrentFocus()).sequence;
                sequence.clear();
                return false;
            }
        });
        ((Button) findViewById(R.id.buttonBackspace2)).setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(final View v) {
                KeystrokeSequence sequence = VectorMath.this.editTexts.get(getCurrentFocus()).sequence;
                sequence.clear();
                return false;
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_history: clearLayout();
                return true;
            case R.id.clear_definitions: //get a list of user definitions and allow user to clear definitions
            	CALC.clearDefinitions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void buttonClick(final View src) { //TODO how do I know which edittext is focused?
        EditText editText = (EditText)getCurrentFocus();
        Input input = this.editTexts.get(editText);
        TextView resultView = input.textView;
        KeystrokeSequence sequence = input.sequence;
        sequence.add(buttons.get(src));
        vibrator.vibrate(25);
        editText.setText(sequence.toString());
        editText.setSelection(sequence.getIndex());
        String solution = sequence.solve(); //TODO if sequence does not have enough ending parenthesis, then program adds them
        //TODO if a variable define operation is in input, then don't do definition
        if (!solution.toString().equals("null")) { //TODO this should display error message
            resultView.setText(solution);
            resultView.setTextColor(Color.parseColor("#999999"));
        }
    } //TODO can I make the numbers appear immediately?
  //I think the solution is to use ontouch
    
    public void buttonBackspace(final View src) {
        EditText editText = (EditText)getCurrentFocus();
        Input input = this.editTexts.get(editText);
        TextView resultView = input.textView;
        KeystrokeSequence sequence = input.sequence;
        sequence.backspace();
        vibrator.vibrate(30);
        editText.setText(sequence.toString());
        editText.setSelection(sequence.getIndex());
        String solution = sequence.solve(); //TODO if sequence does not have enough ending parenthesis, then program adds them
        if (!solution.toString().equals("null")) { //TODO this should display error message
            resultView.setText(solution);
            resultView.setTextColor(Color.parseColor("#999999"));
        }
    }
    
    /*
     * List of bad input:
     */
    public void buttonSolve(final View src) { //TODO prevent calculator from adding new input when input has already been solved
    	CALC.defineMode = true;
        EditText editText = (EditText)getCurrentFocus();
        Input input = this.editTexts.get(editText);
        TextView resultView = input.textView;
        KeystrokeSequence sequence = input.sequence;
        String solution = sequence.solve(); //TODO if sequence does not have enough ending parenthesis, then program adds them
        CALC.defineMode = false;
        if (!solution.toString().equals("null")) { //TODO this should display error message
            resultView.setText(solution);
            resultView.setTextColor(Color.parseColor("#666666"));
            vibrator.vibrate(35);
            if (!input.processed) {
                addNewInput();
                input.processed = true;
            }
        } else {
        	resultView.setText("ERROR");
        }
    }
    
    public void buttonMore(final View src) {
        switch (src.getId()) {
            case R.id.buttonMore: l1.setVisibility(View.GONE); l2.setVisibility(View.VISIBLE); break;
            case R.id.buttonBack: l2.setVisibility(View.GONE); l1.setVisibility(View.VISIBLE); break;
        }
        vibrator.vibrate(35);
    }
    
    boolean hyp = false;
    
    public void buttonHyp(final View src) {
    	if (hyp = !hyp) {
    		Button sin = ((Button) findViewById(R.id.buttonSin));
    		buttons.put(sin,new Keystroke(Keystroke.SINH, Keystroke.PROP_ONE_PARAM));
    		sin.setText("sinh");
    		Button cos = ((Button) findViewById(R.id.buttonCos));
    		buttons.put(cos,new Keystroke(Keystroke.COSH, Keystroke.PROP_ONE_PARAM));
    		cos.setText("cosh");
    		Button tan = ((Button) findViewById(R.id.buttonTan));
    		buttons.put(tan,new Keystroke(Keystroke.TANH, Keystroke.PROP_ONE_PARAM));
    		tan.setText("tanh");
    		Button asin = ((Button) findViewById(R.id.buttonASin));
    		buttons.put(asin,new Keystroke(Keystroke.ASINH, Keystroke.PROP_ONE_PARAM));
    		asin.setText("asinh");
    		Button acos = ((Button) findViewById(R.id.buttonACos));
    		buttons.put(acos,new Keystroke(Keystroke.ACOSH, Keystroke.PROP_ONE_PARAM));
    		acos.setText("acosh");
    		Button atan = ((Button) findViewById(R.id.buttonATan));
    		buttons.put(atan,new Keystroke(Keystroke.ATANH, Keystroke.PROP_ONE_PARAM));
    		atan.setText("atanh");
    		Button hyp = (Button) findViewById(R.id.buttonHyp);
    		hyp.setBackgroundResource(R.drawable.custombuttonorange);
    	} else {
    		Button sin = ((Button) findViewById(R.id.buttonSin));
    		buttons.put(sin,new Keystroke(Keystroke.SIN, Keystroke.PROP_ONE_PARAM));
    		sin.setText("sin");
    		Button cos = ((Button) findViewById(R.id.buttonCos));
    		buttons.put(cos,new Keystroke(Keystroke.COS, Keystroke.PROP_ONE_PARAM));
    		cos.setText("cos");
    		Button tan = ((Button) findViewById(R.id.buttonTan));
    		buttons.put(tan,new Keystroke(Keystroke.TAN, Keystroke.PROP_ONE_PARAM));
    		tan.setText("tan");
    		Button asin = ((Button) findViewById(R.id.buttonASin));
    		buttons.put(asin,new Keystroke(Keystroke.ASIN, Keystroke.PROP_ONE_PARAM));
    		asin.setText("asin");
    		Button acos = ((Button) findViewById(R.id.buttonACos));
    		buttons.put(acos,new Keystroke(Keystroke.ACOS, Keystroke.PROP_ONE_PARAM));
    		acos.setText("acos");
    		Button atan = ((Button) findViewById(R.id.buttonATan));
    		buttons.put(atan,new Keystroke(Keystroke.ATAN, Keystroke.PROP_ONE_PARAM));
    		atan.setText("atan");
    		Button hyp = (Button) findViewById(R.id.buttonHyp);
    		hyp.setBackgroundResource(R.drawable.custombuttoncyan);
    	}
    }
    
    public void buttonDeg(final View src) {
    	if (CALC.useDegrees = !CALC.useDegrees) {
    		Button deg = (Button) findViewById(R.id.buttonDeg);
    		deg.setBackgroundResource(R.drawable.custombuttonorange);
    	} else {
    		Button deg = (Button) findViewById(R.id.buttonDeg);
    		deg.setBackgroundResource(R.drawable.custombuttoncyan);
    	}
    }
    
    public void doNothing(final View src) {
    	
    }
    
    private void addNewInput() { //also scroll automatically
        View ruler = new View(this);
        ruler.setBackgroundColor(Color.parseColor("#E9E9E9"));
        this.inputLayout.addView(ruler, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        /*
         * Create new EditText
         */
        final EditText et = new EditText(this);
        et.setTextSize(20);
        et.setTextColor(Color.parseColor("#111111"));
        et.setBackgroundColor(Color.parseColor("#FFFFFF"));
        et.setPadding(3,5,10,3);
        et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        final InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        final OnTouchListener otl = new InputListener();
        et.setOnTouchListener(otl);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        /*
         * Create new TextView
         */
        final TextView tv = new TextView(this);
        tv.setTextSize(20);
        tv.setTextColor(Color.parseColor("#999999"));
        tv.setPadding(3,5,10,3);
        tv.setGravity(Gravity.RIGHT);
        this.inputLayout.addView(et);
        this.inputLayout.addView(tv);
        this.editTexts.put(et, new Input(new KeystrokeSequence(), tv));
        this.scroll.post(new Runnable() {
            public final void run() {
                VectorMath.this.scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    } //TODO should I just create a new sequence?
    
    private void clearLayout() {
        this.inputLayout.removeAllViews();
        addNewInput();
    }
    
    private class InputListener implements OnTouchListener {
        
        public boolean onTouch(View v, MotionEvent event) {
            v.requestFocus();
            Layout textLayout = ((TextView) v).getLayout();
            EditText editText = (EditText) v;
            int x = (int)event.getX();
            int y = (int)event.getY();
            int line = textLayout.getLineForVertical(y);
            int characterOffset = textLayout.getOffsetForHorizontal(line, x);
            editText.setSelection(VectorMath.this.editTexts.get(v).sequence.putCursor(characterOffset));
            return true;
        }
    }
    
    private static class Input {
        private KeystrokeSequence sequence;
        private TextView textView;
        private boolean processed;
        private Input(final KeystrokeSequence ks, final TextView tv) {
            this.sequence = ks;
            this.textView = tv;
            this.processed = false;
        }
    }
}
