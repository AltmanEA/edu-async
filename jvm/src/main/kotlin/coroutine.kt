import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

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

//          non-interrupt
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
//        while (i < 5) { // computation loop, just wastes CPU
//            // print a message twice a second
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("job: I'm sleeping ${i++} ...  ($currentTime)")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1300L) // delay a bit
//    println("main: I'm tired of waiting!  ($currentTime)")
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("main: Now I can quit.  ($currentTime)")
//}


//          cooperative cancel
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
//        while (isActive) { // cancellable computation loop
//            // print a message twice a second
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("job: I'm sleeping ${i++} ...  ($currentTime)")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1300L) // delay a bit
//    println("main: I'm tired of waiting!  ($currentTime)")
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("main: Now I can quit.  ($currentTime)")
//}

var acquired = 0

class Resource {
    init { acquired++ } // Acquire the resource
    fun close() { acquired-- } // Release the resource
}

//          Resource
//fun main() {
//    runBlocking {
//        repeat(100_000) { // Launch 100K coroutines
//            launch {
//                val resource = withTimeout(60) { // Timeout of 60 ms
//                    delay(50) // Delay for 50 ms
//                    Resource() // Acquire a resource and return it from withTimeout block
//                }
//                resource.close() // Release the resource
//            }
//        }
//    }
//    // Outside of runBlocking all coroutines have completed
//    println(acquired) // Print the number of resources still acquired
//}

//          interrupt
//fun main() {
//    runBlocking {
//        repeat(100_000) { // Launch 100K coroutines
//            launch {
//                var resource: Resource? = null // Not acquired yet
//                try {
//                    withTimeout(60) { // Timeout of 60 ms
//                        delay(50) // Delay for 50 ms
//                        resource = Resource() // Store a resource to the variable if acquired
//                    }
//                    // We can do something else with the resource here
//                } finally {
//                    resource?.close() // Release the resource if it was acquired
//                }
//            }
//        }
//    }
//    // Outside of runBlocking all coroutines have completed
//    println(acquired) // Print the number of resources still acquired
//}

//      Shared resource
//fun main(){
//    println("Start in $currentTime")
//
//    var shared = 0
//
//    suspend fun coreSyncFun(op: (Int) -> Int, delay: Long) {
//        val t = op(shared)
//        println("start op $shared -> $t $currentTime")
//        delay(delay)
//        println("end  op  $shared -> $t $currentTime")
//        shared = t
//    }
//
//    runBlocking {
//        launch {
//            for (delay in listOf(300L, 100L))
//                coreSyncFun({ it + 1 }, delay)
//        }
//        launch {
//            for (delay in listOf(100L, 300L))
//                coreSyncFun({ it + 10 }, delay)
//        }
//        delay(1000)
//        println(shared)
//    }
//}

//      Shared resource
//fun main(){
//    println("Start in $currentTime")
//    val mutex = Mutex()
//    var shared = 0
//
//    suspend fun coreSyncFun(op: (Int) -> Int, delay: Long) {
//        mutex.withLock {
//            val t = op(shared)
//            println("start op $shared -> $t $currentTime")
//            delay(delay)
//            println("end  op  $shared -> $t $currentTime")
//            shared = t
//        }
//    }
//
//    runBlocking {
//        launch {
//            for (delay in listOf(300L, 100L))
//                coreSyncFun({ it + 1 }, delay)
//        }
//        launch {
//            for (delay in listOf(100L, 300L))
//                coreSyncFun({ it + 10 }, delay)
//        }
//        delay(1000)
//        println(shared)
//    }
//}

//      Атомарные ресурсы
//fun main(){
//    println("Start in $currentTime")
//
//    var shared = AtomicInteger()
//
//    suspend fun coreSyncFun(op: (AtomicInteger) -> AtomicInteger, delay: Long) {
//        val t = op(shared)
//        println("start op $shared -> $t $currentTime")
//        delay(delay)
//        println("end  op  $shared -> $t $currentTime")
//        shared = t
//    }
//
//    runBlocking {
//        launch {
//            for (delay in listOf(300L, 100L))
//                coreSyncFun({ it.apply {  getAndAdd(1) }}, delay)
//        }
//        launch {
//            for (delay in listOf(100L, 300L))
//                coreSyncFun({it.apply {  getAndAdd(10) } }, delay)
//        }
//        delay(1000)
//        println(shared)
//    }
//}

//          Chanel
//fun main() = runBlocking {
//    val channel = Channel<Int>()
//    launch {
//        for (x in 1..5) {
//            channel.send(x * x)
//            delay(1000)
//        }
//        channel.close() // we're done sending
//    }
//    // here we print received values using `for` loop (until the channel is closed)
//    for (y in channel) println("$y $currentTime")
//    println("Done!")
//}

//          sync suspend fun
suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}
fun main() = runBlocking {
    val time = measureTimeMillis {
//        val one = doSomethingUsefulOne()
//        val two = doSomethingUsefulTwo()
//        println("The answer is ${one + two}")
        val one: Deferred<Int> = async { doSomethingUsefulOne() }
        val two: Deferred<Int> = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}
