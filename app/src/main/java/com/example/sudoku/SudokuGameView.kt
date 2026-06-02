package com.example.sudoku

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View

/**
 * 主遊戲檢視類別
 * 整合所有元件並處理遊戲邏輯
 */
class SudokuGameView(context: Context) : View(context) {
    private val boardRenderer = SudokuBoardRenderer()
    private val numberDisplay = NumberDisplay()
    private val numberSelectorDisplay = NumberSelectorDisplay()
    private val numberSelector = NumberSelector()
    private var puzzleGenerator = PuzzleGenerator()

    private val puzzleBoard = puzzleGenerator.getPuzzleBoard()
    private val playerBoard = Array(9) { IntArray(9) }
    private val solutionBoard = puzzleGenerator.getSolution()

    private var selectedCell: Pair<Int, Int>? = null
    private var selectedNumber: Int? = null
    private var gameStarted = false
    private var timeElapsed = 0 // 秒
    private var gameFinished = false
    private var errorMessage: String? = null

    init {
        // 初始化玩家棋盤
        for (i in 0..8) {
            for (j in 0..8) {
                playerBoard[i][j] = 0
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 繪製棋盤
        boardRenderer.drawBoard(canvas)

        // 繪製預設和玩家數字
        numberDisplay.drawAllNumbers(canvas, puzzleBoard, playerBoard)

        // 繪選中的格子
        selectedCell?.let {
            boardRenderer.drawHighlightCell(canvas, it.first, it.second)
            val (centerX, centerY) = boardRenderer.getCellCenterPosition(it.first, it.second)
            boardRenderer.drawSelectionRing(canvas, it.first, it.second)
            selectedNumber?.let { num ->
                numberDisplay.drawSelectedCellNumberHint(canvas, centerX, centerY, num)
            }
        }

        // 繪製數字選擇按鈕
        numberSelectorDisplay.drawNumberButtons(canvas, selectedNumber)
        numberSelectorDisplay.drawClearButton(canvas)

        // 繪製計時器
        val minutes = timeElapsed / 60
        val seconds = timeElapsed % 60
        numberSelectorDisplay.drawTimerDisplay(canvas, minutes, seconds)

        // 檢查是否所有格子填滿，顯示提交按鈕
        if (isAllCellsFilled()) {
            numberSelectorDisplay.drawSubmitButton(canvas)
        }

        // 繪製錯誤訊息
        errorMessage?.let {
            numberDisplay.drawErrorMessage(canvas, width.toFloat(), height.toFloat())
        }

        // 繪製成功訊息
        if (gameFinished && puzzleGenerator.isValidSolution(playerBoard)) {
            numberDisplay.drawSuccessMessage(canvas, width.toFloat(), height.toFloat())
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    handleTouchDown(it.x, it.y)
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleTouchDown(x: Float, y: Float) {
        // 檢查是否點擊數字按鈕
        numberSelector.getNumberAtPosition(x, y)?.let { num ->
            selectedNumber = num
            return
        }

        // 檢查是否點擊清除按鈕
        if (numberSelector.isClearButtonPressed(x, y)) {
            selectedCell?.let { (row, col) ->
                if (puzzleBoard[row][col] == 0) { // 只能清除玩家填入的數字
                    playerBoard[row][col] = 0
                }
            }
            selectedNumber = null
            return
        }

        // 檢查是否點擊提交按鈕
        if (numberSelectorDisplay.isSubmitButtonPressed(x, y)) {
            checkSolution()
            return
        }

        // 檢查是否點擊棋盤格子
        boardRenderer.getCellAtPosition(x, y)?.let { (cellX, cellY) ->
            if (puzzleBoard[cellY][cellX] == 0) { // 只能填入空格
                selectedCell = Pair(cellX, cellY)
                // 自動填入選中的數字
                selectedNumber?.let { num ->
                    if (puzzleGenerator.isValidMove(playerBoard, cellY, cellX, num)) {
                        playerBoard[cellY][cellX] = num
                        errorMessage = null
                    } else {
                        errorMessage = "此數字不符合規則"
                    }
                }
            }
            return
        }

        // 如果遊戲尚未開始，點擊螢幕任何地方開始遊戲
        if (!gameStarted) {
            gameStarted = true
            startTimer()
        }

        // 清除錯誤訊息
        if (errorMessage != null) {
            errorMessage = null
        }
    }

    private fun isAllCellsFilled(): Boolean {
        for (i in 0..8) {
            for (j in 0..8) {
                if (playerBoard[i][j] == 0 && puzzleBoard[i][j] == 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun checkSolution() {
        gameFinished = true
        if (!puzzleGenerator.isValidSolution(playerBoard)) {
            errorMessage = "有錯誤"
            // 30秒後自動隱藏錯誤訊息
            postDelayed({
                errorMessage = null
                gameFinished = false
                invalidate()
            }, 3000)
        }
    }

    private fun startTimer() {
        Thread {
            while (gameStarted && !gameFinished) {
                Thread.sleep(1000)
                timeElapsed++
                post { invalidate() }
            }
        }.start()
    }
}
