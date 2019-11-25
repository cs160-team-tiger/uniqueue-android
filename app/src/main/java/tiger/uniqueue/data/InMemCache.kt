package tiger.uniqueue.data

class InMemCache {
    private val _cache = HashMap<String, Any>()
    val cache = _cache
}