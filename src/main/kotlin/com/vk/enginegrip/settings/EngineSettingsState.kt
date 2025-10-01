package com.vk.enginegrip.settings

import com.intellij.database.settings.DataGridSettings.AutoTransposeMode
import com.intellij.database.settings.DataGridSettings.PagingDisplayMode
import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.annotations.OptionTag
import java.time.ZoneId

class EngineSettingsState : BaseState() {
    @get:OptionTag("enablePagingInInEditorResultsByDefault")
    var enablePagingInInEditorResultsByDefault: Boolean = false

    @get:OptionTag("detectTextInBinaryColumns")
    var detectTextInBinaryColumns: Boolean = false

    @get:OptionTag("isDetectUUIDInBinaryColumns")
    var isDetectUUIDInBinaryColumns: Boolean = false

    @get:OptionTag("addToSortViaAltClick")
    var addToSortViaAltClick: Boolean = false

    @get:OptionTag("auto-transpose-mode")
    var autoTransposeMode: AutoTransposeMode? = null

    @get:OptionTag("enableLocalFilterByDefault")
    var enableLocalFilterByDefault: Boolean = false

    @get:OptionTag("disableGridFloatingToolbar")
    var disableGridFloatingToolbar: Boolean = false

    @get:OptionTag("pagingDisplayMode")
    var pagingDisplayMode: PagingDisplayMode? = null

    @get:OptionTag("enableImmediateCompletionInGridCells")
    var enableImmediateCompletionInGridCells: Boolean = false

    @get:OptionTag("lob-length")
    var bytesLimitPerValue: Int = 0

    @get:OptionTag("filtersHistorySize")
    var filtersHistorySize: Int = 0

    @get:OptionTag("disabledAggregators")
    var disabledAggregators: List<String?>? = null

    @get:OptionTag("widgetAggregator")
    var widgetAggregator: String? = null

    @get:OptionTag("numberGroupingEnabled")
    var numberGroupingEnabled: Boolean = false

    @get:OptionTag("numberGroupingSeparator")
    var numberGroupingSeparator: Char = ' '

    @get:OptionTag("decimalSeparator")
    var decimalSeparator: Char = ' '

    @get:OptionTag("infinity")
    var infinity: String = "Infinity"

    @get:OptionTag("nan")
    var nan: String = "NaN"

    @get:OptionTag("effectiveNumberPattern")
    var effectiveNumberPattern: String? = null

    @get:OptionTag("effectiveDateTimePattern")
    var effectiveDateTimePattern: String? = null

    @get:OptionTag("effectiveZonedDateTimePattern")
    var effectiveZonedDateTimePattern: String? = null

    @get:OptionTag("effectiveTimePattern")
    var effectiveTimePattern: String? = null

    @get:OptionTag("effectiveZonedTimePattern")
    var effectiveZonedTimePattern: String? = null

    @get:OptionTag("effectiveDatePattern")
    var effectiveDatePattern: String? = null

    @get:OptionTag("effectiveZoneId")
    var effectiveZoneId: ZoneId? = null

    @get:OptionTag("pageSize")
    var pageSize: Int = 100

    @get:OptionTag("limit-page-size")
    var limitPageSize: Boolean = true
}
