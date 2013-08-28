package examples.kotlin

import jet.runtime.SharedVar

class A {}
class B {}

class ControlFlow {
    fun intFun(): Int = 1
    fun intFun2(i : Int) = i * 2

    fun whenTest(x : Int, y : Any?) {
        when (intFun2(x)) {
            0 -> println("1")
            intFun() -> println("2")
            intFun2(x) + intFun() -> println("3")
            else -> println("4")
        }

        when (y) {
            is ControlFlow -> println("1")
            !is A -> println("2")
            is B -> println("3")
            else -> println("4")
        }
    }

    fun safeMethod() {}

    fun safeCalls(s : ControlFlow?, x : Any?) {
        s!!.safeMethod()
        x!!.hashCode()
    }

    fun newArrayFolding() {
        var newArray = Array<Array<Int>>(1, { i -> Array<Int>(2, { i -> 3 }) })
    }

    fun forTest(list : List<Int>, array: Array<Int>, map : Map<String, Int>) {
        for (i in list) {
            var var1 = 1
            println(i)
            for (j in list) {
                var var2 = 2
                println(j)
            }
            for (k in array) {
                println(k)
                var var3 = 3
                for ((key, value) in map) {
                    var var4 = 4
                    println(key)
                    println(value)
                }
            }
        }
    }

    fun whileIfTest() {
        var i = 1
        if (i * 2 > 3) {
            if (i * 4 > 5) {
                if (i * 6 > 7) {
                    while (i > 0) {
                        println(i)
                        if (i > 9) {
                            break
                        }

                        while (i < 8) {
                            i++
                        }
                    }
                }
            } else {
                println(--i)
            }
        }

        if (i > 1) {
            println(i + 1)
        } else if (i > 2) {
            println(i + 2)
        } else if (i > 3) {
            println(i + 3)
        }
    }
}


