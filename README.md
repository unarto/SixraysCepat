# Sixray (AI Studio Port)

This project has been ported to compile and run within Google AI Studio as an Android Kotlin/Jetpack Compose application. The core logic from the original Sixray project has been preserved, and the required `libv2ray.aar` dependency has been fetched.

## Changes Made
- Moved the Android project root to the workspace root.
- Adjusted `agp` version in `libs.versions.toml`.
- Downloaded pre-compiled `libv2ray.aar` to `app/libs/`.
- Removed unnecessary CI and workflow files.

