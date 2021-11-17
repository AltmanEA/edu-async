import kotlinx.coroutines.*

//          first coroutine
//fun main() = runBlocking { // this: CoroutineScope
//    launch { // launch a new coroutine and continue
//        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
//        println("World! ($currentTime)") // print after delay
//    }
//    println("Hello  ($currentTime)") // main coroutine continues while a previous one is delayed
//}

//          suspend fun
//fun main() = runBlocking { // this: CoroutineScope
//    launch { doWorld() }
//    println("Hello ($currentTime)")
//}
//// this is your first suspending function
//suspend fun doWorld() {
//    delay(1000L)
//    println("World! ($currentTime)")
//}

//          job
//fun main() = runBlocking {
//    val job = launch { // launch a new coroutine and keep a reference to its Job
//        delay(1000L)
//        println("World!  ($currentTime)")
//    }
//    println("Hello ($currentTime)")
//    job.join() // wait until child coroutine completes
//    println("Done  ($currentTime)")
//}


//          job cancel
//fun main() = runBlocking {
//    val job = launch {
//        repeat(1000) { i ->
//            println("job: I'm sleeping $i ...  ($currentTime)")
//            delay(500L)
//        }
//    }
//    delay(1300L) // delay a bit
//    println("main: I'm tired of waiting! ($currentTime)")
//    job.cancel() // cancels the job
//    job.join() // waits for job's completion
//    println("main: Now I can quit.  ($currentTime)")
//}

//          cooperative cancel
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
//        while (i < 5) { // computation loop, just wastes CPU
//            // print a message twice a second
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("job: I'm sleeping ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1300L) // delay a bit
//    println("main: I'm tired of waiting!")
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("main: Now I can quit.")
//}