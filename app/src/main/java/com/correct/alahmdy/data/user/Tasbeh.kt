package com.correct.alahmdy.data.user

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "tasbeh", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Tasbeh (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val user_id: Int,
    val tasbeh: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tasbeh
        if (id != other.id) return false
        if (user_id != other.id) return false
        if (tasbeh != other.tasbeh) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + user_id.hashCode()
        result = 31 * result + tasbeh.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(user_id)
        parcel.writeString(tasbeh)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tasbeh> {
        override fun createFromParcel(parcel: Parcel): Tasbeh {
            return Tasbeh(parcel)
        }

        override fun newArray(size: Int): Array<Tasbeh?> {
            return arrayOfNulls(size)
        }
    }
}