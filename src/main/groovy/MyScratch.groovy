import com.jcabi.ssh.Shell
import com.jcabi.ssh.Ssh

class MyScratch {

    static void main(String[] args) {

        String key = new File('c:/Users/usauerbrei/.ssh/id_rsa').text

        Shell shell = new Ssh("fcu-hh-git", 22, "git", key, "mastermind")
        def result = new Shell.Plain(shell).exec("info")
        println result.split('R W').size()

    }

}