package examples.kotlin

import java.io.Serializable

open class InnerClasses {
    class Inner { class InnerInner {} }
    class Inner2 {}

    fun innerClassesTest() {
        class InnerMethod { }

        var c = 1
        if (c > 2) {
            class InnerMethod {}
        }

        var anonymousClass = object : InnerClasses(), Serializable {
            fun testFun() {}
        }

        var innerMethod : InnerMethod = InnerMethod()
        var innerInner : Inner.InnerInner = Inner.InnerInner()
    }
}