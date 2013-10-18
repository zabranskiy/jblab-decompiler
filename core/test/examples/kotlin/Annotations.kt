package examples.kotlin

annotation class fancy
annotation class nancy (val why : String, val me : Int)

fancy nancy("class", 1) class Annotations {
    fancy nancy("method", 2) fun annotatedMethod(o : Long, fancy nancy("parameter", 3) annotatedParameter : Int, s : String): Int {
        return 4
    }
}