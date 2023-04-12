package ghoudan.anfaSolution.com.networking.entity.enums

enum class CustomersStatus(val status: Int) {
    EN_ATTENTE(1),
    VALIDE(2),
    ACTIVE(3),
    DESACTIVE(4),
    INJOIGNABLE(5),
    OCCUPE(6),
    A_ACTIVER_PLUS_TARD(7),
    HORS_CIBLE(8),
    AWAITING_INFORMATION(9)
}
