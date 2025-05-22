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

-keep @androidx.annotation.Keep public class *

#-----------------------------------------------------

##---------------Begin: proguard configuration for Retrofit, OkHttp, and Gson  ----------

# Retrofit 2.6.0: Preserve classes, interfaces, and HTTP-annotated methods
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# OkHttp 4.5.0 (including Logging Interceptor): Preserve classes
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**
-keep class okio.** { *; }
-dontwarn okio.**
-keep class okhttp3.logging.** { *; }
-dontwarn okhttp3.logging.**

# Gson (via Retrofit Converter 2.6.0): Preserve classes, annotations, and type information
-keep class com.google.gson.** { *; }
-keep class com.squareup.retrofit2.converter.gson.** { *; }
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Preserve your API model and service classes
-keep class com.indie.apps.pennypal.data.service.** { *; }
-keep interface com.indie.apps.pennypal.data.service.** { *; }
-keepclassmembers class com.indie.apps.pennypal.data.service.** { *; }

# Preserve enums for Gson serialization
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Suppress warnings for dependencies
-dontwarn sun.misc.**
-dontwarn java.lang.invoke.**
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn kotlin.**
-dontwarn kotlinx.**
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*



##---------------End: proguard configuration for Retrofit, OkHttp, and Gson  ----------

# Apache POI core
-keep class org.apache.poi.** { *; }
-dontwarn org.apache.poi.**

# Apache POI OOXML (for XSSFColor, XSSFWorkbook, etc.)
-keep class org.apache.poi.xssf.** { *; }
-dontwarn org.apache.poi.xssf.**

# XMLBeans (dependency of poi-ooxml)
-keep class org.apache.xmlbeans.** { *; }
-dontwarn org.apache.xmlbeans.**

# Commons Compress (dependency of poi-ooxml)
-keep class org.apache.commons.compress.** { *; }
-dontwarn org.apache.commons.compress.**

# POI annotations and reflection
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Prevent R8 from removing methods used via reflection
-keepclassmembers class org.apache.poi.** {
    *;
}
-keepclassmembers class org.apache.xmlbeans.** {
    *;
}

# Preserve static initializers and reflection
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class org.apache.poi.**,org.apache.xmlbeans.**,org.apache.logging.log4j.** {
    *;
}

# For XML parsing used by POI
-keep class com.sun.org.apache.xerces.** { *; }
-dontwarn com.sun.org.apache.xerces.**

-dontwarn java.awt.Shape
-dontwarn org.slf4j.impl.StaticLoggerBinder

-keep public class org.apache.poi.** {*;}

# Optimize
-optimizations !field/*,!class/merging/*,*
-mergeinterfacesaggressively

# will keep line numbers and file name obfuscation
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Apache POI
-dontwarn org.apache.**
-dontwarn org.openxmlformats.schemas.**
-dontwarn org.etsi.**
-dontwarn org.w3.**
-dontwarn com.microsoft.schemas.**
-dontwarn com.graphbuilder.**
-dontwarn javax.naming.**
-dontwarn java.lang.management.**
-dontwarn org.slf4j.impl.**
-dontwarn java.awt.**
-dontwarn net.sf.saxon.**
-dontwarn org.apache.batik.**
-dontwarn org.apache.logging.log4j.**

-dontnote org.apache.**
-dontnote org.openxmlformats.schemas.**
-dontnote org.etsi.**
-dontnote org.w3.**
-dontnote com.microsoft.schemas.**
-dontnote com.graphbuilder.**
-dontnote javax.naming.**
-dontnote java.lang.management.**
-dontnote org.slf4j.impl.**

-keeppackagenames org.apache.poi.ss.formula.function

-keep,allowoptimization,allowobfuscation class org.apache.logging.log4j.** { *; }
-keep,allowoptimization class org.apache.commons.compress.archivers.zip.** { *; }
-keep,allowoptimization class org.apache.poi.schemas.** { *; }
-keep,allowoptimization class org.apache.xmlbeans.** { *; }
-keep,allowoptimization class org.openxmlformats.schemas.** { *; }
-keep,allowoptimization class com.microsoft.schemas.** { *; }

#------------------pdf ----------------

# Keep all iText classes
-keep class com.itextpdf.** { *; }

# Keep all annotations
-keepattributes *Annotation*

# Keep classes with specific annotations (iText uses annotations for metadata and parsing)
-keepclassmembers class * {
    @com.itextpdf.** *;
}

# If you're using XML Worker (optional iText component), you may also need:
-keep class com.itextpdf.tool.xml.** { *; }

# Prevent iText from being obfuscated (for licensing/debugging compatibility)
-dontwarn com.itextpdf.**

-dontwarn com.facebook.infer.annotation.Nullsafe$Mode
-dontwarn com.facebook.infer.annotation.Nullsafe

