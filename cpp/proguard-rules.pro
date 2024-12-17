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
-keep class com.indie.apps.cpp.data.CountryDb { *; }
-keep class com.indie.apps.cpp.data.CountryDb$Companion { *; }

# Prevent removal of TypeToken constructor
-keepclassmembers class com.google.gson.reflect.TypeToken {
    <init>(...);
}

# Keep the Country class and its fields intact
-keep class com.indie.apps.cpp.data.model.Country { *; }
-keep class com.indie.apps.cpp.data.repository.CountryRepository { *; }
-keep class com.indie.apps.cpp.data.repository.CountryRepositoryImpl { *; }
-keep class com.indie.apps.cpp.data.utils.GetCountryFlagKt { *; }
-keep class com.indie.apps.cpp.data.utils.GetNumberHintKt { *; }
