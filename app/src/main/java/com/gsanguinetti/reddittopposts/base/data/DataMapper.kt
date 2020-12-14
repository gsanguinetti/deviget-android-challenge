package com.gsanguinetti.reddittopposts.base.data

interface DataMapper<ENTITY, DOMAIN_MODEL> {
    fun mapFromEntity(entity: ENTITY): DOMAIN_MODEL
}