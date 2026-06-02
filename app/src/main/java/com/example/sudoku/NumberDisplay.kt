package com.example.sudoku

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface

/**
 * 數字顯示類別
 * 負責在棋盤上顯示預設數字和玩家填入的數字
 */
class NumberDisplay {
    companion object {
        const val CELL_SIZE = 100f
        const val BOARD_LEFT = 100f
        const val BOARD_TOP = 500f
    }

    private val presetNumberPaint = Paint().apply {
        color = Color.rgb(0, 0, 139) // 深藍色
        textAlign = Paint.Align.CENTER
        textSize = 60f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val playerNumberPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = 60f
        typeface = Typeface.DEFAULT_BOLD
    }

    private val smallNumberPaint = Paint().apply {
        color = Color.GRAY
        textAlign = Paint.Align.CENTER
        textSize = 20f
        typeface = Typeface.DEFAULT
    }

    /**
     * 繪製所有數字（預設 + 玩家填入）
     */
    fun drawAllNumbers(
        canvas: Canvas,
        puzzleBoard: Array<IntArray>,
        playerBoard: Array<IntArray>
    ) {
        for (row in 0..8) {
            for (col in 0..8) {
                if (puzzleBoard[row][col] != 0) {
                    // 繪製預設數字
                    drawNumber(canvas, row, col, puzzleBoard[row][col], isPreset = true)
                } else if (playerBoard[row][col] != 0) {
                    // 繪製玩家填入的數字
                    drawNumber(canvas, row, col, playerBoard[row][col], isPreset = false)
                }
            }
        }
    }

    /**
     * 繪製單個數字
     */
    private fun drawNumber(
        canvas: Canvas,
        row: Int,
        col: Int,
        number: Int,
        isPreset: Boolean
    ) {
        val centerX = BOARD_LEFT + (col * CELL_SIZE) + (CELL_SIZE / 2)
        val centerY = BOARD_TOP + (row * CELL_SIZE) + (CELL_SIZE / 2)

        val paint = if (isPreset) presetNumberPaint else playerNumberPaint
        canvas.drawText(number.toString(), centerX, centerY + 20, paint)
    }

    /**
     * 繪製候選數字（小數字標記）
     */
    fun drawCandidateNumbers(
        canvas: Canvas,
        row: Int,
        col: Int,
        candidates: Set<Int>
    ) {
        if (candidates.isEmpty()) return

        val cellLeft = BOARD_LEFT + (col * CELL_SIZE)
        val cellTop = BOARD_TOP + (row * CELL_SIZE)

        for (num in candidates) {
            val position = getCandidateNumberPosition(num)
            val x = cellLeft + position.first
            val y = cellTop + position.second

            canvas.drawText(num.toString(), x, y, smallNumberPaint)
        }
    }

    /**
     * 取得候選數字在格子內的相對位置
     * 3x3 網格排列 (1-9)
     */
    private fun getCandidateNumberPosition(num: Int): Pair<Float, Float> {
        if (num !in 1..9) return Pair(0f, 0f)

        val row = (num - 1) / 3
        val col = (num - 1) % 3

        val x = 15f + (col * 30f)
        val y = 20f + (row * 30f)

        return Pair(x, y)
    }

    /**
     * 繪製選中格子的數字提示（大圓形框）
     */
    fun drawSelectedCellNumberHint(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        number: Int?
    ) {
        if (number == null) return

        val hintPaint = Paint().apply {
            color = Color.argb(200, 255, 255, 100) // 半透明淡黃色背景
            style = Paint.Style.FILL
        }

        val hintBorderPaint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }

        val radius = 30f
        canvas.drawCircle(centerX, centerY, radius, hintPaint)
        canvas.drawCircle(centerX, centerY, radius, hintBorderPaint)

        val textPaint = Paint().apply {
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 40f
            typeface = Typeface.DEFAULT_BOLD
        }

        canvas.drawText(number.toString(), centerX, centerY + 15, textPaint)
    }

    /**
     * 繪製錯誤提示（紅色 X）
     */
    fun drawErrorMark(canvas: Canvas, row: Int, col: Int) {
        val left = BOARD_LEFT + (col * CELL_SIZE)
        val top = BOARD_TOP + (row * CELL_SIZE)
        val right = left + CELL_SIZE
        val bottom = top + CELL_SIZE

        val errorPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }

        canvas.drawLine(left + 10, top + 10, right - 10, bottom - 10, errorPaint)
        canvas.drawLine(right - 10, top + 10, left + 10, bottom - 10, errorPaint)
    }

    /**
     * 繪製成功訊息
     */
    fun drawSuccessMessage(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        val messagePaint = Paint().apply {
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 80f
            typeface = Typeface.DEFAULT_BOLD
        }

        val backgroundPaint = Paint().apply {
            color = Color.argb(200, 100, 200, 100) // 半透明綠色
            style = Paint.Style.FILL
        }

        val rect = RectF(
            screenWidth / 2 - 300,
            screenHeight / 2 - 150,
            screenWidth / 2 + 300,
            screenHeight / 2 + 150
        )

        canvas.drawRoundRect(rect, 20f, 20f, backgroundPaint)
        canvas.drawText(
            "完成！",
            screenWidth / 2,
            screenHeight / 2 + 30,
            messagePaint
        )
    }

    /**
     * 繪製錯誤提示訊息
     */
    fun drawErrorMessage(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        val messagePaint = Paint().apply {
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 60f
            typeface = Typeface.DEFAULT_BOLD
        }

        val backgroundPaint = Paint().apply {
            color = Color.argb(200, 200, 100, 100) // 半透明紅色
            style = Paint.Style.FILL
        }

        val rect = RectF(
            screenWidth / 2 - 250,
            screenHeight / 2 - 120,
            screenWidth / 2 + 250,
            screenHeight / 2 + 120
        )

        canvas.drawRoundRect(rect, 20f, 20f, backgroundPaint)
        canvas.drawText(
            "有錯誤！",
            screenWidth / 2,
            screenHeight / 2 - 20,
            messagePaint
        )
        canvas.drawText(
            "點擊重試",
            screenWidth / 2,
            screenHeight / 2 + 40,
            messagePaint
        )
    }
}
