package com.sairajpatil108.dailyworkout.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
	primary = LightPurple,
	onPrimary = DarkBlack,
	primaryContainer = DarkPrimaryContainer,
	onPrimaryContainer = Color(0xFFDDCCFF),
	
	secondary = LimeGreen,
	onSecondary = DarkBlack,
	secondaryContainer = DarkSecondaryContainer,
	onSecondaryContainer = Color(0xFFE8F5B8),
	
	tertiary = Color(0xFFB3A0FF),
	onTertiary = DarkBlack,
	tertiaryContainer = DarkTertiaryContainer,
	onTertiaryContainer = Color(0xFFDDCCFF),
	
	background = DarkBackground,
	onBackground = DarkOnSurface,
	surface = DarkSurface,
	onSurface = DarkOnSurface,
	surfaceVariant = DarkSurfaceVariant,
	onSurfaceVariant = DarkOnSurfaceVariant,
	
	error = Color(0xFFFF8A8A),
	onError = DarkBlack,
	errorContainer = DarkErrorContainer,
	onErrorContainer = DarkErrorContent,
	
	outline = Color(0xFF6B7280),
	outlineVariant = Color(0xFF4A4A4A),
	scrim = Color(0x99000000),
	
	// Surface containers for better layering
	surfaceContainer = Color(0xFF252525),
	surfaceContainerHigh = Color(0xFF2F2F2F),
	surfaceContainerHighest = Color(0xFF3A3A3A)
)

private val LightColorScheme = lightColorScheme(
	primary = PrimaryPurple,
	onPrimary = PureWhite,
	primaryContainer = PrimaryContainer,
	onPrimaryContainer = PurpleVariant,
	
	secondary = LimeGreen,
	onSecondary = DarkBlack,
	secondaryContainer = SecondaryContainer,
	onSecondaryContainer = Color(0xFF2E3A00),
	
	tertiary = PurpleVariant,
	onTertiary = PureWhite,
	tertiaryContainer = AccentContainer,
	onTertiaryContainer = PrimaryPurple,
	
	background = SurfaceGray,
	onBackground = DarkBlack,
	surface = CardBackground,
	onSurface = DarkBlack,
	surfaceVariant = CardBackgroundSecondary,
	onSurfaceVariant = OnSurfaceGray,
	
	inverseSurface = DarkBlack,
	inverseOnSurface = SurfaceGray,
	inversePrimary = LightPurple,
	
	error = ErrorRed,
	onError = PureWhite,
	errorContainer = Color(0xFFFFEDEA),
	onErrorContainer = Color(0xFF5C1F1F),
	
	outline = OnSurfaceGray,
	outlineVariant = Color(0xFFD1D5DB),
	scrim = Color(0x99000000),
	
	// Additional surface colors for cards and components
	surfaceContainer = CardBackground,
	surfaceContainerHigh = CardBackgroundSecondary,
	surfaceContainerHighest = Color(0xFFF1F5F9)
)

@Composable
fun DailyWorkoutTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// Disable dynamic color to maintain consistent branding
	dynamicColor: Boolean = false,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}
		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = colorScheme.background.toArgb()
			// Light status bars for dark theme, dark status bars for light theme
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
		}
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}