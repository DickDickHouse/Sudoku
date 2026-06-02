package com.example.sudoku

/**
 * 謎題產生類別
 * 負責生成 Sudoku 謎題、驗證解答等
 */
class PuzzleGenerator {
    private val board = Array(9) { IntArray(9) }
    private val solution = Array(9) { IntArray(9) }

    init {
        generatePuzzle()
    }

    /**
     * 生成完整的 Sudoku 謎題
     */
    fun generatePuzzle() {
        // 清空棋盤
        for (i in 0..8) {
            for (j in 0..8) {
                board[i][j] = 0
                solution[i][j] = 0
            }
        }

        // 生成完整解答
        generateSolution()

        // 複製為謎題
        for (i in 0..8) {
            for (j in 0..8) {
                solution[i][j] = board[i][j]
            }
        }

        // 隨機移除數字以建立謎題
        removeNumbers()
    }

    /**
     * 遞迴生成完整的 Sudoku 解答
     */
    private fun generateSolution() {
        val numbers = (1..9).toMutableList().shuffled()

        for (row in 0..8) {
            for (col in 0..8) {
                if (board[row][col] == 0) {
                    for (num in numbers) {
                        if (isValidPlacement(board, row, col, num)) {
                            board[row][col] = num
                            if (generateSolution()) {
                                return true
                            }
                            board[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    /**
     * 驗證數字放置是否有效
     */
    private fun isValidPlacement(grid: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        // 檢查行
        for (j in 0..8) {
            if (grid[row][j] == num) {
                return false
            }
        }

        // 檢查列
        for (i in 0..8) {
            if (grid[i][col] == num) {
                return false
            }
        }

        // 檢查 3x3 方格
        val boxRow = (row / 3) * 3
        val boxCol = (col / 3) * 3
        for (i in boxRow until boxRow + 3) {
            for (j in boxCol until boxCol + 3) {
                if (grid[i][j] == num) {
                    return false
                }
            }
        }

        return true
    }

    /**
     * 隨機移除數字以建立謎題難度
     */
    private fun removeNumbers() {
        val cellsToRemove = 40 // 移除 40 個數字，難度適中
        var removed = 0

        while (removed < cellsToRemove) {
            val row = (0..8).random()
            val col = (0..8).random()

            if (board[row][col] != 0) {
                val temp = board[row][col]
                board[row][col] = 0

                // 檢查是否只有唯一解
                if (countSolutions(board) == 1) {
                    removed++
                } else {
                    // 還原數字，繼續嘗試
                    board[row][col] = temp
                }
            }
        }
    }

    /**
     * 計算謎題的解答數量
     */
    private fun countSolutions(grid: Array<IntArray>): Int {
        val tempGrid = grid.map { it.copyOf() }.toTypedArray()
        return countSolutionsHelper(tempGrid)
    }

    /**
     * 遞迴計算解答數量的輔助函數
     */
    private fun countSolutionsHelper(grid: Array<IntArray>): Int {
        for (row in 0..8) {
            for (col in 0..8) {
                if (grid[row][col] == 0) {
                    var count = 0
                    for (num in 1..9) {
                        if (isValidPlacement(grid, row, col, num)) {
                            grid[row][col] = num
                            count += countSolutionsHelper(grid)
                            if (count > 1) {
                                grid[row][col] = 0
                                return count
                            }
                            grid[row][col] = 0
                        }
                    }
                    return count
                }
            }
        }
        return 1 // 找到完整解答
    }

    /**
     * 取得謎題棋盤
     */
    fun getPuzzleBoard(): Array<IntArray> {
        return board.map { it.copyOf() }.toTypedArray()
    }

    /**
     * 取得完整解答
     */
    fun getSolution(): Array<IntArray> {
        return solution.map { it.copyOf() }.toTypedArray()
    }

    /**
     * 驗證玩家填入的棋盤是否正確
     */
    fun isValidSolution(playerBoard: Array<IntArray>): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (playerBoard[row][col] != solution[row][col]) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 驗證整個棋盤是否已填滿
     */
    fun isBoardFull(playerBoard: Array<IntArray>): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                if (playerBoard[row][col] == 0) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 檢查特定數字放置在位置是否有效
     */
    fun isValidMove(playerBoard: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        // 檢查行
        for (j in 0..8) {
            if (j != col && playerBoard[row][j] == num) {
                return false
            }
        }

        // 檢查列
        for (i in 0..8) {
            if (i != row && playerBoard[i][col] == num) {
                return false
            }
        }

        // 檢查 3x3 方格
        val boxRow = (row / 3) * 3
        val boxCol = (col / 3) * 3
        for (i in boxRow until boxRow + 3) {
            for (j in boxCol until boxCol + 3) {
                if ((i != row || j != col) && playerBoard[i][j] == num) {
                    return false
                }
            }
        }

        return true
    }
}
