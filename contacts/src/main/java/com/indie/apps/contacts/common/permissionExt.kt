package com.indie.apps.contacts.common

import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionStatus.isPermanentlyDenied(): Boolean{
    return !this.shouldShowRationale && !isGranted
}
