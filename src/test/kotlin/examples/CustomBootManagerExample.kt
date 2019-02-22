package examples

import io.github.nathanpb.bb.BootManager
import io.github.nathanpb.bb.phases.Phase

fun main() {

    //Defining or custom BootManager to display different messages.
    val bm = object: BootManager(){

        //Lets tell bob to start the system BEFORE the starting process.
        override fun onStartupStart() {
            println("Hey bob let's start the system")
        }

        //Does bob deserves a congratulations?
        override fun onStartupEnd(initTime: Long) {
            println("Well done bob, we did it in ${System.currentTimeMillis() - initTime}ms")
        }

        //Let's make sure bob know where is he working on.
        override fun beforeExecution(phase: Phase) {
            println(
                phase.formatDisplay(Phase.INDENTATION) + //How many spaces before displaying things?
                phase.formatDisplay(Phase.INDEXED)    + //The counter that appears before the display. E.g: [2/7]
                phase.formatDisplay {
                    " bob we are working on ${it.display}"
                }
            )
        }

        //I don't need to know how much time I took on this.
        override fun afterExecution(phase: Phase, initTime: Long) {

        }
    }

    //Defining its phases
    bm.phase {
        subphase("Well now I'm starting my wonderful system") {

            subphase("Let's do the job on database fist") {
                subphase("I'm connecting to the database right now") {
                    execute { /* code to connect to the database */ }
                }
                subphase("No let's check the tables") {
                    execute { /* code to check if the database is valid */ }
                }
            }

            subphase("Let's connect all our users") {
                subphase("Looking for high-priority users") {
                    execute { /* VIPs deserves priority, right? */ }
                }
                subphase("Looking for standard users") {
                    execute { /* is everyone connect? */ }
                }
            }

            subphase("No let's send them a notification telling they're online") {
                execute { /* users.forEach { it.sendNotification("ure online bro") } */ }
            }
        }
    }

    //Starting it up.
    bm.startup()
}