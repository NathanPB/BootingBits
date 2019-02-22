package io.github.nathanpb.bb

import io.github.nathanpb.bb.phases.Phase
import io.github.nathanpb.bb.phases.RootPhase

/**
 * Class responsible for starting the execution process and define the phase list. Can be extended to override event methods.
 */
open class BootManager {

    /**
     * Simple container that holds the first phase. This one is not accessible for users working everywhere else.
     */
    private var rootPhase = RootPhase()

    /**
     * Method to start the phase list.
     */
    fun phase(body: Phase.PhaseWrapper.()->Unit) = Phase.PhaseWrapper(RootPhase(body).also { rootPhase = it })

    /**
     * Starts a phase. Also used for recursion to start all its children.
     */
    private fun execute(phase: Phase) {
        val st = System.currentTimeMillis()
        if(phase !is RootPhase) beforeExecution(phase)
        phase.run()
        phase.children.forEach { this.execute(it) }
        if(phase !is RootPhase) afterExecution(phase, st)
    }

    /**
     * Starts from the root phase.
     */
    fun startup() {
        System.currentTimeMillis().let {
            onStartupStart()
            execute(rootPhase)
            onStartupEnd(it)
        }
    }

    /**
     * Executed before a phase be executed.
     * @param[phase] the phase.
     */
    open fun beforeExecution(phase: Phase){
        println(phase.formatDisplay(Phase.DEFAULT_FORMAT))
    }

    /**
     * Executed after a phase get executed.
     * @param[phase] the phase.
     * @param[initTime] when whe execution started.
     */
    open fun afterExecution(phase: Phase, initTime: Long){
        println("${phase.formatDisplay(Phase.INDENTATION)}${phase.formatDisplay(Phase.INDEXED)} done in ${System.currentTimeMillis() - initTime}ms")
    }

    /**
     * Executed when the execution starts.
     */
    open fun onStartupStart() {
        println("Boot process started!")
    }

    /**
     * Executed when the execution ends.
     */
    open fun onStartupEnd(initTime: Long){
        println("Done in ${System.currentTimeMillis() - initTime}ms")
    }
}