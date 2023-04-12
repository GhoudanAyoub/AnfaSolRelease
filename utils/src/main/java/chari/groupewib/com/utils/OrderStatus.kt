package ghoudan.anfaSolution.com.utils

enum class OrderStatus(val text: Int, val intValue: Int) {
    ENREGISTRE(R.string.cmd_status_1, 1),
    CONFIRME(R.string.cmd_status_2, 2),
    DISPATCHE(R.string.cmd_status_3, 3),
    FACTURE(R.string.cmd_status_4, 4),
    LIVRE(R.string.cmd_status_5, 5),
    RETOURNE(R.string.cmd_status_6, 6),
    ANNULE(R.string.cmd_status_7, 7),
    VALIDE(R.string.cmd_status_8, 8)
}
