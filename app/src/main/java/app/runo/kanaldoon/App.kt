package app.runo.kanaldoon

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import okhttp3.OkHttpClient

class App: Application(), ImageLoaderFactory {
    private val context : Context by lazy {
        applicationContext
    }

    override fun onCreate() {
        super.onCreate()

    }
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(context))
                    .build()
            }
            .build()
    }
}