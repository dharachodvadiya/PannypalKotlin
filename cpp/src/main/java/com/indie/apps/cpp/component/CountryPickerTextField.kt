package com.indie.apps.cpp.component

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.coroutineScope

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryTextField(
    label: String = "",
    isError: Boolean = false,
    modifier: Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    expanded: Boolean = false,
    selectedCountry: Country? = null,
    defaultSelectedCountry: Country = countryList(LocalContext.current).first(),
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    onExpandedChange: () -> Unit,
) {
    val countryValue = "${defaultSelectedCountry.dial_code} ${defaultSelectedCountry.name}"

    OutlinedTextField(
        modifier = modifier
            .expandable(menuLabel = label, onExpandedChange = onExpandedChange),
        readOnly = true,
        isError = isError,
        label = { *//*Text(label)*//* },
        value = if (selectedCountry == null) countryValue else "${selectedCountry.dial_code} ${selectedCountry.name}",
        onValueChange = {},
        colors = colors,
        shape = shape,
        trailingIcon = {
            Icon(
                Icons.Filled.ArrowDropDown,
                null,
                Modifier.rotate(
                    if (expanded)
                        180f
                    else
                        0f
                )
            )
        }
    )
}*/

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.expandable(
    onExpandedChange: () -> Unit,
    menuLabel: String
) = pointerInput(Unit) {
    forEachGesture {
        coroutineScope {
            awaitPointerEventScope {
                var event: PointerEvent
                do {
                    event = awaitPointerEvent(PointerEventPass.Initial)
                } while (
                    !event.changes.all { it.changedToUp() }
                )
                onExpandedChange.invoke()
            }
        }
    }
}.semantics {
    contentDescription = menuLabel // this should be a localised string
    onClick {
        onExpandedChange()
        true
    }
}