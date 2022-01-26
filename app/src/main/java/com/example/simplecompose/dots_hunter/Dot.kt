package com.example.simplecompose.dots_hunter

import android.os.Parcelable
import androidx.compose.ui.geometry.Offset
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dot(
    val position: Offset,
    val vector: Offset
) : Parcelable {

    companion object {

//        fun Dot.next(
//            borders: Int,
//            durationMillis: Long,
//            dotRadius: Float,
//            speedCoefficient: Float
//        ): Dot {
//            val speed = vector * speedCoefficient
//
//            return Dot(
//                position = position + Offset(
//                    x = speed.x
//                )
//            )
//        }
    }
}