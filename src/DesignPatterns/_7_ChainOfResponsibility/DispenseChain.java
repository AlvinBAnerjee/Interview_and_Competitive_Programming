package DesignPatterns._7_ChainOfResponsibility;

public interface DispenseChain {

    void setNextChain(DispenseChain nextChain);

    void dispense(Currency cur);
}
