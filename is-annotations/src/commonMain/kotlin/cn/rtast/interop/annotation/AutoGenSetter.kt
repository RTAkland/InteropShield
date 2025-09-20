/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/19/25
 */


package cn.rtast.interop.annotation

/**
 * Auto generate setter
 * @param functionName generated function name, by default the name is `set{originName.upperCase()}`
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoGenSetter(val functionName: String = "@")