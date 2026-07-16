package MachineCoding_LLD.DesignPatterns._07_ChainOfResponsibility;

public interface DispenseChain {

    void setNextChain(DispenseChain nextChain);

    void dispense(Currency cur);
}
