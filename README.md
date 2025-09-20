# InteropShield

A Kotlin Compiler Plugin that can auto generate Java member getter/setter for kotlin extension property.

| Limitation                 | Is supported | Description/Addition info                                                                                                                  |
|----------------------------|--------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Annotation Class           | ❌            | Annotation class has no body                                                                                                               |
| Getter                     | ✅            | None                                                                                                                                       |
| Setter                     | ⚠️           | Not tested fully                                                                                                                           |
| Getter/Setter with generic | ⚠️           | The Type Erasure in JVM, all generic type will be erased to `Object` <br/> Please avoid using generic in extension property getter/setter. |

> The minimum Kotlin version supported by the plugin is `2.2.10`

# How to use

1. Add the maven repository of RTAST both `build.gradle.kts` and `settings.gradle.kts`

```kotlin
// build.gradle.kts
repositories {
    mavenCentral()
    maven("https://repo.maven.rtast.cn/releases")
}

// settings.gradle.kts
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.maven.rtast.cn/releases")
    }
}
```

2. Add the plugin to gradle

```kotlin
// build.gradle.kts
plugins {
    id("cn.rtast.interop") version "<plugin version>"
}
```

3. Configuring the plugin(Optional)

```kotlin
// build.gradle.kts

interopShield {
    // Whether to enable the plugin, by default, it's true
    enabled = true

    // Suppressing the warning, by default, it's false
    suppressWarning = false
}
```

# 中文文档

一个自动为Kotlin的拓展属性生成Java成员getter/setter的Kotlin编译器插件

| 限制                 | 是否支持 | 附加信息                                           |
|--------------------|------|------------------------------------------------|
| 注解类                | ❌    | 注解类内不允许有函数体                                    |
| Getter             | ✅    | ***无***                                        |
| Setter             | ⚠️   | 没有完全测试                                         |
| 带有泛型的Getter/Setter | ⚠️   | 编译时会将泛型类型擦除变为`Object`, 请避免使用带有泛型的getter/setter |

> 插件开始支持的最小Kotlin版本为 `2.2.10`

# 如何使用

1. 在 `build.gradle.kts` 和 `settings.gradle.kts` 中添加如下maven仓库配置

```kotlin
// build.gradle.kts
repositories {
    mavenCentral()
    maven("https://repo.maven.rtast.cn/releases")
}

// settings.gradle.kts
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.maven.rtast.cn/releases")
    }
}
```

2. 将插件添加到gradle内

```kotlin
// build.gradle.kts
plugins {
    id("cn.rtast.interop") version "<plugin version>"
}
```

3. 配置插件(可选)

```kotlin
// build.gradle.kts

interopShield {
    // 是否启用插件, 默认为true
    enabled = true

    // 是否抑制警告, 默认为false
    suppressWarning = false
}
```