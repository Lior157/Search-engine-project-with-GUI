/**
 * This class is used to add objects to load the Documents
 */
public class QueryViewObject {


    private String Number;
    private String DocumentID;
    private String Rank;


    public QueryViewObject(String number, String documentID, String rank) {
        Number = number;
        DocumentID = documentID;
        Rank = rank;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }
}


