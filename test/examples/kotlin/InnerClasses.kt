package examples.kotlin

import java.io.Serializable
import examples.kotlin.InnerClasses.Inner.InnerInner

open class InnerClasses {
    val valField : Int = 1
    var varField : Int = 2

    inner class Inner {
        inner class InnerInner {
            var field1 : Int = valField
            val field2 : Int = valField + varField
        }
        var zxc : Int = valField * varField
    }

    class Inner2 {}

    fun innerClassesTest() {
        class InnerMethod { }

        var c = 1
        if (c > 2) {
            class InnerMethod {}
        }

        var anonymousClass = object : InnerClasses(), Serializable {
            fun testFun() {
                var a : Int = varField
                innerClassesTest()
                privateMethod()
                c = 123 * c
            }
        }

        var innerMethod : InnerMethod = InnerMethod()
        var innerInner : Inner.InnerInner = Inner().InnerInner()
    }

    private fun privateMethod() {}
}