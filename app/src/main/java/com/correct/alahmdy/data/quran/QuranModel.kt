package com.correct.alahmdy.data.quran

import android.os.Parcel
import android.os.Parcelable

data class QuranModel(
    val number: Int,
    val englishName: String,
    val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeString(englishName)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuranModel> {
        override fun createFromParcel(parcel: Parcel): QuranModel {
            return QuranModel(parcel)
        }

        override fun newArray(size: Int): Array<QuranModel?> {
            return arrayOfNulls(size)
        }
    }
}
