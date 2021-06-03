package co.cartrack.za.model

data class OmdbResponse(
    val Response: String,
    val Search: ArrayList<Search>,
    val totalResults: String
)