package examples

import io.github.nathanpb.bb.BootManager

/**
 * Example of changing the phase list on runtime: Adding 5 more phases when each other is done.
 *
 * Output:
 * Boot process started!
 * I currently have 0 children!
 * But not I have 2 children that had born right now!
 * [1/2] A phase created at runtime
 * Yey I born at runtime
 * [1/2] done in 12ms
 * [2/2] Another phase created at runtime
 * I'm his brother
 * [2/2] done in 0ms
 * Done in 15ms
 */
fun main() {

    val bm = BootManager()

    bm.phase {

        //Everything inside ``execute {}`` block will run when the phase is being executed, not when it's defined.
        execute {
            //The phase has no children yet.
            println("I currently have ${phase.children.size} children!")

            //So let's define a new phase WHEN it's being executed.
            this.subphase("A phase created at runtime") {
                execute { println("Yey I born at runtime") }
            }

            //Again...
            this.subphase("Another phase created at runtime") {
                execute { println("I'm his brother") }
            }

            //Now the phase has two children! They were defined above.
            println("But not I have ${phase.children.size} children that had born right now!")
        }
    }

    bm.startup()
}