package di

import org.koin.dsl.module
import source.local.DriverFactory

actual val platformModule = module {

    single<DriverFactory> {
        DriverFactory(context = get())
    }
}