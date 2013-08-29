package examples.kotlin

fun main(args : Array<String>) {
    println(args.any { str -> true })
    println("string1")
}

fun test(a : String, b : String?, i : Int?, j : Long, s : Short?): String? {
    return "string2"
}

fun Array<Int>.extensionTest1(s : Array<Int>, f : (Int) -> Int) {
    f(5)
    s.map(f)
    s.any { w -> false }
    this.extensionTest1(s, f)
}

fun String.extensionTest2(i : Int) {
    println(this + i)
}
