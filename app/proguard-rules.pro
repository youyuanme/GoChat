# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#表单验证
-keep class com.mobsandgeeks.saripaar.** {*;}
-keep @com.mobsandgeeks.saripaar.annotation.ValidateUsing class * {*;}

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
 #忽略警告
-ignorewarning
#############################记录生成的日志数据,gradle build时在本项目根目录输出##
#混淆前后的映射
-printmapping mapping.txt
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
###########################记录生成的日志数据，gradle build时 在本项目根目录输出-end######
#保护注解
#-keepattributes *Annotation*
 #保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
 }
#保持 Parcelable 不被混淆
 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆 如果混淆报错
#，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#}
#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#####混淆保护自己项目的部分代码以及引用的第三方jar包library#######
#-libraryjars libs/umeng-analytics-v5.2.4.jar
#三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
#-libraryjars libs/sdk-v1.0.0.jar
#-libraryjars libs/look-v1.0.1.jar
#如果不想混淆 keep 掉
-keep class com.google.android.gms.** {*; }
-keep class com.google.firebase..** {*; }
#保留一个完整的包
#-keep class com.lippi.recorder.utils.** {*; }
#友盟
-keep class com.umeng.**{*;}
#项目特殊处理代码
#移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-keep class com.mobsandgeeks.saripaar.** {*;}
-keep @com.mobsandgeeks.saripaar.annotation.ValidateUsing class * {*;}

# Glide库
-keepnames class com.mypackage.MyGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# Butter Knife混淆配置
# 虽然官方和GitHub上都介绍了如何使用Butter Knife，但是有一点没有提到，
# 那就是打包混淆的配置，昨天在打包的时候就遇到了无法打包这个问题，
# 原因就是没有加入ButterKnife的混淆：
  -dontwarn butterknife.internal.**
# 如果加上这一句任然不能解决你的问题，可以尝试下面的混淆代码：
#  -keep class butterknife.** { *; }
#  -dontwarn butterknife.internal.**
#  -keep class **$$ViewBinder { *; }
#  -keepclasseswithmembernames class * { @butterknife.* <fields>;}
#  -keepclasseswithmembernames class * { @butterknife.* <methods>;}

#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
#gson
#-libraryjars libs/gson-2.2.2.jar
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
