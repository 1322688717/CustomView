package com.example.customview.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

/**
 * 自定义textview
 */
class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var textList = listOf("孙悟空", "沙悟净", "猪八戒", "唐僧")
    private var colorList = listOf(Color.BLUE, Color.DKGRAY, Color.RED, Color.YELLOW)

    private var timer: Timer? = null
    private var lastRandom = -1
    private var lastColor = -1

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    init {
        initView()
        startTimer()
    }

    private fun initView() {
        updateTextAndColor()
        setTextSize(15.0F)
    }

    private fun updateTextAndColor() {
        var random = Random.nextInt(textList.size)
        while (random == lastRandom) {
            random = Random.nextInt(textList.size)
        }
        text = textList[random]

        var color = colorList[Random.nextInt(colorList.size)]
        while (color == lastColor) {
            color = colorList[Random.nextInt(colorList.size)]
        }
        setTextColor(color)

        lastRandom = random
        lastColor = color
    }

    private fun startTimer() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                scope.launch {
                    updateTextAndColor()
                }
            }
        }, 0, 1000)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        job.cancel()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }
}
