package com.sairajpatil108.dailyworkout.ViewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sairajpatil108.dailyworkout.data.WorkoutRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val isSignedIn: Boolean = false,
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null
)

class AuthViewModel(private val repository: WorkoutRepository) : ViewModel() {
    var authState by mutableStateOf(AuthState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "AuthViewModel"

    init {
        // Check if user is already signed in
        val currentUser = auth.currentUser
        authState = authState.copy(
            isSignedIn = currentUser != null,
            user = currentUser
        )
        
        // Initialize Firestore data for existing user
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    repository.initializeUserData()
                    Log.d(TAG, "Firestore data initialized for existing user")
                } catch (e: Exception) {
                    Log.e(TAG, "Error initializing Firestore data for existing user: ${e.message}")
                }
            }
        }
        
        Log.d(TAG, "Initial auth state: isSignedIn=${authState.isSignedIn}")
    }

    fun initializeGoogleSignIn(context: Context) {
        try {
            val webClientId = context.getString(com.sairajpatil108.dailyworkout.R.string.default_web_client_id)
            Log.d(TAG, "Web Client ID: $webClientId")
            
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(context, gso)
            Log.d(TAG, "Google Sign-In client initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Google Sign-In", e)
            authState = authState.copy(
                errorMessage = "Failed to initialize Google Sign-In: ${e.localizedMessage}"
            )
        }
    }

    fun signInWithGoogle(launcher: ActivityResultLauncher<Intent>) {
        if (!::googleSignInClient.isInitialized) {
            authState = authState.copy(
                errorMessage = "Google Sign-In not initialized. Please restart the app."
            )
            return
        }
        
        authState = authState.copy(isLoading = true, errorMessage = null)
        Log.d(TAG, "Starting Google Sign-In")
        
        try {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error launching sign-in intent", e)
            authState = authState.copy(
                isLoading = false,
                errorMessage = "Failed to start sign-in: ${e.localizedMessage}"
            )
        }
    }

    fun handleSignInResult(data: Intent?) {
        authState = authState.copy(isLoading = true)
        Log.d(TAG, "Handling sign-in result")
        
        viewModelScope.launch {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "Google Sign-In successful: ${account.email}")
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e(TAG, "Google Sign-In failed with code: ${e.statusCode}", e)
                val errorMessage = when (e.statusCode) {
                    12501 -> "Sign-in was cancelled"
                    12502 -> "Network error occurred"
                    12500 -> "Sign-in configuration error"
                    else -> "Google sign-in failed (Code: ${e.statusCode})"
                }
                authState = authState.copy(
                    isLoading = false,
                    errorMessage = errorMessage
                )
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during sign-in", e)
                authState = authState.copy(
                    isLoading = false,
                    errorMessage = "Unexpected error: ${e.localizedMessage}"
                )
            }
        }
    }

    private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        try {
            Log.d(TAG, "Authenticating with Firebase")
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            
            Log.d(TAG, "Firebase authentication successful: ${authResult.user?.email}")
            
            // Initialize Firestore user data for new sign-in
            try {
                repository.initializeUserData()
                Log.d(TAG, "Firestore user data initialized successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing Firestore user data: ${e.message}")
                // Don't fail the sign-in process for this error
            }
            
            authState = authState.copy(
                isSignedIn = true,
                isLoading = false,
                user = authResult.user,
                errorMessage = null
            )
        } catch (e: Exception) {
            Log.e(TAG, "Firebase authentication failed", e)
            authState = authState.copy(
                isLoading = false,
                errorMessage = "Firebase authentication failed: ${e.localizedMessage}"
            )
        }
    }

    fun signOut() {
        Log.d(TAG, "Signing out")
        auth.signOut()
        if (::googleSignInClient.isInitialized) {
            googleSignInClient.signOut()
        }
        authState = authState.copy(
            isSignedIn = false,
            user = null,
            errorMessage = null
        )
    }

    fun clearError() {
        authState = authState.copy(errorMessage = null)
    }
}

// ViewModelFactory for AuthViewModel
class AuthViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 