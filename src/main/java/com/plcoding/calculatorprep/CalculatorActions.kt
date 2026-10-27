package com.plcoding.calculatorprep

sealed class CalculatorActions {
    data class Number(val number: Int): CalculatorActions()
    object Clear: CalculatorActions()
    object Delete: CalculatorActions()
    object Square: CalculatorActions()
    data class Operation(val operation: CalculatorOperation): CalculatorActions()
    object Calculate: CalculatorActions()
    object Decimal: CalculatorActions()

    object ChangeSign: CalculatorActions()
}