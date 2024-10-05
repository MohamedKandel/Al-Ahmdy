package com.correct.alahmdy.data.quran

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "read", foreignKeys = [
    ForeignKey(
        entity = QuranClass::class,
        parentColumns = ["number"],
        childColumns = ["number"],
        onDelete = ForeignKey.CASCADE
    )
])
class Read (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val number: Int,
    val english: String,
    val arabic: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Read

        if (id != other.id) return false
        if (number != other.number) return false
        if (english != other.english) return false
        if (arabic != other.arabic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + english.hashCode()
        result = 31 * result + arabic.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        if (id != null) {
            parcel.writeInt(id)
        }
        parcel.writeInt(number)
        parcel.writeString(english)
        parcel.writeString(arabic)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Read> {
        override fun createFromParcel(parcel: Parcel): Read {
            return Read(parcel)
        }

        override fun newArray(size: Int): Array<Read?> {
            return arrayOfNulls(size)
        }
    }
}