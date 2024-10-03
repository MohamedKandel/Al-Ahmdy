package com.correct.alahmdy.data.user

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pray", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class PrayTime(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val user_id: Int,
    val date: Long,
    val prayEn: String,
    val prayAr: String,
    val pray_time: String,
    val pray_aa: String,
    var isMute: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrayTime

        if (id != other.id) return false
        if (user_id != other.user_id) return false
        if (prayEn != other.prayEn) return false
        if (prayAr != other.prayEn) return false
        if (date != other.date) return false
        if (pray_time != other.pray_time) return false
        if (pray_aa != other.pray_aa) return false
        if (isMute != other.isMute) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + user_id.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + prayEn.hashCode()
        result = 31 * result + prayAr.hashCode()
        result = 31 * result + pray_time.hashCode()
        result = 31 * result + pray_aa.hashCode()
        result = 31 * result + isMute.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(user_id)
        parcel.writeLong(date)
        parcel.writeString(prayEn)
        parcel.writeString(prayAr)
        parcel.writeString(pray_time)
        parcel.writeString(pray_aa)
        parcel.writeInt(isMute)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PrayTime> {
        override fun createFromParcel(parcel: Parcel): PrayTime {
            return PrayTime(parcel)
        }

        override fun newArray(size: Int): Array<PrayTime?> {
            return arrayOfNulls(size)
        }
    }
}