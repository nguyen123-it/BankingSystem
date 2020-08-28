package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

// class that represents a database used to store cards
public class CardDataBase {

    private Connection connection;
    private  SQLiteDataSource dataSource;
    private PreparedStatement advancedStatementHandler;
    private Statement statementHandler;

    // constructor method that connects to the db file taken from arguments
    public CardDataBase(String dataBaseFile) {
        // connect to the db file
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:"+dataBaseFile);

        try{
            connection = dataSource.getConnection();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        // call method to create table
        initTable();
    }

    // create table if it does not exist.
    public void initTable(){
        try{
            statementHandler = connection.createStatement();
            String createTableCmd = "CREATE TABLE IF NOT EXISTS card"+"(\n" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT,\n " + // column id will have numbers that are automatically incremented
                    " number TEXT,\n " +
                    " pin TEXT,\n " +
                    " balance INTEGER DEFAULT 0\n"
                     +");";
            statementHandler.executeUpdate(createTableCmd);// executeUpdate to commit changes
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // insert values into the table card
    public void insertValue(String number,String pin) {
        try{
            advancedStatementHandler = connection.prepareStatement("INSERT INTO card VALUES (?,?,?,?)");
            advancedStatementHandler.setString(2,number);
            advancedStatementHandler.setString(3,pin);
            advancedStatementHandler.setInt(4,0);
            advancedStatementHandler.executeUpdate();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // display the table card
    public void printTable() {
        try {
            advancedStatementHandler = connection.prepareStatement("SELECT*FROM card");
            ResultSet resultSet = advancedStatementHandler.executeQuery();
            while (resultSet.next()){
                System.out.println("id "+resultSet.getInt("id"));
                System.out.println("number "+resultSet.getString("number"));
                System.out.println("pin "+resultSet.getString("pin"));
                System.out.println("balance "+resultSet.getInt("balance"));
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // update one card in a database. Add extra money to the card's balance
    public void updateCard(String cardNum,int extraMoney){
        try {
            int initBalance = getCardBalance(cardNum);
            statementHandler.executeUpdate("UPDATE card set balance ="+(initBalance+extraMoney)+" where number ="+cardNum);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // update 2 cards. The first account will transfer an amount of money to the second account.
    public void updateCard(String accNum1, String accNum2,int transferredMoney){
        try {
            if (accNum1.equals(accNum2)){
                System.out.println("You cannot transfer money to the same account!");
                return;
            }
            advancedStatementHandler = connection.prepareStatement("SELECT balance FROM card where number="+accNum1);
            int balanceAcc1 = 0;
            int balanceAcc2 = 0;
            ResultSet resultSet = advancedStatementHandler.executeQuery();
            if (resultSet.next()){
                balanceAcc1 = resultSet.getInt("balance");
            }
            advancedStatementHandler = connection.prepareStatement("SELECT balance FROM card where number="+accNum2);
            resultSet = advancedStatementHandler.executeQuery();
            if (resultSet.next()){
                balanceAcc2 = resultSet.getInt("balance");
            }
            int remainMoney = balanceAcc1-transferredMoney;
            if (remainMoney<0){
                System.out.println("Not enough money!");
                return;
            }
            statementHandler.executeUpdate("UPDATE card SET balance = "+(balanceAcc2+transferredMoney)+" where number="+accNum2);
            statementHandler.executeUpdate("UPDATE card SET balance = "+(remainMoney)+" where number="+accNum1);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // delete the table card. All records will be permanently removed
    public void dropTable() {
        try {
            advancedStatementHandler = connection.prepareStatement("DROP TABLE card");
            advancedStatementHandler.executeUpdate();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // very card using account number and pin number
    public boolean isVerifyCard(String accountNum, String pin){
        try {
            boolean verifiedAcc = false;
            String accCol = "";
            String pinCol = "";
            advancedStatementHandler = connection.prepareStatement("SELECT * FROM card WHERE number= "+accountNum+" and pin="+pin);
            ResultSet resultSet = advancedStatementHandler.executeQuery();
            while (resultSet.next()) {
                accCol = resultSet.getString("number");
                pinCol = resultSet.getString("pin");

            }
            return true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    // overloaded version of the above method
    public boolean isVerifyCard(String accountNum){
        try {
            boolean verifiedAcc = false;
            advancedStatementHandler = connection.prepareStatement("SELECT * FROM card where number="+accountNum);
            ResultSet resultSet = advancedStatementHandler.executeQuery();
            return resultSet.next();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    // delete one card from the database
    public void removeCard(String accountNum) {
        try{
            statementHandler.executeUpdate("DELETE FROM card where number= "+accountNum);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // print the balance of one account
    public void printCardBalance(String accountNum){
        try {
            advancedStatementHandler = connection.prepareStatement("SELECT balance FROM card where number="+accountNum);
            ResultSet resultSet = advancedStatementHandler.executeQuery();
            if (resultSet.next()){
                System.out.println("Balance: "+resultSet.getInt("balance"));
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // get balance of one account
    public int getCardBalance(String accountNum){
        try {
            advancedStatementHandler = connection.prepareStatement("SELECT balance FROM card where number="+accountNum);
            ResultSet resultSet = advancedStatementHandler.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("balance");
            }
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return 0;
    }
}
