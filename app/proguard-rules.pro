# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Google Play Services Authentication
-keep class com.google.android.gms.auth.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.tasks.** { *; }
-keep class com.google.android.gms.auth.api.signin.** { *; }
-keep class com.google.android.gms.auth.api.identity.** { *; }

# Firebase Authentication
-keep class com.google.firebase.auth.** { *; }
-keep class com.google.firebase.auth.internal.** { *; }
-keep class com.google.firebase.auth.api.** { *; }

# Google Drive API
-keep class com.google.api.services.drive.** { *; }
-keep class com.google.api.client.googleapis.json.** { *; }
-keep class com.google.api.client.googleapis.auth.** { *; }
-keep class com.google.api.client.auth.oauth2.** { *; }
-keep class com.google.api.client.json.** { *; }
-keep class com.google.api.client.util.** { *; }
-keep class com.google.api.client.http.** { *; }
-keep class com.google.api.client.googleapis.** { *; }
-keep class com.google.api.client.auth.** { *; }
-keep class com.google.api.client.googleapis.json.GoogleJsonError { *; }

# Google API Client
-keep class com.google.api.client.** { *; }
-keep class com.google.api.client.util.** { *; }
-keep class com.google.api.client.json.** { *; }
-keep class com.google.api.client.auth.** { *; }
-keepclassmembers class * {
    @com.google.api.client.util.Key <fields>;
}
-keepclassmembers class * {
    @com.google.api.client.util.Key <methods>;
}

# Google OAuth Client
-keep class com.google.oauth.client.** { *; }
-keep class com.google.oauth.client.auth.** { *; }
-keep class com.google.oauth.client.http.** { *; }
-keep class com.google.oauth.client.util.** { *; }

# Google Auth Library
-keep class com.google.auth.** { *; }
-keep class com.google.auth.oauth2.** { *; }
-keep class com.google.auth.http.** { *; }
-keep class com.google.auth.transport.** { *; }
-keep class com.google.auth.Credentials { *; }
-keep class com.google.auth.oauth2.** { *; }

# Accompanist Pager
-keep class com.google.accompanist.pager.** { *; }
-keep class com.google.accompanist.pager.indicators.** { *; }

-dontwarn com.sun.net.httpserver.Headers
-dontwarn com.sun.net.httpserver.HttpContext
-dontwarn com.sun.net.httpserver.HttpExchange
-dontwarn com.sun.net.httpserver.HttpHandler
-dontwarn com.sun.net.httpserver.HttpServer
-dontwarn java.awt.Desktop$Action
-dontwarn java.awt.Desktop

##---------------End: proguard configuration for Gson  ----------