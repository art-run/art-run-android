package com.example.art_run_android.running

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

class DrawRouteView(context: Context?) : View(context) {
    private val paint = Paint()
    private val path: Path = Path()
    private var x = 0
    private var y = 0

    lateinit var strokeListener: StrokeListener

    override fun onDraw(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

        canvas.drawPath(path, paint)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        x = event.x.toInt()
        y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x.toFloat(), y.toFloat())
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x.toFloat(), y.toFloat())
            }
            MotionEvent.ACTION_UP -> {
                val pathPoints = path.approximate(0.5F)
                val pointsList = mutableListOf<Point>().apply {
                    for (i in pathPoints.indices step (3)) {
                        this.add(Point(pathPoints[i + 1].toInt(), pathPoints[i + 2].toInt()))
                    }
                }
                optimizePoints(pointsList)
                strokeListener.onStrokeEvent(pointsList)
                path.reset()
            }
        }

        invalidate()
        return true
    }

    private fun optimizePoints(pointList: MutableList<Point>) {
        var i = 0
        while (i < pointList.size - 2) {
            if (isLine(pointList[i], pointList[i + 1], pointList[i + 2])) {
                pointList.removeAt(i+1)
                i--
            }
            i++
        }
    }

    private fun isLine(p1: Point, p2: Point, p3: Point): Boolean {

        val a = sqrt(
            (p1.x.toDouble() - p3.x.toDouble()).pow(2.0)
                    + (p1.y.toDouble() - p3.y.toDouble()).pow(2.0)
        )
        val b = sqrt(
            (p1.x.toDouble() - p2.x.toDouble()).pow(2.0)
                    + (p1.y.toDouble() - p2.y.toDouble()).pow(2.0)
        )
        val c = sqrt(
            (p2.x.toDouble() - p3.x.toDouble()).pow(2.0)
                    + (p2.y.toDouble() - p3.y.toDouble()).pow(2.0)
        )

        val temp = (b.pow(2.0) + c.pow(2.0) - a.pow(2.0)) / (2 * b * c)
        var ang = acos(temp)
        ang *= (180.0 / Math.PI)

        return (ang in 160.0..200.0)
    }

    interface StrokeListener {
        fun onStrokeEvent(pointList: MutableList<Point>)
    }

    fun SetStrokeListener(listener: StrokeListener) {
        strokeListener = listener
    }
}

