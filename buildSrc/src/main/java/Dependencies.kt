object Versions {
    const val minSdk = 24
    const val targetSdk = 30
    const val compileSdk = 30

    const val kotlin = "1.5.21"
}

object Kotlin {
    private const val core_ktx_version = "1.6.0"
    private const val fragment_ktx_version = "1.3.5"

    val coreKtx = "androidx.core:core-ktx:$core_ktx_version"
    val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val fragmentKtx = "androidx.fragment:fragment-ktx:$fragment_ktx_version"
}

object UI {

    private const val material_version = "1.1.0"

    val materialComponents = "com.google.android.material:material:$material_version"
}

object AndroidX {
    private const val app_compat_version = "1.3.0"
    private const val constraint_layout_version = "2.0.4"
    private const val fragment_container_view_version = "1.2.3"
    private const val recycler_view_version = "1.1.0"

    val appCompat = "androidx.appcompat:appcompat:$app_compat_version"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraint_layout_version"
    val fragmentContainer = "androidx.fragment:fragment-ktx:$fragment_container_view_version"
    val recyclerView = "androidx.recyclerview:recyclerview:$recycler_view_version"

}

object DI {
    private const val koin_version = "3.1.2"

    val coreKoin = "io.insert-koin:koin-core:$koin_version"
    val androidKoin = "io.insert-koin:koin-android:$koin_version"
}

object RxJava {
    private const val rxjava_version = "3.0.13"
    private const val rxandroid_version = "3.0.0"

    private const val core = "io.reactivex.rxjava3:rxjava:$rxjava_version"
    private const val android = "io.reactivex.rxjava3:rxandroid:$rxandroid_version"
}

object Firebase {
    private const val firebase_bom_version = "28.3.0"

    val firebaseBoM = "com.google.firebase:firebase-bom:$firebase_bom_version"
    val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    val firebaseDatabase = "com.google.firebase:firebase-database-ktx"
}

object Log {
    private val timber_version = "4.7.1"

    val timber = "com.jakewharton.timber:timber:$timber_version"
}

object Glide {
    private val glide_version = "4.12.0"

    val glide = "com.github.bumptech.glide:glide:$glide_version"
    val glideCompiler = "com.github.bumptech.glide:compiler:$glide_version"
}

object Lottie {
    private const val lottie_version = "3.7.2"

    val lottie = "com.airbnb.android:lottie:$lottie_version"
}
