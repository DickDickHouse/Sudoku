package com.example.sudoku

/**
 * 數字選擇類別
 * 負責管理玩家選擇的數字以填入棋盤
 */
class NumberSelector {
    private var selectedNumber: Int? = null
    private var selectedCell: Pair<Int, Int>? = null

    companion object {
        const val SELECTOR_TOP = 1550f
        const val SELECTOR_BOTTOM = 1750f
        const val BUTTON_WIDTH = 80f
        const val BUTTON_HEIGHT = 50f
        const val BUTTON_MARGIN = 20f
        const val CLEAR_BUTTON_WIDTH = 100f
    }

    /**
     * 設定選中的數字
     */
    fun selectNumber(number: Int) {
        if (number in 1..9) {
            selectedNumber = number
        }
    }

    /**
     * 設定選中的格子
     */
    fun selectCell(cellX: Int, cellY: Int) {
        selectedCell = Pair(cellX, cellY)
    }

    /**
     * 取消選擇數字
     */
    fun clearNumberSelection() {
        selectedNumber = null
    }

    /**
     * 取消選擇格子
     */
    fun clearCellSelection() {
        selectedCell = null
    }

    /**
     * 取得當前選中的數字
     */
    fun getSelectedNumber(): Int? {
        return selectedNumber
    }

    /**
     * 取得當前選中的格子
     */
    fun getSelectedCell(): Pair<Int, Int>? {
        return selectedCell
    }

    /**
     * 取得數字按鈕在虛擬座標中的位置
     */
    fun getNumberButtonPosition(number: Int): Pair<Float, Float>? {
        if (number !in 1..9) {
            return null
        }

        val column = (number - 1) % 5
        val row = (number - 1) / 5

        val left = 100f + (column * (BUTTON_WIDTH + BUTTON_MARGIN))
        val top = SELECTOR_TOP + (row * (BUTTON_HEIGHT + BUTTON_MARGIN))

        val centerX = left + (BUTTON_WIDTH / 2)
        val centerY = top + (BUTTON_HEIGHT / 2)

        return Pair(centerX, centerY)
    }

    /**
     * 取得數字按鈕的矩形邊界
     */
    fun getNumberButtonBounds(number: Int): com.example.sudoku.ButtonBounds? {
        if (number !in 1..9) {
            return null
        }

        val column = (number - 1) % 5
        val row = (number - 1) / 5

        val left = 100f + (column * (BUTTON_WIDTH + BUTTON_MARGIN))
        val top = SELECTOR_TOP + (row * (BUTTON_HEIGHT + BUTTON_MARGIN))

        return ButtonBounds(left, top, left + BUTTON_WIDTH, top + BUTTON_HEIGHT)
    }

    /**
     * 檢查點擊位置是否在數字按鈕上
     */
    fun getNumberAtPosition(x: Float, y: Float): Int? {
        for (num in 1..9) {
            val bounds = getNumberButtonBounds(num)
            if (bounds != null && x >= bounds.left && x <= bounds.right &&
                y >= bounds.top && y <= bounds.bottom) {
                return num
            }
        }
        return null
    }

    /**
     * 檢查點擊位置是否在清除按鈕上
     */
    fun isClearButtonPressed(x: Float, y: Float): Boolean {
        val clearLeft = 600f
        val clearTop = SELECTOR_TOP
        val clearRight = clearLeft + CLEAR_BUTTON_WIDTH
        val clearBottom = clearTop + BUTTON_HEIGHT

        return x >= clearLeft && x <= clearRight && y >= clearTop && y <= clearBottom
    }

    /**
     * 取得清除按鈕的位置
     */
    fun getClearButtonBounds(): ButtonBounds {
        val clearLeft = 600f
        val clearTop = SELECTOR_TOP
        return ButtonBounds(clearLeft, clearTop, clearLeft + CLEAR_BUTTON_WIDTH, clearTop + BUTTON_HEIGHT)
    }
}

/**
 * 按鈕邊界資料類別
 */
data class ButtonBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)
