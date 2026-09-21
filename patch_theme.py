import re

with open("app/src/main/java/com/sixray/cepat/ui/compose/Theme.kt", "r") as f:
    content = f.read()

content = content.replace(
    "import androidx.compose.ui.Modifier\nimport androidx.compose.ui.graphics.Color",
    "import androidx.compose.ui.Modifier\nimport androidx.compose.ui.graphics.Brush\nimport androidx.compose.foundation.background\nimport androidx.compose.ui.graphics.Color"
)

content = content.replace(
    "background = Color(0xFFFFFFFF), // White\n    onBackground = Color(0xFF1C1B1F), // Near Black\n    surface = Color(0xFFFFFFFF), // White",
    "background = Color.Transparent, // Transparent\n    onBackground = Color(0xFF1C1B1F), // Near Black\n    surface = Color.Transparent, // Transparent"
)

content = content.replace(
    """    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }""",
    """    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            val backgroundModifier = if (!darkTheme) {
                Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF), Color(0xFF87CEEB))
                    )
                )
            } else {
                Modifier.background(colorScheme.background)
            }
            Box(modifier = Modifier.fillMaxSize().then(backgroundModifier)) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }"""
)

with open("app/src/main/java/com/sixray/cepat/ui/compose/Theme.kt", "w") as f:
    f.write(content)
