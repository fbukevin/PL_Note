import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by veck on 15/7/14.
 */
public class First {
    public static void main(String[] args){
        System.out.println("Hello World");
        Logger logger = LoggerFactory.getLogger(First.class);
        logger.debug("Hello World");
    }
}
