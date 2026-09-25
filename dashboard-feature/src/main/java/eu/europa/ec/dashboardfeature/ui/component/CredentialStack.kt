/*
 * Copyright (c) 2026 European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific language
 * governing permissions and limitations under the Licence.
 */

package eu.europa.ec.dashboardfeature.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.europa.ec.dashboardfeature.ui.documents.list.model.DocumentUi

/**
 * How far each card behind the front one peeks out above it, i.e. how much of a covered card's
 * header block (logo + title + category, see [CredentialCard]) stays visible. Measured against
 * [CredentialCard]'s 20dp padding plus its title/category text block, with a small margin.
 */
private val STACK_PEEK_HEIGHT = 72.dp

/**
 * The "collapsed" (recogida) credential layout: full-size [CredentialCard]s stacked on top of one
 * another like a deck, each offset down by [STACK_PEEK_HEIGHT] so only the header strip of every
 * card behind the front one stays visible. The frontmost (last) card keeps its full original size
 * and shows all of its content, matching the reference wallet screenshot.
 *
 * Every card — whether fully visible or only peeking — keeps the exact same [onItemClick]
 * navigation as the extended view: Compose only routes a tap to the topmost composable actually
 * drawn at that point, so a tap on a peeking card's visible strip naturally reaches that card's
 * own `clickable`, with no custom hit-testing needed.
 */
@Composable
fun CredentialStack(
    documents: List<DocumentUi>,
    category: String,
    onItemClick: (DocumentUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (documents.isEmpty()) return

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val cardHeight = maxWidth / CREDENTIAL_CARD_ASPECT_RATIO
        val stackHeight = cardHeight + STACK_PEEK_HEIGHT * (documents.size - 1)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(stackHeight)
        ) {
            documents.forEachIndexed { index, document ->
                CredentialCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = STACK_PEEK_HEIGHT * index),
                    item = document.uiData,
                    documentIdentifier = document.documentIdentifier,
                    category = category,
                    onClick = { onItemClick(document) },
                )
            }
        }
    }
}
