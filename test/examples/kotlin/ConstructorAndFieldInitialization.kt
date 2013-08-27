package examples.kotlin

open class SuperClass1(i : Int, s : String)
trait Trait1
trait Trait2

class ConstructorAndFieldInitialization(i : Int, str : String) : Trait1, Trait2, SuperClass1(12345, "string") {
    var intField = i
    var stringField = str

    var stringField2 : String

    {
        stringField2 = "string2"
        var i = 123
    }
}