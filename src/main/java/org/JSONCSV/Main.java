package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Calculator calc = new Calculator(3, 5);


        System.out.println("EL resultado de la suma es:" + calc.suma());
    }
}
class Calculator{
    int num1 ;
    int num2 ;
    Calculator(int num1, int num2){

        this.num1 = num1;
    this.num2 = num2;
    }

    public int suma (){
        return num1 + num2;

    }


}