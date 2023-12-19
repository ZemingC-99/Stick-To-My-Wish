package com.example.StickToMyWish.util

import androidx.activity.ComponentActivity
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.GridItemListener
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner


class DialogUtil {

    fun showBottomGridDialog(
        context: ComponentActivity,
        list: List<BasicGridItem>,
        listener: GridItemListener<BasicGridItem>
    ): MaterialDialog {
        return MaterialDialog(context, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            cornerRadius(literalDp = 20f)
            gridItems(items = list, selection = listener)
            lifecycleOwner(context)
        }
    }

}