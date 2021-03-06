/* -*- Mode: Java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.autocomplete

import org.junit.Test
import org.junit.runner.RunWith
import org.mozilla.focus.widget.InlineAutocompleteEditText
import org.robolectric.RobolectricTestRunner

import java.util.Collections
import java.util.HashSet

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions

@RunWith(RobolectricTestRunner::class)
class UrlAutoCompleteFilterTest {
    @Test
    fun testAutocompletion() {
        val filter = UrlAutoCompleteFilter()

        val domains = HashSet<String>()
        Collections.addAll(domains, "mozilla.org", "google.com", "facebook.com")
        filter.onDomainsLoaded(domains)

        assertAutocompletion(filter, "m", "mozilla.org")
        assertAutocompletion(filter, "www", "www.mozilla.org")
        assertAutocompletion(filter, "www.face", "www.facebook.com")
        assertAutocompletion(filter, "MOZ", "MOZilla.org")
        assertAutocompletion(filter, "www.GOO", "www.GOOgle.com")
        assertAutocompletion(filter, "WWW.GOOGLE.", "WWW.GOOGLE.com")
        assertAutocompletion(filter, "www.facebook.com", "www.facebook.com")
        assertAutocompletion(filter, "facebook.com", "facebook.com")

        assertNoAutocompletion(filter, "wwww")
        assertNoAutocompletion(filter, "yahoo")
    }

    @Test
    fun testWithoutDomains() {
        val filter = UrlAutoCompleteFilter()

        assertNoAutocompletion(filter, "mozilla")
    }

    @Test
    fun testWithoutView() {
        val filter = UrlAutoCompleteFilter()
        filter.onFilter("mozilla", null)
    }

    private fun assertAutocompletion(filter: UrlAutoCompleteFilter, text: String, completion: String) {
        val view = mock(InlineAutocompleteEditText::class.java)
        filter.onFilter(text, view)

        verify(view).onAutocomplete(completion)
    }

    private fun assertNoAutocompletion(filter: UrlAutoCompleteFilter, text: String) {
        val view = mock(InlineAutocompleteEditText::class.java)
        filter.onFilter(text, view)

        verifyZeroInteractions(view)
    }
}
