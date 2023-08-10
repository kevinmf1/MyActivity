package com.alcorp.core.utils

import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.domain.model.Activity

@Suppress("unused", "unused")
object DataMapper {

//    fun mapResponsesToEntities(input: List<PlayerResponse>): List<PlayerEntity> {
//        val playerList = ArrayList<PlayerEntity>()
//        input.map {
//            val player = PlayerEntity(
//                idPlayer = it.id,
//                name = it.name,
//                club = it.club,
//                rate = it.rate,
//                position = it.position,
//                allGoalsUntilNow = it.allGoalUntilNow,
//                allAssistsUntilNow = it.allAssistUntilNow,
//                activePlayer = it.activePlayer,
//                description = it.description,
//                photo = it.photo,
//                isFavorite = false
//            )
//            playerList.add(player)
//        }
//        return playerList
//    }

    fun mapEntitiesToDomain(input: List<ActivityEntity>): List<Activity> =
        input.map {
            Activity(
                id = it.id,
                date = it.date,
                timeStart = it.timeStart,
                timeEnd = it.timeEnd,
                title = it.title,
                desc = it.desc,
                isDone = it.isDone
            )
        }

    fun mapDomainToEntity(input: Activity) = ActivityEntity(
        id = input.id,
        date = input.date,
        timeStart = input.timeStart,
        timeEnd = input.timeEnd,
        title = input.title,
        desc = input.desc,
        isDone = input.isDone
    )

//    fun mapDomainToResponse(input: Player): PlayerResponse =
//        PlayerResponse(
//            id = input.idPlayer,
//            name = input.name,
//            club = input.club,
//            rate = input.rate,
//            position = input.position,
//            allGoalUntilNow = input.allGoalsUntilNow,
//            allAssistUntilNow = input.allAssistsUntilNow,
//            activePlayer = input.activePlayer,
//            description = input.description,
//            photo = input.photo
//        )
}