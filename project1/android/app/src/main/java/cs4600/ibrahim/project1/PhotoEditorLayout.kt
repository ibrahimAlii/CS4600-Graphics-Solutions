package cs4600.ibrahim.project1

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.core.widget.ImageViewCompat
import kotlin.math.roundToInt

class PhotoEditorLayout(context: Context, attributes: AttributeSet?) : FrameLayout(context, attributes) {
    private var images = ArrayList<DragableImage>()
    private var lastSelectedPosition = 0
    private var background: DragableImage? = null
    private val TAG = "PhotoEditorLayout"

    private data class Image(var dragableImage: DragableImage, var startingX: Float = 0f, var startingY: Float = 0f, var opacity: Int = 0)

    init {

        init()
        //setWillNotDraw(false)

    }

    private fun init() {
    }

    fun addImage(image: Bitmap) {
        val image = image.copy(Bitmap.Config.ARGB_8888, true)
        val dragableImage = DragableImage(context, image)
        if (background == null) {
            background = dragableImage
            addView(dragableImage, LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.START))
        } else {
            background!!.addView(dragableImage)
        }
        images.add(dragableImage)
        invalidate()
    }

    private fun removeImage(image: DragableImage) {
        images.remove(image)
        if (image == background)
            removeAllViews()
        else removeView(image)

        invalidate()
    }

    fun hideControls() {
        for (image in images) {
            image.close.visibility = View.GONE
            image.opacity.visibility = View.GONE
        }
    }

    fun showControls() {
        for (image in images) {
            image.close.visibility = View.VISIBLE
            image.opacity.visibility = View.VISIBLE
        }
    }

    inner class DragableImage(context: Context, val background: Bitmap) : FrameLayout(context) {
        private val TAG = "PhotoEditorLayout"
        private var isAnimating = false
        private var dx = 0f
        private var dy = 0f
        var image = AppCompatImageView(context)
        val close = AppCompatImageView(context)
        val opacity = SeekBar(context)

        init {
            //setWillNotDraw(false)
            init()
        }

        fun addChildView(dragableImage: DragableImage) {

            // some techniques :

            // Additive Blending
            // c = alphaForeground * colorForeground + colorBackground

            // Difference Blending
            // c = Math.abs(alphaForefround * colorForeground - colorBackground)



            // Multiply Blending: (without alpha)
            // c = colorForeground * colorBackground
            // (with alpha)
            // c = alphaForeground * (colorForeground * colorBackground) + (1 - alphaForeground) * colorBackground

            // Screen Blending
            // c = 1 - (1 - colorForeground) * (1 - colorBackground)


            val fgWidth = dragableImage.background.width
            val fgHeight = dragableImage.background.height
            val bgHeight = background.height
            val bgWidth = background.width

            for (y in 0..fgHeight) {

                for (x in 0..fgWidth) {
                    val fgRed = y * (fgWidth * 4) + x * 4
                    val fgGreen = fgRed + 1
                    val fgBlue = fgRed + 2
                    val fgAlpha = fgRed + 3

                    val bgRed = (y) * (bgWidth * 4) + (x) * 4
                    val bgGreen = bgRed + 1
                    val bgBlue = bgRed + 2
                    val bgAlpha = bgRed + 3


                    background.setPixel(x, y, Color.argb(12, 12, 12, 12))
                }
            }
        }

        private fun init() {
            image.setImageBitmap(background)

            close.background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24)
            close.setOnClickListener {
                removeImage(this)
            }


            opacity.setProgress(100)
            opacity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    image.alpha = progress.toFloat() / 100
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            addView(image, LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.START))
            addView(close, LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.END))
            addView(opacity, LayoutParams(400, WRAP_CONTENT, Gravity.BOTTOM))

            this.animate().setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    isAnimating = false
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationRepeat(animation: Animator?) {}

            })
        }


        override fun onTouchEvent(event: MotionEvent?): Boolean {
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    dx = this.getX() - event.getRawX();
                    dy = this.getY() - event.getRawY();
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d(TAG, "onTouchEvent: Moving (Imagee): ${event.x}, ${event.y}")
                    if (!isAnimating) {
                        this.animate().translationX(event.rawX + dx).translationY(event.rawY + dy).setDuration(10).start()
                        isAnimating = true
                    }

                    //invalidate()
                }
            }

            return true
        }


    }

}