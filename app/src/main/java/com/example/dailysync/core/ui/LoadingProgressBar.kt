package com.example.dailysync.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.widget.ContentLoadingProgressBar
import com.example.dailysync.ui.theme.loadingBarColorList
import com.example.dailysync.ui.theme.loadingBarGreen
import com.example.dailysync.ui.theme.loadingBarPurple


@Composable
fun LoadingProgressBar(modifier: Modifier = Modifier) {
    val gradientBrush = Brush.sweepGradient(
        colors = loadingBarColorList
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawCircle(
                            brush = gradientBrush,
                            radius = size.minDimension / 2,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                            blendMode = BlendMode.SrcIn
                        )
                    }
                },
            strokeWidth = 4.dp,
            color = Color.Transparent
        )
    }
}