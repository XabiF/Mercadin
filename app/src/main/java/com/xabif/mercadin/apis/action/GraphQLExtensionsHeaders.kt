package com.xabif.mercadin.apis.action

import com.google.gson.annotations.SerializedName

data class GraphQLExtensionsHeaders(
    @SerializedName("Accept-Language") val acceptLanguage: String,
)
