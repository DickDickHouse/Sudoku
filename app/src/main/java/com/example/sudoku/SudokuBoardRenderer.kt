package com.example.sudoku

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

/**
 * 棋盤顯示類別
 * 負責繪製 Sudoku 棋盤、格子、框線等
 */
class SudokuBoardRenderer {
    // 虛擬座標常數
    companion object {
        const val BOARD_LEFT = 100f
        const val BOARD_TOP = 500f
        const val BOARD_RIGHT = 1000f
        const val BOARD_BOTTOM = 1400f
        const val BOARD_SIZE = 900f
        const val CELL_SIZE = 100f
        const val GRID_COUNT = 9
    }

    // 畫筆
    private val boardBackgroundPaint = Paint().apply {
        color = Color.rgb(255, 255, 200) // 淺黃色
        style = Paint.Style.FILL
    }

    private val outerFramePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }

    private val thickFramePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    private val thinFramePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    /**
     * 繪製棋盤
     */
    fun drawBoard(canvas: Canvas) {
        // 繪製棋盤背景
        canvas.drawRect(BOARD_LEFT, BOARD_TOP, BOARD_RIGHT, BOARD_BOTTOM, boardBackgroundPaint)

        // 繪製棋盤外圍粗黑框
        canvas.drawRect(BOARD_LEFT, BOARD_TOP, BOARD_RIGHT, BOARD_BOTTOM, outerFramePaint)

        // 繪製 3x3 大格粗框
        for (i in 0..3) {
            val pos = BOARD_LEFT + (i * 300f)
            // 垂直線
            canvas.drawLine(pos, BOARD_TOP, pos, BOARD_BOTTOM, thickFramePaint)
            // 水平線
            canvas.drawLine(BOARD_LEFT, BOARD_TOP + (i * 300f), BOARD_RIGHT, BOARD_TOP + (i * 300f), thickFramePaint)
        }

        // 繪製細小格子框線
        for (i in 0..9) {
            val pos = BOARD_LEFT + (i * 100f)
            // 垂直線
            canvas.drawLine(pos, BOARD_TOP, pos, BOARD_BOTTOM, thinFramePaint)
            // 水平線
            canvas.drawLine(BOARD_LEFT, BOARD_TOP + (i * 100f), BOARD_RIGHT, BOARD_TOP + (i * 100f), thinFramePaint)
        }
    }

    /**
     * 繪製選中格子的圓形提示
     */
    fun drawSelectionRing(canvas: Canvas, cellX: Int, cellY: Int) {
        val centerX = BOARD_LEFT + (cellX * CELL_SIZE) + (CELL_SIZE / 2)
        val centerY = BOARD_TOP + (cellY * CELL_SIZE) + (CELL_SIZE / 2)
        val radius = 45f

        val selectionPaint = Paint().apply {
            color = Color.argb(128, 0, 0, 0) // 半透明黑色
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

        canvas.drawCircle(centerX, centerY, radius, selectionPaint)
    }

    /**
     * 繪製高亮格子
     */
    fun drawHighlightCell(canvas: Canvas, cellX: Int, cellY: Int) {
        val left = BOARD_LEFT + (cellX * CELL_SIZE)
        val top = BOARD_TOP + (cellY * CELL_SIZE)
        val right = left + CELL_SIZE
        val bottom = top + CELL_SIZE

        val highlightPaint = Paint().apply {
            color = Color.argb(100, 200, 200, 100) // 半透明淡黃色
            style = Paint.Style.FILL
        }

        canvas.drawRect(left, top, right, bottom, highlightPaint)
    }

    /**
     * 取得虛擬座標中的格子位置
     */
    fun getCellAtPosition(x: Float, y: Float): Pair<Int, Int>? {
        if (x < BOARD_LEFT || x > BOARD_RIGHT || y < BOARD_TOP || y > BOARD_BOTTOM) {
            return null
        }

        val cellX = ((x - BOARD_LEFT) / CELL_SIZE).toInt()
        val cellY = ((y - BOARD_TOP) / CELL_SIZE).toInt()

        return if (cellX in 0 until GRID_COUNT && cellY in 0 until GRID_COUNT) {
            Pair(cellX, cellY)
        } else {
            null
        }
    }

    /**
     * 取得格子中心座標
     */
    fun getCellCenterPosition(cellX: Int, cellY: Int): Pair<Float, Float> {
        val centerX = BOARD_LEFT + (cellX * CELL_SIZE) + (CELL_SIZE / 2)
        val centerY = BOARD_TOP + (cellY * CELL_SIZE) + (CELL_SIZE / 2)
        return Pair(centerX, centerY)
    }
}
