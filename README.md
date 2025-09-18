# InteropShield

Auto generate Java member getter/setter for kotlin extension property.

| Limitation                                  | Is supported | Description/Addition info                                                      |
|---------------------------------------------|--------------|--------------------------------------------------------------------------------|
| Annotation Class                            | ❌            | Annotation class has no body                                                   |
| Setter with generic                         | ❌            | Not supported yet                                                              |
| Setter with generic and return generic type | ❌            | Not supported yet                                                              |
| Normal extension property setter            | ⚠️           | Not tested fully(Not support fully)                                            |
| Normal extension property getter            | ✅            | None                                                                           |
| Getter with generic                         | ✅            | None                                                                           |
| Getter with generic and return generic type | ✅            | None                                                                           |
| Interface class                             | ⚠️           | Generated functions will be marked as deprecated by kotlin compiler internally |

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

自动为Kotlin的拓展属性生成Java成员getter/setter

| 限制                   | 是否支持 | 附加信息                                   |
|----------------------|------|----------------------------------------|
| 注解类                  | ❌    | 注解类不可以有函数体                             |
| 带有泛型的Setter          | ❌    | 暂不支持                                   |
| 带有泛型的Setter并且返回类型为泛型 | ❌    | 暂不支持                                   |
| 常规拓展属性的setter        | ⚠️   | 没完全测试(不完全支持)                           |
| 常规拓展属性的getter        | ✅    | 无                                      |
| 带有泛型的常规getter        | ✅    | 无                                      |
| 带有泛型的getter并且返回类型为泛型 | ✅    | 推荐为泛型加上上界, 编译后Java使用者不用手动cast          |
| 接口类                  | ⚠️   | 生成后的函数会被Kotlin编译器内部自动加上`@Deprecated`注解 |

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