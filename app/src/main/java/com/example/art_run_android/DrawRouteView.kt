package com.example.art_run_android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi

class DrawRouteView(context: Context?) : View(context) {
    private val paint = Paint()
    private val path: Path = Path()
    private var x = 0
    private var y = 0

    private val pathList = mutableListOf<Path>()
    private val redoPathList = mutableListOf<Path>()

    override fun onDraw(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

        canvas.drawPath(path, paint)
        for(p in pathList) {
            canvas.drawPath(p, paint)
        }
    }

    fun undo(){
        if(pathList.isNotEmpty()) {
            path.reset()
            val undoPath = pathList[pathList.lastIndex]
            pathList.remove(undoPath)
            redoPathList.add(undoPath)
            invalidate()
        }
    }

    fun redo(){
        if(redoPathList.isNotEmpty()) {
            val redoPath = redoPathList[redoPathList.lastIndex]
            pathList.add(redoPath)
            redoPathList.remove(redoPath)
            invalidate()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        x = event.x.toInt()
        y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.reset()
                path.moveTo(x.toFloat(), y.toFloat())
            }
            MotionEvent.ACTION_MOVE -> {
                x = event.x.toInt()
                y = event.y.toInt()
                path.lineTo(x.toFloat(), y.toFloat())
            }
            MotionEvent.ACTION_UP -> {
                val currentPath = Path()
                currentPath.addPath(path)
                pathList.add(currentPath)
                redoPathList.clear()
                val pathPoints=path.approximate(0.5F)
            }
        }

        invalidate()
        return true
    }
}