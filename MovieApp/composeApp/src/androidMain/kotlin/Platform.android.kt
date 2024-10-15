import android.os.Build
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import java.util.concurrent.TimeUnit

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun httpClient(config: HttpClientConfig<*>.()-> Unit) = HttpClient(OkHttp){
    config(this)
    engine{
        config{
            retryOnConnectionFailure(true)
            connectTimeout(5, TimeUnit.SECONDS)
        }
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()
