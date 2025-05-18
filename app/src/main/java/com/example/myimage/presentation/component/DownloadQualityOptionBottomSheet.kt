package com.example.myimage.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadQualityOptionBottomSheet(
    modifier: Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    isOpen: Boolean,
    onOptionClick: (option: DownloadOptions) -> Unit,
) {

    if (isOpen) ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {

        DownloadOptions.entries.forEach {
            Box(modifier = modifier
                .fillMaxWidth()
                .clickable { onOptionClick(it) }
                .padding(16.dp)
            ) {
                Text(text = it.label, style = MaterialTheme.typography.bodyLarge)
            }
        }

    }

}

enum class DownloadOptions(val label: String) {
    SMALL(label = "Download Small Size"),
    MEDIUM(label = "Download Medium Size"),
    LARGE(label = "Download Large Size")
}






































