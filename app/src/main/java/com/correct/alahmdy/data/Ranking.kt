package com.correct.alahmdy.data

import android.os.Parcel
import android.os.Parcelable

class Ranking(
    //val viewType: Int,
    val rank: Int,
    val name: String,
    val score: Int,
    val icon: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        //parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        //parcel.writeInt(viewType)
        parcel.writeInt(rank)
        parcel.writeString(name)
        parcel.writeInt(score)
        parcel.writeInt(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ranking> {
        override fun createFromParcel(parcel: Parcel): Ranking {
            return Ranking(parcel)
        }

        override fun newArray(size: Int): Array<Ranking?> {
            return arrayOfNulls(size)
        }
    }
}