package co.cartrack.za.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "search_results_tbl"
)
data class Search(

    @PrimaryKey(autoGenerate = false)
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String

): Serializable