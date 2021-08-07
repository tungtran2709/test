package com.example.calculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickButtion()
    }

    private fun setOnClickButtion() {
        with(binding) {
            listOf(
                    btnZero, btnOne, btnTwo, btnThree, btnFour,
                    btnFive, btnSix, btnSeven, btnEight, btnNine,
                    btnDot, btnDiv, btnAdd, btnMul, btnSub,
                    btnPercent, btnClear, btnDelete, btnPercent, btnEqual
            ).forEach { it.setOnClickListener(this@MainActivity) }
        }
    }

    override fun onClick(v: View?): Unit = with(binding) {
        when (v) {
            btnZero -> addTextCalculator(binding.btnZero.text.toString())
            btnOne -> addTextCalculator(binding.btnOne.text.toString())
            btnTwo -> addTextCalculator(binding.btnTwo.text.toString())
            btnThree -> addTextCalculator(binding.btnThree.text.toString())
            btnFour -> addTextCalculator(binding.btnFour.text.toString())
            btnFive -> addTextCalculator(binding.btnFive.text.toString())
            btnSix -> addTextCalculator(binding.btnSix.text.toString())
            btnSeven -> addTextCalculator(binding.btnSeven.text.toString())
            btnEight -> addTextCalculator(binding.btnEight.text.toString())
            btnNine -> addTextCalculator(binding.btnNine.text.toString())
            btnDot -> addTextCalculator(binding.btnDot.text.toString())
            btnPercent -> addTextCalculator(binding.btnPercent.text.toString())
            btnDiv -> addTextCalculator(binding.btnDiv.text.toString())
            btnMul -> addTextCalculator(binding.btnMul.text.toString())
            btnSub -> addTextCalculator(binding.btnSub.text.toString())
            btnAdd -> addTextCalculator(binding.btnAdd.text.toString())
            btnClear -> clearScreen()
            btnDelete -> delete()
            btnEqual -> {
                val arrayPostfix = convertPostfix(binding.txtCalculator.text.toString())
                val result = calculate(arrayPostfix).toString()
                txtResult.text.clear()
                txtResult.text.append(result)
            }
        }
    }

    private fun addTextCalculator(str: String) {
        binding.txtCalculator.text.append(str)
    }

    private fun clearScreen() = with(binding) {
        txtCalculator.setText(EMPTY_STRING)
        txtResult.setText(DOUBLE_NUMBER_0.toString())
    }

    private fun delete() {
        var txtResult = binding.txtCalculator.text.toString()
        if (txtResult.isNotEmpty()) {
            binding.txtCalculator.text.delete(txtResult.length - 1, txtResult.length)
        }
    }

    private fun checkOperator(text: String): Boolean {
        if (text.equals(OPERATOR_ADD.toString()) || text.equals(OPERATOR_MUL.toString()) || text.equals(
                        OPERATOR_SUB.toString()
                ) ||
                text.equals(OPERATOR_DIV.toString()) || text.equals(OPERATOR_PER.toString())
        ) return true
        return false
    }

    private fun priorityOperator(operator: String) = when (operator) {
        OPERATOR_ADD.toString(), OPERATOR_SUB.toString() -> PRIORITY_ONE
        OPERATOR_DIV.toString(), OPERATOR_MUL.toString(), OPERATOR_PER.toString() -> PRIORITY_TWO
        else -> PRIORITY
    }

    private fun convertPostfix(text: String): ArrayList<String> {
        val arrayPostfix = ArrayList<String>()
        val stackOperator = Stack<String>()
        var num = EMPTY_STRING
        for (i in text.indices) {
            if (!checkOperator(text[i].toString())) {
                num += text[i]
                if (i == text.length - 1) {
                    arrayPostfix.add(num)
                    num = EMPTY_STRING
                }
            } else {
                arrayPostfix.add(num)
                num = EMPTY_STRING
                while (!stackOperator.isEmpty() &&
                        priorityOperator(text[i].toString()) <= priorityOperator(stackOperator.peek())
                ) {
                    arrayPostfix.add(stackOperator.pop().toString())
                }
                stackOperator.push(text[i].toString())
            }
        }
        while (!stackOperator.isEmpty()) {
            arrayPostfix.add(stackOperator.pop().toString())
        }
        return arrayPostfix
    }

    private fun calculate(arrayPrefix: ArrayList<String>): Double {
        val stackNumber = Stack<Double>()
        for (i in arrayPrefix) {
            val num: Double? = i.toDoubleOrNull()
            if (num != null) stackNumber.push(num)
            else {
                val number1 = stackNumber.pop().toDouble()
                val number2 = stackNumber.pop().toDouble()
                when (i) {
                    OPERATOR_ADD.toString() -> stackNumber.push(number2 + number1)
                    OPERATOR_SUB.toString() -> stackNumber.push(number2 - number1)
                    OPERATOR_DIV.toString() -> stackNumber.push(number2 / number1)
                    OPERATOR_MUL.toString() -> stackNumber.push(number2 * number1)
                    OPERATOR_PER.toString() -> stackNumber.push(number2 % number1)
                }
            }

        }
        return if (stackNumber.isNotEmpty()) stackNumber.pop().toDouble() else DOUBLE_NUMBER_0
    }

    companion object {
        const val DOUBLE_NUMBER_0 = 0.0
        const val EMPTY_STRING = ""
        const val OPERATOR_ADD = '+'
        const val OPERATOR_SUB = '-'
        const val OPERATOR_MUL = 'x'
        const val OPERATOR_DIV = '÷'
        const val OPERATOR_PER = '%'
        const val PRIORITY_ONE = 1
        const val PRIORITY_TWO = 2
        const val PRIORITY = -1
    }

}
