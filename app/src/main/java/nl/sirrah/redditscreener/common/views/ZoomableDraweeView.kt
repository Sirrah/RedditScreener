package nl.sirrah.redditscreener.common.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.WindowManager
import com.facebook.drawee.view.SimpleDraweeView

/**
 * A SimpleDraweeView that supports Pinch to zoom.

 * This class is converted to Kotlin from https://gist.github.com/nbarraille/eb5d0da20bc813969b08
 */
class ZoomableDraweeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : SimpleDraweeView(context, attrs, defStyle) {
    private val scaleDetector: ScaleGestureDetector
    private val scaleListener: ScaleGestureDetector.OnScaleGestureListener
    private val gestureDetector: GestureDetector

    private val currentMatrix = Matrix()

    private var currentScale = 0f
    private var midX = 0f
    private var midY = 0f

    /**
     * Setting the actionBarHeight is used when the actionbar is used in the application it will be
     * used in calculating the y-transaction on panning, to be able to calculate the bottom of the
     * screen correctly
     */
    var actionBarHeight: Int = 0

    var onZoomChangeListener: OnZoomChangeListener? = null

    init {
        scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                var scaleFactor = detector.scaleFactor
                val newScale = currentScale * scaleFactor

                // Prevent from zooming out more than original
                if (newScale > 1.0f && newScale < 4.0f) {
                    // We initialize this lazily so that we don't have to register (and force the user
                    // to unregister) a global layout listener on the view.
                    if (midX == 0.0f) {
                        midX = width / 2.0f
                    }
                    if (midY == 0.0f) {
                        midY = height / 2.0f
                    }
                    currentScale = newScale
                    currentMatrix.postScale(scaleFactor, scaleFactor, midX, midY)
                    invalidate()
                } else if (newScale < 1.0f) {
                    scaleFactor = 1.0f / currentScale
                    reset()
                }

                if (onZoomChangeListener != null && scaleFactor != 1.0f) {
                    onZoomChangeListener!!.onZoomChange(currentScale)
                }

                return true
            }
        }
        scaleDetector = ScaleGestureDetector(context, scaleListener)

        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onZoomChangeListener = null
    }

    override fun onDraw(canvas: Canvas) {
        val saveCount = canvas.save()
        canvas.concat(currentMatrix)
        super.onDraw(canvas)
        canvas.restoreToCount(saveCount)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        if (!scaleDetector.isInProgress) {
            gestureDetector.onTouchEvent(event)
        }
        super.onTouchEvent(event)
        return true
    }

    /**
     * Resets the zoom of the attached image. This has no effect if the image has been destroyed
     */
    fun reset() {
        currentMatrix.reset()
        currentScale = 1.0f
        invalidate()
    }

    internal inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            var dx = distanceX
            var dy = distanceY
            //Only scroll when we are zoomed in
            if (currentScale > 1) {
                val values = FloatArray(9)
                currentMatrix.getValues(values)

                //Get the metrics for the display, used to detect edges for scrolling
                val displayMetrics = DisplayMetrics()
                val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                manager.defaultDisplay.getMetrics(displayMetrics)

                //Find the left and right limit for scrolling
                if (dx < 0 && values[Matrix.MTRANS_X] > 0 || dx > 0 && values[Matrix.MTRANS_X] < -1 * (width * values[Matrix.MSCALE_X]) + displayMetrics.widthPixels) {
                    dx = 0f
                }

                //Find the top and bottom limit for scrolling
                if (dy < 0 && values[Matrix.MTRANS_Y] > 0 || dy > 0 && values[Matrix.MTRANS_Y] < -1 * (height * values[Matrix.MSCALE_Y]) + (displayMetrics.heightPixels - actionBarHeight)) {
                    dy = 0f
                }

                currentMatrix.postTranslate(-dx, -dy)
                invalidate()

                return true
            }

            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val scaleFactor = 2.0f
            if (currentScale == 0f) {
                currentScale = 2.0f
                currentMatrix.postScale(scaleFactor, scaleFactor, midX, midY)
            } else if (currentScale < 4.0f) {
                currentScale = 4.0f
                currentMatrix.postScale(scaleFactor, scaleFactor, midX, midY)
            } else {
                currentScale = 0f
                currentMatrix.reset()
            }

            invalidate()
            return true
        }
    }

    /**
     * A listener interface for when the zoom scale changes
     */
    interface OnZoomChangeListener {
        /**
         * Callback method getting triggered when the zoom scale changes. This is not called when
         * the zoom is programmatically reset
         */
        fun onZoomChange(scale: Float)
    }
}
