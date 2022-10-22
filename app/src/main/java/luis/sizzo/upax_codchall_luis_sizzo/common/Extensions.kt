package luis.sizzo.upax_codchall_luis_sizzo.common

import android.content.Context
import android.text.*
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import luis.sizzo.upax_codchall_luis_sizzo.view.adapters.MoviesAdapter

fun RecyclerView.settingsGrid(adapter: MoviesAdapter){
    this.layoutManager = GridLayoutManager(this.context, 3)
    this.adapter = adapter
}

fun View.fadeVisibility(visibility: Int, duration: Long = 300) {
    val transition: Transition = Fade()
    transition.duration = duration
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.visibility = visibility
}
fun GoogleMap.clickMap(listener: (LatLng) -> Unit){
    this.setOnMapClickListener{
        listener(it)
    }
}
fun Context.toast(message: String, lenght: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, lenght).show()
}

fun View.snack(message: String, lenght: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(this, message, lenght).show()
}

fun View.click(listener: (View) -> Unit){
    this.setOnClickListener{
        listener(it)
    }
}

fun AppCompatEditText.textWatcher(listener: (CharSequence) -> Unit){
    this.addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            listener(s)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun afterTextChanged(s: Editable) {

        }
    })
}
