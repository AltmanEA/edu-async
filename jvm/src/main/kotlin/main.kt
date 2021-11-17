import java.lang.management.ManagementFactory
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

val currentTime
    get() = System.nanoTime().toString().subSequence(5, 10)

fun main() {
//    runThread()
//    sleepThread()
//    joinThread()
//    syncThread()
    lockThread()
//    deadlock()
}

fun deadlock() {
    val lockA = ReentrantLock()
    val lockB = ReentrantLock()

    thread {
        lockA.lock()
        println("Thread 1 Lock A $currentTime")
        Thread.sleep(100)
        lockB.lock()
        println("Thread 1 Lock B $currentTime")
        lockB.unlock()
        lockA.unlock()
    }
    thread {
        lockB.lock()
        println("Thread 2 Lock B $currentTime")
        Thread.sleep(100)
        lockA.lock()
        println("Thread 2 Lock A $currentTime")
        lockA.unlock()
        lockB.unlock()
    }
}

fun lockThread(){
    val lock = ReentrantLock()
    var shared = 0

    fun lockedOp(op: (Int) -> Int, delay: Long){
        lock.lock()
        val t = op(shared)
        println("start op $shared -> $t $currentTime")
        Thread.sleep(delay)
        println("end  op  $shared -> $t $currentTime")
        shared = t
        lock.unlock()
    }

    thread {
        for (delay in listOf(300L, 100L))
            lockedOp({ it + 1 }, delay)
    }
    thread {
        for (delay in listOf(100L, 300L))
            lockedOp({ it + 10 }, delay)
    }
}

fun syncThread() {
    println("Start in $currentTime")

    var shared = 0

//    @Synchronized
    fun coreSyncFun(op: (Int) -> Int, delay: Long) {
        val t = op(shared)
        println("start op $shared -> $t $currentTime")
        Thread.sleep(delay)
        println("end  op  $shared -> $t $currentTime")
        shared = t
    }

    thread {
        for (delay in listOf(300L, 100L))
            coreSyncFun({ it + 1 }, delay)
    }
    thread {
        for (delay in listOf(100L, 300L))
            coreSyncFun({ it + 10 }, delay)
    }
    Thread.sleep(1000)
    println(shared)
}

fun joinThread() {
    println("Start of join thread $currentTime")

    thread {
        Thread.sleep(1000L)
        println("I have woken up $currentTime")
    }
        .join()

    println("End of join thread $currentTime")
}

fun sleepThread() {
    println("Begin of sleep funs $currentTime")

    thread {
        Thread.sleep(1000L)
        println("I have woken up $currentTime")
    }

    thread {
        Thread.sleep(10000L)
        println("I slept for 10 seconds $currentTime")
    }
        .interrupt()

    println("End of sleep funs $currentTime")
}

fun runThread() {
    printThreadInfo()
    MyThread().start()
    Thread { // interface Runnable
        printThreadInfo()
    }
        .start()
    thread(name = "MyThread") { printThreadInfo() }
}

fun printThreadInfo() {
    val currentThread = Thread.currentThread()
    val processID = ManagementFactory.getRuntimeMXBean().name
    println("Thread: ${currentThread.name}")
    println("Process: $processID")
}