package com.example.colega.preferences

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.colega.UserProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer: Serializer<UserProto> {
    override val defaultValue: UserProto
        get() = UserProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProto {
        return try {
            UserProto.parseFrom(input)
        }catch (e: InvalidProtocolBufferException){
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: UserProto, output: OutputStream) = t.writeTo(output)
}