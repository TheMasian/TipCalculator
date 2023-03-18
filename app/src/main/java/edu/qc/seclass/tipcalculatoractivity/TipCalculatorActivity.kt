package edu.qc.seclass.tipcalculatoractivity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.qc.seclass.tipcalculatoractivity.ui.theme.TipCalculatorActivityTheme
import kotlin.math.roundToInt

class TipCalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorActivityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipCalculatorApp()
                }
            }
        }
    }
}

@Composable
fun TipCalculatorApp() {

    Column {
        TopAppBarFun()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            var checkAmountValue by remember { mutableStateOf("") }
            val checkAmount = checkAmountValue.toDoubleOrNull() ?: 0.00

            var partySizeValue by remember { mutableStateOf("") }
            val partySize = partySizeValue.toIntOrNull() ?: 0

            var fifteenPercentTipValue by remember { mutableStateOf("") }
            var twentyPercentTipValue by remember { mutableStateOf("") }
            var twentyfivePercentTipValue by remember { mutableStateOf("") }

            var fifteenPercentTotalValue by remember { mutableStateOf("") }
            var twentyPercentTotalValue by remember { mutableStateOf("") }
            var twentyfivePercentTotalValue by remember { mutableStateOf("") }

            val focusManager = LocalFocusManager.current
            val context = LocalContext.current
            InputField(
                name = "Check Amount:",
                value = checkAmountValue,
                onValueChanged = { checkAmountValue = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            InputField(
                name = "Party Size:",
                value = partySizeValue,
                onValueChanged = { partySizeValue = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))

            Button(
                onClick = {

                    if (checkAmount > 0.00 && partySize > 0) {

                        fifteenPercentTipValue = calculateTip(
                            amount = checkAmount,
                            tipPercent = 15,
                            partySize = partySize
                        )

                        twentyPercentTipValue = calculateTip(
                            amount = checkAmount,
                            tipPercent = 20,
                            partySize = partySize
                        )

                        twentyfivePercentTipValue = calculateTip(
                            amount = checkAmount,
                            tipPercent = 25,
                            partySize = partySize
                        )

                        fifteenPercentTotalValue = calculateTotal(
                            amount = checkAmount,
                            tip = fifteenPercentTipValue,
                            partySize = partySize
                        )

                        twentyPercentTotalValue = calculateTotal(
                            amount = checkAmount,
                            tip = twentyPercentTipValue,
                            partySize = partySize
                        )

                        twentyfivePercentTotalValue = calculateTotal(
                            amount = checkAmount,
                            tip = twentyfivePercentTipValue,
                            partySize = partySize
                        )
                    } else {
                        Toast.makeText(context, "Empty or incorrect value(s)!", Toast.LENGTH_SHORT)
                            .show()
                    }

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = LightGray)
            ) {
                Text(text = "COMPUTE TIP")
            }

            BoxWithConstraints {
                if (maxWidth < 400.dp)
                    Spacer(modifier = Modifier.padding(top = 48.dp))
                else
                    Spacer(modifier = Modifier.padding(top = 8.dp))
            }
            Text(
                text = "Tip and totals(per person)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
//
//            OutputField(
//                name = "Tip",
//                value = fifteenPercentTipValue,
//                onValueChanged = { fifteenPercentTipValue = it }
//            )

            OutputField(
                name = "Tip:",
                value = fifteenPercentTipValue,
                value1 = twentyPercentTipValue,
                value2 = twentyfivePercentTipValue,
                onValueChanged = { fifteenPercentTipValue = it },
                onValueChanged1 = { twentyPercentTipValue = it },
                onValueChanged2 = { twentyfivePercentTipValue = it }
            )
            BoxWithConstraints {
                if (maxWidth < 400.dp)
                    Spacer(modifier = Modifier.padding(top = 48.dp))
                else
                    Spacer(modifier = Modifier.padding(top = 8.dp))
            }

            OutputField(
                name = "Total:",
                value = fifteenPercentTotalValue,
                value1 = twentyPercentTotalValue,
                value2 = twentyfivePercentTotalValue,
                onValueChanged = { fifteenPercentTotalValue = it },
                onValueChanged1 = { twentyPercentTotalValue = it },
                onValueChanged2 = { twentyfivePercentTotalValue = it }
            )


        }

    }
}

// Simple composable UI function to generate a top app bar
@Composable
fun TopAppBarFun() {
    TopAppBar(
        elevation = 16.dp,
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 24.dp,
                ambientColor = Black
            )
    )
}

// A composable UI function that takes in user inputs and updates
// current states of text fields to show content as its entered
@Composable
private fun InputField(
    name: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChanged: (String) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(
                start = 16.dp
            )
        )

        TextField(
            value = value,
            onValueChange = onValueChanged,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Magenta,
                focusedBorderColor = Magenta
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            modifier = Modifier
                .padding(end = 16.dp)
                .width(200.dp)
        )
    }
}

// A composable UI function that takes in all the values for either tip percentages
// or total per person and any changes in states, then generates the data in a row
// and calls TipTextFields to populate with new states
@Composable
private fun OutputField(
    name: String,
    value: String,
    value1: String,
    value2: String,
    onValueChanged: (String) -> Unit,
    onValueChanged1: (String) -> Unit,
    onValueChanged2: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(text = "15%")
        ValueTextField(value = value, onValueChanged = onValueChanged)
        Text(text = "20%")
        ValueTextField(value = value1, onValueChanged = onValueChanged1)
        Text(text = "25%")
        ValueTextField(value = value2, onValueChanged = onValueChanged2)
    }

}

// A composable UI function for updating tip and total text field by
// taking in a string value, and passes current state and generates
// a readonly text field
@Composable
private fun ValueTextField(value: String, onValueChanged: (String) -> Unit) {
    Row {
        TextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier
                .width(56.dp),
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.Transparent
            ),
            textStyle = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}

// Private function that takes in amount, tip percentage and party size
// then takes amount multiplied by tipPercent divided by 100 and finally divided by
// party size and rounds to nearest int converted to string and returns the value
private fun calculateTip(amount: Double, tipPercent: Int, partySize: Int): String {

    val tip = amount * tipPercent / 100 / partySize
    return tip.roundToInt().toString()
}

// Private function that takes in the amount, tip amount per person and party size
// Converts tip to an integer value, then divides total amount by party size and adds the tip
// rounding to the nearest integer and converts value to a string
private fun calculateTotal(amount: Double, tip: String, partySize: Int): String {
    val tipInt = tip.toInt()
    return (amount / partySize + tipInt).roundToInt().toString()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorActivityTheme {
        TipCalculatorApp()
    }
}