package co.cartrack.za.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.cartrack.za.R
import co.cartrack.za.model.Search
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_layout.view.*

class OmdbAdapter : RecyclerView.Adapter<OmdbAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem.Poster == newItem.Poster
        }

        override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Search) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchResults = differ.currentList[position]
        holder.itemView.apply {

            tvTitle.text = searchResults.Title
            tvYear.text = searchResults.Year

            Glide.with(imgPoster)
                .load(searchResults.Poster)
                .placeholder(R.mipmap.place_holder)
                .error(R.mipmap.place_holder)
                .fallback(R.mipmap.place_holder)
                .into(imgPoster)

            setOnClickListener {
                onItemClickListener?.let { it(searchResults) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Search) -> Unit) {
        onItemClickListener = listener
    }

}