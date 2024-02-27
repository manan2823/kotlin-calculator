package com.example.kotlin_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.resultsTV
import kotlinx.android.synthetic.main.activity_main.workingTV

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun allClearAction(view: View) {
        workingTV.text = ""
        resultsTV.text = ""
    }
    fun backSpaceAction(view: View) {
        val length = workingTV.length()
        if(length > 0)
            workingTV.text = workingTV.text.subSequence(0,length-1)
    }
    fun equalsAction(view: View) {

        resultsTV.text     = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperator = digitsOperator()
        if(digitsOperator.isEmpty()) return ""

        val timesDivision = timeDivisionCalculator(digitsOperator)
        if(digitsOperator.isEmpty()) return ""

        val result = addSubtractCalculator(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculator(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if(operator == '+')
                    result += nextDigit
                if(operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timeDivisionCalculator(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val preDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(preDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(preDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(preDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList

    }

    private fun digitsOperator(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workingTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }


    fun operationAction(view: View) {
        if(view is Button && canAddOperation)
        {
            workingTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun numberAction(view: View) {

        if(view is Button)
        {
            if(view.text==".")
            {
                if(canAddDecimal)
                    workingTV.append(view.text)

                canAddDecimal = false
            }
            else
                workingTV.append(view.text)
            canAddOperation = true
        }

    }
}