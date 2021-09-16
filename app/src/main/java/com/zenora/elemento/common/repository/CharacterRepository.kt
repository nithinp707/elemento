package com.zenora.elemento.common.repository

import com.apollographql.apollo.api.Response
import com.zenora.elemento.CharactersListQuery

interface CharacterRepository {
    suspend fun queryCharactersList(): Response<CharactersListQuery.Data>
}