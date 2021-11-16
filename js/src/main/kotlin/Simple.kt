import kotlinx.browser.window
import kotlin.js.Date
import kotlin.js.Promise

val currentTime
    get() = Date.now().toString().subSequence(9, 13)

fun main() {
//    callbackExample()
//    promiseExample()
//    promiseChainExample()
    promiseAllExample()
}

fun promiseAllExample() {
    Promise.all(
        arrayOf(
            resourceDownloader("A"),
            resourceDownloader("B"),
            resourceDownloader("C")
        )
    ).then {
        console.log("All downloaded at $currentTime")
    }
}

fun promiseChainExample() {
    resourceDownloader("A")
        .then {
            resourceDownloader("B")
        }
        .then {
            resourceDownloader("C")
        }
}


fun resourceDownloader(name: String): Promise<String> {
    console.log("Start download resource $name at $currentTime")
    return Promise { resolve, reject ->
        window.setTimeout(
            {
                val isOk = (0..100).random() < 95
                if (isOk) {
                    console.log("Resource $name downloaded at $currentTime")
                    resolve("Resource $name downloaded")
                } else {
                    console.log("Download $name failed  at $currentTime")
                    reject(Throwable("Download $name failed"))
                }
            },
            1000
        )
    }
}

fun promiseExample() {
    val promise = Promise<String> { resolve, reject ->
        window.setTimeout(
            {
                val isOk = (0..10).random() < 5
                if (isOk)
                    resolve("Resource downloaded at $currentTime")
                else
                    reject(Throwable("Download failed at $currentTime"))
            },
            1000
        )
    }
    promise.then(
        onFulfilled = { console.log(it) },
        onRejected = { console.log(it.message) }
    )

    console.log("End of promise example at $currentTime")
}

fun callbackExample() {
    window.setTimeout(
        { console.log("I am sleeping  at $currentTime") },
        1000
    )
    console.log("End of timeout example  at $currentTime")
}
