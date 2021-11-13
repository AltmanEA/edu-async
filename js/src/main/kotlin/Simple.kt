import kotlinx.browser.window
import kotlin.js.Promise

fun main() {
    callbackExample()
    promiseExample()
}

fun promiseExample() {
    val promise = Promise<String> { resolve, reject ->
        window.setTimeout(
            {
                val isOk = (0..10).random() < 5
                if (isOk)
                    resolve("Resource downloaded")
                else
                    reject(Throwable("Download failed"))
            },
            1000
        )
    }
    promise.then(
        { console.log(it) },
        { console.log(it.message) }
    )

    console.log("End of promise example")
}

fun callbackExample() {
    window.setTimeout(
        { console.log("I am sleeping") },
        1000
    )
    console.log("End of timeout example")
}
