package com.example.chitaysostav.domain.model

enum class ProductCategory(val emoji: String, val label: String) {
    CHOCOLATE  ("🍫", "Шоколад / Сладости"),
    DAIRY      ("🥛", "Молочные"),
    MEAT       ("🥩", "Мясо / Колбасы"),
    DRINKS     ("🥤", "Напитки"),
    BREAD      ("🍞", "Хлеб / Выпечка"),
    SNACKS     ("🍟", "Снэки / Чипсы"),
    CANNED     ("🥫", "Консервы"),
    JUICE      ("🧃", "Соки"),
    CEREAL     ("🌾", "Крупы / Злаки"),
    SAUCE      ("🫙", "Соусы / Приправы"),
    BAKERY     ("🧁", "Кондитерские"),
    READY_FOOD ("🥗", "Готовая еда"),
    HOUSEHOLD  ("🧴", "Бытовая химия"),
    SUPPLEMENTS("💊", "БАДы / Витамины"),
    OTHER      ("📦", "Общий товар");

    companion object {
        fun fromEmoji(emoji: String): ProductCategory =
            entries.find { it.emoji == emoji } ?: OTHER
    }
}
