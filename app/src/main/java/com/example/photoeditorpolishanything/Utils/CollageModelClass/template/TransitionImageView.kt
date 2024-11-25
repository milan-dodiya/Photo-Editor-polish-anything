package com.example.photoeditorpolishanything.Utils.CollageModelClass.template

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import com.example.photoeditorpolishanything.Utils.ImageDecoder
import com.example.photoeditorpolishanything.Utils.ImageUtils

class TransitionImageView(context: Context) : androidx.appcompat.widget.AppCompatImageView(context) {

    private val mGestureDetector: GestureDetector
    private val mImageMatrix: Matrix
    val scaleMatrix: Matrix
    var image: Bitmap? = null
        private set
    private val mPaint: Paint
    private var mOnImageClickListener: OnImageClickListener? = null
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    private var mScale = 1f

    interface OnImageClickListener {
        fun onLongClickImage(view: TransitionImageView)

        fun onDoubleClickImage(view: TransitionImageView)
    }

    init {
        scaleType = ImageView.ScaleType.MATRIX
        mPaint = Paint()
        mPaint.isFilterBitmap = true
        mPaint.isAntiAlias = true
        mImageMatrix = Matrix()
        scaleMatrix = Matrix()
        mGestureDetector =
            GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener!!.onLongClickImage(this@TransitionImageView)
                    }
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener!!.onDoubleClickImage(this@TransitionImageView)
                    }
                    return true
                }
            })
    }

    fun reset() {
        scaleType = ImageView.ScaleType.MATRIX
    }

    fun setOnImageClickListener(onImageClickListener: OnImageClickListener) {
        mOnImageClickListener = onImageClickListener
    }

    fun recycleImages() {
        if (image != null && !image!!.isRecycled) {
            image!!.recycle()
            image = null
            System.gc()
            invalidate()
        }
    }

    fun setImagePath(path: String) {
        recycleImages()
        val image = ImageDecoder.decodeFileToBitmap(path)
        if (image != null)
            init(image, mViewWidth, mViewHeight, mScale)
    }

    fun init(image: Bitmap, viewWidth: Int, viewHeight: Int, scale: Float) {
        this.image = image
        mViewWidth = viewWidth
        mViewHeight = viewHeight
        mScale = scale
        if (this.image != null) {
            mImageMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    viewWidth.toFloat(),
                    viewHeight.toFloat(),
                    this.image!!.width.toFloat(),
                    this.image!!.height.toFloat()
                )
            )

            scaleMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    scale * viewWidth,
                    scale * viewHeight,
                    this.image!!.width.toFloat(),
                    this.image!!.height.toFloat()
                )
            )
        }



        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (image != null && !image!!.isRecycled) {
            canvas.drawBitmap(image!!, mImageMatrix, mPaint)
        }
    }



    override fun getImageMatrix(): Matrix {
        return mImageMatrix
    }
}
