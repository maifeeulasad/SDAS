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





-ignorewarnings

# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}
-keepnames class com.fasterxml.jackson.** {
    *;
}
-dontwarn com.fasterxml.jackson.databind.**

-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** {
    *;
}
-keep interface com.squareup.okhttp3.** {
    *;
}
-dontwarn javax.annotation.**
-keep class androidx.core.app.CoreComponentFactory {
    *;
}

-keep class com.mua.mobileattendance.retrofit.dto** {
    *;
}
-keep class com.google.** {
    *;
}

-keepattributes Annotation -keepattributes Signature

-keepclassmembers enum * {
    *;
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}