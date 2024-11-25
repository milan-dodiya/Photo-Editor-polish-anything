package com.example.photoeditorpolishanything.Collage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.photoeditorpolishanything.Utils.CollageModelClass.TemplateItem

class CollagePreviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.BLACK
    }

    private var template: TemplateItem? = null

    fun drawTemplate(templateItem: TemplateItem) {
        template = templateItem
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        template?.let { template ->
            for (photoItem in template.photoItemList) {
                // Convert relative coordinates to actual pixels
                val path = Path()
                val points = photoItem.pointList

                if (points.isNotEmpty()) {
                    path.moveTo(
                        points[0].x * width,
                        points[0].y * height
                    )

                    for (i in 1 until points.size) {
                        path.lineTo(
                            points[i].x * width,
                            points[i].y * height
                        )
                    }

                    path.close()
                    canvas.drawPath(path, paint)
                }
            }
        }
    }
}