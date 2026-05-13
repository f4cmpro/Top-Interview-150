import kotlin.random.Random

class RandomizedSet() {
    val set = HashSet<Int>()
    var theLast = 0
    fun insert(`val`: Int): Boolean {
        if (set.contains(`val`)) {
            return false
        }
        set.add(`val`)
        return true
    }

    fun remove(`val`: Int): Boolean {
        if (!set.contains(`val`)) {
            return false
        }
        set.remove(`val`)
        if (set.isEmpty()) {
            theLast = `val`
        }
        return true
    }

    fun getRandom(): Int {
        return if (set.isEmpty()) {
            theLast
        } else {
            set.random()
        }
    }

}