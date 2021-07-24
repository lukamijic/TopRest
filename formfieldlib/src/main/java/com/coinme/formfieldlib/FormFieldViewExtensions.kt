package com.coinme.formfieldlib

import android.text.Editable
import android.text.TextWatcher

// Copied from androidx.core.widget.TextView

/**
 * Add an action which will be invoked before the text changed.
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun FormFieldView.doBeforeTextChanged(
    crossinline action: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit
) = addTextChangedListener(beforeTextChanged = action)

/**
 * Add an action which will be invoked when the text is changing.
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun FormFieldView.doOnTextChanged(
    crossinline action: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit
) = addTextChangedListener(onTextChanged = action)

/**
 * Add an action which will be invoked after the text changed.
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun FormFieldView.doAfterTextChanged(
    crossinline action: (text: Editable?) -> Unit
) = addTextChangedListener(afterTextChanged = action)

/**
 * Add a text changed listener to this TextView using the provided actions
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun FormFieldView.addTextChangedListener(
    crossinline beforeTextChanged: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTextChanged: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline afterTextChanged: (text: Editable?) -> Unit = {}
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(text, start, count, after)
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(text, start, before, count)
        }
    }

    addTextWatcher(textWatcher)

    return textWatcher
}
