package examples.kotlin

open class SuperClass {
    fun superMethod(): SuperClass = SuperClass()
}

trait Trait3 {
    fun traitMethod(i : Int) {}
    fun traitMethodForOverride(i : Int) {}
}

class MethodInvocations : Trait3, SuperClass() {
    public fun publicMethod() {}
    private fun privateMethod() {}
    protected fun protectedMethod() {}

    fun invocationTest() {
        publicMethod()
        privateMethod()
        protectedMethod()
        traitMethod(1)
        super<Trait3>.traitMethod(2)
        superMethod().superMethod().superMethod()
        MethodInvocations().superMethod()
        super<SuperClass>.superMethod()
    }

    override fun traitMethodForOverride(i : Int) {}

    fun defaultParameters(s : String?, i : Int = 1, j : Int = 2, k : Int = 6) {
        defaultParameters("string2", 3)

        var variable = MethodInvocations()
        variable.defaultParameters("string1", k = 123)
        variable.defaultParameters("string3", 4, 5)
    }
}