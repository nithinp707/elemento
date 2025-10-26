package com.zenora.elemento.common.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.zenora.elemento.CharactersListQuery

interface CharacterRepository {
    suspend fun queryCharactersList(): ApolloResponse<CharactersListQuery.Data>
}