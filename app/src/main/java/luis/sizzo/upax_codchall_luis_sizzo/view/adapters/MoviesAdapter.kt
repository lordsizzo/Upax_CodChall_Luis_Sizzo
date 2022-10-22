package luis.sizzo.upax_codchall_luis_sizzo.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import luis.sizzo.upax_codchall_luis_sizzo.R
import luis.sizzo.upax_codchall_luis_sizzo.common.URL_IMAGE
import luis.sizzo.upax_codchall_luis_sizzo.common.toast
import luis.sizzo.upax_codchall_luis_sizzo.databinding.ItemsMoviesBinding
import luis.sizzo.upax_codchall_luis_sizzo.model.local.movies_local.MoviesLocalEntity

class MoviesAdapter(private val items: List<MoviesLocalEntity>) :
    RecyclerView.Adapter<MoviesAdapter.CategoriesViewHolder>() {

    class CategoriesViewHolder(val binding: ItemsMoviesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            ItemsMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        try {
            holder.itemView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rotate)
            holder.binding.title.text = items[position].title
            holder.binding.image.load("${URL_IMAGE}${items[position].poster_path}")
            holder.binding.date.text = items[position].release_date

        } catch (e: Exception) {

            holder.binding.root.context.toast(e.toString())
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }
}