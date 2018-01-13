package de.ostfale.beezle

import de.ostfale.beezle.boundary.BeezleUI
import groovy.util.logging.Slf4j

@Slf4j
public class BeezleMain {

    public static void main(String[] args) {
        log.trace("Start beezle....")
        new BeezleMain().with {
            new BeezleUI().startUI()
        }
    }
}
