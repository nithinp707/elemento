package com.zenora.elemento.common.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.zenora.elemento.CharactersListQuery
import com.zenora.elemento.common.network.BBDeployDemoAPI
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val webService: BBDeployDemoAPI
) : CharacterRepository {

    override suspend fun queryCharactersList(): ApolloResponse<CharactersListQuery.Data> {
        return webService.getApolloClient().query(CharactersListQuery()).execute()
    }

}