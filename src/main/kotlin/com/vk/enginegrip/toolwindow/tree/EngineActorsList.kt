package com.vk.enginegrip.toolwindow.tree

import com.vk.enginegrip.enigne.EngineActor
import com.vk.enginegrip.enigne.EngineActorConnection

object EngineActorsList {
    val LISTS: List<EngineActor> = listOf(
        EngineActor("confdata (prod)", EngineActorConnection("https://danilovchinnikov.dev.vk-apps.com", 14600)),
        EngineActor("confdata (local)", EngineActorConnection("http://localhost:8100", 100)),
        EngineActor("pmcx (prod)", EngineActorConnection("https://danilovchinnikov.dev.vk-apps.com", 14016)),
    )
}
