package com.indie.apps.cpp.component


/*
@Composable
fun CountryPickerBottomSheet(
    title: @Composable () -> Unit,
    show: Boolean,
    onItemSelected: (country: Country) -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val countries = remember { countryList(context) }
    var mcountriesList = countries.toList()
    var selectedCountry by remember { mutableStateOf(countries[0]) }
    var searchValue by remember { mutableStateOf("") }

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    LaunchedEffect(key1 = show) {
        if (show) modalBottomSheetState.show()
        else modalBottomSheetState.hide()
    }

    LaunchedEffect(key1 = modalBottomSheetState.currentValue) {
        if (modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
            onDismissRequest()
        }
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            title()
            Column {
                searchValue = countrySearchView(modalBottomSheetState)
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        if (searchValue.isEmpty()) {
                            mcountriesList
                        } else {
                            mcountriesList.searchCountry(searchValue)
                        }
                    ) { mlist ->
                        var code = mlist.code.toLowerCase()
                        Row(modifier = Modifier
                            .clickable {
                                selectedCountry = mlist
                                onItemSelected(selectedCountry)
                            }
                            .padding(12.dp)) {
//                            Text(text = ("${localeToEmoji(mlist.code)}"))
//                            Text(text = ("${getFlags(it.code.toLowerCase())}"))
                            Image(
                                painterResource(getFlags(code)),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp)
                            )
                            Text(
                                text = "${mlist.name}",
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(2f)
                            )
//                            Text(
//                                text = "${getCode(code)}",
//                                modifier = Modifier
//                                    .padding(start = 8.dp)
//                            )
                        }
                        Divider(
                            color = Color.LightGray, thickness = 0.5.dp
                        )
                    }
                }
            }

        }
    ) {
        content()
    }
}*/
