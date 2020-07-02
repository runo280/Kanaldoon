package app.runo.kanaldoon.utils

private const val schema: String = "tg://resolve?domain="

enum class Targets(val pkgName: String) {
    Default("org.telegram.messenger") {
        override fun schema(username: String?): String = "${schema}${username}"
    },
    Telegram("org.telegram.messenger") {
        override fun schema(username: String?): String = "${schema}${username}"
    },
    PlusMessenger("org.telegram.plus") {
        override fun schema(username: String?): String = "${schema}${username}"
    },
    TurboTel("com.ellipi.messenger") {
        override fun schema(username: String?): String = "${schema}${username}"
    },
    GraphMessenger("ir.ilmili.telegraph") {
        override fun schema(username: String?): String = "${schema}${username}"
    },
    TelegramX("org.thunderdog.challegram") {
        override fun schema(username: String?): String = "${schema}${username}"
    };

    abstract fun schema(username: String?): String
}