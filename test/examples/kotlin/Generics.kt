package examples.kotlin

class Generics <L, M : String?, K : Map<String, List<String>> > (anInt : Int, aString : String) {
    fun <Q, R, L>  genericsTest(aL : L, aQ : Q): Q {
        val aQ1 = aQ
        return aQ1
    }
}