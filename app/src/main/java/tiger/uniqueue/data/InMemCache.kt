package tiger.uniqueue.data

class InMemCache private constructor() {
    private val _cache = HashMap<String, Any?>()
//    val cache = _cache

    companion object {
        val INSTANCE: InMemCache by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            InMemCache()
        }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String): T? =
        _cache[key] as? T

    operator fun set(key: String, value: Any?) {
        _cache[key] = value
    }
}

