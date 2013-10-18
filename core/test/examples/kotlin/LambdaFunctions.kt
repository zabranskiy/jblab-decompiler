package examples.kotlin

class LambdaFunctions {
    fun test(stringArray : Array<String>, valClosure: String, f : (Int) -> Int) {
        fun innerIntFun(i : Int): Int {
            val k = 1
            k + i
        }
        fun innerVoidFun() {
            val k = 2
        }

        innerIntFun(3)
        f(4)

        stringArray.all { i -> true }

        stringArray.map { s ->
            val str = "inner"
            valClosure
        }

        var varClosure1 = 4
        var varClosure2 = 5

        stringArray.map { s ->
            val str = "inner"
            varClosure1 + varClosure2
        }

        println(varClosure1 + varClosure2)
    }

    fun declarationTest(function : (Int, /***/ (Int, Array<String>) -> Map<String, Int> /***/ , String, /***/ (String, Long, Char) -> Double /***/ ) -> Boolean, otherVariable : Int) {}
}