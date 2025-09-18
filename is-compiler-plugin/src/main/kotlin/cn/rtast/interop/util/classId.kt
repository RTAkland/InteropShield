/*
 * Copyright © 2025 RTAkland
 * Author: RTAkland
 * Date: 9/19/25
 */


package cn.rtast.interop.util

import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

val String.classId get() = ClassId.topLevel(this.fqName)

val FqName.classId get() = ClassId.topLevel(this)