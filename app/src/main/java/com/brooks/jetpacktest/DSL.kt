package com.brooks.jetpacktest

class Dependency {
    val libraries = ArrayList<String>()

    fun implementation(lib: String) {
        libraries.add(lib)
    }
}

fun dependencies(block: Dependency.() -> Unit): List<String> {
    val dependency = Dependency()
    dependency.block()
    return dependency.libraries
}

class Td {
    var content = ""

    fun html() = "\n\t\t<td>$content</td>"
}


class Tr {
    private val children = ArrayList<Td>()

    fun td(block: Td.() -> String) {
        val td = Td()
        td.content = td.block()
        children.add(td)
    }

    fun html(): String {
        val builder = StringBuilder()
        builder.append("\n\t<tr>")
        return builder.toString()
    }
}

class Table {
    private val children = ArrayList<Tr>()

    fun tr(block: Tr.() -> Unit) {
        val tr = Tr()
        tr.block()
        children.add(tr)
    }

    fun html(): String {
        val builder = StringBuilder()
        builder.append("<table>")
        for (childTag in children) {
            builder.append(childTag.html())
        }
        builder.append("\n</table>")
        return builder.toString()
    }
}


fun main() {
    val libraries = dependencies {
        implementation("com.squareup.retrofit2:retrofit2:2.6.1")
        implementation("com.squareup.retrofit2:converter-gson:2.6.1")
    }

    for (lib in libraries) {
        println(lib)
    }

    val tr = Tr()
    tr.td { "Apple" }
    tr.td { "Grape" }
    tr.td { "Orange" }

    val table = Table()
    table.tr {
        td { "Apple" }
        td { "Grape" }
        td { "Orange" }
    }
    table.tr {
        td { "Pear" }
        td { "Banana" }
        td { "Watermelon" }
    }

    fun table(block: Table.() -> Unit): String {
        val table = Table()
        table.block()
        return table.html()
    }

    val html = table {
        tr {
            td { "Apple" }
            td { "Grape" }
            td { "Orange" }
        }
        tr {
            td { "Pear" }
            td { "Banana" }
            td { "Watermelon" }
        }
    }
    println(html)


    val html2 = table {
        repeat(2) {
            tr {
                val fruits = listOf("Apple", "Grape", "Orange")
                for (fruit in fruits) {
                    td { fruit }
                }
            }
        }
    }
    println(html2)
}