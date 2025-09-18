/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/13/25
 */


package cn.rtast.interop.annotation

@RequiresOptIn(
    message = "仅供Java使用",
    level = RequiresOptIn.Level.ERROR
)
annotation class JavaOnly