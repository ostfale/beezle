package de.ostfale.beezle.entity.repo

import de.ostfale.beezle.control.ResourceService

enum RepoStatus {

    NEW(ResourceService.ICON_FOLDER), LOADED(ResourceService.ICON_DOWNLOAD)

    def icon

    RepoStatus(def icon) {
        this.icon = icon
    }
}