plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	id("kotlin-kapt")
	id("com.google.gms.google-services")
}

android {
	namespace = "com.sairajpatil108.dailyworkout"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.sairajpatil108.dailyworkout"
		minSdk = 29
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	
	// Navigation
	implementation("androidx.navigation:navigation-compose:2.7.5")
	
	// ViewModel
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
	
	// Room database
	implementation("androidx.room:room-runtime:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	implementation(libs.androidx.navigation.runtime.android)
	kapt("androidx.room:room-compiler:2.6.1")
	
	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
	
	// Material Icons Extended
	implementation("androidx.compose.material:material-icons-extended:1.5.4")
	
	// Firebase
	implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
	implementation("com.google.firebase:firebase-auth-ktx")
	implementation("com.google.firebase:firebase-analytics-ktx")
	implementation("com.google.firebase:firebase-firestore-ktx")
	
	// Google Sign-In
	implementation("com.google.android.gms:play-services-auth:20.7.0")
	
	// Image loading
	implementation("io.coil-kt:coil-compose:2.5.0")
	
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}