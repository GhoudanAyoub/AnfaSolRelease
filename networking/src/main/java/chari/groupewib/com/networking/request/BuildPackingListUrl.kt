package chari.groupewib.com.networking.request

data class BuildPackingListUrl(
    val etag: String,
    val documentNo: String,
    val codeFour: String,
    val ligneAch: Int,
    val noArt: String,
    val numColis: Int,
    val indice: Int = 0,
) {
    fun getUrl(): String {
        val baseUrl = "http://105.73.203.245:7548/ABFISH_Device/ODataV4/Company('ABFISH-140')"
        return "$baseUrl/PackingList(" +
                "Document_No='$documentNo'," +
                "Code_four='$codeFour'," +
                "Ligne_ach=$ligneAch," +
                "No_Art='$noArt'," +
                "Num_Colis=$numColis," +
                "indice=$indice" +
                ")"
    }
}
