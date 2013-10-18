package examples.kotlin

class Expressions() {
    var varIntField = 1
    val valIntField = 2

    fun expressionTest(k : Int, s : String): Long {
        var a = (3 + 4) * 5 / k
        val b : Double? = 6 * (intFun(a) + 7.8)
        a = 9
        return a.toLong() + valIntField
    }

    fun intFun(x : Int): Int {
        return x
    }

    fun propertiesTest() {
        varIntField = 11
        val i = 12 + varIntField + Expressions().varIntField
    }
}