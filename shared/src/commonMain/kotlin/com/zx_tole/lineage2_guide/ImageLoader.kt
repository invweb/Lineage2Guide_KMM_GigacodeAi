package com.zx_tole.lineage2_guide

import androidx.compose.runtime.Composable
import coil3.compose.AsyncImage

/**
 * Platform-specific ImageLoader for Coil
 * Returns a configured ImageLoader that can load images from the server
 */
@Composable
expect fun getAsyncImageLoader(): coil3.ImageLoader?
