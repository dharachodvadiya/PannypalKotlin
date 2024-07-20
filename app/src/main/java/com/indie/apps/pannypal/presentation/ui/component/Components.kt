package com.indie.apps.pannypal.presentation.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun PrimaryButton(
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
){
    Surface(
        onClick = onClick,
        modifier = modifier
            .semantics { role = Role.Button },
        shape = RoundedCornerShape(Util.RoundCornerButton),
        contentColor = MyAppTheme.colors.white
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(brush = LinearGradientsColor(MyAppTheme.colors.button_gradient_blue))
                .padding(
                    horizontal = Util.ButtonHorizontalPadding,
                    vertical = Util.ButtonVerticalPadding),
            content = content
        )
    }

}

@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PrimaryButtonPreviewDarkMode() {
    PannyPalTheme {
        PrimaryButton(
            onClick = {}
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "test")
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "test"
                )
            }
        }
    }
}

@Preview()
@Composable
private fun PrimaryButtonPreview() {
    PannyPalTheme {
        PrimaryButton(
            onClick = {}
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "test")
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "test"
                )
            }
        }
    }
}