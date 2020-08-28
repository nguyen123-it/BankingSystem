package banking;

public class LuhnAlgoValidator {// class that validates if the card is valid

    public String validateCardNum(String num){ // call other methods to generate a valid number
        int sumOfDigit = calSumEvenPlace(num)+calSumOddPlace(num);
        int lastDigit = getCheckSum(sumOfDigit);
        return num+""+lastDigit;
    }

    public int calSumOddPlace(String num){ // cal sum of digits of the number that have odd indexes
        String [] arrNum = num.split("");
        int oddSum = 0;

        for (int i = 15;i>=0;i--){
            if (i%2!=0){
                oddSum+=sumOfDigitNum(arrNum[i-1]);
            }
        }

        return oddSum;
    }

    public int sumOfDigitNum(String elementArr){ // convert numbers that have more than 2 digits into integer type
        int sumOfDigit = 0;
        int doubledNum = Integer.parseInt(elementArr)*2;

        if (String.valueOf(doubledNum).length() == 1){
            return doubledNum;
        }

        String doubledNumAsStr = String.valueOf(doubledNum);
        for (int i = 0; i<doubledNumAsStr.length();i++){
            String digitStr = String.valueOf(doubledNumAsStr.charAt(i));
            sumOfDigit+=Integer.parseInt(digitStr);
        }
        return sumOfDigit;
    }

    public int calSumEvenPlace(String num){ // cal sum of digits of the number that have even indexes
        String tempArr[] = num.split("");
        int evenSum = 0;

        for (int i = 15 ;i>=0;i--){
            if (i%2==0){
                if (i-1>=0) {
                    evenSum += Integer.parseInt(tempArr[i-1]);
                }
            }
        }

        return evenSum;
    }

    public int getCheckSum(int sumOfDigit){ // get the check sum and add it to the 15-digit number as the last digit
        int checkSum = 0;
        while ((sumOfDigit+checkSum)%10!=0){
            checkSum+=1;
        }
        return checkSum;
    }

}
