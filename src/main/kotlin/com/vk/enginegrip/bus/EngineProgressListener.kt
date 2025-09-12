package com.vk.enginegrip.bus

interface EngineProgressListener {

    // Таска началась -> Отправка запроса -> Обработка запроса -> Таска завершилась

    /**
     * Таска началась
     */
    fun taskStarted()

    /**
     * Отправка запроса
     */
    fun sendingRequest()

    /**
     * Обработка запроса
     */
    fun processingRequest()

    /**
     * Таска завершился
     */
    fun taskFinished()
}
