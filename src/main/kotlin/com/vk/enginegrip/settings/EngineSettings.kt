package com.vk.enginegrip.settings

import com.intellij.database.settings.DataGridSettings
import com.intellij.database.settings.DataGridSettings.AutoTransposeMode
import com.intellij.database.settings.DataGridSettings.PagingDisplayMode
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.openapi.util.ModificationTracker
import com.intellij.util.xmlb.XmlSerializerUtil
import java.time.ZoneId
import java.util.concurrent.atomic.AtomicLong

@Service
@State(
    name = "EngineSettings",
    storages = [Storage("engineSettings.xml")],
    category = SettingsCategory.TOOLS
)
class EngineSettings : PersistentStateComponent<EngineSettingsState>, ModificationTracker, DataGridSettings {
    private val state = EngineSettingsState()

    private val myModificationCount = AtomicLong()

    override fun setEnablePagingInInEditorResultsByDefault(enablePagingInInEditorResultsByDefault: Boolean) {
        state.enablePagingInInEditorResultsByDefault = enablePagingInInEditorResultsByDefault
    }

    override fun isEnablePagingInInEditorResultsByDefault(): Boolean = state.enablePagingInInEditorResultsByDefault

    override fun isDetectTextInBinaryColumns(): Boolean = state.detectTextInBinaryColumns

    override fun isDetectUUIDInBinaryColumns(): Boolean = state.isDetectUUIDInBinaryColumns

    override fun setAddToSortViaAltClick(addToSortViaAltClick: Boolean) {
        state.addToSortViaAltClick = addToSortViaAltClick
    }

    override fun isAddToSortViaAltClick(): Boolean = state.addToSortViaAltClick

    override fun setAutoTransposeMode(autoTransposeMode: AutoTransposeMode) {
        state.autoTransposeMode = autoTransposeMode
    }

    override fun getAutoTransposeMode(): AutoTransposeMode {
        return state.autoTransposeMode!! // Maybe NPE
    }

    override fun setEnableLocalFilterByDefault(enableLocalFilterByDefault: Boolean) {
        state.enableLocalFilterByDefault = enableLocalFilterByDefault
    }

    override fun isEnableLocalFilterByDefault(): Boolean = state.enableLocalFilterByDefault

    override fun isDisableGridFloatingToolbar(): Boolean = state.disableGridFloatingToolbar

    override fun setDisableGridFloatingToolbar(disableGridFloatingToolbar: Boolean) {
        state.disableGridFloatingToolbar = disableGridFloatingToolbar
    }

    override fun getPagingDisplayMode(): PagingDisplayMode = state.pagingDisplayMode!!

    override fun setPagingDisplayMode(pagingDisplayMode: PagingDisplayMode) {
        state.pagingDisplayMode = pagingDisplayMode
    }

    override fun isEnableImmediateCompletionInGridCells(): Boolean = state.enableImmediateCompletionInGridCells

    override fun setEnableImmediateCompletionInGridCells(enableImmediateCompletionInGridCells: Boolean) {
        state.enableImmediateCompletionInGridCells = enableImmediateCompletionInGridCells
    }

    override fun setBytesLimitPerValue(bytesLimitPerValue: Int) {
        state.bytesLimitPerValue = bytesLimitPerValue
    }

    override fun getBytesLimitPerValue(): Int = state.bytesLimitPerValue

    override fun getFiltersHistorySize(): Int = state.filtersHistorySize

    override fun setDisabledAggregators(disabledAggregators: List<String?>) {
        state.disabledAggregators = disabledAggregators
    }

    override fun getDisabledAggregators(): List<String?> {
        return state.disabledAggregators!! // NPE ??
    }

    override fun setWidgetAggregator(widgetAggregator: String?) {
        state.widgetAggregator = widgetAggregator
    }

    override fun getWidgetAggregator(): String? {
        return state.widgetAggregator
    }

    override fun isNumberGroupingEnabled(): Boolean {
        return state.numberGroupingEnabled
    }

    override fun getNumberGroupingSeparator(): Char = state.numberGroupingSeparator

    override fun getDecimalSeparator(): Char = state.decimalSeparator

    override fun getInfinity(): String = state.infinity

    override fun getNan(): String = state.nan

    override fun getEffectiveNumberPattern(): String? = state.effectiveNumberPattern

    override fun getEffectiveDateTimePattern(): String? = state.effectiveDateTimePattern

    override fun getEffectiveZonedDateTimePattern(): String? = state.effectiveZonedDateTimePattern

    override fun getEffectiveTimePattern(): String? = state.effectiveTimePattern

    override fun getEffectiveZonedTimePattern(): String? = state.effectiveZonedTimePattern

    override fun getEffectiveDatePattern(): String? = state.effectiveDatePattern

    override fun getEffectiveZoneId(): ZoneId? = state.effectiveZoneId

    override fun fireChanged() {
        TODO("Not yet implemented")
    }

    override fun setPageSize(pageSize: Int) {
        state.pageSize = pageSize
    }

    override fun getPageSize(): Int = state.pageSize

    override fun isLimitPageSize(): Boolean = state.limitPageSize

    override fun getModificationTracker(): ModificationTracker {
        return this
    }

    override fun getModificationCount(): Long = myModificationCount.get()

    override fun getState(): EngineSettingsState {
        return state
    }

    override fun loadState(state: EngineSettingsState) {
        XmlSerializerUtil.copyBean(state, this.state);
    }


    companion object {
        fun getSettings(): EngineSettings {
            return ApplicationManager.getApplication().getService(EngineSettings::class.java)
        }
    }
}
