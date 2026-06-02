package com.example.sudoku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 主 Activity 類別
 * 為 Sudoku 遊戲應用程式的入口
 */
class MainActivity : AppCompatActivity() {
    private lateinit var gameView: SudokuGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 建立遊戲檢視
        gameView = SudokuGameView(this)
        setContentView(gameView)

        // 隱藏系統 UI（狀態欄和導航欄）以獲得全屏體驗
        hideSystemUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }
}
