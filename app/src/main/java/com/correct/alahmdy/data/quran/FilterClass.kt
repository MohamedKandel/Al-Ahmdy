package com.correct.alahmdy.data.quran

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "filter", foreignKeys = [
        ForeignKey(
            entity = QuranClass::class,
            parentColumns = ["number"],
            childColumns = ["number"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class FilterClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val number: Int,
    val english: String,
    val arabic: String,
    val filterType: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilterClass

        if (id != other.id) return false
        if (number != other.number) return false
        if (english != other.english) return false
        if (arabic != other.arabic) return false
        if (filterType != other.filterType) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + english.hashCode()
        result = 31 * result + arabic.hashCode()
        result = 31 * result + filterType.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(number)
        parcel.writeString(english)
        parcel.writeString(arabic)
        parcel.writeInt(filterType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterClass> {
        override fun createFromParcel(parcel: Parcel): FilterClass {
            return FilterClass(parcel)
        }

        override fun newArray(size: Int): Array<FilterClass?> {
            return arrayOfNulls(size)
        }
    }
}