package examples.kotlin

class LambdaFunctions {
    fun test(stringArray : Array<String>, valClosure: String, f : (Int) -> Int) {
        fun innerIntFun(i : Int): Int {
            var k = 1
            k + i
        }
        fun innerVoidFun() {
            var k = 2
        }

        innerIntFun(3)
        f(4)

        stringArray.all { i -> true }

        stringArray.map { s ->
            var str = "inner"
            valClosure
        }

        var varClosure1 = 4
        var varClosure2 = 5

        stringArray.map { s ->
            var str = "inner"
            varClosure1 + varClosure2
        }

        println(varClosure1 + varClosure2)
    }

    fun declarationTest(function : (Int, /***/ (Int, Array<String>) -> Map<String, Int> /***/ , String, /***/ (String, Long, Char) -> Double /***/ ) -> Boolean, otherVariable : Int) {}
}