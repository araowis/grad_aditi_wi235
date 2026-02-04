import service.*;
import context.*;
import factory.*;

public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify a mode:");
            System.out.println("-a (admin), -o (owner), -g (general)");
            return;
        }

        try {
            ServiceFactory factory = ApplicationContext.createServiceFactory();
            Mode mode = Mode.fromArg(args[0]);
            Service service = factory.createService(mode);
            service.run();

        }

        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
