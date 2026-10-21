package com.example.githubclient.data

import com.google.gson.annotations.SerializedName

data class RepoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("owner") val owner: OwnerDto?,
    @SerializedName("fork") val fork: Boolean,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("open_issues_count") val openIssuesCount: Int,
    @SerializedName("parent") val parent: ParentRepoDto?
)

data class OwnerDto(
    @SerializedName("login") val login: String
)

data class ParentRepoDto(
    @SerializedName("full_name") val fullName: String?
)