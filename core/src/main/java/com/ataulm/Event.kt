package com.ataulm

data class Event<T>(val type: Type, val data: T?, val error: Throwable?) {

    fun updateData(data: T): Event<T> {
        return Event(type, data, error)
    }

    enum class Type {

        LOADING,
        ERROR,
        IDLE

    }

    companion object {

        fun <T> loading(): Event<T> {
            return loading(null)
        }

        fun <T> loading(data: T?): Event<T> {
            return Event(Type.LOADING, data, null)
        }

        fun <T> idle(): Event<T> {
            return idle(null)
        }

        fun <T> idle(data: T?): Event<T> {
            return Event(Type.IDLE, data, null)
        }

        fun <T> error(error: Throwable): Event<T> {
            return error(null, error)
        }

        fun <T> error(data: T?, error: Throwable?): Event<T> {
            if (error == null) {
                throw IllegalArgumentException("Error events must contain an error.")
            }
            return Event(Type.ERROR, data, error)
        }
    }

}
