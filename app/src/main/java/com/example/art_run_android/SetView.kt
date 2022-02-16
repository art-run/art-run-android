import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

class SetView(context: Context?) : View(context) {
    private val paint = Paint()
    private val path: Path = Path()
    private var x = 0
    private var y = 0
    override fun onDraw(canvas: Canvas) {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f

        canvas.drawPath(path, paint)
    }

    fun undo(){

    }

    fun redo(){

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        x = event.x.toInt()
        y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> path.moveTo(x.toFloat(), y.toFloat())
            MotionEvent.ACTION_MOVE -> {
                x = event.x.toInt()
                y = event.y.toInt()
                path.lineTo(x.toFloat(), y.toFloat())
            }
        }

        invalidate()
        return true
    }
}