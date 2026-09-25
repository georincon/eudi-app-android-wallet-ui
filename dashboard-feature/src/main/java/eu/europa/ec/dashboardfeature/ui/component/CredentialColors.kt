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

import androidx.compose.ui.graphics.Color
import eu.europa.ec.corelogic.model.DocumentIdentifier

/**
 * Fixed pastel background palette for credential cards, Lissi-style. Each distinct
 * [DocumentIdentifier.formatType] is deterministically hashed to one of these colors, so a
 * given credential *type* (e.g. "PID (MSO MDoc)" vs "PID (SD-JWT VC Compact)") always gets the
 * same color, and different types are very likely to get different colors, without hard-coding
 * a color per known type.
 *
 * Shared between the documents list ([eu.europa.ec.dashboardfeature.ui.documents.list.DocumentsScreen])
 * and the document details screen ([eu.europa.ec.dashboardfeature.ui.documents.detail.DocumentDetailsScreen])
 * so the same credential keeps the same color everywhere it is shown.
 */
private val CREDENTIAL_PASTEL_PALETTE: List<Color> = listOf(
    Color(0xFFC3D4FA), // blue
    Color(0xFFD6C8F5), // violet
    Color(0xFFC3E8CE), // green
    Color(0xFFF8D9AE), // amber
    Color(0xFFF5C7DA), // pink
    Color(0xFFC3EDED), // cyan
)

fun DocumentIdentifier.toCredentialPastelColor(): Color {
    val index = Math.floorMod(formatType.hashCode(), CREDENTIAL_PASTEL_PALETTE.size)
    return CREDENTIAL_PASTEL_PALETTE[index]
}

/**
 * Metallic gradient palette for the large credential cards in the documents list
 * ([eu.europa.ec.dashboardfeature.ui.component.CredentialCard]): each pair is (lighter, darker)
 * shade of the same hue family, for a diagonal light-to-dark background.
 *
 * The first three pairs (indigo, teal, wine) are colors sampled directly off the reference
 * wallet screenshot `colores.png` (its "Email"/indigo card, "Verifiable ID"/teal card, and
 * "Self-Attested"/wine card, in that order), per explicit request. [DocumentIdentifier.MdocPid]
 * and [DocumentIdentifier.SdJwtPid] — the two PID variants this wallet issues today — are mapped
 * directly to the indigo and teal pairs so they always match those exact reference colors. Any
 * other document type falls back to a deterministic hash over the remaining pairs (wine first,
 * then the richened amber/pink/cyan/green set kept close in saturation to the three reference
 * colors so all combinations read as one family), so a third+ credential type still gets a
 * stable, non-pastel, non-neon color without needing to hand-curate one for every possible type.
 */
private val CREDENTIAL_METALLIC_PALETTE: List<Pair<Color, Color>> = listOf(
    Color(0xFF4A4FC5) to Color(0xFF17173F), // 0: indigo — colores.png "Email" card
    Color(0xFF1D8A76) to Color(0xFF0A2620), // 1: teal — colores.png "Verifiable ID" card
    Color(0xFF8C2142) to Color(0xFF3A0F1D), // 2: wine — colores.png "Self-Attested" card
    Color(0xFF4FAE72) to Color(0xFF123D24), // 3: green
    Color(0xFFE8963D) to Color(0xFF6B3305), // 4: amber (richened, was too pastel)
    Color(0xFFD94E82) to Color(0xFF6B1236), // 5: pink (richened, was too pastel)
    Color(0xFF2FB8C4) to Color(0xFF0B4A50), // 6: cyan (deepened, was too neon)
)

private val OTHER_DOCUMENT_PALETTE: List<Pair<Color, Color>> =
    CREDENTIAL_METALLIC_PALETTE.subList(2, CREDENTIAL_METALLIC_PALETTE.size)

fun DocumentIdentifier.toCredentialMetallicGradient(): Pair<Color, Color> {
    return when (this) {
        is DocumentIdentifier.MdocPid -> CREDENTIAL_METALLIC_PALETTE[0]
        is DocumentIdentifier.SdJwtPid -> CREDENTIAL_METALLIC_PALETTE[1]
        else -> {
            val index = Math.floorMod(formatType.hashCode(), OTHER_DOCUMENT_PALETTE.size)
            OTHER_DOCUMENT_PALETTE[index]
        }
    }
}
