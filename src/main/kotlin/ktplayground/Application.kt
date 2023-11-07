package ktplayground

import org.slf4j.LoggerFactory

object Application {
    private val log = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        log.info("Some Log Message!")
        println("Some Random Message!")
    }
}
