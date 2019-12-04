package io.github.chrislo27.rhre3.editor.stage.theme

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Align
import io.github.chrislo27.rhre3.editor.Editor
import io.github.chrislo27.rhre3.editor.stage.EditorStage
import io.github.chrislo27.rhre3.screen.EditorScreen
import io.github.chrislo27.rhre3.theme.LoadedThemes
import io.github.chrislo27.rhre3.theme.Theme
import io.github.chrislo27.toolboks.registry.AssetRegistry
import io.github.chrislo27.toolboks.ui.*


class ThemeChooserStage(val editor: Editor, val palette: UIPalette, parent: EditorStage, camera: OrthographicCamera, pixelsWidth: Float, pixelsHeight: Float)
    : Stage<EditorScreen>(parent, camera, pixelsWidth, pixelsHeight) {

    private val preferences: Preferences
        get() = editor.main.preferences
    private val themeList: ThemeListStage<Theme>

    init {
        this.elements += ColourPane(this, this).apply {
            this.colour.set(Editor.TRANSLUCENT_BLACK)
            this.colour.a = 0.8f
        }
        
        themeList = object : ThemeListStage<Theme>(editor, palette, this@ThemeChooserStage, this@ThemeChooserStage.camera, 362f, 352f) {
            override val itemList: List<Theme> get() = LoadedThemes.themes
            
            override fun getItemName(item: Theme): String = item.name
            override fun isItemNameLocalizationKey(item: Theme): Boolean = item.nameIsLocalization
            override fun getItemBgColor(item: Theme): Color = item.background
            override fun getItemLineColor(item: Theme): Color = item.trackLine

            override fun onItemButtonSelected(leftClick: Boolean, realIndex: Int, buttonIndex: Int) {
                if (leftClick) {
                    LoadedThemes.index = realIndex
                    LoadedThemes.persistIndex(preferences)
                    editor.theme = LoadedThemes.currentTheme
                }
            }
        }.apply { 
            location.set(screenX = 0f, screenY = 0f, screenWidth = 0f, screenHeight = 0f,
                         pixelX = 20f, pixelY = 53f, pixelWidth = 362f, pixelHeight = 352f)
        }
        this.elements += themeList

        this.elements += TextLabel(palette, this, this).apply {
            this.location.set(screenX = 0f, screenWidth = 1f, screenY = 0.875f, screenHeight = 0.125f)

            this.textAlign = Align.center
            this.textWrapping = false
            this.isLocalizationKey = true
            this.text = "editor.themeChooser.title"
            this.location.set(screenWidth = 0.95f, screenX = 0.025f)
        }

        this.elements += Button(palette, this, this).apply {
            this.location.set(0.05f, 0.025f, 0.65f, 0.075f)
            this.addLabel(TextLabel(palette, this, this.stage).apply {
                this.isLocalizationKey = true
                this.text = "editor.themeEditor"
                this.textWrapping = true
                this.fontScaleMultiplier = 0.75f
                this.location.set(pixelWidth = -4f, pixelX = 2f)
            })
        }
    
        this.elements += object : Button<EditorScreen>(palette, this, this) {
            override fun onLeftClick(xPercent: Float, yPercent: Float) {
                super.onLeftClick(xPercent, yPercent)
            // FIXME
//                LoadedThemes.reloadThemes(preferences, false)
//                LoadedThemes.persistIndex(preferences)
//                editor.theme = LoadedThemes.currentTheme
//                buttonScroll = 0
//            
//                resetButtons()
            }
        }.apply {
            this.location.set(0.725f, 0.025f, 0.1f, 0.075f)
            this.tooltipTextIsLocalizationKey = true
            this.tooltipText = "editor.themeChooser.reset"
            this.addLabel(ImageLabel(palette, this, this.stage).apply {
                this.image = TextureRegion(AssetRegistry.get<Texture>("ui_icon_updatesfx"))
            })
        }

        this.elements += object : Button<EditorScreen>(palette, this, this) {
            override fun onLeftClick(xPercent: Float, yPercent: Float) {
                super.onLeftClick(xPercent, yPercent)

                Gdx.net.openURI("file:///${LoadedThemes.THEMES_FOLDER.file().absolutePath}")
            }
        }.apply {
            this.location.set(0.85f, 0.025f, 0.1f, 0.075f)
            this.addLabel(ImageLabel(palette, this, this.stage).apply {
                this.renderType = ImageLabel.ImageRendering.ASPECT_RATIO
                this.image = TextureRegion(AssetRegistry.get<Texture>("ui_icon_folder"))
            })
        }
        
    }
    
    fun resetEverything() {
        // TODO implement correctly
        themeList.resetButtons()
    }

    override fun scrolled(amount: Int): Boolean {
        if (this.isMouseOver()) {
            // FIXME
            themeList.scroll(amount)
        }

        return true
    }

}