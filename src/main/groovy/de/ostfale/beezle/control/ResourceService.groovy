package de.ostfale.beezle.control

import javafx.scene.text.Font

class ResourceService {

    // font
    static Font FA14 = Font.loadFont(getClass().getResourceAsStream("/fontawesome-webfont.ttf"), 14)
    static Font FA16 = Font.loadFont(getClass().getResourceAsStream("/fontawesome-webfont.ttf"), 16)

    // font awesome icon aliases
    static final String ICON_EXIT = '\uf011'
    static final String ICON_EYE = '\uf06e'
    static final String ICON_DOWNLOAD = '\uf019'
    static final String ICON_FILE = '\uf15b'
    static final String ICON_FOLDER = '\uf07b'
    static final String ICON_GITLAB = '\uf296'
    static final String ICON_INFO = '\uf129'
    static final String ICON_PENCIL = '\uf044'
    static final String ICON_REFRESH = '\uf021'
}
