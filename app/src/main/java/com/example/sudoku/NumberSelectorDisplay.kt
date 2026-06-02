package com.example.sudoku

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

/**
 * 數字選擇顯示類別
 * 負責繪製數字按鈕和UI元素
 */
class NumberSelectorDisplay {
    companion object {
        const val BUTTON_WIDTH = 80f
        const val BUTTON_HEIGHT = 50f
        const val BUTTON_MARGIN = 20f
        const val SELECTOR_TOP = 1550f
        const val CLEAR_BUTTON_WIDTH = 100f
        const val CLEAR_BUTTON_HEIGHT = 50f
    }

    private val buttonBackgroundPaint = Paint().apply {
        color = Color.rgb(200, 200, 200) // 淺灰色
        style = Paint.Style.FILL
    }

    private val buttonBorderPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val selectedButtonPaint = Paint().apply {
        color = Color.rgb(100, 150, 200) // 淺藍色（選中狀態）
        style = Paint.Style.FILL
    }

    private val buttonTextPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = 40f
    }

    private val clearButtonPaint = Paint().apply {
        color = Color.rgb(255, 100, 100) // 淡紅色
        style = Paint.Style.FILL
    }

    private val clearButtonBorderPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val clearButtonTextPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = 30f
    }

    /**
     * 繪製所有數字按鈕
     */
    fun drawNumberButtons(
        canvas: Canvas,
        selectedNumber: Int?
    ) {
        for (num in 1..9) {
            drawNumberButton(canvas, num, num == selectedNumber)
        }
    }

    /**
     * 繪製單個數字按鈕
     */
    private fun drawNumberButton(
        canvas: Canvas,
        number: Int,
        isSelected: Boolean
    ) {
        val column = (number - 1) % 5
        val row = (number - 1) / 5

        val left = 100f + (column * (BUTTON_WIDTH + BUTTON_MARGIN))
        val top = SELECTOR_TOP + (row * (BUTTON_HEIGHT + BUTTON_MARGIN))
        val right = left + BUTTON_WIDTH
        val bottom = top + BUTTON_HEIGHT

        val backgroundPaint = if (isSelected) selectedButtonPaint else buttonBackgroundPaint

        canvas.drawRect(left, top, right, bottom, backgroundPaint)
        canvas.drawRect(left, top, right, bottom, buttonBorderPaint)

        val centerX = left + (BUTTON_WIDTH / 2)
        val centerY = top + (BUTTON_HEIGHT / 2)

        canvas.drawText(number.toString(), centerX, centerY + 15, buttonTextPaint)
    }

    /**
     * 繪製清除按鈕
     */
    fun drawClearButton(canvas: Canvas) {
        val clearLeft = 600f
        val clearTop = SELECTOR_TOP
        val clearRight = clearLeft + CLEAR_BUTTON_WIDTH
        val clearBottom = clearTop + CLEAR_BUTTON_HEIGHT

        canvas.drawRect(clearLeft, clearTop, clearRight, clearBottom, clearButtonPaint)
        canvas.drawRect(clearLeft, clearTop, clearRight, clearBottom, clearButtonBorderPaint)

        val centerX = clearLeft + (CLEAR_BUTTON_WIDTH / 2)
        val centerY = clearTop + (CLEAR_BUTTON_HEIGHT / 2)

        canvas.drawText("清除", centerX, centerY + 12, clearButtonTextPaint)
    }

    /**
     * 繪製提交按鈕
     */
    fun drawSubmitButton(canvas: Canvas) {
        val submitLeft = 300f
        val submitTop = 1600f
        val submitRight = 700f
        val submitBottom = 1700f

        val submitButtonPaint = Paint().apply {
            color = Color.rgb(144, 238, 144) // 淡綠色
            style = Paint.Style.FILL
        }

        val submitBorderPaint = Paint().apply {
            color = Color.rgb(255, 215, 0) // 金色
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

        val submitTextPaint = Paint().apply {
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 50f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        canvas.drawRect(submitLeft, submitTop, submitRight, submitBottom, submitButtonPaint)
        canvas.drawRect(submitLeft, submitTop, submitRight, submitBottom, submitBorderPaint)

        val centerX = (submitLeft + submitRight) / 2
        val centerY = (submitTop + submitBottom) / 2

        canvas.drawText("提交?", centerX, centerY + 20, submitTextPaint)
    }

    /**
     * 檢查提交按鈕是否被點擊
     */
    fun isSubmitButtonPressed(x: Float, y: Float): Boolean {
        val submitLeft = 300f
        val submitTop = 1600f
        val submitRight = 700f
        val submitBottom = 1700f

        return x >= submitLeft && x <= submitRight && y >= submitTop && y <= submitBottom
    }

    /**
     * 繪製計時器顯示框
     */
    fun drawTimerDisplay(
        canvas: Canvas,
        minutes: Int,
        seconds: Int
    ) {
        val timerLeft = 400f
        val timerTop = 200f
        val timerRight = 600f
        val timerBottom = 400f

        val timerBackgroundPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
        }

        val timerBorderPaint = Paint().apply {
            color = Color.rgb(128, 128, 128) // 中灰色
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }

        val timerTextPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 60f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        // 繪製圓角矩形
        val rect = RectF(timerLeft, timerTop, timerRight, timerBottom)
        canvas.drawRoundRect(rect, 20f, 20f, timerBackgroundPaint)
        canvas.drawRoundRect(rect, 20f, 20f, timerBorderPaint)

        // 顯示時間
        val timeString = String.format("%02d:%02d", minutes, seconds)
        canvas.drawText(timeString, 500f, 320f, timerTextPaint)
    }

    /**
     * 繪製難度指示器
     */
    fun drawDifficultyIndicator(canvas: Canvas, difficulty: String) {
        val diffPaint = Paint().apply {
            color = Color.BLACK
            textAlign = Paint.Align.LEFT
            textSize = 30f
        }

        canvas.drawText("難度: $difficulty", 100f, 450f, diffPaint)
    }
}
