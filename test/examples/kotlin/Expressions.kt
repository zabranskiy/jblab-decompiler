package examples.kotlin

class Expressions() {
    var intField = 1

    fun expressionTest(k : Int, s : String): Long {
        var a = (1 + 2) * 3 / k
        var b = 2 * (intFun(a) + 12.5)
        return a.toLong()
    }

    fun intFun(x : Int): Int {
        return x
    }

    fun propertiesTest() {
        intField = 1
        var i = 2 + intField + Expressions().intField
    }
}