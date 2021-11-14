package com.example.firebasechapter1

internal class Person {
    var name: String? = null
    var mail: String? = null
    var age = 0

    constructor() {}
    constructor(name: String?, mail: String?, age: Int) {
        this.name = name
        this.mail = mail
        this.age = age
    }

    override fun toString(): String {
        return name + " [" + mail + "," + age + "]"
    }
}
