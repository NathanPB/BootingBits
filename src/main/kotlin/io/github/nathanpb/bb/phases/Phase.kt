package io.github.nathanpb.bb.phases

/**
 * POJO for a single phase on boot process. Usually the developers will not directly interact with this, but with [PhaseWrapper] instead.
 *
 * @param[display] the phase display text.
 * @param[body] phase body, here is defined subphases and execution blocks.
 */
open class Phase(val display: String, body: PhaseWrapper.()->Unit){

    companion object {

        /**
         * Formatter that returns the default message type. With indentation, index and the display text.
         *
         * E.g:`         [3/4] Initializing something.`
         */
        val DEFAULT_FORMAT = { it: Phase ->
            "${it.formatDisplay(INDENTATION)}${it.formatDisplay(INDEXED)} ${it.display}"
        }

        /**
         * Formatter that returns only the index.
         *
         * E.g: `[3/4]`
         */
        val INDEXED = { it: Phase ->
            it.parent?.let { parent ->
                "[${parent.children.indexOf(it)+1}/${parent.children.size}]"
            } ?: ""
        }

        /**
         * Formatter that returns only the required indentation. Each subphase means 4 spaces.
         *
         * E.g: `           `
         */
        val INDENTATION = { it: Phase ->
            (0..it.depth-2).joinToString(""){"    "}
        }
    }

    /**
     * The executable block.
     */
    var run: ()->Unit = {}

    /**
     * Phase parent.
     */
    var parent: Phase? = null

    /**
     * Phase children list.
     */
    val children = mutableListOf<Phase>()


    init {
        body(PhaseWrapper(this))
    }

    /**
     * Phase dept, how many parents to reach the root.
     * E.g:
     *
     * ```
     * [1/1] Well now I'm starting my wonderful system          - 1 level
     *      [1/3] Let's do the job on database fist             - 2 levels
     *          [1/2] I'm connecting to the database right now  - 3 levels
     * ```
     */
    val depth: Int
        get() {
            var counter = 0
            var next = parent
            while(next != null){
                next = next.parent
                counter++
            }
            return counter
        }

    /**
     * Formats the phase according to a formatter.
     *
     * Formatter Examples:
     * -[DEFAULT_FORMAT]
     * -[INDEXED]
     * -[INDENTATION]
     *
     * Every return on [body] block will be turn into [String] and output.
     */
    open fun formatDisplay(body: (Phase)->Any): String = body(this).toString()

    /**
     * Holds the [execute] and [subphase] blocks.
     *
     * @param[phase] the phase to work on.
     */
    class PhaseWrapper(val phase: Phase) {

        /**
         * Defines the phase execution lambda. [body] will be executed when the phase is being executed.
         */
        fun execute(body: ()->Unit){
            phase.run = body
        }

        /**
         * Creates a [Phase] that has the current phase as parent.
         */
        fun subphase(display: String, body: PhaseWrapper.()->Unit) {
            phase.children += Phase(display, body).apply { parent = phase }
        }
    }
}

/**
 * Just a dummy class to represent empty phases.
 */
class RootPhase(body: PhaseWrapper.() -> Unit) : Phase("", body){
    constructor(): this({})
    override fun formatDisplay(body: (Phase) -> Any) = ""
}