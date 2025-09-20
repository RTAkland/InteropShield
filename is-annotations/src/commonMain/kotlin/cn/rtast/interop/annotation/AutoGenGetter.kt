/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/19/25
 */


package cn.rtast.interop.annotation

/**
 * Auto generate getter
 * @param functionName generated function name, by default the name is `get{originName.upperCase()}`
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoGenGetter(val functionName: String = "@")