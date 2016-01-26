package io.github.mcvlaga.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface AppResources extends ClientBundle {
    interface Normalize extends CssResource {
    }

    interface Style extends CssResource {
        @ClassName("flex-table-border")
        String flex_table_border();

        @ClassName("error-label-red")
        String error_label_red();

        @ClassName("success-label-green")
        String success_label_green();


        @ClassName("flex-table-last-column-no-border")
        String flex_table_last_column_no_border();

        @ClassName("tools-panel-no-border")
        String tools_panel_no_border();

        @ClassName("inline")
        String inline();

        @ClassName("phone-number-margin")
        String phone_number_margin();

        @ClassName("big-font")
        String big_font();
    }

    @Source("css/normalize.gss")
    Normalize normalize();

    @Source("css/style.gss")
    Style style();
}
