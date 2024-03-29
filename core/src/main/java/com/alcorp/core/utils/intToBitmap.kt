package com.alcorp.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun intToBitmap(context: Context, photoValue: Int): Bitmap? {
    return BitmapFactory.decodeResource(context.resources, photoValue)
}
