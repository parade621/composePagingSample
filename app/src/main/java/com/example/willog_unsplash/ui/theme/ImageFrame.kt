package com.example.willog_unsplash.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.willog_unsplash.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ImageFrame(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(lightGray)
            .clickable(
                onClick = { /*TODO*/ }
            )
    ) {
        GlideImage(
            imageModel = { /*이미지 url*/ },
            modifier = Modifier.size(32.dp),
            previewPlaceholder = /*이미지가 없을 때*/ R.drawable.ic_launcher_foreground,
        )
    }
}

@Preview
@Composable
fun PreviewImageFrame() {
    ImageFrame()
}