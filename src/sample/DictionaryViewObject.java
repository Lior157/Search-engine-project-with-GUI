package sample;

public class DictionaryViewObject {

    private String Term;
    private String Amount;

    public DictionaryViewObject(String term, String amount) {
        Term = term;
        Amount = amount;
    }

    public String getTerm() {
        return Term;
    }

    public String getAmount() {
        return Amount;
    }

    public void setTerm(String term) {
        Term = term;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
