package banking;

import java.util.Random;

// the class that represents cards.
public class Card {

    private String accountNum;
    private String pin;
    private int balance;

    // this algorithm class verifies the validity of cards using luhn algorithm
    private LuhnAlgoValidator luhnAlgoValidator;

    public Card(){
        accountNum = "";
        pin = "";
        balance = 0;
    }

    // method that set attributes of cards: accountNum, pin, balance
    public void createCard(LuhnAlgoValidator luhnAlgoValidator){
        // make use of algorithm class to generate account number
        this.luhnAlgoValidator = luhnAlgoValidator;
        accountNum = this.luhnAlgoValidator.validateCardNum(generateAccountNum());
        // generate pin
        pin = generatePin();

    }

    // generate pin using Boolean. This pin will contain 4 random digits.
    public String generatePin(){
        Random random = new Random();
        int randomFourDigs = random.nextInt(10);
        String stringFourDigit = "";

        for (int i = 0; i<4;i++){
            stringFourDigit+=""+randomFourDigs;
            randomFourDigs = random.nextInt(10);
        }

        return stringFourDigit;
    }

    /*method that generates the 15-digit account number.
       This account number will have the last digit as the
       16th digit after we passing it to the method validateCardNum
       of class LuhnAlgoValidator
     */
    public String generateAccountNum(){
        Random random = new Random();
        int randomAccountNum = random.nextInt(10);
        String stringTenDigit = "";

        for (int i = 0; i<9;i++){
            stringTenDigit+=""+randomAccountNum;
            randomAccountNum = random.nextInt(10);
        }

        return "400000"+stringTenDigit;
    }

    //getter classes to get attributes of card
    public String getAccountNum(){
        return accountNum;
    }

    public String getPin(){
        return pin;
    }

    public int getBalance(){
        return balance;
    }
}
