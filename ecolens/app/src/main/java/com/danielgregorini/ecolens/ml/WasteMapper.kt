package com.danielgregorini.ecolens.ml

data class WasteInfo(
    val pt: String,
    val bin: String
)

val wasteMap = mapOf(
    "Cardboard" to WasteInfo("Papelão", "Lixeira Azul"),
    "Paper" to WasteInfo("Papel", "Lixeira Azul"),
    "Plastic" to WasteInfo("Plástico", "Lixeira Amarela"),
    "Metal" to WasteInfo("Metal", "Lixeira Amarela"),
    "Glass" to WasteInfo("Vidro", "Lixeira Verde"),
    "Food Organics" to WasteInfo("Orgânico", "Lixeira Castanha"),
    "Vegetation" to WasteInfo("Orgânico", "Lixeira Castanha"),
    "Textile Trash" to WasteInfo("Têxtil", "Ponto de recolha têxtil"),
    "Miscellaneous Trash" to WasteInfo("Indiferenciado", "Lixeira Cinzenta")
)
