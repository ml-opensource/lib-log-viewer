package com.nodesagency.logviewer.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nodesagency.logviewer.utils.toBoolean
import com.nodesagency.logviewer.utils.toInt

@Entity(tableName = "Categories")
data class Category(
    /**
     * Generally you don't want to set the ID yourself, as it might have unforeseen consequences.
     */
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,

    val isPinned: Boolean = false

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString() ?: "",
        parcel.readInt().toBoolean()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeInt(isPinned.toInt())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}