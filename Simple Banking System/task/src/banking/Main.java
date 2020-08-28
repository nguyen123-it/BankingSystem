package banking;

public class Main {
    public static void main(String[] args) {
        CardDataBase cardDataBase = new CardDataBase(args[1]); // init database
        LuhnAlgoValidator luhnAlgoValidator = new LuhnAlgoValidator(); // init algo class

        Controller controller = new Controller(luhnAlgoValidator,cardDataBase); // pass them to controller as arguments
        controller.start();// start the program
    }

}
