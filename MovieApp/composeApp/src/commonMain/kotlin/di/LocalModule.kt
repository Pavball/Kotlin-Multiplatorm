package di

import org.koin.dsl.module
import source.local.Database

internal val localModule = module {

    single<Database> { Database(get()) }

}