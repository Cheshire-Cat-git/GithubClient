package com.example.githubclient.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("orgs/{org}/repos")
    suspend fun getOrgRepos(@Path("org") org: String): Response<List<RepoDto>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepoDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<RepoDto>
}