package com.indie.apps.pannypal.presentation.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
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
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
        contentColor = MyAppTheme.colors.white
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(brush = LinearGradientsColor(MyAppTheme.colors.gradientBlue))
                .padding(
                    horizontal = dimensionResource(R.dimen.button_horizontal_padding),
                    vertical = dimensionResource(R.dimen.button_item_vertical_padding)
                ),
            content = content
        )
    }

}

@Composable
fun TopBarProfile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(
        onClick = onClick,
        modifier = modifier
            .semantics { role = Role.Button },
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
        contentColor = MyAppTheme.colors.gray1
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(dimensionResource(R.dimen.top_bar_profile))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        MyAppTheme.colors.gray1
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )
                .background(MyAppTheme.colors.white)
        ){
            Icon(
                Icons.Filled.Person,
                contentDescription = "Profile"
            )
        }


    }
}

@Composable
fun RoundImage(
    imageVector: ImageVector,
    tint: Color,
    backGround: Color,
    contentDescription: String,
    modifier: Modifier = Modifier.size(dimensionResource(id = R.dimen.item_image))
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = backGround,
                shape = CircleShape)
    ){
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
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

@Preview()
@Composable
private fun RoundImagePreview() {
    PannyPalTheme {
        RoundImage(
            imageVector = Icons.Filled.Person,
            tint = MyAppTheme.colors.white,
            backGround = MyAppTheme.colors.brandBg,
            contentDescription = "person"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopbarProfilePreview() {
    PannyPalTheme {
        TopBarProfile(onClick = {  })
    }
}

