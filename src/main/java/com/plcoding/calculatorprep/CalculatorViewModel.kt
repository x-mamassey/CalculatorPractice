package com.plcoding.calculatorprep

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {

    var state by mutableStateOf(CalculatorState())

    fun changeSign() {
        if (state.number2.isNotEmpty()) {
            state = state.copy(
                number2 = if (state.number2.startsWith("-"))
                    state.number2.drop(1)
                else "-${state.number2}"
            )
        } else if (state.number1.isNotEmpty()) {
            state = state.copy(
                number1 = if (state.number1.startsWith("-"))
                    state.number1.drop(1)
                else "-${state.number1}"
            )
        }
    }
    fun onAction(action: CalculatorActions) {
        when(action) {
            is CalculatorActions.Number -> enterNumber(action.number)
            is CalculatorActions.Delete -> delete()
            is CalculatorActions.Clear -> state = CalculatorState()
            is CalculatorActions.Operation -> enterOperation(action.operation)
            is CalculatorActions.Decimal -> enterDecimal()
            is CalculatorActions.Calculate -> calculate()
            is CalculatorActions.Square -> square()
            is CalculatorActions.ChangeSign -> changeSign()
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if(state.number1.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun calculate() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if(number1 != null && number2 != null) {
            val result = when(state.operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                null -> return
            }
            state = state.copy(
                number1 = result.toString().take(15),
                number2 = "",
                operation = null
            )
        }
    }

    private fun square() {
        val currentNumber = if (state.operation != null) {
            state.number2
        } else {
            state.number1
        }

        val number = currentNumber.toDoubleOrNull() ?: return
        val result = number * number

        val formattedResult = if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString()
        }

        if (state.operation != null) {
            state = state.copy(number2 = formattedResult)
        } else {
            state = state.copy(number1 = formattedResult)
        }
    }

    private fun delete() {
        when {
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.number1.isNotBlank() -> state = state.copy(
                number1 = state.number1.dropLast(1)
            )
        }
    }

    private fun enterDecimal() {
        if(state.operation == null && !state.number1.contains(".") && state.number1.isNotBlank()) {
            state = state.copy(
                number1 = state.number1 + "."
            )
            return
        } else if(!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(
                number2 = state.number2 + "."
            )
        }
    }

    private fun enterNumber(number: Int) {
        if(state.operation == null) {
            if(state.number1.length >= MAX_NUM_LENGTH) {
                return
            }
            state = state.copy(
                number1 = state.number1 + number
            )
            return
        }
        if(state.number2.length >= MAX_NUM_LENGTH) {
            return
        }
        state = state.copy(
            number2 = state.number2 + number
        )
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}