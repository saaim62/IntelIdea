package com.example.fypintelidea.core.providers.models

import java.io.Serializable

data class Schedule(
    val id: String? = null,
    val repeated: Boolean = false,
    val repeat_period: String? = null,
    val repeat_after: Int = 0,
    val name1: String? = null,
    val name: String? = null,
    val optionsRadios: OptionsRadios? = null,
    val maximum_occurrences: Int? = null,
    val end: Int? = null,
) : Serializable

enum class OptionsRadios {
    after, never, on,
}
