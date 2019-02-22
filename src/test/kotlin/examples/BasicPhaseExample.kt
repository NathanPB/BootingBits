package examples

import io.github.nathanpb.bb.BootManager

/**
 * Simple example on how to use Booting Bits.
 *
 * Output:
 * Boot process started!
 *      [1/1] Well now I'm starting my wonderful system
 *      hey bob lets start the system
 *          [1/3] Let's do the job on database fist
 *              [1/2] I'm connecting to the database right now
 *              [1/2] done in 0ms
 *              [2/2] No let's check the tables
 *              [2/2] done in 0ms
 *          [1/3] done in 7ms
 *              [2/3] Let's connect all our users
 *              [1/2] Looking for high-priority users
 *              [1/2] done in 0ms
 *              [2/2] Looking for standard users
 *              [2/2] done in 0ms
 *          [2/3] done in 0ms
 *          [3/3] No let's send them a notification telling they're online
 *          [3/3] done in 0ms
 *     [1/1] done in 18ms
 */
fun main() {

    //Defining our boot manager
    val bm = BootManager()

    //Defining its phases
    bm.phase {
        subphase("Well now I'm starting my wonderful system") {
            execute { println("hey bob lets start the system") }

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

    //Nothing will happen until whe doesn't start it up
    bm.startup()
}