package com.vk.enginegrip.extractors;

import com.intellij.database.csv.CsvFormatsSettings;
import com.intellij.database.datagrid.CoreGrid;
import com.intellij.database.datagrid.GridColumn;
import com.intellij.database.datagrid.GridRow;
import com.intellij.database.extractors.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class EngineExtractorsHelper extends BaseExtractorsHelper {
    @SuppressWarnings("unused")
    public static final EngineExtractorsHelper INSTANCE = new EngineExtractorsHelper();

    @Override
    public boolean isApplicable(@Nullable CoreGrid<GridRow, GridColumn> grid) {
        return true;
//        return grid != null && grid.getDataHookup() instanceof EngineGridDataHookUp;
    }

    @Override
    public @NotNull DataExtractorFactory createScriptExtractorFactory(@NotNull String scriptFileName, @Nullable BiConsumer<String, Project> installPlugin) {
        return new EngineScript(scriptFileName, installPlugin);
    }

    @Override
    public @NotNull DataAggregatorFactory createScriptAggregatorFactory(@NotNull String scriptFileName, @Nullable BiConsumer<String, Project> installPlugin) {
        return new EngineScript(scriptFileName, installPlugin);
    }

    @Override
    public @NotNull List<DataExtractorFactory> getBuiltInFactories() {
        Stream<DataExtractorFactory> externalExtractors = ExtractorsHelper.EP.getExtensionList().stream()
                .filter(e -> !(e instanceof EngineExtractorsHelper))
                .flatMap(e -> e.getBuiltInFactories().stream());

        Stream<DataExtractorFactory> internalExtractors = Stream.of(new EngineFixturesExtractorFactory());

//        return Stream.concat(externalExtractors, internalExtractors).toList();
        return internalExtractors.toList();
    }

    @Override
    public DataExtractorFactory getDefaultExtractorFactory(@NotNull CoreGrid<GridRow, GridColumn> grid, @Nullable BiConsumer<String, Project> installPlugin, @Nullable CsvFormatsSettings settings) {
        return null;
    }

    @Override
    public @NotNull ExtractorConfig createExtractorConfig(@NotNull CoreGrid<GridRow, GridColumn> grid, @NotNull ObjectFormatter formatter) {
        return new BaseExtractorConfig(formatter, grid.getProject());
    }


    private static class EngineScript extends BaseExtractorsHelper.Script {
        public EngineScript(@NotNull String scriptFileName, @Nullable BiConsumer<String, Project> installPlugin) {
            super(scriptFileName, installPlugin);
        }

        @Override
        public @Nullable DataExtractor createExtractor(@NotNull ExtractorConfig config) {
            return super.createExtractor(config);
        }
    }
}
