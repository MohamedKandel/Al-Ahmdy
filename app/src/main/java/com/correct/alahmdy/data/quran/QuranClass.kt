package com.correct.alahmdy.data.quran

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran")
class QuranClass(
    @PrimaryKey
    val number: Int,
    val english: String,
    val arabic: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuranClass

        if (number != other.number) return false
        if (english != other.english) return false
        if (arabic != other.arabic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + english.hashCode()
        result = 31 * result + arabic.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeString(english)
        parcel.writeString(arabic)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuranClass> {
        override fun createFromParcel(parcel: Parcel): QuranClass {
            return QuranClass(parcel)
        }

        override fun newArray(size: Int): Array<QuranClass?> {
            return arrayOfNulls(size)
        }
    }
}