package com.example.mekan.database
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity


import androidx.room.PrimaryKey

@Entity
data class Food(

    @ColumnInfo(name = "YemekAdi")
    var yemekAdi: String,

    @ColumnInfo(name = "YemekMalzeme")
    var malzeme: String,

    @ColumnInfo(name = "yapilis")
    var yapilis: String,

    @ColumnInfo(name = "gorsel")
    val gorsel: ByteArray

): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createByteArray() ?: ByteArray(0)
    ) {
        uid = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(yemekAdi)
        parcel.writeString(malzeme)
        parcel.writeString(yapilis)
        parcel.writeByteArray(gorsel)
        parcel.writeInt(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}
