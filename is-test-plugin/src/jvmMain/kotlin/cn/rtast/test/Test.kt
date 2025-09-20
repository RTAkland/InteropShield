/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/14/25
 */


package cn.rtast.test

import cn.rtast.interop.annotation.AutoGenGetter
import cn.rtast.interop.annotation.AutoGenSetter


//class GenericClass<T>(var value: T) {
//}
//
//class NormalClass
//
//interface InterfaceClass
//
//interface MyList<out E> : Collection<E>
//
//class ClassWithCompanionObject {
//    companion object InnerObject
//}
//
//annotation class AnnotationClass
//
//object ObjectClass
//
//enum class EnumClass {
//    Green, Red
//}
//
//sealed class SealedClass {
//    var temp = 0
//}
//
//final class FinalClass
//
//open class OpenClass {
//    var temp = 2
//}
//
//abstract class AbstractClass
//
//@AutoGenSetter
//@AutoGenGetter
//var SealedClass.a
//    get() = 1
//    set(value) {
//        this.temp = value
//    }
//
//@AutoGenGetter
//val FinalClass.a get() = 1
//
//@AutoGenGetter
//val AbstractClass.a get() = 1
//
//@AutoGenGetter
//var OpenClass.a
//    get() = this.temp
//    set(value) {
//        this.temp = value
//    }
//
//@AutoGenGetter
//val EnumClass.a get() = 1
//
//@AutoGenGetter
//val ObjectClass.a get() = 1
//
////@AutoGenGetter
////val AnnotationClass.a get() = 1
//
//@AutoGenGetter
//val ClassWithCompanionObject.InnerObject.name get() = 18
//
//@AutoGenGetter
//private val NormalClass.name get() = "1"
//
//@AutoGenGetter
//val <T> GenericClass<T>.age get(): Int = 18
//
//@AutoGenGetter
//var <T> GenericClass<T>.valueWithGeneric get(): T = this.value
//    set(value) {
//        this.value = value
//    }
//
//@AutoGenGetter
//val NormalClass.delegated by lazy { "Hello" }
//
//@AutoGenGetter
//val InterfaceClass.name get() = 18
//
//@AutoGenGetter
//val <T> MyList<T>.firstOrNullItem: T?
//    get() = this.firstOrNull()
//
//class SS : SealedClass()

//@AutoGenGetter
val List<String>.text get() = this.joinToString { it }

abstract class Test<out E>(override val size: Int, var test: Int) : List<E> {}

//@AutoGenGetter
var Test<String>.text
    get() = test
    set(value) {
        this.test = value
    }

class Setter(var innerText: String)

@AutoGenGetter
@AutoGenSetter
var Setter.text
    get() = this.innerText
    set(value) {
        this.innerText = value
    }

@AutoGenGetter
val Test<Int>.text2 get(): Int = first()

class Generic2<T>(var inner: T)

@AutoGenGetter("sssss")
var <T: String> Generic2<T>.sss
    get(): T = inner
    set(value) {
        inner = value
    }



fun main() {
//    val op = OpenClass()
//    op.a = 444
//    println(op.a)
    println("Hello world".map { it.toString() }.text)
}