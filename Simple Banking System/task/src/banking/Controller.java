package banking;

import java.util.Scanner;

// controller class that handles the interaction between text UI and logic classes
public class Controller {

    private LuhnAlgoValidator luhnAlgoValidator;
    private Scanner scanner;
    private CardDataBase cardDataBase;
    private boolean running;

    // constructor that initializes objects of classes CardDataBase, LuhnAlgoValidator and Scanner
    public Controller(LuhnAlgoValidator luhnAlgoValidator, CardDataBase cardDataBase){
        this.luhnAlgoValidator = luhnAlgoValidator;
        this.cardDataBase = cardDataBase;
        scanner = new Scanner(System.in);
        running = true;
    }

    // start method to start the program
    public void start(){
        while (running){
            mainMenu();
            int choice = scanner.nextInt();
            if (choice == 0){
                System.out.println("Bye!");
                running = false;
            }
            else {
                command(choice);
            }
        }
    }

    // take different actions depends on the value of the variable choice
    public void command(int choice){
        if (choice == 1){ // create card and add it to the database
            Card card = new Card();
            card.createCard(luhnAlgoValidator);

            System.out.println("Your card have been created");
            System.out.println("Your card number:\n"+card.getAccountNum());
            System.out.println("Your card PIN:\n"+card.getPin());
            cardDataBase.insertValue(card.getAccountNum(),card.getPin());// add card into the db
            //cardDataBase.printTable();
        }
        else if (choice == 2){ // log into one account.
            System.out.println("Enter your card number: ");
            scanner.nextLine();
            String inputCardNum = scanner.nextLine();// enter account number
            System.out.println("Enter your PIN: ");
            String inputPin = scanner.nextLine(); // enter pin number
            if (cardDataBase.isVerifyCard(inputCardNum)) { // call this method to verify the card
                System.out.println("You have successfully logged in!");
                boolean isValid = true;
                while (isValid) { // if the card is valid, do something with it.
                    accountInfoMenu();
                    int option = scanner.nextInt();
                    if (option == 0) {
                        isValid = false;
                        running = false;
                    }
                    else if (option == 1) {// print balance of account
                       cardDataBase.printCardBalance(inputCardNum);
                    }
                    else if (option == 2){
                        System.out.println("Enter income:");
                        int extraMoney = scanner.nextInt();
                        cardDataBase.updateCard(inputCardNum,extraMoney);
                    }
                    else if (option  == 3){ // transfer money from 1 acc to the other
                        scanner.nextLine();
                        System.out.println("Transfer");
                        System.out.println("Enter card number:");
                        String accountReceiveMoney = scanner.nextLine();
                        System.out.println("Enter how much money you want to transfer:");
                        int transferredMoney = scanner.nextInt();
                        if ( !luhnAlgoValidator.validateCardNum(accountReceiveMoney.substring(0,accountReceiveMoney.length()-1)).equals(accountReceiveMoney)){
                            System.out.println("Probably you made a mistake in the card number.Please try again");
                        }
                        if (!cardDataBase.isVerifyCard(accountReceiveMoney)){
                            System.out.println("Such a card does not exist");
                        }
                        else{
                            cardDataBase.updateCard(inputCardNum,accountReceiveMoney,transferredMoney);
                        }
                    }
                    else if (option == 4){ // delete account from database
                        cardDataBase.removeCard(inputCardNum);
                    }
                    else if (option == 5){ // log out. return nothing
                        System.out.println("You have successfully logged out!");
                        return;
                    }
                }
            }
            else {
                System.out.println("Wrong card number or Pin");
            }
        }
    }

    private void mainMenu(){ // main menu for log in screen
        System.out.println("1. Create an account" +
                "\n2. Log into account" +
                "\n0. Exit");
    }

    private void accountInfoMenu(){ // main menu for account manipulation screen
        System.out.println("1. Balance" +
                "\n2. Add income"+
                "\n3. Do transfer"+
                "\n4. Close account"+
                "\n5. Log out" +
                "\n0. Exit");
    }

}
