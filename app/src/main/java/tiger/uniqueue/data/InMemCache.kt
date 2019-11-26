package tiger.uniqueue.data

class InMemCache private constructor() {
    private val _cache = HashMap<String, Any?>()
    val cache = _cache

    companion object {
        val INSTANCE: InMemCache by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            InMemCache()
        }
    }
}