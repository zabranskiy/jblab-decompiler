package examples.kotlin

class Expressions() {
    var intField = 1

    fun expressionTest(k : Int, s : String): Long {
        var a = (1 + 2) * 3 / k
        val b = 2 * (intFun(a) + 12.5)
        a = 123
        return a.toLong()
    }

    fun intFun(x : Int): Int {
        return x
    }

    fun propertiesTest() {
        intField = 1
        val i = 2 + intField + Expressions().intField
    }
}