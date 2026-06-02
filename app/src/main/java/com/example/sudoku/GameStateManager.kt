package com.example.sudoku

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

/**
 * 遊戲狀態管理類別
 * 負責管理遊戲的整體狀態和邏輯流程
 */
class GameStateManager {
    enum class GameState {
        IDLE,           // 遊戲未開始
        PLAYING,        // 遊戲進行中
        PAUSED,         // 遊戲暫停
        CHECKING,       // 檢查答案中
        WON,            // 遊戲勝利
        LOST            // 遊戲失敗
    }

    private var currentState = GameState.IDLE
    private var timeElapsed = 0
    private var errorCount = 0

    companion object {
        const val MAX_ERRORS = 3
    }

    /**
     * 設定遊戲狀態
     */
    fun setGameState(state: GameState) {
        currentState = state
    }

    /**
     * 取得當前遊戲狀態
     */
    fun getGameState(): GameState {
        return currentState
    }

    /**
     * 增加時間
     */
    fun incrementTime() {
        timeElapsed++
    }

    /**
     * 取得已用時間
     */
    fun getTimeElapsed(): Int {
        return timeElapsed
    }

    /**
     * 重置時間
     */
    fun resetTime() {
        timeElapsed = 0
    }

    /**
     * 增加錯誤計數
     */
    fun addError(): Boolean {
        errorCount++
        return errorCount >= MAX_ERRORS
    }

    /**
     * 取得錯誤計數
     */
    fun getErrorCount(): Int {
        return errorCount
    }

    /**
     * 重置錯誤計數
     */
    fun resetErrorCount() {
        errorCount = 0
    }

    /**
     * 重置遊戲
     */
    fun resetGame() {
        currentState = GameState.IDLE
        timeElapsed = 0
        errorCount = 0
    }

    /**
     * 繪製遊戲狀態提示
     */
    fun drawStateIndicator(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        when (currentState) {
            GameState.IDLE -> drawIdleIndicator(canvas, screenWidth, screenHeight)
            GameState.PLAYING -> {} // 遊戲進行中不需要特別提示
            GameState.PAUSED -> drawPausedIndicator(canvas, screenWidth, screenHeight)
            GameState.CHECKING -> drawCheckingIndicator(canvas, screenWidth, screenHeight)
            GameState.WON -> drawWonIndicator(canvas, screenWidth, screenHeight)
            GameState.LOST -> drawLostIndicator(canvas, screenWidth, screenHeight)
        }
    }

    private fun drawIdleIndicator(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        val paint = Paint().apply {
            color = Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 50f
        }

        canvas.drawText("點擊開始遊戲", screenWidth / 2, screenHeight / 2, paint)
    }

    private fun drawPausedIndicator(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        val backgroundPaint = Paint().apply {
            color = Color.argb(150, 0, 0, 0)
            style = Paint.Style.FILL
        }

        val textPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 60f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val rect = RectF(screenWidth / 2 - 200, screenHeight / 2 - 100, screenWidth / 2 + 200, screenHeight / 2 + 100)
        canvas.drawRect(rect, backgroundPaint)
        canvas.drawText("暫停中", screenWidth / 2, screenHeight / 2 + 20, textPaint)
    }

    private fun drawCheckingIndicator(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        val backgroundPaint = Paint().apply {
            color = Color.argb(150, 100, 100, 100)
            style = Paint.Style.FILL
        }

        val textPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 50f
        }

        val rect = RectF(screenWidth / 2 - 250, screenHeight / 2 - 80, screenWidth / 2 + 250, screenHeight / 2 + 80)
        canvas.drawRect(rect, backgroundPaint)
        canvas.drawText("檢查中...", screenWidth / 2, screenHeight / 2 + 15, textPaint)
    }

    private fun drawWonIndicator(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        val backgroundPaint = Paint().apply {
            color = Color.argb(200, 0, 150, 0)
            style = Paint.Style.FILL
        }

        val textPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 80f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val rect = RectF(screenWidth / 2 - 300, screenHeight / 2 - 150, screenWidth / 2 + 300, screenHeight / 2 + 150)
        canvas.drawRect(rect, backgroundPaint)
        canvas.drawText("成功完成!", screenWidth / 2, screenHeight / 2 + 30, textPaint)
    }

    private fun drawLostIndicator(canvas: Canvas, screenWidth: Float, screenHeight: Float) {
        val backgroundPaint = Paint().apply {
            color = Color.argb(200, 150, 0, 0)
            style = Paint.Style.FILL
        }

        val textPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 60f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        val rect = RectF(screenWidth / 2 - 200, screenHeight / 2 - 100, screenWidth / 2 + 200, screenHeight / 2 + 100)
        canvas.drawRect(rect, backgroundPaint)
        canvas.drawText("遊戲結束", screenWidth / 2, screenHeight / 2 + 20, textPaint)
    }
}
