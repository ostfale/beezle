import groovy.util.logging.Slf4j
import de.ostfale.beezle.boundary.BeezleUI

@Slf4j
class BeezleMain {

    static void main(String[] args) {
        log.trace("Start beezle....")
        new BeezleMain().with {
            new BeezleUI().startUI()
        }
    }
}
