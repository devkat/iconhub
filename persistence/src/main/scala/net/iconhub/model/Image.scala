package net.iconhub.model

import org.squeryl.annotations.Column

class Image (
    val mimeType: String,
    val width: Int,
    val height: Int,
    val content: Array[Byte],
    @Column("icon_id")
    val iconId: Long
    ) {
}

