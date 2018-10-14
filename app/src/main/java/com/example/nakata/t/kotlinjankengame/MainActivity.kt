package com.example.nakata.t.kotlinjankengame

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity() {

    private val handler = Handler()
    private var timer: Timer = Timer()
    private var comHand: Int = GU
    private var playerHand: Int = GU

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gu_button.setOnClickListener {
            Log.d(TAG, "gu_buttonをクリックしました")
            stopAnimation()
            playerHand = GU
            showResultDialog()
        }

        choki_button.setOnClickListener {
            Log.d(TAG, "choki_buttonをクリックしました")
            stopAnimation()
            playerHand = CHI
            showResultDialog()
        }

        pa_button.setOnClickListener {
            Log.d(TAG, "pa_buttonをクリックしました")
            stopAnimation()
            playerHand = PA
            showResultDialog()
        }

    }

    override fun onResume() {
        super.onResume()
        startAnimation()
    }

    override fun onPause() {
        super.onPause()
        stopAnimation()
    }

    private fun startAnimation() {
        timer = Timer()
        timer.schedule(timerTask {
            handler.post {

                comHand = getRandom()

                when (comHand) {
                    GU -> image.setImageResource(R.drawable.janken_gu)
                    CHI -> image.setImageResource(R.drawable.janken_choki)
                    PA -> image.setImageResource(R.drawable.janken_pa)
                }
            }
        }, INTERVAL, INTERVAL)
    }

    private fun getRandom(): Int {
        return Random().nextInt(3)
    }

    private fun stopAnimation() {
        timer.cancel()
    }

    private fun createMessage(): String {
        var msg = ""
        when (playerHand) {
            GU -> msg += "あなたは『グー』で、"
            CHI -> msg += "あなたは『チョキ』で、"
            PA -> msg += "あなたは『パー』で、"
        }

        when (comHand) {
            GU -> msg += "相手が『グー』で"
            CHI -> msg += "相手が『チョキ』で"
            PA -> msg += "相手が『パー』で"
        }

        msg += judge(playerHand, comHand)

        return msg
    }

    /**
     * ジャンケンの結果を判定する
     * 0 = グー, 1 = チョキ, 2 = パー
     */
    private fun judge(player: Int, com: Int): String {
        if (player == com) {
            return "\nあいこです。"
        }

        val judge = (player - com + 3).rem(3)

        if (judge == 2) {
            return "\nあなたの勝ちです。"
        } else if (judge == 1) {
            return "\nあなたの負けです。"
        } else {
            return ""
        }
    }

    private fun showResultDialog() {
        val positiveButtonListener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                startAnimation()
            }
        }

        AlertDialog.Builder(this)
                .setMessage(createMessage())
                .setPositiveButton("OK", positiveButtonListener)
                .create().show()
    }


    companion object {
        private const val TAG = "MainActivity"
        const val INTERVAL = 100L

        private const val GU = 0
        private const val CHI = 1
        private const val PA = 2
    }
}