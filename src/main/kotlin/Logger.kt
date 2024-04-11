class Logger (private val dryRun: Boolean) {
    fun log(action: String){
        if(dryRun){
            println("[Dry Run] $action")
        } else {
            println(action)
        }
    }

    fun info(message: String) {
        log("Info: $message")
    }

    fun error(message: String) {
        System.err.println("Error: $message")
    }
}