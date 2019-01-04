package com.ataulm.chunks

import java.util.*

data class Id(val value: UUID) {

    companion object {

        fun create(): Id {
            val uuid = UUID.randomUUID()
            return Id(uuid)
        }

        fun createFrom(rawUuid: String): Id {
            val uuid = UUID.fromString(rawUuid)
            return Id(uuid)
        }
    }

}

