package control

import javafx.scene.text.Font

class ResourceService {

    // font
    static Font FA14 = Font.loadFont(getClass().getResourceAsStream("/fontawesome-webfont.ttf"), 14)

    // font awesome icon aliases
    static final String ICON_EXIT = '\uf011'
}
