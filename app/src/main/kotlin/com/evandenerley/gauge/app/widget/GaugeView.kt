package com.evandenerley.gauge.app.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.support.v4.content.ContextCompat.getColor
import android.util.AttributeSet
import android.widget.FrameLayout
import com.evandenerley.gauge.R
import com.evandenerley.gauge.util.GaugeUtil
import hugo.weaving.DebugLog

/**
 * A Custom view that handles displaying a Gauge and its content.
 */
class GaugeView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {

        private val GAUGE_TEXT_BOTTOM_MARGIN = 16F
        private val GAUGE_TICK_PADDING = 40F
        private val GAUGE_OUTER_VIEW_PADDING = 60F
        // 300 degrees in a circle looks right for now
        val TICK_KERNING = 150
        // minimum and maximum values for the Gauge in degrees
        val MAX_GAUGE_ANGLE = 188
        val MIN_GAUGE_ANGLE = -37
        val MIN_ACCEPTABLE_RANGE = 30
        val MAX_ACCEPTABLE_RANGE = 60
    }

    //Get Paints from here
    private val gaugeUtil: GaugeUtil
    //Radii values calculated as padding from the smallest square width
    private var innerTickRadius: Float = 0F
    private var outerTickRadius: Float = 0F
    private var meterRadius: Float = 0F

    private var layoutRect: Rect? = null
    private var tickMarkerPaint: Paint? = null
    private var acceptedRangePaint: Paint? = null
    private var dimensionOutlinePaint: Paint? = null

    private var centerX: Float = 0F
    private var centerY: Float = 0F

    private val readoutString = "-90000"

    init {
        setWillNotDraw(false)
        gaugeUtil = GaugeUtil.newInstance(resources.displayMetrics.density)
    }

    override
            /**
             * Calculate the bounds of this layout and save them in mLayoutRect.
             */
    fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            initGaugeLayoutCenter(left, top, right, bottom)
            initPaint()
            initializeGuageRadii(layoutRect!!)
        }
    }

    private fun initPaint() {
        val context = context;
        dimensionOutlinePaint = gaugeUtil.getOutlinePaint(
                getColor(context, R.color.common_deep_purple))
        tickMarkerPaint = gaugeUtil.getTickMarkPaint(
                getColor(context, R.color.common_text_secondary_inverse))
        acceptedRangePaint = gaugeUtil.getTickMarkPaint(getColor(context, android.R.color.white))
    }

    /**
     * Initialize the radii used to draw this view.
     */
    @DebugLog
    private fun initializeGuageRadii(layoutRect: Rect) {
        outerTickRadius = calculateSmallestRadius(layoutRect) - GAUGE_OUTER_VIEW_PADDING
        innerTickRadius = outerTickRadius - GAUGE_TICK_PADDING
        meterRadius = innerTickRadius - (2f * GAUGE_TICK_PADDING)
    }

    /**
     * Initialize the center point of this view.
     */
    private fun initGaugeLayoutCenter(left: Int, top: Int, right: Int, bottom: Int) {
        layoutRect = Rect(left, top, right, bottom)
        centerX = layoutRect!!.width() / 2f
        centerY = layoutRect!!.height() / 2f
    }

    /**
     * Calculate the smallest radius of the largest square that fits in the width/height of this
     * view.

     * @param layoutRect the bounds of this view
     */
    @DebugLog
    private fun calculateSmallestRadius(layoutRect: Rect): Float {
        return (Math.min(layoutRect.width(), layoutRect.height()) / 2f)
    }

    /**
     * Calculate the bounds of a square based of the given radius.

     * @param radius radius of the circle
     * *
     * @return Rectangle
     */
    private fun calculateRadialSquare(radius: Float): Rect {
        val left = (centerX - radius).toInt()
        val top = (centerY - radius).toInt()
        val right = (centerX + radius).toInt()
        val bottom = (centerY + radius).toInt()

        return Rect(left, top, right, bottom)
    }

    /**
     * Calculate the bounds of a square that fits inside a circle based of the given radius.

     * @param radius radius of the circle
     * *
     * @return Rectangle
     */
    private fun calculateInnerRadialSquare(radius: Float): Rect {
        val factor = (Math.sqrt(2.0) / 2).toFloat()
        val left = (centerX - (radius * factor)).toInt()
        val top = (centerY - (radius * factor)).toInt()
        val right = (centerX + (radius * factor)).toInt()
        val bottom = (centerY + (radius * factor)).toInt()

        return Rect(left, top, right, bottom)
    }

    /**
     * Calculate the bounds of the Readout content rectangle.

     * @param radius radius of the inner circle
     * *
     * @return SubReading Rectangle
     */
    private fun calculateReadoutRect(radius: Float): Rect {
        // normal radial length from center
        val factor = (Math.sqrt(2.0) / 2).toFloat()
        val half_factor = factor / 2

        val left = (centerX - (radius * factor)).toInt()
        val top = (centerY - (radius * factor)).toInt()
        val right = (centerX + (radius * factor)).toInt()
        val bottom = (centerY + (radius * half_factor)).toInt()

        return Rect(left, top, right, bottom)
    }

    /**
     * Calculate the bounds of the SubReading content rectangle.

     * @param radius radius of the inner circle
     * *
     * @return SubReading Rectangle
     */
    private fun calculateSubReadoutRect(radius: Float): Rect {
        // normal radial length from center
        val factor = (Math.sqrt(2.0) / 2).toFloat()
        val half_factor = factor / 2

        val left = (centerX - (radius * factor)).toInt()
        val top = (centerY + (radius * half_factor)).toInt()
        val right = (centerX + (radius * factor)).toInt()
        val bottom = (centerY + (radius * factor)).toInt()

        return Rect(left, top, right, bottom)
    }


    private fun drawGaugeTicks(canvas: Canvas) {
        val tickPath = Path()
        val acceptableTickRange = Path()

        //I like to do my circle math with 0 facing directly East!
        for (i in MIN_GAUGE_ANGLE..MAX_GAUGE_ANGLE - 1) {
            val theta = (i * Math.PI / TICK_KERNING).toFloat()
            val startX = (innerTickRadius * Math.cos(theta.toDouble())).toFloat()
            val startY = (innerTickRadius * -Math.sin(theta.toDouble())).toFloat()
            val endX = (outerTickRadius * Math.cos(theta.toDouble())).toFloat()
            val endY = (outerTickRadius * -Math.sin(theta.toDouble())).toFloat()

            tickPath.moveTo(centerX + startX, centerY + startY)
            tickPath.lineTo(centerX + endX, centerY + endY)

            if (i > MIN_ACCEPTABLE_RANGE && i < MAX_ACCEPTABLE_RANGE) {
                acceptableTickRange.moveTo(centerX + startX, centerY + startY)
                acceptableTickRange.lineTo(centerX + endX, centerY + endY)
            }
        }
        canvas.drawPath(tickPath, tickMarkerPaint)
        canvas.drawPath(acceptableTickRange, acceptedRangePaint)
    }

    /**
     * Draw the Temperature Readout.

     * @param canvas canvas to draw on
     */
    private fun drawTemperatureReadout(canvas: Canvas) {
        val readoutRect = calculateReadoutRect(meterRadius)
        canvas.drawText(readoutString, readoutRect.left.toFloat(),
                centerY - GAUGE_TEXT_BOTTOM_MARGIN, gaugeUtil.getScaledReadoutPaint(readoutString,
                readoutRect, getColor(context, R.color.common_red_dark)))
    }

    /**
     * Draw the outline of the Readout content boxes.

     * @param canvas Screen canvas
     */
    private fun drawReadoutContentOutlines(canvas: Canvas) {
        canvas.drawRect(calculateReadoutRect(meterRadius), dimensionOutlinePaint)
        canvas.drawRect(calculateSubReadoutRect(meterRadius), dimensionOutlinePaint)
    }

    /**
     * Draw the outline of the Readout content boxes.

     * @param canvas Screen canvas
     */
    private fun drawGaugeOutline(canvas: Canvas) {
        val readoutRect = calculateRadialSquare(outerTickRadius)
        canvas.drawRect(readoutRect, gaugeUtil.getOutlinePaint(
                resources.getColor(R.color.common_indigo)))
    }

    /**
     * Draw the outline of the Readout content boxes.

     * @param canvas Screen canvas
     */
    private fun drawInnerSquareOutline(canvas: Canvas) {
        val readoutRect = calculateInnerRadialSquare(innerTickRadius)
        canvas.drawRect(readoutRect, gaugeUtil.getOutlinePaint(
                resources.getColor(R.color.common_deep_purple_dark)))
    }

    /**
     * Draw a boarder around this entire custom view's bounds.

     * @param canvas Screen canvas
     */
    private fun drawViewLayout(canvas: Canvas) {
        canvas.drawRect(layoutRect, gaugeUtil.getOutlinePaint(
                resources.getColor(R.color.common_green)))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawViewLayout(canvas)
        drawGaugeOutline(canvas)
        drawInnerSquareOutline(canvas)
        drawReadoutContentOutlines(canvas)

        drawGaugeTicks(canvas)
        drawTemperatureReadout(canvas)
    }
}
