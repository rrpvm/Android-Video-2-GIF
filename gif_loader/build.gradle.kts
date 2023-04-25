plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("maven-publish")
}

android {
    namespace = "com.rrpvm.gif_loader"
    compileSdk = 33

    defaultConfig {

        minSdk = 28
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
        }
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

val room_version = "2.5.1"
dependencies {

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

   /* implementation("com.github.wseemann:FFmpegMediaMetadataRetriever-core:1.0.19")
    implementation("com.github.wseemann:FFmpegMediaMetadataRetriever-native-armeabi-v7a:1.0.19")
    implementation("com.github.wseemann:FFmpegMediaMetadataRetriever-native-x86:1.0.19")
    implementation("com.github.wseemann:FFmpegMediaMetadataRetriever-native-x86_64:1.0.19")
    implementation("com.github.wseemann:FFmpegMediaMetadataRetriever-native-arm64-v8a:1.0.19")*/


    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation("io.github.waynejo:androidndkgif:1.0.1")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.rrpvm"
                artifactId = "gif_loader"
                version = "1.0.8"

                from(components["release"])
            }
        }
    }
}