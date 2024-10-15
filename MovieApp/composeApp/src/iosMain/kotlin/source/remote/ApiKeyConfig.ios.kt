package source.remote

import platform.Foundation.NSBundle

actual fun ApiKey() = NSBundle.mainBundle.objectForInfoDictionaryKey("API_KEY") as? String ?: "DefaultApiKey"
