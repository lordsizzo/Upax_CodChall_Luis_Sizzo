package luis.sizzo.upax_codchall_luis_sizzo.common

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import luis.sizzo.upax_codchall_luis_sizzo.R

class Utilities {
    companion object{
        fun getRandomColor(context: Context): Int {
            val allColors = context.resources.getStringArray(R.array.colors)
            val rnds = (allColors.indices).random()
            return Color.parseColor(allColors[rnds])
        }

        fun isStoragePermissionGranted(context: Context): Boolean {
            var condicion = true
            if (Build.VERSION.SDK_INT >= 23) {
                for (i in Dialogs.PERMISSIONS.indices) {
                    if (ContextCompat.checkSelfPermission(context, Dialogs.PERMISSIONS[i]) == PackageManager.PERMISSION_GRANTED) {
                        condicion = true
                    } else {
                        condicion = false
                        break
                    }
                }
            } else {
                condicion = true
            }
            return condicion
        }

        fun createDrawableFromView(context: Context, view: View): Bitmap? {
            val width = context.resources.displayMetrics.widthPixels
            val height = context.resources.displayMetrics.heightPixels
            view.layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            )
            view.measure(width, height)
            view.layout(0, 0, width, height)
            view.buildDrawingCache()
            val bitmap =
                Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }
}