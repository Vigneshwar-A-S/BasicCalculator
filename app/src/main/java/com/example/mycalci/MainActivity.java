package com.example.mycalci;


import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;

    // Variables to hold the operands and type of calculations
    private Double operand1 = null;    //operand 1 is nothing but a result EditText
    private Double operand2 = null;    //operand 2 is nothing but a value EditText
    private String pendingOperation = "=";   //stores the operands equivalent, in order to prevent collapsed operations.

    private static final String STATEofPENDING_OPERATION = "PendingOperation";
    private static final String STATEofOPERAND1 = "Operand1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


        result = ( EditText ) findViewById(R.id.result);
        newNumber = ( EditText ) findViewById(R.id.newNumber);
        displayOperation = ( TextView ) findViewById(R.id.operation);

//      num 1 to 9 buttons with Dot '.' for Decimal Operation

        Button button0 = ( Button ) findViewById(R.id.button0);
        Button button1 = ( Button ) findViewById(R.id.button1);
        Button button2 = ( Button ) findViewById(R.id.button2);
        Button button3 = ( Button ) findViewById(R.id.button3);
        Button button4 = ( Button ) findViewById(R.id.button4);
        Button button5 = ( Button ) findViewById(R.id.button5);
        Button button6 = ( Button ) findViewById(R.id.button6);
        Button button7 = ( Button ) findViewById(R.id.button7);
        Button button8 = ( Button ) findViewById(R.id.button8);
        Button button9 = ( Button ) findViewById(R.id.button9);
        Button buttonDot = ( Button ) findViewById(R.id.buttonDot);
        Button buttonNeg = ( Button ) findViewById(R.id.buttonNeg);

//      5   Operation Buttons  ( = + * - / )

        Button buttonEquals = ( Button ) findViewById(R.id.buttonEquals);
        Button buttonDivide = ( Button ) findViewById(R.id.buttonDivide);
        Button buttonMultiply = ( Button ) findViewById(R.id.buttonMultiply);
        Button buttonMinus = ( Button ) findViewById(R.id.buttonMinus);
        Button buttonPlus = ( Button ) findViewById(R.id.buttonPlus);


//      Let's handle first nine numbers with dot.

//        Process of entering number to the value(newNumber) on clicking particular button,
//        it will automatically paste the particular button id built-in particular text number.

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = ( Button ) view;                     // 'view' results storing the value in b
                newNumber.append(b.getText().toString());      //this code will append the particular number of the button.
                // may be clicking 2 and 5 consecutively will result 25
            }
        };

//        setting onClickListener
//        While Clicking the Button, It will call the listener

        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);


//        Now Let's Handle the Operator buttons

        View.OnClickListener opListener = new View.OnClickListener()      //here, opListener is an Object rather than nine button had listener as an Object
        {
            @Override
            public void onClick(View view) {
                Button b = ( Button ) view;
                String op = b.getText().toString();                  // usual stuff that it get its particular operator in the text view
                String value = newNumber.getText().toString();       // we are storing the newNumber in the value to get involved in the operation.

//                if (value.length() != 0) {                            //checking value lenght to performOperation(String. String ); in case of presence of value
//                    performOperation(value, op);
//                }
                try {
                    Double doubleValue = Double.valueOf(value);         // considering and converting the value as a double 0.0
                    performOperation(doubleValue, op);                  //
                } catch (NumberFormatException e) {
                    newNumber.setText("");                              // newNumber was "-" or ".", so clear it
                }
                pendingOperation = op;                                  //nothing but a previous operation text for the upcomming newNumnber
                displayOperation.setText(pendingOperation);             //setting operation that needs to be done after entering operand 2, to solve the problem
            }
        };


        buttonEquals.setOnClickListener(opListener);
        buttonDivide.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);


        /*Lets talk about the implementation of Negative Numbers with negative sign*/


        View.OnClickListener negListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String value = newNumber.getText().toString();

                if (value.length() == 0) {
                    newNumber.setText("-");
                } else {
                    try {
                        Double doubleValue = Double.valueOf(value);
                        doubleValue *= -1;
                        newNumber.setText(doubleValue.toString());
                    } catch (NumberFormatException e) {
                        // newNumber was "-" or ".", so clear it
                        newNumber.setText("");
                    }
                }

            }

        };


        try {
            buttonNeg.setOnClickListener(negListener);
        }
        catch(Exception e)
        {
            e.getMessage();
        }



    }

    /*Now, Lets look into the Landscape problems*/

    /*We are using onSave & onRestore InstanceState to retain the value in both operands*/

    /* We need to save both OperatorDisplay & Operand1 because they are the chance of making the apk worse*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {                            //Starts to save the instance
        outState.putString(STATEofPENDING_OPERATION, pendingOperation);               //pendingOperation stores in STATEofPENDING_OPERATION
        if (operand1 != null) {
            outState.putDouble(STATEofOPERAND1, operand1);                            //operand1 stores in STATEofOPERAND1
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {                  //restores after rotates to landscape mode

        pendingOperation = savedInstanceState.getString(STATEofPENDING_OPERATION);        //getting stored stuff of "STATEofPENDING_OPERATION"
        operand1 = savedInstanceState.getDouble(STATEofOPERAND1);                           //getting stored stuff of "STATEofOPERAND1"
        displayOperation.setText(pendingOperation);

        super.onRestoreInstanceState(savedInstanceState);
    }

    /* Lets get into the operation mode */


    private void performOperation(Double value, String operation) {
        if (null == operand1) {
            operand1 = value;
        } else {
            if (pendingOperation.equals("="))              //equlas();   because pending operation value is "="  at first  ----  we assigned value "="  to  pendingOperation
            {
                pendingOperation = operation;             // performOperation(Double value, String operation)
            }

            switch (pendingOperation)        //we are passing pendingOperation as a switch input, becoz., pending operations should be switched as required
            {
                case "=":
                    operand1 = value;                      //performOperation(Double value, String operation)
                    break;
                case "/":
                    if (value == 0)                         //in order to avoid for ArithmeticOutOfBoundsException
                    {
                        operand1 = 0.0;                     // 0.0 implies operand is of double primitive DataType
                    } else {
                        operand1 /= value;
                    }
                    break;
                case "*":
                    operand1 *= value;
                    break;
                case "-":
                    operand1 -= value;
                    break;
                case "+":
                    operand1 += value;
                    break;
            }
        }

        result.setText(operand1.toString());                        //within in the performOperation code
        newNumber.setText("");                                      // we will display the output and opens door for input
    }
}




