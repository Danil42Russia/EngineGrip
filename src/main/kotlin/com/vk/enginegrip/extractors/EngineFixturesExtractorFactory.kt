package com.vk.enginegrip.extractors

import com.intellij.database.extractors.DataExtractor
import com.intellij.database.extractors.DataExtractorFactory
import com.intellij.database.extractors.ExtractorConfig

class EngineFixturesExtractorFactory : DataExtractorFactory {
    override fun getName(): String {
        return "PAAS Export"
    }

    override fun supportsText(): Boolean {
        return true
    }

    override fun getFileExtension(): String {
        return "json"
    }

    override fun createExtractor(config: ExtractorConfig): DataExtractor {
        return EngineFixturesExtractor(config.objectFormatter)
    }
}