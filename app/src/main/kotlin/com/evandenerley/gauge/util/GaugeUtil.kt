package com.evandenerley.gauge.util

import android.graphics.Paint
import android.graphics.Rect

import hugo.weaving.DebugLog

/**
 * Created by Evan on 2/18/15.
 */
class GaugeUtil private constructor(private val mScreenDensity: Float) {
    private val mReadoutPaint: Paint


    init {
        mReadoutPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mReadoutPaint.style = Paint.Style.FILL_AND_STROKE
        mReadoutPaint.textSize = 100f
    }

    fun getOutlinePaint(colorRes: Int): Paint {
        //Box outline
        val dimensionOutlinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dimensionOutlinePaint.color = colorRes
        dimensionOutlinePaint.style = Paint.Style.STROKE
        dimensionOutlinePaint.strokeWidth = STROKE_WIDTH * mScreenDensity

        return dimensionOutlinePaint
    }

    fun getTickMarkPaint(colorRes: Int): Paint {
        //Tick Mark Paint for the whole gauge range
        val tickMarkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        tickMarkPaint.color = colorRes
        tickMarkPaint.style = Paint.Style.STROKE
        tickMarkPaint.strokeWidth = STROKE_WIDTH * mScreenDensity

        return tickMarkPaint
    }

    @DebugLog
    fun getScaledReadoutPaint(readoutString: String, readoutRect: Rect, colorRes: Int): Paint {
        mReadoutPaint.color = colorRes
        val textWidth = mReadoutPaint.measureText(readoutString)
        if (textWidth < readoutRect.width()) {
            return scaleReadoutTextSizeUp(readoutString, textWidth, readoutRect.width().toFloat(),
                    readoutRect.height().toFloat())
        } else if (textWidth > readoutRect.width()) {
            return scaleReadoutTextSizeDown(readoutString, textWidth, readoutRect.width().toFloat(),
                    readoutRect.height().toFloat())
        } else {
            return mReadoutPaint
        }
    }

    private fun scaleReadoutTextSizeUp(
            readoutString: String, textWidth: Float,
            readoutWidth: Float, readoutHeight: Float): Paint {
        if (textWidth + GAUGE_TEXT_HORIZONTAL_PADDING >= readoutWidth) {
            //we're really close, return the paint since is a small threshold away
            return mReadoutPaint
        } else {
            mReadoutPaint.textSize = mReadoutPaint.textSize + 1
            val bounds = Rect()
            mReadoutPaint.getTextBounds(readoutString, 0, readoutString.length, bounds)
            if (bounds.height() > readoutHeight) {
                mReadoutPaint.textSize = mReadoutPaint.textSize - 1
                return mReadoutPaint
            }
            return scaleReadoutTextSizeUp(readoutString, mReadoutPaint.measureText(readoutString),
                    readoutWidth, readoutHeight)
        }
    }

    private fun scaleReadoutTextSizeDown(
            readoutString: String, textWidth: Float, readoutWidth: Float,
            readoutHeight: Float): Paint {
        if (textWidth + GAUGE_TEXT_HORIZONTAL_PADDING <= readoutWidth) {
            return mReadoutPaint
        } else {
            mReadoutPaint.textSize = mReadoutPaint.textSize - 1
            val bounds = Rect()
            mReadoutPaint.getTextBounds(readoutString, 0, readoutString.length, bounds)
            if (bounds.height() < readoutHeight) {
                mReadoutPaint.textSize = mReadoutPaint.textSize + 1
                return mReadoutPaint
            }
            return scaleReadoutTextSizeUp(readoutString, mReadoutPaint.measureText(readoutString),
                    readoutWidth, readoutHeight)
        }
    }

    companion object {

        private val GAUGE_TEXT_HORIZONTAL_PADDING = 3f
        private val STROKE_WIDTH = 2f
        private val LARGE_STROKE_WIDTH = 10f

        fun newInstance(screenDensity: Float): GaugeUtil {
            return GaugeUtil(screenDensity)
        }
    }


}
