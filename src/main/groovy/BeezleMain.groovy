import groovy.util.logging.Slf4j
import ui.BeezleUI

@Slf4j
class BeezleMain {

    static void main(String[] args) {
        log.trace("Start beezle....")
        new BeezleMain().with {
            new BeezleUI().startUI()
        }
    }
}
